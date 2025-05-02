plugins {
    kotlin("jvm") version "2.1.20"
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.18.0"
}

group = "org.ntqqrev.saltify"
version = "0.1.0"

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
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = rootProject.name

            pom {
                name = "saltify-protobuf"
                description = "ProtoBuf runtime solution for Kotlin JVM"
                url = "https://github.com/SaltifyDev/saltify-protobuf"
                inceptionYear = "2025"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "Wesley-Young"
                        name = "Wesley F. Young"
                        email = "wesley.f.young@outlook.com"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/SaltifyDev/saltify-protobuf.git"
                    developerConnection = "scm:git:ssh://github.com/SaltifyDev/saltify-protobuf.git"
                    url = "https://github.com/SaltifyDev/saltify-protobuf"
                }
            }

            from(components["java"])
        }
    }

    repositories {
        maven {
            url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
        }
    }
}

jreleaser {
    signing {
        setActive("ALWAYS")
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                setActive("ALWAYS")
                create("sonatype") {
                    setActive("ALWAYS")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}