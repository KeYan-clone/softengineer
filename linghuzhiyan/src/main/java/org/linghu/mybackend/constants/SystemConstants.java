package org.linghu.mybackend.constants;

/**
 * 系统常量
 */
public final class SystemConstants {
    // 用户角色
    public static final String ROLE_STUDENT = "ROLE_STUDENT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_ASSISTANT = "ROLE_ASSISTANT";
    // 通用状态
    public static final String STATUS_ACTIVE = "1";
    public static final String STATUS_INACTIVE = "0";

    // 错误码前缀
    public static final int BUSINESS_ERROR_PREFIX = 100000;
    public static final int SYSTEM_ERROR_PREFIX = 200000;
    public static final int PARAMETER_ERROR_PREFIX = 300000;
    public static final int AUTH_ERROR_PREFIX = 400000;
    public static final int EXTERNAL_ERROR_PREFIX = 500000;

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
