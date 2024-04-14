dependencies {
    api(project(":Core"))

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui")
}

tasks.bootJar {
    enabled = false
}
