plugins {
    id("buildlogic.kotlin.app")
    alias(libs.plugins.ktor)
}

dependencies {

    // External Configuration
    implementation(libs.hoplite.core)
    implementation(libs.hoplite.yaml)

    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.default.headers)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.serialization.jackson)

    // DB Access
    implementation(libs.h2)
    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.hikaricp)

    // Logging
    runtimeOnly(libs.logback.classic)
    implementation(libs.sl4j.api)

    // GSon
    implementation(libs.gson)
}


application {
    mainClass = "petstore.PetStoreAppKt"
    applicationName = "petstore"
    val isDevelopment= project.ext.has("development")
    applicationDefaultJvmArgs += "-Dio.ktor.development=$isDevelopment"
}

dependencies {
    implementation(project(":commons"))
}

