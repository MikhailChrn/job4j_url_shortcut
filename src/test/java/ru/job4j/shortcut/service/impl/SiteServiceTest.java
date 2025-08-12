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
import ru.job4j.shortcut.dto.response.StatisticResponseDto;
import ru.job4j.shortcut.dto.response.inner.SiteDto;
import ru.job4j.shortcut.entity.LinkEntity;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.SiteEntity;
import ru.job4j.shortcut.mapper.SiteMapper;
import ru.job4j.shortcut.repository.LinkRepository;
import ru.job4j.shortcut.repository.PersonRepository;
import ru.job4j.shortcut.repository.SiteRepository;

import java.util.List;
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

    @Mock
    SiteMapper siteMapper;

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
        when(siteRepository.existsByDomainName(any(String.class))).thenReturn(false);

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

        when(siteRepository.existsByDomainName(
                any(String.class))).thenReturn(true);
        when(linkRepository.existsByOriginalUrl(any(String.class))).thenReturn(false);
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
    void whenConvertLinkButLinkAlreadyExistsThenGetFalse() {
        String requestUrl = "http://example.com/page";
        String domainName = "example.com";
        String username = "testuser";

        UrlRequestDTO requestDTO = new UrlRequestDTO();
        requestDTO.setUrl(requestUrl);

        PersonEntity person = PersonEntity.builder().username(username).id(11).build();
        SiteEntity site = SiteEntity.builder().domainName(domainName).person(person).build();

        when(siteRepository.existsByDomainName(domainName))
                .thenReturn(true);
        when(siteRepository.findByDomainName(anyString())).thenReturn(Optional.ofNullable(site));
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
    void whenConvertLinkAndSiteUnauthorizedThenGetFalse() {
        String requestUrl = "http://notallowed.com/page";
        String username = "testuser";

        when(siteRepository.existsByDomainName("notallowed.com"))
                .thenReturn(false);

        ConvertLinkResponseDTO expectedDto = new ConvertLinkResponseDTO(false,
                null, null);
        ConvertLinkResponseDTO actualResponse = siteService.convertLink(
                new UrlRequestDTO(requestUrl), username);

        assertEquals(expectedDto, actualResponse);
        verify(linkRepository, never()).save(any(LinkEntity.class));
    }

    @Test
    void whenGetOriginalLinkByCodeThenIncrementCounterByOne() {
        String requestCode = "abc123";
        String responseUrl = "http://example.com/page";
        PersonEntity person = PersonEntity.builder().username("testuser").build();
        SiteEntity site = SiteEntity.builder()
                .domainName("example.com")
                .person(person)
                .total(3).build();
        LinkEntity link = LinkEntity.builder().
                code(requestCode)
                .originalUrl(responseUrl)
                .site(site)
                .total(5).build();

        when(linkRepository.findByCode(requestCode)).thenReturn(Optional.of(link));

        Optional<String> result = siteService.getOriginalLink(requestCode);

        assertEquals(responseUrl, result.get());
        assertEquals(6, link.getTotal());
        assertEquals(4, site.getTotal());

        verify(linkRepository).save(link);
        verify(siteRepository).save(site);
    }

    @Test
    void getStatisticByUsernameWhenUserExistsWithSitesThenReturnResponseWithSites() {
        String username = "userWithSites";
        PersonEntity person = PersonEntity.builder().id(11).username(username).build();

        SiteEntity siteEntity1 = SiteEntity.builder()
                .domainName("github.com")
                .person(person)
                .total(5).build();
        SiteEntity siteEntity2 = SiteEntity.builder()
                .domainName("gitverse.ru")
                .person(person)
                .total(7).build();
        List<SiteEntity> siteEntities = List.of(siteEntity1, siteEntity2);

        SiteDto siteDto1 = new SiteDto();
        SiteDto siteDto2 = new SiteDto();

        when(personRepository.findByUsername(any(String.class))).thenReturn(Optional.of(person));
        when(siteRepository.findAllByPersonId(anyInt())).thenReturn(siteEntities);
        when(siteMapper.getSiteDtoFromEntity(siteEntity1)).thenReturn(siteDto1);
        when(siteMapper.getSiteDtoFromEntity(siteEntity2)).thenReturn(siteDto2);

        StatisticResponseDto result = siteService.getStatisticByUsername(username);

        assertNotNull(result);
        assertNotNull(result.getSites());
        assertEquals(2, result.getSites().size());
        assertTrue(result.getSites().containsAll(List.of(siteDto1, siteDto2)));

        verify(personRepository).findByUsername(username);
        verify(siteRepository).findAllByPersonId(person.getId());
        verify(siteMapper).getSiteDtoFromEntity(siteEntity1);
        verify(siteMapper).getSiteDtoFromEntity(siteEntity2);
    }
}