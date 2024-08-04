@file:Suppress("UnstableApiUsage")

plugins {
    id("buildlogic.kotlin")
}

dependencies {
    implementation(libs.sl4j.api)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.jackson.datatype.jsr310)
}