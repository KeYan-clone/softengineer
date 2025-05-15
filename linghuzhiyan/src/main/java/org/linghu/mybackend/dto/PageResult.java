package org.linghu.mybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果包装类
 * @param <T> 数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    /**
     * 数据列表
     */
    private List<T> list;
    
    /**
     * 总记录数
     */
    private long total;
    
    /**
     * 当前页码
     */
    private int pageNum;
    
    /**
     * 每页大小
     */
    private int pageSize;
    
    /**
     * 创建分页结果
     * @param <T> 数据类型
     * @param list 数据列表
     * @param total 总记录数
     * @param pageNum 当前页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    public static <T> PageResult<T> of(List<T> list, long total, int pageNum, int pageSize) {
        return PageResult.<T>builder()
                .list(list)
                .total(total)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .build();
    }
}
