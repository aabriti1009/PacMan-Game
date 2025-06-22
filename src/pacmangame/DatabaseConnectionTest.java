package pacmangame;

import pacmangame.dao.UserDAO;
import pacmangame.model.User;

/**
 * Test class to verify database connection and login functionality
 */
public class DatabaseConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("=== DATABASE CONNECTION TEST ===");
        
        try {
            // Test database connection
            System.out.println("Testing database connection...");
            UserDAO userDAO = new UserDAO();
            
            // Test if connection is successful
            boolean connectionSuccess = userDAO.testConnection();
            if (connectionSuccess) {
                System.out.println("✅ Database connection successful!");
                
                // Test user registration
                System.out.println("\n--- Testing User Registration ---");
                User testUser = new User("testuser", "test@example.com", "password123");
                boolean registrationSuccess = userDAO.registerUser(testUser);
                
                if (registrationSuccess) {
                    System.out.println("✅ User registration successful!");
                    
                    // Test user login
                    System.out.println("\n--- Testing User Login ---");
                    User foundUser = userDAO.findByUsername("testuser");
                    
                    if (foundUser != null) {
                        System.out.println("✅ User found in database!");
                        System.out.println("Username: " + foundUser.getUserName());
                        System.out.println("Email: " + foundUser.getEmail());
                        
                        // Test password verification
                        if (foundUser.getPassword().equals("password123")) {
                            System.out.println("✅ Password verification successful!");
                            System.out.println("Login functionality is working correctly!");
                        } else {
                            System.out.println("❌ Password verification failed!");
                        }
                    } else {
                        System.out.println("❌ User not found in database!");
                    }
                    
                    // Test duplicate username
                    System.out.println("\n--- Testing Duplicate Username ---");
                    User duplicateUser = new User("testuser", "another@example.com", "password456");
                    boolean duplicateSuccess = userDAO.registerUser(duplicateUser);
                    
                    if (!duplicateSuccess) {
                        System.out.println("✅ Duplicate username detection working!");
                    } else {
                        System.out.println("❌ Duplicate username detection failed!");
                    }
                    
                } else {
                    System.out.println("❌ User registration failed!");
                }
                
            } else {
                System.out.println("❌ Database connection failed!");
                System.out.println("Please check:");
                System.out.println("1. MySQL server is running");
                System.out.println("2. Database 'pacman_game' exists");
                System.out.println("3. Username 'root' and password 'pratikshya123' are correct");
                System.out.println("4. Table 'userspacman' exists");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error during database test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TEST COMPLETED ===");
    }
} 