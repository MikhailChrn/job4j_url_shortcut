package ru.job4j.shortcut.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterSiteResponseDTO {

    /**
     * Флаг 'status' указывает, что регистрация выполнена или нет,
     * false - если сайт у данного пользователя уже есть в системе.
     */
    private boolean status;

    private String domainName;

    private String ownerName;

}
