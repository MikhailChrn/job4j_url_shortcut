package ru.job4j.shortcut.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.job4j.shortcut.service.SiteService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SiteMapper siteMapper;

    private final Hashids hashids = new Hashids("some salt", 7);

    /**
     * Метод добавляет в базу доменное имя для сайта
     * Флаг 'status' указывает, что регистрация выполнена или нет,
     * false - если сайт уже есть в системе.
     */
    @Override
    @Transactional
    public RegisterSiteResponseDTO registerSite(UrlRequestDTO urlRequestDTO, String username) {
        String domainName = getDomainFromURL(urlRequestDTO.getUrl());

        if (siteRepository.existsByDomainName(domainName)) {
            SiteEntity site = siteRepository.findByDomainName(domainName).get();
            return new RegisterSiteResponseDTO(false,
                    site.getDomainName(),
                    site.getPerson().getUsername());
        }

        PersonEntity person = personRepository.findByUsername(username).get();

        SiteEntity site = SiteEntity.builder()
                .domainName(getDomainFromURL(urlRequestDTO.getUrl()))
                .person(person)
                .total(0)
                .build();
        siteRepository.save(site);

        return new RegisterSiteResponseDTO(true,
                site.getDomainName(),
                site.getPerson().getUsername());
    }

    /**
     * Метод добавляет в базу ссылку на конечную страницу с привязкой к пользователю.
     * Флаг 'status' указывает, что регистрация выполнена,
     * false - если ссылка уже есть в системе, или отсутствует соответствующее доменное имя
     * (Выполняются проверки:
     * - наличие 'site';
     * - принадлежность 'site';
     * - отсутствие 'link';
     */
    @Override
    @Transactional
    public ConvertLinkResponseDTO convertLink(UrlRequestDTO urlRequestDTO, String username) {

        if (!siteRepository.existsByDomainName(
                getDomainFromURL(urlRequestDTO.getUrl()))) {
            return new ConvertLinkResponseDTO(false, null, null);
        }

        SiteEntity site = siteRepository.findByDomainName(getDomainFromURL(urlRequestDTO.getUrl())).get();

        if (!site.getPerson().getUsername().equals(username)) {
            return new ConvertLinkResponseDTO(false, null, null);
        }

        if (linkRepository.existsByOriginalUrl(urlRequestDTO.getUrl())) {
            LinkEntity link = linkRepository.findByOriginalUrl(urlRequestDTO.getUrl()).get();
            return new ConvertLinkResponseDTO(false,
                    link.getCode(), username);
        }

        LinkEntity link = LinkEntity.builder()
                .originalUrl(urlRequestDTO.getUrl())
                .site(site)
                .total(0).build();
        linkRepository.save(link);

        String code = getAssociatedCodeFromId(link.getId());
        link.setCode(code);

        linkRepository.save(link);

        return new ConvertLinkResponseDTO(true,
                link.getCode(), username);
    }

    /**
     * Метод извлекает из базы требуемую ссылку,
     * инкрементирует счётчики для 'link' и 'site'
     */
    @Override
    @Transactional
    public Optional<String> getOriginalLink(String code) {
        Optional<LinkEntity> linkOptional = linkRepository.findByCode(code);
        if (linkOptional.isPresent()) {
            int countLink = linkOptional.get().getTotal() + 1;
            SiteEntity site = linkOptional.get().getSite();
            int countSite = linkOptional.get().getSite().getTotal() + 1;
            linkOptional.get().setTotal(countLink);
            site.setTotal(countSite);
            linkRepository.save(linkOptional.get());
            siteRepository.save(site);
        }

        return linkOptional.isEmpty()
                ? Optional.empty()
                : Optional.of(linkOptional.get().getOriginalUrl());
    }

    /**
     * Метод используется для пользователей 'ROLE_USER',
     * возвращает статистику запросов по всем принадлежащим пользователю 'site'
     */
    @Override
    @Transactional
    public StatisticResponseDto getStatisticByUsername(String username) {
        Optional<PersonEntity> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isEmpty()) {
            return new StatisticResponseDto();
        }

        List<SiteDto> sites = new ArrayList<>();
        siteRepository.findAllByPersonId(optionalPerson.get().getId()).stream()
                .map(siteMapper::getSiteDtoFromEntity)
                .forEach(sites::add);

        return new StatisticResponseDto(sites);
    }

    /**
     * Метод используется для пользователей 'ROLE_ADMIN',
     * возвращает статистику запросов по всем 'site'
     */
    @Override
    @Transactional
    public StatisticResponseDto getAllStatistic() {
        List<SiteDto> sites = new ArrayList<>();

        siteRepository.findAll().stream()
                .map(siteMapper::getSiteDtoFromEntity)
                .forEach(sites::add);

        return new StatisticResponseDto(sites);
    }

    /**
     * Получаем доменное имя из ссылки с помощью пакета 'java.net.URL'
     */
    private static String getDomainFromURL(String urlString) {
        String domainString = "";
        try {
            URL url = new URL(urlString);
            domainString = url.getHost();
        } catch (MalformedURLException e) {
            log.error("Некорректный URL: {}", e.getMessage());
        }

        return domainString;
    }

    /**
     * Вычисляем шифр с помощью библиотеки 'org.hashids'
     */
    private String getAssociatedCodeFromId(Integer id) {

        return hashids.encode(id);
    }
}
