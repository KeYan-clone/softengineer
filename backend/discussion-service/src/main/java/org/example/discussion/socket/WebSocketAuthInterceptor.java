package org.example.discussion.socket;

import lombok.RequiredArgsConstructor;
import org.example.discussion.config.JwtService;
import org.example.discussion.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket interceptor for authentication
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);
    private final JwtService jwtService;

    /**
     * Configure client inbound channel to add authentication interceptor
     *
     * @param registration Channel registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");
                    logger.debug("Processing WebSocket connection with Authorization: {}", token);
                    
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        if (jwtService.validateToken(token)) {
                            UserDTO user = jwtService.extractUser(token);
                            accessor.setUser(() -> user.getId());
                            accessor.getSessionAttributes().put("user", user);
                            logger.debug("WebSocket authenticated user: {}", user.getUsername());
                        } else {
                            logger.warn("Invalid JWT token in WebSocket connection");
                        }
                    } else {
                        logger.warn("No JWT token found in WebSocket connection");
                    }
                }
                
                return message;
            }
        });
    }
}
