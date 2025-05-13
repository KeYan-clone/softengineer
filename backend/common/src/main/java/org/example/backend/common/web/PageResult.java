package org.example.backend.common.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果包装类
 *
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    private List<T> list;    // 数据列表
    private long total;      // 总记录数
    private int pageNum;     // 页码
    private int pageSize;    // 每页记录数
}
