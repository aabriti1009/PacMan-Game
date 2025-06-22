package GUI;

import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SettingsPanel extends JDialog {

    private JSlider pacmanSpeedSlider;
    private JSpinner startingLivesSpinner;
    private JSlider ghostSpeedSlider;
    private JSpinner ghostCountSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    public SettingsPanel(JFrame parent) {
        super(parent, "Settings", true);
        initComponents();
        loadSettings();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Pac-Man Settings
        JPanel pacmanPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        pacmanPanel.setBorder(new TitledBorder("Pac-Man Settings"));
        pacmanSpeedSlider = new JSlider(1, 10, (int) Settings.get(EParam.pacman_speed));
        pacmanPanel.add(new JLabel("Speed:"));
        pacmanPanel.add(pacmanSpeedSlider);

        startingLivesSpinner = new JSpinner(new SpinnerNumberModel((int) Settings.get(EParam.pacman_starting_lives), 1, 10, 1));
        pacmanPanel.add(new JLabel("Starting Lives:"));
        pacmanPanel.add(startingLivesSpinner);
        mainPanel.add(pacmanPanel);

        // Ghost Settings
        JPanel ghostPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        ghostPanel.setBorder(new TitledBorder("Ghost Settings"));
        ghostSpeedSlider = new JSlider(1, 10, (int) Settings.get(EParam.ghost_speed));
        ghostPanel.add(new JLabel("Speed:"));
        ghostPanel.add(ghostSpeedSlider);

        ghostCountSpinner = new JSpinner(new SpinnerNumberModel((int) Settings.get(EParam.ghost_count), 1, 10, 1));
        ghostPanel.add(new JLabel("Ghost Count:"));
        ghostPanel.add(ghostCountSpinner);
        mainPanel.add(ghostPanel);
        
        add(mainPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveSettings());
        cancelButton.addActionListener(e -> setVisible(false));
    }

    private void loadSettings() {
        pacmanSpeedSlider.setValue((int) ((double) Settings.get(EParam.pacman_speed)));
        startingLivesSpinner.setValue(Settings.get(EParam.pacman_starting_lives));
        ghostSpeedSlider.setValue((int) ((double) Settings.get(EParam.ghost_speed)));
        ghostCountSpinner.setValue(Settings.get(EParam.ghost_count));
    }

    private void saveSettings() {
        Settings.set(EParam.pacman_speed, (double) pacmanSpeedSlider.getValue());
        Settings.set(EParam.pacman_starting_lives, startingLivesSpinner.getValue());
        Settings.set(EParam.ghost_speed, (double) ghostSpeedSlider.getValue());
        Settings.set(EParam.ghost_count, ghostCountSpinner.getValue());
        setVisible(false);
    }
} 