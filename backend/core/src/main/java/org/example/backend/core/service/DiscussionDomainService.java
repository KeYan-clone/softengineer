package org.example.backend.core.service;

import org.example.backend.core.domain.Discussion;
import org.example.backend.core.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Discussion Domain Service interface
 * Defines core domain operations related to discussions,
 * focusing on data operations rather than business logic
 */
public interface DiscussionDomainService {
    
    /**
     * Create a new discussion
     * 
     * @param discussion discussion to create
     * @return created discussion
     */
    Discussion createDiscussion(Discussion discussion);
    
    /**
     * Find discussion by ID
     * 
     * @param id discussion ID
     * @return discussion optional
     */
    Optional<Discussion> findById(String id);
    
    /**
     * Find discussions by user ID
     * 
     * @param userId user ID
     * @param pageable pagination information
     * @return page of discussions
     */
    Page<Discussion> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find discussions by experiment ID
     * 
     * @param experimentId experiment ID
     * @param pageable pagination information
     * @return page of discussions
     */
    Page<Discussion> findByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * Update a discussion
     * 
     * @param discussion discussion to update
     * @return updated discussion
     */
    Discussion updateDiscussion(Discussion discussion);
    
    /**
     * Delete a discussion by ID
     * 
     * @param id discussion ID
     */
    void deleteDiscussion(String id);
    
    /**
     * Add a comment to a discussion
     * 
     * @param discussionId discussion ID
     * @param comment comment to add
     * @return updated discussion
     */
    Discussion addComment(String discussionId, Comment comment);
    
    /**
     * Find recent discussions
     * 
     * @param pageable pagination information
     * @return page of discussions
     */
    Page<Discussion> findRecent(Pageable pageable);
    
    /**
     * Find discussions by tags
     * 
     * @param tags tags to search for
     * @param pageable pagination information
     * @return page of discussions
     */
    Page<Discussion> findByTags(List<String> tags, Pageable pageable);
    
    /**
     * Search discussions by keyword
     * 
     * @param keyword keyword to search for
     * @param pageable pagination information
     * @return page of discussions
     */
    Page<Discussion> searchByKeyword(String keyword, Pageable pageable);
}
