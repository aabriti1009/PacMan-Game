package GUI;

import Game.Game;
import Media.EImage;
import Media.Media;
import Settings.EParam;
import Settings.Settings;
import pacmangame.model.User;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PostLoginStartGamePanel extends JPanel implements Runnable {

    private PostLoginMainMenu menu;
    private User currentUser;
    private JButton startGameButton;
    private JButton backButton;
    private Thread animationThread;

    public PostLoginStartGamePanel(int width, PostLoginMainMenu menu, User user) {
        this.menu = menu;
        this.currentUser = user;
        
        setLayout(new GridBagLayout());
        setBackground(new Color(135, 206, 235)); // Sky Blue
        
        initComponents();
        addListeners();

        setSize(width, menu.getHeight());

        animationThread = new Thread(this);
        animationThread.start();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Start Game");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 30, 10); // Add bottom margin
        add(titleLabel, gbc);

        // Reset insets
        gbc.insets = new Insets(10, 10, 10, 10);

        startGameButton = new JButton("Start Game");
        styleButton(startGameButton);
        // Add icon to the start game button
        try {
            ImageIcon playIcon = new ImageIcon(getClass().getResource("/imagepicker/img/play-button (1).png"));
            Image scaledIcon = playIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            startGameButton.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Could not find play icon for start button.");
            e.printStackTrace();
        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(startGameButton, gbc);
        
        backButton = new JButton("Back to Menu");
        styleButton(backButton);
        // Add icon to the back button
        try {
            ImageIcon backIcon = new ImageIcon(getClass().getResource("/imagepicker/img/replay (1) (1).png"));
            Image scaledIcon = backIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            System.err.println("Could not find back icon for back button.");
            e.printStackTrace();
        }
        gbc.gridy = 2;
        add(backButton, gbc);
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setBorder(new LineBorder(Color.BLACK, 2));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 60));
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
    }
    
    private void addListeners() {
        startGameButton.addActionListener(e -> {
            // The game will now use the default number of lives set in Settings.
            new Game(currentUser, menu.getSize());
            menu.dispose();
        });
        
        startGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startGameButton.setBackground(new Color(70, 130, 180));
                startGameButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startGameButton.setBackground(Color.YELLOW);
                startGameButton.setForeground(Color.BLACK);
            }
        });

        backButton.addActionListener(e -> menu.showMainPanel());
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(70, 130, 180));
                backButton.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.YELLOW);
                backButton.setForeground(Color.BLACK);
            }
        });
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(20); // ~50 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int yPosition = getHeight() - 80;
        int canvasWidth = getWidth();
        if (canvasWidth == 0) return;

        long time = System.currentTimeMillis();
        
        // Pac-Man animation
        int pacmanFrame = (int) ((time / 150) % 4);
        EImage pacmanImage;
        switch (pacmanFrame) {
            case 0: pacmanImage = EImage.pacman_right_1; break;
            case 1: pacmanImage = EImage.pacman_right_2; break;
            case 2: pacmanImage = EImage.pacman_right_3; break;
            default: pacmanImage = EImage.pacman_right_4; break;
        }
        
        int xPacman = (int) ((time / 4) % (canvasWidth + 250)) - 100;
        g.drawImage(Media.getImg(pacmanImage), xPacman, yPosition, 40, 40, this);

        // Ghosts
        g.drawImage(Media.getImg(EImage.ghost1_right), xPacman - 60, yPosition, 40, 40, this);
        g.drawImage(Media.getImg(EImage.ghost2_right), xPacman - 110, yPosition, 40, 40, this);
        g.drawImage(Media.getImg(EImage.ghost3_right), xPacman - 160, yPosition, 40, 40, this);
        g.drawImage(Media.getImg(EImage.ghost4_right), xPacman - 210, yPosition, 40, 40, this);
    }
} 