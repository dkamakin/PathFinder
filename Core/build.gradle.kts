dependencies {
    implementation("org.springframework.data:spring-data-jpa")
    implementation("org.geotools:gt-main")
}

tasks.bootJar {
    enabled = false
}
