package org.linghu.mybackend.repository;

import org.linghu.mybackend.domain.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscussionRepository extends MongoRepository<Discussion, String> {
    
    @Query("{'deleted': false}")
    Page<Discussion> findAllNonDeleted(Pageable pageable);
    
    @Query("{'status': ?0, 'deleted': false}")
    Page<Discussion> findByStatus(String status, Pageable pageable);
    
    @Query("{'userId': ?0, 'deleted': false}")
    Page<Discussion> findByUserId(String userId, Pageable pageable);
    
    @Query("{'experimentId': ?0, 'deleted': false}")
    Page<Discussion> findByExperimentId(String experimentId, Pageable pageable);
    
    @Query("{'tags': {$in: ?0}, 'deleted': false}")
    Page<Discussion> findByTagsIn(List<String> tags, Pageable pageable);
    
    @Query("{'status': ?0, 'tags': {$in: ?1}, 'deleted': false}")
    Page<Discussion> findByStatusAndTagsIn(String status, List<String> tags, Pageable pageable);
    
    @Query("{'_id': ?0, 'deleted': false}")
    Optional<Discussion> findByIdAndNotDeleted(String id);
    
    @Query(value = "{'$text': {'$search': ?0}, 'status': 'APPROVED', 'deleted': false}")
    Page<Discussion> searchByKeyword(String keyword, Pageable pageable);
    
    @Query("{'status': ?0, 'experimentId': ?1, 'deleted': false}")
    Page<Discussion> findByStatusAndExperimentId(String status, String experimentId, Pageable pageable);
    
    @Query("{'status': ?0, 'userId': ?1, 'deleted': false}")
    Page<Discussion> findByStatusAndUserId(String status, String userId, Pageable pageable);
}
