package org.example.backend.core.repository;

import org.example.backend.core.domain.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Discussion repository interface
 * Provides data access operations for Discussion entities
 */
@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, String> {
    
    /**
     * Find discussion by ID where deleted is false
     * 
     * @param id discussion ID
     * @return optional of discussion
     */
    Optional<Discussion> findByIdAndDeletedFalse(String id);
      /**
     * Find discussions by creator ID
     * 
     * @param userId creator ID
     * @return list of discussions
     */
    List<Discussion> findByUserId(String userId);
    
    /**
     * Find discussions by creator ID with pagination
     * 
     * @param userId creator ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByUserId(String userId, Pageable pageable);
    
    /**
     * Find non-deleted discussions by creator ID with pagination
     * 
     * @param userId creator ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByUserIdAndDeletedFalse(String userId, Pageable pageable);
    
    /**
     * Find discussions by related experiment ID
     * 
     * @param experimentId experiment ID
     * @return list of discussions
     */
    List<Discussion> findByExperimentId(String experimentId);
      /**
     * Find discussions by related experiment ID with pagination
     * 
     * @param experimentId experiment ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByExperimentId(String experimentId, Pageable pageable);
    
    /**
     * Find non-deleted discussions by experiment ID with pagination
     * 
     * @param experimentId experiment ID
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByExperimentIdAndDeletedFalse(String experimentId, Pageable pageable);
    
    /**
     * Find non-deleted discussions
     * 
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByDeletedFalse(Pageable pageable);
    
    /**
     * Find non-deleted discussions with any of the given tags
     * 
     * @param tags list of tags to match
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByTagsInAndDeletedFalse(List<String> tags, Pageable pageable);
    
    /**
     * Find non-deleted discussions with title or content containing the given keyword
     * 
     * @param title keyword to search in title
     * @param content keyword to search in content
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByTitleContainingOrContentContainingAndDeletedFalse(String title, String content, Pageable pageable);
    
    /**
     * Find discussions by title containing the given text
     * 
     * @param title title text to search
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByTitleContaining(String title, Pageable pageable);
    
    /**
     * Find discussions by content containing the given text
     * 
     * @param content content text to search
     * @param pageable pagination info
     * @return page of discussions
     */
    Page<Discussion> findByContentContaining(String content, Pageable pageable);
}
