import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("org.springframework.boot") version "3.2.2" apply false
    id("com.google.cloud.tools.jib") version "3.4.2" apply false
    id("java")
    id("java-test-fixtures")
    id("java-library")
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "java-library")
    apply(plugin = "java-test-fixtures")

    val lombokVersion = "1.18.30"
    val assertJVersion = "3.25.3"
    val h2Version = "2.2.224"
    val postgresqlVersion = "42.7.2"
    val guavaVersion = "33.0.0-jre"
    val springRetryVersion = "2.0.5"
    val springActuatorVersion = "3.2.3"
    val springCloudBusVersion = "4.1.0"
    val apacheCommonsVersion = "3.14.0"
    val mapStructVersion = "1.5.5.Final"
    val springSecurityTestVersion = "6.2.2"
    val springBootVersion = "3.2.2"
    val springMonitorVersion = "4.1.0"
    val geoToolsVersion = "30.2"
    val westnordostVersion = "3.0"
    val testcontainersVersion = "1.19.4"
    val springCloudConfigVersion = "4.1.0"
    val springDocVersion = "2.3.0"
    val springAmqpVersion = "3.1.1"
    val jwtVersion = "4.4.0"
    val testcontainersSpringVersion = "3.1.5"

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-json:$springBootVersion")
        implementation("org.springframework.amqp:spring-rabbit:$springAmqpVersion")
        implementation("org.springframework.amqp:spring-amqp:$springAmqpVersion")
        implementation("de.westnordost:osmapi-core:$westnordostVersion")
        implementation("de.westnordost:osmapi-map:$westnordostVersion")
        implementation("de.westnordost:osmapi-overpass:$westnordostVersion")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
        implementation("org.springframework.cloud:spring-cloud-starter-config:$springCloudConfigVersion")
        implementation("org.springframework.data:spring-data-jpa:$springBootVersion")
        implementation("org.geotools:gt-main:$geoToolsVersion")
        implementation("org.springframework.cloud:spring-cloud-config-monitor:$springMonitorVersion")
        implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit:$springMonitorVersion")
        implementation("org.springframework.cloud:spring-cloud-config-server:$springCloudConfigVersion")
        implementation("org.mapstruct:mapstruct:$mapStructVersion")
        implementation("org.springframework.boot:spring-boot-starter-validation:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-aop:$springBootVersion")
        implementation("org.springframework.retry:spring-retry:$springRetryVersion")
        implementation("org.apache.commons:commons-lang3:$apacheCommonsVersion")
        implementation("org.springframework.boot:spring-boot-starter-actuator:$springActuatorVersion")
        implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")
        implementation("org.postgresql:postgresql:$postgresqlVersion")
        implementation("com.google.guava:guava:$guavaVersion")
        implementation("org.springframework.boot:spring-boot-starter-security:$springBootVersion")
        implementation("com.auth0:java-jwt:$jwtVersion")
        implementation("org.springframework.boot:spring-boot-starter-data-neo4j:$springBootVersion")

        api("org.springframework.cloud:spring-cloud-starter-bus-amqp:$springCloudBusVersion")

        compileOnly("org.projectlombok:lombok:$lombokVersion")

        annotationProcessor("org.projectlombok:lombok:$lombokVersion")
        annotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

        testRuntimeOnly("com.h2database:h2:$h2Version")
        testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
        testImplementation("org.testcontainers:rabbitmq:$testcontainersVersion")
        testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
        testImplementation("org.assertj:assertj-core:$assertJVersion")
        testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
        testImplementation("org.springframework.security:spring-security-test:$springSecurityTestVersion")
        testImplementation("com.playtika.testcontainers:testcontainers-spring-boot:$testcontainersSpringVersion")
        testImplementation("com.playtika.testcontainers:embedded-neo4j:$testcontainersSpringVersion")

        testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
        testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapStructVersion")

        testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    }

    tasks.test {
        useJUnitPlatform()

        testLogging {
            events(
                TestLogEvent.FAILED,
                TestLogEvent.PASSED,
                TestLogEvent.SKIPPED,
            )
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true

            debug {
                events(
                    TestLogEvent.STARTED,
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_ERROR,
                    TestLogEvent.STANDARD_OUT
                )
                exceptionFormat = TestExceptionFormat.FULL
            }

            info.events = debug.events
            info.exceptionFormat = debug.exceptionFormat
        }
    }

}

tasks.wrapper {
    version = "8.7"
}