import javax.swing.*;           // For JPanel, Timer, etc.
import java.awt.*;              // For Graphics, Color, Dimension
import java.awt.event.*;        // For ActionListener, KeyAdapter, KeyEvent, ActionEvent
import java.util.ArrayList;     // For ArrayList

public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private Player pacman;
    private ArrayList<Wall> walls;

    public Board() {
        setPreferredSize(new Dimension(400, 600));  // Set size of the board
        setBackground(Color.BLACK);                  // Set background color
        setFocusable(true);                          // Allow key input

        pacman = new Player(200, 200);               // Create player at (200, 200)
        walls = new ArrayList<>();

        // Add walls on each side of the board (border walls)
        walls.add(new Wall(0, 0, 400, 20));          // Top wall
        walls.add(new Wall(0, 580, 400, 20));        // Bottom wall
        walls.add(new Wall(0, 0, 20, 600));          // Left wall
        walls.add(new Wall(380, 0, 20, 600));        // Right wall

        // Add sample inner walls
        walls.add(new Wall(100, 100, 200, 20));      // Horizontal wall
        walls.add(new Wall(100, 200, 20, 100));      // Vertical wall

        // Add key listener to control Pac-Man (both press and release)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pacman.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
                pacman.keyReleased(e);
            }
        });

        timer = new Timer(40, this);   // Timer fires every 40ms
        timer.start();                 // Start game loop
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pacman.move(walls);    // Move player, checking walls
        repaint();             // Refresh screen
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        pacman.draw(g);        // Draw Pac-Man

        // Draw all walls
        for (Wall w : walls) {
            w.draw(g);
        }
    }
}
