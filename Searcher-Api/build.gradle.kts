dependencies {
    api(project(":Messaging"))
    api(project(":Core"))

    testImplementation(testFixtures(project(":Core-Test")))
}

tasks.bootJar {
    enabled = false
}
