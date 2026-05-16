plugins {
    id("pdfmaster.java-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    testImplementation(platform("org.testcontainers:testcontainers-bom:${libs.versions.testcontainers.get()}"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:rabbitmq")
    testImplementation("org.testcontainers:localstack")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
}

// Forward Docker SDK env vars so Testcontainers (docker-java) talks to the right socket on
// Docker Desktop. Docker Desktop 4.71+ exposes a guard socket at the standard path that
// returns stub Info; the real engine lives at ~/Library/Containers/com.docker.docker/Data/docker.raw.sock.
// We also pin api.version because docker-java defaults to 1.32 which the engine rejects.
tasks.withType<Test>().configureEach {
    listOf("DOCKER_HOST", "DOCKER_API_VERSION", "DOCKER_TLS_VERIFY", "DOCKER_CERT_PATH")
        .forEach { key ->
            System.getenv(key)?.let { environment(key, it) }
        }
    System.getenv("DOCKER_API_VERSION")?.let { systemProperty("api.version", it) }
    System.getenv("DOCKER_HOST")?.let { systemProperty("docker.host", it) }
}
