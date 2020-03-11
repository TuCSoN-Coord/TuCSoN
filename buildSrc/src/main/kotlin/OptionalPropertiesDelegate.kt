import org.gradle.api.Project
import java.io.File
import kotlin.reflect.KProperty

class OptionalPropertiesDelegate(private val project: Project) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        val key = property.name
        if (project.hasProperty(key)) {
            return project.property(key)!!.toString()
        }
        warn("""Property '$key' is not set
            |You are not the maintainer of the ${project.name} project, you can ignore this warning.
            |Otherwise, you can perform one of the following actions:
            |   - add new line in ${project.projectDir}${File.separator}gradle.properties in the form $key=<value> (insecure)
            |   - pass an argument in the form -P$key=<value> when calling gradle(w) (if you are using your on PC)
            |   - export a new environment variable in the form ORG_GRADLE_PROJECT_$key=<value> (if you are setting up CI)
        """.trimMargin())
        return null
    }

    private fun warn(message: String) {
        System.err.println("WARNING: $message")
    }
}