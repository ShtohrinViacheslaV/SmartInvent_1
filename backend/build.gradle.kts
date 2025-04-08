plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}


group = "com.smartinvent"
version = "0.0.1-SNAPSHOT"


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // Use a compatible JDK version
    }
}

//java {
//    toolchain {
//        languageVersion = JavaLanguageVersion.of(17)
//    }
//}

//repositories {
//    mavenCentral()
//}


dependencies {
    implementation("org.apache.commons:commons-compress:1.27.1")

    implementation("org.projectlombok:lombok:1.18.30")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.xerial:sqlite-jdbc:3.47.0.0")
    implementation("org.hibernate.orm:hibernate-core:6.6.9.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.6.9.Final")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.springframework.boot:spring-boot-starter:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-security:3.4.2")
//    implementation("org.springframework.boot:spring-boot-starter-data-jdbc:3.4.2")
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.2")

    // Spring REST Docs
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.zxing:javase:3.5.1")

    implementation("org.springframework.boot:spring-boot-devtools:3.4.2")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation("org.flywaydb:flyway-core:11.3.1")

    // Тестові залежності
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.2")
    testImplementation("org.springframework.security:spring-security-test:6.4.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.11.4")
    testImplementation("org.junit.platform:junit-platform-runner:1.11.3")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

