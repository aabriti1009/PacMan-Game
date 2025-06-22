package Entities;

import AnimationEngine.BlinkAnimator;
import Game.Game;
import Map.Map;
import Media.EImage;
import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * An enemy Ghost.
 */
public class Ghost extends MovingEntity {
    
    private enum GhostMode { CHASE, SCATTER, VULNERABLE }
    private GhostMode currentMode = GhostMode.SCATTER;

    private EGhostType type;
    private static boolean vulnerable = false;
    private boolean dead = false;
    private final Point homePosition;
    private final Point scatterTarget;
    private Timer modeTimer;

    /**
     * Initializes a Ghost object.
     */
    public Ghost(int col, int row, Map map, EGhostType ghost) {
        super(col, row, map, ((Double) Settings.get(EParam.ghost_speed)).intValue());
        this.type = ghost;
        this.homePosition = new Point(col, row);
        this.scatterTarget = getScatterTargetForType(ghost, map);
        setupModeTimer();
        updateAnimation();
    }

    private Point getScatterTargetForType(EGhostType type, Map map) {
        int rows = map.getMapLayout().length;
        int cols = map.getMapLayout()[0].length;
        switch (type) {
            case ghost1: return new Point(1, 1); // Top-left
            case ghost2: return new Point(cols - 2, 1); // Top-right
            case ghost3: return new Point(1, rows - 2); // Bottom-left
            case ghost4: return new Point(cols - 2, rows - 2); // Bottom-right
            default: return new Point(1, 1);
        }
    }

    private void setupModeTimer() {
        modeTimer = new Timer(7000, e -> { // Switch mode every 7 seconds
            if (currentMode == GhostMode.CHASE) {
                currentMode = GhostMode.SCATTER;
            } else {
                currentMode = GhostMode.CHASE;
            }
        });
        modeTimer.setRepeats(true);
        modeTimer.start();
    }

    /**
     * Called every game tick to update the Ghost's state.
     */
    public void update() {
        if (dead) return;

        if (vulnerable) {
            currentMode = GhostMode.VULNERABLE;
        } else if (currentMode == GhostMode.VULNERABLE) {
            currentMode = GhostMode.CHASE; // Revert after vulnerability ends
        }

        calculateNextDirection();
        move();
    }

    private void calculateNextDirection() {
        Point target;
        switch (currentMode) {
            case SCATTER:
                target = scatterTarget;
                break;
            case VULNERABLE: // Fleeing logic
                Pacman pacman = Game.gamestate().getPacman();
                if (pacman != null) {
                    Point pacmanPos = pacman.getGridPosition();
                    fleeFrom(pacmanPos);
                }
                return;
            case CHASE:
            default:
                pacman = Game.gamestate().getPacman();
                target = (pacman != null) ? pacman.getGridPosition() : homePosition;
                break;
        }
        chaseTarget(target);
    }
    
    private void chaseTarget(Point target) {
        double bestDistance = -1;
        Direction bestDirection = Direction.NONE;
        List<Direction> possibleDirections = getValidDirections();

        for (Direction dir : possibleDirections) {
            Point nextPos = getNextCellInDir(dir);
            double distance = target.distanceSq(nextPos);
            if (bestDistance == -1 || distance < bestDistance) {
                bestDistance = distance;
                bestDirection = dir;
            }
        }
        setNextDirection(bestDirection);
    }

    private void fleeFrom(Point target) {
        double bestDistance = -1;
        Direction bestDirection = Direction.NONE;
        List<Direction> possibleDirections = getValidDirections();

        for (Direction dir : possibleDirections) {
            Point nextPos = getNextCellInDir(dir);
            double distance = target.distanceSq(nextPos);
            if (bestDistance == -1 || distance > bestDistance) {
                bestDistance = distance;
                bestDirection = dir;
            }
        }
        setNextDirection(bestDirection);
    }

    private List<Direction> getValidDirections() {
        List<Direction> validDirections = new ArrayList<>();
        Direction oppositeDirection = getOppositeDirection(currentDirection);

        for (Direction dir : Direction.values()) {
            if (dir == Direction.NONE || dir == oppositeDirection) {
                continue;
            }
            Point nextPos = getNextCellInDir(dir);
            if (!map.isWall(nextPos.y, nextPos.x)) {
                validDirections.add(dir);
            }
        }
        
        if (validDirections.isEmpty()) {
            // If stuck, allow reversal
            Point nextPos = getNextCellInDir(oppositeDirection);
            if (oppositeDirection != Direction.NONE && !map.isWall(nextPos.y, nextPos.x)) {
                validDirections.add(oppositeDirection);
            }
        }
        
        return validDirections;
    }
    
    private Point getNextCellInDir(Direction dir) {
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
    
    private Direction getOppositeDirection(Direction dir) {
        switch (dir) {
            case UP: return Direction.DOWN;
            case DOWN: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
            default: return Direction.NONE;
        }
    }

    private void updateAnimation() {
        if (vulnerable) {
            setImage(EImage.ghost_vuln);
        } else {
            switch(type) {
                case ghost1: setImage(EImage.ghost1_left); break;
                case ghost2: setImage(EImage.ghost2_right); break;
                case ghost3: setImage(EImage.ghost3_right); break;
                default: setImage(EImage.ghost4_right); break;
            }
        }
        getInitialAnimationFrame();
    }
    
    @Override
    public void removeSprite() {
        super.removeSprite();
        Game.gamestate().removeGhost(this);
    }

    public void setVulnerable(boolean to) {
        vulnerable = to;
        if (vulnerable) {
            currentMode = GhostMode.VULNERABLE;
            setSpeed(((Double)Settings.get(EParam.ghost_vuln_speed)).intValue());
        } else {
            currentMode = GhostMode.CHASE;
            setSpeed(((Double)Settings.get(EParam.ghost_speed)).intValue());
        }
        updateAnimation();
    }

    public void die() {
        dead = true;
        this.gridPosition.setLocation(homePosition);
        updatePixelPosition();
        
        BlinkAnimator b = new BlinkAnimator(this, 100, true);
        b.start();
        Timer t = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dead = false;
                b.stop();
                updateAnimation();
            }
        });
        t.setRepeats(false);
        t.start();
    }
    
    public EGhostType getType(){
        return type;
    }
    
    public static boolean isVulnerable(){
        return vulnerable;
    }
}