package org.example.backend.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.backend.common.exception.BusinessException;
import org.example.backend.core.domain.Discussion;
import org.example.backend.core.repository.DiscussionRepository;
import org.example.backend.core.service.DiscussionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Discussion service implementation
 */
@Service
@RequiredArgsConstructor
public class DiscussionServiceImpl implements DiscussionService {
    
    private final DiscussionRepository discussionRepository;
    
    @Override
    @Transactional
    public Discussion createDiscussion(Discussion discussion) {
        // Set creation and update time
        LocalDateTime now = LocalDateTime.now();
        discussion.setCreateTime(now);
        discussion.setUpdateTime(now);
        
        // Initialize view count if not set
        if (discussion.getViewCount() == null) {
            discussion.setViewCount(0);
        }
        
        return discussionRepository.save(discussion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discussion> findById(String id) {
        return discussionRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discussion> findByUserId(String userId) {
        return discussionRepository.findByUserId(userId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findByUserId(String userId, Pageable pageable) {
        return discussionRepository.findByUserId(userId, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discussion> findByExperimentId(String experimentId) {
        return discussionRepository.findByExperimentId(experimentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> findByExperimentId(String experimentId, Pageable pageable) {
        return discussionRepository.findByExperimentId(experimentId, pageable);
    }
    
    @Override
    @Transactional
    public Discussion updateDiscussion(Discussion discussion) {
        // Check if discussion exists
        Discussion existingDiscussion = discussionRepository.findById(discussion.getId())
                .orElseThrow(() -> new BusinessException("Discussion not found"));
        
        // Set update time
        discussion.setUpdateTime(LocalDateTime.now());
        // Preserve creation time
        discussion.setCreateTime(existingDiscussion.getCreateTime());
        // Preserve view count
        discussion.setViewCount(existingDiscussion.getViewCount());
        
        return discussionRepository.save(discussion);
    }
    
    @Override
    @Transactional
    public void deleteDiscussion(String id) {
        discussionRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public Discussion incrementViewCount(String id) {
        // Find discussion
        Discussion discussion = discussionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Discussion not found"));
        
        // Increment view count
        discussion.setViewCount(discussion.getViewCount() + 1);
        
        return discussionRepository.save(discussion);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> searchByTitle(String title, Pageable pageable) {
        return discussionRepository.findByTitleContaining(title, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discussion> searchByContent(String content, Pageable pageable) {
        return discussionRepository.findByContentContaining(content, pageable);
    }
}
