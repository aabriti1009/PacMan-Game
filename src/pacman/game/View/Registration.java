/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pacman.game.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.Timer;
import pacman.game.Controller.RegistrationController;

/**
 *
 * @author ACER
 */

public class Registration extends javax.swing.JFrame {
   private RegistrationController controller;
    
    // Colors
    private static final Color THEME_BLUE = new Color(0, 122, 255);
    private static final Color THEME_DARK = new Color(25, 25, 25);
    private static final Color THEME_YELLOW = new Color(255, 223, 0);
    
    // Components declaration
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JLabel confirmPasswordLabel;
    
    // Password visibility toggles
    private JButton passwordVisibilityToggle;
    private JButton confirmPasswordVisibilityToggle;
    
    // Animation properties
    private Timer animationTimer;
    private Timer pacmanTimer;
    private int animationStep = 0;
    private final int ANIMATION_DELAY = 50;
    
    // Pacman animation properties
    private int pacmanX = 0;
    private int pacmanY = 0;
    private int pacmanSize = 30;
    private int mouthAngle = 0;
    private boolean mouthClosing = false;
    private Color pacmanColor = THEME_YELLOW;
    
    // Ghost properties
    private static final int NUM_GHOSTS = 4;
    private int[] ghostX = new int[NUM_GHOSTS];
    private int[] ghostY = new int[NUM_GHOSTS];
    private Color[] ghostColors = {
        Color.RED,      // Blinky
        Color.PINK,     // Pinky
        Color.CYAN,     // Inky
        Color.ORANGE    // Clyde
    };
    
    // Maze properties
    private static final int CELL_SIZE = 20;
    private static final int WALL_THICKNESS = 2;
    private static final Color MAZE_COLOR = new Color(33, 33, 222, 100);
    private static final Color DOT_COLOR = new Color(255, 255, 255, 80);
    private int[][] maze;
    
    // Validation labels
    private JLabel emailValidationLabel;
    private JLabel passwordValidationLabel;
    
    public Registration() {
        controller = new RegistrationController();
        initializeMaze();
        initializeGhosts();
        initializeComponents();
        setupAnimations();
        setupPacmanAnimation();
        customizeComponents();
        layoutComponents();
        addEventListeners();
        
        // Test database connection
        pacman.game.dao.UserDAO dao = new pacman.game.dao.UserDAO();
        dao.testConnection();
    }
    
    private void initializeComponents() {
        mainPanel = new JPanel(null);
        titleLabel = new JLabel("JOIN THE GAME");
        usernameLabel = new JLabel("Username");
        emailLabel = new JLabel("Email");
        passwordLabel = new JLabel("Password");
        confirmPasswordLabel = new JLabel("Confirm Password");
        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        registerButton = new JButton("START GAME");
        backButton = new JButton("← Back");
        emailValidationLabel = new JLabel("");
        passwordValidationLabel = new JLabel("");
        
        // Initialize show/hide buttons
        passwordVisibilityToggle = new JButton("Show");
        confirmPasswordVisibilityToggle = new JButton("Show");
        
        // Style visibility toggle buttons
        styleToggleButton(passwordVisibilityToggle);
        styleToggleButton(confirmPasswordVisibilityToggle);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pacman Registration");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeMaze() {
        // Larger maze pattern that repeats horizontally and vertically
        int[] basePattern = {1,1,1,1,1,0,0,0,1,1,1,1,1};
        maze = new int[30][40]; // Larger maze size
        
        // Fill the maze with a repeating pattern
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                if (row == 0 || row == maze.length-1 || col == 0 || col == maze[0].length-1) {
                    maze[row][col] = 1; // Border walls
                } else {
                    // Create a pattern of paths and walls
                    boolean isVerticalWall = col % 4 == 0;
                    boolean isHorizontalWall = row % 4 == 0;
                    boolean isIntersection = isVerticalWall && isHorizontalWall;
                    
                    if (isIntersection) {
                        maze[row][col] = 1;
                    } else if (isVerticalWall || isHorizontalWall) {
                        maze[row][col] = Math.random() < 0.7 ? 1 : 0; // 70% chance of wall
                    } else {
                        maze[row][col] = 0; // Path
                    }
                }
            }
        }
    }
    
    private void initializeGhosts() {
        // Initialize ghosts at different positions
        for (int i = 0; i < NUM_GHOSTS; i++) {
            ghostX[i] = (i + 1) * 100;
            ghostY[i] = 300;
        }
    }
    
    private void customizeComponents() {
        mainPanel.setBackground(new Color(0, 0, 0, 200));
        
        // Style labels with semi-transparent background panels
        JLabel[] labels = {titleLabel, usernameLabel, emailLabel, passwordLabel, confirmPasswordLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.WHITE);
            label.setFont(label == titleLabel ? 
                new Font("Press Start 2P", Font.BOLD, 24) : 
                new Font("Segoe UI", Font.PLAIN, 14));
            label.setOpaque(true);
            label.setBackground(new Color(0, 0, 0, 150));
        }
        
        titleLabel.setForeground(THEME_YELLOW);
        
        // Style text fields with semi-transparent background
        JTextField[] fields = {usernameField, emailField, passwordField, confirmPasswordField};
        for (JTextField field : fields) {
            field.setBackground(new Color(45, 45, 45, 200));
            field.setForeground(Color.WHITE);
            field.setCaretColor(Color.WHITE);
            field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(THEME_BLUE, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setOpaque(true);
        }
        
        // Style validation labels
        emailValidationLabel.setOpaque(true);
        passwordValidationLabel.setOpaque(true);
        emailValidationLabel.setBackground(new Color(0, 0, 0, 150));
        passwordValidationLabel.setBackground(new Color(0, 0, 0, 150));
        
        // Style buttons
        registerButton.setBackground(new Color(THEME_BLUE.getRed(), THEME_BLUE.getGreen(), THEME_BLUE.getBlue(), 220));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(new LineBorder(THEME_BLUE, 2));
        registerButton.setFont(new Font("Press Start 2P", Font.BOLD, 16));
        
        backButton.setBackground(new Color(THEME_DARK.getRed(), THEME_DARK.getGreen(), THEME_DARK.getBlue(), 200));
        backButton.setForeground(Color.WHITE);
        backButton.setBorder(null);
        backButton.setFocusPainted(false);
    }
    
    private void styleToggleButton(JButton button) {
        // Style the show/hide buttons
        button.setBackground(new Color(240, 240, 240));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(THEME_BLUE, 1));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
                button.setBorder(BorderFactory.createLineBorder(THEME_BLUE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
                button.setBorder(BorderFactory.createLineBorder(THEME_BLUE, 1));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(200, 200, 200));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(220, 220, 220));
            }
        });
    }
    
    private void layoutComponents() {
        // Main panel
        mainPanel.setBounds(0, 0, 800, 600);
        add(mainPanel);
        
        // Title
        titleLabel.setBounds(200, 50, 400, 40);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Labels and Fields
        usernameLabel.setBounds(250, 120, 300, 20);
        usernameField.setBounds(250, 145, 300, 40);
        
        emailLabel.setBounds(250, 195, 300, 20);
        emailField.setBounds(250, 220, 300, 40);
        
        passwordLabel.setBounds(250, 270, 300, 20);
        passwordField.setBounds(250, 295, 260, 40);
        
        confirmPasswordLabel.setBounds(250, 345, 300, 20);
        confirmPasswordField.setBounds(250, 370, 260, 40);
        
        // Buttons
        registerButton.setBounds(250, 440, 300, 50);
        backButton.setBounds(20, 20, 80, 30);
        
        // Add validation labels
        emailValidationLabel.setBounds(250, 260, 300, 20);
        passwordValidationLabel.setBounds(250, 335, 300, 20);
        
        // Position password field and its visibility toggle
        passwordField.setBounds(250, 295, 260, 40);
        passwordVisibilityToggle.setBounds(515, 300, 60, 34);
        
        // Position confirm password field and its visibility toggle
        confirmPasswordField.setBounds(250, 370, 260, 40);
        confirmPasswordVisibilityToggle.setBounds(515, 375, 60, 34);

        // Add toggle buttons to panel
        mainPanel.add(passwordVisibilityToggle);
        mainPanel.add(confirmPasswordVisibilityToggle);
        
        // Make toggle buttons invisible initially (they will be shown during animation)
        passwordVisibilityToggle.setVisible(false);
        confirmPasswordVisibilityToggle.setVisible(false);
        
        // Add components to panel
        JComponent[] components = {
            titleLabel, usernameLabel, usernameField,
            emailLabel, emailField, passwordLabel, passwordField,
            confirmPasswordLabel, confirmPasswordField,
            registerButton, backButton,
            emailValidationLabel, passwordValidationLabel
        };
        
        for (JComponent component : components) {
            mainPanel.add(component);
            component.setVisible(false);
        }
    }
    
    private void setupAnimations() {
        JComponent[] components = {
            titleLabel, usernameLabel, usernameField,
            emailLabel, emailField, 
            passwordLabel, passwordField, passwordVisibilityToggle,
            confirmPasswordLabel, confirmPasswordField, confirmPasswordVisibilityToggle,
            registerButton, backButton
        };
        
        animationTimer = new Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationStep < components.length) {
                    animateComponent(components[animationStep]);
                    animationStep++;
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        
        SwingUtilities.invokeLater(() -> animationTimer.start());
    }
    
    private void animateComponent(JComponent component) {
        final int originalX = component.getX();
        final int targetX = originalX;
        component.setLocation(originalX - 50, component.getY());
        component.setVisible(true);
        
        Timer slideTimer = new Timer(10, null);
        slideTimer.addActionListener(new ActionListener() {
            float alpha = 0f;
            int currentX = component.getX();
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 0.1f;
                currentX += 5;
                
                if (alpha >= 1f && currentX >= targetX) {
                    component.setLocation(targetX, component.getY());
                    slideTimer.stop();
                } else {
                    component.setLocation(currentX, component.getY());
                    if (component instanceof JLabel || component instanceof JButton) {
                        component.setForeground(new Color(
                            component.getForeground().getRed(),
                            component.getForeground().getGreen(),
                            component.getForeground().getBlue(),
                            (int)(alpha * 255)
                        ));
                    }
                }
                component.repaint();
            }
        });
        slideTimer.start();
    }
    
    private void setupPacmanAnimation() {
        pacmanTimer = new Timer(50, e -> {
            // Move Pacman
            pacmanX = (pacmanX + 5) % getWidth();
            
            // Animate mouth
            if (mouthClosing) {
                mouthAngle -= 5;
                if (mouthAngle <= 0) {
                    mouthClosing = false;
                }
            } else {
                mouthAngle += 5;
                if (mouthAngle >= 45) {
                    mouthClosing = true;
                }
            }
            
            // Move ghosts with wider range
            for (int i = 0; i < NUM_GHOSTS; i++) {
                ghostX[i] = (ghostX[i] + 3 + i) % getWidth();
                ghostY[i] = 100 + (int)(200 * Math.sin((ghostX[i] + i * 50) / 100.0));
            }
            
            // Force repaint
            mainPanel.repaint();
        });
        pacmanTimer.start();
        
        // Override the panel's paintComponent
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw maze covering the entire background
                drawMaze(g2d);
                
                // Draw ghosts
                for (int i = 0; i < NUM_GHOSTS; i++) {
                    drawGhost(g2d, ghostX[i], ghostY[i], ghostColors[i]);
                }
                
                // Draw Pacman
                g2d.setColor(pacmanColor);
                g2d.fillArc(pacmanX, pacmanY + 300, pacmanSize, pacmanSize, mouthAngle, 360 - 2 * mouthAngle);
            }
            
            private void drawMaze(Graphics2D g2d) {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                
                // Calculate scaling factors to fill the entire panel
                double scaleX = (double) panelWidth / (maze[0].length * CELL_SIZE);
                double scaleY = (double) panelHeight / (maze.length * CELL_SIZE);
                
                g2d.scale(scaleX, scaleY);
                
                for (int row = 0; row < maze.length; row++) {
                    for (int col = 0; col < maze[0].length; col++) {
                        if (maze[row][col] == 1) {
                            g2d.setColor(MAZE_COLOR);
                            g2d.fillRect(
                                col * CELL_SIZE,
                                row * CELL_SIZE,
                                CELL_SIZE,
                                CELL_SIZE
                            );
                        } else {
                            // Draw dots
                            g2d.setColor(DOT_COLOR);
                            g2d.fillOval(
                                col * CELL_SIZE + CELL_SIZE/2 - 2,
                                row * CELL_SIZE + CELL_SIZE/2 - 2,
                                4,
                                4
                            );
                        }
                    }
                }
                
                // Reset the scaling
                g2d.scale(1/scaleX, 1/scaleY);
            }
            
            private void drawGhost(Graphics2D g2d, int x, int y, Color color) {
                g2d.setColor(color);
                
                // Ghost body
                g2d.fillArc(x, y, 30, 30, 0, 180);
                g2d.fillRect(x, y + 15, 30, 15);
                
                // Ghost skirt
                int[] xPoints = {x, x + 5, x + 10, x + 15, x + 20, x + 25, x + 30};
                int[] yPoints = {y + 30, y + 25, y + 30, y + 25, y + 30, y + 25, y + 30};
                g2d.fillPolygon(xPoints, yPoints, 7);
                
                // Ghost eyes
                g2d.setColor(Color.WHITE);
                g2d.fillOval(x + 7, y + 8, 6, 6);
                g2d.fillOval(x + 17, y + 8, 6, 6);
                g2d.setColor(Color.BLACK);
                g2d.fillOval(x + 9, y + 10, 2, 2);
                g2d.fillOval(x + 19, y + 10, 2, 2);
            }
        };
        mainPanel.setBackground(THEME_DARK);
    }
    
    private void addEventListeners() {
        registerButton.addActionListener(e -> handleRegistration());
        backButton.addActionListener(e -> dispose());
        
        // Add hover effects
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(0, 90, 190));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(THEME_BLUE);
            }
        });
        
        // Real-time email validation
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateEmail(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateEmail(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateEmail(); }
        });
        
        // Password validation
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validatePassword(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validatePassword(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validatePassword(); }
        });

        // Add password visibility toggle listeners
        passwordVisibilityToggle.addActionListener(e -> {
            boolean visible = passwordVisibilityToggle.getText().equals("Show");
            passwordField.setEchoChar(visible ? (char)0 : '•');
            passwordVisibilityToggle.setText(visible ? "Hide" : "Show");
        });

        confirmPasswordVisibilityToggle.addActionListener(e -> {
            boolean visible = confirmPasswordVisibilityToggle.getText().equals("Show");
            confirmPasswordField.setEchoChar(visible ? (char)0 : '•');
            confirmPasswordVisibilityToggle.setText(visible ? "Hide" : "Show");
        });
    }
    
    private void handleRegistration() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // First check if passwords match (this is UI logic)
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match. Please try again");
            confirmPasswordField.requestFocus();
            return;
        }
        
        // Delegate to controller for registration
        if (controller.registerUser(username, email, password)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful!\nWelcome to Pacman!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close registration window
        } else {
            showError(controller.getLastError());
        }
    }
    
    private void validateEmail() {
        String email = emailField.getText();
        String result = controller.validateEmail(email);
        
        if (result == null) {
            // Valid email
            emailValidationLabel.setText("Valid email ✓");
            emailValidationLabel.setForeground(new Color(0, 255, 0));
        } else {
            // Invalid email
            emailValidationLabel.setText(result);
            emailValidationLabel.setForeground(Color.RED);
        }
    }
    
    private void validatePassword() {
        String password = new String(passwordField.getPassword());
        String result = controller.validatePassword(password);
        
        if (result == null) {
            // Valid password
            passwordValidationLabel.setText("Password valid ✓");
            passwordValidationLabel.setForeground(new Color(0, 255, 0));
        } else {
            // Invalid password
            passwordValidationLabel.setText(result);
            passwordValidationLabel.setForeground(Color.RED);
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Registration Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Registration().setVisible(true);
        });
    }
}   
    

