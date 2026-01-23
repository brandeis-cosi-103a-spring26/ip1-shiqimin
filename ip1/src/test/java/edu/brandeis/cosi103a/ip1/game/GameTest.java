package edu.brandeis.cosi103a.ip1.game;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.core.Supply;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;

/**
 * Unit tests for Game class.
 */
public class GameTest {
    private Game game;
    
    @Before
    public void setUp() {
        game = new Game();
    }
    
    @Test
    public void testGameInitialization() {
        // Test that game can be created
        assertNotNull(game);
    }
    
    @Test
    public void testStarterDeckSetup() {
        Supply supply = new Supply();
        Player player = new Player("Test Player");
        
        // Manually initialize starter deck (simulating Game.initializeStarterDeck)
        java.util.List<Card> starterCards = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Card bitcoin = supply.purchaseCard(Bitcoin.class);
            starterCards.add(bitcoin);
        }
        for (int i = 0; i < 3; i++) {
            Card method = supply.purchaseCard(Method.class);
            starterCards.add(method);
        }
        
        edu.brandeis.cosi103a.ip1.core.Deck deck = new edu.brandeis.cosi103a.ip1.core.Deck(starterCards);
        player.setDeck(deck);
        
        // Verify starter deck has 10 cards (7 Bitcoin + 3 Method)
        assertEquals(10, deck.getAllCards().size());
        
        // Count Bitcoins and Methods
        int bitcoinCount = 0;
        int methodCount = 0;
        for (Card card : deck.getAllCards()) {
            if (card instanceof Bitcoin) {
                bitcoinCount++;
            } else if (card instanceof Method) {
                methodCount++;
            }
        }
        assertEquals(7, bitcoinCount);
        assertEquals(3, methodCount);
    }
    
    @Test
    public void testHandDealing() {
        Supply supply = new Supply();
        Player player = new Player("Test Player");
        
        // Initialize starter deck
        java.util.List<Card> starterCards = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            starterCards.add(supply.purchaseCard(Bitcoin.class));
        }
        for (int i = 0; i < 3; i++) {
            starterCards.add(supply.purchaseCard(Method.class));
        }
        
        edu.brandeis.cosi103a.ip1.core.Deck deck = new edu.brandeis.cosi103a.ip1.core.Deck(starterCards);
        player.setDeck(deck);
        
        // Deal 5 cards
        java.util.List<Card> hand = deck.drawCards(5);
        player.setHand(hand);
        
        // Verify hand has 5 cards
        assertEquals(5, player.getHand().size());
        
        // Verify deck has 5 cards remaining (10 - 5 = 5)
        assertEquals(5, deck.getAllCards().size());
    }
    
    @Test
    public void testSupplyCountsAfterStarterDecks() {
        Supply supply = new Supply();
        
        // Initialize two starter decks (simulating two players)
        for (int player = 0; player < 2; player++) {
            for (int i = 0; i < 7; i++) {
                supply.purchaseCard(Bitcoin.class);
            }
            for (int i = 0; i < 3; i++) {
                supply.purchaseCard(Method.class);
            }
        }
        
        // Verify supply counts decreased
        // Bitcoin: 60 - 14 = 46
        assertEquals(46, supply.getCount(Bitcoin.class));
        // Method: 14 - 6 = 8
        assertEquals(8, supply.getCount(Method.class));
    }
    
    @Test
    public void testPlayerSwitching() {
        Game game = new Game();
        game.start();
        
        // Get initial current player
        edu.brandeis.cosi103a.ip1.core.GameState gameState = game.getGameState();
        Player initialPlayer = gameState.getCurrentPlayer();
        
        // Switch player
        gameState.switchPlayer();
        Player switchedPlayer = gameState.getCurrentPlayer();
        
        // Should be different player
        assertNotSame(initialPlayer, switchedPlayer);
        
        // Switch back
        gameState.switchPlayer();
        Player backToInitial = gameState.getCurrentPlayer();
        
        // Should be back to initial player
        assertSame(initialPlayer, backToInitial);
    }
    
    @Test
    public void testGameEndsWhenAllFrameworksPurchased() {
        Supply supply = new Supply();
        
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.purchaseCard(edu.brandeis.cosi103a.ip1.features.cards.automation.Framework.class);
        }
        
        assertTrue(supply.isGameOver());
    }
    
    @Test
    public void testWinnerDetermination() {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        
        // Give player1 more APs
        java.util.List<Card> cards1 = new java.util.ArrayList<>();
        cards1.add(new edu.brandeis.cosi103a.ip1.features.cards.automation.Framework()); // 6 AP
        cards1.add(new edu.brandeis.cosi103a.ip1.features.cards.automation.Module()); // 3 AP
        edu.brandeis.cosi103a.ip1.core.Deck deck1 = new edu.brandeis.cosi103a.ip1.core.Deck(cards1);
        player1.setDeck(deck1);
        
        // Give player2 fewer APs
        java.util.List<Card> cards2 = new java.util.ArrayList<>();
        cards2.add(new Method()); // 1 AP
        edu.brandeis.cosi103a.ip1.core.Deck deck2 = new edu.brandeis.cosi103a.ip1.core.Deck(cards2);
        player2.setDeck(deck2);
        
        // Player1 should have more APs
        assertTrue(player1.getTotalAutomationPoints() > player2.getTotalAutomationPoints());
        assertEquals(9, player1.getTotalAutomationPoints());
        assertEquals(1, player2.getTotalAutomationPoints());
    }
    
    @Test
    public void testGameLoopExecutesPhases() {
        // This is a simplified test - a full game would take too long
        // We test that phases can be executed without errors        
        // Create a minimal game state for testing
        Supply supply = new Supply();
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        
        // Initialize minimal decks
        java.util.List<Card> starter1 = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            starter1.add(supply.purchaseCard(Bitcoin.class));
        }
        edu.brandeis.cosi103a.ip1.core.Deck deck1 = new edu.brandeis.cosi103a.ip1.core.Deck(starter1);
        player1.setDeck(deck1);
        player1.setHand(deck1.drawCards(5));
        
        java.util.List<Card> starter2 = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            starter2.add(supply.purchaseCard(Bitcoin.class));
        }
        edu.brandeis.cosi103a.ip1.core.Deck deck2 = new edu.brandeis.cosi103a.ip1.core.Deck(starter2);
        player2.setDeck(deck2);
        player2.setHand(deck2.drawCards(5));
        
        // Test that buy phase can execute
        edu.brandeis.cosi103a.ip1.features.phases.BuyPhase buyPhase = new edu.brandeis.cosi103a.ip1.features.phases.BuyPhase();
        buyPhase.execute(player1, supply);
        
        // Test that cleanup phase can execute
        edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase cleanupPhase = new edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase();
        cleanupPhase.execute(player1);
        
        // Verify hand was dealt
        assertTrue(player1.getHand().size() > 0);
    }
}
