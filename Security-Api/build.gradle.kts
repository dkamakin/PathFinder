dependencies {
    api(project(":Messaging"))
    api(project(":Core-Web"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")

    testImplementation(testFixtures(project(":Core-Test")))
}

tasks.bootJar {
    enabled = false
}
