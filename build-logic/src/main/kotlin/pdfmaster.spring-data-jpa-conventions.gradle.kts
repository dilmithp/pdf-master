plugins {
    id("pdfmaster.spring-boot-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core:${libs.versions.flyway.get()}")
    implementation("org.flywaydb:flyway-database-postgresql:${libs.versions.flyway.get()}")
    runtimeOnly("org.postgresql:postgresql:${libs.versions.postgresql.get()}")
}
