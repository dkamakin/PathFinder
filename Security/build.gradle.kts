plugins {
    id("com.google.cloud.tools.jib")
}
dependencies {
    implementation(project(":Security-Api"))
    implementation(project(":Messaging"))

    implementation("com.auth0:java-jwt")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
    implementation("org.springframework.amqp:spring-amqp")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    testImplementation("org.testcontainers:rabbitmq")

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
        image = "${System.getenv("REGISTRY_URL")}/path-finder-security:${System.getenv("TAG_NAME")}"
    }
}