package org.example.discussion.repository;

import org.example.discussion.domain.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Discussion entity
 */
@Repository
public interface DiscussionRepository extends MongoRepository<Discussion, String> {

    /**
     * Find discussions by user ID
     *
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of discussions
     */
    Page<Discussion> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(String userId, Pageable pageable);
    
    /**
     * Find discussions by experiment ID
     *
     * @param experimentId Experiment ID
     * @param pageable Pagination information
     * @return Page of discussions
     */
    Page<Discussion> findByExperimentIdAndIsDeletedFalseOrderByCreateTimeDesc(String experimentId, Pageable pageable);
    
    /**
     * Find discussions by tag
     *
     * @param tag Tag to search for
     * @param pageable Pagination information
     * @return Page of discussions
     */
    Page<Discussion> findByTagsContainingAndIsDeletedFalseOrderByCreateTimeDesc(String tag, Pageable pageable);
    
    /**
     * Find discussions by title or content containing keyword
     *
     * @param keyword Keyword to search for
     * @param pageable Pagination information
     * @return Page of discussions
     */
    Page<Discussion> findByTitleContainingOrContentContainingAndIsDeletedFalseOrderByCreateTimeDesc(
            String title, String content, Pageable pageable);
            
    /**
     * Find non-deleted discussion by ID
     *
     * @param id Discussion ID
     * @return Optional of Discussion
     */
    Optional<Discussion> findByIdAndIsDeletedFalse(String id);
    
    /**
     * Find recent discussions
     *
     * @param pageable Pagination information
     * @return Page of discussions
     */
    Page<Discussion> findByIsDeletedFalseOrderByCreateTimeDesc(Pageable pageable);
}
