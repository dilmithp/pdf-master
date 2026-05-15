plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.spring-data-jpa-conventions")
    id("pdfmaster.testcontainers-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

description = "PDF AI service (chat, summarize, translate, redact; pgvector-backed embeddings)"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("software.amazon.awssdk:s3:${libs.versions.awsSdk.get()}")
    implementation("com.pgvector:pgvector:0.1.6")

    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
