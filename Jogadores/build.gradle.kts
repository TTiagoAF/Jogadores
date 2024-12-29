plugins {
    alias(libs.plugins.kotlin.jvm) // Usa o plugin Kotlin
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "1.9.10"// Usa o plugin Ktor
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral() // Repositório Maven Central
    google()       // Repositório do Google, se necessário para outras dependências
}

dependencies {
    implementation(libs.ktor.server.core)               // Núcleo do servidor Ktor
    implementation(libs.ktor.server.host.common)        // Recursos comuns de servidor
    implementation(libs.ktor.server.call.logging)       // Logging para chamadas
    implementation(libs.ktor.server.auth)              // Autenticação
    implementation(libs.ktor.server.netty)             // Servidor Ktor usando Netty
    implementation(libs.ktor.server.config.yaml)       // Configuração YAML
    implementation(libs.logback.classic)               // Logging com Logback
    implementation("org.jetbrains.exposed:exposed-core:0.41.1") // Exposed Core
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")  // Exposed DAO
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1") // Exposed JDBC
    implementation("mysql:mysql-connector-java:8.0.33") // Conector MySQL
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation(libs.ktor.server.test.host)      // Testes do servidor
    testImplementation(libs.kotlin.test.junit)          // Testes com JUnit
}
