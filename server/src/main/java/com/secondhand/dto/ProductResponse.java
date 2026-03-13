package com.secondhand.dto;

import java.math.BigDecimal;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final BigDecimal originalPrice;
    private final String imageUrl;
    private final String category;
    private final String condition;
    private final String campus;
    private final String status;
    private final String createdAt;
    private final SellerSummary seller;

    public ProductResponse(Long id, String name, String description, BigDecimal price, BigDecimal originalPrice,
                           String imageUrl, String category, String condition, String campus, String status,
                           String createdAt, SellerSummary seller) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.category = category;
        this.condition = condition;
        this.campus = campus;
        this.status = status;
        this.createdAt = createdAt;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getCondition() {
        return condition;
    }

    public String getCampus() {
        return campus;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public SellerSummary getSeller() {
        return seller;
    }

    public static class SellerSummary {
        private final Long id;
        private final String username;
        private final String name;
        private final String school;

        public SellerSummary(Long id, String username, String name, String school) {
            this.id = id;
            this.username = username;
            this.name = name;
            this.school = school;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }

        public String getSchool() {
            return school;
        }
    }
}
