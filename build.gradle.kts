plugins {
    java
    `java-library`
    id("org.danilopianini.git-sensitive-semantic-versioning")
    id("maven-publish")
    signing
    id("com.github.johnrengelman.shadow")
}

group = "it.unibo.tucson"

gitSemVer {
    minimumVersion.set("0.1.0")
    developmentIdentifier.set("dev")
    noTagIdentifier.set("archeo")
    fullHash.set(false) // set to true if you want to use the full git hash
    maxVersionLength.set(Int.MAX_VALUE) // Useful to limit the maximum version length, e.g. Gradle Plugins have a limit on 20
    developmentCounterLength.set(2) // How many digits after `dev`
    enforceSemanticVersioning.set(true) // Whether the plugin should stop if the resulting version is not a valid SemVer, or just warn
    // The separator for the pre-release block.
    // Changing it to something else than "+" may result in non-SemVer compatible versions
    preReleaseSeparator.set("-")
    // The separator for the build metadata block.
    // Some systems (notably, the Gradle plugin portal) do not support versions with a "+" symbol.
    // In these cases, changing it to "-" is appropriate.
    buildMetadataSeparator.set("+")
    distanceCounterRadix.set(36) // The radix for the commit-distance counter. Must be in the 2-36 range.
    // A prefix on tags that should be ignored when computing the Semantic Version.
    // Many project are versioned with tags named "vX.Y.Z", de-facto building valid SemVer versions but for the leading "v".
    // If it is the case for some project, setting this property to "v" would make these tags readable as SemVer tags.
    versionPrefix.set("")
    assignGitSemanticVersion()
}

logger.lifecycle("{}, version: {}", rootProject.name, version)

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
    }

    java {
        withSourcesJar()
        withJavadocJar()
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
        testImplementation("junit:junit:_")
        testImplementation("org.junit.jupiter:junit-jupiter-api:_")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:_")
    }

    apply(rootProject.file("maven-publication.gradle.kts"))
}

dependencies {
    api(project(":core"))
    api(project(":service"))
    api(project(":client"))
    testImplementation("org.slf4j:slf4j-simple:_")
}

tasks.getByName<Jar>("shadowJar") {
    archiveBaseName.set("${rootProject.name}-full")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("redist")
}

tasks.create("version") {
    doLast {
        println(rootProject.version)
    }
}
