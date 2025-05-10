plugins {
    java
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "be.kdg"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
//    maven { url = uri("https://maven.pkg.dev/") }
//    maven { url = uri("https://maven.pkg.dev/google-cloud-java/public") } // ✅ this one is needed
}

extra["springCloudAzureVersion"] = "5.22.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("com.azure.spring:spring-cloud-azure-starter")
    implementation("com.azure.spring:spring-cloud-azure-starter-jdbc-postgresql")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("io.projectreactor:reactor-test")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // parquet
    implementation ("org.apache.parquet:parquet-avro:1.13.1")
    implementation ("org.apache.hadoop:hadoop-common:3.3.4")
    implementation ("org.apache.hadoop:hadoop-client:3.3.4")

    // json parser
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")



    configurations.all {
        exclude(group = "org.slf4j", module = "slf4j-reload4j")
        exclude(group = "commons-logging", module = "commons-logging")
    }

    implementation("io.github.cdimascio:dotenv-java:3.0.0")

    // SWAGGER
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")


    // GEMINI

//    implementation ("com.google.cloud:google-cloud-aiplatform:3.30.0")
//    implementation ("com.google.cloud:google-cloud-ai-generativelanguage:0.3.0")


}

dependencyManagement {
    imports {
        mavenBom("com.azure.spring:spring-cloud-azure-dependencies:${property("springCloudAzureVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
