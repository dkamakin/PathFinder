plugins {
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(libs.spring.cloud.monitor)
    implementation(libs.spring.cloud.stream)
    implementation(libs.spring.cloud.config.server)
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
        image = "${System.getenv("REGISTRY_URL")}/path-finder-configuration:${System.getenv("TAG_NAME")}"
    }
}