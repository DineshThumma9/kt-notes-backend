# KT Notes Backend

This is a simple **Note-Taking Backend API** built using **Kotlin**, **Spring Boot**, and **MongoDB**.  
It provides basic **CRUD operations for notes** and **JWT-based authentication**.

---

## ✅ Features

- User Authentication using **JWT (JSON Web Tokens)**
- CRUD operations for **Notes**
    - Create a note
    - Read/Get notes
    - Update a note
    - Delete a note
- Data persisted in **MongoDB**
- Secure endpoints (only authenticated users can access their notes)

---

## ⚙️ Tech Stack

- Kotlin  
- Spring Boot  
- MongoDB  
- Spring Security with JWT  
- Gradle  

---

## 🚀 How It Works

1. Users can **register and login** using email and password.  
2. Upon login, the API returns a **JWT token**.  
3. Authenticated users send the JWT in the header:  
   `Authorization: Bearer <token>`  
   to perform CRUD operations on their personal notes.  
4. Notes are stored in **MongoDB**, each linked to its creator.

---



---

## 🛠️ Installation

1. Install **MongoDB** and start the service.  
2. Clone the project:  
    ```bash
    git clone https://github.com/your-username/kt-notes-backend.git
    ```  
3. Configure environment variables in `application.properties`:  
    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/kt_notes_db
    jwt.secret=YOUR_SECRET_KEY
    jwt.expiration=3600000
    ```  
4. Build and run:  
    ```bash
    ./gradlew bootRun
    ```

---

## ✅ Summary of Changes Done

- Set up Spring Boot project using Kotlin.  
- Configured MongoDB connection.  
- Implemented **User authentication** with **JWT**.  
- Implemented **CRUD API for notes**.  
- Secured endpoints using Spring Security to require valid JWT tokens.  
- Created simple DTOs and Services for clean code architecture.

