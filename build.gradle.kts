plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.springframework.boot") version "3.4.2" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false // 👈 Firebase plugin
    id("io.sentry.android.gradle") version "5.3.0" apply false // 👈 Sentry plugin для Android
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
