FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# 复制maven包和配置文件
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# 构建应用
RUN ./mvnw clean package -DskipTests

# 生产阶段镜像
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp

# 设置时区
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata

# 创建非root用户
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# 添加应用
ARG JAR_FILE=/workspace/app/target/*.jar
COPY --from=build ${JAR_FILE} app.jar

# 运行参数
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Duser.timezone=Asia/Shanghai"
EXPOSE 8083

# 入口点
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]
