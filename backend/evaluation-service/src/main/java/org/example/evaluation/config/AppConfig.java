package org.example.evaluation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 应用程序主配置类
 */
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
@EnableSpringDataWebSupport
public class AppConfig {

    /**
     * 方法参数验证处理器
     * 用于支持方法级别的参数验证
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
    /**
     * 异步任务执行配置
     * 用于支持异步数据分析任务
     */
    @Configuration
    @Profile("!test") // 非测试环境生效
    public static class AsyncConfig {
        
        // 这里可以添加异步任务相关的配置
        // 例如：线程池、任务执行器等
        
    }
}
