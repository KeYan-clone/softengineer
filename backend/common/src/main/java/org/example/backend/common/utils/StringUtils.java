package org.example.backend.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 字符串工具类
 */
public final class StringUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    private StringUtils() {
        // 私有构造函数，防止实例化
    }
    
    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 判断字符串是否不为空
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 生成UUID
     * @return UUID字符串
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 生成带时间前缀的ID
     * @return ID字符串
     */
    public static String generateTimeBasedId() {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        return timestamp + random;
    }
    
    /**
     * 截断字符串
     * @param str 字符串
     * @param maxLength 最大长度
     * @return 截断后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str)) {
            return str;
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }
}
