pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        google()
        jcenter()
        mavenLocal()
    }
}

rootProject.name = "SignConnect"
include (":app", ":lib_interpreter")
