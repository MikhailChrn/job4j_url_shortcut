package ru.job4j.shortcut.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.job4j.shortcut.entity.ERole;
import ru.job4j.shortcut.entity.RoleEntity;
import ru.job4j.shortcut.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRoleRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void deleteAllBefore() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @AfterAll
    public void deleteAllAfterAll() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        Optional<UserEntity> optionalUser = userRepository.findById(1);

        assertThat(optionalUser).isEmpty();
    }

    @Test
    void whenSaveSeveralThenFindByFullName() {
        UserEntity user1 = UserEntity.builder()
                .email("email1@email.")
                .password("password1").build();
        UserEntity user2 = UserEntity.builder()
                .email("email2@email.")
                .password("password2").build();
        UserEntity user3 = UserEntity.builder()
                .email("email3@email.")
                .password("password3").build();
        List.of(user3, user1, user2).forEach(userRepository::save);

        Optional<UserEntity> optionalFoundedUser =
                userRepository.findByEmail(user2.getEmail());

        assertThat(optionalFoundedUser).isPresent();
        assertThat(optionalFoundedUser.get().getEmail())
                .isEqualTo(user2.getEmail());
    }

    @Test
    void whenAddSeveralWithRoleThenGetAllAssignedRoles() {
        RoleEntity role1 = RoleEntity.builder().title(ERole.ROLE_ADMIN).build();
        RoleEntity role2 = RoleEntity.builder().title(ERole.ROLE_USER).build();
        List.of(role2, role1).forEach(roleRepository::save);

        UserEntity user1 = UserEntity.builder()
                .email("email1@email.")
                .password("password1").build();
        user1.setRoleEntities(Set.of(role2));
        UserEntity user2 = UserEntity.builder()
                .email("email2@email.")
                .password("password2").build();
        user2.setRoleEntities(Set.of(role2, role1));
        List.of(user2, user1).forEach(userRepository::save);

        Optional<UserEntity> optionalFoundedUser =
                userRepository.findByEmail(user2.getEmail());

        assertThat(optionalFoundedUser).isPresent();
        assertEquals(2, optionalFoundedUser.get().getRoleEntities().size());

    }
}