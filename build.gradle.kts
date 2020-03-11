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
    id("com.github.breadmoirai.github-release") version Versions.com_github_breadmoirai_github_release_gradle_plugin
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

allprojects {

    apply(plugin="java")
    apply(plugin="java-library")
    apply(plugin="com.github.johnrengelman.shadow")

    group = rootProject.group
    version = rootProject.version

    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        testImplementation(Libs.junit)
        testImplementation(Libs.junit_jupiter_api)
        testImplementation(Libs.junit_jupiter_engine)
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
        api(Libs.slf4j_api)
        implementation(Libs.slf4j_jdk14)
//        implementation("ch.qos.logback:logback-parent:1.2.3")
    }
}