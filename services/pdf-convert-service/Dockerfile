FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.gradle ./gradlew :services:pdf-convert-service:bootJar -x test

FROM eclipse-temurin:17-jre AS runtime
# LibreOffice headless is provided by a separate sidecar image in production; here we expect
# the binary to be available on PATH when run as a unified container, or override LIBREOFFICE_BIN.
RUN groupadd --system app && useradd --system --gid app --create-home --home-dir /home/app app
WORKDIR /opt/app
COPY --from=build /workspace/services/pdf-convert-service/build/libs/*.jar app.jar
USER app
EXPOSE 8087
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8087/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/opt/app/app.jar"]
