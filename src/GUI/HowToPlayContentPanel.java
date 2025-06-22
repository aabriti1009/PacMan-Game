package GUI;

import javax.swing.*;
import java.awt.*;

public class HowToPlayContentPanel extends JPanel {
    public HowToPlayContentPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(70, 130, 180));

        // Title
        JLabel titleLabel = new JLabel("How to Play", new ImageIcon(getClass().getResource("/imagepicker/img/play-button (1).png")), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        String howToPlayInfo = "<html>"
            + "<body style='padding: 20px; color: white;'>"
            + "<h2>CONTROLS:</h2>"
            + "<ul>"
            + "<li>Use Arrow Keys to move PacMan</li>"
            + "<li>Collect all dots to complete the level</li>"
            + "<li>Avoid the ghosts (unless they're blue)</li>"
            + "<li>Eat power pellets to make ghosts vulnerable</li>"
            + "</ul>"
            + "<h2>OBJECTIVE:</h2>"
            + "<ul>"
            + "<li>Clear all dots from the maze</li>"
            + "<li>Survive as long as possible</li>"
            + "<li>Achieve the highest score</li>"
            + "</ul>"
            + "<h2>TIPS:</h2>"
            + "<ul>"
            + "<li>Plan your route carefully</li>"
            + "<li>Use power pellets strategically</li>"
            + "<li>Watch ghost movement patterns</li>"
            + "</ul>"
            + "</body>"
            + "</html>";
        JLabel label = new JLabel(howToPlayInfo);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setVerticalAlignment(SwingConstants.TOP);
        add(label, BorderLayout.CENTER);
    }
} 