-- Visitor Management System - MySQL schema
-- Run this once in MySQL Workbench or the mysql command line:
--   mysql -u root -p < database/schema.sql

CREATE DATABASE IF NOT EXISTS visitor_db;
USE visitor_db;

-- Table 3.1: Login Details
CREATE TABLE IF NOT EXISTS login (
    login_id  VARCHAR(20)  PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL,
    password  VARCHAR(100) NOT NULL
);

-- Table 3.2: Visitor Details (visitor IDs start from 1001 and are shown as V-1001)
CREATE TABLE IF NOT EXISTS visitors (
    visitor_id  INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    phone       VARCHAR(15)  NOT NULL,
    id_proof    VARCHAR(50),
    department  VARCHAR(80)
) AUTO_INCREMENT = 1001;

-- Table 3.3: Visit Details
CREATE TABLE IF NOT EXISTS visits (
    visit_id        INT AUTO_INCREMENT PRIMARY KEY,
    visitor_id      INT          NOT NULL,
    purpose         VARCHAR(150),
    person_to_meet  VARCHAR(100),
    check_in        DATETIME     NOT NULL,
    check_out       DATETIME     NULL,
    status          VARCHAR(20)  NOT NULL DEFAULT 'Inside',
    FOREIGN KEY (visitor_id) REFERENCES visitors (visitor_id)
);

-- Default login used in the project:
--   Login ID: VMS101   Username: admin   Password: admin123
INSERT IGNORE INTO login (login_id, username, password) VALUES ('VMS101', 'admin', 'admin123');
