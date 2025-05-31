import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    private int x, y;           // Current position
    private int size = 30;      // Size of Pac-Man (width & height)
    private int speed = 5;      // Movement speed per update

    private int dx = 0;         // Movement in x-direction
    private int dy = 0;         // Movement in y-direction

    // Mouth animation variables
    private int mouthAngle = 45;        // Mouth opening size in degrees
    private boolean mouthOpening = true; // Toggle for mouth open/close animation

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // Draw Pac-Man with animated mouth (wedge missing)
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);

        // Animate mouth opening and closing
        if (mouthOpening) {
            mouthAngle -= 5;
            if (mouthAngle <= 0) mouthOpening = false;
        } else {
            mouthAngle += 5;
            if (mouthAngle >= 45) mouthOpening = true;
        }

        // Draw Pac-Man as a yellow arc with mouth opening
        g.fillArc(x, y, size, size, mouthAngle, 360 - 2 * mouthAngle);
    }

    // Move the player by dx, dy but prevent moving through walls
    public void move(ArrayList<Wall> walls) {
        // Calculate tentative new position
        int newX = x + dx;
        int newY = y + dy;

        // Create a rectangle for the new position to check collisions
        Rectangle newBounds = new Rectangle(newX, newY, size, size);

        // Check collision with all walls
        for (Wall wall : walls) {
            if (newBounds.intersects(wall.getBounds())) {
                // Collision detected, cancel movement in that direction
                // Check each axis separately for smoother collision handling

                // Try moving only horizontally
                Rectangle horizBounds = new Rectangle(newX, y, size, size);
                if (!horizBounds.intersects(wall.getBounds())) {
                    x = newX;  // Move horizontally only
                }

                // Try moving only vertically
                Rectangle vertBounds = new Rectangle(x, newY, size, size);
                if (!vertBounds.intersects(wall.getBounds())) {
                    y = newY;  // Move vertically only
                }
                return;  // Stop further movement if collision
            }
        }

        // No collision, update position fully
        x = newX;
        y = newY;
    }

    // Return current bounds (used for collision detection)
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    // Handle key press to update movement direction
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -speed;
            dy = 0;
        } else if (key == KeyEvent.VK_RIGHT) {
            dx = speed;
            dy = 0;
        } else if (key == KeyEvent.VK_UP) {
            dx = 0;
            dy = -speed;
        } else if (key == KeyEvent.VK_DOWN) {
            dx = 0;
            dy = speed;
        }
    }

    // Handle key release to stop movement on released arrow keys
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }
}
