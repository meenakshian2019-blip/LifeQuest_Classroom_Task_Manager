CREATE DATABASE IF NOT EXISTS lifequest;
USE lifequest;

CREATE TABLE IF NOT EXISTS users (
 id INT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL UNIQUE,
 email VARCHAR(100) NOT NULL UNIQUE,
 password VARCHAR(100) NOT NULL,
 role VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
 level INT NOT NULL DEFAULT 1, xp INT NOT NULL DEFAULT 0, coins INT NOT NULL DEFAULT 0,
 health INT NOT NULL DEFAULT 50, focus INT NOT NULL DEFAULT 50, discipline INT NOT NULL DEFAULT 50,
 strength INT NOT NULL DEFAULT 50, dark_mode BOOLEAN NOT NULL DEFAULT FALSE,
 notifications BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Upgrade an older LifeQuest database without deleting existing users.
DROP PROCEDURE IF EXISTS add_role_column_if_missing;
DELIMITER $$
CREATE PROCEDURE add_role_column_if_missing()
BEGIN
 IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='lifequest' AND TABLE_NAME='users' AND COLUMN_NAME='role') THEN
  ALTER TABLE users ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'STUDENT';
 END IF;
END$$
DELIMITER ;
CALL add_role_column_if_missing();
DROP PROCEDURE add_role_column_if_missing;

CREATE TABLE IF NOT EXISTS quests (
 id INT PRIMARY KEY AUTO_INCREMENT, user_id INT NOT NULL, name VARCHAR(120) NOT NULL,
 category VARCHAR(50) NOT NULL, priority VARCHAR(20) NOT NULL, difficulty VARCHAR(20) NOT NULL,
 xp INT NOT NULL, repeat_type VARCHAR(30) NOT NULL DEFAULT 'None', completed BOOLEAN NOT NULL DEFAULT FALSE,
 completed_at TIMESTAMP NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assignments (
 id INT PRIMARY KEY AUTO_INCREMENT,
 title VARCHAR(150) NOT NULL,
 description TEXT,
 difficulty VARCHAR(20) NOT NULL DEFAULT 'Medium',
 points INT NOT NULL DEFAULT 50,
 due_date DATE NULL,
 created_by INT NOT NULL,
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS submissions (
 id INT PRIMARY KEY AUTO_INCREMENT,
 assignment_id INT NOT NULL,
 student_id INT NOT NULL,
 note TEXT,
 status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
 points INT NOT NULL DEFAULT 0,
 submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
 reviewed_at TIMESTAMP NULL,
 UNIQUE KEY unique_assignment_student (assignment_id, student_id),
 FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
 FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Demo teacher account. Change the password after first login if used beyond a local demo.
INSERT INTO users(username,email,password,role)
SELECT 'teacher','teacher@lifequest.local','teacher123','ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username='teacher' OR email='teacher@lifequest.local');
