package ru.job4j.shortcut.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.job4j.shortcut.entity.ERole;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.RoleEntity;

import java.util.Optional;
import java.util.Set;

/**
 * Хабр ПРИМЕР
 * https://habr.com/ru/articles/916672/
 * разбираем
 *
 * @EventListener: Эта аннотация указывает, что метод должен быть вызван при публикации события определенного типа.
 * <p>
 * onApplicationEvent(ContextRefreshedEvent event):
 * Это имя метода, которое обрабатывает событие. Внутри этого метода можно выполнять любую логику,
 * которая должна быть выполнена после инициализации или обновления контекста.
 * <p>
 * ContextRefreshedEvent:
 * Это событие сигнализирует о том, что контекст приложения был инициализирован или обновлен.
 * Это может произойти при старте приложения, при загрузке конфигурации,
 * или при динамическом обновлении контекста.
 * <p>
 * Таким образом, является удобным способом выполнить код после инициализации или обновления контекста Spring,
 * позволяя приложению выполнить необходимые действия, когда оно полностью готово к работе.
 */

@Slf4j
@Component
@AllArgsConstructor
public class DbInit {
    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createDefaultUsers();
    }

    private void createDefaultUsers() {
        RoleEntity adminRole = createRole(ERole.ROLE_ADMIN);
        RoleEntity userRole = createRole(ERole.ROLE_USER);

        createUser("admin", adminRole);
        createUser("user", userRole);
    }

    private void createUser(String username, RoleEntity role) {
        log.warn("Creating user {}", username);
        Optional<PersonEntity> personRepositoryByUsername = personRepository.findByUsername(username);

        if (personRepositoryByUsername.isEmpty()) {
            PersonEntity person = PersonEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(username))
                    .roles(Set.of(role)).build();
            personRepository.save(person);
        }
    }

    private RoleEntity createRole(ERole title) {
        log.warn("Creating roleEntityOptional {}", title);
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByTitle(title);
        if (roleEntityOptional.isEmpty()) {
            roleEntityOptional = Optional.of(RoleEntity.builder()
                    .title(title).build());
        }
        roleRepository.save(roleEntityOptional.get());
        return roleEntityOptional.get();
    }
}
