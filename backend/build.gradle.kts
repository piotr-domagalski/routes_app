
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    //alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.serialization)
    application
}

group = "com.example.routeserver"
version = "1.0.0-SNAPSHOT"

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.routeserver.MainKt"
    }
}

application {
    //mainClass = "io.ktor.server.netty.EngineMain"
    mainClass = "com.example.routeserver.MainKt"
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.resources)
    implementation(libs.logback.classic)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.mariadb)
    implementation(libs.hikari)

    implementation(project(":shared"))

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
