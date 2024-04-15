dependencies {
    implementation(project(":Core"))

    implementation(libs.spring.json)
    implementation(libs.spring.amqp)
    implementation(libs.spring.rabbit)
    implementation(libs.spring.validation)
}

tasks.bootJar {
    enabled = false
}
