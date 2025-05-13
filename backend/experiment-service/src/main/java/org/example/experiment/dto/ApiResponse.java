package org.example.experiment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper for APIs
 * @param <T> Data type
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    
    /**
     * Create successful response with data
     *
     * @param data Response data
     * @param <T> Data type
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .data(data)
                .build();
    }
    
    /**
     * Create successful response without data
     *
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .code(200)
                .message("success")
                .build();
    }
    
    /**
     * Create error response
     *
     * @param code Error code
     * @param message Error message
     * @param <T> Data type
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(int code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}
