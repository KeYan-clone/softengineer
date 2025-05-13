package org.example.backend.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一API响应结果包装类
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    
    private int code;        // 状态码
    private String message;  // 消息
    private T data;          // 数据
    
    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 结果对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    /**
     * 成功响应（无数据）
     *
     * @return 结果对象
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    
    /**
     * 失败响应
     *
     * @param code 错误码
     * @param message 错误消息
     * @return 结果对象
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
