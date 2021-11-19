plugins {
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(project(":core"))
    implementation("org.slf4j:slf4j-simple:_")
}

val mainKlass = "alice.tuplecentre.tucson.introspection.tools.InspectorGUI"

val shadowJar = tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes("Main-Class" to mainKlass)
    }
    archiveBaseName.set("${rootProject.name}-${project.name}")
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("redist")
}

tasks.register<JavaExec>("runInspector") {
    dependsOn("classes")
    group = "run"
    description = "Starts a new InspectorGUI"

    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set(mainKlass)

    if ("port" in rootProject.properties) {
        args("-portno", rootProject.properties["port"].toString())
    }
}