package org.example.backend.common.constants;

/**
 * 系统常量
 */
public final class SystemConstants {
    // 通用状态
    public static final String STATUS_ACTIVE = "1";
    public static final String STATUS_INACTIVE = "0";
    
    // 错误码前缀
    public static final int BUSINESS_ERROR_PREFIX = 100000;
    public static final int SYSTEM_ERROR_PREFIX = 200000;
    public static final int PARAMETER_ERROR_PREFIX = 300000;
    public static final int AUTH_ERROR_PREFIX = 400000;
    public static final int EXTERNAL_ERROR_PREFIX = 500000;
    
    // 模块编码
    public static final int USER_MODULE = 1000;
    public static final int EXPERIMENT_MODULE = 2000;
    public static final int DISCUSSION_MODULE = 3000;
    public static final int EVALUATION_MODULE = 4000;
    public static final int SUPPORT_MODULE = 5000;
    
    // 时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // 分页默认值
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_NUM = 1;
    
    private SystemConstants() {
        // 私有构造函数，防止实例化
    }
}
