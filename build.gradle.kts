import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-simple:1.7.25")

    implementation("org.hexworks.zircon:zircon.core-jvm:2021.1.0-RELEASE")
    implementation("org.hexworks.zircon:zircon.jvm.swing:2021.1.0-RELEASE")

    implementation(kotlin("stdlib-jdk8"))

    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-all:1.10.19")
    testImplementation("org.assertj:assertj-core:3.6.2")

    implementation("org.hexworks.amethyst:amethyst.core-jvm:2020.1.1-RELEASE")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("zircon.skeleton.kotlin")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.example.MainKt"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "com.example.MainKt"
    }
}


