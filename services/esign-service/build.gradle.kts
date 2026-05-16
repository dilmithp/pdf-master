plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.spring-data-jpa-conventions")
    id("pdfmaster.testcontainers-conventions")
}

description = "E-signature workflows, signer ordering, and X.509 certificate scaffolding."

dependencies {
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
}
