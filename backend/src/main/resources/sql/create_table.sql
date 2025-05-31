CREATE TABLE Company (
                         company_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255) NOT NULL,
                         phone VARCHAR(50) NOT NULL UNIQUE,
                         email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Role (
                      role_id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL UNIQUE,
                      description TEXT
);

INSERT INTO Role (name, description) VALUES
                                         ('ADMIN', 'Має всі права'),
                                         ('USER', 'Має обмежені права');

CREATE TABLE Employee (
                          employee_id SERIAL PRIMARY KEY,
                          company_id INT NOT NULL REFERENCES Company(company_id) ON DELETE CASCADE,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL UNIQUE,
                          employee_work_id VARCHAR(50) NOT NULL UNIQUE,
                          phone VARCHAR(50) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          role_id INT NOT NULL REFERENCES Role(role_id)
);

CREATE TABLE Category (
                          category_id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL UNIQUE,
                          description TEXT
);

CREATE TABLE Storage (
                         storage_id SERIAL PRIMARY KEY,
                         company_id INT NOT NULL REFERENCES Company(company_id) ON DELETE CASCADE,
                         name VARCHAR(100) NOT NULL UNIQUE,
                         location VARCHAR(255),
                         details TEXT
);

CREATE TABLE Product (
                         product_id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         product_work_id VARCHAR(100) NOT NULL UNIQUE,
                         category_id INT NOT NULL REFERENCES Category(category_id),
                         storage_id INT REFERENCES Storage(storage_id) NOT NULL,
                         price DECIMAL(10,2) CHECK (price > 0),
                         manufacturer VARCHAR(255),
                         expiration_date DATE,
                         weight DECIMAL(10,2) CHECK (weight > 0),
                         dimensions VARCHAR(50)
);

CREATE TABLE TransactionType (
                                 transaction_type_id SERIAL PRIMARY KEY,
                                 name VARCHAR(100) NOT NULL UNIQUE,
                                 description TEXT
);

INSERT INTO TransactionType (name, description) VALUES
                                                    ('ARRIVAL', 'Надходження товару'),
                                                    ('DEPARTURE', 'Вибуття товару'),
                                                    ('UPDATE', 'Оновлення інформації про товар');

CREATE TABLE Transaction (
                             transaction_id SERIAL PRIMARY KEY,
                             transaction_type_id INT NOT NULL REFERENCES TransactionType(transaction_type_id),
                             product_id INT NOT NULL REFERENCES Product(product_id) ON DELETE CASCADE,
                             employee_id INT NOT NULL REFERENCES Employee(employee_id) ON DELETE CASCADE,
                             quantity INT NOT NULL CHECK (quantity > 0),
                             transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE InventoryProductStatus (
                                        status_id SERIAL PRIMARY KEY,
                                        name VARCHAR(50) UNIQUE NOT NULL,
                                        description TEXT
);

INSERT INTO InventoryProductStatus (name, description) VALUES
                                                           ('CONFIRMED', 'Підтверджений товар'),
                                                           ('MODIFIED', 'Змінений товар'),
                                                           ('NOT_FOUND', 'Не знайдений в списку товарів'),
                                                           ('UNCHECKED', 'Не перевірений товар'),
                                                           ('ADDED', 'Новий товар');

CREATE TABLE InventorySessionStatus (
                                        status_id SERIAL PRIMARY KEY,
                                        name VARCHAR(50) UNIQUE NOT NULL,
                                        description TEXT
);

INSERT INTO InventorySessionStatus (name, description) VALUES
                                                           ('ACTIVE', 'Інвентаризація активна'),
                                                           ('COMPLETED', 'Інвентаризація завершена'),
                                                           ('CANCELLED', 'Інвентаризація скасована');





CREATE TABLE InventorySession (
                                  inventory_session_id SERIAL PRIMARY KEY,
                                  session_name VARCHAR(255) NOT NULL,
                                  description TEXT,
                                  employee_id INT NOT NULL REFERENCES Employee(employee_id),
                                  start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  end_time TIMESTAMP CHECK (end_time IS NULL OR end_time > start_time),
                                  inventory_session_status_id INT NOT NULL REFERENCES InventorySessionStatus(status_id)

);



CREATE TABLE InventoryResult (
                                 inventory_result_id SERIAL PRIMARY KEY,
                                 inventory_session_id INT NOT NULL REFERENCES InventorySession(inventory_session_id) ON DELETE CASCADE,
                                 product_id INT NOT NULL REFERENCES Product(product_id) ON DELETE CASCADE,
                                 scanned_by INT NOT NULL REFERENCES Employee(employee_id) ON DELETE CASCADE,
                                 scan_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 status_id INT NOT NULL REFERENCES InventoryProductStatus(status_id),
                                 description TEXT
);