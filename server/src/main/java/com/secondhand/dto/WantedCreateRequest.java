package com.secondhand.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class WantedCreateRequest {
    @NotBlank(message = "标题不能为空")
    private String title;

    private BigDecimal expectedPrice;
    private String deadline;
    private String description;
    private String campus;
}
