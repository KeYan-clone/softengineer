package org.example.discussion.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Utility class for API responses
 */
public class ApiResponse {
    
    private ApiResponse() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Create a successful response with data
     *
     * @param data Response data
     * @param <T> Data type
     * @return Result object
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    /**
     * Create a successful response with message
     *
     * @param message Success message
     * @return Result object
     */
    public static Result<Void> success(String message) {
        return new Result<>(200, message, null);
    }
    
    /**
     * Create a failed response with error message
     *
     * @param code Error code
     * @param message Error message
     * @return Result object
     */
    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * Create a page result from Spring Page
     *
     * @param page Spring Page object
     * @param <T> Data type
     * @return PageResult object
     */
    public static <T> PageResult<T> page(Page<T> page) {
        return new PageResult<>(
                page.getContent(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
    
    /**
     * Result class for API responses
     *
     * @param <T> Data type
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result<T> {
        private int code;
        private String message;
        private T data;
    }
    
    /**
     * PageResult class for paginated API responses
     *
     * @param <T> Data type
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResult<T> {
        private List<T> list;
        private long total;
        private int pageNum;
        private int pageSize;
    }
}
