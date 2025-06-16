package GUI;

import AnimationEngine.BlinkAnimator;
import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Media.*;
import Painter.Scaler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;


/**
 * The Mainmenu of the game
 */
public class MainMenu extends JFrame {
    
    private StartGamePanel startpanel;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color THEME_BLUE = new Color(0, 122, 255);
    private static final Color THEME_DARK = new Color(25, 25, 25);
    private static final Color THEME_YELLOW = new Color(255, 223, 0);
    private static final Color THEME_PINK = new Color(255, 0, 204);
    private static final Color THEME_MAGENTA = new Color(204, 0, 204);
    
    // Animation properties
    private Timer animationTimer;
    private ArrayList<PacmanEntity> pacmanEntities;
    private ArrayList<GhostEntity> ghostEntities;
    private Random random;
    
    // Maze properties
    private static final int CELL_SIZE = 20;
    private static final Color MAZE_COLOR = new Color(33, 33, 222, 100);
    private static final Color DOT_COLOR = new Color(255, 255, 255, 80);
    private int[][] maze;
    
    private boolean isFullscreen = false;
    private Dimension prevSize;
    private Point prevLocation;
    
    public static void main(String[] args) {
        // Set system properties for better rendering
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        
        // Use EventQueue to ensure proper thread handling
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new MainMenu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Initializes the MainMenu frame.
     */
    public MainMenu() {
        super();
        setTitle("Pacman Game");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);

        // 1. Create a JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // 2. Animated floating dots background (modern animated background)
        JPanel backgroundPanel = new JPanel() {
            private java.util.List<Point2D.Float> dots = new java.util.ArrayList<>();
            private java.util.List<Point2D.Float> velocities = new java.util.ArrayList<>();
            {
                int dotCount = 40;
                for (int i = 0; i < dotCount; i++) {
                    dots.add(new Point2D.Float((float)(Math.random()*getWidth()), (float)(Math.random()*getHeight())));
                    velocities.add(new Point2D.Float((float)(Math.random()*1.5+0.5), (float)(Math.random()*1.5+0.5)));
                }
                animationTimer = new Timer(30, e -> {
                    for (int i = 0; i < dots.size(); i++) {
                        Point2D.Float p = dots.get(i);
                        Point2D.Float v = velocities.get(i);
                        p.x += v.x; p.y += v.y;
                        if (p.x < 0 || p.x > getWidth()) v.x = -v.x;
                        if (p.y < 0 || p.y > getHeight()) v.y = -v.y;
                    }
                    repaint();
                });
                animationTimer.start();
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,new Color(30,30,40),0,getHeight(),new Color(10,10,20));
                g2d.setPaint(gp);
                g2d.fillRect(0,0,getWidth(),getHeight());
                for (Point2D.Float p : dots) {
                    g2d.setColor(new Color(255,255,255,60));
                    g2d.fillOval((int)p.x, (int)p.y, 32, 32);
                }
            }
        };
        backgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(backgroundPanel, JLayeredPane.DEFAULT_LAYER);

        // 3. Centered menu panel with layout
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setOpaque(false);
        menuPanel.setBounds(0, 0, getWidth(), getHeight());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,20,0);
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel title = new JLabel("PAC-MAN");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(new Color(255, 223, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        menuPanel.add(title, gbc);

        // Button factory
        gbc.gridy++;
        gbc.insets = new Insets(10,0,10,0);
        int buttonWidth = 350, buttonHeight = 60;
        Font buttonFont = new Font("Arial", Font.BOLD, 28);
        Color buttonBg = new Color(40,40,50);
        Color buttonFg = Color.WHITE;
        Color buttonHover = new Color(255,223,0);

        JButton startButton = new JButton("\u25B6\uFE0F  Start Game");
        JButton settingsButton = new JButton("\u2699\uFE0F  Settings / Options");
        JButton helpButton = new JButton("\uD83D\uDCD6  Instructions / Help");
        JButton highScoreButton = new JButton("\uD83C\uDFC6  High Scores");
        JButton exitButton = new JButton("\uD83D\uDD12  Exit");
        JButton[] buttons = {startButton, settingsButton, helpButton, highScoreButton, exitButton};
        String[] tooltips = {
            "Start a new game!",
            "Change sound, difficulty, and more",
            "How to play Pac-Man",
            "View high scores",
            "Exit the game"
        };
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];
            btn.setFont(buttonFont);
            btn.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            btn.setBackground(buttonBg);
            btn.setForeground(buttonFg);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(255,223,0), 2));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setToolTipText(tooltips[i]);
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(buttonHover);
                    btn.setForeground(Color.BLACK);
                    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(buttonBg);
                    btn.setForeground(buttonFg);
                    btn.setBorder(BorderFactory.createLineBorder(new Color(255,223,0), 2));
                }
            });
            menuPanel.add(btn, gbc);
            gbc.gridy++;
        }

        // Button actions: open placeholder windows
        startButton.addActionListener(e -> {
            JFrame next = new JFrame("Start Game");
            next.setSize(600,400);
            next.setLocationRelativeTo(this);
            next.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel label = new JLabel("Game Window (to be implemented)", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 32));
            next.add(label);
            next.setVisible(true);
        });
        settingsButton.addActionListener(e -> {
            JFrame next = new JFrame("Settings / Options");
            next.setSize(600,400);
            next.setLocationRelativeTo(this);
            next.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel label = new JLabel("Settings / Options (to be implemented)", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 32));
            next.add(label);
            next.setVisible(true);
        });
        helpButton.addActionListener(e -> {
            JFrame next = new JFrame("Instructions / Help");
            next.setSize(600,400);
            next.setLocationRelativeTo(this);
            next.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel label = new JLabel("Instructions / Help (to be implemented)", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 32));
            next.add(label);
            next.setVisible(true);
        });
        highScoreButton.addActionListener(e -> {
            JFrame next = new JFrame("High Scores");
            next.setSize(600,400);
            next.setLocationRelativeTo(this);
            next.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JLabel label = new JLabel("High Scores (to be implemented)", SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 32));
            next.add(label);
            next.setVisible(true);
        });
        exitButton.addActionListener(e -> System.exit(0));

        layeredPane.add(menuPanel, JLayeredPane.PALETTE_LAYER);

        // Responsive resizing (with animation timer restart on resize)
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                layeredPane.setPreferredSize(new Dimension(w, h));
                backgroundPanel.setBounds(0, 0, w, h);
                menuPanel.setBounds(0, 0, w, h);
                layeredPane.revalidate();
                layeredPane.repaint();
                // Stop and restart the animation timer (if it exists) so that dots update their bounds
                if (backgroundPanel instanceof JPanel) {
                    JPanel bp = (JPanel) backgroundPanel;
                    Timer t = (Timer) bp.getClientProperty("animationTimer");
                    if (t != null) {
                        t.stop();
                        t.start();
                    }
                }
            }
        });
        setVisible(true);
    }
    
    private void initializeMaze() {
        maze = new int[WINDOW_HEIGHT/CELL_SIZE][WINDOW_WIDTH/CELL_SIZE];
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (row == 0 || row == maze.length-1 || col == 0 || col == maze[0].length-1) {
                    maze[row][col] = 1; // Border walls
                } else {
                    maze[row][col] = random.nextFloat() < 0.2 ? 1 : 0; // Random walls
                }
            }
        }
    }
    
    private void initializeEntities() {
        // Add multiple Pacman entities
        for (int i = 0; i < 3; i++) {
            pacmanEntities.add(new PacmanEntity(
                random.nextInt(WINDOW_WIDTH),
                random.nextInt(WINDOW_HEIGHT),
                30,
                THEME_YELLOW
            ));
        }
        
        // Add ghosts with different colors
        Color[] ghostColors = {
            Color.RED,      // Blinky
            Color.PINK,     // Pinky
            Color.CYAN,     // Inky
            Color.ORANGE    // Clyde
        };
        
        for (int i = 0; i < ghostColors.length; i++) {
            ghostEntities.add(new GhostEntity(
                random.nextInt(WINDOW_WIDTH),
                random.nextInt(WINDOW_HEIGHT),
                30,
                ghostColors[i]
            ));
        }
    }
    
    private void setupAnimations() {
        animationTimer = new Timer(16, e -> {
            // Update entities
            for (PacmanEntity pacman : pacmanEntities) {
                pacman.update();
            }
            for (GhostEntity ghost : ghostEntities) {
                ghost.update();
            }
            repaint();
        });
        animationTimer.start();
    }
    
    private void drawMaze(Graphics2D g2d) {
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (maze[row][col] == 1) {
                    g2d.setColor(MAZE_COLOR);
                    g2d.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else {
                    g2d.setColor(DOT_COLOR);
                    g2d.fillOval(
                        col * CELL_SIZE + CELL_SIZE/2 - 2,
                        row * CELL_SIZE + CELL_SIZE/2 - 2,
                        4, 4
                    );
                }
            }
        }
    }
    
    private void customizeComponents() {
        // Style main panel
        main_panel.setBackground(new Color(0, 0, 0, 170));
        
        // Style title
        title.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 64f));
        title.setForeground(THEME_YELLOW);
        title.setBounds(WINDOW_WIDTH/2 - 250, 50, 500, 80);
        
        // Style buttons with modern look
        styleButton(play_button, "PLAY", WINDOW_WIDTH/2 - 150, 250);
        styleButton(exit_button, "EXIT", WINDOW_WIDTH/2 - 150, 350);
        styleButton(github_button, "GitHub", WINDOW_WIDTH - 150, WINDOW_HEIGHT - 80);
    }
    
    private void styleButton(JButton button, String text, int x, int y) {
        button.setText(text);
        button.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 24f));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 0, 0, 150));
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(THEME_YELLOW, 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setBounds(x, y, 200, 50);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 0, 0, 200));
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(THEME_YELLOW, 3),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 0, 0, 150));
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(THEME_YELLOW, 2),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
    }
    
    private void addButtonListeners() {
        // GitHub button
        github_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BlinkAnimator blinkAnimator = new BlinkAnimator(github_button, 80, true);
                blinkAnimator.start();
                AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        blinkAnimator.stop();
                        try {
                            openWebpage(new URI("https://github.com/aabriti1009/PacMan-Game.git"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
        
        // Exit button
        exit_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BlinkAnimator blinkAnimator = new BlinkAnimator(exit_button, 80, true);
                blinkAnimator.start();
                AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        blinkAnimator.stop();
                        System.exit(0);
                    }
                });
            }
        });
        
        // Play button
        play_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BlinkAnimator blinkAnimator = new BlinkAnimator(play_button, 80, true);
                blinkAnimator.start();
                AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        blinkAnimator.stop();
                        showNewGamepanel();
                    }
                });
            }
        });
    }
    
    // Entity classes for animation
    private class PacmanEntity {
        private double x, y;
        private double speedX, speedY;
        private int size;
        private Color color;
        private double mouthAngle;
        private boolean mouthClosing;
        
        public PacmanEntity(int x, int y, int size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.speedX = random.nextDouble() * 4 - 2;
            this.speedY = random.nextDouble() * 4 - 2;
            this.mouthAngle = 0;
            this.mouthClosing = false;
        }
        
        public void update() {
            // Move Pacman
            x += speedX;
            y += speedY;
            
            // Bounce off walls
            if (x < 0 || x > WINDOW_WIDTH - size) speedX *= -1;
            if (y < 0 || y > WINDOW_HEIGHT - size) speedY *= -1;
            
            // Animate mouth
            if (mouthClosing) {
                mouthAngle -= 5;
                if (mouthAngle <= 0) mouthClosing = false;
            } else {
                mouthAngle += 5;
                if (mouthAngle >= 45) mouthClosing = true;
            }
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            double direction = Math.atan2(speedY, speedX);
            AffineTransform old = g2d.getTransform();
            g2d.translate(x + size/2, y + size/2);
            g2d.rotate(direction);
            g2d.fillArc(-size/2, -size/2, size, size, 
                (int)mouthAngle, 360 - 2 * (int)mouthAngle);
            g2d.setTransform(old);
        }
    }
    
    private class GhostEntity {
        private double x, y;
        private double speedX, speedY;
        private int size;
        private Color color;
        
        public GhostEntity(int x, int y, int size, Color color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.color = color;
            this.speedX = random.nextDouble() * 3 - 1.5;
            this.speedY = random.nextDouble() * 3 - 1.5;
        }
        
        public void update() {
            // Move ghost
            x += speedX;
            y += speedY;
            
            // Bounce off walls
            if (x < 0 || x > WINDOW_WIDTH - size) speedX *= -1;
            if (y < 0 || y > WINDOW_HEIGHT - size) speedY *= -1;
            
            // Random direction changes
            if (random.nextFloat() < 0.02) {
                speedX = random.nextDouble() * 3 - 1.5;
                speedY = random.nextDouble() * 3 - 1.5;
            }
        }
        
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            
            // Ghost body
            g2d.fillArc((int)x, (int)y, size, size, 0, 180);
            g2d.fillRect((int)x, (int)y + size/2, size, size/2);
            
            // Ghost skirt
            int[] xPoints = {(int)x, (int)x + size/6, (int)x + size/3, (int)x + size/2, 
                           (int)x + 2*size/3, (int)x + 5*size/6, (int)x + size};
            int[] yPoints = {(int)y + size, (int)y + size - size/6, (int)y + size, 
                           (int)y + size - size/6, (int)y + size, (int)y + size - size/6, (int)y + size};
            g2d.fillPolygon(xPoints, yPoints, 7);
            
            // Ghost eyes
            g2d.setColor(Color.WHITE);
            g2d.fillOval((int)x + size/4, (int)(y + size/3), size/4, size/4);
            g2d.fillOval((int)x + size/2, (int)(y + size/3), size/4, size/4);
            g2d.setColor(Color.BLACK);
            g2d.fillOval((int)x + size/3, (int)(y + size/2.5), size/8, size/8);
            g2d.fillOval((int)x + 5*size/8, (int)(y + size/2.5), size/8, size/8);
        }
    }
    
    /**
     * Opens a webpage in the Systems default web browser.
     * @param uri
     */
    public void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void showMainPanel() {
        startpanel.setVisible(false);
        main_panel.setVisible(true);
    }
    
    public void showNewGamepanel() {
        main_panel.setVisible(false);
        startpanel.setVisible(true);
    }
    
    
    
    public void adjustSizes() {
        setSize(Scaler.scale(main_panel.getWidth()), Scaler.scale(main_panel.getHeight()));
        int width = this.getWidth();
        int height = this.getHeight();
        Media.rescaleMedia(Scaler.getScale_factor());
        
        main_panel.setSize(width, height);
        title.setBounds(Scaler.scale(title.getX()), Scaler.scale(title.getY()), Scaler.scale(title.getWidth()),Scaler.scale(title.getHeight()));
        title.setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, Scaler.scale(title.getFont().getSize())));
        
        play_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, (int) Scaler.scale(play_button.getFont().getSize())));
        play_button.setBounds(Scaler.scale(play_button.getX()), Scaler.scale(play_button.getY()), Scaler.scale(play_button.getWidth()),Scaler.scale(play_button.getHeight()));
        play_button.setBorder(BorderFactory.createLineBorder(Color.yellow, Scaler.scale(3)));
        play_button.setOpaque(true);
        
        exit_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, (int) Scaler.scale(exit_button.getFont().getSize())));
        exit_button.setBounds(Scaler.scale(exit_button.getX()), Scaler.scale(exit_button.getY()), Scaler.scale(exit_button.getWidth()),Scaler.scale(exit_button.getHeight()));
        exit_button.setBorder(BorderFactory.createLineBorder(Color.yellow, Scaler.scale(3)));
        exit_button.setOpaque(true);
        
        github_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, (int) Scaler.scale(github_button.getFont().getSize())));
        github_button.setBounds(Scaler.scale(github_button.getX()), Scaler.scale(github_button.getY()), Scaler.scale(github_button.getWidth()),Scaler.scale(github_button.getHeight()));
        github_button.setBorder(BorderFactory.createLineBorder(Color.yellow, Scaler.scale(3)));
        github_button.setOpaque(true);
    }
    
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        main_panel = new JPanel();
        title = new JLabel();
        github_button = new JButton();
        play_button = new JButton();
        exit_button = new JButton();

        //======== this ========
        setTitle("Pacman Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== main_panel ========
        {
            main_panel.setBackground(new Color(0, 0, 0, 170));
            main_panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            main_panel.setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            main_panel.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
            main_panel.setLayout(null);

            //---- title ----
            title.setText("PACMAN");
            title.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 64f));
            title.setForeground(THEME_YELLOW);
            title.setHorizontalAlignment(SwingConstants.CENTER);
            main_panel.add(title);
            title.setBounds(WINDOW_WIDTH/2 - 250, 50, 500, 80);

            //---- github_button ----
            github_button.setText("GitHub");
            github_button.setBackground(new Color(0, 0, 27));
            github_button.setForeground(THEME_MAGENTA);
            github_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 20f));
            github_button.setBorder(new LineBorder(THEME_YELLOW, 2, true));
            github_button.setContentAreaFilled(false);
            main_panel.add(github_button);
            github_button.setBounds(WINDOW_WIDTH - 150, WINDOW_HEIGHT - 80, 120, 50);

            //---- play_button ----
            play_button.setText("PLAY");
            play_button.setBackground(new Color(0, 0, 27));
            play_button.setBorder(new LineBorder(THEME_YELLOW, 2, true));
            play_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 32f));
            play_button.setForeground(THEME_PINK);
            play_button.setContentAreaFilled(false);
            main_panel.add(play_button);
            play_button.setBounds(WINDOW_WIDTH/2 - 150, 250, 300, 60);

            //---- exit_button ----
            exit_button.setText("EXIT");
            exit_button.setBackground(new Color(0, 0, 27));
            exit_button.setForeground(THEME_PINK);
            exit_button.setFont(Media.getFont(EFont.regular).deriveFont(Font.BOLD, 32f));
            exit_button.setBorder(new LineBorder(THEME_YELLOW, 2, true));
            exit_button.setContentAreaFilled(false);
            main_panel.add(exit_button);
            exit_button.setBounds(WINDOW_WIDTH/2 - 150, 350, 300, 60);

            // DEBUG LABEL
            JLabel debugLabel = new JLabel("DEBUG: MAIN MENU VISIBLE");
            debugLabel.setForeground(Color.RED);
            debugLabel.setFont(new Font("Arial", Font.BOLD, 32));
            debugLabel.setBounds(10, 10, 500, 50);
            main_panel.add(debugLabel);
        }
        contentPane.add(main_panel);
        main_panel.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel main_panel;
    private JLabel title;
    private JButton github_button;
    private JButton play_button;
    private JButton exit_button;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}