import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("de.fayard:dependencies:0.+")
    }
}

bootstrapRefreshVersionsAndDependencies()

include("core")
include("service")
include("inspector")
include("client")
include("cli")
include("examples")

rootProject.name = "tucson"

