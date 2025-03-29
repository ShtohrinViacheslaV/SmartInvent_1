# Генерація документації Javadoc

Ця інструкція допоможе вам згенерувати документацію Javadoc для проєкту.

## Вимоги

Перед тим як розпочати, переконайтеся, що у вас встановлені:
- **Java JDK**
- **Maven**

## Загальний Guidelines

- Використовуйте **Javadoc** (`/** ... */`), щоб документувати код.
- Додайте **descriptive comments**, що пояснюють призначення та поведінку коду.
- Використовуйте стандартні теги **Javadoc tags**:
 - `@param` – Описує параметри методу.
 - `@return` – описує значення, що повертається.
 - `@throws` – вказує можливі винятки.
 - `@see` – пов’язує пов’язані методи або класи.
 - `@since` – вказує інформацію про версію.

## Приклад Javadoc

```java
/**
 * Represents a user in the system.
 * Stores basic information such as name and age.
 */
public class User {
    private String name;
    private int age;
    
    /**
     * Constructor for new user.
     *
     * @param name User's name.
     * @param age User's age.
     */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Gets the user's name.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }
}
```

## Генерація документації через IntelliJ IDEA

1. Перейдіть до **Tools** → **Generate JavaDoc**.
2. Змініть `-encoding UTF-8 -charset UTF-8` в **additional options**.
3. Натисніть **OK**.

## Генерація документації через командний рядок

1. Відкрийте термінал або командний рядок.
2. Перейдіть до кореневої папки проєкту.
3. Виконайте команду для генерації Javadoc:

   ```sh
   javadoc -encoding UTF-8 -charset UTF-8 -d docs -sourcepath src -subpackages com.yourpackage
   ```
   Де:
   - `-encoding UTF-8` — встановлює кодування для коректного відображення символів.
   - `-charset UTF-8` — встановлює кодування для HTML-документів.
   - `-d docs` — вказує директорію, куди буде збережена документація.
   - `-sourcepath src` — шлях до вихідного коду.
   - `-subpackages com.yourpackage` — включає всі підпакети.

## Висновок

Ця інструкція допоможе вам швидко згенерувати документацію вашого Java-проєкту за допомогою Javadoc. Підтримуйте документацію в актуальному стані та додавайте Javadoc-коментарі до всіх важливих класів і методів.

