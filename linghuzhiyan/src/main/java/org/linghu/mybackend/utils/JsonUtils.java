package org.linghu.mybackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * JSON工具类，提供JSON相关的操作方法
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 定义用户资料可选字段及其默认值
    private static final Map<String, Object> PROFILE_FIELDS = new HashMap<>();

    static {
        // 个人信息
        PROFILE_FIELDS.put("realName", ""); // 真实姓名
        PROFILE_FIELDS.put("nickname", ""); // 昵称
        PROFILE_FIELDS.put("gender", ""); // 性别
        PROFILE_FIELDS.put("birthdate", ""); // 出生日期
        PROFILE_FIELDS.put("bio", ""); // 个人简介
        PROFILE_FIELDS.put("location", ""); // 所在地区

        // 联系方式
        PROFILE_FIELDS.put("phone", ""); // 联系电话
        PROFILE_FIELDS.put("wechat", ""); // 微信号

        // 教育/工作信息
        PROFILE_FIELDS.put("education", ""); // 教育背景
        PROFILE_FIELDS.put("school", ""); // 学校
        PROFILE_FIELDS.put("major", ""); // 专业

        // 其他信息
        PROFILE_FIELDS.put("interests", ""); // 兴趣爱好
        PROFILE_FIELDS.put("skills", ""); // 技能特长
    }

    /**
     * 将对象转换为JSON字符串
     * 
     * @param object 要转换的对象
     * @return JSON字符串，如果转换失败则返回null
     */
    public static String toJsonString(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error("Convert object to JSON string error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将JSON字符串解析为对象
     * 
     * @param json      JSON字符串
     * @param valueType 目标类型
     * @return 解析后的对象，如果解析失败则返回null
     */
    public static <T> T parseObject(String json, Class<T> valueType) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            logger.error("Parse JSON string error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 标准化处理用户资料
     * 将各种格式的用户资料字符串转换为标准的JSON格式，包含预定义字段
     * 
     * @param profileStr 用户资料字符串，可以是JSON字符串、普通文本或null
     * @return 标准化的用户资料JSON字符串
     */
    public static String standardizeProfile(String profileStr) {
        // 如果为空，返回空的标准JSON结构
        if (StringUtils.isBlank(profileStr)) {
            return createDefaultProfile();
        }

        try {
            // 如果已经是有效的JSON对象，则合并默认字段
            if (profileStr.startsWith("{")) {
                JsonNode existingProfile = objectMapper.readTree(profileStr);
                if (existingProfile.isObject()) {
                    return mergeWithDefaultProfile((ObjectNode) existingProfile);
                }
            }

            // 如果是JSON数组或其他格式，将其放入info字段
            if (profileStr.startsWith("[") || !profileStr.startsWith("{")) {
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put("info", profileStr);
                return mergeWithDefaultProfile(rootNode);
            }

        } catch (JsonProcessingException e) {
            // 如果解析失败，将输入作为普通文本处理
            logger.warn("解析用户资料JSON时出错: {}，将以文本方式处理", e.getMessage());
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("info", profileStr);
            return mergeWithDefaultProfile(rootNode);
        }

        // 兜底返回默认结构
        return createDefaultProfile();
    }

    public static String standardizeProfile(Object profile) {
        if (profile == null) {
            return "{}";
        }

        if (profile instanceof String) {
            return standardizeProfile((String) profile);
        }

        // 如果是对象，先转换为 JSON 字符串
        String profileJson = toJsonString(profile);
        return standardizeProfile(profileJson);
    }

    /**
     * 将用户资料与默认字段合并
     * 
     * @param profileNode 用户资料JSON节点
     * @return 合并后的标准JSON字符串
     */
    private static String mergeWithDefaultProfile(ObjectNode profileNode) {
        ObjectNode result = profileNode.deepCopy();

        // 添加默认字段（如果不存在）
        for (Map.Entry<String, Object> entry : PROFILE_FIELDS.entrySet()) {
            String key = entry.getKey();
            if (!result.has(key)) {
                Object defaultValue = entry.getValue();
                if (defaultValue instanceof String) {
                    result.put(key, (String) defaultValue);
                } else if (defaultValue instanceof Number) {
                    result.put(key, ((Number) defaultValue).doubleValue());
                } else if (defaultValue instanceof Boolean) {
                    result.put(key, (Boolean) defaultValue);
                }
            }
        }

        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            logger.error("序列化合并后的用户资料时出错: {}", e.getMessage());
            return createDefaultProfile();
        }
    }

    /**
     * 创建包含所有默认字段的资料JSON
     * 
     * @return 默认资料JSON字符串
     */
    private static String createDefaultProfile() {
        ObjectNode defaultProfile = objectMapper.createObjectNode();

        // 设置所有默认字段
        for (Map.Entry<String, Object> entry : PROFILE_FIELDS.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                defaultProfile.put(key, (String) value);
            } else if (value instanceof Number) {
                defaultProfile.put(key, ((Number) value).doubleValue());
            } else if (value instanceof Boolean) {
                defaultProfile.put(key, (Boolean) value);
            }
        }

        try {
            return objectMapper.writeValueAsString(defaultProfile);
        } catch (JsonProcessingException e) {
            logger.error("序列化默认用户资料时出错: {}", e.getMessage());
            return "{}"; // 最终兜底
        }
    }

    /**
     * 获取用户资料中的特定字段值
     * 
     * @param profileStr 用户资料JSON字符串
     * @param fieldName  字段名
     * @return 字段值，如不存在或出错则返回空字符串
     */
    public static String getProfileField(String profileStr, String fieldName) {
        if (StringUtils.isBlank(profileStr)) {
            return "";
        }

        try {
            JsonNode rootNode = objectMapper.readTree(profileStr);
            JsonNode fieldNode = rootNode.get(fieldName);
            return fieldNode != null ? fieldNode.asText("") : "";
        } catch (Exception e) {
            logger.warn("获取用户资料字段时出错: {}", e.getMessage());
            return "";
        }
    }

    /**
     * 更新用户资料中的特定字段
     * 
     * @param profileStr 原用户资料JSON字符串
     * @param fieldName  要更新的字段名
     * @param fieldValue 新的字段值
     * @return 更新后的用户资料JSON字符串
     */
    public static String updateProfileField(String profileStr, String fieldName, String fieldValue) {
        String standardProfile = standardizeProfile(profileStr);

        try {
            JsonNode rootNode = objectMapper.readTree(standardProfile);
            ObjectNode objectNode = (ObjectNode) rootNode;
            objectNode.put(fieldName, fieldValue);
            return objectMapper.writeValueAsString(objectNode);
        } catch (Exception e) {
            logger.error("更新用户资料字段时出错: {}", e.getMessage());
            return standardProfile;
        }
    }
}