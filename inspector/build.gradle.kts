dependencies {
    api(project(":core"))
    implementation(Libs.slf4j_jdk14)
}

val mainClass = "alice.tuplecentre.tucson.introspection.tools.InspectorGUI"

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainClass)
    }
}

tasks.register<JavaExec>("runInspector") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new InspectorGUI"

    classpath = sourceSets.getByName("main").runtimeClasspath
    main = mainClass

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}