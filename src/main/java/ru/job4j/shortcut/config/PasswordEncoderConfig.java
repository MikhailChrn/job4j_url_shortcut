package ru.job4j.shortcut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Для разрыва циклической зависимости PasswordEncoder вынесен в отдельный конфигурационный класс
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Интерфейс PasswordEncoder в Spring Security используется для одностороннего преобразования паролей
     * с целью их безопасного хранения.
     * Он обеспечивает возможность шифрования паролей перед сохранением в базу данных,
     * а также проверки вводимых пользователем паролей на соответствие зашифрованному варианту
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
