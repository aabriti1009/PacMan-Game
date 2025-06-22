package pacmangame.Controller;

import pacmangame.dao.UserDAO;
import pacmangame.model.User;

/**
 * Controller for handling login operations
 */
public class LoginController {
    
    private UserDAO userDAO;
    private String lastError;
    private User loggedInUser;
    
    public LoginController() {
        userDAO = new UserDAO();
        lastError = "";
    }
    
    /**
     * Attempts to login a user with the given credentials
     * @param username the username
     * @param password the password
     * @return true if login successful, false otherwise
     */
    public boolean loginUser(String username, String password) {
        try {
            System.out.println("Login attempt for username: '" + username + "'");
            
            // Validate input
            if (username == null || username.trim().isEmpty()) {
                lastError = "Username cannot be empty";
                System.out.println("Login failed: Username is empty");
                return false;
            }
            
            if (password == null || password.trim().isEmpty()) {
                lastError = "Password cannot be empty";
                System.out.println("Login failed: Password is empty");
                return false;
            }
            
            // Attempt to find user in database
            User user = userDAO.findByUsername(username.trim());
            
            if (user == null) {
                lastError = "User not found";
                System.out.println("Login failed: User '" + username + "' not found in database");
                return false;
            }
            
            System.out.println("User found: " + user.getUserName() + ", checking password...");
            
            // Check password
            if (!user.getPassword().equals(password)) {
                lastError = "Invalid password";
                System.out.println("Login failed: Password mismatch for user '" + username + "'");
                return false;
            }
            
            // Login successful
            loggedInUser = user;
            lastError = "";
            System.out.println("Login successful for user: " + username);
            return true;
            
        } catch (Exception e) {
            lastError = "Login failed: " + e.getMessage();
            System.out.println("Login exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Gets the last error message
     * @return the last error message
     */
    public String getLastError() {
        return lastError;
    }
    
    /**
     * Gets the currently logged-in user.
     * @return The logged-in User object, or null if no user is logged in.
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
} 
