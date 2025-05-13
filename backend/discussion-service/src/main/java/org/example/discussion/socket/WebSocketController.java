package org.example.discussion.socket;

import lombok.RequiredArgsConstructor;
import org.example.discussion.dto.MessageCreateDTO;
import org.example.discussion.dto.MessageDTO;
import org.example.discussion.dto.UserDTO;
import org.example.discussion.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

/**
 * Controller for WebSocket messaging
 */
@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final MessageService messageService;

    /**
     * Handle private message sent through WebSocket
     *
     * @param createDTO Message creation data
     * @param headerAccessor Header accessor for session attributes
     * @return Created message
     */
    @MessageMapping("/private-message")
    public MessageDTO sendPrivateMessage(
            @Payload MessageCreateDTO createDTO, 
            SimpMessageHeaderAccessor headerAccessor) {
        // Get user from session
        UserDTO user = (UserDTO) headerAccessor.getSessionAttributes().get("user");
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        return messageService.sendMessage(createDTO, user);
    }

    /**
     * Handle user joined notification
     *
     * @param headerAccessor Header accessor for session attributes
     */
    @MessageMapping("/user-join")
    public void userJoined(SimpMessageHeaderAccessor headerAccessor) {
        // Store user in WebSocket session for later use
        UserDTO user = (UserDTO) headerAccessor.getSessionAttributes().get("user");
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        
        // You can broadcast user joined notification if needed
    }
}
