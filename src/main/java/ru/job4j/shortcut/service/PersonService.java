package ru.job4j.shortcut.service;

import ru.job4j.shortcut.dto.request.SignupUserRequestDTO;
import ru.job4j.shortcut.dto.response.RegisterUserResponseDTO;

public interface PersonService {

    boolean hasRole(String role);

    RegisterUserResponseDTO signUp(SignupUserRequestDTO signUpRequest);

}
