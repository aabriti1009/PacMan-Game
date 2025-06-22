package Game;

import AudioEngine.AudioEngine;
import AudioEngine.FunctionCallback;
import AudioEngine.PlaybackMode;
import Entities.Ghost;
import Entities.Pacman;
import Media.EAudio;
import Painter.Painter;

/**
 * Responsible for the game loop, entity updates, and game state transitions.
 */
public class GameThread implements Runnable {
    
    private volatile boolean running = true;
    private volatile boolean paused = false;
    
    @Override
    public void run() {
        try {
            performRoundIntro();
            
            long lastTime = System.nanoTime();
            double amountOfTicks = 30.0; // Target ticks per second
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;

            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    if (!paused) {
                        updateGame();
                    }
                    delta--;
                }
                
                // Yield to prevent busy-waiting
                Thread.yield();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Updates the state of all game entities.
     */
    private void updateGame() {
        Pacman pacman = Game.gamestate().getPacman();
        if (pacman != null) {
            pacman.update();
        }

        for (Ghost g : Game.gamestate().getGhosts()) {
            g.update();
        }
    }
    
    /**
     * Stops the game loop.
     */
    public void stopGame() {
        running = false;
    }
    
    /**
     * Pauses the game logic.
     */
    public void pauseGame() {
        paused = true;
    }

    /**
     * Resumes the game logic.
     */
    public void resumeGame() {
        paused = false;
    }

    /**
     * Handles what happens on a new round sequence.
     */
    public void performRoundIntro() {
        Painter.getRoundHUD().getBlinkAnimator().start();
        pauseGame();
        AudioEngine.play(EAudio.round_start, PlaybackMode.regular, new FunctionCallback() {
            @Override
            public void callback() {
                Painter.getRoundHUD().getBlinkAnimator().stop();
                resumeGame();
            }
        });
    }
    
    /**
     * handles what happens on a death sequence
     */
    public void performDeathSequence() {
        pauseGame();
        AudioEngine.play(EAudio.death_sound, PlaybackMode.regular, new FunctionCallback() {
            @Override
            public void callback() {
                Game.gamestate().reshuffleEntityPositions();
                performRoundIntro();
            }
        });
    }
}
