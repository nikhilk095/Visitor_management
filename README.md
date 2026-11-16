# Visitor Management System

Java Swing desktop application with a MySQL database.
Developed by **Nikhil Kumar**, Poornima Institute of Engineering & Technology.

## Screens

| Screen | What it does |
|---|---|
| Login | Checks login ID, username and password. Wrong details show "Invalid user". |
| Dashboard | Six tiles that open the other screens. |
| Visitor Entry | Registers a new visitor and starts the visit. The ID (V-1001, V-1002 ...) and check-in time are filled automatically. |
| Visitor Records | Table of all visits with search by name, phone or ID. |
| Check-Out | Search a visitor by ID and record the check-out time. |
| Change Password | Updates the login password. |
| About / Contact | Project and developer information. |

## What you need

1. **JDK 11 or newer** (IntelliJ IDEA already comes with one).
2. **MySQL Server** (and MySQL Workbench if you like) running on your computer.
3. **MySQL Connector/J** jar, from https://dev.mysql.com/downloads/connector/j/
   (choose "Platform Independent", unzip it and keep `mysql-connector-j-x.x.x.jar`).

## Step 1: create the database

Open MySQL Workbench, open `database/schema.sql` and run it (lightning icon).
Or from a terminal:

    mysql -u root -p < database/schema.sql

This creates the database `visitor_db`, the tables `login`, `visitors`, `visits`,
and one login:

    Login ID: VMS101    Username: admin    Password: admin123

## Step 2: set your MySQL password

Open `src/main/resources/db.properties` and change `user` / `password`
to your own MySQL user and password.

## Step 3: run in IntelliJ IDEA

1. **File > Open** and choose this `VisitorManagementSystem` folder.
2. **File > Project Structure > Libraries > + > Java** and select the
   `mysql-connector-j` jar you downloaded. Click OK.
3. Right-click `src/main/java` and choose **Mark Directory as > Sources Root**.
   Right-click `src/main/resources` and choose **Mark Directory as > Resources Root**.
4. Open `vms/Main.java` and click the green Run arrow.

(If IntelliJ detects `pom.xml` as a Maven project and has internet access,
it will download the driver for you, so step 2 is not needed.)

## Optional: run from the command line

    javac -d out -cp mysql-connector-j-8.4.0.jar src/main/java/vms/*.java
    copy src\main\resources\* out\          (Windows)
    java -cp "out;mysql-connector-j-8.4.0.jar" vms.Main      (Windows)
    java -cp "out:mysql-connector-j-8.4.0.jar" vms.Main      (Linux / Mac)

## Project structure

    VisitorManagementSystem/
      database/schema.sql              MySQL tables and default login
      src/main/java/vms/
        Main.java                      starts the application
        DBConnection.java              JDBC connection to visitor_db
        UI.java                        shared colours, fonts, buttons
        BaseFrame.java                 logo header + footer used by every screen
        LoginPage.java                 login screen
        Dashboard.java                 dashboard tiles
        VisitorEntry.java              register visitor
        VisitorRecords.java            records table and search
        VisitorCheckOut.java           check-out
        ChangePassword.java            change password
        AboutContact.java              about page
      src/main/resources/
        logo.png                       Poornima logo
        db.properties                  database settings
      pom.xml                          Maven file (optional)

## Troubleshooting

- **"Cannot connect to the database"**: MySQL is not running, or the user/password
  in `db.properties` is wrong, or the connector jar was not added to the project.
- **"No suitable driver"**: the MySQL Connector/J jar is missing from the classpath.
- **Passwords are stored as plain text** to keep this a simple student project.
  A real system should store hashed passwords.
# Visitor_management
