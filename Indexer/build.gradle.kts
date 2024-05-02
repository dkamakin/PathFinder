plugins {
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":Core"))
    implementation(project(":Core-Database"))
    implementation(project(":Searcher-Api"))

    implementation(libs.osmapi.core)
    implementation(libs.osmapi.map)
    implementation(libs.osmapi.overpass)

    implementation(libs.spring.retry)
    implementation(libs.spring.jpa)
    implementation(libs.spring.cloud.config.client)
    implementation(libs.spring.validation)
    implementation(libs.postgresql)

    testImplementation(libs.spring.test)
    testImplementation(libs.spring.security.test)

    testImplementation(testFixtures(project(":Messaging-Test")))
    testImplementation(testFixtures(project(":Core-Test")))
    testImplementation(testFixtures(project(":Database-Test")))
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