import org.flywaydb.core.Flyway

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.flywaydb:flyway-core:11.8.2")
        classpath("org.flywaydb:flyway-database-postgresql:11.8.2")
        classpath("org.postgresql:postgresql:42.7.4")
    }
}

plugins {
    java
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.vegetabs"
version = "0.0.1-SNAPSHOT"
description = "iam-service"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // REST API
    implementation("org.springframework.boot:spring-boot-starter-webmvc")

    // GraphQL
    implementation("org.springframework.boot:spring-boot-starter-graphql")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")

    // Security
    implementation("org.springframework.security:spring-security-crypto")

    // MapStruct
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Lombok + MapStruct integration
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    // Monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.2.0")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-starter-graphql-test")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Custom Flyway migration task
tasks.register("flywayMigrate") {
    group = "flyway"
    description = "Runs Flyway database migrations"

    // Providers are tracked by Gradle and stay configuration-cache friendly
    val dbUrl = providers.environmentVariable("DB_URL")
    val dbUser = providers.environmentVariable("DB_USERNAME")
    val dbPassword = providers.environmentVariable("DB_PASSWORD")
    val migrationsDir = layout.projectDirectory.dir("src/main/resources/db/migration").asFile

    doLast {
        Flyway.configure()
            .dataSource(
                dbUrl.orNull ?: error("DB_URL is not set"),
                dbUser.orNull ?: error("DB_USERNAME is not set"),
                dbPassword.orNull ?: error("DB_PASSWORD is not set")
            )
            .locations("filesystem:${migrationsDir.absolutePath}")
            .cleanDisabled(true)
            .load()
            .migrate()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
