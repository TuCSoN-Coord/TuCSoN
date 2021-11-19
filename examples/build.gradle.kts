dependencies {
    api(project(":client"))
}

tasks.register<JavaExec>("runMaster") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("masterWorkers.MasterAgent")
}

tasks.register<JavaExec>("runWorker") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("masterWorkers.WorkerAgent")
}

tasks.register<JavaExec>("runPhilos") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("diningPhilos.DiningPhilosophersTest")
}

tasks.register<JavaExec>("runTimedPhilos") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("timedDiningPhilos.TDiningPhilosophersTest")
}

tasks.register<JavaExec>("runProvider") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("loadBalancing.ServiceProvider")
}

tasks.register<JavaExec>("runRequestor") {
    group = "example"
    dependsOn("classes")
    classpath = sourceSets.getByName("main").runtimeClasspath
    mainClass.set("loadBalancing.ServiceRequestor")
}
