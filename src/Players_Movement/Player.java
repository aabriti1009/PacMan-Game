import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {
    private int x, y;
    private int size = 30;
    private int speed = 5;

    private int dx = 0, dy = 0;

    private int mouthAngle = 45;
    private boolean mouthOpening = true;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);

        // Animate mouth
        if (mouthOpening) {
            mouthAngle -= 5;
            if (mouthAngle <= 5) mouthOpening = false;
        } else {
            mouthAngle += 5;
            if (mouthAngle >= 45) mouthOpening = true;
        }

        // Determine direction angle for mouth
        int startAngle = 0;
        if (dx > 0) startAngle = mouthAngle;           // Right
        else if (dx < 0) startAngle = 180 + mouthAngle; // Left
        else if (dy > 0) startAngle = 270 + mouthAngle; // Down
        else if (dy < 0) startAngle = 90 + mouthAngle;  // Up

        // Draw Pac-Man as arc
        g.fillArc(x, y, size, size, startAngle, 360 - 2 * mouthAngle);
    }

    public void move(ArrayList<Wall> walls) {
        int newX = x + dx;
        int newY = y + dy;

        Rectangle newBounds = new Rectangle(newX, newY, size, size);

        for (Wall wall : walls) {
            if (newBounds.intersects(wall.getBounds())) {
                // Try moving only in X direction
                Rectangle horiz = new Rectangle(newX, y, size, size);
                Rectangle vert = new Rectangle(x, newY, size, size);

                if (!horiz.intersects(wall.getBounds())) {
                    x = newX;
                }
                if (!vert.intersects(wall.getBounds())) {
                    y = newY;
                }
                return; // Stop full movement
            }
        }

        // No collision
        x = newX;
        y = newY;
    }

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
