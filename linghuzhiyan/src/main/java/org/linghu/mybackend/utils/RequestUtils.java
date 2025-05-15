package org.linghu.mybackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类，用于处理HTTP请求相关的工具方法
 */
public class RequestUtils {

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    /**
     * 获取客户端真实IP地址
     * @param request HTTP请求
     * @return 客户端IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                // 可能有多个IP，第一个为客户端真实IP
                String ip = ipList.split(",")[0];
                return ip;
            }
        }
        
        return request.getRemoteAddr();
    }

    /**
     * 获取客户端设备类型
     * @param request HTTP请求
     * @return 设备类型
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "unknown";
        }
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("mobile")) {
            return "Mobile";
        } else if (userAgent.contains("tablet")) {
            return "Tablet";
        } else if (userAgent.contains("android")) {
            return "Android";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad")) {
            return "iOS";
        } else if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac")) {
            return "Mac";
        } else if (userAgent.contains("linux")) {
            return "Linux";
        } else {
            return "Desktop";
        }
    }

    /**
     * 收集请求信息为JSON
     * @param request HTTP请求
     * @return JSON格式的请求信息
     */
    public static String collectRequestInfo(HttpServletRequest request) {
        try {
            Map<String, Object> requestInfo = new HashMap<>();
            requestInfo.put("userAgent", request.getHeader("User-Agent"));
            requestInfo.put("referer", request.getHeader("Referer"));
            requestInfo.put("method", request.getMethod());
            requestInfo.put("uri", request.getRequestURI());
            
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(requestInfo);
        } catch (Exception e) {
            return "{}";
        }
    }
}
