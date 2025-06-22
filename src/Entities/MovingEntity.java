package Entities;

import AnimationEngine.AnimationManager;
import Map.Map;
import java.awt.Point;

/**
 * An entity that has the ability of navigating and moving through the Map.
 */
public class MovingEntity extends Entity {
    
    protected int speed;
    protected Direction currentDirection = Direction.NONE;
    protected Direction nextDirection = Direction.NONE;
    private int moveTimer = 0;
    
    /**
     * Initializes a MovingEntity object.
     * @param col the starting column in the map grid
     * @param row the starting row in the map grid
     * @param map the map object for navigation
     * @param speed the speed of the MovingEntity
     */
    public MovingEntity(int col, int row, Map map, int speed){
        super(col, row, null, map); // Image is set by subclasses
        this.speed = speed;
    }
    
    /**
     * Updates the entity's position based on its direction and speed.
     * This method should be called every game tick.
     */
    public void move() {
        moveTimer++;
        // The threshold is calculated so that a higher speed value means less waiting.
        // A speed of 10 would mean moving almost every tick, speed of 1 is very slow.
        int moveThreshold = Math.max(1, 10 - speed); 
        if (moveTimer < moveThreshold) {
            return; // Not time to move yet
        }
        moveTimer = 0; // Reset timer

        EntityManager.checkCollisions(this);

        Point nextCell = getNextCell(nextDirection);
        if (!map.isWall(nextCell.y, nextCell.x)) {
            currentDirection = nextDirection;
        } else {
            nextCell = getNextCell(currentDirection);
            if (map.isWall(nextCell.y, nextCell.x)) {
                currentDirection = Direction.NONE;
            }
            // If the user's next intended direction is a wall, stop.
            if (nextDirection != currentDirection) {
                currentDirection = Direction.NONE;
            }
        }

        if (currentDirection != Direction.NONE) {
            gridPosition = getNextCell(currentDirection);
            updatePixelPosition();
            updateAnimation();
        }
    }

    private Point getNextCell(Direction dir) {
        int newCol = gridPosition.x;
        int newRow = gridPosition.y;

        switch (dir) {
            case UP: newRow--; break;
            case DOWN: newRow++; break;
            case LEFT: newCol--; break;
            case RIGHT: newCol++; break;
            default: break;
        }
        return new Point(newCol, newRow);
    }

    public void setNextDirection(Direction dir) {
        this.nextDirection = dir;
    }

    private void updateAnimation() {
        // This can be overridden by subclasses to set specific animations
        // based on the currentDirection.
        // For now, we just reset it.
        setImage(AnimationManager.getFirstFrame(this));
    }
    
    ////////////////
    // Setters and Getters below
    
    public void resetEntity() {
        currentDirection = Direction.NONE;
        nextDirection = Direction.NONE;
    }
    
    public Direction getDirection(){
        return currentDirection;
    }
    
    public int getSpeed(){
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
