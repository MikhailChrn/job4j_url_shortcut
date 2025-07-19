package ru.job4j.shortcut.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
