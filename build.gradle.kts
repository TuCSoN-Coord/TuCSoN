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
    id("org.danilopianini.git-sensitive-semantic-versioning") version Versions.org_danilopianini_git_sensitive_semantic_versioning_gradle_plugin
    id("com.github.johnrengelman.shadow") version Versions.com_github_johnrengelman_shadow_gradle_plugin apply false
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
}

group = "it.unibo.tucson"

gitSemVer {
    minimumVersion.set("0.1.0")
    developmentIdentifier.set("dev")
    noTagIdentifier.set("archeo")
    developmentCounterLength.set(2) // How many digits after `dev`
    version = computeGitSemVer() // THIS IS MANDATORY, AND MUST BE LAST IN THIS BLOCK!
}

println("TuCSoN, version: $version")

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
        testImplementation("junit", "junit", Versions.junit)
        testImplementation("org.junit.jupiter", "junit-jupiter-api", Versions.org_junit_jupiter)
        testImplementation("org.junit.jupiter", "junit-jupiter-engine", Versions.org_junit_jupiter)

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
        api("org.slf4j", "slf4j-api", Versions.slf4j_api)
        implementation("org.slf4j", "slf4j-jdk14", Versions.slf4j_jdk14)
//        implementation("ch.qos.logback:logback-parent:1.2.3")
    }
}