# Sonic Home Appliance Store/Management System

## 📖 Description

The **Sonic Home Appliance Store/Management System** is a Java-based application that demonstrates the management of home appliances, customer data, and user roles. It provides a robust **web interface** with features such as login authentication (featuring secure password hashing), shopping basket management, and seamless database connection supporting full CRUD operations. This project is structured to demonstrate scalable application development, incorporating modern design patterns (MVC, Singleton etc) and best practices for software engineering.

---

## 🎯 Motivation

This project aims to serve as a practice example in learning about scalable application development, while also highlighting how technical designs can enhance both the user experience and back-end efficiency.

Managing a home appliance store, whether real or simulated, involves multiple complex tasks, from maintaining inventory to processing customer purchases. I created the Sonic Home Appliances Store to tackle these challenges in a way that combines practical functionality with educational value. 

This project isn’t just about building a system; it’s about showcasing how Java can be used to solve real-world problems by implementing robust design patterns and combining them with advanced features like a web interface, authentication, and dynamic shopping basket functionality. By bridging the gap between console-based and web-based interfaces, the project demonstrates the versatility and power of Java technologies in building adaptable and user-focused program.

---

## 🛠️ Technologies

This project implements a variety of technologies and libraries to build and manage its functionality. Below is a breakdown of the technologies used:

### Programming Language
- **Java**: The primary programming language for the application, used for developing back-end logic and implementing object-oriented programming principles.

### Database
- **SQLite**: A lightweight and efficient relational database for storing application data. The project includes a schema file (`appliance.sql`) for database setup.

### Frameworks and Libraries
- **JUnit**: A widely-used testing framework for Java, ensuring code quality and functionality through unit tests.
- **Mockito**: A mocking library for Java, used for testing components in isolation by simulating dependencies.
- **JBcrypt**: For securely hashing passwords, enhancing the application's security.
- **Byte Buddy**: A runtime code generation library, often utilised by mocking frameworks like Mockito.
- **Objenesis**: A library used by Mockito to instantiate objects without calling their constructors.

### Design and Architecture
- **DAO Pattern**: Used for database operations, ensuring clean separation between data access logic and the rest of the application (e.g., `CustomerDAO`, `OrderDAO`, `HomeApplianceDAO`).
- **MVC**: The project adopts an MVC-like structure, with web handlers for the controller layer and DAO classes for the model layer.
	- Controller: The `Controller` class implements the menu system, managing user interactions and coordinating between the Model and View layers.
- **Singleton Pattern**: Used for centralised session management in `LoginSessionManager`, ensuring a single shared instance for active sessions.

### Front-end and Styling
- **Bootstrap**: Front-end framework used for responsive web design and styling in web-based interfaces.
- **HTML/CSS**: Used for any UI components in the project, as seen in the generated documentation or potential static resources.

### Other Tools
- **SQLite JDBC Driver**: A library for interacting with SQLite databases from Java.
- **JavaDoc**: For generating HTML-based documentation for the project's classes and methods.

---

## 🚀 Quick Start

### Prerequisites

Ensure the following are installed:
- **Java Development Kit (JDK)** (version 17 or higher)
- **SQLite JDBC** driver (provided in the `lib` folder)
- A suitable IDE like **Eclipse**, **IntelliJ IDEA**, or a text editor.

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/SonicHomeApplianceProject.git
   cd SonicHomeApplianceStore
   ```
2. Set Up the Database:
	```bash
   sqlite3 appliance.db < src/main/resources/appliance.sql
   ```	

3. Compile the source code:
   ```bash
   javac -d bin -cp lib/* src/main/java/**/*.java
   ```

4. Run the application (web mode):
   ```bash
   java -cp bin:lib/* homeappliance.web.Main
   ```

5. Run the application (console mode):
   ```bash
   java -cp bin:lib/* homeappliance.console.Menu
   ```

5. Access the Web-interface:
	- Open your browser and navigate to http://localhost:8080.

### Running Tests

1. Compile the test files:
   ```bash
   javac -d bin -cp lib/*:src/test/java src/test/java/**/*.java
   ```

2. Run the tests:
   ```bash
   java -cp bin:lib/* org.junit.runner.JUnitCore customers.CustomerDAOTest
   ```

---

## 📖 Usage

### Features

#### Console Menu System/Web Interface:
- Features and operations can be executed via the console menu and we interface.

#### Authentication:
- Users can sign up, log in, and log out using the secure web interface.
- Passwords are securely hashed to ensure user data protection via jbcrypt.

#### Shopping Basket:
- Customers can browse products, add them to their basket, and proceed to checkout.
- The basket supports real-time updates as users add or remove items.

#### Admin Dashboard:
- Administrators can:
  - View and manage all customers and products.
  - Update product details or remove products.
  - Assign roles to users.

#### CRUD Operations:
- **Customers:**
  - Add, update, and delete customer details.
- **Products:**
  - Add, update, and delete appliance details.
- **Orders:**
  - View and manage customer orders.

#### Sorting and Filtering:
- Customers and administrators can sort and filter products or customer records by various attributes, such as price, name, or registration date.

#### Search Functionality:
- A search bar is available for users to quickly find products or customers by name, ID, or other relevant fields.

#### Unit Testing:
- The project includes comprehensive unit tests to ensure the functionality of key features such as database operations, business logic, and user flows.
- Run the tests with:
  ```bash
  mvn test
  ```

---

## 🤝 Contributing

Contributions are welcome! Follow these steps to get started:

1. Clone the repository:
   ```bash
   git clone https://github.com/amritsingh-dev/SonicHomeApplianceProject.git
   cd SonicHomeApplianceProject
   ```

2. Build the project:
   ```bash
   javac -d bin -cp lib/* src/main/java/**/*.java
   ```

3. Run the tests:
   ```bash
   java -cp bin:lib/* org.junit.runner.JUnitCore customers.CustomerDAOTest
   ```

4. Submit a pull request:
   - Fork the repository.
   - Create a new branch:
     ```bash
     git checkout -b feature/YourFeatureName
     ```
   - Commit your changes:
     ```bash
     git commit -m "Add YourFeatureName"
     ```
   - Push your branch:
     ```bash
     git push origin feature/YourFeatureName
     ```
   - Open a pull request to the `main` branch.

