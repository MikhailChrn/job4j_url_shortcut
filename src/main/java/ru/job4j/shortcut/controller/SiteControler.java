package ru.job4j.shortcut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.MessageResponseDTO;
import ru.job4j.shortcut.dto.response.StatisticResponseDto;
import ru.job4j.shortcut.service.SiteService;

@RestController
@RequestMapping("api/site")
public class SiteControler {

    @Autowired
    private SiteService siteService;

    /**
     * Метод добавляет в базу доменное имя сайта для добавления ссылок и ведения статистики
     */
    @PostMapping("/registration")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponseDTO> registerSite(
            Authentication authentication,
            @RequestBody UrlRequestDTO registerSiteDTO) {
        String username = authentication.getName();

        return null;

    }

    /**
     * Метод добавляет в базу ссылку и генерирует ассоциированный с ней код
     * для добавления ссылок и ведения статистики
     */
    @PostMapping("/convert")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<MessageResponseDTO> convertLink(
            Authentication authentication,
            @RequestBody UrlRequestDTO registerSiteDTO) {

        String username = authentication.getName();

        return null;
    }

    /**
     * Метод возвращает статистику по всем ссылкам ассоциированным с текущем пользователем
     * (Если у текущего пользователя присвоена роль 'admin', то возвращается статистика
     * по всем ссылкам имеющимся в базе приложения)
     */
    @GetMapping ("/statistic")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<StatisticResponseDto> getStatistic(
            Authentication authentication,
            @RequestBody UrlRequestDTO registerSiteDTO) {
        String username = authentication.getName();

        return null;
    }

}
