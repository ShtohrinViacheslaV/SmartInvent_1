plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.withType<Test> {
    useJUnitPlatform()
}