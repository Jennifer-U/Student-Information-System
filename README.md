# Student Information System

A Student Information System (SIS) implemented in Java as a two-tier database application, consisting of a client application and a database server. Built using Java, JDBC, and MySQL.


## Project Information

- **Course**: CSCI 711-222 Database Systems
- **Semester**: Fall 2025

## Technologies Used

- **Language**: Java
- **Database**: MySQL Server
- **Database Tool**: MySQL Workbench
- **Build Tool**: Gradle (Kotlin DSL)
- **IDE**: VS Code
- **Database Connectivity**: JDBC (MySQL Connector/J 8.4.0)

## Prerequisites

- JDK 11 or higher
- MySQL Server 8.0 or higher
- MySQL Workbench
- Gradle (wrapper included)

## Installation

### 1. Clone the repository
```bash
git clone [repository-url]
cd student-information-system
```

### 2. Set up MySQL Database

#### Using MySQL Workbench:

1. Open MySQL Workbench and connect to your MySQL Server
2. Open the `create_sis_db.sql` file from the project root
3. Execute the script to create the database schema
   - This will create the `sis_db` database
   - All required tables, relationships, and constraints will be set up

#### Alternative (Command Line):
```bash
mysql -u root -p < create_sis_db.sql
```

### 3. Configure Database Connection

Update the database credentials in `SIS.java`:
```java
// Update these values with your MySQL credentials
String url = "jdbc:mysql://localhost:3306/sis_db";
String username = "your_username";
String password = "your_password";
```

### 4. Build the Project
```bash
./gradlew build
```

## Running the Application

```bash
./gradlew run
```

## Project Structure

```
student-information-system/
├── .gradle/                    # Gradle cache
├── .vscode/                    # VS Code configuration
├── app/
│   ├── build/                  # Compiled classes
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── org/
│   │       │       └── CS711_project1/
│   │       │           └── SIS.java
│   │       └── resources/      # Configuration files
│   └── build.gradle.kts        # App-level build config
├── build/                      # Build output
├── gradle/                     # Gradle wrapper
├── .gitattributes
├── .gitignore
├── create_sis_db.sql           # Database schema
├── gradle.properties           # Gradle properties
├── gradlew                     # Gradle wrapper (Unix)
├── gradlew.bat                 # Gradle wrapper (Windows)
├── README.md
└── settings.gradle.kts         # Project settings
```

## Dependencies

The project uses the following dependencies (defined in `app/build.gradle.kts`):

```kotlin
dependencies {
    // This dependency is used by the application.
    implementation(libs.guava)
    // MySQL JDBC driver
    implementation("com.mysql:mysql-connector-j:8.4.0")
}
```

## Database Schema

The database schema (`sis_db`) is defined in `create_sis_db.sql` located in the project root. The schema includes tables for:
- Students
- Courses
- Enrollments
- Grades
- [Other tables as defined in the schema]

## Main Application

The main application class is located at:
```
app/src/main/java/org/CS711_project1/SIS.java
```

This class contains:
- Database connection configuration
- JDBC connection setup
- All CRUD operations for the student information system

## Building and Running

### Build the project
```bash
./gradlew build
```

### Run the application
```bash
./gradlew run
```

### Clean build artifacts
```bash
./gradlew clean
```

## Database Connection

The application connects to MySQL Server using JDBC:
- **Database Name**: `sis_db`
- **Connection URL**: `jdbc:mysql://localhost:3306/sis_db`
- **Driver**: MySQL Connector/J 8.4.0
- **Credentials**: Configured directly in `SIS.java`

## Development Workflow

1. Database schema is provided in `create_sis_db.sql`
2. Execute the schema script in MySQL Workbench or via command line
3. Configure MySQL credentials in `SIS.java`
4. Application connects to the database via JDBC
5. All CRUD operations are performed through the Java application

## Features

- Student enrollment management
- Course registration
- Grade tracking
- Student records CRUD operations
- Database connectivity via JDBC

## Troubleshooting

### Connection Issues
- Ensure MySQL Server is running on `localhost:3306`
- Verify credentials in `SIS.java` match your MySQL setup
- Check that `sis_db` schema exists in MySQL
- Confirm the MySQL JDBC connector dependency is downloaded

### Schema Issues
- Re-run the `create_sis_db.sql` script in MySQL Workbench if tables are missing
- Verify all tables were created successfully

### Build Issues
- Run `./gradlew clean build` to rebuild the project
- Ensure JDK is properly installed and configured