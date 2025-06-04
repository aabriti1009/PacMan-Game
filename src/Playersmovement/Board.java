/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private Player pacman;
    private ArrayList<Wall> walls;

    public Board() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);
        setFocusable(true);

        pacman = new Player(200, 200);
        walls = new ArrayList<>();

        // Add sample walls
        walls.add(new Wall(100, 100, 200, 20)); // Horizontal wall
        walls.add(new Wall(100, 200, 20, 100)); // Vertical wall

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                pacman.keyPressed(e);
            }
        });

        timer = new Timer(40, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        pacman.move(walls);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        pacman.draw(g);

        for (Wall w : walls) {
            w.draw(g);
        }
    }
}
