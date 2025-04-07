
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)

}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "com.example.ApplicationKt"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}
//kotlin {
//    jvmToolchain(17) // Target Java 17 (LTS version)
//}
//
//// OR for older projects
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions.jvmTarget = "17"
//}

dependencies {
  val  exposed_version="0.31.1"
    val hikaricp_version="4.0.3"
    val postgres_version="42.2.75"

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
//    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.server.content.negotiation)
//    implementation(libs.postgresql)
//    implementation(libs.h2)
    implementation("com.h2database:h2:2.2.224") // or the latest version

//    implementation(libs.ktor.serialization.gson)
    implementation(libs.logback.classic)
//    implementation(libs.ktor.server.content.negotiation)
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation ("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//    implementation ("com.zaxxer:HikariCP:$hikaricp_version")

    implementation("io.ktor:ktor-server-content-negotiation")
//    implementation(libs.ktor.serialization.kotlinx.json)
//    implementation(libs.ktor.server.content.negotiation)
//    implementation(libs.postgresql)
//    implementation(libs.h2)
//    implementation(libs.ktor.serialization.gson)
    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Ktor
    implementation("io.ktor:ktor-server-core:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
    implementation("io.ktor:ktor-server-cors-jvm:3.0.3") // CORS Plugin


    // Kotlinx Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")




}
