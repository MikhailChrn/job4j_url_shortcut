package ru.job4j.shortcut.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.job4j.shortcut.entity.LinkEntity;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.SiteEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LinkSiteRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private LinkRepository linkRepository;

    @BeforeEach
    public void deleteAllBefore() {
        linkRepository.deleteAll();
        siteRepository.deleteAll();
        personRepository.deleteAll();
    }

    @AfterAll
    public void deleteAllAfterAll() {
        linkRepository.deleteAll();
        siteRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<LinkEntity> optionalLink = linkRepository.findById(1);

        assertThat(optionalLink).isEmpty();
    }

    @Test
    public void whenSaveOneUserTwoSiteAndFourLinkThenGetAllEntities() {
        PersonEntity person = PersonEntity.builder()
                .username("username1")
                .password("password1").build();
        personRepository.save(person);

        SiteEntity site1 = SiteEntity.builder()
                .domainName("job4j.ru")
                .person(person).build();
        SiteEntity site2 = SiteEntity.builder()
                .domainName("github.com")
                .person(person).build();
        List.of(site2, site1).forEach(siteRepository::save);

        LinkEntity link1 = LinkEntity.builder()
                .originalUrl("job4j.ru/unit1")
                .site(site1).build();
        LinkEntity link2 = LinkEntity.builder()
                .originalUrl("job4j.ru/unit2")
                .site(site1).build();
        LinkEntity link3 = LinkEntity.builder()
                .originalUrl("github.com/repo1")
                .site(site2).build();
        LinkEntity link4 = LinkEntity.builder()
                .originalUrl("github.com/repo2")
                .site(site2).build();
        List.of(link3, link1, link4, link2).forEach(linkRepository::save);

        Optional<PersonEntity> foundedOptionalUser =
                Optional.of(
                        linkRepository.findByOriginalUrl(link3.getOriginalUrl()).get()
                                .getSite().getPerson());

        assertTrue(foundedOptionalUser.isPresent());
        assertThat(foundedOptionalUser.get().getUsername()).isEqualTo(person.getUsername());
    }

    @Test
    public void whenCheckSiteByDomainNameAndPersonThenGetCorrectResults() {
        PersonEntity person1 = PersonEntity.builder()
                .username("username1")
                .password("password1").build();
        PersonEntity person2 = PersonEntity.builder()
                .username("username2")
                .password("password2").build();
        List.of(person2, person1).forEach(personRepository::save);

        SiteEntity site1 = SiteEntity.builder()
                .domainName("job4j.ru")
                .person(person2).build();
        SiteEntity site2 = SiteEntity.builder()
                .domainName("github.com")
                .person(person1).build();
        List.of(site2, site1).forEach(siteRepository::save);

        assertFalse(siteRepository.existsByDomainNameAndPersonId(site1.getDomainName(), site2.getPerson().getId()));
        assertFalse(siteRepository.existsByDomainNameAndPersonId(site2.getDomainName(), site1.getPerson().getId()));
        assertTrue(siteRepository.existsByDomainNameAndPersonId(site2.getDomainName(), site2.getPerson().getId()));
        assertTrue(siteRepository.existsByDomainNameAndPersonId(site1.getDomainName(), site1.getPerson().getId()));
    }

    @Test
    public void whenCheckLinkByOriginalUrlThenGetCorrectResultThenFindAllBySiteId() {
        PersonEntity person1 = PersonEntity.builder().username("username1").password("password1").build();
        PersonEntity person2 = PersonEntity.builder().username("username2").password("password2").build();
        List.of(person2, person1).forEach(personRepository::save);

        SiteEntity site1 = SiteEntity.builder().domainName("job4j.ru").person(person1).build();
        SiteEntity site2 = SiteEntity.builder().domainName("github.com").person(person2).build();
        List.of(site2, site1).forEach(siteRepository::save);

        LinkEntity link11 = LinkEntity.builder()
                .originalUrl("https://job4j.ru/exercise/216/task/1219/547426")
                .site(site1).build();
        LinkEntity link12 = LinkEntity.builder()
                .originalUrl("https://job4j.ru/exercise/214/task-view/1214")
                .site(site1).build();
        LinkEntity link21 = LinkEntity.builder()
                .originalUrl("https://github.com/MikhailChrn/job4j_github_analysis/commits/master/")
                .site(site2).build();
        LinkEntity link22 = LinkEntity.builder()
                .originalUrl("https://github.com/MikhailChrn/job4j_url_shortcut/commits/main/")
                .site(site2).build();
        List.of(link22, link11, link21, link12).forEach(linkRepository::save);

        link11.setCode("code11");
        link12.setCode("code12");
        link21.setCode("code21");
        link22.setCode("code22");
        List.of(link22, link11, link21, link12).forEach(linkRepository::save);

        assertTrue(linkRepository.existsByOriginalUrl(link21.getOriginalUrl()));
        assertFalse(linkRepository.existsByOriginalUrl(link21.getOriginalUrl() + "/plus"));
        PersonEntity person21 = linkRepository.findByCode(link21.getCode()).get().getSite().getPerson();
        assertEquals(person21, person2);

        List<LinkEntity> expectedList = List.of(link22, link21);
        List<LinkEntity> actualList = (List<LinkEntity>) linkRepository.findAllBySiteId(site1.getId());

        assertEquals(actualList.size(), expectedList.size());
        assertTrue(actualList.containsAll(expectedList));
    }

    @Test
    void whenFindAllSitesByPersonIdThenGetListOfSites() {
        PersonEntity person1 = PersonEntity.builder()
                .username("username1").password("password1").build();
        PersonEntity person2 = PersonEntity.builder()
                .username("username2").password("password2").build();
        List.of(person2, person1).forEach(personRepository::save);

        SiteEntity site1 = SiteEntity.builder()
                .domainName("https://web.whatsapp.com/").person(person1).build();
        SiteEntity site2 = SiteEntity.builder()
                .domainName("https://job4j.ru/").person(person2).build();
        SiteEntity site3 = SiteEntity.builder()
                .domainName("https://github.com/").person(person2).build();
        List.of(site2, site3, site1).forEach(siteRepository::save);

        List<SiteEntity> expectedList = List.of(site3, site2);
        List<SiteEntity> actualList = (List<SiteEntity>) siteRepository.findAllByPersonId(person2.getId());

        assertEquals(actualList.size(), expectedList.size());
        assertTrue(actualList.containsAll(expectedList));
    }
}