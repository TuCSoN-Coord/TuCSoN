apply(plugin = "maven-publish")
apply(plugin = "signing")

// env ORG_GRADLE_PROJECT_signingKey
val signingKey: String? by project
// env ORG_GRADLE_PROJECT_signingPassword
val signingPassword: String? by project
// env ORG_GRADLE_PROJECT_mavenRepo
val mavenRepo: String? by project
// env ORG_GRADLE_PROJECT_mavenUsername
val mavenUsername: String? by project
// env ORG_GRADLE_PROJECT_mavenPassword
val mavenPassword: String? by project

val gcName: String by project

val gcEmail: String by project

val gcUrl: String by project

val projectName: String by project

val projectHomepage: String by project

val projectLicense: String by project

val projectLicenseUrl: String by project

val projectIssues: String by project


project.configure<PublishingExtension> {
    repositories {
        maven {
            if (mavenRepo != null) {
                url = uri(mavenRepo)
            }
            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }

    project.afterEvaluate {
        publications.create<MavenPublication>("maven") {
            groupId = project.group.toString()
            version = project.version.toString()

            from(project.components.getByName("java"))

            pom {
                name.set("$projectName -- Module `${project.name.capitalize()}`")
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
    }

    configure<SigningExtension> {
        if (arrayOf(signingKey, signingPassword).none { it.isNullOrBlank() }) {
            useInMemoryPgpKeys(signingKey, signingPassword)
            sign(publications)
        }

        val signAll = project.tasks.create("signAllPublications")
        project.tasks.withType<Sign> {
            signAll.dependsOn(this)
        }
    }
}
