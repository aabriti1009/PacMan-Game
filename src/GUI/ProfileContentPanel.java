package GUI;

import pacmangame.model.User;

import javax.swing.*;
import java.awt.*;

public class ProfileContentPanel extends JPanel {

    public ProfileContentPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Main Title ---
        JLabel titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // --- Center Panel for User Info and Stats ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // --- Profile Image ---
        try {
            ImageIcon profileIcon = new ImageIcon(getClass().getResource("/imagepicker/img/profile-user (1) (1).png"));
            Image scaledImage = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            centerPanel.add(imageLabel);
        } catch (Exception e) {
            System.err.println("Profile image not found.");
        }

        // --- Username ---
        JLabel usernameLabel = new JLabel(user.getUserName());
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(usernameLabel);
        
        // --- Email ---
        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(emailLabel);

        centerPanel.add(Box.createVerticalStrut(30)); // Spacer

        // --- Stats Panel ---
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        addStat(statsPanel, gbc, "Games Played:", String.valueOf(user.getGamesPlayed()), 0);
        addStat(statsPanel, gbc, "High Score:", String.valueOf(user.getHighScore()), 1);
        addStat(statsPanel, gbc, "Total Score:", String.valueOf(user.getTotalScore()), 2);
        
        centerPanel.add(statsPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void addStat(JPanel panel, GridBagConstraints gbc, String label, String value, int y) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelComponent.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(labelComponent, gbc);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        valueComponent.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(valueComponent, gbc);
    }
} 