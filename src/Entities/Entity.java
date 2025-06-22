package Entities;

import Map.Map;
import Media.EImage;
import java.awt.Point;

/**
 * An entity that has an active presence in the Map and can collide with other Entities.
 */
public class Entity extends Sprite {

    protected Point gridPosition; // Using Point to store (col, row)
    protected Map map;

    /**
     * Initializes an Entity object.
     *
     * @param col the starting column in the map grid
     * @param row the starting row in the map grid
     * @param en the EImage ENUM for the image
     * @param map the map object for navigation
     */
    public Entity(int col, int row, EImage en, Map map) {
        super(0, 0, en); // Initialize pixel coords at (0,0), will be updated
        this.map = map;
        this.gridPosition = new Point(col, row);
        updatePixelPosition();
        
        getInitialAnimationFrame();
    }
    
    /**
     * Updates the entity's pixel position based on its grid position.
     * This should be called whenever the grid position changes.
     */
    protected void updatePixelPosition() {
        Point pixelCoords = map.getPixelCoordinates(gridPosition.y, gridPosition.x);
        setX(pixelCoords.x);
        setY(pixelCoords.y);
    }
    
    /**
     * To be called when the Entity collides with another Entity.
     * @param e the Entity collided with
     */
    public void onCollision(Entity e) {
    
    }
    
    ///////////////////
    // Setters and getters below
    
    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(int col, int row) {
        this.gridPosition.setLocation(col, row);
        updatePixelPosition();
    }
}
