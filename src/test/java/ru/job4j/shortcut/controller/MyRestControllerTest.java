package ru.job4j.shortcut.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class MyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void hellowTest() throws Exception {
        mockMvc.perform(get("/api/hellow"))
                .andExpect(status().isOk());
    }
}