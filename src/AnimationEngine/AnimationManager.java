package AnimationEngine;

import Entities.Ghost;
import Entities.Pacman;
import Entities.Sprite;
import Media.EImage;

import java.util.EnumMap;


/**
 * Handles all the animations of the game.
 */
public class AnimationManager {
    private static EnumMap<EImage,EImage> animation;
    
    /**
     * Creates all the initial animation mapping.
     */
    public static void initAnimations() {
        EnumMap<EImage,EImage> newAnimation =new EnumMap<>(EImage.class);
        
        ///////////
        
        newAnimation.put(EImage.pacman_left_1,EImage.pacman_left_2);
        newAnimation.put(EImage.pacman_left_2,EImage.pacman_left_3);
        newAnimation.put(EImage.pacman_left_3,EImage.pacman_left_4);
        newAnimation.put(EImage.pacman_left_4,EImage.pacman_left_1);
    
    
        newAnimation.put(EImage.pacman_right_1,EImage.pacman_right_2);
        newAnimation.put(EImage.pacman_right_2,EImage.pacman_right_3);
        newAnimation.put(EImage.pacman_right_3,EImage.pacman_right_4);
        newAnimation.put(EImage.pacman_right_4,EImage.pacman_right_1);
    
    
        newAnimation.put(EImage.pacman_down_1,EImage.pacman_down_2);
        newAnimation.put(EImage.pacman_down_2,EImage.pacman_down_3);
        newAnimation.put(EImage.pacman_down_3,EImage.pacman_down_4);
        newAnimation.put(EImage.pacman_down_4,EImage.pacman_down_1);
    
    
        newAnimation.put(EImage.pacman_up_1,EImage.pacman_up_2);
        newAnimation.put(EImage.pacman_up_2,EImage.pacman_up_3);
        newAnimation.put(EImage.pacman_up_3,EImage.pacman_up_4);
        newAnimation.put(EImage.pacman_up_4,EImage.pacman_up_1);
    
        ///////////
     
        
        newAnimation.put(EImage.ghost1_left,EImage.ghost1_left);
        newAnimation.put(EImage.ghost1_right,EImage.ghost1_right);
        newAnimation.put(EImage.ghost1_up,EImage.ghost1_up);
        newAnimation.put(EImage.ghost1_down,EImage.ghost1_down);
    
        newAnimation.put(EImage.ghost2_left,EImage.ghost2_left);
        newAnimation.put(EImage.ghost2_right,EImage.ghost2_right);
        newAnimation.put(EImage.ghost2_up,EImage.ghost2_up);
        newAnimation.put(EImage.ghost2_down,EImage.ghost2_down);
    
        newAnimation.put(EImage.ghost3_left,EImage.ghost3_left);
        newAnimation.put(EImage.ghost3_right,EImage.ghost3_right);
        newAnimation.put(EImage.ghost3_up,EImage.ghost3_up);
        newAnimation.put(EImage.ghost3_down,EImage.ghost3_down);
    
        newAnimation.put(EImage.ghost4_left,EImage.ghost4_left);
        newAnimation.put(EImage.ghost4_right,EImage.ghost4_right);
        newAnimation.put(EImage.ghost4_up,EImage.ghost4_up);
        newAnimation.put(EImage.ghost4_down,EImage.ghost4_down);
        
        
        animation = newAnimation;
    }
    
    /**
     * Returns the next frame of the specified ENUM image.
     * @param currFrame the current frame of the animation
     * @return the next frame image
     */
}   