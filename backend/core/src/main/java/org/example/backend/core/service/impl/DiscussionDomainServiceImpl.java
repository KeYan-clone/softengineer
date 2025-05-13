package org.example.backend.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.core.domain.Comment;
import org.example.backend.core.domain.Discussion;
import org.example.backend.core.repository.DiscussionRepository;
import org.example.backend.core.service.DiscussionDomainService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of DiscussionDomainService
 * Handles core domain operations for discussion entities
 */
@Service
@RequiredArgsConstructor
public class DiscussionDomainServiceImpl implements DiscussionDomainService {

    private final DiscussionRepository discussionRepository;
    
    @Override
    @Transactional
    public Discussion createDiscussion(Discussion discussion) {
        // Set creation and update time
        LocalDateTime now = LocalDateTime.now();
        discussion.setCreateTime(now);
        discussion.setUpdateTime(now);
        discussion.setDeleted(false);
        
        return discussionRepository.save(discussion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discussion> findById(String id) {
        return discussionRepository.findByIdAndDeletedFalse(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findByUserId(String userId, Pageable pageable) {
        return discussionRepository.findByUserIdAndDeletedFalse(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findByExperimentId(String experimentId, Pageable pageable) {
        return discussionRepository.findByExperimentIdAndDeletedFalse(experimentId, pageable);
    }
    
    @Override
    @Transactional
    public Discussion updateDiscussion(Discussion discussion) {
        discussion.setUpdateTime(LocalDateTime.now());
        return discussionRepository.save(discussion);
    }
    
    @Override
    @Transactional
    public void deleteDiscussion(String id) {
        discussionRepository.findById(id).ifPresent(discussion -> {
            discussion.setDeleted(true);
            discussion.setUpdateTime(LocalDateTime.now());
            discussionRepository.save(discussion);
        });
    }
    
    @Override
    @Transactional
    public Discussion addComment(String discussionId, Comment comment) {
        return discussionRepository.findById(discussionId)
                .map(discussion -> {
                    comment.setCreateTime(LocalDateTime.now());
                    comment.setDeleted(false);
                    
                    if (discussion.getComments() == null) {
                        discussion.initComments();
                    }
                    
                    discussion.getComments().add(comment);
                    discussion.setUpdateTime(LocalDateTime.now());
                    discussion.setCommentCount(discussion.getComments().size());
                    
                    return discussionRepository.save(discussion);
                })
                .orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findRecent(Pageable pageable) {
        return discussionRepository.findByDeletedFalse(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findByTags(List<String> tags, Pageable pageable) {
        return discussionRepository.findByTagsInAndDeletedFalse(tags, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> searchByKeyword(String keyword, Pageable pageable) {
        return discussionRepository.findByTitleContainingOrContentContainingAndDeletedFalse(
                keyword, keyword, pageable);
    }
}
