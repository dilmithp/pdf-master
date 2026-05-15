plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.testcontainers-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

description = "PDF OCR service (Tesseract via Tess4J)"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("software.amazon.awssdk:s3:${libs.versions.awsSdk.get()}")
    implementation("net.sourceforge.tess4j:tess4j:${libs.versions.tess4j.get()}") {
        // Avoid pulling in transitive deps that conflict with Spring Boot's logging stack.
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
    }

    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
