package com.secondhand.controller;

import com.secondhand.entity.Product;
import com.secondhand.entity.Transaction;
import com.secondhand.entity.User;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.TransactionRepository;
import com.secondhand.repository.UserRepository;
import com.secondhand.repository.WantedPostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WantedPostRepository wantedPostRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testDashboardStatsRequiresAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard/stats").with(user("normal-user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDashboardStatsReturnsRepositoryCounts() throws Exception {
        mockMvc.perform(get("/api/admin/dashboard/stats").with(user("admin-user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userCount").value(userRepository.count()))
                .andExpect(jsonPath("$.productCount").value(productRepository.count()))
                .andExpect(jsonPath("$.wantedCount").value(wantedPostRepository.count()))
                .andExpect(jsonPath("$.orderCount").value(transactionRepository.count()))
                .andExpect(jsonPath("$.completedOrderCount").value(transactionRepository.countByStatus("COMPLETED")));
    }

    @Test
    public void testUpdateProductStatusPersistsChange() throws Exception {
        User seller = createUser("seller", "USER", true, true);
        Product product = createProduct(seller, "AVAILABLE");

        mockMvc.perform(
                        patch("/api/admin/products/" + product.getId() + "/status")
                                .param("status", "SOLD")
                                .with(user("admin-user").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.status").value("SOLD"));

        Product updated = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("SOLD");
    }

    @Test
    public void testDeleteProductRemovesRecord() throws Exception {
        User seller = createUser("seller", "USER", true, true);
        Product product = createProduct(seller, "AVAILABLE");

        mockMvc.perform(delete("/api/admin/products/" + product.getId()).with(user("admin-user").roles("ADMIN")))
                .andExpect(status().isOk());

        assertThat(productRepository.findById(product.getId())).isEmpty();
    }

    @Test
    public void testUpdateUserEnabledAndVerifyPersistsChange() throws Exception {
        User targetUser = createUser("target", "USER", false, true);

        mockMvc.perform(
                        patch("/api/admin/users/" + targetUser.getId() + "/enabled")
                                .param("enabled", "false")
                                .with(user("admin-user").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(targetUser.getId()))
                .andExpect(jsonPath("$.enabled").value(false));

        mockMvc.perform(
                        patch("/api/admin/users/" + targetUser.getId() + "/verify")
                                .param("verified", "true")
                                .with(user("admin-user").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.verified").value(true));

        User updated = userRepository.findById(targetUser.getId()).orElseThrow();
        assertThat(updated.isEnabled()).isFalse();
        assertThat(updated.isVerified()).isTrue();
    }

    @Test
    public void testOrdersReturnsAdminDtoShape() throws Exception {
        User seller = createUser("seller", "USER", true, true);
        User buyer = createUser("buyer", "USER", true, true);
        Product product = createProduct(seller, "AVAILABLE");
        Transaction transaction = createOrder(product, buyer, seller, "PAID");

        mockMvc.perform(
                        get("/api/admin/orders")
                                .param("status", "PAID")
                                .with(user("admin-user").roles("ADMIN"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id=='o-" + transaction.getId() + "')].productName", hasItem("后台测试商品")))
                .andExpect(jsonPath("$[?(@.id=='o-" + transaction.getId() + "')].buyerName", hasItem("buyer_name")))
                .andExpect(jsonPath("$[?(@.id=='o-" + transaction.getId() + "')].sellerName", hasItem("seller_name")));
    }

    private User createUser(String prefix, String role, boolean verified, boolean enabled) {
        String unique = prefix + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        User user = new User();
        user.setUsername(unique);
        user.setPassword(passwordEncoder.encode("password123"));
        user.setEmail(unique + "@example.com");
        user.setName(prefix + "_name");
        user.setSchool("主校区");
        user.setVerified(verified);
        user.setRole(role);
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    private Product createProduct(User seller, String status) {
        Product product = new Product();
        product.setName("后台测试商品");
        product.setDescription("用于验证后台接口");
        product.setPrice(new BigDecimal("88.00"));
        product.setOriginalPrice(new BigDecimal("128.00"));
        product.setImageUrl("https://example.com/admin-product.jpg");
        product.setCategory("数码");
        product.setCondition("9成新");
        product.setCampus("主校区");
        product.setStatus(status);
        product.setSeller(seller);
        return productRepository.save(product);
    }

    private Transaction createOrder(Product product, User buyer, User seller, String status) {
        Transaction transaction = new Transaction();
        transaction.setProduct(product);
        transaction.setBuyer(buyer);
        transaction.setSeller(seller);
        transaction.setAmount(product.getPrice());
        transaction.setNote("admin-order-" + UUID.randomUUID());
        Transaction saved = transactionRepository.save(transaction);
        saved.setStatus(status);
        return transactionRepository.save(saved);
    }
}
