plugins {
    id("com.google.cloud.tools.jib")
}

dependencies {
    api(project(":Core"))
    api(project(":Searcher-Api"))

    implementation("de.westnordost:osmapi-core")
    implementation("de.westnordost:osmapi-map")
    implementation("de.westnordost:osmapi-overpass")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    testImplementation(testFixtures(project(":Messaging-Test")))
    testImplementation(testFixtures(project(":Core-Test")))
}

jib {
    from {
        image = "eclipse-temurin:21.0.1_12-jre-jammy"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }

    to {
        image = "${System.getenv("REGISTRY_URL")}/path-finder-indexer:${System.getenv("TAG_NAME")}"
    }
}