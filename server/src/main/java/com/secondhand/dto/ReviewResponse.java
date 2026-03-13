package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Long reviewerId;
    private String reviewerName;
    private int rating;
    private String comment;
    private String createdAt;
}
