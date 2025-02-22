-- Відключаємо перевірку зовнішніх ключів
SET session_replication_role = 'replica';

-- Очищення таблиць
TRUNCATE TABLE ActionLog RESTART IDENTITY CASCADE;
TRUNCATE TABLE Transaction RESTART IDENTITY CASCADE;
TRUNCATE TABLE Product RESTART IDENTITY CASCADE;
TRUNCATE TABLE Category RESTART IDENTITY CASCADE;
TRUNCATE TABLE Storage RESTART IDENTITY CASCADE;
TRUNCATE TABLE Employee RESTART IDENTITY CASCADE;
TRUNCATE TABLE Company RESTART IDENTITY CASCADE;

-- Включаємо перевірку зовнішніх ключів
SET session_replication_role = 'origin';
