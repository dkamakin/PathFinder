dependencies {
    implementation(libs.spring.jpa)
}

tasks.bootJar {
    enabled = false
}
