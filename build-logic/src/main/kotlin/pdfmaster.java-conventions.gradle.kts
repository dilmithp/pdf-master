import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.jk1.license.LicenseReportExtension

plugins {
    java
    id("com.diffplug.spotless")
    id("com.github.jk1.dependency-license-report")
}

group = "com.pdfmaster"
version = "0.0.1-SNAPSHOT"

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get().toInt()))
    }
}

// Repositories are declared at the settings level (FAIL_ON_PROJECT_REPOS).

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all", "-parameters"))
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    systemProperty("file.encoding", "UTF-8")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${libs.versions.assertj.get()}")
    testImplementation("org.mockito:mockito-junit-jupiter:${libs.versions.mockitoJupiter.get()}")
    testImplementation("com.tngtech.archunit:archunit-junit5:${libs.versions.archunit.get()}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configure<SpotlessExtension> {
    java {
        target("src/**/*.java")
        googleJavaFormat("1.25.2")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.5.0")
    }
}

configure<LicenseReportExtension> {
    outputDir = layout.buildDirectory.dir("reports/licenses").get().asFile.absolutePath
    allowedLicensesFile = file("${rootProject.projectDir}/build-logic/allowed-licenses.json")
    filters = arrayOf(com.github.jk1.license.filter.LicenseBundleNormalizer())
}

tasks.named("check") {
    dependsOn("checkLicense")
}
