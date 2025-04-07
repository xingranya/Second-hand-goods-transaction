package com.secondhand.controller;

import com.secondhand.entity.Message;
import com.secondhand.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Message>> getUserConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUserConversations(userId));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversationBetweenUsers(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id) {
        return ResponseEntity.ok(messageService.getConversationBetweenUsers(user1Id, user2Id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Message>> getMessagesByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(messageService.getMessagesByProduct(productId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Message> markMessageAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.markMessageAsRead(id));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllMessagesAsRead(@PathVariable Long userId) {
        messageService.markAllMessagesAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUnreadMessageCount(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
} 