package ru.job4j.shortcut.service.security;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.security.UserDetailsRegular;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.repository.PersonRepository;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceRegular implements UserDetailsService {

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User Details Service searching for a person: {}", username);
        Optional<PersonEntity> personEntityOptional =
                personRepository.findByUsername(username);
        if (personEntityOptional.isPresent()) {
            return new UserDetailsRegular(personEntityOptional.get()) {
            };
        } else {
            throw new UsernameNotFoundException("person not found");
        }
    }

    @PostConstruct
    public void init() {
        System.out.println("Используемый PasswordEncoder: " + passwordEncoder.getClass().getName());
    }
}