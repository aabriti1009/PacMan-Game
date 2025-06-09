/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman.game.dao;
import pacman.game.model.User;
import java.sql.*;


/**
 *
 * @author ACER
 */
public class UserDAO{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pacman_game";
    private static final String USER ="root";
    private static final String PASS = "";
    
    public UserDAO(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public boolean registerUser(User user){
        String sql = "INSERT INTO users (username,email,password) VALUES (?,?,?)";
        try(Connection conn =DriverManager.getConnection(DB_URL,USER,PASS);
                PreparedStatement pstmt = conn.prepareStatement(sql)){
                
        }catch (SQLException e){
                e.printStackTrace();
               
        }
        return false;
    }
    public boolean isUsernameExists(String username){
        String sql ="SELECT COUNT(*)FROM users WHERE username = ?";
        try (Connection conn =DriverManager.getConnection(DB_URL,USER,PASS);
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
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
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

   



   