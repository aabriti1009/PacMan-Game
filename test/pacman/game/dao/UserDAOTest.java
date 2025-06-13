/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package pacman.game.dao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pacman.game.model.User;

/**
 *
 * @author ACER
 */
public class UserDAOTest {
    String correctEmail="madhu12@gmail.com";
    String correctName="Madhu user12";
    String password="madhuK08029";
    UserDAO dao=new UserDAO();
    @Test 
    public void registrationWithDetails(){
        User user=new User(correctName,correctEmail,password);
        boolean result=dao.registerUser(user);
        Assert.assertTrue("Register should work with unique details",result);
        
    }
    @Test
    public void registrationWithDuplicateDetails(){
        User user=new User(correctName,correctEmail,password);
        boolean result =dao.registerUser(user);
        Assert.assertFalse("Register should fails with duplicate credentials",result);
    }
    
   
}