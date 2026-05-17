# Online Job Portal Management System

A full-stack Job Portal Management System developed using Spring Boot, Thymeleaf, Spring Security, and H2 Database. The platform enables students/job seekers to search and apply for jobs while employers can post and manage job openings.

## Live Demo

Deployed on Render:

[https://jobportal-6ct3.onrender.com](https://jobportal-6ct3.onrender.com)

---

# Features

## Student Features

* User Registration & Login
* Secure Authentication using Spring Security
* Browse Available Jobs
* Apply for Jobs
* Upload Resume
* Track Applications

## Employer Features

* Employer Registration & Login
* Post New Jobs
* Manage Job Listings
* View Applicants

## Admin Features

* Manage Users
* Manage Jobs
* Monitor Applications

## Additional Features

* Email Notifications using Java Mail
* File Upload Support
* H2 Database Integration
* Responsive UI with Thymeleaf
* Role-Based Authentication & Authorization

---

# Tech Stack

| Technology          | Description                    |
| ------------------- | ------------------------------ |
| Java 17             | Programming Language           |
| Spring Boot         | Backend Framework              |
| Spring Security     | Authentication & Authorization |
| Spring Data JPA     | Database Operations            |
| Thymeleaf           | Frontend Template Engine       |
| H2 Database         | Embedded Database              |
| Maven               | Dependency Management          |
| HTML/CSS/JavaScript | Frontend Technologies          |
| Docker              | Containerization               |
| Render              | Cloud Deployment               |

---

# Project Structure

```
src
 └── main
      ├── java
      │     └── com.jobportal
      ├── resources
      │     ├── static
      │     ├── templates
      │     └── application.properties
pom.xml
Dockerfile
render.yaml
```

---

# Installation & Setup

## Clone Repository

```bash
git clone https://github.com/Jeevanasree24/Online-Job-Portal-Management-System.git
```

## Navigate to Project

```bash
cd Online-Job-Portal-Management-System
```

## Run Application

Using Eclipse:

* Right Click Project
* Run As → Spring Boot App

Or using Maven:

```bash
mvn spring-boot:run
```

---

# Database Configuration

The project uses H2 file-based database.

```properties
spring.datasource.url=jdbc:h2:file:./data/jobportaldb
```

H2 Console:

```
http://localhost:8080/h2-console
```

---

# Deployment

The application is containerized using Docker and deployed on Render.

## Docker Deployment

```dockerfile
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/jobportal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

---

# Environment Variables

Configure these variables in Render:

| Variable      | Description        |
| ------------- | ------------------ |
| MAIL_USERNAME | Gmail Address      |
| MAIL_PASSWORD | Gmail App Password |

---

# Screenshots

Add screenshots of:

* Home Page
* Login Page
* Job Listings
* Employer Dashboard
* Admin Dashboard

---

# Future Enhancements

* PostgreSQL/MySQL Integration
* Resume Parsing
* AI-Based Job Recommendation
* Email Verification
* OTP Authentication
* REST API Integration
* Cloud File Storage

---

# Author

Jeevanasree Perisetla

* GitHub: [https://github.com/Jeevanasree24](https://github.com/Jeevanasree24)
* HackerRank: [https://www.hackerrank.com/profile/vtu25933](https://www.hackerrank.com/profile/vtu25933)
* GeeksforGeeks: [https://www.geeksforgeeks.org/user/jeevanasreeperisetla/](https://www.geeksforgeeks.org/user/jeevanasreeperisetla/)

---

# License

This project is developed for educational and learning purposes.
