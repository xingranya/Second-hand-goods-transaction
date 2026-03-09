package com.secondhand.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VerifyRequest {
    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "学号不能为空")
    private String studentNo;

    @NotBlank(message = "学校不能为空")
    private String school;
}
