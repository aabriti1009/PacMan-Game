package GUI;

import AnimationEngine.BlinkAnimator;
import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Media.*;
import pacmangame.dao.UserDAO;
import pacmangame.model.User;
import pacmangame.view.LoginView;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Enhanced Main Menu with a split-screen layout.
 */
public class PostLoginMainMenu extends JFrame {
    
    private PostLoginStartGamePanel startpanel;
    private JPanel buttonPanel;
    private JPanel placeholderPanel;
    private CardLayout cardLayout;
    private JLabel title;
    private JLabel welcomeLabel;
    private JButton play_button;
    private JButton settings_button;
    private JButton profile_button;
    private JButton howToPlay_button;
    private JButton logout_button;
    private String username;
    private User currentUser;
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        
        new PostLoginMainMenu("TestUser");
    }
    
    /**
     * Initializes the PostLoginMainMenu frame with a User object.
     * @param user the logged-in user
     */
    public PostLoginMainMenu(User user) {
        super();
        this.currentUser = user;
        this.username = user.getUserName();
        
        setTitle("Main Menu");
        setIconImage(Media.getImg(EImage.pacman_right_1));
        
        initComponents();
        setupButtonListeners();
        
        AudioEngine.play(EAudio.ost, PlaybackMode.loop, null);
    
        startpanel = new PostLoginStartGamePanel(this.getWidth(), this, currentUser);
        
        add(startpanel, 0);
        startpanel.setLocation(0, 0);
        startpanel.setVisible(false);
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Initializes the PostLoginMainMenu frame.
     * @param username the logged-in username
     */
    public PostLoginMainMenu(String username) {
        super();
        this.username = username;
        
        UserDAO userDAO = new UserDAO();
        this.currentUser = userDAO.findByUsername(username);
        
        setTitle("Main Menu");
        setIconImage(Media.getImg(EImage.pacman_right_1));
        
        initComponents();
        setupButtonListeners();
        
        AudioEngine.play(EAudio.ost, PlaybackMode.loop, null);
    
        startpanel = new PostLoginStartGamePanel(this.getWidth(), this, currentUser);
        
        add(startpanel, 0);
        startpanel.setLocation(0, 0);
        startpanel.setVisible(false);
        
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setupButtonListeners() {
        // Play button
        play_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showNewGamepanel();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                play_button.setBackground(new Color(70, 130, 180));
                play_button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                play_button.setBackground(Color.YELLOW);
                play_button.setForeground(Color.BLACK);
            }
        });
        
        // Settings button
        settings_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Pass the currentUser to the SettingsContentPanel
                SettingsContentPanel settingsPanel = new SettingsContentPanel(currentUser);
                placeholderPanel.add(settingsPanel, "Settings");
                cardLayout.show(placeholderPanel, "Settings");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                settings_button.setBackground(new Color(70, 130, 180));
                settings_button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                settings_button.setBackground(Color.YELLOW);
                settings_button.setForeground(Color.BLACK);
            }
        });
        
        // Profile button
        profile_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ProfileContentPanel profilePanel = new ProfileContentPanel(currentUser);
                placeholderPanel.add(profilePanel, "Profile");
                cardLayout.show(placeholderPanel, "Profile");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                profile_button.setBackground(new Color(70, 130, 180));
                profile_button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                profile_button.setBackground(Color.YELLOW);
                profile_button.setForeground(Color.BLACK);
            }
        });
        
        // How To Play button
        howToPlay_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Pass the currentUser to the HowToPlayContentPanel if needed, or just show it
                HowToPlayContentPanel howToPlayPanel = new HowToPlayContentPanel();
                placeholderPanel.add(howToPlayPanel, "HowToPlay");
                cardLayout.show(placeholderPanel, "HowToPlay");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                howToPlay_button.setBackground(new Color(70, 130, 180));
                howToPlay_button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                howToPlay_button.setBackground(Color.YELLOW);
                howToPlay_button.setForeground(Color.BLACK);
            }
        });
        
        // Logout button
        logout_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logout();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                logout_button.setBackground(new Color(70, 130, 180));
                logout_button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                logout_button.setBackground(Color.YELLOW);
                logout_button.setForeground(Color.BLACK);
            }
        });
    }
    
    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            AudioEngine.stop(EAudio.ost);
            this.dispose();
            new LoginView();
        }
    }
    
    public void showMainPanel() {
        startpanel.setVisible(false);
        buttonPanel.setVisible(true);
        placeholderPanel.setVisible(true);
    }
    
    public void showNewGamepanel() {
        buttonPanel.setVisible(false);
        placeholderPanel.setVisible(false);
        startpanel.setVisible(true);
    }
    
    private void initComponents() {
        title = new JLabel();
        welcomeLabel = new JLabel();
        play_button = new JButton();
        settings_button = new JButton();
        profile_button = new JButton();
        howToPlay_button = new JButton();
        logout_button = new JButton();

        //======== this ========
        setTitle("Main Menu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setBackground(new Color(135, 206, 235)); // Sky Blue
        contentPane.setLayout(new BorderLayout());

        //======== buttonPanel (Left side for buttons) ========
        buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(135, 206, 235)); // Sky Blue
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //---- title ----
        title.setText("Main Menu");
        title.setFont(new Font("Times New Roman", Font.BOLD, 36));
        title.setForeground(Color.BLACK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        buttonPanel.add(title, gbc);

        //---- welcome label ----
        welcomeLabel.setText("Welcome back, " + username + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.BLACK);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        buttonPanel.add(welcomeLabel, gbc);

        //---- settings_button ----
        settings_button.setText("Settings");
        settings_button.setBackground(Color.YELLOW);
        settings_button.setBorder(new LineBorder(Color.BLACK, 2, true));
        settings_button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        settings_button.setForeground(Color.BLACK);
        gbc.gridy = 2;
        gbc.ipady = 20;
        buttonPanel.add(settings_button, gbc);

        //---- profile_button ----
        profile_button.setText("Profile");
        profile_button.setBackground(Color.YELLOW);
        profile_button.setBorder(new LineBorder(Color.BLACK, 2, true));
        profile_button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        profile_button.setForeground(Color.BLACK);
        gbc.gridy = 3;
        buttonPanel.add(profile_button, gbc);

        //---- howToPlay_button ----
        howToPlay_button.setText("How to Play");
        howToPlay_button.setBackground(Color.YELLOW);
        howToPlay_button.setBorder(new LineBorder(Color.BLACK, 2, true));
        howToPlay_button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        howToPlay_button.setForeground(Color.BLACK);
        gbc.gridy = 4;
        buttonPanel.add(howToPlay_button, gbc);

        //---- play_button ----
        play_button.setText("Play Game");
        play_button.setBackground(Color.YELLOW);
        play_button.setBorder(new LineBorder(Color.BLACK, 2, true));
        play_button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        play_button.setForeground(Color.BLACK);
        gbc.gridy = 5;
        buttonPanel.add(play_button, gbc);

        //---- logout_button ----
        logout_button.setText("Logout");
        logout_button.setBackground(Color.YELLOW);
        logout_button.setBorder(new LineBorder(Color.BLACK, 2, true));
        logout_button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        logout_button.setForeground(Color.BLACK);
        gbc.gridy = 6;
        gbc.ipady = 10;
        gbc.insets = new Insets(30, 20, 10, 20);
        buttonPanel.add(logout_button, gbc);
        
        contentPane.add(buttonPanel, BorderLayout.WEST);

        //======== placeholderPanel (Right side for content) ========
        cardLayout = new CardLayout();
        placeholderPanel = new JPanel(cardLayout);
        placeholderPanel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        
        JLabel defaultLabel = new JLabel("Welcome! Use the menu to explore your options.", SwingConstants.CENTER);
        defaultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        defaultLabel.setForeground(Color.WHITE);
        
        JPanel defaultPanel = new JPanel(new BorderLayout());
        defaultPanel.setBackground(new Color(70, 130, 180));
        defaultPanel.add(defaultLabel, BorderLayout.CENTER);
        
        placeholderPanel.add(defaultPanel, "Default");
        placeholderPanel.add(new SettingsContentPanel(currentUser), "Settings");
        placeholderPanel.add(new ProfileContentPanel(currentUser), "Profile");
        placeholderPanel.add(new HowToPlayContentPanel(), "HowToPlay");
        
        contentPane.add(placeholderPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(getOwner());
    }
} 