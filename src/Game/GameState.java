package Game;

import Entities.*;
import Map.Map;
import Settings.EParam;
import Settings.Settings;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Saves all the valuable stats and data about the game.
 */
public class GameState {
    private Pacman pacman;
    private ArrayList<Ghost> ghosts = new ArrayList<>();
    private HashMap<Point, Food> food = new HashMap<>();
    private Map map;
    
    private int round = 1;
    
    /**
     * Initializes the GameState object.
     */
    public GameState() {
        map = new Map();
    }

    public void createEntities() {
        // Defined starting positions
        Point pacmanStart = new Point(9, 15);
        List<Point> ghostStarts = new ArrayList<>();
        ghostStarts.add(new Point(9, 8));
        ghostStarts.add(new Point(8, 9));
        ghostStarts.add(new Point(9, 9));
        ghostStarts.add(new Point(10, 9));

        // Create Pacman
        pacman = new Pacman(pacmanStart.x, pacmanStart.y, map);
        Game.painter().registerSprite(pacman);
        
        // Create Ghosts
        for(int i = 0; i < (int)Settings.get(EParam.ghost_count); i++) {
            Point startPos = ghostStarts.get(i % ghostStarts.size());
            EGhostType type = EGhostType.values()[i % 4];
            Ghost g = new Ghost(startPos.x, startPos.y, map, type);
            ghosts.add(g);
            Game.painter().registerSprite(g);
        }
        
        // Create Food
        createFood();
    }

    public void createFood() {
        int[][] mapLayout = map.getMapLayout();
        for (int row = 0; row < mapLayout.length; row++) {
            for (int col = 0; col < mapLayout[row].length; col++) {
                if (mapLayout[row][col] == 0) { // If it's a path
                    Point position = new Point(col, row);
                    if (getFoodAt(position) == null && !position.equals(pacman.getGridPosition())) {
                         // Check for "power pellet" positions
                        boolean isLarge = (row == 3 && col == 1) || (row == 3 && col == 18) || (row == 15 && col == 1) || (row == 15 && col == 18);
                        Food f = isLarge ? new LargeFood(col, row, map) : new Food(col, row, map);
                        addFood(f);
                        Game.painter().registerSprite(f);
                    }
                }
            }
        }
    }
    
    /**
     * Resets all moving entities to their starting positions.
     */
    public void reshuffleEntityPositions() {
        pacman.setGridPosition(9, 15);
        
        List<Point> ghostStarts = new ArrayList<>();
        ghostStarts.add(new Point(9, 8));
        ghostStarts.add(new Point(8, 9));
        ghostStarts.add(new Point(9, 9));
        ghostStarts.add(new Point(10, 9));
        
        for (int i = 0; i < ghosts.size(); i++) {
            Point startPos = ghostStarts.get(i % ghostStarts.size());
            ghosts.get(i).setGridPosition(startPos.x, startPos.y);
            ghosts.get(i).resetEntity();
        }
        pacman.resetEntity();
    }
    
    //////////////
    // Getters and Setters below
    public Map getMap(){
        return map;
    }

    public Food getFoodAt(Point p) {
        return food.get(p);
    }
    
    public void addFood(Food f) {
        food.put(f.getGridPosition(), f);
    }

    public void removeFood(Food f) {
        if (f != null) {
            food.remove(f.getGridPosition());
            f.removeSprite();

            if(food.isEmpty()){
                Game.win();
            }
        }
    }
    
    public void removeGhost(Ghost g) {
        ghosts.remove(g);
    }
    public void removePacman(Pacman p) {
        pacman = null;
    }
    public void setRound(int round){
        this.round = round;
        Game.painter().updateRoundPanel(round);
    }
    
    public ArrayList<Ghost> getGhosts(){
        return ghosts;
    }
    
    public Pacman getPacman(){
        return pacman;
    }

    public int getRound() {return round; }
}
