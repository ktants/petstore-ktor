plugins {
    id("buildlogic.kotlin.app")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {

    // Ktor
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.host.common)
    implementation(libs.ktor.serialization.kotlinx.json)

    // DB Access
    implementation(libs.h2)
    implementation(libs.postgresql)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)

    // Logging
    runtimeOnly(libs.logback.classic)
    implementation(libs.sl4j.api)
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

