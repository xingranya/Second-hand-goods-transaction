package com.secondhand.service.impl;

import com.secondhand.entity.Review;
import com.secondhand.repository.ReviewRepository;
import com.secondhand.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review createReview(Review review) {
        if (hasUserReviewedProduct(review.getProduct().getId(), review.getReviewer().getId())) {
            throw new RuntimeException("User has already reviewed this product");
        }
        return reviewRepository.save(review);
    }

    @Override
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
    }

    @Override
    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public List<Review> getReviewsByUser(Long userId) {
        return reviewRepository.findByReviewerId(userId);
    }

    @Override
    public Double getAverageRating(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    @Override
    public Long getReviewCount(Long productId) {
        return reviewRepository.getReviewCountByProductId(productId);
    }

    @Override
    @Transactional
    public Review updateReview(Long id, Review reviewDetails) {
        Review review = getReviewById(id);
        
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = getReviewById(id);
        reviewRepository.delete(review);
    }

    @Override
    public boolean hasUserReviewedProduct(Long productId, Long userId) {
        return reviewRepository.existsByProductIdAndReviewerId(productId, userId);
    }
} 