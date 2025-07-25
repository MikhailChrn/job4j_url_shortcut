package ru.job4j.shortcut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.shortcut.service.SiteService;

import java.net.URI;

@RestController
@RequestMapping("/redirect")
public class RedirectController {

    @Autowired
    private SiteService siteService;

    /**
     * Метод перенаправляет пользователя по ассоциированной ссылке
     * в ответ на запрос по 'code' из запроса
     */
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable("code") String code) {

        String originalUrl = siteService.getOriginalLink(code);

        if (!originalUrl.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(originalUrl))
                    .build();
        } else {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

    }
}
