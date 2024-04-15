dependencies {
    implementation(project(":Core"))

    implementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar {
    enabled = false
}
