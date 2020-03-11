import org.gradle.api.Project

val Project.optionalProperties
    get() = OptionalPropertiesDelegate(this)

fun Iterable<Project>.forEachProject(action: Project.() -> Unit) {
    this.forEach { it.action() }
}

fun Project.subprojects(vararg names: String, action: Project.() -> Unit) {
    for (name in names) {
        project(":$name", action)
    }
}