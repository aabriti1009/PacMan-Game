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
    
    public RegistrationController() {
        userDAO = new UserDAO();
    }
    
    public boolean registerUser(String username, String email, String password) {
        if (!validateUsername(username) || !validateEmail(email) || !validatePassword(password)) {
            return false;
        }
        
        User user = new User(username, email, password);
        return userDAO.registerUser(user);
    }
    
    public boolean validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        if (username.length() < 3 || username.length() > 20) {
            return false;
        }
        
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return false;
        }
        
        return !userDAO.isUsernameExists(username);
    }
    
    public boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            return false;
        }
        
        return !userDAO.isEmailExists(email);
    }
    
    public boolean validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }
        
        // Password must contain at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Password must contain at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        // Password must contain at least one number
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        
        return true;
    }
    
    public int calculatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() > 8) strength += 20;
        if (password.matches(".*[A-Z].*")) strength += 20;
        if (password.matches(".*[a-z].*")) strength += 20;
        if (password.matches(".*[0-9].*")) strength += 20;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength += 20;
        return strength;
    }

  
}
    


