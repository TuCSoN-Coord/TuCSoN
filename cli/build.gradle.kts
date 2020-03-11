dependencies {
    api(project(":client"))
}

val mainClass = "alice.tuplecentre.tucson.service.tools.CommandLineInterpreter"

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainClass)
    }
}

tasks.register<JavaExec>("runCli") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new Command Line Interface with defaults"

    classpath = sourceSets.getByName("main").runtimeClasspath
    main = mainClass

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}