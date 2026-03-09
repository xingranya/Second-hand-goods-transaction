package com.secondhand.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ConversationMessageRequest {
    @NotBlank(message = "消息内容不能为空")
    private String content;
    private Long productId;
}
