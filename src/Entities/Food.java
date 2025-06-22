package Entities;

import Game.Game;
import Map.Map;
import Media.EImage;
import Settings.EParam;
import Settings.Settings;

/**
 * A Food entity that can be eaten by the Pacman.
 */
public class Food extends Entity{
    private int points;
    
    /**
     * Initializes a Food object.
     * @param col the starting column in the map grid
     * @param row the starting row in the map grid
     * @param map the map object for navigation
     */
    public Food(int col, int row, Map map){
        super(col, row, EImage.food, map);
        this.points = (int)Settings.get(EParam.food_points);
    }
    
    // Overloaded constructor for subclasses like LargeFood
    protected Food(int col, int row, EImage image, Map map, int points) {
        super(col, row, image, map);
        this.points = points;
    }

    /**
     * Handles the consequences when this object collides with another Entity
     * @param e the Entity collided with
     */
    @Override
    public void onCollision(Entity e){
        if(e instanceof Pacman){
            Game.gamestate().removeFood(this);
        }
    }
    
    ///////////////
    // Getters and setters below
    public int getPoints() {
        return points;
    }
}
