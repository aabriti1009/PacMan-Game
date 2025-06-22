package GUI;

import Game.Game;
import pacmangame.dao.UserDAO;
import pacmangame.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardWindow extends JFrame {

    public LeaderboardWindow() {
        setTitle("Leaderboard");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        getContentPane().setBackground(new Color(25, 25, 112)); // Midnight Blue

        // When this window is closed, resume the game
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (Game.gamethread() != null) {
                    Game.gamethread().resumeGame();
                }
            }
        });

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(25, 25, 112)); // Midnight Blue
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.YELLOW);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Fetch and sort user data
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        users.sort(Comparator.comparingInt(User::getHighScore).reversed());

        // Table setup
        String[] columnNames = {"Rank", "Username", "High Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.YELLOW);
        table.setFillsViewportHeight(true);
        
        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(Color.YELLOW);
        header.setForeground(Color.BLACK);

        // Populate table data
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Object[] row = {i + 1, user.getUserName(), user.getHighScore()};
            model.addRow(row);
        }

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(25, 25, 112)); // Match panel background
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setBackground(Color.YELLOW);
        closeButton.setForeground(Color.BLACK);
        closeButton.addActionListener(e -> {
            if (Game.gamethread() != null) {
                Game.gamethread().resumeGame();
            }
            dispose();
        });
        
        // Button panel for better layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(25, 25, 112));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }
} 