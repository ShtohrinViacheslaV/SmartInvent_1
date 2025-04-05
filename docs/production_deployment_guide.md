# Production Deployment Guide (Windows)

Інструкція для Release Engineer / DevOps щодо розгортання SmartInvent у production-середовищі на Windows.

---

## 🖥️ 1. Вимоги до апаратного забезпечення

### Сервер (Windows машина з backend і PostgreSQL)

| Компонент | Мінімальні вимоги           |
|-----------|-----------------------------|
| ОС        | Windows 10/11 або Server 2019+ |
| CPU       | 2 ядра (рекомендовано 4)    |
| RAM       | 4 ГБ (рекомендовано 8 ГБ)   |
| Диск      | 20 ГБ SSD (рекомендовано 50+ ГБ для БД і логів) |

---

## 💽 2. Необхідне програмне забезпечення

- Java Development Kit (JDK) 17: https://www.java.com/releases/
- PostgreSQL 13+: https://www.postgresql.org/download/windows/
- Git for Windows: https://git-scm.com/downloads
- Gradle (або використовувати `gradlew.bat`)
- (Опціонально) Docker Desktop
---

## 🌐 3. Налаштування мережі

- Відкрити порт **8080** у фаєрволі Windows (або порт, який ви використовуєте для бекенду)
- Відкрити порт **5432** для PostgreSQL (тільки для внутрішніх підключень!)
- Рекомендується використовувати статичну IP-адресу або налаштувати домен

---

## 🧰 4. Налаштування PostgreSQL

### Під час встановлення:

- Оберіть пароль для `postgres`
- Дозвольте підключення через мережу (у `pg_hba.conf` дозволити md5)
- У `postgresql.conf` змінити:

    - `listen_addresses = '*'` (для доступу з інших машин)
    - `port = 5432` (або інший, якщо потрібно)


## 🚀 5. Розгортання коду

### Варіант 1: Через Git + Gradle Wrapper

1. **Клонуйте репозиторій** на сервер

   Використовуйте Git для клонування репозиторію:

   ```bash
   git clone https://github.com/ShtohrinViacheslaV/SmartInvent_1.git
    ```
   
2.   Перейдіть до каталогу з бекендом:

   ```bash
    cd SmartInvent_1\backend
    ```
3.   Побудуйте проект за допомогою Gradle Wrapper:

   ```bash
    gradlew.bat build
    ```
4.   Запустіть додаток:

   ```bash
    java -jar build\libs\backend-0.0.1-SNAPSHOT.jar
    ```
5.   Тепер сервер буде доступний за адресою:

   ```bash
    http://<IP_адреса>:8080
    ```


### Варіант 2: Використання Docker для розгортання

1. **Встановіть Docker**   (https://www.docker.com/products/docker-desktop)
2. **Створіть Dockerfile у бекенді:**
   ```bash
   FROM openjdk:17-jdk-slim

    WORKDIR /app
    COPY . /app

    RUN ./gradlew build

    CMD ["java", "-jar", "build/libs/backend-0.0.1-SNAPSHOT.jar"]

    ```
3. **Побудуйте образ:**

   ```bash
    docker build -t smartinvent-backend .
    ```
4. **Запустіть контейнер:**

   ```bash
    docker run -p 8080:8080 smartinvent-backend
    ```



## ✅ 6. Перевірка працездатності

### Перевірка API:

1.   GET-запит до `/api/health`:

    http://<IP_адреса>:8080/api/health


2.   Перевірка PostgreSQL:

    psql -h <IP_адреса> -U smartuser -d smartinvent


3.  Перевірка Android-додатку
    - Встановіть APK на Android-пристрій
    - Введіть IP-адресу сервера у налаштуваннях додатку
    - Перевірте, правильність URL:
    

    private static final String BASE_URL = "http://<IP_адреса>:8080/";

