pluginManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm")
        id("org.jetbrains.kotlin.android")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven {
            url = java.net.URI("https://maven.bridgefy.me")
        }
    }
}

rootProject.name = "Bridgefy Remote Control"
include(":app")
