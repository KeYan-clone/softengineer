# 评价服务模块（Evaluation Service）

## 概述

评价服务模块是"灵狐智验"系统的核心组件之一，负责收集和处理用户对实验的评价以及系统反馈信息。该模块提供了全面的API接口，支持评价数据的收集、查询、统计分析等功能。

## 主要功能

1. **实验评价管理**：
   - 用户对实验进行评分和评论
   - 基于多维度标准的评价（如准确性、可用性等）
   - 评价数据的增删改查
   - 评价数据的统计分析

2. **系统反馈处理**：
   - 用户提交系统反馈
   - 反馈状态跟踪和更新
   - 按类别、状态等条件查询反馈
   - 反馈数据的统计分析

## 技术栈

- **开发框架**：Spring Boot 3.x
- **数据访问**：Spring Data JPA
- **数据库**：MySQL
- **API文档**：SpringDoc OpenAPI
- **服务注册与发现**：Spring Cloud
- **安全认证**：Spring Security + OAuth2
- **单元测试**：JUnit 5 + Mockito
- **集成测试**：SpringBootTest + H2 Database

## 模块结构

```
src/main/java/org/example/evaluation/
│
├── EvaluationApplication.java       # 应用程序入口
│
├── domain/                          # 领域模型
│   ├── CriteriaRating.java          # 评价标准评分
│   ├── ExperimentEvaluation.java    # 实验评价
│   └── SystemFeedback.java          # 系统反馈
│
├── dto/                             # 数据传输对象
│   ├── CriteriaRatingDTO.java
│   ├── EvaluationStatisticsDTO.java
│   ├── ExperimentEvaluationDTO.java
│   ├── ExperimentEvaluationResponseDTO.java
│   ├── SystemFeedbackDTO.java
│   └── SystemFeedbackResponseDTO.java
│
├── repository/                      # 数据访问层
│   ├── ExperimentEvaluationRepository.java
│   └── SystemFeedbackRepository.java
│
├── service/                         # 业务逻辑层
│   ├── ExperimentEvaluationService.java
│   ├── SystemFeedbackService.java
│   └── impl/
│       ├── ExperimentEvaluationServiceImpl.java
│       └── SystemFeedbackServiceImpl.java
│
├── controller/                      # 接口控制层
│   ├── ExperimentEvaluationController.java
│   └── SystemFeedbackController.java
│
├── analyzer/                        # 数据分析
│   ├── EvaluationAnalyzer.java
│   └── FeedbackAnalyzer.java
│
└── config/                          # 配置类
    ├── AppConfig.java
    ├── CorsConfig.java
    ├── GlobalExceptionHandler.java
    ├── OpenApiConfig.java
    └── SecurityConfig.java
```

## API 端点

### 实验评价 API

- `POST /api/evaluations/experiments` - 创建新评价
- `GET /api/evaluations/experiments/{id}` - 获取评价详情
- `PUT /api/evaluations/experiments/{id}` - 更新评价
- `DELETE /api/evaluations/experiments/{id}` - 删除评价
- `GET /api/evaluations/experiments` - 获取实验的所有评价
- `GET /api/evaluations/experiments/paged` - 分页获取实验评价
- `GET /api/evaluations/experiments/{experimentId}/statistics` - 获取实验评价统计数据

### 系统反馈 API

- `POST /api/feedback` - 提交反馈
- `GET /api/feedback/{id}` - 获取反馈详情
- `PUT /api/feedback/{id}/status` - 更新反馈状态
- `DELETE /api/feedback/{id}` - 删除反馈
- `GET /api/feedback/user/{userId}` - 获取用户的所有反馈
- `GET /api/feedback/category/{category}` - 获取特定类别的反馈
- `GET /api/feedback/status/{status}` - 获取特定状态的反馈

## 安全性

评价服务采用 OAuth2 资源服务器模式进行身份验证，确保只有授权用户才能访问受保护的资源。API 端点根据不同的权限级别进行保护，例如，评价统计数据可以公开访问，而提交或修改评价则需要用户身份验证。

## 数据统计分析

该模块提供了强大的数据分析功能，包括：

- 实验评价的平均分、分布情况
- 评价趋势分析（按时间）
- 按标准维度的评分分析
- 反馈类别分布分析
- 反馈处理效率分析

## 部署说明

服务通过 Spring Cloud 进行注册，支持微服务架构下的服务发现和负载均衡。数据库配置和其他环境设置可在 `application.properties` 文件中进行调整。

## 测试覆盖

该模块包含全面的单元测试和集成测试，涵盖了所有关键功能和边缘情况。测试环境使用 H2 内存数据库，确保测试的隔离性和可重复性。
