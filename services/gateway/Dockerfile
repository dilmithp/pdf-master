# syntax=docker/dockerfile:1.7

# ---------- Stage 1: build ----------
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Copy gradle wrapper and project metadata first to maximise layer caching.
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle.kts build.gradle.kts gradle.properties ./
COPY build-logic build-logic

# Copy only the gateway source so unrelated services do not bust the build cache.
COPY services/gateway services/gateway

RUN chmod +x ./gradlew \
 && ./gradlew --no-daemon :services:gateway:bootJar

# Extract layered jar for better runtime caching.
WORKDIR /workspace/extracted
RUN cp /workspace/services/gateway/build/libs/*.jar app.jar \
 && java -Djarmode=layertools -jar app.jar extract

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache curl tini \
 && addgroup -S app \
 && adduser -S -G app -h /home/app app

USER app
WORKDIR /home/app

COPY --from=build --chown=app:app /workspace/extracted/dependencies/ ./
COPY --from=build --chown=app:app /workspace/extracted/spring-boot-loader/ ./
COPY --from=build --chown=app:app /workspace/extracted/snapshot-dependencies/ ./
COPY --from=build --chown=app:app /workspace/extracted/application/ ./

EXPOSE 8080

ENV JAVA_OPTS=""

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -fsS http://localhost:8080/actuator/health/liveness || exit 1

ENTRYPOINT ["/sbin/tini", "--", "sh", "-c", "exec java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
