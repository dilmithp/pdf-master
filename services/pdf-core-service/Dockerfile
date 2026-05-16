# syntax=docker/dockerfile:1.7
# Builder stage — uses the repo's gradle wrapper to build the bootJar
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace
# Copy gradle wrapper + build files first for layer caching
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties* ./
COPY gradle ./gradle
COPY build-logic ./build-logic
# Copy only this service
COPY services/pdf-core-service ./services/pdf-core-service
RUN chmod +x ./gradlew && ./gradlew --no-daemon :services:pdf-core-service:bootJar -x test

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
LABEL org.opencontainers.image.title="pdfmaster/pdf-core-service"
LABEL org.opencontainers.image.description="PDF core operations service (merge, split, compress, rotate, reorder)"
LABEL org.opencontainers.image.source="https://github.com/dilmithp/pdf-master"
LABEL org.opencontainers.image.licenses="Apache-2.0"

RUN apk add --no-cache qpdf wget

# Non-root user
RUN addgroup -S -g 10001 pdfmaster && adduser -S -u 10001 -G pdfmaster pdfmaster
WORKDIR /app
COPY --from=builder /workspace/services/pdf-core-service/build/libs/*.jar /app/app.jar
RUN chown -R pdfmaster:pdfmaster /app
USER pdfmaster

EXPOSE 8082
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8082/actuator/health || exit 1
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
