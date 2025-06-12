package Playersmovement;/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    private int x, y, dx, dy;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, 20, 20, 45, 270); // Simple Pac-Man shape
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)  { dx = -5; dy = 0; }
        if (key == KeyEvent.VK_RIGHT) { dx = 5; dy = 0; }
        if (key == KeyEvent.VK_UP)    { dx = 0; dy = -5; }
        if (key == KeyEvent.VK_DOWN)  { dx = 0; dy = 5; }
    }
    public void move(ArrayList<Wall> walls) {
    int nextX = x + dx;
    int nextY = y + dy;
    Rectangle nextBounds = new Rectangle(nextX, nextY, 20, 20);

    for (Wall wall : walls) {
        if (nextBounds.intersects(wall.getBounds())) {
            return; // Collision, don't move
        }
    }

    x = nextX;
    y = nextY;
}

}
