DROP SCHEMA IF EXISTS sis_db;
CREATE SCHEMA sis_db;
USE sis_db;

-- 1. INSTRUCTOR: Surrogate Key Only (Starting at 100)
CREATE TABLE Instructor (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    office_number VARCHAR(10)
);
-- Set initial AUTO_INCREMENT value
ALTER TABLE Instructor AUTO_INCREMENT = 100;

-- 2. STUDENT: Surrogate Key Only (Starting at 500)
CREATE TABLE Student (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    enrollment_year INT
);
-- Set initial AUTO_INCREMENT value
ALTER TABLE Student AUTO_INCREMENT = 500;

-- 3. COURSE: Surrogate (PK) + Natural Unique Key (course_code)
CREATE TABLE Course (
    course_pk INT AUTO_INCREMENT PRIMARY KEY,
    course_code CHAR(10) UNIQUE NOT NULL, -- Natural Key (e.g., CS111)
    title VARCHAR(100) NOT NULL,
    credits INT
);

-- 4. SECTION: Surrogate Key + Foreign Keys
CREATE TABLE Section (
    section_id INT AUTO_INCREMENT PRIMARY KEY,
    course_pk INT NOT NULL,
    instructor_id INT NOT NULL,
    semester CHAR(6) NOT NULL, -- 'Fall' or 'Spring'
    year INT NOT NULL,
    section_number INT NOT NULL,
    
    UNIQUE(course_pk, instructor_id, semester, year, section_number),
    FOREIGN KEY (course_pk) REFERENCES Course(course_pk),
    FOREIGN KEY (instructor_id) REFERENCES Instructor(instructor_id)
);

-- 5. TAKES: Junction Table with Composite Primary Key
CREATE TABLE Takes (
    student_id INT NOT NULL,
    section_id INT NOT NULL,
    grade VARCHAR(2), -- 'A', 'B+', or NULL
    
    PRIMARY KEY (student_id, section_id),
    FOREIGN KEY (student_id) REFERENCES Student(student_id),
    FOREIGN KEY (section_id) REFERENCES Section(section_id)
);


-- =================================================================
-- DML: DATA MANIPULATION LANGUAGE
-- Populates the tables according to the specified constraints.
-- =================================================================

-- 1. INSTRUCTOR INSERTIONS (5 Instructors, IDs 100-104 with office numbers)
INSERT INTO Instructor (first_name, last_name, office_number) VALUES
('Alex', 'Doe', 'SB201'),        -- 100
('Brenda', 'Smith', 'SB202'),    -- 101
('Charles', 'Lee', 'SB301'),     -- 102
('Diana', 'Jones', 'QH205'),     -- 103
('Ethan', 'Wang', 'PM207');      -- 104


-- 2. STUDENT INSERTIONS (5 Students, IDs 500-504)
INSERT INTO Student (first_name, last_name, enrollment_year) VALUES
('Olivia', 'Martinez', 2022), -- 500 (3 sections)
('Noah', 'Hernandez', 2023),  -- 501 (4 sections)
('Emma', 'Wilson', 2022),     -- 502 (2 sections)
('Liam', 'Garcia', 2023),     -- 503 (1 section)
('Ava', 'Brown', 2023);       -- 504 (3 sections)


-- 3. COURSE INSERTIONS (8 Courses)
INSERT INTO Course (course_code, title, credits) VALUES
('CS111', 'Introduction to Programming', 4), -- PK 1
('CS313', 'Data Structures', 3),             -- PK 2
('CS331', 'Database Systems', 4),            -- PK 3
('MATH101', 'Calculus 1', 4),                -- PK 4
('MATH102', 'Calculus 2', 4),                -- PK 5
('PHY200', 'Physics 1', 3),                  -- PK 6
('PHY400', 'Quantum Mechanics', 4),          -- PK 7
('CHEM101', 'Chemistry 1', 3);               -- PK 8


-- 4. SECTION INSERTIONS (12 Sections total)
INSERT INTO Section (course_pk, instructor_id, semester, year, section_number) VALUES
-- Instructor 100 (2 courses: CS111, CS313)
(1, 100, 'Fall', 2024, 1),  -- Section 1
(2, 100, 'Spring', 2025, 1),-- Section 2

-- Instructor 101 (1 course: CS331)
(3, 101, 'Fall', 2025, 1),  -- Section 3

-- Instructor 102 (3 courses: MATH101, MATH102, PHY200)
(4, 102, 'Fall', 2023, 1),  -- Section 4
(5, 102, 'Spring', 2024, 1),-- Section 5
(6, 102, 'Fall', 2024, 1),  -- Section 6

-- Instructor 103 (4 courses: PHY400, CHEM101, CS111, CS331)
(7, 103, 'Spring', 2025, 1),-- Section 7
(8, 103, 'Fall', 2023, 1),  -- Section 8
(1, 103, 'Spring', 2025, 2),-- Section 9
(3, 103, 'Fall', 2024, 2),  -- Section 10

-- Instructor 104 (2 courses: MATH101, MATH102)
(4, 104, 'Fall', 2025, 2),  -- Section 11
(5, 104, 'Spring', 2023, 2);-- Section 12


-- 5. TAKES INSERTIONS (13 Enrollments total)
INSERT INTO Takes (student_id, section_id, grade) VALUES
-- Student 500 (3 sections)
(500, 1, 'A'),        
(500, 4, 'B+'),       
(500, 10, NULL),       -- Target for UPDATE

-- Student 501 (4 sections)
(501, 2, 'A-'),       
(501, 5, 'B'),        
(501, 7, 'A'),        
(501, 10, NULL),      -- Target for UPDATE (e.g., student 501 in CS331-2024-Fall)

-- Student 502 (2 sections)
(502, 3, 'C'),        
(502, 6, 'A'),        

-- Student 503 (1 section)
(503, 8, 'B'),        

-- Student 504 (3 sections)
(504, 11, 'C+'),      
(504, 12, 'B-'),      
(504, 1, 'A');        -- Section 1 (CS111, I100) now has two students (500, 504)
