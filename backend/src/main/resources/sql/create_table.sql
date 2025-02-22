-- Таблиця Company
CREATE TABLE Company (
                         company_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         address VARCHAR(255) NOT NULL,
                         phone VARCHAR(50) NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблиця Employee
CREATE TABLE Employee (
                          employee_id SERIAL PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(100) NOT NULL,
                          empoyee_work_id INT NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL UNIQUE,
                          role VARCHAR(20) CHECK (role IN ('ADMIN', 'USER'))
);

CREATE TABLE Category (
                          category_id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          product_type VARCHAR(255)
);


-- Таблиця Product
CREATE TABLE Product (
                         product_id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         description TEXT,
                         product_work_id VARCHAR(100),
                         count INT NOT NULL,
                         qrCode BYTEA,
                         category_id INT REFERENCES Category(category_id),
                         storage_id INT REFERENCES Storage(storage_id)

);




-- Таблиця Storage
CREATE TABLE Storage (
                         storage_id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         location VARCHAR(255),
                         details TEXT
);

-- Таблиця Transaction
CREATE TABLE Transaction (
                             transaction_id SERIAL PRIMARY KEY,
                             type VARCHAR(50) CHECK (type IN ('arrivals', 'outgoing', 'transfer', 'inventory')),
                             date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             employee_id INT REFERENCES Employee(employee_id) ON DELETE CASCADE,
                             storage_id INT REFERENCES Storage(storage_id) ON DELETE CASCADE,
                             product_id INT REFERENCES Product(product_id) ON DELETE CASCADE,
                             quantity INT NOT NULL
);

-- Таблиця Backup
CREATE TABLE Backup (
                        backup_id SERIAL PRIMARY KEY,
                        type VARCHAR(50) CHECK (type IN ('cloud', 'local')),
                        date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблиця Printout
CREATE TABLE Printout (
                          printout_id SERIAL PRIMARY KEY,
                          type VARCHAR(50) CHECK (type IN ('lists', 'statistics')),
                          date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблиця ActionLog
CREATE TABLE ActionLog (
                           action_id SERIAL PRIMARY KEY,
                           employee_id INT REFERENCES Employee(employee_id) ON DELETE CASCADE,
                           action_type VARCHAR(50),
                           action_description TEXT,
                           product_id INT REFERENCES Product(product_id) ON DELETE CASCADE,
                           field_name VARCHAR(100) NOT NULL,
                           old_value TEXT,
                           new_value TEXT,
                           timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
