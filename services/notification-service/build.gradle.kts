plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.spring-data-jpa-conventions")
    id("pdfmaster.testcontainers-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

description = "Transactional notification delivery via Postmark, fed from RabbitMQ."

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
