dependencies {
    implementation(libs.spring.transaction)
}

tasks.bootJar {
    enabled = false
}
