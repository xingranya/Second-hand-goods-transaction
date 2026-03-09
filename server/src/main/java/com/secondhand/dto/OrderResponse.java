package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String status;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private Participant buyer;
    private Participant seller;
    private String payMethod;
    private String createTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Participant {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String id;
        private String title;
        private Integer count;
        private BigDecimal price;
    }
}
