package GUI;

import Game.Game;
import pacmangame.dao.UserDAO;
import pacmangame.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.util.Comparator;
import java.util.List;

public class GameOverWindow extends JFrame {

    public GameOverWindow(User currentUser, long finalScore) {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600); // Match main menu size
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(135, 206, 235)); // Sky Blue

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(new Color(135, 206, 235)); // Sky Blue
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // --- Top Panel for Game Over, Score, and Rank ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(new Color(135, 206, 235)); // Sky Blue

        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Times New Roman", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.BLACK);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Fetch users and determine rank
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();
        users.sort(Comparator.comparingInt(User::getHighScore).reversed());
        int rank = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == currentUser.getId()) {
                rank = i + 1;
                break;
            }
        }
        final int finalRank = rank;

        JLabel scoreLabel = new JLabel("Your Score: " + finalScore);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rankLabel = new JLabel("You Ranked: #" + finalRank);
        rankLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        rankLabel.setForeground(Color.BLACK);
        rankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(gameOverLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(scoreLabel);
        topPanel.add(rankLabel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // --- Center Panel for Leaderboard ---
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBackground(new Color(135, 206, 235)); // Sky Blue
        
        JLabel leaderboardTitle = new JLabel("Leaderboard");
        leaderboardTitle.setFont(new Font("Times New Roman", Font.BOLD, 32));
        leaderboardTitle.setForeground(Color.BLACK);
        leaderboardTitle.setBorder(new EmptyBorder(0,0,10,0));
        leaderboardPanel.add(leaderboardTitle, BorderLayout.NORTH);

        String[] columnNames = {"Rank", "Player Name", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        
        // Style the table
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(135, 206, 250)); // Light sky blue grid
        table.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(173, 216, 230)); // Light blue selection
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(135, 206, 250)); // Sky blue header
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Populate table
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            Object[] row = {i + 1, user.getUserName(), user.getHighScore()};
            model.addRow(row);
        }
        
        // Highlight current player
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if ((int)table.getValueAt(row, 0) == finalRank) {
                    c.setForeground(Color.BLUE);
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    if(column == 1) {
                         ((JLabel)c).setText("YOU");
                    }
                } else {
                    c.setForeground(table.getForeground());
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                return c;
            }
        });
        
        leaderboardPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(leaderboardPanel, BorderLayout.CENTER);

        // --- Bottom Panel for Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(135, 206, 235)); // Sky Blue
        
        JButton playAgainButton = new JButton("Play Again");
        JButton returnToMenuButton = new JButton("Return to Menu");
        JButton exitButton = new JButton("Exit");

        // Style buttons
        Font buttonFont = new Font("Times New Roman", Font.BOLD, 20);
        for(JButton btn : new JButton[]{playAgainButton, returnToMenuButton, exitButton}) {
            btn.setFont(buttonFont);
            btn.setBackground(Color.YELLOW);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(new LineBorder(Color.BLACK, 2, true));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(70, 130, 180));
                    btn.setForeground(Color.WHITE);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(Color.YELLOW);
                    btn.setForeground(Color.BLACK);
                }
            });
        }

        // Add actions
        playAgainButton.addActionListener(e -> {
            Game.restartGame();
            dispose();
        });

        returnToMenuButton.addActionListener(e -> {
            new PostLoginMainMenu(currentUser);
            dispose();
        });

        exitButton.addActionListener(e -> System.exit(0));
        
        buttonPanel.add(playAgainButton);
        buttonPanel.add(returnToMenuButton);
        buttonPanel.add(exitButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
} 