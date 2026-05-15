FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
RUN --mount=type=cache,target=/root/.gradle ./gradlew :services:pdf-ocr-service:bootJar -x test

FROM eclipse-temurin:17-jre AS runtime
# Tesseract native libs are installed by the production image (see /infra). The JRE image is the
# minimum so the binary layer is small; runtime images add `tesseract-ocr` and language data.
RUN groupadd --system app && useradd --system --gid app --create-home --home-dir /home/app app
WORKDIR /opt/app
COPY --from=build /workspace/services/pdf-ocr-service/build/libs/*.jar app.jar
USER app
EXPOSE 8088
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://127.0.0.1:8088/actuator/health | grep -q '"status":"UP"' || exit 1
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","/opt/app/app.jar"]
