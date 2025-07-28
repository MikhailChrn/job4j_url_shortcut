package ru.job4j.shortcut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.ConvertLinkResponseDTO;
import ru.job4j.shortcut.dto.response.MessageResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterSiteResponseDTO;
import ru.job4j.shortcut.dto.response.StatisticResponseDto;

import ru.job4j.shortcut.entity.ERole;

import ru.job4j.shortcut.service.PersonService;
import ru.job4j.shortcut.service.SiteService;

@RestController
@RequestMapping("api/site")
public class SiteControler {

    @Autowired
    private SiteService siteService;

    @Autowired
    private PersonService personService;

    /**
     * Метод добавляет в базу доменное имя сайта для добавления ссылок
     * и ведения статистики
     */
    @PostMapping("/registration")
    public ResponseEntity<RegisterSiteResponseDTO> registerSite(
            Authentication authentication,
            @RequestBody UrlRequestDTO urlRequestDTO) {

        String username = authentication.getName();

        RegisterSiteResponseDTO responseDTO = siteService.registerSite(urlRequestDTO, username);

        return ResponseEntity.status(responseDTO.isStatus() ? HttpStatus.CREATED : HttpStatus.ACCEPTED)
                .body(responseDTO);
    }

    /**
     * Метод добавляет в базу ссылку и генерирует ассоциированный с ней код
     * для добавления ссылок и ведения статистики
     */
    @PostMapping("/convert")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponseDTO> convertLink(
            Authentication authentication,
            @RequestBody UrlRequestDTO urlRequestDTO) {

        String username = authentication.getName();

        ConvertLinkResponseDTO responseDTO = siteService.convertLink(urlRequestDTO, username);

        return ResponseEntity.status(responseDTO.isStatus() ? HttpStatus.CREATED : HttpStatus.ACCEPTED)
                .body(MessageResponseDTO.builder().message(responseDTO.getCode()).build());
    }

    /**
     * Метод возвращает статистику по всем ссылкам ассоциированным с текущем пользователем
     * (Если у текущего пользователя присвоена роль 'admin', то возвращается статистика
     * по всем ссылкам имеющимся в базе приложения)
     */
    @GetMapping("/statistic")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StatisticResponseDto> getStatistic(
            Authentication authentication) {

        if (personService.hasRole(ERole.ROLE_ADMIN.name())) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(siteService.getAllStatistic());
        }

        String username = authentication.getName();

        return ResponseEntity.status(HttpStatus.OK)
                .body(siteService.getStatisticByUsername(username));
    }

}
