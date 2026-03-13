package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDetailResponse {
    private String id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private Long productId;
    private String productName;
    private String content;
    private boolean read;
    private String createdAt;
}
