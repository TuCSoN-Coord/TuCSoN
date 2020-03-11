group = "it.unibo.tucson"

buildscript {
    repositories {
        mavenCentral()
        jcenter()
        gradlePluginPortal()
    }
}

plugins {
    java
    `java-library`
    id("org.danilopianini.git-sensitive-semantic-versioning") version "0.2.2"
    id("com.github.johnrengelman.shadow") version "5.2.0" apply false
}

// Apply to All Projects
allprojects {

    apply(plugin="java")
    apply(plugin="java-library")
    apply(plugin="com.github.johnrengelman.shadow")

    // In this section you declare where to find the dependencies of all projects
    repositories {
        jcenter()
        mavenCentral()
    }

    // Common Dependencies to all Projects
    dependencies {

        // Use JUnit test framework
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
        testImplementation("junit:junit:4.12")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")

    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
}

// Dependencies for main project (actually only examples/tests)
dependencies {
    testImplementation(project(":core"))
    testImplementation(project(":service"))
    testImplementation(project(":client"))
}

subprojects {
    dependencies {
        // SLF4J
        implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.9")
        implementation(group = "org.slf4j", name = "slf4j-jdk14", version = "1.7.25")

    }
}