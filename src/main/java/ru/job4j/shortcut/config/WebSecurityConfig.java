package ru.job4j.shortcut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @EnableMethodSecurity - это аннотация в Spring Security, которая включает безопасность на уровне методов для вашего приложения.
 * Это означает, что вы можете применять правила авторизации непосредственно к методам, используя различные аннотации безопасности,
 * такие как @PreAuthorize, @PostAuthorize, @Secured.
 */

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/test").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
