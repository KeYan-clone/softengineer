package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用响应结果包装类
 * @param <T> 数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    /**
     * 状态码，200表示成功
     */
    private int code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    /**
     * 创建成功结果
     * @param <T> 数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return success(null);
    }
    
    /**
     * 创建带数据的成功结果
     * @param <T> 数据类型
     * @param data 数据
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }
    
    /**
     * 创建失败结果
     * @param <T> 数据类型
     * @param code 错误码
     * @param message 错误消息
     * @return 失败结果
     */
    public static <T> Result<T> failure(int code, String message) {
        return Result.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
