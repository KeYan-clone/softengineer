package org.example.backend.core.service;

import org.example.backend.core.domain.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Discussion service interface
 * Defines core business operations related to discussions
 */
public interface DiscussionService {
    
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
     * @return list of discussions
     */
    List<Discussion> findByUserId(String userId);
    
    /**
     * Find discussions by user ID with pagination
     * 
     * @param userId user ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find discussions by experiment ID
     * 
     * @param experimentId experiment ID
     * @return list of discussions
     */
    List<Discussion> findByExperimentId(String experimentId);
    
    /**
     * Find discussions by experiment ID with pagination
     * 
     * @param experimentId experiment ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * Update discussion
     * 
     * @param discussion discussion to update
     * @return updated discussion
     */
    Discussion updateDiscussion(Discussion discussion);
    
    /**
     * Delete discussion by ID
     * 
     * @param id discussion ID
     */
    void deleteDiscussion(String id);
    
    /**
     * Increment view count
     * 
     * @param id discussion ID
     * @return updated discussion
     */
    Discussion incrementViewCount(String id);
    
    /**
     * Search discussions by title
     * 
     * @param title title to search
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> searchByTitle(String title, Pageable pageable);
    
    /**
     * Search discussions by content
     * 
     * @param content content to search
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> searchByContent(String content, Pageable pageable);
}
