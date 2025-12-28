package org.CS711_project1; 
import java.util.Scanner;
import java.sql.*;


/**
 * SIS.java
 * The main application class for the Student Information System (SIS).
 * Implements a tiered menu structure and all JDBC operations for the five-table schema.
 * * NOTE: The bodies of all CRUD and Report methods are intentionally empty 
 * and left for the student to implement the required JDBC code.
 */
public class SIS {
    
    // --- JDBC Configuration ---
    // NOTE: Replace these with your actual MySQL credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sis_db";
    private static final String USER = "Enter your Username";  
    private static final String PASS = "Enter your password";

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        System.out.println("Welcome to the Student Information System (SIS) CLI.");
        try {
            // Ensure the JDBC Driver is loaded once
            Class.forName("com.mysql.cj.jdbc.Driver");
            runMenu(scanner);
        } catch (ClassNotFoundException e) {
            System.err.println("FATAL ERROR: JDBC Driver not found. Make sure the MySQL Connector JAR is included in your classpath.");
        }  
    }

    // =================================================================
    // CORE UTILITIES & HELPERS (LEFT IMPLEMENTED)
    // =================================================================
    
    /**
     * Helper function to safely read an integer from the console, handling bad input.
     */
    private static int getValidIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                // Check if the next token is an integer
                if (scanner.hasNextInt()) {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    return choice;
                } else {
                    System.out.println("Invalid input. Please enter a whole number.");
                    scanner.nextLine(); // Clear the bad input
                    System.out.print(prompt);
                }
            } catch (Exception e) {
                // Catch any other unexpected scanner issues
                System.out.println("An unexpected error occurred during input.");
                scanner.nextLine();
                System.out.print(prompt);
            }
        }
    }

    /**
     * Retrieves the surrogate key (course_pk) given the natural key (course_code).
     * Returns -1 if the course code is not found.
     * This is useful for inserting into the Section table.
     */
    private static int getCoursePk(String courseCode) {
        String sql = "SELECT course_pk FROM Course WHERE course_code = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, courseCode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("course_pk");
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.err.println("DATABASE ERROR during course lookup: " + e.getMessage());
            return -1;
        }
    }
    
    // =================================================================
    // LEVEL 1: MAIN MENU
    // =================================================================
    
    private static void runMenu(Scanner scanner) throws SQLException {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|  STUDENT INFORMATION SYSTEM (SIS) - MENU  |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Instructors");
            System.out.println("3. Manage Courses");
            System.out.println("4. Manage Sections");
            System.out.println("5. Manage Enrollment & Grading (Takes)");
            System.out.println("6. Generate Reports (Complex Queries)");
            System.out.println("0. EXIT");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: studentMenu(); break;
                case 2: instructorMenu(); break;
                case 3: courseMenu(); break;
                case 4: sectionMenu(); break;
                case 5: enrollmentMenu(); break;
                case 6: reportsMenu(); break;
                case 0:
                    System.out.println("Thank you for using the SIS. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (true);
    }

    // =================================================================
    // LEVEL 2 MENUS (Menu structure is kept intact)
    // =================================================================

    private static void studentMenu() {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|          STUDENT OPERATIONS MENU         |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Insert new Student");
            System.out.println("2. Get Student details by ID");
            System.out.println("3. Get a list of ALL Students");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: insertStudent(); break;
                case 2: getStudentDetails(); break;
                case 3: listAllStudents(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    private static void instructorMenu() {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|        INSTRUCTOR OPERATIONS MENU        |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Insert new Instructor");
            System.out.println("2. Get Instructor details by ID");
            System.out.println("3. Get a list of ALL Instructors");
            System.out.println("4. Update Instructor Office Number");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: insertInstructor(); break;
                case 2: getInstructorDetails(); break;
                case 3: listAllInstructors(); break;
                case 4: updateInstructorOffice(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    private static void courseMenu() throws SQLException {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|          COURSE OPERATIONS MENU          |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Insert new Course");
            System.out.println("2. Get Course details by Code (Natural Key)");
            System.out.println("3. Get a list of ALL Courses");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: insertCourse(); break;
                case 2: getCourseDetails(); break;
                case 3: listAllCourses(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    private static void sectionMenu() throws SQLException {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|          SECTION OPERATIONS MENU         |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Insert new Section");
            System.out.println("2. Get a list of ALL Sections");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: insertSection(); break;
                case 2: listAllSections(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    private static void enrollmentMenu() throws SQLException {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|       ENROLLMENT & GRADING MENU          |");
            System.out.println("--------------------------------------------");
            System.out.println("1. Enroll Student in Section (Insert TAKES)");
            System.out.println("2. Drop Student from Section (Delete TAKES)");
            System.out.println("3. Update Grade (Update TAKES)");
            System.out.println("4. List ALL Enrollments and Grades");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: enrollStudent(); break;
                case 2: dropStudent(); break;
                case 3: updateGrade(); break;
                case 4: listAllEnrollmentsAndGrades(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    private static void reportsMenu() {
        int choice;
        do {
            System.out.println("\n--------------------------------------------");
            System.out.println("|             REPORTS MENU (JOINS)         |");
            System.out.println("--------------------------------------------");
            System.out.println("1. List Sections by Instructor (General)");
            System.out.println("2. List Sections by Instructor (Specific Year/Semester)");
            System.out.println("3. List Sections by Year and Semester");
            System.out.println("4. List Courses Taken by Student (General)");
            System.out.println("5. List Courses Taken by Student (Specific Year/Semester)");
            System.out.println("0. Back to Main Menu");
            System.out.println("--------------------------------------------");

            choice = getValidIntInput("Enter your choice: ");

            switch (choice) {
                case 1: listSectionsByInstructorGeneral(); break;
                case 2: listSectionsByInstructorSpecific(); break;
                case 3: listSectionsByYearSemester(); break;
                case 4: reportStudentCoursesGeneral(); break;
                case 5: reportStudentCoursesSpecific(); break;
                case 0: return;
                default: System.out.println("Invalid option. Try again.");
            }
        } while (true);
    }

    // =================================================================
    // STUDENT OPERATIONS (1. Insert, 2. Get Details, 3. List All)
    // =================================================================

    private static void insertStudent() {
        System.out.println("\n--- Insert New Student ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        int year = getValidIntInput("Enter Enrollment Year (e.g., 2024): ");

        // === STUDENT IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: INSERT INTO Student (...) VALUES (?, ?, ?)
        // 3. Execute update and retrieve generated keys (student_id)
        // 4. Print success message with the new ID or an error message.
        
        String sqlInsert = "INSERT INTO Student (first_name, last_name, enrollment_year) VALUES (?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setInt(3, year);
            int rowsInserted = stmt.executeUpdate();
            
            if (rowsInserted > 0){
                ResultSet results = stmt.getGeneratedKeys();
                if (results.next()){
                    int newID = results.getInt(1);
                    System.out.println("\nStudent inserted successfully! New ID: " + newID);
                }
            }else{
                System.out.println("\nInsert Failed. No rows affected");
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("\nError inserting student");
        }   
    }

    private static void getStudentDetails() {
        int studentId = getValidIntInput("\n--- Get Student Details ---\nEnter Student ID: ");
        
        // === STUDENT IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: SELECT * FROM Student WHERE student_id = ?
        // 3. Execute query
        // 4. Print results or a "not found" message.
        String query = "SELECT * FROM Student WHERE student_id = ?";
        ResultSet results = null; 

        // header
        System.out.println();
        System.out.printf("%-11s %-12s %-17s %-12s%n",
        "Student ID", "First Name", "Last Name", "Enrollment Year" );
        System.out.println("-----------------------------------------------------------");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, studentId);
            results = stmt.executeQuery();
                if(results.next()){
                        String value1 = results.getString("first_name");
                        String value2 = results.getString("last_name");
                        int    value3 = results.getInt("enrollment_year");    
                        System.out.printf("%-11s %-12s %-17s %-12s%n", studentId, value1, value2, value3);
                        System.out.print("\n-----------------------Student Found-----------------------");
                }else{
                    System.out.println("\nStudent with ID " + studentId + " not found.");
                }          
        }catch(SQLException e){
            System.out.println("\nerror occured");
            e.printStackTrace();
        }finally{
                //ResultSet is closed manually.
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException closeEx) {
                // Log or handle the close exception if necessary
                closeEx.printStackTrace();
            }
        }
    }

    private static void listAllStudents() {
        System.out.println("\n--- List All Students ---");

        // === STUDENT IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Execute SQL statement: SELECT * FROM Student
        // 3. Loop through result set and print details in a formatted table.
        
        System.out.println();
        System.out.printf("%-12s %-12s %-17s %-11s%n",
        "First Name", "Last Name", "Enrollment Year", "Student ID");
        System.out.println("------------------------------------------------------");
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM Student");
            while(results.next()){
                String value1 = results.getString("first_name");
                String value2 = results.getString("last_name");
                int    value3 = results.getInt("enrollment_year");
                int    value4 = results.getInt("student_id");
                System.out.printf("%-12s %-12s %-17s %-11s%n", value1, value2, value3, value4);
            }
        }catch(SQLException e){
            System.out.println("\nNo students");
        }
    }
    
    // =================================================================
    // INSTRUCTOR OPERATIONS (1. Insert, 2. Get Details, 3. List All, 4. Update)
    // =================================================================
    
    private static void insertInstructor() {
        System.out.println("\n--- Insert New Instructor ---");
        System.out.print("\nEnter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("\nEnter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("\nEnter Office Number: ");
        String office = scanner.nextLine();

        // === INSTRUCTOR IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: INSERT INTO Instructor (...) VALUES , ?,(? ?)
        // 3. Execute update and retrieve generated keys (instructor_id)
        // 4. Print success message.
        String sqlInsert = "INSERT INTO Instructor (first_name, last_name, office_number) VALUES (?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, office);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0){
                ResultSet results = stmt.getGeneratedKeys();
                if (results.next()){
                    int newID = results.getInt(1);
                    System.out.println("\nInstructor inserted successfully! New ID: " + newID);
                }
            }else{
                System.out.println("\nInsert Failed. No rows affected");
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("\nError inserting instructor");
        }   
    }

    private static void getInstructorDetails() {
        int instructorId = getValidIntInput("\n--- Get Instructor Details ---\nEnter Instructor ID: ");
        
        // === INSTRUCTOR IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: SELECT * FROM Instructor WHERE instructor_id = ?
        // 3. Execute query
        // 4. Print results or a "not found" message.
        String query = "SELECT * FROM Instructor WHERE instructor_id = ?";
        ResultSet results = null; 

        // header
        System.out.println();
        System.out.printf("%-14s %-12s %-17s %-14s%n",
        "Instructor ID", "First Name", "Last Name", "Office Number" );
        System.out.println("-----------------------------------------------------------");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, instructorId);
            results = stmt.executeQuery();
                if(results.next()){
                        int    value1 = results.getInt("instructor_id");
                        String value2 = results.getString("first_name");
                        String value3 = results.getString("last_name");
                        String value4 = results.getString("office_number");
                        System.out.printf("%-14s %-12s %-17s %-14s%n", value1, value2, value3, value4);
                        System.out.println("\n-----------------------------Instructor Found------------------------------");
                }else{
                    System.out.println("\nInstructor with ID " + instructorId + " not found.");
                }          
        }catch(SQLException e){
            System.out.println("\nError Occured");
            e.printStackTrace();
        }finally{
                //ResultSet is closed manually.
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException closeEx) {
                // Log or handle the close exception if necessary
                closeEx.printStackTrace();
            }
        }
    }

    private static void listAllInstructors() {
        System.out.println("\n--- List All Instructors ---");
        
        // === INSTRUCTOR IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Execute SQL statement: SELECT * FROM Instructor
        // 3. Loop through result set and print details.

        // header
        System.out.println();
        System.out.printf("%-14s %-12s %-17s %-14s%n",
        "Instructor ID", "First Name", "Last Name", "Office Number" );
        System.out.println("-----------------------------------------------------------");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM Instructor");
            while(results.next()){
                int    value1 = results.getInt("instructor_id");
                String value2 = results.getString("first_name");
                String value3 = results.getString("last_name");
                String value4 = results.getString("office_number");

                System.out.printf("%-14s %-12s %-17s %-14s%n", value1, value2, value3, value4);
            }
        }catch(SQLException e){
            System.out.println("error");
            e.printStackTrace();
        }
    }

    private static void updateInstructorOffice() {
        System.out.println("\n--- Update Instructor Office ---");
        int instructorId = getValidIntInput("Enter Instructor ID to update: ");
        System.out.print("Enter New Office Number: ");
        String newOffice = scanner.nextLine();

        // === INSTRUCTOR IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: UPDATE Instructor SET office_number = ? WHERE instructor_id = ?
        // 3. Execute update
        // 4. Check affected rows and print success/warning.
        String query = "UPDATE Instructor SET office_number = ? WHERE instructor_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, instructorId);
            stmt.setString(1, newOffice );
            stmt.setInt(2, instructorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("\n----Successful update----");
                System.out.println("\nInstructor Id: " + instructorId + " " + "New Office #: " + newOffice);
            }else{
                System.out.println("\nWarning--did not update. Id not found.");
            }
        }catch(SQLException e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    
    // =================================================================
    // COURSE OPERATIONS (1. Insert, 2. Get Details, 3. List All)
    // =================================================================

    private static void insertCourse() throws SQLException {
        System.out.println("\n--- Insert New Course ---");
        System.out.print("\nEnter Course Code (e.g., CS499): ");
        String code = scanner.nextLine();
        System.out.print("\nEnter Course Title: ");
        String title = scanner.nextLine();
        int credits = getValidIntInput("\nEnter Credits: ");

        // === COURSE IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: INSERT INTO Course (...) VALUES (?, ?, ?)
        // 3. Execute update and retrieve generated keys (course_pk)
        // 4. Handle potential UNIQUE constraint violation on course_code.
        String sqlInsert = "INSERT INTO Course (course_code, title, credits) VALUES (?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
                stmt.setString(1, code);
                stmt.setString(2, title);
                stmt.setInt(3, credits);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0){
                ResultSet results = stmt.getGeneratedKeys();
                if (results.next()){
                    int course_pk = results.getInt(1);
                    System.out.println("\nCourse inserted successfully! New course primary key: " + course_pk);
                }
            }else{
                System.out.println("\nInsert Failed. No rows affected");
            }
        }catch(SQLException e){ //unique constraint violation on course_code
            if(e.getSQLState().equals("2300") && e.getErrorCode() ==1062 ){
                System.out.println("\nDuplicate detected");
            }else{ //handles other SQL exceptions
                throw e; 
            }  
        }   
    }

    private static void getCourseDetails() {
        System.out.println("\n--- Get Course Details ---");
        System.out.print("\nEnter Course Code (e.g., CS331): ");
        String code = scanner.nextLine();
        
        // === COURSE IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: SELECT * FROM Course WHERE course_code = ?
        // 3. Execute query
        // 4. Print results or a "not found" message.
        String query = "SELECT * FROM Course WHERE course_code = ?";
        ResultSet results = null; 
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, code);
            results = stmt.executeQuery();
                if(results.next()){
                        String value1 = results.getString("course_code");
                        String value2 = results.getString("title");
                        int    value3 = results.getInt("credits");
                        System.out.println("\n--------Course Details--------");
                        // header
                        System.out.println();
                        System.out.printf("%-12s %-29s %-7s%n",
                "Course Code", "Title", "Credits");
                        System.out.println("-----------------------------------------------------------");
                        System.out.printf("%-12s %-29s %-7s%n", value1,value2,value3);
                }else{
                    System.out.println("\nCourse with code " + code + " not found.");
                }          
        }catch(SQLException e){
            System.out.println("\nError occured");
            e.printStackTrace();
        }finally{
                //ResultSet is closed manually.
            try {
                if (results != null) {
                    results.close();
                }
            } catch (SQLException closeEx) {
                // Log or handle the close exception if necessary
                closeEx.printStackTrace();
            }
        }
    }

    private static void listAllCourses() {
        System.out.println("\n--- List All Courses ---");

        // === COURSE IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Execute SQL statement: SELECT * FROM Course
        // 3. Loop through result set and print details.

        // header
        System.out.println();
        System.out.printf("%-12s %-29s %-7s%n",
        "Course Code", "Title", "Credits");
        System.out.println("-----------------------------------------------------------");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM Course");
            while(results.next()){
                String value1 = results.getString("course_code");
                String value2 = results.getString("title");
                String value3 = results.getString("credits");
                System.out.printf("%-12s %-29s %-7s%n", value1,value2,value3);
            }
        }catch(SQLException e){
            System.out.println("error");
            e.printStackTrace();
        }
    }

    // =================================================================
    // SECTION OPERATIONS (1. Insert, 2. List All)
    // =================================================================

    private static void insertSection() throws SQLException {
        System.out.println("\n--- Insert New Section ---");
        System.out.print("Enter Course Code (e.g., CS313): ");
        String courseCode = scanner.nextLine();
        
        // This helper is kept as it is useful for the Section insert logic
        int coursePk = getCoursePk(courseCode); 
        if (coursePk == -1) {
            System.out.println("FAILURE: Course code '" + courseCode + "' not found. Cannot insert section.");
            return;
        }
        
        int instructorId = getValidIntInput("Enter Instructor ID: ");
        System.out.print("Enter Semester ('Fall' or 'Spring'): ");
        String semester = scanner.nextLine();
        int year = getValidIntInput("Enter Year (e.g., 2025): ");
        int sectionNumber = getValidIntInput("Enter Section Number (e.g., 1): ");

        // === SECTION IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Prepare SQL statement: INSERT INTO Section (...) VALUES (?, ?, ?, ?, ?)
        // 3. Use the retrieved coursePk (first parameter)
        // 4. Handle Foreign Key (instructor_id) or UNIQUE Key constraint violations.
        String sqlInsert = "INSERT INTO Section (course_pk, instructor_id, semester, year, section_number) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
                stmt.setInt(1, coursePk);
                stmt.setInt(2, instructorId);
                stmt.setString(3, semester);
                stmt.setInt(4, year);
                stmt.setInt(5,sectionNumber);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0){
                ResultSet results = stmt.getGeneratedKeys();
                if (results.next()){
                    System.out.println("Section inserted successfully!");
                }
            }else{
                System.out.println("Insert Failed. No rows affected");
            }
        }catch(SQLException e){ 
            //UNIQUE constraint violation check
            if(e.getSQLState().equals("2300") && e.getErrorCode() ==1062 ){
                System.out.println("Duplicate detected");
            // FOREIGN KEY Constraint check (Invalid Instructor ID)
            }else if (e.getSQLState().equals("2300") && e.getErrorCode() == 1452) { 
                System.out.println("FAILURE: Instructor ID " + instructorId + " does not exist. Please check the Instructor table.");
            }else{ //handles other SQL exceptions
                throw e; 
            }  
        }   
    }

    private static void listAllSections() {
        System.out.println("\n--- List All Sections ---");

        // === SECTION IMPLEMENTATION REQUIRED HERE ===
        // 1. Establish connection
        // 2. Execute SQL statement with JOIN: SELECT S.*, C.course_code, C.title FROM Section S JOIN Course C ON S.course_pk = C.course_pk
        // 3. Loop through result set and print details.
         try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT S.*, C.course_code, C.title FROM Section S JOIN Course C ON S.course_pk = C.course_pk");

            //Header
            System.out.printf("%-15s %-29s %-21s %-15s %-12s %-6s %-16s%n",
                "Course Code: ", "Title: ", "Course Primary Key: ","Instructor ID: ","Semester: ", "Year: ", "Section Number: ");
                System.out.println("-----------------------------------------------------------------------------------------------------------------------");
            
            while(results.next()){
                String value1 = results.getString("course_code");
                String value2 = results.getString("title");
                String value3 = results.getString("course_pk");
                int    value4 = results.getInt("instructor_id");
                String value5 = results.getString("semester");
                int    value6 = results.getInt("year");
                int    value7 = results.getInt("section_number");
                
                System.out.printf("%-15s %-29s %-21s %-15s %-12s %-6s %-16s%n",
                value1, value2,value3,value4,value5, value6,value7);
            }
        }catch(SQLException e){
            System.out.println("error");
            e.printStackTrace();
        }
    }

    // =================================================================
    // ENROLLMENT (TAKES) OPERATIONS (1. Enroll, 2. Drop, 3. Update Grade, 4. List All)
    // =================================================================

    private static void enrollStudent() throws SQLException {
        System.out.println("\n--- Enroll Student in Section ---");
        int studentId = getValidIntInput("Enter Student ID: ");
        int sectionId = getValidIntInput("Enter Section ID: ");

        // === ENROLLMENT IMPLEMENTATION REQUIRED HERE (INSERT) ===
        // 1. Establish connection
        // 2. Prepare SQL statement: INSERT INTO Takes (student_id, section_id) VALUES (?, ?)
        // 3. Handle Foreign Key (ID not found) or Composite PK (already enrolled) violations.

        String sqlInsert = "INSERT INTO Takes (student_id, section_id) VALUES (?, ?)";
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(sqlInsert,Statement.RETURN_GENERATED_KEYS)){
                stmt.setInt(1,studentId);
                stmt.setInt(2, sectionId);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0){
                ResultSet results = stmt.getGeneratedKeys();
                if (results.next()){
                    System.out.println("Student enrolled in section successfully!");
                }
            }else{
                System.out.println("Student enrollment in section failed. No rows affected");
            }
        }catch(SQLException e){
            // --- Integrity Constraint Violation Handling (SQL State 23xxx) ---
        
            // 1. UNIQUE/COMPOSITE PK VIOLATION (Student already enrolled)
            // Checks for the general integrity state (23000/2300) AND MySQL's specific duplicate key code (1062).
            if(e.getSQLState() != null && e.getSQLState().startsWith("23") && e.getErrorCode() == 1062) {
                System.out.println("FAILURE: Student " + studentId + " is already enrolled in Section " + sectionId + ".");
                
            // 2. FOREIGN KEY VIOLATION (Invalid Student ID or Section ID)
            // Checks for the general integrity state AND MySQL's specific FK constraint failure code (1452).
            } else if (e.getSQLState() != null && e.getSQLState().startsWith("23") && e.getErrorCode() == 1452) { 
                
                // Check the exception message to see which ID caused the failure (Student or Section)
                String message = e.getMessage().toLowerCase();
                
                if (message.contains("student")) {
                    System.out.println("FAILURE: Student ID " + studentId + " does not exist. Please verify the ID.");
                } else if (message.contains("section")) {
                    System.out.println("FAILURE: Section ID " + sectionId + " does not exist. Please verify the ID.");
                } else {
                    // Fallback for an unidentifiable FK error
                    System.out.println("FAILURE: A required ID was not found in the database (Foreign Key Error).");
                }

            // 3. OTHER SQL EXCEPTIONS (Catch all others)
            } else {
                // Handles other severe exceptions (Connection issue, syntax error, etc.)
                System.out.println("CRITICAL ERROR: A non-integrity related SQL exception occurred.");
                throw e; // Re-throw to crash/handle upstream
            }
        } 
    }

    private static void dropStudent() {
        System.out.println("\n--- Drop Student from Section ---");
        int studentId = getValidIntInput("Enter Student ID to drop: ");
        int sectionId = getValidIntInput("Enter Section ID to drop from: ");

        // === ENROLLMENT IMPLEMENTATION REQUIRED HERE (DELETE) ===
        // 1. Establish connection
        // 2. Prepare SQL statement: DELETE FROM Takes WHERE student_id = ? AND section_id = ?
        // 3. Check affected rows and print success/warning.
        String sqlDelete = "DELETE FROM Takes WHERE student_id = ? AND section_id = ?";
        
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(sqlDelete);
            stmt.setInt(1, studentId);
            stmt.setInt(2, sectionId);
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){//row with above parameters found
                System.out.println("Student with student id " +studentId+ " successfully deleted");
            }else{
                System.out.println("WARNING! No rows changed! Enrollment of student with student id " +studentId+ "not found");
            }
        }catch(SQLException e){
            System.out.println("Error occured");
            e.printStackTrace();
        }
    }

    private static void updateGrade() {
        System.out.println("\n--- Update Grade ---");
        int studentId = getValidIntInput("Enter Student ID: ");
        int sectionId = getValidIntInput("Enter Section ID: ");
        System.out.print("Enter New Grade (e.g., A, B+, NULL): ");
        String newGrade = scanner.nextLine();
        
        // Logic to handle user input for NULL grades
        if (newGrade.trim().equalsIgnoreCase("NULL")) {
            newGrade = null;
        }

        // === ENROLLMENT IMPLEMENTATION REQUIRED HERE (UPDATE) ===
        // 1. Establish connection
        // 2. Prepare SQL statement: UPDATE Takes SET grade = ? WHERE student_id = ? AND section_id = ?
        // 3. Use setString(1, newGrade) to correctly handle NULL
        // 4. Check affected rows and print success/warning.

        String sqlUpdateGrade =  "UPDATE Takes SET grade = ? WHERE student_id = ? AND section_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(sqlUpdateGrade);
            stmt.setString(1, newGrade);
            stmt.setInt(2, studentId);
            stmt.setInt(3, sectionId);
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){//row with above parameters found
                System.out.println("Student with student id " +studentId+ " had grade successfully updated");
            }else{
                System.out.println("WARNING! No rows changed! Grade not updated. Student with student id " + studentId + "not found");
            }
        }catch(SQLException e){
            System.out.println("Error occured");
            e.printStackTrace();
        }
    }
    
    /**
     * Report: Lists all enrollment records (Takes), joined with Course and Section 
     * to provide full context (course code, title, term).
     */
    private static void listAllEnrollmentsAndGrades() {
        System.out.println("\n--- List ALL Enrollments and Grades (For Verification) ---");
        
        // === ENROLLMENT IMPLEMENTATION REQUIRED HERE (REPORT) ===
        // 1. Establish connection
        // 2. Execute SQL statement (with JOINS on Takes, Section, and Course)
        // 3. Loop through result set and print formatted data.
       
        //Header
        System.out.printf("%-11s %-11s %-6s %-12s %-29s %-9s %-6s%n",
         "student_id", "section_id", "grade","course_code","title", "semester", "year");
        System.out.println("----------------------------------------------------------------------------------------");
            

        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            Statement stmt = connection.createStatement();
            ResultSet results = stmt.executeQuery("SELECT student_id, section_id, grade, course_code, title, semester, year FROM Takes JOIN Section USING(section_id) JOIN Course USING(course_pk) ORDER BY student_id"
        );
            while(results.next()){
                String value1 = results.getString("student_id");
                String value2 = results.getString("section_id");
                String value3 = results.getString("grade");
                String value4 = results.getString("course_code");
                String value5 = results.getString("title");
                String value6 = results.getString("semester");
                int    value7 = results.getInt("year");
                
                System.out.printf("%-11s %-11s %-6s %-12s %-29s %-9s %-6s%n",
                value1, value2,value3,value4,value5, value6,value7);
            }

        }catch(SQLException se){
            System.out.println("Error");
            se.printStackTrace();
            
        }
    }

    // =================================================================
    // REPORT OPERATIONS (COMPLEX JOINS)
    // =================================================================

    private static void listSectionsByInstructorGeneral() {
        int instructorId = getValidIntInput("\n--- Report: Sections by Instructor (General) ---\nEnter Instructor ID: ");
        
        // === REPORT IMPLEMENTATION REQUIRED HERE ===
        // Goal: List all sections taught by the given Instructor ID, showing course details.
        // Tables: Instructor, Section, Course

        String query ="SELECT I.instructor_id, C.course_code, C.title, S.course_pk, S.semester, S.year, S.section_number\n" + //
                        "FROM Instructor I\n" + //
                        "JOIN Section S ON I.instructor_id = S.instructor_id\n" + //
                        "JOIN Course C ON S.course_pk = C.course_pk\n" + //
                        "WHERE I.instructor_id = ?\n" + //
                        "ORDER BY S.year DESC, S.semester DESC, C.course_code";
        // Header
        System.out.println();
        System.out.printf("%-15s %-29s %-11s %-6s %-11s %-11s%n",
        "Course Code", "Title", "Semester", "Year", "Section #", "Course PK");
        System.out.println("---------------------------------------------------------------------------------------");
            
        int rowCount = 0; // Counter for results
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            try (ResultSet results = stmt.executeQuery()) { 
                // while loop to process ALL rows
                while(results.next()){
                    rowCount++;
                    String courseCode     = results.getString("course_code");
                    String title          = results.getString("title");
                    String semester       = results.getString("semester");
                    int    year           = results.getInt("year");
                    int    sectionNumber  = results.getInt("section_number");
                    String coursePk       = results.getString("course_pk");
                    //print format
                    System.out.printf("%-15s %-29s %-11s %-6s %-11s %-11s%n",
                        courseCode, title, semester, year, sectionNumber, coursePk);
                }
            } 
            if (rowCount == 0){
                System.out.println("\nNo sections found for Instructor ID: " + instructorId);
            }
        } catch(SQLException e){
            System.out.println("\nError occurred during report generation.");
            e.printStackTrace();
        }
    }

    private static void listSectionsByInstructorSpecific() {
        System.out.println("\n--- Report: Sections by Instructor (Specific Term) ---");
        int instructorId = getValidIntInput("\nEnter Instructor ID: ");
        int year = getValidIntInput("\nEnter Year (e.g., 2024): ");
        System.out.print("\nEnter Semester ('Fall' or 'Spring'): ");
        String semester = scanner.nextLine();
        
        // === REPORT IMPLEMENTATION REQUIRED HERE ===
        // Goal: List sections taught by Instructor ID in the specific year/semester.
        // Tables: Instructor, Section, Course
        
        String query ="SELECT I.instructor_id, S.year, S.semester, C.course_code, C.title, S.course_pk, S.section_number\n" + //
                        "FROM Instructor I\n" + //
                        "JOIN Section S ON I.instructor_id = S.instructor_id\n" + //
                        "JOIN Course C ON S.course_pk = C.course_pk\n" + //
                        "WHERE I.instructor_id = ? AND S.year = ? AND S.semester = ?\n" + //
                        "ORDER BY C.course_code";

        //Header
        System.out.println();
        System.out.printf("%-15s %-7s %-12s %-15s %-29s %-21s %-17s%n",
        "Instructor ID: ","Year: ","Semester: ","Course Code: ", "Title: ", "Course Primary Key: ", "Section Number: ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        
        int rowCount = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, instructorId);
            stmt.setInt(2, year);
            stmt.setString(3, semester);
            try(ResultSet results = stmt.executeQuery()){
                while(results.next()){
                        int    value1 = results.getInt("instructor_id");
                        int    value2 = results.getInt("year");
                        String value3 = results.getString("semester");
                        String value4 = results.getString("course_code");
                        String value5 = results.getString("title");
                        String value6 = results.getString("course_pk");
                        int    value7 = results.getInt("section_number");
                        System.out.printf("%-15s %-7s %-12s %-15s %-29s %-21s %-17s%n",
                        value1, value2,value3,value4,value5, value6,value7);
                }
            }
            if(rowCount == 0){
                System.out.println("\nNo section found for Instructor ID " + instructorId+ " in " + semester + " " + year + ".");
            }     
        }catch(SQLException e){
            System.out.println("\nError occured");
            e.printStackTrace();
        }   
    }
    
    private static void listSectionsByYearSemester() {
        System.out.println("\n--- Report: Sections by Term ---");
        int year = getValidIntInput("\nEnter Year (e.g., 2024): ");
        System.out.print("\nEnter Semester ('Fall' or 'Spring'): ");
        String semester = scanner.nextLine();
        
        // === REPORT IMPLEMENTATION REQUIRED HERE ===
        // Goal: List all sections offered in the specific year/semester, showing instructor ID.
        // Tables: Section, Course

        String query = "SELECT S.year, S.semester, S.course_pk, C.course_code, C.title, S.section_number, S.instructor_id\n" + //
                        "FROM Section S JOIN Course C ON S.course_pk = C.course_pk\n" + //
                        "WHERE S.year = ? AND S.semester = ?\n" + //
                        "ORDER BY C.course_code";
        
        //Header
        System.out.println();
        System.out.printf("%-7s %-12s %-15s %-15s%-17s %-29s %-15s%n",
         "Year: ","Semester: ","Course Code: ", "Section Number: ", "Instructor ID: ", "Title: ", "Course Primary Key: ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        
        int rowCount = 0; 
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, year);
            stmt.setString(2, semester);
            try(ResultSet results = stmt.executeQuery()){
                while(results.next()){
                    rowCount ++;
                        int    value1 = results.getInt("year");
                        String value2 = results.getString("semester");
                        String value3 = results.getString("course_code");
                        int    value4 = results.getInt("section_number");
                        int    value5 = results.getInt("instructor_id");
                        String value6 = results.getString("title");
                        String value7 = results.getString("course_pk");
                        
                        System.out.printf("%-7s %-12s %-15s %-15s%-17s %-29s %-15s%n",
                        value1, value2,value3,value4,value5, value6,value7);
                }
            } 
            if(rowCount ==0){
                System.out.println("\nNo Sections found for  " + semester + " " + year + ".");
            }     
        }catch(SQLException e){
            System.out.println("\nError occured");
            e.printStackTrace();
        }
    }

    private static void reportStudentCoursesGeneral() {
        int studentId = getValidIntInput("\n--- Report: Courses Taken by Student (General) ---\nEnter Student ID: ");
        
        // === REPORT IMPLEMENTATION REQUIRED HERE ===
        // Goal: List ALL courses taken by the student (all terms), including grade.
        // Tables: Student, Takes, Section, Course
        String query ="SELECT T.student_id, C.course_code, C.title, C.credits, S.semester, S.year, T.grade, S.section_id, S.course_pk\n" + //
                        "FROM Student St\n" + //
                        "JOIN Takes T ON St.student_id = T.student_id\n" + //
                        "JOIN Section S ON T.section_id = S.section_id\n" + //
                        "JOIN Course C ON S.course_pk = C.course_pk\n" + //
                        "WHERE St.student_id = ?\n" + //
                        "ORDER BY S.year DESC, S.semester DESC, C.course_code";
        //Header
        System.out.println();
        System.out.printf("%-12s %-15s %-30s %-8s %-10s %-7s %-10s %-8s%n",
        "Student ID", "Course Code", "Title", "Credits", "Semester", "Year", "Grade", "Section #");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        
        int rowCount = 0; 
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, studentId);
            try(ResultSet results = stmt.executeQuery()){
                while(results.next()){
                    rowCount++;
                    int    value1 = results.getInt("student_id");
                    String value2 = results.getString("course_code");
                    String value3 = results.getString("title");
                    int    value4 = results.getInt("credits");
                    String value5 = results.getString("semester");
                    int    value6 = results.getInt("year");
                    String value7 = results.getString("grade"); 
                    int    value8 = results.getInt("section_id");
                    System.out.printf("%-12s %-15s %-30s %-8s %-10s %-7s %-10s %-8s%n",
                    value1, value2,value3,value4,value5, value6,value7, value8);
                }
            }    
            if(rowCount == 0){
                System.out.println("\nNo course with Student ID " + studentId + " found.");
            }     
        }catch(SQLException e){
            System.out.println("\nError occured");
            e.printStackTrace();
        }
    }

    private static void reportStudentCoursesSpecific() {
        System.out.println("\n--- Report: Courses Taken by Student (Specific Term) ---");
        int studentId = getValidIntInput("\nEnter Student ID: ");
        int year = getValidIntInput("\nEnter Year (e.g., 2024): ");
        System.out.print("\nEnter Semester ('Fall' or 'Spring'): ");
        String semester = scanner.nextLine();
        
        // === REPORT IMPLEMENTATION REQUIRED HERE ===
        // Goal: List courses taken by student in the specific year/semester, including grade.
        // Tables: Student, Takes, Section, Course

        String query = "SELECT T.student_id, S.semester, S.year, C.course_code, C.title, C.credits, T.grade, S.section_id, S.course_pk\n" + //
                        "FROM Student St\n" + //
                        "JOIN Takes T ON St.student_id = T.student_id\n" + //
                        "JOIN Section S ON T.section_id = S.section_id\n" + //
                        "JOIN Course C ON S.course_pk = C.course_pk\n" + //
                        "WHERE St.student_id = ? AND S.year = ? AND S.semester = ?\n" + //
                        "ORDER BY C.course_code";
        //Header
        System.out.println();
        System.out.printf("%-12s %-10s %-7s %-15s %-30s %-8s %-10s %-8s%n",
        "Student ID", "Semester", "Year", "Course Code", "Title", "Credits", "Grade", "Section #");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        
        int rowCount = 0;
        try(Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)){
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, studentId);
            stmt.setInt(2, year);
            stmt.setString(3, semester);

            try(ResultSet results = stmt.executeQuery() ){
                while(results.next()){
                    rowCount++;
                    int    value1 = results.getInt("student_id");
                    String value2 = results.getString("semester");
                    int    value3 = results.getInt("year");
                    String value4 = results.getString("course_code");
                    String value5 = results.getString("title");
                    int    value6 = results.getInt("credits");
                    String value7 = results.getString("grade");
                    int    value8 = results.getInt("section_id"); 

                    System.out.printf("%-12s %-10s %-7s %-15s %-30s %-8s %-10s %-8s%n",
                    value1, value2, value3, value4, value5, value6, value7, value8);
                }
            }
            if(rowCount == 0){
                System.out.println("\nNo courses found for Student ID " + studentId + " in " + semester + " " + year + ".");
            }
        }catch(SQLException se){
            System.out.println("\nError found");
            se.printStackTrace();
        }
    }
    
}
