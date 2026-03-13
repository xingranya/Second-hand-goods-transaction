package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SystemSummaryResponse {
    private long userCount;
    private long productCount;
    private long wantedCount;
    private long orderCount;
    private long completedOrderCount;
}
