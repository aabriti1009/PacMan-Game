package Score_Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PacmanLeaderboard extends JFrame {
    // Sample leaderboard data
    private static final String[] NAMES = {
            "SIMRAN", "BLAZE", "PIXEL", "GHOST", "CHOMP",
            "DOTTY", "RETRO", "ZAP", "FLASH", "URUSHA",
            "CYBER", "NOVA", "SPARK", "VORTEX"
    };
    private static final int[] SCORES = {
            12000, 11000, 10500, 9900, 9500, 9000, 8500, 8000, 7500, 7000,
            6800, 6500, 6200, 6000
    };
    // Neon colors for names
    private static final Color[] NEON_COLORS = {
            new Color(57,255,20), // Green
            new Color(0,255,255), // Cyan
            new Color(255,0,255), // Magenta
            new Color(255,255,0), // Yellow
            new Color(255,51,51), // Red
            new Color(255,153,51), // Orange
            new Color(51,153,255), // Blue
            new Color(255,102,255), // Pink
            new Color(255,255,255), // White
            new Color(255,0,102)   // Hot Pink
    };
    // Ghost colors
    private static final Color[] GHOST_COLORS = {
            new Color(255,51,51),   // Red
            new Color(51,153,255),  // Blue
            new Color(255,102,255), // Pink
            new Color(255,153,51)   // Orange
    };
    // Pixel font (fallback to Monospaced if not found)
    private Font pixelFont;
    // Animation for Pac-Man
    private static final int NUM_PACMANS = 3;
    private int[] pacmanX = new int[NUM_PACMANS];
    private int pacmanY;
    private Timer timer;
    private static final int PACMAN_SIZE = 36;
    private static final int PACMAN_SPEED = 4;
    // Attract mode variables
    private Timer attractTimer;
    private boolean inAttractMode = false;
    private int attractPacmanX = 0;
    private int attractGhostX = -80;
    private int attractY = 0;
    private static final int ATTRACT_PACMAN_SIZE = 40;
    private static final int ATTRACT_GHOST_SIZE = 40;
    private static final int ATTRACT_DOT_SIZE = 10;
    private static final int ATTRACT_NUM_DOTS = 12;
    private int[] attractDotX = new int[ATTRACT_NUM_DOTS];
    private boolean[] attractDotEaten = new boolean[ATTRACT_NUM_DOTS];
    private Timer attractAnimTimer;
    private long lastActivityTime = System.currentTimeMillis();
    // Scroll variables
    private int scrollIndex = 0;
    private JButton upButton, downButton;
    private static final int VISIBLE_ROWS = 10;

    public PacmanLeaderboard() {
        setTitle("PAC-MAN LEADERBOARD");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.BLACK);
        setResizable(true);
        loadPixelFont();
        // Initialize Pac-Man positions
        for (int i = 0; i < NUM_PACMANS; i++) {
            pacmanX[i] = -i * 120; // Staggered start
        }
        // Scroll buttons
        setLayout(null);
        upButton = new JButton("▲");
        downButton = new JButton("▼");
        upButton.setFocusable(false);
        downButton.setFocusable(false);
        upButton.setBounds(getWidth() - 70, 160, 50, 40);
        downButton.setBounds(getWidth() - 70, 160 + 45*VISIBLE_ROWS, 50, 40);
        upButton.addActionListener(e -> scrollUp());
        downButton.addActionListener(e -> scrollDown());
        // White retro style for scroll buttons
        upButton.setBackground(Color.WHITE);
        upButton.setForeground(Color.BLACK);
        upButton.setBorderPainted(false);
        upButton.setFocusPainted(false);
        upButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        downButton.setBackground(Color.WHITE);
        downButton.setForeground(Color.BLACK);
        downButton.setBorderPainted(false);
        downButton.setFocusPainted(false);
        downButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(upButton);
        add(downButton);
        updateScrollButtons();
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                upButton.setBounds(getWidth() - 70, 160, 50, 40);
                downButton.setBounds(getWidth() - 70, 160 + 45*VISIBLE_ROWS, 50, 40);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                lastActivityTime = System.currentTimeMillis();
                if (inAttractMode) {
                    stopAttractMode();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (timer != null) timer.stop();
                    if (attractTimer != null) attractTimer.stop();
                    if (attractAnimTimer != null) attractAnimTimer.stop();
                    dispose();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    scrollUp();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    scrollDown();
                }
            }
        });
        // Animation timer
        timer = new Timer(30, e -> {
            if (!inAttractMode) {
                for (int i = 0; i < NUM_PACMANS; i++) {
                    pacmanX[i] += PACMAN_SPEED;
                    if (pacmanX[i] > getWidth()) {
                        pacmanX[i] = -PACMAN_SIZE;
                    }
                }
                repaint();
            }
        });
        timer.start();
        // Attract mode inactivity timer
        attractTimer = new Timer(1000, e -> checkAttractMode());
        attractTimer.start();
    }

    private void loadPixelFont() {
        try {
            // Try to load a pixel font from resources (add your .ttf to resources if available)
            // For now, fallback to Monospaced bold
            pixelFont = new Font("Monospaced", Font.BOLD, 28);
        } catch (Exception e) {
            pixelFont = new Font("Monospaced", Font.BOLD, 28);
        }
    }

    @Override
    public void paint(Graphics g) {
        // Double buffering to prevent flicker
        int width = getWidth();
        int height = getHeight();
        BufferedImage offscreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = offscreen.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (inAttractMode) {
            // Draw black background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, width, height);
            // Draw dots
            for (int i = 0; i < ATTRACT_NUM_DOTS; i++) {
                if (!attractDotEaten[i]) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(attractDotX[i], attractY + ATTRACT_PACMAN_SIZE/2 - ATTRACT_DOT_SIZE/2, ATTRACT_DOT_SIZE, ATTRACT_DOT_SIZE);
                }
            }
            // Draw Pac-Man
            drawPacman(g2, attractPacmanX, attractY, ATTRACT_PACMAN_SIZE);
            // Draw ghost
            drawGhost(g2, attractGhostX, attractY, new Color(255,51,51));
            // Demo message
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 32f));
            g2.setColor(Color.YELLOW);
            String msg = "DEMO MODE - PRESS ANY KEY";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, (width-msgWidth)/2, 120);
        } else {
            // Background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, width, height);
            // Title
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 40f));
            g2.setColor(Color.YELLOW);
            String title = "LEADERBOARD";
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, (width-titleWidth)/2, 90);
            // Columns
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 28f));
            g2.setColor(Color.CYAN);
            int colY = 150;
            int rankX = width/2 - 200;
            int nameX = width/2 - 40;
            int scoreX = width/2 + 160;
            g2.drawString("RANK", rankX, colY);
            g2.drawString("NAME", nameX, colY);
            g2.drawString("SCORE", scoreX, colY);
            // Rows
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 28f));
            int rowHeight = 45;
            int maxRows = Math.min(VISIBLE_ROWS, NAMES.length);
            for (int i = 0; i < maxRows; i++) {
                int y = colY + 50 + i*rowHeight;
                int dataIndex = scrollIndex + i;
                g2.setColor(Color.YELLOW);
                g2.drawString(String.valueOf(dataIndex+1), rankX, y);
                g2.setColor(NEON_COLORS[dataIndex % NEON_COLORS.length]);
                g2.drawString(NAMES[dataIndex], nameX, y);
                g2.setColor(Color.WHITE);
                g2.drawString(String.valueOf(SCORES[dataIndex]), scoreX, y);
            }
            // Animated Pac-Mans
            pacmanY = colY + 50 + VISIBLE_ROWS*rowHeight + 10; // Just below last row
            for (int i = 0; i < NUM_PACMANS; i++) {
                drawPacman(g2, pacmanX[i], pacmanY, PACMAN_SIZE);
            }
            // Draw ghosts
            int ghostY = height - 120;
            int ghostSpacing = 100;
            int ghostStartX = width/2 - (ghostSpacing*2 - 20);
            for (int i = 0; i < 4; i++) {
                drawGhost(g2, ghostStartX + i*ghostSpacing, ghostY, GHOST_COLORS[i]);
            }
            // Bottom message
            g2.setFont(pixelFont.deriveFont(Font.BOLD, 24f));
            g2.setColor(Color.YELLOW);
            String msg = "PRESS ENTER TO PLAY AGAIN";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, (width-msgWidth)/2, height-40);
        }
        g2.dispose();
        g.drawImage(offscreen, 0, 0, null);
    }

    private void drawGhost(Graphics2D g2, int x, int y, Color bodyColor) {
        int w = 60, h = 60;
        // Body
        g2.setColor(bodyColor);
        g2.fillRoundRect(x, y, w, h, 30, 30);
        // Bottom waves
        for (int i = 0; i < 4; i++) {
            g2.fillOval(x + i*15, y + 45, 15, 20);
        }
        // Eyes
        g2.setColor(Color.WHITE);
        g2.fillOval(x+12, y+18, 16, 18);
        g2.fillOval(x+32, y+18, 16, 18);
        g2.setColor(Color.BLUE);
        g2.fillOval(x+18, y+28, 8, 8);
        g2.fillOval(x+38, y+28, 8, 8);
    }

    private void drawPacman(Graphics2D g2, int x, int y, int size) {
        g2.setColor(Color.YELLOW);
        g2.fillArc(x, y, size, size, 30, 300); // Open mouth Pac-Man
        g2.setColor(Color.BLACK);
        g2.fillArc(x+size/4, y+size/8, size/2, size/2, 0, 180); // Mouth shadow
        g2.setColor(Color.BLACK);
        g2.fillOval(x+size/2+2, y+size/6, size/7, size/7); // Eye
    }

    private void checkAttractMode() {
        if (!inAttractMode && System.currentTimeMillis() - lastActivityTime > 15000) {
            startAttractMode();
        }
    }

    private void startAttractMode() {
        inAttractMode = true;
        // Setup demo positions
        attractPacmanX = 40;
        attractGhostX = -80;
        attractY = getHeight()/2 + 40;
        for (int i = 0; i < ATTRACT_NUM_DOTS; i++) {
            attractDotX[i] = 80 + i * 45;
            attractDotEaten[i] = false;
        }
        if (timer != null) timer.stop();
        attractAnimTimer = new Timer(30, e -> animateAttractMode());
        attractAnimTimer.start();
        repaint();
    }

    private void stopAttractMode() {
        inAttractMode = false;
        lastActivityTime = System.currentTimeMillis();
        if (attractAnimTimer != null) attractAnimTimer.stop();
        if (timer != null) timer.start();
        repaint();
    }

    private void animateAttractMode() {
        attractPacmanX += 6;
        attractGhostX += 6;
        // Eat dots
        for (int i = 0; i < ATTRACT_NUM_DOTS; i++) {
            if (!attractDotEaten[i] && attractPacmanX + ATTRACT_PACMAN_SIZE/2 > attractDotX[i]) {
                attractDotEaten[i] = true;
            }
        }
        // Loop
        if (attractPacmanX > getWidth() + 40) {
            attractPacmanX = 40;
            attractGhostX = -80;
            for (int i = 0; i < ATTRACT_NUM_DOTS; i++) attractDotEaten[i] = false;
        }
        repaint();
    }

    private void scrollUp() {
        if (scrollIndex > 0) {
            scrollIndex--;
            repaint();
            updateScrollButtons();
        }
    }

    private void scrollDown() {
        if (scrollIndex < NAMES.length - VISIBLE_ROWS) {
            scrollIndex++;
            repaint();
            updateScrollButtons();
        }
    }

    private void updateScrollButtons() {
        upButton.setEnabled(scrollIndex > 0);
        downButton.setEnabled(scrollIndex < NAMES.length - VISIBLE_ROWS);
        upButton.setVisible(NAMES.length > VISIBLE_ROWS);
        downButton.setVisible(NAMES.length > VISIBLE_ROWS);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PacmanLeaderboard().setVisible(true);
        });
    }
} 