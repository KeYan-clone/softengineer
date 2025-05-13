# 灵狐智验 - 评价服务部署指南

## 部署方式

评价服务支持多种部署方式，以下是主要的几种：

### 1. 本地开发环境部署

#### 前置条件
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

#### 步骤
1. **克隆代码**
   ```bash
   git clone <repository-url>
   cd evaluation-service
   ```

2. **配置数据库**
   - 创建数据库
   ```sql
   CREATE DATABASE evaluation_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   - 创建用户并授权
   ```sql
   CREATE USER 'evaluation_user'@'%' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON evaluation_db.* TO 'evaluation_user'@'%';
   FLUSH PRIVILEGES;
   ```

3. **配置应用**
   - 修改 `src/main/resources/application.properties` 中的数据库连接信息

4. **构建与运行**
   ```bash
   mvn clean package
   java -jar target/evaluation-service-0.0.1-SNAPSHOT.jar
   ```

### 2. Docker 容器部署

#### 前置条件
- Docker
- Docker Compose (可选)

#### 步骤
1. **构建 Docker 镜像**
   ```bash
   docker build -t linghu/evaluation-service:latest .
   ```

2. **使用 Docker Compose 运行**
   - 创建 `docker-compose.yml` 文件
   ```yaml
   version: '3.8'
   services:
     evaluation-db:
       image: mysql:8.0
       environment:
         MYSQL_DATABASE: evaluation_db
         MYSQL_USER: evaluation_user
         MYSQL_PASSWORD: your_password
         MYSQL_ROOT_PASSWORD: root_password
       volumes:
         - evaluation_db_data:/var/lib/mysql
       ports:
         - "3306:3306"
       healthcheck:
         test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
         timeout: 5s
         retries: 10
   
     evaluation-service:
       image: linghu/evaluation-service:latest
       depends_on:
         evaluation-db:
           condition: service_healthy
       environment:
         SPRING_DATASOURCE_URL: jdbc:mysql://evaluation-db:3306/evaluation_db
         SPRING_DATASOURCE_USERNAME: evaluation_user
         SPRING_DATASOURCE_PASSWORD: your_password
       ports:
         - "8083:8083"
   
   volumes:
     evaluation_db_data:
   ```
   
   - 启动服务
   ```bash
   docker-compose up -d
   ```

### 3. Kubernetes 部署

#### 前置条件
- Kubernetes 集群
- kubectl 命令行工具
- Helm (可选)

#### 步骤
1. **推送 Docker 镜像到仓库**
   ```bash
   docker push linghu/evaluation-service:latest
   ```

2. **创建 Kubernetes Secret**
   ```bash
   kubectl create secret generic db-credentials \
     --from-literal=username=evaluation_user \
     --from-literal=password=your_password
   ```

3. **部署服务**
   ```bash
   kubectl apply -f deployment/kubernetes/deployment.yaml
   ```

4. **验证部署**
   ```bash
   kubectl get pods -l app=evaluation-service
   kubectl get services evaluation-service
   ```

## 服务监控

### 健康检查

服务提供了以下健康检查端点：

- 存活探针: `/actuator/health/liveness`
- 就绪探针: `/actuator/health/readiness`

### 日志

- **本地部署**: 日志位于 `logs/` 目录下
- **Docker/K8s 部署**: 使用 `docker logs` 或 `kubectl logs` 查看日志

### 指标监控

服务暴露了 Spring Boot Actuator 端点，可以用于监控：

- `/actuator/health` - 健康状态
- `/actuator/info` - 应用信息
- `/actuator/metrics` - 应用指标

## API 文档

部署成功后，可以通过以下URL访问API文档：

- Swagger UI: `http://<host>:<port>/swagger-ui.html`
- OpenAPI 规范: `http://<host>:<port>/api-docs`

## 故障排除

1. **连接数据库失败**
   - 检查数据库连接字符串
   - 确认数据库用户权限
   - 检查网络连接和防火墙设置

2. **服务启动失败**
   - 检查日志中的错误信息
   - 验证配置文件是否正确
   - 确认所需的环境变量是否正确设置

3. **API 调用返回错误**
   - 检查请求参数是否正确
   - 查看服务日志中的异常信息
   - 确认用户权限和认证信息

## 联系支持

如遇到部署问题，请联系灵狐智验开发团队：

- 邮箱: support@linghu.edu
- 项目管理工具: <issue-tracker-url>
