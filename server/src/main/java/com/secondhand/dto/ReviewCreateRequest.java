package com.secondhand.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReviewCreateRequest {
    @NotNull(message = "商品 ID 不能为空")
    private Long productId;

    @Min(value = 1, message = "评分最低为 1 分")
    @Max(value = 5, message = "评分最高为 5 分")
    private int rating;

    private String comment;
}
