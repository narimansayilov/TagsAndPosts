plugins {
    java
    groovy
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

//extra["springCloudVersion"] = "2022.0.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-junit-jupiter:3.11.2")
    testImplementation("org.spockframework:spock-spring:2.3-groovy-4.0")
    testImplementation("io.github.benas:random-beans:3.9.0")
    testImplementation("org.springframework.security:spring-security-test")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // DataBase
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")

    // Feign-client
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.0")

    // Mapper
    implementation("org.mapstruct:mapstruct:1.4.2.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.4.2.Final")

    // Mix
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation(kotlin("script-runtime"))

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.4")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
