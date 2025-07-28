package ru.job4j.shortcut.controller.study;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
public class MyRestController {

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String hello() {

        return "hello";
    }

    @GetMapping("/http/test")
    @ResponseStatus(HttpStatus.OK)
    public String httpAuthTest() {

        return "successful httpAuthTest";
    }
}
