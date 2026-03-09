package com.secondhand.controller;

import com.secondhand.dto.ConversationMessageRequest;
import com.secondhand.dto.ConversationResponse;
import com.secondhand.dto.MessageItemResponse;
import com.secondhand.entity.Message;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.service.MessageService;
import com.secondhand.service.ProductService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        return ResponseEntity.ok(messageService.sendMessage(message));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Long currentUserId = currentUser.getId();

        List<Message> raw = messageService.getUserConversations(currentUserId);
        Map<Long, ConversationResponse> map = new LinkedHashMap<>();

        for (Message item : raw) {
            User peer = item.getSender().getId().equals(currentUserId) ? item.getReceiver() : item.getSender();
            ConversationResponse existing = map.get(peer.getId());
            if (existing == null) {
                existing = new ConversationResponse(
                        "u-" + peer.getId(),
                        new ConversationResponse.PeerUser(peer.getId(), displayName(peer)),
                        item.getContent(),
                        0
                );
                map.put(peer.getId(), existing);
            }

            if (item.getReceiver().getId().equals(currentUserId) && !item.isRead()) {
                existing.setUnreadCount(existing.getUnreadCount() + 1);
            }
        }
        return ResponseEntity.ok(new ArrayList<>(map.values()));
    }

    @GetMapping("/conversations/{peerUserId}")
    public ResponseEntity<List<MessageItemResponse>> getConversationMessages(
            @PathVariable String peerUserId,
            Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Long peerId = parsePeerId(peerUserId);

        List<MessageItemResponse> list = messageService.getConversationBetweenUsers(currentUser.getId(), peerId)
                .stream()
                .map(item -> toMessageItem(item, currentUser.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/conversations/{peerUserId}")
    public ResponseEntity<MessageItemResponse> sendConversationMessage(
            @PathVariable String peerUserId,
            @Valid @RequestBody ConversationMessageRequest request,
            Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        User peerUser = userService.getUserById(parsePeerId(peerUserId));

        Message message = new Message();
        message.setSender(currentUser);
        message.setReceiver(peerUser);
        message.setContent(request.getContent());

        if (request.getProductId() != null) {
            Product product = productService.getProductById(request.getProductId());
            message.setProduct(product);
        }

        Message saved = messageService.sendMessage(message);
        return ResponseEntity.ok(toMessageItem(saved, currentUser.getId()));
    }

    @GetMapping("/{id:\\d+}")
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

    @PatchMapping("/{id:\\d+}/read")
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

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    private Long parsePeerId(String peerUserId) {
        if (peerUserId != null && peerUserId.startsWith("u-")) {
            return Long.parseLong(peerUserId.substring(2));
        }
        return Long.parseLong(peerUserId);
    }

    private MessageItemResponse toMessageItem(Message message, Long currentUserId) {
        String from = message.getSender().getId().equals(currentUserId) ? "me" : "peer";
        return new MessageItemResponse(
                String.valueOf(message.getId()),
                from,
                message.getContent(),
                message.getCreatedAt() == null ? "" : message.getCreatedAt().toString()
        );
    }

    private String displayName(User user) {
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
