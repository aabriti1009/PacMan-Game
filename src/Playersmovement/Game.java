package Playersmovement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
import javax.swing.*;

public class Game extends JFrame {
    public Game() {
        setTitle("Pac-Man Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new Board());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
