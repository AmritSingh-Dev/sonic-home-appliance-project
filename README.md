# Sonic Home Appliance Store/Management System

## 📝 Description

The **Sonic Home Appliance Store** is a full-stack Java application that simulates an online home appliance store with administrative management capabilities. It supports customer accounts, product management, shopping baskets, and order processing through a web-based interface.

This project was developed to showcase industry-standard Java development practices, including layered architecture, design patterns, database abstraction, and automated testing.

---

## 🎯 Motivation

This project aims to serve as a  example in learning about scalable application development, while also highlighting how technical designs can enhance both the user experience and back-end efficiency. This project isn’t just about building a system; it’s about showcasing how Java can be used to solve real-world problems by implementing robust design patterns and combining them with advanced features like a web interface, authentication, and dynamic shopping basket functionality.

This project was built to:
- Demonstrate real-world Java backend development
- Apply software engineering best practices
- Serve as a portfolio piece for academic and graduate-level applications

---

## ⭐ Key Features

#### Customer Functionality
- User registration and authentication
- Secure password storage using BCrypt
- Browse home appliances
- Search, filter, and sort products
- Shopping basket management
- Order placement and history

#### Admin Functionality
- Manage home appliances (create, update, delete)
- View and manage customers
- View and manage orders
- Role-based access control

---

## 🛠️ Technology Stack

- **Language:** Java
- **Web:** Embedded HTTP server (localhost:8080)
- **Frontend:** HTML, CSS, Bootstrap
- **Database:** SQLite
- **Security:** BCrypt password hashing
- **Testing:** JUnit, Mockito

---

## 🏛️ Architecture

The project follows a clean, modular architecture inspired by MVC principles.

#### Design Patterns Used
- **DAO (Data Access Object)** – Database abstraction
- **Singleton** – Session and authentication management
- **Separation of Concerns** – Clear boundaries between logic, data, and presentation

---

## 🛢️ Database

- Uses **SQLite** for lightweight persistence
- All database interactions are handled through DAO classes
- Schema is automatically initialized on startup (if required)

---

## ✅ Testing

- Unit tests written with **JUnit**
- **Mockito** used for mocking database dependencies
- Focus on business logic and data access reliability

---

## 🚀 Getting Started

#### Prerequisites
- Java JDK 17 or later
- Eclipse (or any Java IDE)
- SQLite (optional — database file is included)

#### Running the Application
1. Import the project into your Java IDE
2. Ensure the correct JDK is configured
3. Run the main application class
4. Open a browser and navigate to: http://localhost:8080

---

## 📸 Previews


---



