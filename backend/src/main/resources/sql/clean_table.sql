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






DROP TABLE InventoryResult CASCADE;
DROP TABLE InventorySession CASCADE;
DROP TABLE InventorySessionStatus CASCADE;
DROP TABLE InventoryProductStatus CASCADE;

DROP TABLE Transaction CASCADE;
DROP TABLE TransactionType CASCADE;

DROP TABLE Product CASCADE;
DROP TABLE Storage CASCADE;
DROP TABLE Category CASCADE;
DROP TABLE Employee CASCADE;
DROP TABLE Role CASCADE;
DROP TABLE Company CASCADE;
