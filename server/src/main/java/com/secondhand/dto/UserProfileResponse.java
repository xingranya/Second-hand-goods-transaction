package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String username;
    private String name;
    private String school;
    private boolean verified;
    private String role;
    private boolean enabled;
    private int publishCount;
    private int soldCount;
    private int wantedCount;
    private int orderCount;
    private long unreadMessageCount;
    private int favoriteCount;
    private String latestOrderId;
}
