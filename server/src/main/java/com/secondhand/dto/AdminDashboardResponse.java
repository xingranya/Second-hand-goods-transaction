package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardResponse {
    private long userCount;
    private long productCount;
    private long wantedCount;
    private long orderCount;
    private long completedOrderCount;
    private long verifiedUserCount;
    private long availableProductCount;
}
