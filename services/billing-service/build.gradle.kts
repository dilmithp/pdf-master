plugins {
    id("pdfmaster.spring-mvc-conventions")
    id("pdfmaster.spring-data-jpa-conventions")
    id("pdfmaster.testcontainers-conventions")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

description = "Subscription billing, Stripe integration, and webhook ingestion."

dependencies {
    implementation("com.stripe:stripe-java:${libs.versions.stripeJava.get()}")
}
