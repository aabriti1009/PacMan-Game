package Entities;

import Game.Game;
import Game.GameState;

/**
 * Handles and manages most of the Entites of the game.
 */
public class EntityManager {
    
    /**
     * Checks for collisions based on entity grid positions.
     * @param e the entity to check for collisions
     */
    public static void checkCollisions(Entity e) {
        GameState gs = Game.gamestate();
        if (e instanceof Pacman) {
            Pacman p = (Pacman) e;
            // Check collision with Ghosts
            for (Ghost g : gs.getGhosts()) {
                if (areColliding(p, g)) {
                    p.onCollision(g);
                    g.onCollision(p);
                }
            }
            // Check collision with Food
            Food foodOnCell = gs.getFoodAt(p.getGridPosition());
            if (foodOnCell != null) {
                p.onCollision(foodOnCell);
                foodOnCell.onCollision(p);
            }

        }
    }
    
    /**
     * Returns whether two Entities are on the same grid cell.
     * @param a the first Entity
     * @param b the second Entity
     * @return whether two Entities are on the same grid cell.
     */
    public static boolean areColliding(Entity a, Entity b) {
        if (a == null || b == null) return false;
        
        return a.getGridPosition().equals(b.getGridPosition());
    }
    
    /**
     * Makes all the Ghosts of the game vulnerable or not.
     * @param toVulnerable the new vulnerable value of the Ghosts
     */
    public static void makeGhostVulnerable(boolean toVulnerable) {
        for (Ghost g : Game.gamestate().getGhosts()) {
            g.setVulnerable(toVulnerable);
        }
    }
}