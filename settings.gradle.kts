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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SRI Movil Android"
include(":app")
include(":common:domain")
include(":common:data")
include(":common:framework")
include(":feature:estado_tributario")
include(":feature:deudas")
include(":feature:deudas:data")
include(":feature:deudas:domain")
include(":feature:deudas:di")
