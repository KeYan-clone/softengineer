package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDTO {
    
    @NotNull(message = "Receiver ID is required")
    private String receiverId;
    
    @NotBlank(message = "Message content is required")
    @Size(min = 1, max = 5000, message = "Content must be between 1 and 5000 characters")
    private String content;
    
    private List<String> attachments = new ArrayList<>();
}
