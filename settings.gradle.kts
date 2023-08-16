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
        maven { url = uri("https://www.jitpack.io" ) }
        //twitter
        maven { url = uri("https://jcenter.bintray.com" ) }
    }
}

rootProject.name = "AllVideoDownloader"
include(":app")
include(":youtubeExtractor")
