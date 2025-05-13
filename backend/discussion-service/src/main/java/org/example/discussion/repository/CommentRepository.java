package org.example.discussion.repository;

import org.example.discussion.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Comment entity
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    /**
     * Find comments by discussion ID
     *
     * @param discussionId Discussion ID
     * @param pageable Pagination information
     * @return Page of comments
     */
    Page<Comment> findByDiscussionIdAndIsDeletedFalseAndParentIdIsNullOrderByCreateTimeDesc(
            String discussionId, Pageable pageable);
    
    /**
     * Find replies to a comment
     *
     * @param parentId Parent comment ID
     * @return List of replies
     */
    List<Comment> findByParentIdAndIsDeletedFalseOrderByCreateTimeAsc(String parentId);
    
    /**
     * Count comments for a discussion
     *
     * @param discussionId Discussion ID
     * @return Number of comments
     */
    int countByDiscussionIdAndIsDeletedFalse(String discussionId);
    
    /**
     * Find comments by user ID
     *
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of comments
     */
    Page<Comment> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(String userId, Pageable pageable);
}
