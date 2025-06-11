/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman.game.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import pacman.game.Database.DbConnection;

/**
 *
 * @author ACER
 */
public class MySqlConnection implements DbConnection {

    @Override
    public Connection openConnection() {
      
      try{
          String username="root";
          String password="pratikshya123";
          String database="pacman_game";
          Class.forName("com.mysql.jdbc.Driver");
          Connection conn;
          conn =DriverManager.getConnection("jdbc:mysql://localhost:3306/"+
                  database,username,password);
          return conn;
          
  
          
      }catch(Exception e){
          return null;
          
      }
    }

    @Override
        public void closeConnection(Connection conn) {
            try{
            if(conn!=null && !conn.isClosed ()){
                conn.close();
            }
        
            
    
            } catch(Exception e){
        
        }
    
    }
            
    
}
