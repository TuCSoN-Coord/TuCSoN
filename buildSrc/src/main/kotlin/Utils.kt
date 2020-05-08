import org.gradle.api.Project

private val FULL_VERSION_REGEX = "^[0-9]+\\.[0-9]+\\.[0-9]+$".toRegex()

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

fun Project.subprojects(vararg names: String): List<Project> =
        names.map { project(":$it") }

val Project.isFullVersion: Boolean
    get() = version.toString().matches(FULL_VERSION_REGEX)

fun warn(message: String) {
    System.err.println("WARNING: $message")
}

fun log(message: String) {
    System.out.println("LOG: $message")
}