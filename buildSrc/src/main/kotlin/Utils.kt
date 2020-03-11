import org.gradle.api.Project

val Project.optionalProperties
    get() = OptionalPropertiesDelegate(this)