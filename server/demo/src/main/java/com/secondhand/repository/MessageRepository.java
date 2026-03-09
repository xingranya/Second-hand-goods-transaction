package com.secondhand.repository;

import com.secondhand.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);
    
    List<Message> findByReceiverId(Long receiverId);
    
    List<Message> findByProductId(Long productId);
    
    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId OR m.receiver.id = :userId) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findUserConversations(@Param("userId") Long userId);
    
    @Query("SELECT m FROM Message m WHERE " +
           "((m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
           "(m.sender.id = :user2Id AND m.receiver.id = :user1Id)) " +
           "ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id);
    
    @Query("SELECT COUNT(m) FROM Message m WHERE " +
           "m.receiver.id = :userId AND m.isRead = false")
    long countUnreadMessages(@Param("userId") Long userId);
} 