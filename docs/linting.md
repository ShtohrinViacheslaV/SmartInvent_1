# 🧹 Linting у SmartInvent

## 1. Обраний інструмент

Ми використовуємо **Checkstyle** — лінтер для Java, який дозволяє перевіряти стиль та структуру коду згідно з обраними правилами. Обраний через просту інтеграцію з Gradle, підтримку Google Java Style та широке поширення в Java-проєктах.

**Чому вибрано Checkstyle?**
- **Стандарт**: Checkstyle є стандартним інструментом для перевірки стилю коду в Java проєктах.
- **Гнучкість**: Checkstyle дозволяє налаштовувати правила відповідно до потреб проєкту.
- **Підтримка спільноти**: Це популярний інструмент, добре задокументований і активно підтримується.

## 2. Базові правила лінтингу

Ось деякі базові правила, що використовуються для перевірки стилю коду:
- **Конвенція іменування**:
    - Імена класів повинні бути в CamelCase і починатися з великої літери (наприклад, `ProductService`).
    - Імена методів повинні починатися з маленької літери, також використовується CamelCase (наприклад, `calculateTotal`).
- **Відступи**: Використовуються відступи у 4 пробіли для кожного рівня вкладеності.
- **Лінії**: Кожна лінія не повинна перевищувати 120 символів.
- **Пробіли**:
    - Пробіли повинні бути після кожного коми, але перед дужками не потрібно ставити пробіли.
    - Перед і після операційних символів має бути один пробіл (наприклад, `a + b`).



## 3. Інструкція з запуску лінтера



### Налаштування

Checkstyle для модуля app підключено в app/build.gradle.kts, із використанням кастомного конфігу:
``` 
checkstyle {
toolVersion = "10.17.0"
configFile = rootProject.file("tools/checkstyle/checkstyle.xml")
}

tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

tasks.named("build") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

tasks.register<Checkstyle>("checkstyleMain") {
    source("src/main/java")
    include("**/*.java")
    classpath = files()
}

tasks.register<Checkstyle>("checkstyleTest") {
    source("src/test/java")
    include("**/*.java")
    classpath = files()
}

```
Checkstyle для модуля backend підключено в backend/build.gradle.kts, із використанням кастомного конфігу:
``` 
checkstyle {
toolVersion = "10.17.0"
configFile = rootProject.file("tools/checkstyle/checkstyle.xml")
}

tasks.named("check") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

tasks.named("build") {
    dependsOn("checkstyleMain", "checkstyleTest")
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required.set(false)
        html.required.set(true)
    }
}

```

### Запуск перевірки модуля app:

``` 
./gradlew :app:checkstyleMain   

./gradlew :app:checkstyleTest   
```

### Запуск перевірки модуля backend:

``` 
./gradlew :backend:checkstyleMain

./gradlew :backend:checkstyleTest
```

## Ігнорування

Певні директорії можна виключити через `exclude` у `build.gradle.kts`, наприклад:
```kotlin
checkstyle {
    sourceSets = listOf(project.sourceSets["main"])
    configFile = rootProject.file("tools/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
}
```

## ✅ Перевірка якості коду

### Git hooks

Перед комітом автоматично запускається скрипт `scripts/code-check.sh`, який перевіряє:
- стиль коду (Checkstyle)
- статичний аналіз (SpotBugs)
- тести

Якщо буде знайдено помилки — коміт буде скасовано.

### Інтеграція з процесом збірки

Команди `./gradlew check` та `./gradlew build` запускають лінтер і статичну перевірку автоматично:

```bash
./gradlew check         # Включає checkstyle + spotbugs
./gradlew build         # check + збірка
