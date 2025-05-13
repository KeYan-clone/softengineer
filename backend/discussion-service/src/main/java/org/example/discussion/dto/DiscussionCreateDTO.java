package org.example.discussion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for creating a new discussion
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionCreateDTO {
    
    @NotBlank(message = "Discussion title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;
    
    @NotBlank(message = "Discussion content is required")
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;
    
    private List<String> tags = new ArrayList<>();
    
    private List<String> attachments = new ArrayList<>();
    
    // Optional: If the discussion is related to an experiment
    private String experimentId;
}
