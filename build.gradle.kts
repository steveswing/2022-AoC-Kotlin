plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.4.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
