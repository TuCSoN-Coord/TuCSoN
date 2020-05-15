import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.github.breadmoirai.githubreleaseplugin.GithubReleaseTask

dependencies {
    api(project(":client"))
}

val mainClass = "alice.tuplecentre.tucson.service.tools.CommandLineInterpreter"
val githubToken by optionalProperties

val shadowJar = tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainClass)
    }
}

if (githubToken != null) {
    rootProject.run {
        configure<GithubReleaseExtension> {
            releaseAssets(*(releaseAssets.toList() + shadowJar).toTypedArray())
        }
        tasks.withType(GithubReleaseTask::class) {
            dependsOn(shadowJar)
        }
    }
}

tasks.register<JavaExec>("runCli") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new Command Line Interface with defaults"

    classpath = sourceSets.getByName("main").runtimeClasspath
    main = mainClass
    standardInput = System.`in`

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}