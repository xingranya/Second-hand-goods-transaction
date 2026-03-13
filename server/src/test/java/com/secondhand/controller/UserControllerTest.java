package com.secondhand.controller;

import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String username;

    @BeforeEach
    public void setUp() {
        username = "tester_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setEmail(username + "@example.com");
        user.setName("测试用户");
        user.setSchool("主校区");
        userRepository.save(user);
    }

    @Test
    public void testGetCurrentUserRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetCurrentUserProfile() throws Exception {
        mockMvc.perform(get("/api/users/me").with(user(username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("测试用户"))
                .andExpect(jsonPath("$.school").value("主校区"))
                .andExpect(jsonPath("$.publishCount").value(0));
    }
}
