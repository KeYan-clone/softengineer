# 灵狐智验系统后端架构

## 项目概述

灵狐智验系统是一个面向高校实验教学的综合管理平台，采用微服务架构设计，由七个核心模块组成，提供用户管理、实验管理、讨论交流、评估反馈和系统支持等功能。

## 架构设计

系统采用基于Spring Boot的微服务架构，主要模块包括：

- **Common模块**：公共组件库
- **Core模块**：核心业务逻辑和领域模型
- **User-Service**：用户服务
- **Discussion-Service**：讨论服务
- **Evaluation-Service**：评估服务
- **Experiment-Service**：实验服务
- **Support-Service**：支持服务

## 技术栈

- **开发语言**：Java 21
- **核心框架**：Spring Boot 3.4.5, Spring Cloud
- **数据访问**：MyBatis, Spring Data JPA
- **数据库**：MySQL, MongoDB, Redis
- **安全认证**：Spring Security, JWT
- **消息队列**：RabbitMQ
- **搜索引擎**：Elasticsearch
- **API文档**：Swagger/OpenAPI 3
- **构建工具**：Maven

## 项目结构

```
backend/
├── pom.xml                  # 父POM文件
├── common/                  # 公共模块
├── core/                    # 核心业务模块
├── user-service/            # 用户服务
├── discussion-service/      # 讨论服务
├── evaluation-service/      # 评估服务
├── experiment-service/      # 实验服务
└── support-service/         # 支持服务
```

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.8+
- MySQL 8.0+
- MongoDB 5.0+
- Redis 6.0+

### 构建与运行

1. **克隆项目**
   ```bash
   git clone https://your-repository-url/backend.git
   cd backend
   ```

2. **编译所有模块**
   ```bash
   mvn clean package
   ```

3. **运行服务**
   ```bash
   # 依次启动各服务，或使用Docker Compose
   java -jar user-service/target/user-service-0.0.1-SNAPSHOT.jar
   ```

## API文档

启动服务后，可通过以下URL访问Swagger API文档：

- User Service: http://localhost:8081/swagger-ui.html
- Discussion Service: http://localhost:8082/swagger-ui.html
- Evaluation Service: http://localhost:8083/swagger-ui.html
- Experiment Service: http://localhost:8084/swagger-ui.html
- Support Service: http://localhost:8085/swagger-ui.html

## 模块依赖关系

- 所有服务模块依赖common模块和core模块
- 服务模块之间通过REST API和事件通信，保持松耦合

## 部署架构

项目支持多种部署方式：

1. **单机部署**：适用于开发和测试环境
2. **分布式部署**：适用于生产环境，各服务独立部署
3. **容器化部署**：使用Docker和Kubernetes进行容器编排

## 开发指南

详细的开发指南请参考项目Wiki或开发文档。

## 测试计划

项目采用多层次测试策略，详情请参考测试计划文档。
