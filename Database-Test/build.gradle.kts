dependencies {
    implementation(project(":Core"))

    implementation(libs.testcontainers.postgresql)
}

tasks.bootJar {
    enabled = false
}
