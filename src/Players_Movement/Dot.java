/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author admin
 */
import java.awt.*;

// Dot represents a small point Pac-Man can eat
public class Dot {
    private int x, y;
    private boolean eaten;

    // Constructor to initialize dot position
    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
        this.eaten = false;
    }

    // Draw the dot only if it's not eaten
    public void draw(Graphics g) {
        if (!eaten) {
            g.setColor(Color.WHITE);
            g.fillOval(x + 15, y + 15, 10, 10); // Centered in a 40x40 grid
        }
    }

    // Returns true if the dot has been eaten
    public boolean isEaten() {
        return eaten;
    }

    // Set the dot to eaten or not
    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    // Return the bounding box for collision detection
    public Rectangle getBounds() {
        return new Rectangle(x + 15, y + 15, 10, 10); // Match size in draw()
    }
}
