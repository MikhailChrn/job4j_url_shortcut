package ru.job4j.shortcut.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.request.SignupUserRequestDTO;
import ru.job4j.shortcut.dto.response.RegisterUserResponseDTO;
import ru.job4j.shortcut.entity.ERole;
import ru.job4j.shortcut.entity.PersonEntity;
import ru.job4j.shortcut.entity.RoleEntity;
import ru.job4j.shortcut.repository.RoleRepository;
import ru.job4j.shortcut.repository.PersonRepository;
import ru.job4j.shortcut.service.PersonService;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final RoleRepository roleRepository;

    /**
     * Метод регистрирует нового пользователя и присваивает,
     * по-возможности (если присутствуют в БД), роли из запроса.
     */
    public RegisterUserResponseDTO signUp(SignupUserRequestDTO signUpRequest) {

        if (Boolean.TRUE.equals(personRepository.existsByUsername(signUpRequest.getUsername()))) {
            return new RegisterUserResponseDTO(HttpStatus.BAD_REQUEST,
                    "Error: Username is already taken !");
        }

        PersonEntity person = PersonEntity.builder().username(signUpRequest.getUsername())
                .password(encoder.encode(signUpRequest.getPassword())).build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<RoleEntity> roles = new HashSet<>();

        Supplier<RuntimeException> throwException = () -> new RuntimeException("Error: Role is not found.");

        if (strRoles == null) {
            roles.add(roleRepository.findByTitle(ERole.ROLE_USER).orElseThrow(throwException));
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> roles.add(roleRepository.findByTitle(ERole.ROLE_ADMIN).orElseThrow(throwException));
                    case "user" -> roles.add(roleRepository.findByTitle(ERole.ROLE_USER).orElseThrow(throwException));
                    default -> roles.add(roleRepository.findByTitle(ERole.ROLE_USER).orElseThrow(throwException));
                }
            });
        }

        person.setRoles(roles);
        personRepository.save(person);

        return new RegisterUserResponseDTO(HttpStatus.OK,
                String.format("Person '%s' registered successfully!", person.getUsername()));
    }

    /**
     * Метод проверяет наличие роли
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (role.equals(authority.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }
}
