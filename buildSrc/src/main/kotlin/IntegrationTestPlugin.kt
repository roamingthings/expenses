package de.roamingthings.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet


open class IntegrationTestPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.configureSourceSet()
        project.configureIntegrationTestConfigurations()
        project.configureIntegrationTestTask()
    }
}

internal fun Project.configureSourceSet() {
    val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
    javaConvention.sourceSets {
        create("testIntegration") {
            java.srcDirs("src/integration-test/java")
            resources.srcDirs("src/integration-test/resources")
            withConvention(KotlinSourceSet::class) {
                kotlin.srcDirs("src/integration-test/kotlin")
            }
            compileClasspath += javaConvention.sourceSets["main"].output + javaConvention.sourceSets["test"].output
            runtimeClasspath += javaConvention.sourceSets["main"].output + javaConvention.sourceSets["test"].output
        }
    }
}

internal fun Project.configureIntegrationTestConfigurations() {
    configurations.getByName("testIntegrationImplementation") {
        extendsFrom(configurations.findByName("testImplementation"))
    }
    configurations["testIntegrationRuntimeOnly"].extendsFrom(configurations.findByName("runtimeOnly"))
}

internal fun Project.configureIntegrationTestTask() {
    val integrationTest = task<Test>("integrationTest") {
        description = "Runs integration tests."
        group = "verification"

        useJUnitPlatform()

        systemProperty("spring.profiles.active", "integrationtest")

        val javaConvention = project.convention.getPlugin(JavaPluginConvention::class.java)
        testClassesDirs = javaConvention.sourceSets["testIntegration"].output.classesDirs
        classpath = javaConvention.sourceSets["testIntegration"].runtimeClasspath

        testLogging {
            events("passed", "skipped", "failed")
        }

        shouldRunAfter("test")
    }

    tasks.getByName("check") { dependsOn(integrationTest) }
}
