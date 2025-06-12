package GUI;

import AnimationEngine.BlinkAnimator;
import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Media.*;
import Painter.Scaler;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;


/**
 * The Mainmenu of the game
 */
public class MainMenu extends JFrame {
    
    private StartGamePanel startpanel;
    
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        
        new MainMenu();
    }
    
    /**
     * Initializes the MainMenu frame.
     */
    public MainMenu() {
        super();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int size = Math.min(screenSize.height, screenSize.width);
        Scaler.setNewsize(size);
        
        setTitle("SwingPacman - MENU");
        setIconImage(Media.getImg(EImage.pacman_right_1));
        
        initComponents();
        adjustSizes();
        
        AudioEngine.play(EAudio.ost, PlaybackMode.loop, null);
    
        startpanel = new StartGamePanel(this.getWidth(),  this);
        
        github_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BlinkAnimator blinkAnimator = new BlinkAnimator(github_button,80,true);
                blinkAnimator.start();
                AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        blinkAnimator.stop();
                        try{
                            openWebpage(new URI("https://github.com/AlbertCerfeda/SwingPacman"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        exit_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                    BlinkAnimator blinkAnimator = new BlinkAnimator(exit_button,80,true);
                    blinkAnimator.start();
                    AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                        @Override
                        public void callback(){
                            blinkAnimator.stop();
                            System.exit(0);
                        }
                    });
                }
            
        });
        play_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                BlinkAnimator blinkAnimator = new BlinkAnimator(play_button,80,true);
                blinkAnimator.start();
                AudioEngine.play(EAudio.button_click, PlaybackMode.regular, new FunctionCallback() {
                    @Override
                    public void callback() {
                        blinkAnimator.stop();
                        showNewGamepanel();
                    }
                });
            }
        });
    
        setUndecorated(true);
        
        add(startpanel,0);
        startpanel.setLocation(0,0);
        startpanel.setVisible(false);
    
        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("res/gif/gameplay.gif");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(
                    buffer.toByteArray()
            ).getImage().getScaledInstance(getWidth(),getHeight(),Image.SCALE_DEFAULT));
            
            JLabel label = new JLabel(imageIcon);
            add(label);
            label.setBounds(0,0,getWidth(), getHeight());
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
    }
    
    /**
     * Opens a webpage in the Systems default web browser.
     * @param uri
     */
}