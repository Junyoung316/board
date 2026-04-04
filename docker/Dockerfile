FROM gradle:8-jdk21-alpine AS builder
WORKDIR /app
COPY ../.. .                          # 루트에서 소스 복사
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]