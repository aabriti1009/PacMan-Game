/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacmangame.dao;
import pacmangame.model.User;
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
    private static final String TABLE_NAME = "users";
    
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

    public boolean registerUser(User user) {
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
        
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            int rowsAffected = stmt.executeUpdate();
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
    
    /**
     * Finds a user by username
     * @param username the username to search for
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        System.out.println("Searching for user: " + username);
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("User found: " + username);
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("games_played"),
                    rs.getInt("high_score"),
                    rs.getLong("total_score")
                );
            } else {
                System.out.println("User not found: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Database error while searching for user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserStats(User user) {
        String query = "UPDATE users SET games_played = ?, high_score = ?, total_score = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user.getGamesPlayed());
            stmt.setInt(2, user.getHighScore());
            stmt.setLong(3, user.getTotalScore());
            stmt.setString(4, user.getUserName());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("games_played"),
                    rs.getInt("high_score"),
                    rs.getLong("total_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

   



   