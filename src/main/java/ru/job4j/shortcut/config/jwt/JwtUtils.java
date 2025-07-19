package ru.job4j.shortcut.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.job4j.shortcut.dto.security.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

/**
 * Класс предназначен для работы с JWT (JSON Web Token'ами) в Spring Boot-приложении.
 * Его функции:
 *     Генерация JWT-токена;
 *     Извлечение имени пользователя (username) из токена;
 *     Проверка валидности JWT-токена.
 */

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.token.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expired}")
    private int jwtExpirationMs;

    /**
     * Метод используется для создания JWT-токена после успешной аутентификации пользователя.
     *
     * Интерфейс 'org.springframework.security.core.Authentication' представляет собой объект,
     * содержащий информацию о текущем пользователе, его учётных данных и правах доступа.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Метод генерирует ключ HMAC из строки (секретного значения).
     * Использует BASE64-декодирование и готовит ключ через io.jsonwebtoken.security.Keys.
     *
     * !!! ЭТО ВАЖНО: строка-секрет в application.properties должна быть
     * валидной BASE64-строкой соответствующего размера для HS256.
     *
     * Интерфейс 'java.security.Key' в Java используется для представления криптографических ключей.
     *
     * Класс 'io.jsonwebtoken.security.Keys' в библиотеке jjwt (Java JWT) используется для работы с ключами,
     * используемыми для подписи и верификации JSON Web Tokens (JWT).
     * Он предоставляет статические методы для генерации, кодирования и декодирования ключей различных типов,
     * в основном используемых в JWT для обеспечения безопасности.
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Метод парсит токен;
     *     Проверяет подпись через key();
     *     Возвращает subject из тела токена, то есть getUsername().
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     *
     * Метод пытается распарсить и проверить подпись токена;
     *      Если всё хорошо — возвращает true;
     *      Если есть ошибка (некорректный, истёкший, неподдерживаемый токен и т.д.)
     *      — логирует ошибку и возвращает false.
     *
     *  Обрабатываются исключения из пакета io.jsonwebtoken:
     *      MalformedJwtException — токен повреждён;
     *      ExpiredJwtException — срок действия токена истёк;
     *      UnsupportedJwtException — неподдерживаемый формат токена;
     *      IllegalArgumentException — пустой токен.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
