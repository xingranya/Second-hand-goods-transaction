package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminUserItemResponse {
    private Long id;
    private String username;
    private String name;
    private String school;
    private String role;
    private boolean verified;
    private boolean enabled;
    private long publishCount;
    private long orderCount;
}
