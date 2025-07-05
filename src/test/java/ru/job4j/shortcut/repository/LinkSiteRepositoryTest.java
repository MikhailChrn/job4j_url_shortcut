package ru.job4j.shortcut.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.shortcut.entity.LinkEntity;
import ru.job4j.shortcut.entity.SiteEntity;
import ru.job4j.shortcut.entity.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LinkSiteRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private LinkRepository linkRepository;

    @BeforeEach
    public void deleteAllBefore() {
        linkRepository.deleteAll();
        siteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    public void deleteAllAfterAll() {
        linkRepository.deleteAll();
        siteRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<LinkEntity> optionalLink = linkRepository.findById(1);

        assertThat(optionalLink).isEmpty();
    }

    @Test
    public void whenSaveOneUserTwoSiteAndFourLinkThenGetAllEntities() {
        UserEntity user = UserEntity.builder()
                .email("email1@email.ru")
                .password("password1").build();
        userRepository.save(user);

        SiteEntity site1 = SiteEntity.builder()
                .domainName("job4j.ru")
                .user(user).build();
        SiteEntity site2 = SiteEntity.builder()
                .domainName("github.com")
                .user(user).build();
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

        Optional<UserEntity> foundedOptionalUser =
                Optional.of(
                        linkRepository.findAllByOriginalUrl(link3.getOriginalUrl()).get()
                                .getSite().getUser());

        assertTrue(foundedOptionalUser.isPresent());
        assertThat(foundedOptionalUser.get().getEmail()).isEqualTo(user.getEmail());
    }

}