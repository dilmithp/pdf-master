plugins {
    id("pdfmaster.spring-webflux-conventions")
}

description = "Edge API gateway: TLS termination, JWT verification, rate limiting, routing."

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    testImplementation("org.springframework.security:spring-security-test")
}
