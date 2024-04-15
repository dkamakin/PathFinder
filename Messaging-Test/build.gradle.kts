dependencies {
    implementation(project(":Messaging"))
    implementation(project(":Core"))

    implementation(libs.testcontainers.rabbitmq)
    implementation(libs.testcontainers.junit)
    implementation(libs.spring.test)
}

tasks.bootJar {
    enabled = false
}