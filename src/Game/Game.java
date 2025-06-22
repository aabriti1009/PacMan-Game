package Game;


import Media.Media;
import Painter.Painter;
import pacmangame.dao.UserDAO;
import pacmangame.model.User;
import java.awt.Dimension;

/**
 * The main class that starts the game.
 */
public class Game {
    private static GameState gamestate;
    private static Painter painter;
    
    private static GameThread gamethread;
    private static GameInputManager gameinput;
    private static User currentUser;
    
    /**
     * Starts the Game.
     * @param user The user playing the game.
     * @param size The dimensions for the game window.
     */
    public Game(User user, Dimension size) {
        System.setProperty("sun.java2d.opengl", "true");
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("sun.java2d.uiScale", "1");
        
        currentUser = user;
        gameinput = new GameInputManager();
        painter = new Painter(gameinput, size);
    
        gamestate = new GameState();
        gamestate.getMap().calculateDimensions(painter.getGamepanel().getWidth(), painter.getGamepanel().getHeight());
        gamestate.createEntities(); // Create entities after map dimensions are set
        painter.registerMap(gamestate.getMap());
    
        gamethread = new GameThread();
        Thread t = new Thread(gamethread);
        t.start();
    }
    
    public static void main(String[] args){
        // This main method is for testing purposes only.
        // In a real scenario, the Game should be started from the UI with a logged-in user.
        new Game(new User("testuser", "test@example.com", "password"), new Dimension(800, 600));
    }
    
    /**
     * Handles the gameover phase.
     */
    public static void gameOver() {
        gamethread.stopGame();
        updateUserStats();
        painter.getGameframe().dispose();
        new GUI.GameOverWindow(currentUser, gamestate.getPacman().getScore());
    }
    
    /**
     * Handles the win phase.
     */
    public static void win() {
        if (gamestate.getRound() >= 3) {
            gamethread.stopGame();
            updateUserStats();
            painter.getGameframe().dispose();
            new GUI.GameOverWindow(currentUser, gamestate.getPacman().getScore());
        } else {
            gamethread.pauseGame();
            gamestate.setRound(gamestate.getRound() + 1);
            gamestate.reshuffleEntityPositions();
            gamestate.createFood();
            gamethread.performRoundIntro();
        }
    }

    private static void updateUserStats() {
        if (currentUser != null && gamestate != null && gamestate.getPacman() != null) {
            long currentScore = gamestate.getPacman().getScore();
            currentUser.incrementGamesPlayed();
            currentUser.setTotalScore(currentUser.getTotalScore() + currentScore);
            if (currentScore > currentUser.getHighScore()) {
                currentUser.setHighScore((int) currentScore);
            }
            
            UserDAO userDAO = new UserDAO();
            userDAO.updateUserStats(currentUser);
        }
    }
    
    /**
     * Restarts the game.
     */
    public static void restartGame() {
        if (gamethread != null) {
            gamethread.stopGame();
        }
        if (painter != null) {
            painter.getGameframe().dispose();
        }
        // Start a new game with the same user and window size
        new Game(currentUser, painter.getGameframe().getSize());
    }
    
    //////////////////////////
    // Getters and Setters below
    
    public static Painter painter() { return painter; }
    public static GameState gamestate() { return gamestate; }
    public static GameThread gamethread() { return gamethread; }
    public static User getCurrentUser() { return currentUser; }
}


