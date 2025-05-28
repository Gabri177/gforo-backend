FROM maven:3.9.6-eclipse-temurin-17 AS builder


# 复制当前 backend/gforo 下所有内容（含 pom.xml 和 src）
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk


WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
