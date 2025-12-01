# Database Setup Guide

## Step 1: Install MySQL (if not already installed)
- Download from: https://dev.mysql.com/downloads/mysql/
- Install MySQL Server and MySQL Workbench

## Step 2: Start MySQL Service
**Windows:**
- Open Services (services.msc)
- Find "MySQL80" or similar
- Right-click → Start
- OR: Run `net start MySQL80` in Command Prompt (as Administrator)

**Verify MySQL is running:**
```bash
mysql -u root -p
```

## Step 3: Create Database and Table

Open MySQL Command Line or Workbench and run:

```sql
-- Create database
CREATE DATABASE IF NOT EXISTS project3;

-- Use the database
USE project3;

-- Create game table
CREATE TABLE IF NOT EXISTS game (
  user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user VARCHAR(50) NOT NULL UNIQUE,
  scores INT DEFAULT 0,
  number_of_games_played INT DEFAULT 0
);

-- Insert test data (optional)
INSERT INTO game (user, scores, number_of_games_played) VALUES 
('yusra', 0, 0),
('mama', 0, 0),
('sadia', 0, 0);
```

## Step 4: Update Game.java Connection String

Open `Game.java` and find the `Connectivity` class:

```java
private static final String URL = "jdbc:mysql://localhost:3306/project3";
private static final String USER = "root";
private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";  // Change this!
```

Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL password (default is often blank or "root")

## Step 5: Verify MySQL JDBC Driver

Make sure you have the MySQL connector jar file. If you get "Driver not found" error:
1. Download: https://dev.mysql.com/downloads/connector/j/
2. Extract the JAR file
3. Add it to your Java classpath or IDE project

## Common Issues

### "Communications link failure"
- MySQL is not running
- Wrong hostname (should be localhost)
- Wrong port (should be 3306)

### "Access denied for user 'root'"
- Wrong password
- Update PASSWORD in Connectivity class

### "Unknown database 'project3'"
- Database not created
- Run the SQL commands above

### "Driver not found"
- MySQL JDBC driver not installed
- Download connector/J from MySQL website

## Test Connection

When you run the Game, it will automatically test the database connection and show an error if it fails.

Good luck! 🎮
