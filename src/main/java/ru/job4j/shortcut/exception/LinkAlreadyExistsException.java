package ru.job4j.shortcut.exception;

public class LinkAlreadyExistsException extends RuntimeException {
    private final String originalUrl;

    public LinkAlreadyExistsException(String originalUrl) {
        super("Ссылка '" + originalUrl + "' уже зарегистрирован");
        this.originalUrl = originalUrl;
    }
}
