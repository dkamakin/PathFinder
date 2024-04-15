plugins {
    alias(libs.plugins.jib)
}
dependencies {
    implementation(project(":Security-Api"))
    implementation(project(":Messaging"))
    implementation(project(":Core"))
    implementation(project(":Core-Database"))
    implementation(project(":Core-Web"))

    implementation(libs.jwt)
    implementation(libs.spring.doc)
    implementation(libs.spring.amqp)
    implementation(libs.spring.jpa)
    implementation(libs.spring.security)
    implementation(libs.spring.cloud.config.client)
    implementation(libs.postgresql)

    testImplementation(libs.testcontainers.rabbitmq)
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
        image = "${System.getenv("REGISTRY_URL")}/path-finder-security:${System.getenv("TAG_NAME")}"
    }
}