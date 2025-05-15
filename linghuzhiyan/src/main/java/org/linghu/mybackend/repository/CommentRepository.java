package org.linghu.mybackend.repository;

import org.linghu.mybackend.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    
    @Query("{'discussionId': ?0, 'parentId': null, 'deleted': false}")
    Page<Comment> findRootCommentsByDiscussionId(String discussionId, Pageable pageable);
    
    @Query("{'discussionId': ?0, 'deleted': false}")
    Page<Comment> findByDiscussionId(String discussionId, Pageable pageable);
    
    @Query("{'parentId': ?0, 'deleted': false}")
    Page<Comment> findByParentId(String parentId, Pageable pageable);
    
    @Query("{'rootId': ?0, 'deleted': false}")
    List<Comment> findByRootId(String rootId);
    
    @Query("{'_id': ?0, 'deleted': false}")
    Optional<Comment> findByIdAndNotDeleted(String id);
    
    @Query("{'userId': ?0, 'deleted': false}")
    Page<Comment> findByUserId(String userId, Pageable pageable);
    
    @Query(value = "{'$text': {'$search': ?0}, 'discussionId': ?1, 'deleted': false}")
    Page<Comment> searchByKeywordInDiscussion(String keyword, String discussionId, Pageable pageable);
    
    long countByDiscussionIdAndDeletedFalse(String discussionId);
}
