dependencies {
    implementation(project(":Core"))

    implementation(libs.spring.doc)
    implementation(libs.spring.web)
}

tasks.bootJar {
    enabled = false
}
