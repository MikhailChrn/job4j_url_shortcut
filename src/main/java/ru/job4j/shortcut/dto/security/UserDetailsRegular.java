package ru.job4j.shortcut.dto.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.job4j.shortcut.entity.PersonEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * UserDetails - это основной интерфейс, представляющий информацию о пользователе,
 * необходимую для аутентификации и авторизации.
 * Он содержит геттеры для основных данных, таких как имя пользователя,
 * пароль, полномочия (права) и другие атрибуты,
 * влияющие на аутентификацию и авторизацию.
 */

@Slf4j
public class UserDetailsRegular implements UserDetails {

    private final PersonEntity person;

    public UserDetailsRegular(PersonEntity person) {
        log.info("UserDetails created for a user {}", person.getUsername());
        this.person = person;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public String getUsername() {
        log.info("User Details providing username: {}", person.getUsername());
        return person.getUsername();
    }

    @Override
    public String getPassword() {
        log.info("User Details providing password for a user: {}", person.getUsername());
        return person.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info("User Details providing grants for a user: {}", person.getUsername());
        Set<GrantedAuthority> authorities = new HashSet<>();
        log.info("User's roles: {}", person.getUsername());
        person.getRoles().forEach(role -> authorities.add(
                new SimpleGrantedAuthority(role.getTitle().toString())));
        for (GrantedAuthority authority:authorities) {
            System.out.println("Authorities: " + authority.getAuthority());
        }
        return authorities;
    }
}
