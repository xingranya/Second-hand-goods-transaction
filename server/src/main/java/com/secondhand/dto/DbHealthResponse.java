package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DbHealthResponse {
    private boolean ok;
    private String message;
}
