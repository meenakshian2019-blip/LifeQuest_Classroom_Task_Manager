# LifeQuest — Classroom Task Manager

Java Swing/AWT + JDBC + MySQL + Maven. Keeps the original dark/purple UI style and Lumi mascot, while adding teacher-created assignments, student submissions, approval-based scoring, and a classroom leaderboard.

## Requirements
- JDK 17+
- Maven
- MySQL Server and MySQL Workbench (or another MySQL client)

## Setup
1. Extract the ZIP.
2. Open `database.sql` in MySQL Workbench and execute the whole script. It creates/updates the `lifequest` schema and classroom tables.
3. Set your MySQL connection values. The app reads these environment variables (recommended):
   - `LIFEQUEST_DB_URL` (default `jdbc:mysql://localhost:3306/lifequest?useSSL=false&serverTimezone=UTC`)
   - `LIFEQUEST_DB_USER` (default `root`)
   - `LIFEQUEST_DB_PASSWORD` (default `m1234` for compatibility with the uploaded project)
   On Windows PowerShell for the current terminal, for example:
   ```powershell
   $env:LIFEQUEST_DB_USER = "root"
   $env:LIFEQUEST_DB_PASSWORD = "YOUR_MYSQL_PASSWORD"
   ```
4. From the extracted project folder, run:
   ```powershell
   mvn clean compile
   mvn exec:java
   ```

## Demo login
- Teacher: username `teacher`, password `teacher123` (created by `database.sql`)
- Student: use **Create an account** on the login screen. All self-registered accounts are assigned the STUDENT role.

## Main workflow
- Teacher: log in → Classroom Manager → publish assignment → Review submissions → approve/reject → view leaderboard.
- Student: register/log in → Assignments → select assignment → submit a short note/work summary → view status and leaderboard.
- Points are awarded only after the teacher approves a submission. The database has a unique assignment/student pair so each student has one submission record per assignment.

## Notes
- This is a local college-project prototype. It uses the original project's basic plaintext password approach and should not be used with real student data or deployed publicly without adding password hashing, stronger access control and secure submission-file handling.
- The current submission flow stores a text summary, not uploaded documents. It can be extended to accept files later.
- Existing personal quest/history modules are retained for student accounts; teacher accounts get the classroom manager navigation.


### Dashboard roles and character stats
- Teacher accounts see classroom counts, pending reviews, approved submissions, and student count on the dashboard.
- Student accounts see their LifeQuest level, XP, coins, and character stats.
- Character stats (Health, Focus, Discipline, Strength) start at 50 by design in the current schema. They are starting values and are not automatically changed by assignment approval. Assignment points are tracked separately on the class leaderboard.
