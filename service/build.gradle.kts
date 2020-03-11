dependencies {
    api(project(":core"))
}

val mainClass = "alice.tuplecentre.tucson.service.TucsonNodeService"

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainClass)
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