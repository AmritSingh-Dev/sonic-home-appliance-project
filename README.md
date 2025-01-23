# Sonic Home Appliances Store/Management System

## Description

The **Sonic Home Appliance Store/Management System** is a Java-based application that demonstrates the management of home appliances, customer data, and user roles. It provides a robust **web interface** with features such as login authentication (featuring secure password hashing), shopping basket management, and seamless database connection supporting full CRUD operations. This project is structured to demonstrate scalable application development, incorporating modern design patterns (MVC, Singleton etc) and best practices for software engineering.

---

## Motivation

This project aims to serve as a practice example in learning about scalable application development, while also highlighting how technical designs can enhance both the user experience and backend efficiency.

Managing a home appliance store, whether real or simulated, involves multiple complex tasks, from maintaining inventory to processing customer purchases. I created the Sonic Home Appliances Store to tackle these challenges in a way that combines practical functionality with educational value. 

This project isn’t just about building a system; it’s about showcasing how Java can be used to solve real-world problems by implementing robust design patterns and combining them with advanced features like a web interface, authentication, and dynamic shopping basket functionality. By bridging the gap between console-based and web-based interfaces, the project demonstrates the versatility and power of Java technologies in building adaptable and user-focused program.

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
   git clone https://github.com/yourusername/HomeApplianceStore.git
   cd HomeApplianceStore
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
   git clone https://github.com/yourusername/HomeApplianceStore.git
   cd HomeApplianceStore
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

