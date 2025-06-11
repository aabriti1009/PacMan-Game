/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacmangame.Controller;
import pacman.game.model.User;
import pacman.game.dao.UserDAO;

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
    
    public boolean registerUser(String username, String email, String password) {
        // Reset last error
        lastError = null;
        
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
        
        User user = new User(username, email, password);
        boolean success = userDAO.registerUser(user);
        
        if (!success) {
            lastError = "Registration failed. Please try again.";
        }
        
        return success;
    }
    
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
        
        return null; // null means validation passed
    }
    
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
        
        return null; // null means validation passed
    }
    
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
        
        return null; // null means validation passed
    }
    
    
}
    


