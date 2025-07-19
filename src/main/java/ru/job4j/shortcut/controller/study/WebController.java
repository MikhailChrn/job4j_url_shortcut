package ru.job4j.shortcut.controller.study;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    /**
     * Доступная для всех страница логина
     * ПРИМЕР ХАБР
     * https://habr.com/ru/articles/916672/
     *
     * Для авторизации через HTML форму (.loginProcessingUrl("/process-login")) в Postman,
     * нужно отправить POST-запрос на URL формы, передав данные авторизации в теле запроса
     * (обычно в формате x-www-form-urlencoded).
     * После успешной авторизации, сервер может вернуть cookie или токен,
     * которые нужно будет использовать в последующих запросах.
     * Пример:
     * JSESSIONID
     * 1CF82E1983E18EE674023167E2B7F6D2
     */

    @GetMapping("/login")
    public String handleLoginPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String handleAdminPage(Model model) {
        String message = "Hello, master "
                + SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("message", message);
        return "admin";
    }

    @GetMapping("/")
    public String handleMainPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        String message = "Ho do you do, mister " + userName + "? "
                + "Your authorities: " + auth.getAuthorities();
        model.addAttribute("message", message);
        return "index";
    }
}
