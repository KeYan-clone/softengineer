package org.example.backend.common.exception;

/**
 * 讨论相关业务异常
 * 集中管理讨论模块的所有异常类型
 */
public class DiscussionException extends BaseException {
    
    public static final int DISCUSSION_NOT_FOUND = 300001;
    public static final int COMMENT_NOT_FOUND = 300002;
    public static final int UNAUTHORIZED_ACCESS = 300003;
    public static final int INVALID_CONTENT = 300004;
    
    public DiscussionException(int code, String message) {
        super(code, message);
    }
    
    public DiscussionException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    /**
     * 讨论未找到异常
     * 
     * @param discussionId 讨论ID
     * @return 异常对象
     */
    public static DiscussionException discussionNotFound(String discussionId) {
        return new DiscussionException(DISCUSSION_NOT_FOUND, 
            String.format("讨论未找到: %s", discussionId));
    }
    
    /**
     * 评论未找到异常
     * 
     * @param commentId 评论ID
     * @return 异常对象
     */
    public static DiscussionException commentNotFound(String commentId) {
        return new DiscussionException(COMMENT_NOT_FOUND, 
            String.format("评论未找到: %s", commentId));
    }
    
    /**
     * 未授权访问异常
     * 
     * @param userId 用户ID
     * @param resource 资源描述
     * @return 异常对象
     */
    public static DiscussionException unauthorizedAccess(String userId, String resource) {
        return new DiscussionException(UNAUTHORIZED_ACCESS, 
            String.format("用户 %s 没有权限访问 %s", userId, resource));
    }
    
    /**
     * 无效内容异常
     * 
     * @param reason 原因描述
     * @return 异常对象
     */
    public static DiscussionException invalidContent(String reason) {
        return new DiscussionException(INVALID_CONTENT, 
            String.format("无效的内容: %s", reason));
    }
}
