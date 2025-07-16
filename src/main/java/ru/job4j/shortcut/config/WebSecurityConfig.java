package ru.job4j.shortcut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @EnableMethodSecurity - это аннотация в Spring Security, которая включает безопасность на уровне методов для вашего приложения.
 * Это означает, что вы можете применять правила авторизации непосредственно к методам, используя различные аннотации безопасности,
 * такие как @PreAuthorize, @PostAuthorize, @Secured.
 * @EnableWebSecurity *
 * — отвечает за безопасность на уровне HTTP-запросов
 * (например, доступ к определённым URL'ам, настройка авторизации, форм логина и др.).
 * <p>
 * 💡 Их назначение не перекрывается, а дополняет друг друга.
 *
 * .loginProcessingUrl("/perform_login") *
 *     Указывает URL, на который отправляется POST-запрос с логином и паролем.
 *     Это POST-запрос, который отправляется с формы входа.
 *     Spring Security обрабатывает этот запрос — проверяет логин и пароль.
 *     Не нужно создавать контроллер для этого URL — Spring Security обрабатывает его сам.
 *     Если ты укажешь этот URL, то твоя HTML-форма должна отправлять данные именно на /perform_login.
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process-login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(form -> form
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
