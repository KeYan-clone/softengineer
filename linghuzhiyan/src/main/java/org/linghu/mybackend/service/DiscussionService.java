package org.linghu.mybackend.service;

import org.linghu.mybackend.domain.Discussion;
import org.linghu.mybackend.dto.DiscussionRequestDTO;
import org.linghu.mybackend.dto.DiscussionResponseDTO;
import org.linghu.mybackend.dto.PriorityRequestDTO;
import org.linghu.mybackend.dto.ReviewRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscussionService {
    
    DiscussionResponseDTO createDiscussion(DiscussionRequestDTO requestDTO, String userId);
    
    Page<DiscussionResponseDTO> getDiscussions(
            String[] tags, 
            String experimentId, 
            String userId, 
            String status, 
            String keyword, 
            String sortBy, 
            String order, 
            int page, 
            int size,
            String currentUserId);
    
    DiscussionResponseDTO getDiscussionById(String id, String currentUserId);
    
    DiscussionResponseDTO updateDiscussion(String id, DiscussionRequestDTO requestDTO, String userId);
    
    void deleteDiscussion(String id, String userId);
    
    DiscussionResponseDTO reviewDiscussion(String id, ReviewRequestDTO requestDTO, String reviewerId);
    
    DiscussionResponseDTO updatePriority(String id, PriorityRequestDTO requestDTO, String userId);
    
    DiscussionResponseDTO toggleLike(String id, String userId);
    
    void incrementViewCount(String id);
}
