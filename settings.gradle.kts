import java.net.URI

dependencyResolutionManagement {
    repositories {
        maven { url = URI.create("https://repo.osgeo.org/repository/release/") }
        mavenCentral()
    }
}

rootProject.name = "PathFinder"

include(
    ":Core",
    ":Core-Test",
    ":Core-Database",
    ":Database-Test",
    ":Searcher",
    ":Indexer",
    ":Security",
    ":Configuration",
    ":Security-Api",
    ":Messaging",
    ":Core-Web",
    ":Messaging-Test",
    ":Searcher-Api"
)