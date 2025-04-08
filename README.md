# 🚀 SmartInvent_1

SmartInvent_1 is an inventory management application designed to help users manage products, categories, and storages efficiently. 
The application allows users to add products, generate unique QR codes for each product, and track product transactions.

## Features

- Add new products with details such as name, description, category, storage, and quantity.
- Generate unique QR codes for products.
- Scan QR codes to retrieve product information.
- Load categories and storages dynamically.
- Save product details and ensure QR code uniqueness.
- Track product transactions.


## Usage

1. Open the application.
2. Use the interface to add new products by filling in the required fields.
3. Generate a QR code for the product.
4. Save the product details.
5. Scan QR codes to retrieve product information.
6. Track product transactions through the application.

## Technologies Used

- **Java**: Main programming language.
- **Kotlin**: Used for some parts of the project.
- **Gradle**: Build automation tool.
- **SQL**: Database management.
- **Spring Boot**: Framework for building the application.


## 🧰 New Developer Guide

### ⚖️ Required Tools

Before you start, make sure you have the following tools installed:

- [Java 17+ JDK](https://adoptium.net/)  
- [Gradle]  
- [Git](https://git-scm.com/)  
- [PostgreSQL 14+](https://www.postgresql.org/)  
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)


### 🌱 Environment Setup

1. Set environment variables or create a `.env` file (optional).
2. Configure the database:

#### 📦 Creating a PostgreSQL database

1. Log in to the PostgreSQL CLI or use pgAdmin:
```sql
CREATE DATABASE smartinvent;
CREATE USER smart_user WITH ENCRYPTED PASSWORD 'smart_pass';
GRANT ALL PRIVILEGES ON DATABASE smartinvent TO smart_user;
```

2. Change `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/smartinvent
spring.datasource.username=smart_user
spring.datasource.password=smart_pass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```


### Installation

To set up the project locally, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/ShtohrinViacheslaV/SmartInvent_1.git
    ```
2. Navigate to the project directory:
    ```sh
    cd SmartInvent_1
    ```
3. Build the project using Gradle:
    ```sh
    ./gradlew build
    ```
4. Run the application:
    ```sh
    ./gradlew bootRun
    ```

After running: [http://localhost:8080](http://localhost:8080)


## 📌 Code Documentation Rules

This project is developed in **Java** and follows industry standards for clean code and documentation.  
We use **Javadoc** for code documentation to ensure clarity and maintainability.

### ✅ **General Guidelines**
- Use **Javadoc** (`/** ... */`) to document code.
- Include **descriptive comments** explaining the purpose and behavior of code.
- Use the standard **Javadoc tags**:
  - `@param` – Describes method parameters.
  - `@return` – Describes the return value.
  - `@throws` – Specifies possible exceptions.
  - `@see` – Links related methods or classes.
  - `@since` – Specifies version information.

### 📝 **Javadoc Example**
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

### ⚙️ **Generating Javadoc**
To generate documentation, run the following command:

```sh
javadoc -encoding UTF-8 -charset UTF-8 -d docs src/*.java
```

🔧 Using IntelliJ IDEA
1. Go to Tools → Generate JavaDoc.
2. Set -encoding UTF-8 -charset UTF-8 in additional options.
3. Click OK.
