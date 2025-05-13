package org.example.evaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 评价服务应用程序入口
 * 提供实验评价和系统反馈功能
 */
@SpringBootApplication
@EnableCaching          // 启用缓存
@EnableAsync            // 启用异步
@EnableScheduling       // 启用定时任务
public class EvaluationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EvaluationApplication.class, args);
	}

}
