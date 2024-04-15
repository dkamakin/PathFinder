dependencies {
    implementation(project(":Messaging"))
    implementation(project(":Core-Web"))
    implementation(project(":Core"))

    implementation(libs.spring.security)
    implementation(libs.spring.doc)
    implementation(libs.spring.amqp)
    implementation(libs.spring.web)

    testImplementation(libs.spring.test)

    testImplementation(testFixtures(project(":Core-Test")))
}

tasks.bootJar {
    enabled = false
}
