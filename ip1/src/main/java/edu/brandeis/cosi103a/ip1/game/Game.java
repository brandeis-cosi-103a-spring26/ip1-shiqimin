package edu.brandeis.cosi103a.ip1.game;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.Deck;
import edu.brandeis.cosi103a.ip1.core.GameState;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.core.Supply;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;
import edu.brandeis.cosi103a.ip1.features.phases.BuyPhase;
import edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Main game orchestration class.
 * Coordinates game flow, players, and phases.
 */
public class Game {
    private GameState gameState;
    private BuyPhase buyPhase;
    private CleanupPhase cleanupPhase;
    private Random random;
    private Player player1;
    private Player player2;
    
    public Game() {
        this.buyPhase = new BuyPhase();
        this.cleanupPhase = new CleanupPhase();
        this.random = new Random();
    }
    
    /**
     * Initializes and starts a new game.
     */
    public void start() {
        // Initialize supply
        Supply supply = new Supply();
        
        // Create players
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        
        // Initialize starter decks: 7 Bitcoins + 3 Methods for each player
        initializeStarterDeck(player1, supply);
        initializeStarterDeck(player2, supply);
        
        // Create game state
        gameState = new GameState(player1, player2, supply);
        
        // Choose starting player randomly
        if (random.nextBoolean()) {
            gameState.switchPlayer();
        }
        
        System.out.println("=== Game Start ===");
        System.out.println("Starting player: " + gameState.getCurrentPlayer().getName());
        System.out.println();
        
        // Main game loop
        playGame();
    }
    
    /**
     * Initializes a player's starter deck with 7 Bitcoins and 3 Methods.
     * @param player the player to initialize
     * @param supply the supply to purchase cards from
     */
    private void initializeStarterDeck(Player player, Supply supply) {
        List<Card> starterCards = new ArrayList<>();
        
        // Add 7 Bitcoins
        for (int i = 0; i < 7; i++) {
            Card bitcoin = supply.purchaseCard(Bitcoin.class);
            if (bitcoin != null) {
                starterCards.add(bitcoin);
            }
        }
        
        // Add 3 Methods
        for (int i = 0; i < 3; i++) {
            Card method = supply.purchaseCard(Method.class);
            if (method != null) {
                starterCards.add(method);
            }
        }
        
        // Shuffle the starter cards
        Collections.shuffle(starterCards, random);
        
        // Create deck with shuffled cards
        Deck deck = new Deck(starterCards);
        player.setDeck(deck);
        
        // Deal 5 cards to hand
        List<Card> hand = deck.drawCards(5);
        player.setHand(hand);
    }
    
    /**
     * Main game loop.
     */
    private void playGame() {
        int turn = 0;
        
        while (!gameState.isGameOver()) {
            turn++;
            Player currentPlayer = gameState.getCurrentPlayer();
            
            System.out.println("--- Turn " + turn + ": " + currentPlayer.getName() + " ---");
            
            // Buy phase
            buyPhase.execute(currentPlayer, gameState.getSupply());
            
            // Cleanup phase
            cleanupPhase.execute(currentPlayer);
            
            // Check if game should end (all Framework cards purchased)
            if (gameState.getSupply().isGameOver()) {
                gameState.setGameOver(true);
                break;
            }
            
            System.out.println("  Framework supply: " + gameState.getSupply().getCount(Framework.class) + " remaining.");
            System.out.println();
            
            // Switch players
            gameState.switchPlayer();
        }
        
        // Determine winner
        determineWinner();
    }
    
    /**
     * Determines and displays the winner.
     */
    private void determineWinner() {
        // Use stored player references
        int player1AP = this.player1.getTotalAutomationPoints();
        int player2AP = this.player2.getTotalAutomationPoints();
        
        System.out.println("\n=== Game Over! ===");
        System.out.println(player1.getName() + ": " + player1AP + " AP");
        System.out.println(player2.getName() + ": " + player2AP + " AP");
        
        if (player1AP > player2AP) {
            System.out.println("\n" + player1.getName() + " wins!");
        } else if (player2AP > player1AP) {
            System.out.println("\n" + player2.getName() + " wins!");
        } else {
            System.out.println("\nIt's a tie!");
        }
    }
    
    /**
     * Gets the game state (for testing).
     * @return the game state
     */
    public GameState getGameState() {
        return gameState;
    }
}
