
# 📦 Інструкція з розгортання SmartInvent_1 у production

Цей документ призначений для Release Engineer / DevOps інженера та описує, як правильно розгорнути SmartInvent_1 у виробничому середовищі.

---

## 🖥️ Вимоги до апаратного забезпечення

| Компонент | Мінімальні вимоги |
|----------|-------------------|
| Архітектура | x86_64 (64-bit) |
| CPU       | 2 ядра @ 2.4GHz+ |
| RAM       | 4 GB             |
| Диск      | 10 GB вільного простору (SSD рекомендовано) |

---

## 🔧 Необхідне програмне забезпечення

- **Java 17+** (OpenJDK або Oracle JDK)
- **PostgreSQL 14+**
- **Gradle** (не обов’язково, можна використовувати `./gradlew`)
- **Git**
- **Сервер додатків** (наприклад, systemd для Linux)
- **Docker** *(опційно, для контейнеризації)*

---

## 🌐 Налаштування мережі

- Відкритий порт `8080` (або інший, якщо вказано у `application.properties`)
- Доступ до бази даних PostgreSQL (локально або через VPN/tunnel)
- Опціонально — доступ до сервісів зовнішніх API (наприклад, сервіс генерації QR-кодів)

---

## 🖥️ Конфігурація серверів

1. **Оновити систему:**
   ```sh
   sudo apt update && sudo apt upgrade -y
   ```

2. **Встановити Java:**
   ```sh
   sudo apt install openjdk-17-jdk -y
   ```

3. **Додати системного користувача:**
   ```sh
   sudo adduser smartinvent
   ```

4. **Клонувати репозиторій та надати права:**
   ```sh
   sudo su - smartinvent
   git clone https://github.com/ShtohrinViacheslaV/SmartInvent_1.git
   cd SmartInvent_1
   ./gradlew build
   ```

---

## 🛢️ Налаштування PostgreSQL

```sql
CREATE DATABASE smartinvent_prod;
CREATE USER smart_prod WITH ENCRYPTED PASSWORD 'prod_pass';
GRANT ALL PRIVILEGES ON DATABASE smartinvent_prod TO smart_prod;
```

🔧 У `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smartinvent_prod
spring.datasource.username=smart_prod
spring.datasource.password=prod_pass
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
server.port=8080
```

---

## 🚀 Розгортання коду

1. **Збірка:**
   ```sh
   ./gradlew clean build
   ```

2. **Запуск з `jar`:**
   ```sh
   java -jar build/libs/SmartInvent_1-0.0.1-SNAPSHOT.jar
   ```

3. **(Рекомендовано) Створити systemd-сервіс:**

```ini
# /etc/systemd/system/smartinvent.service
[Unit]
Description=SmartInvent Production Service
After=network.target

[Service]
User=smartinvent
WorkingDirectory=/home/smartinvent/SmartInvent_1
ExecStart=/usr/bin/java -jar build/libs/SmartInvent_1-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
```

```sh
sudo systemctl daemon-reexec
sudo systemctl enable smartinvent
sudo systemctl start smartinvent
```

---

## ✅ Перевірка працездатності

1. **Перевірити статус сервісу:**
   ```sh
   systemctl status smartinvent
   ```

2. **Перевірити лог:**
   ```sh
   journalctl -u smartinvent -f
   ```

3. **Відкрити у браузері:**
   [http://your_server_ip:8080](http://your_server_ip:8080)

4. **Health check endpoint (якщо реалізовано):**
   ```
   GET /actuator/health
   ```

---

## 📎 Додаткові рекомендації

- Використовуйте **Nginx** або **Apache** як reverse proxy
- Сертифікація HTTPS — через **Let's Encrypt**
- Налаштувати **логування** з ротацією
- Моніторинг — через **Prometheus**, **Grafana** або інші APM-рішення
