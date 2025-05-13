package org.example.discussion.repository;

import org.example.discussion.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Message entity
 */
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    /**
     * Find messages for a user (sent or received)
     *
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of messages
     */
    @Query("{ '$or': [ { 'senderId': ?0 }, { 'receiverId': ?0 } ], 'isDeleted': false }")
    Page<Message> findBySenderIdOrReceiverIdAndIsDeletedFalseOrderByCreateTimeDesc(String userId, Pageable pageable);
    
    /**
     * Find conversation between two users
     *
     * @param userId1 First user ID
     * @param userId2 Second user ID
     * @param pageable Pagination information
     * @return Page of messages
     */
    @Query("{ '$or': [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ], 'isDeleted': false }")
    Page<Message> findConversation(String userId1, String userId2, Pageable pageable);
    
    /**
     * Count unread messages for a user
     *
     * @param receiverId Receiver ID
     * @return Number of unread messages
     */
    int countByReceiverIdAndIsReadFalseAndIsDeletedFalse(String receiverId);
}
