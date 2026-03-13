package com.secondhand.controller;

import com.secondhand.dto.ProductCreateRequest;
import com.secondhand.dto.ProductResponse;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.service.ProductService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Product product = toProduct(request, new Product(), currentUser);
        return ResponseEntity.ok(toResponse(productService.createProduct(product)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(productService.getProductById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String campus,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String status) {
        List<Product> products = (keyword == null || keyword.trim().isEmpty())
                ? productService.getAllProducts()
                : productService.searchProducts(keyword.trim());

        List<ProductResponse> list = products.stream()
                .filter(item -> matchesCampus(item, campus))
                .filter(item -> matchesStatus(item, status))
                .sorted(resolveComparator(sort))
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductResponse>> getProductsBySeller(@PathVariable Long sellerId) {
        return ResponseEntity.ok(productService.getProductsBySeller(sellerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateRequest request) {
        Product existing = productService.getProductById(id);
        Product updated = toProduct(request, existing, existing.getSeller());
        return ResponseEntity.ok(toResponse(productService.updateProduct(id, updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProductResponse> updateProductStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(toResponse(productService.updateProductStatus(id, status)));
    }

    private boolean matchesCampus(Product product, String campus) {
        if (campus == null || campus.trim().isEmpty()) {
            return true;
        }
        return campus.trim().equals(product.getCampus());
    }

    private boolean matchesStatus(Product product, String status) {
        if (status == null || status.trim().isEmpty()) {
            return true;
        }
        return status.trim().equalsIgnoreCase(product.getStatus());
    }

    private Comparator<Product> resolveComparator(String sort) {
        if ("priceAsc".equalsIgnoreCase(sort)) {
            return Comparator.comparing(Product::getPrice, Comparator.nullsLast(Comparator.naturalOrder()));
        }
        if ("priceDesc".equalsIgnoreCase(sort)) {
            return Comparator.comparing(Product::getPrice, Comparator.nullsLast(Comparator.reverseOrder()));
        }
        return Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()));
    }

    private Product toProduct(ProductCreateRequest request, Product product, User seller) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(request.getCategory());
        product.setCondition(request.getCondition());
        product.setCampus(request.getCampus());
        product.setStatus(resolveStatus(request.getStatus()));
        product.setSeller(seller);
        return product;
    }

    private ProductResponse toResponse(Product product) {
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
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }

    private String resolveStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return "AVAILABLE";
        }
        return status.trim().toUpperCase(Locale.ROOT);
    }
}
