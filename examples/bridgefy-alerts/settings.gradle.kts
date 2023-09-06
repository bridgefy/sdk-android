pluginManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()

//        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven {
            url = java.net.URI("http://34.82.5.94:8081/artifactory/libs-release-local")
            isAllowInsecureProtocol = true
        }

//        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "Bridgefy Remote Control"
include(":app")
