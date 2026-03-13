package com.secondhand.controller;

import com.secondhand.dto.AdminDashboardResponse;
import com.secondhand.dto.AdminOrderItemResponse;
import com.secondhand.dto.AdminUserItemResponse;
import com.secondhand.dto.ProductResponse;
import com.secondhand.entity.Product;
import com.secondhand.entity.Transaction;
import com.secondhand.entity.User;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.TransactionRepository;
import com.secondhand.repository.UserRepository;
import com.secondhand.repository.WantedPostRepository;
import com.secondhand.service.ProductService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WantedPostRepository wantedPostRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardResponse> dashboardStats() {
        long verifiedUserCount = userRepository.findAll().stream().filter(User::isVerified).count();
        long availableProductCount = productRepository.findByStatus("AVAILABLE").size();

        return ResponseEntity.ok(new AdminDashboardResponse(
                userRepository.count(),
                productRepository.count(),
                wantedPostRepository.count(),
                transactionRepository.count(),
                transactionRepository.countByStatus("COMPLETED"),
                verifiedUserCount,
                availableProductCount
        ));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> products(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        List<Product> list = (keyword == null || keyword.trim().isEmpty())
                ? productService.getAllProducts()
                : productService.searchProducts(keyword.trim());

        return ResponseEntity.ok(list.stream()
                .filter(item -> matchesStatus(item, status))
                .sorted(Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toProductResponse)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/products/{id}/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(toProductResponse(productService.updateProductStatus(id, status)));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserItemResponse>> users(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean enabled) {
        List<User> users = (keyword == null || keyword.trim().isEmpty())
                ? userRepository.findAll()
                : userRepository.findByUsernameContainingIgnoreCaseOrNameContainingIgnoreCase(keyword.trim(), keyword.trim());

        return ResponseEntity.ok(users.stream()
                .filter(item -> role == null || role.trim().isEmpty() || role.equalsIgnoreCase(item.getRole()))
                .filter(item -> enabled == null || item.isEnabled() == enabled)
                .sorted(Comparator.comparing(User::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toAdminUserResponse)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/users/{id}/enabled")
    public ResponseEntity<AdminUserItemResponse> updateUserEnabled(
            @PathVariable Long id,
            @RequestParam boolean enabled) {
        User user = userService.getUserById(id);
        user.setEnabled(enabled);
        return ResponseEntity.ok(toAdminUserResponse(userRepository.save(user)));
    }

    @PatchMapping("/users/{id}/verify")
    public ResponseEntity<AdminUserItemResponse> updateUserVerify(
            @PathVariable Long id,
            @RequestParam boolean verified) {
        User user = userService.getUserById(id);
        user.setVerified(verified);
        return ResponseEntity.ok(toAdminUserResponse(userRepository.save(user)));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderItemResponse>> orders(@RequestParam(required = false) String status) {
        List<Transaction> orders = (status == null || status.trim().isEmpty())
                ? transactionRepository.findAll()
                : transactionRepository.findByStatus(status.trim().toUpperCase(Locale.ROOT));

        return ResponseEntity.ok(orders.stream()
                .sorted(Comparator.comparing(Transaction::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toAdminOrderResponse)
                .collect(Collectors.toList()));
    }

    private boolean matchesStatus(Product product, String status) {
        if (status == null || status.trim().isEmpty()) {
            return true;
        }
        return status.trim().equalsIgnoreCase(product.getStatus());
    }

    private AdminUserItemResponse toAdminUserResponse(User user) {
        long publishCount = productRepository.countBySellerId(user.getId());
        long orderCount = transactionRepository.countByBuyerId(user.getId()) + transactionRepository.countBySellerId(user.getId());
        return new AdminUserItemResponse(
                user.getId(),
                user.getUsername(),
                displayName(user),
                user.getSchool(),
                user.getRole(),
                user.isVerified(),
                user.isEnabled(),
                publishCount,
                orderCount
        );
    }

    private AdminOrderItemResponse toAdminOrderResponse(Transaction transaction) {
        return new AdminOrderItemResponse(
                "o-" + transaction.getId(),
                transaction.getStatus(),
                transaction.getProduct() == null ? "未知商品" : transaction.getProduct().getName(),
                displayName(transaction.getBuyer()),
                displayName(transaction.getSeller()),
                transaction.getAmount(),
                transaction.getCreatedAt() == null ? "" : transaction.getCreatedAt().toString()
        );
    }

    private ProductResponse toProductResponse(Product product) {
        User seller = product.getSeller();
        ProductResponse.SellerSummary sellerSummary = seller == null ? null :
                new ProductResponse.SellerSummary(
                        seller.getId(),
                        seller.getUsername(),
                        displayName(seller),
                        seller.getSchool()
                );
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getOriginalPrice(),
                product.getImageUrl(),
                product.getCategory(),
                product.getCondition(),
                product.getCampus(),
                product.getStatus(),
                product.getCreatedAt() == null ? "" : product.getCreatedAt().toString(),
                sellerSummary
        );
    }

    private String displayName(User user) {
        if (user == null) {
            return "未知用户";
        }
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
