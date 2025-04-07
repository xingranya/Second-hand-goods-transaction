package com.secondhand.service;

import com.secondhand.entity.Review;
import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    
    Review getReviewById(Long id);
    
    List<Review> getReviewsByProduct(Long productId);
    
    List<Review> getReviewsByUser(Long userId);
    
    Double getAverageRating(Long productId);
    
    Long getReviewCount(Long productId);
    
    Review updateReview(Long id, Review review);
    
    void deleteReview(Long id);
    
    boolean hasUserReviewedProduct(Long productId, Long userId);
} 