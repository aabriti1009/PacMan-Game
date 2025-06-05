/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pacman.game.Database;

import java.sql.Connection;


/**
 *
 * @author ACER
 */
 public interface DbConnection {
     Connection openConnection();
    void closeConnection(Connection conn);
     
    
}
