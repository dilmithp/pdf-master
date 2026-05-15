@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
    // The "libs" version catalog is auto-loaded from gradle/libs.versions.toml.
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "pdf-master"

include(
    "services:gateway",
    "services:auth-service",
    "services:pdf-core-service",
    "services:pdf-convert-service",
    "services:pdf-ocr-service",
    "services:pdf-ai-service",
    "services:esign-service",
    "services:billing-service",
    "services:notification-service",
)
