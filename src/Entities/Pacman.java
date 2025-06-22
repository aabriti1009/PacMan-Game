package Entities;

import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Game.Game;
import Map.Map;
import Media.EAudio;
import Media.EImage;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;

/**
 * The main character of the game, Pacman.
 */
public class Pacman extends MovingEntity{
    
    private int lives;
    private long score = 0;
    private int streak = 1;
    
    /**
     * Initializes a Pacman object.
     * @param col the starting column in the map grid
     * @param row the starting row in the map grid
     * @param map the map object for navigation
     */
    public Pacman(int col, int row, Map map){
        super(col, row, map, ((Double) Settings.get(EParam.pacman_speed)).intValue());
        setLives((int)Settings.get(EParam.pacman_starting_lives));
        setImage(EImage.pacman_right_1); // Default starting image
        updateAnimation();
    }
    
    /**
     * Called every game tick to update Pacman's state.
     */
    public void update() {
        move();
    }
    
    /**
     * Handles the complete removal of the Pacman from the game.
     */
    @Override
    public void removeSprite() {
        super.removeSprite();
        Game.gamestate().removePacman(this);
    }
    
    /**
     * To be called when the Pacman collides with another Entity.
     * @param e the Entity the Pacman collided with
     */
    @Override
    public void onCollision(Entity e) {
        if (e instanceof Ghost) {
            Ghost ghost = (Ghost) e;
            if (!ghost.isVulnerable()) {
                handleDeath();
            } else {
                AudioEngine.play(EAudio.ghost_ate, PlaybackMode.regular, null);
                ghost.die();
                increaseScore((int)Settings.get(EParam.ghost_vuln_val));
            }
        } else if(e instanceof Food) {
            increaseScore(((Food) e).getPoints());
            if (e instanceof LargeFood) {
                // The vulnerability logic is handled in LargeFood's onCollision
            } else {
                AudioEngine.play(EAudio.small_food, PlaybackMode.regular, null);
            }
            Game.gamestate().removeFood((Food)e); // Ensure food is removed
        }
    }
    
    private void handleDeath() {
        if (Game.gamestate().getRound() < 2) {
            Game.gamestate().setRound(2);
            Game.gamethread().performDeathSequence();
        } else {
            Game.gameOver();
        }
    }
    
    private void updateAnimation() {
        switch (currentDirection) {
            case UP:
                setImage(EImage.pacman_up_1);
                break;
            case DOWN:
                setImage(EImage.pacman_down_1);
                break;
            case LEFT:
                setImage(EImage.pacman_left_1);
                break;
            case RIGHT:
                setImage(EImage.pacman_right_1);
                break;
            case NONE:
                // Keep last direction's image when stopped
                break;
        }
        getInitialAnimationFrame();
    }
    
    @Override
    public void setNextDirection(Direction dir) {
        // Allow instant reversal of direction
        if (dir == Direction.UP && currentDirection == Direction.DOWN ||
            dir == Direction.DOWN && currentDirection == Direction.UP ||
            dir == Direction.LEFT && currentDirection == Direction.RIGHT ||
            dir == Direction.RIGHT && currentDirection == Direction.LEFT) {
            currentDirection = dir;
        }
        this.nextDirection = dir;
    }
    
    //////////////////
    // Getters and setters below
    
    public long getScore(){
        return score;
    }
    
    public void setLives(int lives){
        this.lives=lives;
        Game.painter().updateLivesPanel(lives);
    }
    
    public void increaseScore(long score) {
        setScore(this.score + score);
    }
    
    public void setScore(long score){
        this.score = score;
        Game.painter().updateScoreLabel(score);
    }
}
