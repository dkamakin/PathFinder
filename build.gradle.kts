import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring) apply false
    alias(libs.plugins.jib) apply false
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

    dependencies {
        implementation(rootProject.libs.guava)
        implementation(rootProject.libs.mapStruct)

        compileOnly(rootProject.libs.lombok)

        annotationProcessor(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.mapStruct.processor)

        testImplementation(rootProject.libs.assertj)
        testImplementation(rootProject.libs.junit.api)

        testRuntimeOnly(rootProject.libs.junit.engine)

        testAnnotationProcessor(rootProject.libs.lombok)
        testAnnotationProcessor(rootProject.libs.mapStruct.processor)

        testCompileOnly(rootProject.libs.lombok)
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