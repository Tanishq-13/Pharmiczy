pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()

    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")
//        mavenCentral()
        maven(url = "https://jcenter.bintray.com")
        maven(url = "https://storage.zero.im/maven")
        maven(url="https://maven.google.com")

    }
}

rootProject.name = "Pharmiczy"
include(":app")
 