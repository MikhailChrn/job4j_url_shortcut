package ru.job4j.shortcut.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ru.job4j.shortcut.config.jwt.AuthEntryPointJwt;
import ru.job4j.shortcut.config.jwt.AuthTokenFilter;

/**
 * Основной конфигурационный класс для работы со Spring-security по сценарию JWT-токенов
 */
@Configuration
@EnableMethodSecurity
public class JwtSecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * AuthTokenFilter - фильтр для токенов — это компонент системы безопасности, отвечающий за перехват, извлечение,
     * валидацию и обработку токенов в процессе аутентификации и авторизации HTTP-запросов.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Интерфейс AuthenticationManager в Spring Security выполняет функцию аутентификации пользователя.
     * Является центральным компонентом в процессе аутентификации Spring Security.
     * Его единственный метод authenticate(Authentication authentication) принимает частично заполненный
     * объект Authentication и пытается аутентифицировать пользователя, заполняя его данными,
     * полученными из различных провайдеров
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Интерфейс DaoAuthenticationProvider как конкретная реализация AuthenticationProvider
     * в Spring Security выполняет функцию аутентификации пользователей,
     * используя данные из хранилища (обычно базы данных) через DAO (Data Access Object).
     * <p>
     * Он взаимодействует с AuthenticationManager для проверки учётных данных пользователя
     * и определения, является ли аутентификация успешной.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    /**
     * Интерфейс SecurityFilterChain в Spring Security определяет последовательность фильтров безопасности,
     * которые должны быть применены к входящему HTTP-запросу.
     * Он предоставляет структуру для управления этими фильтрами и определяет,
     * какие из них будут активны для конкретного запроса.
     * SecurityFilterChain позволяет настраивать, какие фильтры применяются, в каком порядке, и для каких URL-шаблонов.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
