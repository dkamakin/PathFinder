plugins {
    id("com.google.cloud.tools.jib")
}

dependencies {
    api(project(":Security-Api"))
    api(project(":Searcher-Api"))
    implementation(project(":Messaging"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    testImplementation("com.playtika.testcontainers:testcontainers-spring-boot")
    testImplementation("com.playtika.testcontainers:embedded-neo4j")

    testImplementation("org.testcontainers:rabbitmq")

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