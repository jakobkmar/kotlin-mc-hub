import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("fabric-loom") version "0.12-SNAPSHOT"
    id("io.github.juuxel.loom-quiltflower") version "1.7.2"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "net.axay"
version = "1.0.0"
description = "A minecraft hub mod written in Kotlin"

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:1.19")
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:1.19+build.1:v2"))
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:0.14.6")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.55.1+1.19")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.7.4+kotlin.1.6.21")

    val fabrikVersion = "1.8.0"
    modImplementation("net.axay:fabrikmc-core:$fabrikVersion")
    modImplementation("net.axay:fabrikmc-commands:$fabrikVersion")
    modImplementation("net.axay:fabrikmc-igui:$fabrikVersion")
    modImplementation("net.axay:fabrikmc-persistence:$fabrikVersion")
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}

tasks {
    withType<JavaCompile> {
        options.release.set(17)
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    processResources {
        val props = mapOf(
            "version" to project.version,
            "description" to project.description,
            "mc_version" to "1.19"
        )

        inputs.properties(props)

        filesMatching("fabric.mod.json") {
            expand(props)
        }
    }
}
