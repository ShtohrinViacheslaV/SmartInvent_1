# 📄 Генерація документації Javadoc

Ця інструкція допоможе вам згенерувати документацію Javadoc для модуля `backend` у проєкті **SmartInvent**.

---

## ✅ Вимоги

Перед тим як розпочати, переконайтеся, що у вас встановлено:

- **Java JDK** (рекомендовано 17+)
- **IntelliJ IDEA**
- (Опціонально) **Maven** або **Gradle** для командної генерації

---

## 📌 Загальні рекомендації до написання Javadoc

Документуйте всі **публічні класи, методи, інтерфейси та поля**. Використовуйте стандартні теги:

| Тег         | Призначення                                           |
|-------------|--------------------------------------------------------|
| `@param`    | Опис параметра методу                                 |
| `@return`   | Пояснює, що повертає метод                            |
| `@throws`   | Описує винятки, які може викинути метод               |
| `@see`      | Посилання на інші класи/методи                        |
| `@since`    | Вказує, з якої версії доступний клас/метод            |

## ✍️ Приклад Javadoc

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

## ⚙️ Генерація документації через IntelliJ IDEA

1. Перейдіть до **Tools** → **Generate JavaDoc**.
2. Змініть `-encoding UTF-8 -charset UTF-8` в **additional options**.
3. Натисніть **OK**.



1. Відкрийте проєкт SmartInvent в IntelliJ IDEA.
2. Перейдіть до модуля backend.
3. У головному меню оберіть:
**Tools** → **Generate JavaDoc…**
4. У вікні, що з’явиться:
**Output directory: SmartInvent/backend/docs**\
**Scope: backend/src/main/java**\
**Other command line arguments: -encoding UTF-8 -charset UTF-8**\
**Зніміть галочку Include dependencies**
**Натисніть OK**
5. Після генерації відкриється документація (index.html) у браузері.

## 🧪 Генерація документації через командний рядок

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

## 🗂️ Архівація документації

1. Перейдіть у backend/doc
2. Виділіть весь вміст (включаючи index.html)
3. Створіть архів javadoc.zip
4. Завантажте в систему



## ℹ️ Примітки
Генерація працює тільки для backend, оскільки Android SDK (у app модулі) не підтримується стандартним Javadoc без складної конфігурації.
Оновлюйте документацію щоразу після змін у публічних API.

## Висновок

Ця інструкція допоможе вам швидко згенерувати документацію вашого Java-проєкту за допомогою Javadoc. Підтримуйте документацію в актуальному стані та додавайте Javadoc-коментарі до всіх важливих класів і методів.

