package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WantedPostResponse {
    private String id;
    private String title;
    private BigDecimal expectedPrice;
    private String deadline;
    private String description;
    private String campus;
    private Publisher publisher;
    private String publishTime;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Publisher {
        private Long id;
        private String name;
    }
}
