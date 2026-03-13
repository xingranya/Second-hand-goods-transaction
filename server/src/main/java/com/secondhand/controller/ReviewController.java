package com.secondhand.controller;

import com.secondhand.dto.ReviewCreateRequest;
import com.secondhand.dto.ReviewResponse;
import com.secondhand.entity.Product;
import com.secondhand.entity.Review;
import com.secondhand.entity.User;
import com.secondhand.service.ProductService;
import com.secondhand.service.ReviewService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request, Principal principal) {
        User reviewer = userService.getUserByUsername(principal.getName());
        Product product = productService.getProductById(request.getProductId());

        Review review = new Review();
        review.setProduct(product);
        review.setReviewer(reviewer);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return ResponseEntity.ok(toResponse(reviewService.createReview(review)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(reviewService.getReviewById(id)));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getAverageRating(productId));
    }

    @GetMapping("/product/{productId}/count")
    public ResponseEntity<Long> getReviewCount(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewCount(productId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewCreateRequest request,
            Principal principal) {
        Review existing = reviewService.getReviewById(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        if (!existing.getReviewer().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("只能修改自己的评价");
        }

        Review details = new Review();
        details.setRating(request.getRating());
        details.setComment(request.getComment());
        return ResponseEntity.ok(toResponse(reviewService.updateReview(id, details)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Principal principal) {
        Review review = reviewService.getReviewById(id);
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean canDelete = review.getReviewer().getId().equals(currentUser.getId())
                || "ADMIN".equalsIgnoreCase(currentUser.getRole());
        if (!canDelete) {
            throw new IllegalArgumentException("无权删除该评价");
        }
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserReviewedProduct(
            @RequestParam Long productId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(reviewService.hasUserReviewedProduct(productId, userId));
    }

    private ReviewResponse toResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProduct() == null ? null : review.getProduct().getId(),
                review.getProduct() == null ? "" : review.getProduct().getName(),
                review.getReviewer() == null ? null : review.getReviewer().getId(),
                displayName(review.getReviewer()),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt() == null ? "" : review.getCreatedAt().toString()
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
