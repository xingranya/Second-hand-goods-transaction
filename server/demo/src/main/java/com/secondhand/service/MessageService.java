package com.secondhand.service;

import com.secondhand.entity.Message;
import java.util.List;

public interface MessageService {
    Message sendMessage(Message message);
    
    Message getMessageById(Long id);
    
    List<Message> getUserConversations(Long userId);
    
    List<Message> getConversationBetweenUsers(Long user1Id, Long user2Id);
    
    List<Message> getMessagesByProduct(Long productId);
    
    Message markMessageAsRead(Long messageId);
    
    void markAllMessagesAsRead(Long userId);
    
    long getUnreadMessageCount(Long userId);
    
    void deleteMessage(Long messageId);
} 