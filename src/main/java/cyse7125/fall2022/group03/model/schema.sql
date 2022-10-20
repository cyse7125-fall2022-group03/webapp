mysql -u root -p
pwd: password123

/*CREATE DATABASE IF NOT EXISTS todo;*/

USE todo;

SHOW DATABASES;

/*
CREATE TABLE IF NOT EXISTS user (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255),
	middle_name VARCHAR(255),
	last_name VARCHAR(255),
    password VARCHAR(255),
    email_address VARCHAR(255) UNIQUE
);

SHOW TABLES;

INSERT INTO user (first_name, middle_name, last_name, password, email_address) VALUES ('Vignesh', '', 'Gunasekaran', 'password123', 'vig@gmail.com');
INSERT INTO user (first_name, middle_name, last_name, password, email_address) VALUES ('Ram', 'Gopal', 'Varma', 'password123', 'ram@gmail.com');
*/
SELECT * FROM user;

