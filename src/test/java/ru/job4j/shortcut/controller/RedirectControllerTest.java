package ru.job4j.shortcut.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.shortcut.service.SiteService;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.when;

/**
 * @WebMvcTest — поднимает контекст только для Web слоя (легкие и быстрые тесты).
 * mockMvc — позволяет тестировать HTTP-запросы без запуска сервера.
 * @TestConfiguration - выполняет ЯВНУЮ регистрацию моков через
 * @AutoConfigureMockMvc(addFilters = false) - отключает регистрацию фильтров
 *      из контекста приложения (Spring Security) с MockMvc в тестах
 */
@WebMvcTest(RedirectController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
@Import(RedirectControllerTest.MockConfig.class)
class RedirectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    SiteService siteService;

    @TestConfiguration
    static class MockConfig {

        @Bean
        SiteService siteService() {
            return Mockito.mock(SiteService.class);
        }
    }

    @Test
    @WithAnonymousUser
    void whenRequestValidCodeThenRedirectWith302() throws Exception {
        String codeRequest = "abc123";
        String urlResponse = "https://example.com/example";

        when(siteService.getOriginalLink(codeRequest)).thenReturn(Optional.of(urlResponse));

        mockMvc.perform(get("/redirect/" + codeRequest))
                .andExpect(status().isFound())
                .andExpect(header().string(HttpHeaders.LOCATION, urlResponse));
    }

    @Test
    @WithAnonymousUser
    void whenInvalidCodeThenReturn404() throws Exception {
        String codeRequest = "invalidCode";

        when(siteService.getOriginalLink(codeRequest)).thenReturn(Optional.empty());

        mockMvc.perform(get("/redirect/" + codeRequest))
                .andExpect(status().isNotFound());
    }

}