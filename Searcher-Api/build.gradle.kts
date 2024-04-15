dependencies {
    implementation(project(":Messaging"))
    implementation(project(":Core"))

    implementation(libs.spring.security)
    implementation(libs.spring.amqp)
    implementation(libs.spring.rabbit)
    implementation(libs.spring.validation)

    testImplementation(testFixtures(project(":Core-Test")))
}

tasks.bootJar {
    enabled = false
}
