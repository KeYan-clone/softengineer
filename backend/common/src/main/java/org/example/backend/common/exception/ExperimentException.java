package org.example.backend.common.exception;

/**
 * 实验相关业务异常
 * 集中管理实验模块的所有异常类型
 */
public class ExperimentException extends BaseException {
    
    public static final int EXPERIMENT_NOT_FOUND = 200001;
    public static final int RESULT_NOT_FOUND = 200002;
    public static final int UNAUTHORIZED_ACCESS = 200003;
    
    public ExperimentException(int code, String message) {
        super(code, message);
    }
    
    public ExperimentException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    
    /**
     * 实验未找到异常
     * 
     * @param experimentId 实验ID
     * @return 异常对象
     */
    public static ExperimentException experimentNotFound(String experimentId) {
        return new ExperimentException(EXPERIMENT_NOT_FOUND, 
            String.format("实验未找到: %s", experimentId));
    }
    
    /**
     * 实验结果未找到异常
     * 
     * @param resultId 结果ID
     * @return 异常对象
     */
    public static ExperimentException resultNotFound(String resultId) {
        return new ExperimentException(RESULT_NOT_FOUND, 
            String.format("实验结果未找到: %s", resultId));
    }
    
    /**
     * 未授权访问异常
     * 
     * @param userId 用户ID
     * @param resource 资源描述
     * @return 异常对象
     */
    public static ExperimentException unauthorizedAccess(String userId, String resource) {
        return new ExperimentException(UNAUTHORIZED_ACCESS, 
            String.format("用户 %s 没有权限访问 %s", userId, resource));
    }
}
