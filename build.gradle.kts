import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "cl.gringraz"
version = "1.0.0"

plugins {
    application
    kotlin("jvm") version "1.3.72"
}

repositories {
    mavenCentral()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    val ktorVersion = "1.3.2"
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation(group = "junit", name = "junit", version = "4.12")
}
