package ru.job4j.shortcut.service.impl;

import org.hashids.Hashids;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.ConvertLinkResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterSiteResponseDTO;
import ru.job4j.shortcut.entity.LinkEntity;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.SiteEntity;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.PersonRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @ExtendWith(MockitoExtension.class) + @Mock - создают МОК для unit-тестов,
 *      управляемый JUnit и Mockito (но он не внедряется в Spring-контекст)
 */
@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

    @Mock
    PersonRepository personRepository;

    @Mock
    SiteRepository siteRepository;

    @Mock
    LinkRepository linkRepository;

    @InjectMocks
    SiteServiceImpl siteService;

    @Test
    void whenRegistreSiteThenGetSuccess() {
        String urlSiteRequest = "https://github.com/";
        String domainName = "github.com";

        PersonEntity person = PersonEntity.builder().id(7).username("person1")
                .password("password1").build();
        SiteEntity site = SiteEntity.builder().domainName(domainName)
                .person(person).build();

        when(personRepository.findByUsername(any(String.class))).thenReturn(Optional.of(person));
        when(siteRepository.existsByDomainNameAndPersonId(any(String.class), anyInt())).thenReturn(false);

        RegisterSiteResponseDTO expectedDto = new RegisterSiteResponseDTO(true,
                site.getDomainName(), person.getUsername());

        assertEquals(expectedDto, siteService.registerSite(
                new UrlRequestDTO(urlSiteRequest), person.getUsername()));
        verify(siteRepository, times(1)).save(any(SiteEntity.class));
    }

    @Test
    void whenConvertLinkThenGetSuccess() {
        Hashids hashids = new Hashids("some salt", 7);
        String domainName = "github.com";
        int id = 7;
        String urlLinkRequest = "https://github.com/MikhailChrn/job4j_url_shortcut/commits/main/";
        String codeResponse = hashids.encode(id);

        PersonEntity person = PersonEntity.builder().id(11).username("person1")
                .password("password1").build();
        SiteEntity site = SiteEntity.builder().domainName(domainName)
                .person(person).build();

        when(siteRepository.existsByDomainNameAndPersonId(
                any(String.class), any(Integer.class))).thenReturn(true);
        when(linkRepository.existsByOriginalUrl(any(String.class))).thenReturn(false);
        when(personRepository.findByUsername(any(String.class))).thenReturn(Optional.of(person));
        when(siteRepository.findByDomainName(any(String.class))).thenReturn(Optional.of(site));
        when(linkRepository.save(any(LinkEntity.class)))
                .thenAnswer(invocation -> {
                    LinkEntity link = invocation.getArgument(0);
                    link.setId(id);
                    return link;
                });

        ConvertLinkResponseDTO expectedDto = new ConvertLinkResponseDTO(true,
                codeResponse, person.getUsername());

        ConvertLinkResponseDTO actualResponse = siteService.convertLink(
                new UrlRequestDTO(urlLinkRequest), person.getUsername());

        assertEquals(expectedDto, actualResponse);
        verify(linkRepository, times(2)).save(any(LinkEntity.class));
    }

    @Test
    public void whenConvertLinkButLinkAlreadyExistsThenGetFalse() {
        String requestUrl = "http://example.com/page";

        UrlRequestDTO requestDTO = new UrlRequestDTO();
        requestDTO.setUrl(requestUrl);

        PersonEntity person = PersonEntity.builder().username("testuser").id(11).build();

        when(personRepository.findByUsername(any(String.class)))
                .thenReturn(Optional.of(person));
        when(siteRepository.existsByDomainNameAndPersonId("example.com", person.getId()))
                .thenReturn(true);
        when(linkRepository.existsByOriginalUrl(requestUrl)).thenReturn(true);

        LinkEntity existingLink = LinkEntity.builder().originalUrl(requestUrl).code("abc123").build();
        when(linkRepository.findByOriginalUrl(requestUrl)).thenReturn(Optional.of(existingLink));

        ConvertLinkResponseDTO expectedDto = new ConvertLinkResponseDTO(false,
                existingLink.getCode(), person.getUsername());
        ConvertLinkResponseDTO actualResponse = siteService.convertLink(
                new UrlRequestDTO(requestUrl), person.getUsername());

        assertEquals(expectedDto, actualResponse);
        verify(linkRepository, never()).save(any(LinkEntity.class));
    }

    @Test
    public void whenConvertLinkAndSiteUnauthorizedThenGetFalse() {
        String requestUrl = "http://notallowed.com/page";

        PersonEntity person = PersonEntity.builder().username("testuser").id(11).build();

        when(personRepository.findByUsername(person.getUsername()))
                .thenReturn(Optional.of(person));
        when(siteRepository.existsByDomainNameAndPersonId("notallowed.com", person.getId()))
                .thenReturn(false);

        ConvertLinkResponseDTO expectedDto = new ConvertLinkResponseDTO(false,
                null, null);
        ConvertLinkResponseDTO actualResponse = siteService.convertLink(
                new UrlRequestDTO(requestUrl), person.getUsername());

        assertEquals(expectedDto, actualResponse);
        verify(linkRepository, never()).save(any(LinkEntity.class));
    }
}