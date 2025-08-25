Linkdrop

Linkdrop is a full-stack web application designed to help you save and manage your bookmarks across multiple devices. The application features a server-side rendered frontend powered by Thymeleaf and a robust backend built with Spring Boot. The goal is to provide a seamless way to access your saved links from anywhere.
Features

    User Authentication: Secure user registration and login to create a personal space for your bookmarks.

    Bookmark Management: Create, retrieve, update, and delete bookmarks.

    Cross-Device Sync: Access your bookmarks on any device simply by logging in to your account.

    Server-Side Rendering: The initial version uses Thymeleaf to render views on the server, providing a fast and efficient user experience.

Technologies
Backend

    Spring Boot: The core framework for building the server-side application.

    Spring Security: Handles all user authentication and authorization.

    Spring Data JPA: Manages the database interactions and object-relational mapping (ORM).

    H2 Database: An in-memory database used for local development and testing.

    Lombok: A utility library to reduce boilerplate code in Java classes.

    Spring Boot DevTools: Provides automatic application restarts for a faster development cycle.

    Thymeleaf: A server-side Java template engine used to generate the HTML views.

Frontend

    HTML, CSS, JavaScript: Standard web technologies for the user interface.

Getting Started
Prerequisites

    Java Development Kit (JDK) 17 or higher

    Maven

Running the Application

    Clone the Repository:

    git clone [your-repository-url]
    cd linkdrop


    Build the Project:

    ./mvnw package


    Run the Application:

    java -jar target/linkdrop-0.0.1-SNAPSHOT.jar


The application will start on http://localhost:8080.
Project Structure

src/
├── main/
│   ├── java/com/linkdrop
│   │   ├── LinkdropApplication.java
│   │   ├── model/
│   │   │   ├── User.java
│   │   │   └── Bookmark.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java
│   │   │   └── BookmarkRepository.java
│   │   ├── controller/
│   │   │   ├── AuthController.java
│   │   │   └── BookmarkController.java
│   │   └── config/
│   │       └── SecurityConfig.java
│   └── resources/
│       ├── templates/
│       │   ├── index.html
│       │   ├── login.html
│       │   └── bookmarks.html
│       └── static/
│           ├── css/
│           └── js/
└── test/
└── java/com/linkdrop
└── LinkdropApplicationTests.java


Future Enhancements

    Transition the frontend to a modern JavaScript framework like React to create a single-page application (SPA) and separate the frontend and backend architectures.

    Implement a Chrome Extension to easily save bookmarks from any webpage.

    Switch to a production-ready database like PostgreSQL or MySQL.

    Add tagging and searching functionality for better bookmark organization.