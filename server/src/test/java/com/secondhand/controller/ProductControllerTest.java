package com.secondhand.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetProductDetailContainsNewFields() throws Exception {
        String username = "seller_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        User seller = new User();
        seller.setUsername(username);
        seller.setPassword(passwordEncoder.encode("password123"));
        seller.setEmail(username + "@example.com");
        seller.setSchool("主校区");
        seller = userRepository.save(seller);

        Product product = new Product();
        product.setName("测试商品");
        product.setDescription("测试描述");
        product.setPrice(new BigDecimal("88.00"));
        product.setOriginalPrice(new BigDecimal("168.00"));
        product.setCampus("东校区");
        product.setCategory("数码");
        product.setCondition("95新");
        product.setStatus("AVAILABLE");
        product.setImageUrl("https://example.com/product.jpg");
        product.setSeller(seller);
        product = productRepository.save(product);

        mockMvc.perform(get("/api/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("测试商品"))
                .andExpect(jsonPath("$.originalPrice").value(168.00))
                .andExpect(jsonPath("$.campus").value("东校区"))
                .andExpect(jsonPath("$.seller.id").value(seller.getId()));
    }

    @Test
    public void testCreateProductBindsCurrentUserAndReturnsDto() throws Exception {
        String username = "creator_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        User seller = new User();
        seller.setUsername(username);
        seller.setPassword(passwordEncoder.encode("password123"));
        seller.setEmail(username + "@example.com");
        seller.setName("测试卖家");
        seller.setSchool("主校区");
        userRepository.save(seller);

        Map<String, Object> request = new HashMap<>();
        request.put("name", "新发布商品");
        request.put("description", "用于测试商品发布");
        request.put("price", 128.5);
        request.put("originalPrice", 299.0);
        request.put("imageUrl", "https://example.com/new.jpg");
        request.put("category", "数码");
        request.put("condition", "9成新");
        request.put("campus", "主校区");
        request.put("status", "AVAILABLE");

        mockMvc.perform(post("/api/products")
                        .with(user(username))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("新发布商品"))
                .andExpect(jsonPath("$.campus").value("主校区"))
                .andExpect(jsonPath("$.originalPrice").value(299.0))
                .andExpect(jsonPath("$.seller.username").value(username));
    }
}
