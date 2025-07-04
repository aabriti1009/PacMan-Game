pratikshya
//package Painter;
//
//import Entities.Sprite;
//import Game.GameInputManager;
//import Map.Map;
//import Media.*;
//import Painter.HUD.LivesJPanel;
//import Painter.HUD.RoundJLabel;
//import Painter.HUD.ScoreJLabel;
//import Settings.EParam;
//import Settings.Settings;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//
///**
// * Responsible of painting and updating all the on-screen graphic elements.
// */
//public class Painter {
//    
//    private static Toolkit t= Toolkit.getDefaultToolkit();
//    private ArrayList<Sprite> sprites = new ArrayList<>();
//    
//    private JLayeredPane gamepanel;
//    private JFrame gameframe;
//    
//    private ScoreJLabel scoreHUD;
//    private LivesJPanel livesHUD;
//    private static RoundJLabel roundHUD;
//    
//    /**
//     * Initializes the Painter object by creating and setting the game window.
//     * @param g the input listener for the users inputs
//     */
//    public Painter(GameInputManager g) {
//        // Set window size to 1000x600
//        int width = 1000;
//        int height = 600;
//    
//        System.out.println("size: " + width + "x" + height);
//        System.out.println("DPI: " + t.getScreenResolution());
//    
//    
//        // Scales the Sprite images and graphic settings based on the screen size
//        Scaler.setNewsize(Math.min(width, height));
//        Scaler.setNewsize(Math.min(width, height)-Scaler.scale(100));
//        Media.rescaleMedia(Scaler.getScale_factor());
//        Settings.rescaleSettings(Scaler.getScale_factor());
//        
//        gameframe = new JFrame("SwingPacman");
//        gameframe.setIconImage(Media.getImg(EImage.pacman_right_1));
//        
//        gameframe.setSize(width, height);
//        gameframe.setLocationRelativeTo(null); // Center the window on screen
//        gameframe.setResizable(false);
//        gameframe.getContentPane().setBackground((Color)Settings.get(EParam.background_color));
//        gameframe.setUndecorated(true);
//        
//        gameframe.setLayout(null);
//        gameframe.setVisible(true);
//        
//        gamepanel = new JLayeredPane();
//        gamepanel.setBounds(0,0,width,height);
//        
//        
//        gameframe.add(gamepanel);
//    
//        scoreHUD= new ScoreJLabel();
//        scoreHUD.setBounds(0,height-Scaler.scale(100), Scaler.scale(500), (int)Settings.get(EParam.label_size));
//        
//        livesHUD = new LivesJPanel((int)Settings.get(EParam.pacman_starting_lives));
//        livesHUD.setBounds(width-Scaler.scale(600),height-Scaler.scale(100), Scaler.scale(600), Scaler.scale(100));
//        
//        roundHUD = new RoundJLabel();
//        roundHUD.setLocation(Scaler.scale(400), Scaler.scale(470));
//
//        gameframe.add(scoreHUD);
//        gameframe.add(livesHUD);
//        gameframe.add(roundHUD);
//        
//        gamepanel.setFocusable(true);
//        gamepanel.grabFocus();
//        gamepanel.addKeyListener(g);
//        
//        gamepanel.revalidate();
//        gamepanel.repaint();
//        gameframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//    
//    /**
//     * Initializes a Sprite on the game screen by adding it to the game panel.
//     *  to the game frame.
//     * @param sprite the sprite to initialize
//     */
//    public void registerSprite(Sprite sprite) {
//        if ( !sprites.contains(sprite)) {
//            sprites.add(sprite);
//            gamepanel.add(sprite,1);
//        }
//    }
//    
//    /**
//     * Removes a sprite from the game panel.
//     * @param sprite the sprite to unregister/delete
//     */
//    public void unregisterSprite(Sprite sprite) {
//        if ( sprites.contains(sprite)) {
//            sprites.remove(sprite);
//            gamepanel.remove(sprite);
//        }
//    }
//    
//    /**
//     * Add the map to the game panel to be displayed
//     * @param map the sprite to initialize
//     */
//    public void registerMap(Map map) {
//        gamepanel.add(map,-1);
//        gamepanel.revalidate();
//        gamepanel.repaint();
//    }
//    
//    /**
//     * Returns whether the Sprite is registered in the game panel.
//     * @param sprite the sprite to check for
//     * @return whether the Sprite is registered in the game panel
//     */
//    public boolean isSpriteRegistered(Sprite sprite) {
//        return sprites.contains(sprite);
//    }
//    
//    ///////////////////
//    // Getters and Setters below
//    public void updateScoreLabel(long newScore) {
//        scoreHUD.updateScore(newScore);
//    }
//    public void updateLivesPanel(int newLives) { livesHUD.updateLives(newLives);}
//    public void updateRoundPanel(int newRound) { roundHUD.updateRound(newRound);}
//    public static RoundJLabel getRoundHUD() { return roundHUD; }
//    
//    public JLayeredPane getGamepanel(){
//        return gamepanel;
//    }
//    
//    public JFrame getGameframe(){
//        return gameframe;
//    }
//}
=======
 main
package Painter;

import Entities.Sprite;
import Game.GameInputManager;
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
     */
    public Painter(GameInputManager g) {
 pratikshya
        // Set window size to 1000x600
        int width = 1000;
        int height = 600;
    
        System.out.println("size: " + width + "x" + height);
=======
        Dimension screenSize = t.getScreenSize();
        int size = Math.min(screenSize.height, screenSize.width);
    
        System.out.println("size: " + size);
 main
        System.out.println("DPI: " + t.getScreenResolution());
    
    
        // Scales the Sprite images and graphic settings based on the screen size
pratikshya
        Scaler.setNewsize(width, height);
=======
        Scaler.setNewsize(size);
        Scaler.setNewsize(size-Scaler.scale(100));
 main
        Media.rescaleMedia(Scaler.getScale_factor());
        Settings.rescaleSettings(Scaler.getScale_factor());
        
        gameframe = new JFrame("SwingPacman");
        gameframe.setIconImage(Media.getImg(EImage.pacman_right_1));
        
 pratikshya
        gameframe.setSize(width, height);
        gameframe.setLocationRelativeTo(null); // Center the window on screen
=======
        gameframe.setSize(Scaler.getNewSize(), size);
 main
        gameframe.setResizable(false);
        gameframe.getContentPane().setBackground((Color)Settings.get(EParam.background_color));
        gameframe.setUndecorated(true);
        
        gameframe.setLayout(null);
        gameframe.setVisible(true);
        
        gamepanel = new JLayeredPane();
 pratikshya
        gamepanel.setBounds(0,0,width,height);
=======
        gamepanel.setBounds(0,0,size,size);
 main
        
        
        gameframe.add(gamepanel);
    
        scoreHUD= new ScoreJLabel();
 pratikshya
        scoreHUD.setBounds(0,height-Scaler.scale(100), Scaler.scale(500), (int)Settings.get(EParam.label_size));
        
        livesHUD = new LivesJPanel((int)Settings.get(EParam.pacman_starting_lives));
        livesHUD.setBounds(width-Scaler.scale(600),height-Scaler.scale(100), Scaler.scale(600), Scaler.scale(100));
=======
        scoreHUD.setBounds(0,size-Scaler.scale(100), Scaler.scale(500), (int)Settings.get(EParam.label_size));
        
        livesHUD = new LivesJPanel((int)Settings.get(EParam.pacman_starting_lives));
        livesHUD.setBounds(size-Scaler.scale(600),size-Scaler.scale(100), Scaler.scale(600), Scaler.scale(100));
 main
        
        roundHUD = new RoundJLabel();
        roundHUD.setLocation(Scaler.scale(400), Scaler.scale(470));

        gameframe.add(scoreHUD);
        gameframe.add(livesHUD);
        gameframe.add(roundHUD);
        
        gamepanel.setFocusable(true);
        gamepanel.grabFocus();
        gamepanel.addKeyListener(g);
        
        gamepanel.revalidate();
        gamepanel.repaint();
        gameframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Initializes a Sprite on the game screen by adding it to the game panel.
     *  to the game frame.
     * @param sprite the sprite to initialize
     */
    public void registerSprite(Sprite sprite) {
        if ( !sprites.contains(sprite)) {
            sprites.add(sprite);
            gamepanel.add(sprite,1);
        }
    }
    
    /**
     * Removes a sprite from the game panel.
     * @param sprite the sprite to unregister/delete
     */
    public void unregisterSprite(Sprite sprite) {
        if ( sprites.contains(sprite)) {
            sprites.remove(sprite);
            gamepanel.remove(sprite);
        }
    }
    
    /**
     * Add the map to the game panel to be displayed
     * @param map the sprite to initialize
     */
    public void registerMap(Map map) {
        gamepanel.add(map,-1);
        gamepanel.revalidate();
        gamepanel.repaint();
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
        scoreHUD.updateScore(newScore);
    }
    public void updateLivesPanel(int newLives) { livesHUD.updateLives(newLives);}
    public void updateRoundPanel(int newRound) { roundHUD.updateRound(newRound);}
    public static RoundJLabel getRoundHUD() { return roundHUD; }
    
    public JLayeredPane getGamepanel(){
        return gamepanel;
    }
    
    public JFrame getGameframe(){
        return gameframe;
    }
}
