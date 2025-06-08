package Painter.HUD;

import Media.EFont;
import Media.Media;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * The score JLabel that shows Pacmans score as part of the game's HUD system.
 */
public class ScoreJLabel extends JLabel {
    
    private long score = 0;
    
    public ScoreJLabel() {
        super();
        setOpaque(false);
        setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, (int)Settings.get(EParam.label_size)));
        setForeground((Color)Settings.get(EParam.label_color));
        updateScore(0);
    }
    
    /**
     * Sets the new score to be displayed.
     * @param score the new score to be displayed
     */
    public void updateScore(long score) {
        this.score = score;
        setText("SCORE     "+score);
        repaint();
    }
}package Painter.HUD;

import Media.EFont;
import Media.Media;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * The score JLabel that shows Pacman's score as part of the game's HUD system.
 */
public class ScoreJLabel extends JLabel {

    private long score = 0;

    public ScoreJLabel() {
        super();
        setOpaque(false);
        setFont(Media.getFont(EFont.regular).deriveFont(Font.PLAIN, (int) Settings.get(EParam.label_size)));
        setForeground((Color) Settings.get(EParam.label_color));
        updateScore(0);
    }

    /**
     * Updates the displayed score.
     * @param score the new score to display
     */
    public void updateScore(long score) {
        this.score = score;
        setText(String.format("SCORE     %d", score));
        repaint();
    }

    /**
     * Returns the current score.
     * @return the current score value
     */
    public long getScore() {
        return score;
    }
}
  