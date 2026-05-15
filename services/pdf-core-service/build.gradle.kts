plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.testcontainers-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

description = "PDF core operations service (merge, split, compress, rotate, reorder)"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("software.amazon.awssdk:s3:${libs.versions.awsSdk.get()}")
    implementation("org.apache.pdfbox:pdfbox:${libs.versions.pdfbox.get()}")

    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
