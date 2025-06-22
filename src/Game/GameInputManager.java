package Game;

import Entities.Direction;
import Settings.EParam;
import Settings.Settings;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Handles the Keyboard input from the user.
 */
public class GameInputManager implements KeyListener {
    
    private HashMap<Integer, Direction> keyDirectionMapping = new HashMap<>();
    
    /**
     * Initializes the GameInputManager object.
     */
    public GameInputManager() {
        keyDirectionMapping.put((int)Settings.get(EParam.KEY_turn_down), Direction.DOWN);
        keyDirectionMapping.put((int)Settings.get(EParam.KEY_turn_up), Direction.UP);
        keyDirectionMapping.put((int)Settings.get(EParam.KEY_turn_left), Direction.LEFT);
        keyDirectionMapping.put((int)Settings.get(EParam.KEY_turn_right), Direction.RIGHT);
    }
    
    public void keyTyped(KeyEvent e) {
    
    }
    
    /**
     * Handles the keyboard key presses to control the Pacman.
     * @param e the key event
     */
    public void keyPressed(KeyEvent e) {
        if (keyDirectionMapping.containsKey(e.getKeyCode())) {
            Game.gamestate().getPacman().setNextDirection(keyDirectionMapping.get(e.getKeyCode()));
            return;
        }
        
        // Example pause/resume, can be mapped to a single key
        if (e.getKeyChar() == 'p') {
            Game.gamethread().pauseGame();
        }
        if (e.getKeyChar() == 'u') {
            Game.gamethread().resumeGame();
        }
    }
    
    public void keyReleased(KeyEvent e) {
    
    }
}
