package edu.brandeis.cosi103a.ip1.game;

/**
 * Game orchestrator that coordinates game initialization and execution.
 * This class can be extended to handle more complex game setup.
 */
public class GameOrchestrator {
    
    /**
     * Creates and starts a new game.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
