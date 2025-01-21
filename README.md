# Home Appliance Store Store/Management System

## Description

The **Home Appliance Store Store/Management System** is a Java-based application that demonstrates the use of design patterns such as **MVC** and advanced features like a **web interface** with login authentication, a shopping basket (add, remove, checkout), and a console-based menu system. It offers a seamless user experience for both a web-based frontend and a command-line backend, showcasing the versatility of Java technologies in real-world scenarios.

---

## Why?

Managing a home appliance store involves complex workflows like:
- Maintaining an up-to-date inventory.
- Simplifying order management and customer interactions.
- Providing a secure login system for customers and admins.
- Streamlining the checkout process through a robust shopping basket feature.

This project was born out of the need to integrate multiple functionalities into a single application. Whether you're an admin overseeing operations or a customer making purchases, this system caters to all needs while adhering to good software design principles.

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

2. Compile the source code:
   ```bash
   javac -d bin -cp lib/* src/main/java/**/*.java
   ```

3. Run the application (web mode):
   ```bash
   java -cp bin:lib/* homeappliance.web.Main
   ```

4. Run the application (console mode):
   ```bash
   java -cp bin:lib/* homeappliance.console.Menu
   ```

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

- **Customer Management**:
  - Add, update, delete, and view customer profiles.
- **Product Management**:
  - Maintain an inventory of appliances with options to add, update, or delete products.
- **Order Processing**:
  - Create and manage orders, with a basket feature for adding/removing items and completing checkouts.
- **Login System**:
  - Secure login functionality for both customers and admins.
- **Dual Interfaces**:
  - A **web-based interface** for modern accessibility.
  - A **console-based menu system** for quick operations directly in the terminal.

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

