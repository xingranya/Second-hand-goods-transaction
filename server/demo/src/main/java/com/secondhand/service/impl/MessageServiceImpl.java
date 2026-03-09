package com.secondhand.service.impl;

import com.secondhand.entity.Message;
import com.secondhand.repository.MessageRepository;
import com.secondhand.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @Transactional
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message not found with id: " + id));
    }

    @Override
    public List<Message> getUserConversations(Long userId) {
        return messageRepository.findUserConversations(userId);
    }

    @Override
    public List<Message> getConversationBetweenUsers(Long user1Id, Long user2Id) {
        return messageRepository.findConversationBetweenUsers(user1Id, user2Id);
    }

    @Override
    public List<Message> getMessagesByProduct(Long productId) {
        return messageRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public Message markMessageAsRead(Long messageId) {
        Message message = getMessageById(messageId);
        message.setRead(true);
        return messageRepository.save(message);
    }

    @Override
    @Transactional
    public void markAllMessagesAsRead(Long userId) {
        List<Message> unreadMessages = messageRepository.findByReceiverId(userId);
        unreadMessages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    @Override
    public long getUnreadMessageCount(Long userId) {
        return messageRepository.countUnreadMessages(userId);
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = getMessageById(messageId);
        messageRepository.delete(message);
    }
} 