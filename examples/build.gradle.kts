dependencies {
    api(project(":client"))
}

tasks.register<JavaExec>("runMaster") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "masterWorkers.MasterAgent"
}

tasks.register<JavaExec>("runWorker") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "masterWorkers.WorkerAgent"
}

tasks.register<JavaExec>("runPhilos") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "diningPhilos.DiningPhilosophersTest"
}

tasks.register<JavaExec>("runTimedPhilos") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "timedDiningPhilos.TDiningPhilosophersTest"
}

tasks.register<JavaExec>("runProvider") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "loadBalancing.ServiceProvider"
}

tasks.register<JavaExec>("runRequestor") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    main = "loadBalancing.ServiceRequestor"
}
