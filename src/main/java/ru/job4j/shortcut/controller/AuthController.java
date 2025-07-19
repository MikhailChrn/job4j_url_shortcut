package ru.job4j.shortcut.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.config.jwt.JwtUtils;
import ru.job4j.shortcut.dto.request.LoginUserRequestDTO;
import ru.job4j.shortcut.dto.request.SignupUserRequestDTO;
import ru.job4j.shortcut.dto.response.JwtResponseDTO;
import ru.job4j.shortcut.dto.response.MessageResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterUserResponseDTO;
import ru.job4j.shortcut.dto.security.UserDetailsImpl;
import ru.job4j.shortcut.service.PersonService;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PersonService personService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Метод создаёт нового пользователя
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDTO> registerUser(@Valid @RequestBody SignupUserRequestDTO signUpRequest) {

        RegisterUserResponseDTO registerUserResponseDTO = personService.signUp(signUpRequest);

        return ResponseEntity.status(registerUserResponseDTO.getStatus())
                .body(new MessageResponseDTO(registerUserResponseDTO.getMessage()));
    }

    /**
     * Метод аутентифицирует пользователя и возвращает актуальный JWT-токен
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponseDTO> authenticateUser(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDTO) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginUserRequestDTO.getUsername(),
                        loginUserRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity
                .ok(new JwtResponseDTO(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }
}
