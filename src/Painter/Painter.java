package Painter;

import Entities.Sprite;
import Game.Game;
import Game.GameInputManager;
import GUI.LeaderboardWindow;
import GUI.PostLoginMainMenu;
import Map.Map;
import Media.*;
import Painter.HUD.LivesJPanel;
import Painter.HUD.RoundJLabel;
import Painter.HUD.ScoreJLabel;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Responsible of painting and updating all the on-screen graphic elements.
 */
public class Painter {
    
    private static Toolkit t= Toolkit.getDefaultToolkit();
    private ArrayList<Sprite> sprites = new ArrayList<>();
    
    private JLayeredPane gamepanel;
    private JFrame gameframe;
    
    private ScoreJLabel scoreHUD;
    private LivesJPanel livesHUD;
    private static RoundJLabel roundHUD;
    
    /**
     * Initializes the Painter object by creating and setting the game window.
     * @param g the input listener for the users inputs
     * @param size The dimensions of the parent window.
     */
    public Painter(GameInputManager g, Dimension size) {
        // Scales the Sprite images and graphic settings based on the screen size
        Scaler.setNewsize(size.height);
        Media.rescaleMedia(Scaler.getScale_factor());
        Settings.rescaleSettings(Scaler.getScale_factor());
        
        gameframe = new JFrame("SwingPacman");
        gameframe.setIconImage(Media.getImg(EImage.pacman_right_1));
        
        gameframe.setSize(size);
        gameframe.setResizable(false);
        gameframe.getContentPane().setBackground(new Color(135, 206, 235)); // Sky Blue
        gameframe.setUndecorated(true);
        
        gameframe.setLayout(null);
        gameframe.setVisible(true);
        
        gameframe.setLocationRelativeTo(null);
        
        gamepanel = new JLayeredPane();
        gamepanel.setBounds(0,0,size.width,size.height);
        
        gameframe.add(gamepanel);
    
        roundHUD = new RoundJLabel();
        roundHUD.setLocation(Scaler.scale(400), Scaler.scale(470));
        gamepanel.add(roundHUD, JLayeredPane.MODAL_LAYER);

        addExitButton(); // Add only the Exit button
        
        gamepanel.setFocusable(true);
        gamepanel.grabFocus();
        gamepanel.addKeyListener(g);
        
        gamepanel.revalidate();
        gamepanel.repaint();
        gameframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void addExitButton() {
        JButton exitButton = new JButton("Exit");
        Font buttonFont = new Font("Arial", Font.BOLD, 12);
        exitButton.setFont(buttonFont);
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.YELLOW);
        exitButton.setFocusable(false);
        int buttonWidth = 100;
        int buttonHeight = 30;
        int padding = 10;
        int xPos = gameframe.getWidth() - buttonWidth - padding;
        exitButton.setBounds(xPos, padding, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> {
            Game.gamethread().stopGame();
            gameframe.dispose();
            new PostLoginMainMenu(Game.getCurrentUser());
        });
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(new Color(70, 130, 180));
                exitButton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitButton.setBackground(Color.YELLOW);
                exitButton.setForeground(Color.BLACK);
            }
        });
        gamepanel.add(exitButton, JLayeredPane.MODAL_LAYER);
    }
    
    /**
     * Initializes a Sprite on the game screen by adding it to the game panel.
     *  to the game frame.
     * @param sprite the sprite to initialize
     */
    public void registerSprite(Sprite sprite) {
        if (!sprites.contains(sprite)) {
            sprites.add(sprite);
        }
        gamepanel.add(sprite, JLayeredPane.PALETTE_LAYER);
    }
    
    /**
     * Removes a sprite from the game panel.
     * @param sprite the sprite to unregister/delete
     */
    public void unregisterSprite(Sprite sprite) {
        if (sprites.contains(sprite)) {
            sprites.remove(sprite);
            gamepanel.remove(sprite);
            gamepanel.repaint();
        }
    }
    
    /**
     * Add the map to the game panel to be displayed
     * @param map the sprite to initialize
     */
    public void registerMap(Map map) {
        gamepanel.add(map, JLayeredPane.DEFAULT_LAYER);
        map.setSize(gamepanel.getSize());
    }
    
    /**
     * Returns whether the Sprite is registered in the game panel.
     * @param sprite the sprite to check for
     * @return whether the Sprite is registered in the game panel
     */
    public boolean isSpriteRegistered(Sprite sprite) {
        return sprites.contains(sprite);
    }
    
    ///////////////////
    // Getters and Setters below
    public void updateScoreLabel(long newScore) {
        // scoreHUD.updateScore(newScore);
    }
    public void updateLivesPanel(int newLives) { 
        // livesHUD.updateLives(newLives);
    }
    public void updateRoundPanel(int newRound) { roundHUD.updateRound(newRound);}
    public static RoundJLabel getRoundHUD() { return roundHUD; }
    
    public JLayeredPane getGamepanel(){
        return gamepanel;
    }
    
    public JFrame getGameframe(){
        return gameframe;
    }
    
    public void drawGameOver() {
        JLabel gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(0, 0, gamepanel.getWidth(), gamepanel.getHeight());
        gameOverLabel.setHorizontalAlignment(JLabel.CENTER);
        gamepanel.add(gameOverLabel, JLayeredPane.MODAL_LAYER);
        gamepanel.repaint();
    }
    
    public void drawYouWin() {
        JLabel youWinLabel = new JLabel("YOU WIN!");
        youWinLabel.setFont(new Font("Arial", Font.BOLD, 50));
        youWinLabel.setForeground(Color.GREEN);
        youWinLabel.setBounds(0, 0, gamepanel.getWidth(), gamepanel.getHeight());
        youWinLabel.setHorizontalAlignment(JLabel.CENTER);
        gamepanel.add(youWinLabel, JLayeredPane.MODAL_LAYER);
        gamepanel.repaint();
    }
}
