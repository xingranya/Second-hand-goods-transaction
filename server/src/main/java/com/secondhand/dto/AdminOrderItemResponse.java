package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AdminOrderItemResponse {
    private String id;
    private String status;
    private String productName;
    private String buyerName;
    private String sellerName;
    private BigDecimal amount;
    private String createdAt;
}
