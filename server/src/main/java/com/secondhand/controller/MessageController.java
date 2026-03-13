package com.secondhand.controller;

import com.secondhand.dto.ConversationMessageRequest;
import com.secondhand.dto.ConversationResponse;
import com.secondhand.dto.MessageDetailResponse;
import com.secondhand.dto.MessageItemResponse;
import com.secondhand.entity.Message;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.service.MessageService;
import com.secondhand.service.ProductService;
import com.secondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MessageDetailResponse> getMessage(@PathVariable Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Message message = messageService.getMessageById(id);
        if (!canAccessMessage(message, currentUser)) {
            throw new IllegalArgumentException("无权查看该消息");
        }
        return ResponseEntity.ok(toMessageDetail(message));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDetailResponse>> getUserConversations(@PathVariable Long userId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        ensureCurrentUserOrAdmin(currentUser, userId);
        return ResponseEntity.ok(messageService.getUserConversations(userId)
                .stream()
                .map(this::toMessageDetail)
                .collect(Collectors.toList()));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDetailResponse>> getConversationBetweenUsers(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        boolean canView = isAdmin(currentUser)
                || currentUser.getId().equals(user1Id)
                || currentUser.getId().equals(user2Id);
        if (!canView) {
            throw new IllegalArgumentException("无权查看该会话");
        }
        return ResponseEntity.ok(messageService.getConversationBetweenUsers(user1Id, user2Id)
                .stream()
                .map(this::toMessageDetail)
                .collect(Collectors.toList()));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MessageDetailResponse>> getMessagesByProduct(@PathVariable Long productId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        if (!isAdmin(currentUser)) {
            throw new IllegalArgumentException("仅管理员可查看商品消息汇总");
        }
        return ResponseEntity.ok(messageService.getMessagesByProduct(productId)
                .stream()
                .map(this::toMessageDetail)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/{id:\\d+}/read")
    public ResponseEntity<MessageDetailResponse> markMessageAsRead(@PathVariable Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Message message = messageService.getMessageById(id);
        if (!canAccessMessage(message, currentUser)) {
            throw new IllegalArgumentException("无权操作该消息");
        }
        return ResponseEntity.ok(toMessageDetail(messageService.markMessageAsRead(id)));
    }

    @PatchMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllMessagesAsRead(@PathVariable Long userId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        ensureCurrentUserOrAdmin(currentUser, userId);
        messageService.markAllMessagesAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable Long userId, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        ensureCurrentUserOrAdmin(currentUser, userId);
        return ResponseEntity.ok(messageService.getUnreadMessageCount(userId));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id, Principal principal) {
        User currentUser = userService.getUserByUsername(principal.getName());
        Message message = messageService.getMessageById(id);
        if (!canAccessMessage(message, currentUser)) {
            throw new IllegalArgumentException("无权删除该消息");
        }
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }

    private void ensureCurrentUserOrAdmin(User currentUser, Long targetUserId) {
        if (currentUser.getId().equals(targetUserId) || isAdmin(currentUser)) {
            return;
        }
        throw new IllegalArgumentException("无权访问该用户消息");
    }

    private boolean canAccessMessage(Message message, User currentUser) {
        return isAdmin(currentUser)
                || message.getSender().getId().equals(currentUser.getId())
                || message.getReceiver().getId().equals(currentUser.getId());
    }

    private boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
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

    private MessageDetailResponse toMessageDetail(Message message) {
        return new MessageDetailResponse(
                String.valueOf(message.getId()),
                message.getSender().getId(),
                displayName(message.getSender()),
                message.getReceiver().getId(),
                displayName(message.getReceiver()),
                message.getProduct() == null ? null : message.getProduct().getId(),
                message.getProduct() == null ? "" : message.getProduct().getName(),
                message.getContent(),
                message.isRead(),
                message.getCreatedAt() == null ? "" : message.getCreatedAt().toString()
        );
    }

    private String displayName(User user) {
        if (user == null) {
            return "未知用户";
        }
        if (user.getName() != null && !user.getName().trim().isEmpty()) {
            return user.getName();
        }
        return user.getUsername();
    }
}
