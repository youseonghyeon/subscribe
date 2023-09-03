package com.example.subscribify.controller;

import com.example.subscribify.dto.CreateUserDto;
import com.example.subscribify.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입 성공 케이스")
    @Transactional
    void signUpSuccessCase() throws Exception {
        String username = "qwer1234";

        mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMockCreateUserDto(username)))
        ).andExpect(status().isOk());

        assertTrue(userRepository.findByUsername(username).isPresent());
    }

    @Test
    @DisplayName("회원가입 실패 케이스 - 중복된 Username")
    @Transactional
    void signUpFailCaseDuplicatedUsername() throws Exception {
        String username = "qwer1234";
        mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMockCreateUserDto(username)))
        ).andExpect(status().isOk());
        // when
        mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMockCreateUserDto(username)))
        ).andExpect(status().is4xxClientError());

        long userSize = userRepository.findAll().stream().filter(u -> u.getUsername().equals(username)).count();
        assertEquals(1, userSize);
    }

    @Test
    @DisplayName("회원가입 실패 케이스 - 중복된 Email")
    @Transactional
    void signUpFailCaseDuplicatedEmail() throws Exception {
        String username = "qwer1234";
        mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMockCreateUserDto(username + "1")))
        ).andExpect(status().isOk());
        // when
        mockMvc.perform(post("/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMockCreateUserDto(username + "2")))
        ).andExpect(status().is4xxClientError());

        long userSize = userRepository.findAll().stream().filter(u -> u.getEmail().equals("email")).count();
        assertEquals(1, userSize);

    }

    private CreateUserDto createMockCreateUserDto(String username) {
        return new CreateUserDto(username, "password", "email", "firstName",
                "lastName", "address", "city", "state", "zip", "country");
    }

    private CreateUserDto createMockCreateUserDto() {
        return new CreateUserDto("username", "password", "email", "firstName",
                "lastName", "address", "city", "state", "zip", "country");
    }
}
