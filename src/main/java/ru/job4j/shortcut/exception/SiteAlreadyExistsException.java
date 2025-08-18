package ru.job4j.shortcut.exception;

/**
 * Выбрасывается при попытке зарегистрировать домен, который уже существует в системе.
 */
public class SiteAlreadyExistsException extends RuntimeException {

    private final String domainName;

    public SiteAlreadyExistsException(String domainName) {
        super("Домен '" + domainName + "' уже зарегистрирован");
        this.domainName = domainName;
    }
}
