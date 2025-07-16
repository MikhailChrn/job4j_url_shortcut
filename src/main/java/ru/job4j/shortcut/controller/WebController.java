package ru.job4j.shortcut.controller;

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
     * @return login.html, хранящийся в папке templates
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
