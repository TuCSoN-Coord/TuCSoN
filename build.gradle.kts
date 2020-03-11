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

group = "it.unibo.tucson"

gitSemVer {
    minimumVersion.set("0.1.0")
    developmentIdentifier.set("dev")
    noTagIdentifier.set("archeo")
    developmentCounterLength.set(2) // How many digits after `dev`
    version = computeGitSemVer() // THIS IS MANDATORY, AND MUST BE LAST IN THIS BLOCK!
}


// Apply to All Projects
allprojects {

    apply(plugin="java")
    apply(plugin="java-library")
    apply(plugin="com.github.johnrengelman.shadow")

    group = rootProject.group
    version = rootProject.version

    // In this section you declare where to find the dependencies of all projects
    repositories {
        jcenter()
        mavenCentral()
    }

    // Common Dependencies to all Projects
    dependencies {

        // Use JUnit test framework
        testImplementation("junit:junit:4.12")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")

    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }

    tasks.getByName<Jar>("shadowJar") {
        archiveBaseName.set("tucson-${archiveBaseName.get()}")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("redist")
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
        api(group = "org.slf4j", name = "slf4j-api", version = "1.7.9")
        implementation(group = "org.slf4j", name = "slf4j-jdk14", version = "1.7.25")
//        implementation("ch.qos.logback:logback-parent:1.2.3")
    }
}