/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacmangame.model;


/**
 *
 * @author ACER
 */
public class User {
    private String username;
    private String email;
    private String password;
    
    public User(){}
    
    public User(String username,String email,String password){
        this.username=username;
        this.email=email;
        this.password=password;
        
        
    }
    public String getUserName(){
        return username;
        
    }
    public void setuserName(String username){
        this.username=username;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password =password;
    }
    
  
        
    }
    



   