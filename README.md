# SmartInvent_1

SmartInvent_1 is an inventory management application designed to help users manage products, categories, and storages efficiently. 
The application allows users to add products, generate unique QR codes for each product, and track product transactions.

## Features

- Add new products with details such as name, description, category, storage, and quantity.
- Generate unique QR codes for products.
- Scan QR codes to retrieve product information.
- Load categories and storages dynamically.
- Save product details and ensure QR code uniqueness.
- Track product transactions.

## Installation

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

## Code Documentation Rules

Java uses Javadoc, a standard tool for creating documentation. 
Documentation is added in the form of special comments:

/**
* A class that represents a user in the system.
*/
public class User {
    private String name;
    private int age;
    
    /**
    * Constructor of the User class.
    * @param name Username
    * @param age User's age
    */
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

- **Spring Boot**: Framework for building the application.
