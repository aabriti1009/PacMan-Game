/*













































 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacmangame.Controller;
import pacmangame.model.User;
import pacmangame.dao.UserDAO;

/**
 *
 * @author ACER
 */
public class RegistrationController {
    private UserDAO userDAO;
    private String lastError;
    
    public RegistrationController() {
        userDAO = new UserDAO();
    }
    
    public String getLastError() {
        return lastError;
    }
    
    /**
     * Handles the user registration process
     * @param username Username to register
     * @param email Email address to register
     * @param password Password for the account
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String username, String email, String password) {
        // Reset last error
        lastError = null;
        
        // Basic input validation
        if (username == null || username.trim().isEmpty()) {
            lastError = "Username cannot be empty";
            return false;
        }
        
        if (email == null || email.trim().isEmpty()) {
            lastError = "Email cannot be empty";
            return false;
        }
        
        if (password == null || password.isEmpty()) {
            lastError = "Password cannot be empty";
            return false;
        }
        
        // Validate username
        String usernameError = validateUsername(username);
        if (usernameError != null) {
            lastError = usernameError;
            return false;
        }
        
        // Validate email
        String emailError = validateEmail(email);
        if (emailError != null) {
            lastError = emailError;
            return false;
        }
        
        // Validate password
        String passwordError = validatePassword(password);
        if (passwordError != null) {
            lastError = passwordError;
            return false;
        }
        
        // Create and save user
        User user = new User(username, email, password);
        if (!userDAO.registerUser(user)) {
            lastError = "Failed to create account. Please try again.";
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates username format and availability
     * @param username Username to validate
     * @return null if valid, error message otherwise
     */
    public String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        
        if (username.length() < 3) {
            return "Username must be at least 3 characters long";
        }
        
        if (username.length() > 20) {
            return "Username cannot be longer than 20 characters";
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username can only contain letters, numbers, and underscores";
        }
        
        if (userDAO.isUsernameExists(username)) {
            return "This username is already taken";
        }
        
        return null; // validation passed
    }
    
    /**
     * Validates email format and availability
     * @param email Email to validate
     * @return null if valid, error message otherwise
     */
    public String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty";
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            return "Invalid email format";
        }
        
        if (userDAO.isEmailExists(email)) {
            return "This email is already registered";
        }
        
        return null; // validation passed
    }
    
    /**
     * Validates password strength
     * @param password Password to validate
     * @return null if valid, error message otherwise
     */
    public String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }
        
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }
        
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number";
        }
        
        return null; // validation passed
    }
} 