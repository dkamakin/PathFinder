dependencies {
    implementation(project(":Core"))

    implementation(libs.spring.test)
    implementation(libs.spring.jpa)
}

tasks.bootJar {
    enabled = false
}
