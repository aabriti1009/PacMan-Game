/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman.game.dao;
import pacman.game.model.User;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ACER
 */
public class UserDAO{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pacman_game";
    private static final String USER ="root";
    private static final String PASS = "pratikshya123";
    private static final String TABLE_NAME = "userspacman";
    
    public UserDAO(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Test the connection when DAO is initialized
            testConnection();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found.\nError: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // New method to test database connection
    public boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // If we get here, connection was successful
            System.out.println("Database connection successful!");
            
            // Test if we can access the users table
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + TABLE_NAME);
                rs.next(); // Move to first row
                int userCount = rs.getInt(1);
                System.out.println("Number of users in database: " + userCount);
            }
            
            return true;
        } catch (SQLException e) {
            String errorMessage = "Database connection failed!\n";
            errorMessage += "Error: " + e.getMessage() + "\n\n";
            errorMessage += "Please verify:\n";
            errorMessage += "1. MySQL is running\n";
            errorMessage += "2. Database 'pacman_game' exists\n";
            errorMessage += "3. Username and password are correct\n";
            errorMessage += "4. Port 3306 is correct";
            
            JOptionPane.showMessageDialog(null, 
                errorMessage, 
                "Database Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(User user){
        // First check if email already exists
        if (isEmailExists(user.getEmail())) {
            JOptionPane.showMessageDialog(null, 
                "This email address is already registered.\nPlease use a different email or try to login.", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Then check if username already exists
        if (isUsernameExists(user.getUserName())) {
            JOptionPane.showMessageDialog(null, 
                "This username is already taken.\nPlease choose a different username.", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        String sql = "INSERT INTO " + TABLE_NAME + " (username,email,password) VALUES (?,?,?)";
        try(Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Failed to create user account.\nPlease try again.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
                
        } catch (SQLException e) {
            String errorMessage = "Registration failed.\n";
            if (e.getErrorCode() == 1062) { // MySQL duplicate entry error code
                errorMessage += "This email or username is already registered.";
            } else {
                errorMessage += "Database error: " + e.getMessage();
            }
            
            JOptionPane.showMessageDialog(null, 
                errorMessage,
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            
            e.printStackTrace();
            return false;
        }
    }
    public boolean isUsernameExists(String username){
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()){
                return rs.getInt(1)>0;
                
                
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isEmailExists(String email){
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE email = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);
                PreparedStatement pstmt =conn.prepareStatement(sql)){
            
            pstmt.setString(1,email);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()){
                return rs.getInt(1)>0;
                
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
        
        
    

    
}

   



   