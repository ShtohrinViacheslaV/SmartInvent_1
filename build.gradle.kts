plugins {
    id("com.android.application") version "8.2.0" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
