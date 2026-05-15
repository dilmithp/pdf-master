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
