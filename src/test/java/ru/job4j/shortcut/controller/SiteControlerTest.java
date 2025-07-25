package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ru.job4j.shortcut.dto.request.UrlRequestDTO;
import ru.job4j.shortcut.dto.response.ConvertLinkResponseDTO;
import ru.job4j.shortcut.dto.response.RegisterSiteResponseDTO;
import ru.job4j.shortcut.service.SiteService;

import java.net.URL;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(SiteControlerTest.MockConfig.class)
class SiteControlerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SiteService siteService;

    @TestConfiguration
    static class MockConfig {

        @Bean
        SiteService siteService() {
            return mock(SiteService.class);
        }
    }

    @Test
    @WithMockUser(username = "user201", roles = {"USER"})
    void whenRequestRegistrationSiteThenResponse201CreatedStatus() throws Exception {

        String urlRequest = "https://example.com/";
        String username = "user201";

        RegisterSiteResponseDTO responseDTO = new RegisterSiteResponseDTO(
                true, new URL(urlRequest).getHost(), username);
        when(siteService.registerSite(any(), eq(username)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/site/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UrlRequestDTO(urlRequest))))
                .andExpect(status().isCreated());

        verify(siteService).registerSite(any(UrlRequestDTO.class), eq(username));
    }

    @Test
    @WithMockUser(username = "user202", roles = {"USER"})
    void whenRequestRegistrationSiteThenResponse202AcceptedStatus() throws Exception {

        String urlRequest = "https://example.com/";
        String username = "user202";

        RegisterSiteResponseDTO responseDTO = new RegisterSiteResponseDTO(
                false, new URL(urlRequest).getHost(), "user201");
        when(siteService.registerSite(any(), eq(username)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/site/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UrlRequestDTO(urlRequest))))
                .andExpect(status().isAccepted());

        verify(siteService).registerSite(any(UrlRequestDTO.class), eq(username));
    }

    @Test
    @WithMockUser(username = "user201", roles = {"USER"})
    void whenRequestConvertLinkThenResponse201CreatedStatus() throws Exception {
        String urlRequest = "https://example.com/example/example.html";
        String username = "user201";
        String code = "abc123";

        ConvertLinkResponseDTO responseDTO = new ConvertLinkResponseDTO(
                true, code, username);
        when(siteService.convertLink(any(UrlRequestDTO.class), eq(username)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/site/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UrlRequestDTO(urlRequest))))
                .andExpect(status().isCreated());

        verify(siteService).convertLink(any(UrlRequestDTO.class), eq(username));
    }

    @Test
    @WithMockUser(username = "user202", roles = {"USER"})
    void whenRequestConvertLinkThenResponse202AcceptedStatus() throws Exception {
        String urlRequest = "https://example.com/example/example.html";
        String username = "user202";
        String code = "abc123";

        ConvertLinkResponseDTO responseDTO = new ConvertLinkResponseDTO(
                false, code, "user201");
        when(siteService.convertLink(any(UrlRequestDTO.class), eq(username)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/site/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UrlRequestDTO(urlRequest))))
                .andExpect(status().isAccepted());

        verify(siteService).convertLink(any(UrlRequestDTO.class), eq(username));
    }
}