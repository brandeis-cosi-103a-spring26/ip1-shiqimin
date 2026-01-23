package edu.brandeis.cosi103a.ip1.core;

/**
 * Represents the current state of the game.
 */
public class GameState {
    private Player currentPlayer;
    private Player otherPlayer;
    private Supply supply;
    private boolean gameOver;
    
    public GameState(Player player1, Player player2, Supply supply) {
        this.currentPlayer = player1;
        this.otherPlayer = player2;
        this.supply = supply;
        this.gameOver = false;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public Player getOtherPlayer() {
        return otherPlayer;
    }
    
    public Supply getSupply() {
        return supply;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
    /**
     * Switches the current player.
     */
    public void switchPlayer() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }
}
