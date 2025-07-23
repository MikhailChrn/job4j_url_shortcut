package ru.job4j.shortcut.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.job4j.shortcut.dto.request.SignupUserRequestDTO;
import ru.job4j.shortcut.dto.response.RegisterUserResponseDTO;
import ru.job4j.shortcut.entity.ERole;

public interface PersonService {

    static boolean hasRole(String role) {
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

    RegisterUserResponseDTO signUp(SignupUserRequestDTO signUpRequest);

    boolean hasRole(String username, ERole role);
}
