plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(project(":client"))
    implementation("org.slf4j:slf4j-nop:_")
}

val mainKlass = "alice.tuplecentre.tucson.service.tools.CommandLineInterpreter"

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainKlass)
    }
    archiveBaseName.set("${rootProject.name}-${project.name}")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("redist")
}

tasks.register<JavaExec>("runCli") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new Command Line Interface with defaults"

    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set(mainKlass)
    standardInput = System.`in`

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}