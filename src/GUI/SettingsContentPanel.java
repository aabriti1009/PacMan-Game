package GUI;

import Settings.EParam;
import Settings.Settings;
import AudioEngine.AudioEngine;
import Media.EAudio;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import pacmangame.model.User;
import java.awt.*;

public class SettingsContentPanel extends JPanel {
    private JSlider pacmanSpeedSlider, ghostSpeedSlider, musicVolumeSlider, sfxVolumeSlider;
    private JLabel pacmanSpeedValue, ghostSpeedValue, musicVolumeValue, sfxVolumeValue;
    private JCheckBox audioEnabledCheckbox;
    private JSpinner ghostCountSpinner, livesSpinner;
    private JButton saveButton;

    public SettingsContentPanel(User currentUser) {
        this();
    }

    public SettingsContentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(70, 130, 180)); // Soft blue from image
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title
        JLabel titleLabel = new JLabel("Game Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // Main settings panel
        JPanel settingsGridPanel = new JPanel(new GridBagLayout());
        settingsGridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        addGameSettings(settingsGridPanel, gbc);
        addAudioSettings(settingsGridPanel, gbc);

        add(settingsGridPanel, BorderLayout.CENTER);

        // Save Button
        saveButton = new JButton("Save Settings");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saveButton.setBackground(Color.YELLOW);
        saveButton.setForeground(Color.BLACK);
        saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        saveButton.addActionListener(e -> saveSettings());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadSettings();
    }
    
    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title, int gridy) {
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        sectionTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(20, 0, 10, 0);
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1; // Reset gridwidth
    }

    private void addGameSettings(JPanel panel, GridBagConstraints gbc) {
        addSectionTitle(panel, gbc, "Game Settings", 0);

        pacmanSpeedSlider = createSlider(1, 10);
        pacmanSpeedValue = new JLabel();
        addSettingRow(panel, gbc, "Pacman Speed:", pacmanSpeedSlider, pacmanSpeedValue, 1);

        ghostSpeedSlider = createSlider(1, 10);
        ghostSpeedValue = new JLabel();
        addSettingRow(panel, gbc, "Ghost Speed:", ghostSpeedSlider, ghostSpeedValue, 2);

        JLabel livesLabel = new JLabel("Starting Lives:");
        livesLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        livesLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        panel.add(livesLabel, gbc);

        livesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 3, 1));
        Dimension spinnerSize = new Dimension(60, 25);
        livesSpinner.setPreferredSize(spinnerSize);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(livesSpinner, gbc);
        gbc.gridwidth = 1;
        
        JLabel ghostCountLabel = new JLabel("Ghost Count:");
        ghostCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        ghostCountLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        panel.add(ghostCountLabel, gbc);

        ghostCountSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        ghostCountSpinner.setPreferredSize(spinnerSize);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(ghostCountSpinner, gbc);
        gbc.gridwidth = 1;
    }

    private void addAudioSettings(JPanel panel, GridBagConstraints gbc) {
        addSectionTitle(panel, gbc, "Audio Settings", 5);

        audioEnabledCheckbox = new JCheckBox("Enable Audio");
        audioEnabledCheckbox.setFont(new Font("Segoe UI", Font.BOLD, 18));
        audioEnabledCheckbox.setForeground(Color.WHITE);
        audioEnabledCheckbox.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(audioEnabledCheckbox, gbc);
        gbc.gridwidth = 1;
        
        musicVolumeSlider = createSlider(0, 100);
        musicVolumeValue = new JLabel();
        addSettingRow(panel, gbc, "Music Volume:", musicVolumeSlider, musicVolumeValue, 7);

        sfxVolumeSlider = createSlider(0, 100);
        sfxVolumeValue = new JLabel();
        addSettingRow(panel, gbc, "Sound Effects Volume:", sfxVolumeSlider, sfxVolumeValue, 8);
    }
    
    private void addSettingRow(JPanel panel, GridBagConstraints gbc, String label, JSlider slider, JLabel valueLabel, int gridy) {
        JLabel settingLabel = new JLabel(label);
        settingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        settingLabel.setForeground(Color.WHITE);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);

        slider.addChangeListener(e -> valueLabel.setText(String.valueOf(slider.getValue())));

        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 10);
        panel.add(settingLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(slider, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 10, 5, 0);
        panel.add(valueLabel, gbc);
    }
    
    private JSlider createSlider(int min, int max) {
        JSlider slider = new JSlider(min, max);
        slider.setOpaque(false);
        return slider;
    }

    private void loadSettings() {
        pacmanSpeedSlider.setValue(((Double) Settings.get(EParam.pacman_speed)).intValue());
        ghostSpeedSlider.setValue(((Double) Settings.get(EParam.ghost_speed)).intValue());
        livesSpinner.setValue(Settings.get(EParam.pacman_starting_lives));
        ghostCountSpinner.setValue(Settings.get(EParam.ghost_count));

        audioEnabledCheckbox.setSelected((Boolean) Settings.get(EParam.audio_enabled));
        musicVolumeSlider.setValue((Integer) Settings.get(EParam.music_volume));
        sfxVolumeSlider.setValue((Integer) Settings.get(EParam.sfx_volume));
    }

    private void saveSettings() {
        Settings.set(EParam.pacman_speed, (double) pacmanSpeedSlider.getValue());
        Settings.set(EParam.ghost_speed, (double) ghostSpeedSlider.getValue());
        Settings.set(EParam.pacman_starting_lives, livesSpinner.getValue());
        Settings.set(EParam.ghost_count, ghostCountSpinner.getValue());
        
        boolean audioEnabled = audioEnabledCheckbox.isSelected();
        Settings.set(EParam.audio_enabled, audioEnabled);
        Settings.set(EParam.music_volume, musicVolumeSlider.getValue());
        Settings.set(EParam.sfx_volume, sfxVolumeSlider.getValue());
        
        AudioEngine.setAudioEnabled(audioEnabled);
        
        float musicVolume = musicVolumeSlider.getValue() / 100.0f;
        AudioEngine.setVolume(EAudio.ost, musicVolume);
        
        float sfxVolume = sfxVolumeSlider.getValue() / 100.0f;
        AudioEngine.setVolume(EAudio.button_click, sfxVolume);
        AudioEngine.setVolume(EAudio.death_sound, sfxVolume);
        AudioEngine.setVolume(EAudio.ghost_ate, sfxVolume);
        AudioEngine.setVolume(EAudio.large_food, sfxVolume);
        AudioEngine.setVolume(EAudio.small_food, sfxVolume);
        AudioEngine.setVolume(EAudio.round_start, sfxVolume);
        
        JOptionPane.showMessageDialog(this, "Settings saved successfully!", "Settings Saved", JOptionPane.INFORMATION_MESSAGE);
    }
} 