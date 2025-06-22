# Database Setup Guide for PacMan Game

## Prerequisites

1. **MySQL Server** - Make sure MySQL is installed and running on your system
2. **MySQL JDBC Driver** - You need to add the MySQL connector to your project

## Step 1: Set up MySQL Database

1. Open MySQL Workbench or MySQL command line client
2. Run the `database_setup.sql` script to create the database and tables:

```sql
-- This will create:
-- Database: pacman_game
-- Table: users (for user registration and login)
-- Table: game_scores (for storing game scores)
-- Table: game_settings (for user preferences)
```

## Step 2: Add MySQL JDBC Driver

1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/
2. Extract the JAR file (e.g., `mysql-connector-j-8.0.33.jar`)
3. Copy the JAR file to your project's `lib` folder
4. In NetBeans, right-click on your project → Properties → Libraries
5. Click "Add JAR/Folder" and select the MySQL connector JAR file

## Step 3: Verify Database Connection

1. Make sure your MySQL server is running
2. Verify the connection details in `UserDAO.java`:
   - Database URL: `jdbc:mysql://localhost:3306/pacman_game`
   - Username: `root`
   - Password: `pratikshya123`
   - Table: `users`

3. Run the `DatabaseConnectionTest.java` class to test the connection

## Step 4: Test Login Functionality

1. Run the `NavigationTest.java` class
2. Try registering a new user
3. Try logging in with the registered credentials
4. Verify that the login works correctly

## Troubleshooting

### Common Issues:

1. **"MySQL JDBC Driver not found"**
   - Solution: Add the MySQL connector JAR to your project libraries

2. **"Database connection failed"**
   - Check if MySQL server is running
   - Verify database name, username, and password
   - Make sure the `pacman_game` database exists

3. **"Table doesn't exist"**
   - Run the `database_setup.sql` script to create the tables

4. **"Access denied"**
   - Check MySQL user permissions
   - Verify username and password in UserDAO.java

### Database Connection Details:

- **Host**: localhost
- **Port**: 3306
- **Database**: pacman_game
- **Username**: root
- **Password**: pratikshya123
- **Table**: users

## Testing the Login System

After setup, you can test the login system by:

1. Running `DatabaseConnectionTest.java` - This will test the database connection and basic functionality
2. Running `NavigationTest.java` - This will test the full UI flow including login and registration
3. Manually testing through the LoginView and RegistrationView

The login system will:
- Validate username and password
- Show appropriate error messages for invalid credentials
- Navigate to MainMenu on successful login
- Allow navigation between LoginView and RegistrationView 