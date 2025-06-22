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
    private int id;
    private String username;
    private String email;
    private String password;
    private int gamesPlayed;
    private int highScore;
    private long totalScore;
    
    public User(){}
    
    public User(String username,String email,String password){
        this.username=username;
        this.email=email;
        this.password=password;
        this.gamesPlayed = 0;
        this.highScore = 0;
        this.totalScore = 0;
    }
    
    public User(int id, String username, String email, String password, int gamesPlayed, int highScore, long totalScore) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gamesPlayed = gamesPlayed;
        this.highScore = highScore;
        this.totalScore = totalScore;
    }
    
    public int getId() {
        return id;
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
    
    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }
    
    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
    }
    



   