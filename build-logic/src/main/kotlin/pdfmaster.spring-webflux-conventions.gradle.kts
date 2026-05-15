plugins {
    id("pdfmaster.spring-boot-conventions")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("io.projectreactor:reactor-test")
}
