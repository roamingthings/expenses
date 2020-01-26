import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	id("de.roamingthings.kotlinspring")
	id("de.roamingthings.integrationtest")
	id("com.bmuschko.docker-spring-boot-application") version "5.2.0"
	idea
}

group = "de.roamingthings.tracing"
version = "0.0.1-SNAPSHOT"

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
}

val wireMockVersion: String by project
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	runtimeOnly("com.h2database:h2")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testIntegrationImplementation("com.github.tomakehurst:wiremock-standalone:$wireMockVersion")
}

tasks.compileJava { dependsOn(tasks.processResources) }

tasks.test {
	useJUnitPlatform()

	systemProperty("spring.profiles.active", "test")

	testLogging {
		events("passed", "skipped", "failed")
	}
}

docker {
	springBootApplication {
		baseImage.set("openjdk:11")
		ports.set(listOf(8080, 5005))
		tag.set("author-service:latest")
		jvmArgs.set(listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"))
	}
}

tasks {
	"dockerCreateDockerfile"(Dockerfile::class) {
		instruction("HEALTHCHECK CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1")
	}
}

tasks.build { dependsOn(tasks.dockerBuildImage) }

idea {
	module {
		testSourceDirs = testSourceDirs + sourceSets["testIntegration"].withConvention(KotlinSourceSet::class) {
			kotlin.srcDirs
		}
		testSourceDirs = testSourceDirs + sourceSets["testIntegration"].resources.srcDirs
		testResourceDirs = testResourceDirs + sourceSets["testIntegration"].resources.srcDirs

		isDownloadJavadoc = true
		isDownloadSources = true
	}
}
