# 使用Ubuntu作为基础镜像进行构建
FROM ubuntu:22.04 AS build

# 设置工作目录
WORKDIR /app

# 安装必要的依赖：JDK 17、Maven、curl
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    maven \
    curl \
    && rm -rf /var/lib/apt/lists/*

# 配置阿里云Maven镜像加速构建
RUN mkdir -p /root/.m2 && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd"> \
    <mirrors> \
    <mirror> \
    <id>aliyunmaven</id> \
    <name>阿里云公共仓库</name> \
    <url>https://maven.aliyun.com/repository/public</url> \
    <mirrorOf>central</mirrorOf> \
    </mirror> \
    </mirrors> \
    </settings>' > /root/.m2/settings.xml

# 复制pom.xml并下载依赖（利用Docker缓存层）
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码
COPY backend/src ./src

# 构建应用
RUN mvn clean package -DskipTests

# 运行阶段 - 使用Ubuntu + JRE
FROM ubuntu:22.04

# 设置工作目录
WORKDIR /app

# 安装JRE 17
RUN apt-get update && apt-get install -y \
    openjdk-17-jre \
    && rm -rf /var/lib/apt/lists/*

# 从构建阶段复制jar文件
COPY --from=build /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
