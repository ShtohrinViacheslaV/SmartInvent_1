plugins {
    java
    id("com.android.application") version "8.2.0" apply false
    id("base") // Додайте плагін base для wrapper
    id("checkstyle")
    id("com.github.spotbugs") version "6.0.3"
}



checkstyle {
    toolVersion = "10.17.0"
    configFile = file("tools/checkstyle/checkstyle.xml")
}

tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

tasks.named("build") {
    dependsOn("checkstyleMain")
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}


spotbugs {
    toolVersion.set("4.8.3")
    showProgress.set(true)
}


tasks.spotbugsMain {
    reports.create("html") {
        required = true
        outputLocation = file("$buildDir/reports/spotbugs.html")
        setStylesheet("fancy-hist.xsl")
    }
}

//
//subprojects {
//    apply(plugin = "checkstyle")
//
//    checkstyle {
//        toolVersion = "10.0"  // Встановіть відповідну версію
//        configFile = rootProject.file("tools/checkstyle/checkstyle.xml")  // Шлях до вашого конфігураційного файлу
//    }
//
//    tasks.withType<Checkstyle>().configureEach {
//        reports {
//            xml.required.set(false)
//            html.required.set(true)
//        }
//    }

//}

//tasks.register<Delete>("clean") {
//    delete(rootProject.buildDir)
//}
//
//
//tasks.register<Wrapper>("wrapper") {
//    gradleVersion = "8.11" // Вказуємо вашу версію Gradle
//}
