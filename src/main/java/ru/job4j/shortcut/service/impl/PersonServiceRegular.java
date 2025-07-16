package ru.job4j.shortcut.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.dto.request.SignupRequestDTO;
import ru.job4j.shortcut.dto.response.RegisterResponseDTO;
import ru.job4j.shortcut.repository.RoleRepository;
import ru.job4j.shortcut.repository.PersonRepository;
import ru.job4j.shortcut.service.PersonService;

@Service
@AllArgsConstructor
public class PersonServiceRegular implements PersonService {

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Override
    public RegisterResponseDTO signUp(SignupRequestDTO signUpRequest) {
        return null;
    }
}
