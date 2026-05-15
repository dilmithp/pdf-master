plugins {
    `kotlin-dsl`
}

dependencies {
    // Expose the type-safe `libs` version catalog accessor to precompiled script plugins.
    // Workaround documented at https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation("org.springframework.boot:spring-boot-gradle-plugin:${libs.versions.springBoot.get()}")
    implementation("io.spring.gradle:dependency-management-plugin:${libs.versions.springDependencyManagement.get()}")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:${libs.versions.spotless.get()}")
    implementation("com.github.jk1:gradle-license-report:${libs.versions.licenseReport.get()}")
}
