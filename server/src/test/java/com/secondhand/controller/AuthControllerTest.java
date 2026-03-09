package com.secondhand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.dto.JwtRequest;
import com.secondhand.model.User;
import com.secondhand.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void testLoginUser() throws Exception {
        // First register a user
        User user = new User();
        user.setUsername("logintest");
        user.setPassword("password123");
        user.setEmail("logintest@example.com");
        userService.registerUser(user);

        // Then try to login
        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername("logintest");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("logintest"));
    }

    @Test
    public void testRegisterDuplicateUsername() throws Exception {
        // First register a user
        User user = new User();
        user.setUsername("duplicate");
        user.setPassword("password123");
        user.setEmail("duplicate@example.com");
        userService.registerUser(user);

        // Try to register another user with the same username
        User duplicateUser = new User();
        duplicateUser.setUsername("duplicate");
        duplicateUser.setPassword("password123");
        duplicateUser.setEmail("another@example.com");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicateUser)))
                .andExpect(status().isBadRequest());
    }
} 