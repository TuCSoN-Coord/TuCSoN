import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

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
    id("maven-publish")
    signing
    id("com.jfrog.bintray") version Versions.com_jfrog_bintray_gradle_plugin
}

group = "it.unibo.tucson"

gitSemVer {
    minimumVersion.set("0.1.0")
    developmentIdentifier.set("dev")
    noTagIdentifier.set("archeo")
    developmentCounterLength.set(2) // How many digits after `dev`
    version = computeGitSemVer() // THIS IS MANDATORY, AND MUST BE LAST IN THIS BLOCK!
}

val gcName by optionalProperties
val gcEmail by optionalProperties
val gcUrl by optionalProperties
val projectHomepage by optionalProperties
val bintrayRepo by optionalProperties
val bintrayUserOrg by optionalProperties
val projectLicense by optionalProperties
val projectLicenseUrl by optionalProperties
val projectIssues by optionalProperties
val githubOwner by optionalProperties
val githubRepo by optionalProperties
val signingKey by optionalProperties
val signingPassword by optionalProperties
val bintrayUser by optionalProperties
val bintrayKey by optionalProperties
val ossrhUsername by optionalProperties
val ossrhPassword by optionalProperties
val githubToken by optionalProperties

println("TuCSoN, version: $version")

allprojects {

    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "com.jfrog.bintray")

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

    createMavenPublications("main", "java")
    configureSigning()
    configureUploadToBintray()
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
        runtimeOnly(Libs.slf4j_jdk14)
    }
}

println(subprojects().map { it.tasks.getByName<Jar>("shadowJar") })

if (githubToken != null) {

    configure<GithubReleaseExtension> {
        if (githubToken != null) {
            token(githubToken)
            owner(githubOwner)
            repo(githubRepo)
            tagName { version.toString() }
            releaseName { version.toString() }
            allowUploadToExisting { true }
            prerelease { false }
            draft { false }
            overwrite { false }
            try {
                body(
                    """|## CHANGELOG
                       |${changelog().call()}
                       """.trimMargin()
                )
            } catch (e: NullPointerException) {
                e.message?.let { warn(it) }
            }
        }
    }
}

fun Project.configureSigning() {

    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
        signing {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publishing.publications)
        }
    }

    publishing {
        val signAllTask = task<Sign>("signAllPublications")
        for (it in publications.withType<MavenPublication>()) {
            signAllTask.dependsOn(it)
        }
    }
}

fun Project.configureUploadToBintray() {

    val publishAllToBintrayTask = rootProject.tasks.maybeCreate("publishAllToBintray").also {
        it.group = "publishing"
    }

    publishing {
        bintray {
            user = bintrayUser
            key = bintrayKey
            setPublications(*project.publishing.publications.withType<MavenPublication>().map { it.name }.toTypedArray())
            override = true
            with(pkg) {
                repo = bintrayRepo
                name = project.name
                userOrg = bintrayUserOrg
                vcsUrl = projectHomepage
                setLicenses(projectLicense)
                with(version) {
                    name = project.version.toString()
                }
            }
        }
    }
    this.tasks.withType<BintrayUploadTask> {
        publishAllToBintrayTask.dependsOn(this)
    }
}

fun Project.createMavenPublications(name: String, vararg componentsStrings: String, docArtifact: String? = null) {

    val sourcesJar by tasks.creating(Jar::class) {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("sources")
    }

    publishing {
        publications.create<MavenPublication>(name) {
            groupId = project.group.toString()
            version = project.version.toString()

            for (component in componentsStrings) {
                from(components[component])
            }

            if (docArtifact != null && docArtifact in tasks.names) {
                artifact(tasks.getByName(docArtifact)) {
                    classifier = "javadoc"
                }
            } else if (docArtifact == null || !docArtifact.endsWith("KotlinMultiplatform")) {
                log(
                        "no javadoc artifact for publication $name in project ${project.name}: " +
                                "no such a task: $docArtifact"
                )
            }

            artifact(sourcesJar)

            configurePom(project.name)
        }
    }
}

fun MavenPublication.configurePom(projectName: String) {
    pom {
        name.set("TuCSoN -- Module `${projectName.capitalize()}`")
        description.set("Tuple Centres Spread over the Network")
        url.set(projectHomepage)
        licenses {
            license {
                name.set(projectLicense)
                url.set(projectLicenseUrl)
            }
        }

        developers {
            developer {
                name.set(gcName)
                email.set(gcEmail)
                url.set(gcUrl)
                organization.set("University of Bologna")
                organizationUrl.set("https://www.unibo.it/en")
            }
            developer {
                name.set("Stefano Mariani")
                email.set("s.mariani@unimore.it")
                url.set("https://personale.unimore.it/rubrica/dettaglio/s.mariani")
                organization.set("University of Modena and Reggio-Emilia")
                organizationUrl.set("https://www.unimore.it/")
            }
        }

        scm {
            connection.set("scm:git:git:///github.com/TuCSoN-Coord/TuCSoN.git")
            url.set("https://github.com/TuCSoN-Coord/TuCSoN")
        }
    }
}