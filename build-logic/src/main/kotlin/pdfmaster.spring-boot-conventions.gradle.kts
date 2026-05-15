plugins {
    id("pdfmaster.java-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback:logstash-logback-encoder:8.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
}

tasks.withType<Test>().configureEach {
    // Spring Boot tests rely on annotations preserved via -parameters
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}
