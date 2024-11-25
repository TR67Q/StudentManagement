CREATE TABLE IF NOT EXISTS students
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    kana_name VARCHAR(50) NOT NULL,
    nickname VARCHAR(50),
    mail_address VARCHAR(50) NOT NULL,
    area VARCHAR(50),
    age INT,
    gender VARCHAR(10),
    remark TEXT,
    isDeleted boolean
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_name VARCHAR(36) NOT NULL,
    starting_date TIMESTAMP,
    end_date TIMESTAMP
);