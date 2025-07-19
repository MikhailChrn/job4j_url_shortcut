package ru.job4j.shortcut.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponseDTO {

    private String token;

    private String type = "Bearer";

    private Integer id;

    private String username;

    private List<String> roles;

    public JwtResponseDTO(String accessToken,
                          Integer id,
                          String username,
                          List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
