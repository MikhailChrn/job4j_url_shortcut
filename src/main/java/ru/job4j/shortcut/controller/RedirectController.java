package ru.job4j.shortcut.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.shortcut.service.SiteService;

@Controller
@RequestMapping("api/redirect")
public class RedirectController {

    @Autowired
    private SiteService siteService;

    /**
     * Метод перенаправляет пользователя по ассоциированной ссылке
     * в ответ на запрос по 'code' из запроса
     */
    @GetMapping("/{code}")
    public String redirectToOriginalLink(@PathVariable String code) {

        String originalUrl = siteService.getOriginalLink(code);

        return "redirect:" + originalUrl;
    }
}
