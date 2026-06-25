# syntax=docker/dockerfile:1

### 1) Build stage ───────────────────────────────
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Gradle 래퍼 + 빌드 스크립트 먼저 복사해 의존성 레이어를 캐시한다.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# 소스 복사 후 실행 가능한 bootJar 생성 (테스트는 CI에서 별도 수행)
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

### 2) Runtime stage ─────────────────────────────
FROM eclipse-temurin:17-jre
WORKDIR /app

# 비루트 사용자로 실행
RUN groupadd -r spring && useradd -r -g spring spring

COPY --from=build /app/build/libs/*.jar app.jar
USER spring

EXPOSE 8080
# 설정값(DB·OAuth·JWT·Redis·네이버 키)은 모두 환경변수로 주입한다.
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE="prod"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
