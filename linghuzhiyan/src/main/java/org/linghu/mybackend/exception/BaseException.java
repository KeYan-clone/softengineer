package org.linghu.mybackend.exception;

import lombok.Getter;

/**
 * 异常基类
 */
@Getter
public abstract class BaseException extends RuntimeException {
    /**
     * 错误码
     */
    private final int code;

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    protected BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     * @param cause 异常原因
     */
    protected BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
