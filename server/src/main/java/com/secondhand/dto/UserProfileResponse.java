package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String name;
    private String school;
    private boolean verified;
    private int publishCount;
    private int soldCount;
    private int favoriteCount;
    private String latestOrderId;
}
