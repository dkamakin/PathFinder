dependencies {
    implementation(project(":Messaging"))
    implementation(project(":Core"))

    implementation("org.testcontainers:testcontainers")
    implementation("org.testcontainers:rabbitmq")
    implementation("org.testcontainers:junit-jupiter")
    implementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    enabled = false
}