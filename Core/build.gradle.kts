dependencies {
    implementation(libs.geoTools)
    implementation(libs.spring.aop)
    implementation(libs.spring.json)
}

tasks.bootJar {
    enabled = false
}
