package Entities;

import AnimationEngine.BlinkAnimator;
import Game.Game;
import Map.EDirection;
import Map.Edge;
import Map.Node;
import Media.EImage;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * An enemy Ghost.
 */
public class Ghost extends MovingEntity {
    
    EGhostType type;
    private static boolean vulnerable = false;
    
    private LinkedList<EDirection> priorityQueue = new LinkedList<>();
    
    private boolean dead = false;
    
    /**
     * Initializes a Ghost object.
     * @param location the Edge where the ghost is located.
     */
    public Ghost(Edge location, EGhostType ghost) {
        super(null, location, EDirection.DOWN, (int)Settings.get(EParam.ghost_speed));
        type = ghost;
        
        // Chooses the respective image for the ghost
        resetGhostImg();
        
        getInitialAnimationFrame();
    }
    
    /**
     * Handles the complete removal of the Ghost from the game.
     */
    @Override
    public void removeSprite() {
        super.removeSprite();
        Game.gamestate().removeGhost(this);
    }
    
    /**
     * To be called when the Ghost collides with another Entity.
     * @param e the Entity the Ghost collided with
     */
    @Override
    public void onCollision(Entity e) {
    
    }
    
    @Override
    public void step() {
        if (!dead)
            super.step();
    }
    
    /**
     * Unqueues a turn from the queue and tries to perform it on the Node.
     * @param n The Node to perform the turn on.
     */
}