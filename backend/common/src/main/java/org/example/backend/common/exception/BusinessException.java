package org.example.backend.common.exception;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {
    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
