package com.secondhand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private String id;
    private PeerUser peerUser;
    private String lastMessage;
    private int unreadCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeerUser {
        private Long id;
        private String name;
    }
}
