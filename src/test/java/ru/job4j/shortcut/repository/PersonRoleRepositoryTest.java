package ru.job4j.shortcut.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.shortcut.entity.ERole;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRoleRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void deleteAllBefore() {
        personRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @AfterAll
    public void deleteAllAfterAll() {
        personRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<PersonEntity> optionalUser = personRepository.findById(1);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void whenSaveSeveralThenFindByFullName() {
        PersonEntity person1 = PersonEntity.builder()
                .username("username1")
                .password("password1").build();
        PersonEntity person2 = PersonEntity.builder()
                .username("username2")
                .password("password2").build();
        PersonEntity person3 = PersonEntity.builder()
                .username("username3")
                .password("password3").build();
        List.of(person3, person1, person2).forEach(personRepository::save);

        Optional<PersonEntity> optionalFoundedUser =
                personRepository.findByUsername(person2.getUsername());

        assertThat(optionalFoundedUser).isPresent();
        assertThat(optionalFoundedUser.get().getUsername())
                .isEqualTo(person2.getUsername());
    }

    @Test
    void whenAddSeveralWithRoleThenGetAllAssignedRoles() {
        RoleEntity role1 = RoleEntity.builder().title(ERole.ROLE_ADMIN).build();
        RoleEntity role2 = RoleEntity.builder().title(ERole.ROLE_USER).build();
        List.of(role2, role1).forEach(roleRepository::save);

        PersonEntity person1 = PersonEntity.builder()
                .username("username1")
                .password("password1").build();
        person1.setRoles(Set.of(role2));
        PersonEntity person2 = PersonEntity.builder()
                .username("username2")
                .password("password2").build();
        person2.setRoles(Set.of(role2, role1));
        List.of(person2, person1).forEach(personRepository::save);

        Optional<PersonEntity> optionalFoundedUser =
                personRepository.findByUsername(person2.getUsername());

        assertThat(optionalFoundedUser).isPresent();
        assertEquals(2, optionalFoundedUser.get().getRoles().size());

    }
}