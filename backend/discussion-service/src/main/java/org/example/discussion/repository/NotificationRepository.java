package org.example.discussion.repository;

import org.example.discussion.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Notification entity
 */
@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    /**
     * Find notifications for a user
     *
     * @param userId User ID
     * @param pageable Pagination information
     * @return Page of notifications
     */
    Page<Notification> findByUserIdAndIsDeletedFalseOrderByCreateTimeDesc(String userId, Pageable pageable);
    
    /**
     * Count unread notifications for a user
     *
     * @param userId User ID
     * @return Number of unread notifications
     */
    int countByUserIdAndIsReadFalseAndIsDeletedFalse(String userId);
}
