plugins {
    alias(libs.plugins.jib)
}

dependencies {
    implementation(project(":Security-Api"))
    implementation(project(":Searcher-Api"))
    implementation(project(":Messaging"))
    implementation(project(":Core"))
    implementation(project(":Core-Database"))
    implementation(project(":Core-Web"))

    implementation(libs.spring.security)
    implementation(libs.spring.doc)
    implementation(libs.spring.neo4j)
    implementation(libs.spring.web)

    testImplementation(libs.spring.test)
    testImplementation(libs.testcontainers.spring)
    testImplementation(libs.testcontainers.neo4j)
    testImplementation(libs.testcontainers.rabbitmq)
    testImplementation(libs.spring.security.test)

    testImplementation(testFixtures(project(":Messaging-Test")))
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
        image = "${System.getenv("REGISTRY_URL")}/path-finder-searcher:${System.getenv("TAG_NAME")}"
    }
}