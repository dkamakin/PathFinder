dependencies {
    implementation(project(":Core"))

    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework.amqp:spring-rabbit")
    implementation("org.springframework.amqp:spring-amqp")
}

tasks.bootJar {
    enabled = false
}
