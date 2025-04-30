plugins {
    kotlin("jvm") version "2.1.20"
    `java-library`
    `maven-publish`
}

group = "com.github.SaltifyDev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = rootProject.name

            from(components["java"])
        }
    }
}