package ru.job4j.shortcut.service;

import ru.job4j.shortcut.dto.request.SignupRequestDTO;
import ru.job4j.shortcut.dto.response.RegisterResponseDTO;

public interface PersonService {

    RegisterResponseDTO signUp(SignupRequestDTO signUpRequest);

}
