# 🎬 MovieRate

> A desktop-based movie rating and review application developed using **Java Swing**, **MySQL**, and **Object-Oriented Programming (OOP)**.

![Java](https://img.shields.io/badge/Java-24-orange)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![JDBC](https://img.shields.io/badge/Connectivity-JDBC-green)
![Status](https://img.shields.io/badge/Status-Completed-success)

---

## 📖 Overview

MovieRate is a desktop application that allows users to browse movies, view movie details, give ratings, and write reviews. The application was developed as the Final Semester Project (UAS) for the **Object-Oriented Programming** course.

The project applies Object-Oriented Programming (OOP) principles such as **Encapsulation**, **Inheritance**, **Abstraction**, and **Polymorphism**, while implementing a layered architecture consisting of **View**, **Model**, **DAO**, and **Database Configuration**.

---

## ✨ Features

- 🔐 User Login
- 📝 User Registration
- 🏠 Dashboard / Home
- 🎬 Movie List
- 🔍 Search Movies
- 📄 Movie Detail
- ⭐ Movie Rating
- 💬 Movie Reviews
- 👤 User Profile
- 🗄️ MySQL Database Integration

---

## 🛠️ Built With

- Java SE
- Java Swing
- JDBC
- MySQL
- Visual Studio Code
- Git & GitHub

---

## 📂 Project Structure

```text
MovieRate
│
├── assets/
│   └── poster/
│
├── src/
│   ├── app/
│   ├── config/
│   ├── dao/
│   ├── helper/
│   ├── model/
│   ├── test/
│   └── view/
│
└── README.md
```

---

## 🖥️ Application Preview

> Tambahkan screenshot aplikasi pada folder **screenshots** kemudian ubah link di bawah.

### Login

```
![Login](screenshots/login.png)
```

### Register

```
![Register](screenshots/register.png)
```

### Dashboard

```
![Dashboard](screenshots/home.png)
```

### Movies

```
![Movies](screenshots/movies.png)
```

### Movie Detail

```
![Detail](screenshots/detail.png)
```

### Profile

```
![Profile](screenshots/profile.png)
```

### Reviews

```
![Reviews](screenshots/review.png)
```

---

## 🚀 Getting Started

### Clone Repository

```bash
git clone https://github.com/Ririda-tech/MovieRate.git
```

### Open Project

Open the project using:

- Visual Studio Code

or

- Apache NetBeans

---

### Configure Database

1. Create a MySQL database.

```sql
CREATE DATABASE movierate;
```

2. Import the SQL file.

3. Update the database configuration inside:

```
src/config/
```

Example:

```java
private static final String URL = "jdbc:mysql://localhost:3306/movierate";
private static final String USER = "root";
private static final String PASSWORD = "";
```

---

## 📚 Object-Oriented Programming Concepts

This project implements several OOP concepts:

- ✅ Encapsulation
- ✅ Inheritance
- ✅ Polymorphism
- ✅ Abstraction

---

## 👥 Team Members

| Name | Role |
|------|------|
| **Riri Rida Lestari** | Frontend Developer |
| **Zetta Yemima Arini Uktolseja** | Backend Developer |
| **Chelsea Poibe Zefanya Sagala** | Documentation |
| **Femilia Mutiara Marjono** | Documentation |
| **Hana Khaila** | Documentation |

---

## 📌 Future Improvements

- Add Favorite Movie feature
- Upload movie posters
- Movie recommendation system
- Advanced filtering & sorting
- Responsive UI improvements
- Password encryption
- TMDb API integration
- User activity history

---

## 📄 License

This project was developed for educational purposes as the Final Semester Project (UAS) of the **Object-Oriented Programming** course at **Universitas Pembangunan Nasional Veteran Jakarta**.

---

## 👨‍💻 Developed By

**Kelompok 4**  
D3 Sistem Informasi  
Fakultas Ilmu Komputer  
Universitas Pembangunan Nasional Veteran Jakarta  
2026
