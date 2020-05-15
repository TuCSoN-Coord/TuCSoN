import com.github.breadmoirai.githubreleaseplugin.GithubReleaseExtension
import com.github.breadmoirai.githubreleaseplugin.GithubReleaseTask

dependencies {
    api(project(":core"))
//    implementation(Libs.slf4j_jdk14)
}

val mainClass = "alice.tuplecentre.tucson.service.TucsonNodeService"
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

tasks.register<JavaExec>("runNode") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new TucsonNode with defaults"

    classpath = sourceSets.getByName("main").runtimeClasspath
    main = mainClass

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}