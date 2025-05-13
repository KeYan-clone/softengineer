package org.example.discussion.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.backend.common.exception.DiscussionException;
import org.example.backend.core.domain.Discussion;
import org.example.backend.core.domain.Comment;
import org.example.backend.core.service.DiscussionDomainService;
import org.example.discussion.dto.CommentCreateDTO;
import org.example.discussion.dto.CommentDTO;
import org.example.discussion.dto.DiscussionCreateDTO;
import org.example.discussion.dto.DiscussionDTO;
import org.example.discussion.dto.UserDTO;
import org.example.discussion.service.DiscussionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 讨论服务实现类
 * 实现业务层面的讨论操作，依赖于DiscussionDomainService处理核心领域操作
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionDomainService discussionDomainService;
    
    @Override
    @Transactional
    public DiscussionDTO createDiscussion(DiscussionCreateDTO createDTO, UserDTO currentUser) {
        log.debug("Creating discussion: {}", createDTO.getTitle());
        
        // 验证讨论内容
        if (createDTO.getTitle() == null || createDTO.getTitle().trim().isEmpty()) {
            throw DiscussionException.invalidContent("标题不能为空");
        }
        
        if (createDTO.getContent() == null || createDTO.getContent().trim().isEmpty()) {
            throw DiscussionException.invalidContent("内容不能为空");
        }
        
        // 创建讨论实体
        Discussion discussion = new Discussion();
        discussion.setTitle(createDTO.getTitle());
        discussion.setContent(createDTO.getContent());
        discussion.setUserId(currentUser.getId());
        discussion.setUsername(currentUser.getUsername());
        discussion.setExperimentId(createDTO.getExperimentId());
        discussion.setTags(createDTO.getTags());
        discussion.setCommentCount(0);
        discussion.setViewCount(0);
        discussion.setLikeCount(0);
        discussion.setDeleted(false);
        
        // 使用领域服务保存讨论
        Discussion savedDiscussion = discussionDomainService.createDiscussion(discussion);
        
        return convertToDTO(savedDiscussion, currentUser);
    }
    
    @Override
    public DiscussionDTO getDiscussion(String id, UserDTO currentUser) {
        log.debug("Getting discussion with id: {}", id);
        
        Discussion discussion = discussionDomainService.findById(id)
                .orElseThrow(() -> DiscussionException.discussionNotFound(id));
        
        // 增加查看次数
        discussion.setViewCount(discussion.getViewCount() + 1);
        discussionDomainService.updateDiscussion(discussion);
        
        return convertToDTO(discussion, currentUser);
    }
    
    @Override
    public Page<DiscussionDTO> getDiscussionsByUser(String userId, int page, int size, UserDTO currentUser) {
        log.debug("Getting discussions for user: {}", userId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Discussion> discussions = discussionDomainService.findByUserId(userId, pageable);
        
        return discussions.map(discussion -> convertToDTO(discussion, currentUser));
    }
    
    @Override
    public Page<DiscussionDTO> getDiscussionsByExperiment(String experimentId, int page, int size, UserDTO currentUser) {
        log.debug("Getting discussions for experiment: {}", experimentId);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Discussion> discussions = discussionDomainService.findByExperimentId(experimentId, pageable);
        
        return discussions.map(discussion -> convertToDTO(discussion, currentUser));
    }
    
    @Override
    @Transactional
    public DiscussionDTO updateDiscussion(String id, DiscussionCreateDTO updateDTO, UserDTO currentUser) {
        log.debug("Updating discussion with id: {}", id);
        
        Discussion discussion = discussionDomainService.findById(id)
                .orElseThrow(() -> DiscussionException.discussionNotFound(id));
        
        // 检查权限
        if (!discussion.getUserId().equals(currentUser.getId())) {
            throw DiscussionException.unauthorizedAccess(currentUser.getId(), "Discussion " + id);
        }
        
        // 更新讨论内容
        discussion.setTitle(updateDTO.getTitle());
        discussion.setContent(updateDTO.getContent());
        discussion.setTags(updateDTO.getTags());
        
        Discussion updatedDiscussion = discussionDomainService.updateDiscussion(discussion);
        
        return convertToDTO(updatedDiscussion, currentUser);
    }
    
    @Override
    @Transactional
    public void deleteDiscussion(String id, UserDTO currentUser) {
        log.debug("Deleting discussion with id: {}", id);
        
        Discussion discussion = discussionDomainService.findById(id)
                .orElseThrow(() -> DiscussionException.discussionNotFound(id));
        
        // 检查权限
        if (!discussion.getUserId().equals(currentUser.getId())) {
            throw DiscussionException.unauthorizedAccess(currentUser.getId(), "Discussion " + id);
        }
        
        discussionDomainService.deleteDiscussion(id);
    }
    
    @Override
    @Transactional
    public DiscussionDTO addComment(String discussionId, CommentCreateDTO commentDTO, UserDTO currentUser) {
        log.debug("Adding comment to discussion: {}", discussionId);
        
        // 验证评论内容
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            throw DiscussionException.invalidContent("评论内容不能为空");
        }
        
        Discussion discussion = discussionDomainService.findById(discussionId)
                .orElseThrow(() -> DiscussionException.discussionNotFound(discussionId));
        
        // 创建评论
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUserId(currentUser.getId());
        comment.setUsername(currentUser.getUsername());
        comment.setReplyToId(commentDTO.getReplyToId());
        comment.setReplyToUsername(commentDTO.getReplyToUsername());
        
        // 添加评论
        Discussion updatedDiscussion = discussionDomainService.addComment(discussionId, comment);
        
        return convertToDTO(updatedDiscussion, currentUser);
    }
    
    @Override
    public Page<DiscussionDTO> getRecentDiscussions(int page, int size, UserDTO currentUser) {
        log.debug("Getting recent discussions");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Discussion> discussions = discussionDomainService.findRecent(pageable);
        
        return discussions.map(discussion -> convertToDTO(discussion, currentUser));
    }
    
    @Override
    public Page<DiscussionDTO> searchDiscussions(String keyword, int page, int size, UserDTO currentUser) {
        log.debug("Searching discussions with keyword: {}", keyword);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Discussion> discussions = discussionDomainService.searchByKeyword(keyword, pageable);
        
        return discussions.map(discussion -> convertToDTO(discussion, currentUser));
    }
    
    @Override
    public Page<DiscussionDTO> getDiscussionsByTags(List<String> tags, int page, int size, UserDTO currentUser) {
        log.debug("Getting discussions with tags: {}", tags);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Discussion> discussions = discussionDomainService.findByTags(tags, pageable);
        
        return discussions.map(discussion -> convertToDTO(discussion, currentUser));
    }
    
    /**
     * 将Discussion实体转换为DTO
     */
    private DiscussionDTO convertToDTO(Discussion discussion, UserDTO currentUser) {
        DiscussionDTO dto = new DiscussionDTO();
        dto.setId(discussion.getId());
        dto.setTitle(discussion.getTitle());
        dto.setContent(discussion.getContent());
        dto.setUserId(discussion.getUserId());
        dto.setUsername(discussion.getUsername());
        dto.setExperimentId(discussion.getExperimentId());
        dto.setTags(discussion.getTags());
        dto.setCommentCount(discussion.getCommentCount());
        dto.setViewCount(discussion.getViewCount());
        dto.setLikeCount(discussion.getLikeCount());
        dto.setCreateTime(discussion.getCreateTime());
        dto.setUpdateTime(discussion.getUpdateTime());
        
        // 设置当前用户对讨论的交互状态
        dto.setOwner(currentUser != null && currentUser.getId().equals(discussion.getUserId()));
        dto.setLiked(false); // 这里需要从点赞记录中获取
        
        // 处理评论
        if (discussion.getComments() != null) {
            List<CommentDTO> commentDTOs = discussion.getComments().stream()
                    .filter(comment -> !comment.isDeleted())
                    .map(this::convertToCommentDTO)
                    .collect(Collectors.toList());
            dto.setComments(commentDTOs);
        } else {
            dto.setComments(Collections.emptyList());
        }
        
        return dto;
    }
    
    /**
     * 将Comment实体转换为DTO
     */
    private CommentDTO convertToCommentDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUserId());
        dto.setUsername(comment.getUsername());
        dto.setReplyToId(comment.getReplyToId());
        dto.setReplyToUsername(comment.getReplyToUsername());
        dto.setCreateTime(comment.getCreateTime());
        return dto;
    }
}
