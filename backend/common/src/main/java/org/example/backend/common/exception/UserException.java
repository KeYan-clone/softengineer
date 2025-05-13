package org.example.backend.common.exception;

/**
 * 用户相关业务异常
 * 集中管理用户模块的所有异常类型
 */
public class UserException extends BaseException {
    
    public static final int USER_NOT_FOUND = 100001;
    public static final int USERNAME_ALREADY_EXISTS = 100002;
    public static final int EMAIL_ALREADY_EXISTS = 100003;
    public static final int INVALID_CREDENTIALS = 100004;
    public static final int INVALID_OLD_PASSWORD = 100005;
    
    public UserException(int code, String message) {
        super(code, message);
    }
    
    public static UserException userNotFound() {
        return new UserException(USER_NOT_FOUND, "用户不存在");
    }
    
    public static UserException usernameAlreadyExists() {
        return new UserException(USERNAME_ALREADY_EXISTS, "用户名已存在");
    }
    
    public static UserException emailAlreadyExists() {
        return new UserException(EMAIL_ALREADY_EXISTS, "邮箱已存在");
    }
    
    public static UserException invalidCredentials() {
        return new UserException(INVALID_CREDENTIALS, "用户名或密码错误");
    }
    
    public static UserException invalidOldPassword() {
        return new UserException(INVALID_OLD_PASSWORD, "原密码错误");
    }
}
