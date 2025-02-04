# Build stage
FROM bellsoft/liberica-openjdk-alpine:17 AS builder

WORKDIR /app

COPY . .

# ARG로 설정 파일 경로 전달
ARG S3Config
ARG JWTConfig
ARG ProdConfig
ARG ApplicationConfig

# 설정 파일 복사
COPY ${S3Config} /app/module-api/src/main/resources/application-s3.yaml
COPY ${JWTConfig} /app/module-api/src/main/resources/application-jwt.yaml
COPY ${ProdConfig} /app/module-api/src/main/resources/application-prod.yaml
COPY ${ApplicationConfig} /app/module-api/src/main/resources/application.yaml

# gradlew에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 명령어 실행
RUN ./gradlew clean :module-api:buildNeeded --stacktrace --refresh-dependencies -x test -Dspring.profiles.active=prod


# Run stage
FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app

# 빌드된 JAR 파일 복사 (정확한 경로를 수정해야 할 수 있음)
COPY --from=builder /app/module-api/build/libs/*.jar app.jar

EXPOSE 80

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
