package org.example.discussion.service;

import lombok.RequiredArgsConstructor;
import org.example.discussion.domain.Message;
import org.example.discussion.dto.MessageCreateDTO;
import org.example.discussion.dto.MessageDTO;
import org.example.discussion.dto.UserDTO;
import org.example.discussion.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Service for message-related operations
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * Send a private message
     *
     * @param createDTO Message creation data
     * @param user Current user
     * @return Created message
     */
    public MessageDTO sendMessage(MessageCreateDTO createDTO, UserDTO user) {
        Message message = Message.builder()
                .senderId(user.getId())
                .receiverId(createDTO.getReceiverId())
                .content(createDTO.getContent())
                .attachments(createDTO.getAttachments())
                .createTime(new Date())
                .isRead(false)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        MessageDTO messageDTO = convertToDTO(savedMessage, user.getUsername(), null);
        
        // Send message via WebSocket
        messagingTemplate.convertAndSendToUser(
                createDTO.getReceiverId(),
                "/queue/messages",
                messageDTO
        );
        
        return messageDTO;
    }
    
    /**
     * Get messages for the current user
     *
     * @param user Current user
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of messages
     */
    public Page<MessageDTO> getUserMessages(UserDTO user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        return messageRepository.findBySenderIdOrReceiverIdAndIsDeletedFalseOrderByCreateTimeDesc(
                user.getId(), pageable)
                .map(message -> convertToDTO(message, null, null));
    }
    
    /**
     * Get conversation between two users
     *
     * @param user Current user
     * @param otherUserId Other user ID
     * @param otherUserName Other user name
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of messages
     */
    public Page<MessageDTO> getConversation(UserDTO user, String otherUserId, String otherUserName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").ascending());
        Page<Message> messages = messageRepository.findConversation(user.getId(), otherUserId, pageable);
        
        // Mark messages from the other user as read
        messages.getContent().stream()
                .filter(message -> message.getReceiverId().equals(user.getId()) && !message.isRead())
                .forEach(message -> {
                    message.setRead(true);
                    messageRepository.save(message);
                });
        
        return messages.map(message -> {
            String senderName = message.getSenderId().equals(user.getId()) ? user.getUsername() : otherUserName;
            String receiverName = message.getReceiverId().equals(user.getId()) ? user.getUsername() : otherUserName;
            return convertToDTO(message, senderName, receiverName);
        });
    }
    
    /**
     * Mark message as read
     *
     * @param messageId Message ID
     * @param user Current user
     * @return Updated message
     * @throws RuntimeException If message not found or user not authorized
     */
    public MessageDTO markAsRead(String messageId, UserDTO user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        // Check if user is the receiver
        if (!message.getReceiverId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to update this message");
        }
        
        message.setRead(true);
        Message updatedMessage = messageRepository.save(message);
        
        return convertToDTO(updatedMessage, null, null);
    }
    
    /**
     * Delete message (soft delete)
     *
     * @param messageId Message ID
     * @param user Current user
     * @throws RuntimeException If message not found or user not authorized
     */
    public void deleteMessage(String messageId, UserDTO user) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        // Check if user is the sender or receiver
        if (!message.getSenderId().equals(user.getId()) && !message.getReceiverId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to delete this message");
        }
        
        message.setDeleted(true);
        messageRepository.save(message);
    }
    
    /**
     * Count unread messages for a user
     *
     * @param userId User ID
     * @return Number of unread messages
     */
    public int countUnreadMessages(String userId) {
        return messageRepository.countByReceiverIdAndIsReadFalseAndIsDeletedFalse(userId);
    }
    
    /**
     * Convert Message entity to DTO
     *
     * @param message Message entity
     * @param senderName Sender name (optional)
     * @param receiverName Receiver name (optional)
     * @return Message DTO
     */
    private MessageDTO convertToDTO(Message message, String senderName, String receiverName) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .senderName(senderName)
                .receiverId(message.getReceiverId())
                .receiverName(receiverName)
                .content(message.getContent())
                .attachments(message.getAttachments())
                .createTime(message.getCreateTime())
                .isRead(message.isRead())
                .build();
    }
}
