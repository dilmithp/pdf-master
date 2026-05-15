plugins {
    base
}

description = "PDF Master — privacy-first PDF productivity SaaS"

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.11"
}
