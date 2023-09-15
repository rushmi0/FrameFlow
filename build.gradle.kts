plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.videotoimg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // https://mvnrepository.com/artifact/org.bytedeco/ffmpeg
    implementation("org.bytedeco:ffmpeg:5.0-1.5.7")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}