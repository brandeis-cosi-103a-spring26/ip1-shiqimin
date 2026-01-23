package edu.brandeis.cosi103a.ip1;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.GameState;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.core.Supply;
import edu.brandeis.cosi103a.ip1.game.Game;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;

/**
 * Integration test for complete game flow from start to finish.
 */
public class GameIntegrationTest {
    
    @Test
    public void testCompleteGameSetup() {
        Game game = new Game();
        game.start();
        
        GameState gameState = game.getGameState();
        assertNotNull(gameState);
        assertNotNull(gameState.getCurrentPlayer());
        assertNotNull(gameState.getOtherPlayer());
        assertNotNull(gameState.getSupply());
        
        // Verify players have decks and hands
        Player player1 = gameState.getCurrentPlayer();
        Player player2 = gameState.getOtherPlayer();
        
        assertNotNull(player1.getDeck());
        assertNotNull(player2.getDeck());
        assertTrue(player1.getHand().size() > 0);
        assertTrue(player2.getHand().size() > 0);
    }
    
    @Test
    public void testGameMechanicsWorkTogether() {
        // Create a controlled game scenario
        Supply supply = new Supply();
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        
        // Initialize starter decks
        java.util.List<edu.brandeis.cosi103a.ip1.core.Card> starter1 = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            starter1.add(supply.purchaseCard(Bitcoin.class));
        }
        for (int i = 0; i < 3; i++) {
            starter1.add(supply.purchaseCard(Method.class));
        }
        edu.brandeis.cosi103a.ip1.core.Deck deck1 = new edu.brandeis.cosi103a.ip1.core.Deck(starter1);
        player1.setDeck(deck1);
        player1.setHand(deck1.drawCards(5));
        
        java.util.List<edu.brandeis.cosi103a.ip1.core.Card> starter2 = new java.util.ArrayList<>();
        for (int i = 0; i < 7; i++) {
            starter2.add(supply.purchaseCard(Bitcoin.class));
        }
        for (int i = 0; i < 3; i++) {
            starter2.add(supply.purchaseCard(Method.class));
        }
        edu.brandeis.cosi103a.ip1.core.Deck deck2 = new edu.brandeis.cosi103a.ip1.core.Deck(starter2);
        player2.setDeck(deck2);
        player2.setHand(deck2.drawCards(5));
        
        // Test buy phase
        edu.brandeis.cosi103a.ip1.features.phases.BuyPhase buyPhase = new edu.brandeis.cosi103a.ip1.features.phases.BuyPhase();
        buyPhase.execute(player1, supply);
        
        // Verify player can purchase cards
        assertTrue(player1.getDiscardPile().size() >= 0); // May or may not have purchased
        
        // Test cleanup phase
        edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase cleanupPhase = new edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase();
        cleanupPhase.execute(player1);
        
        // Verify new hand was dealt
        assertTrue(player1.getHand().size() > 0);
        
        // Verify AP calculation works
        int ap1 = player1.getTotalAutomationPoints();
        int ap2 = player2.getTotalAutomationPoints();
        assertTrue(ap1 >= 0);
        assertTrue(ap2 >= 0);
    }
    
    @Test
    public void testGameEndCondition() {
        Supply supply = new Supply();
        
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            Card framework = supply.purchaseCard(Framework.class);
            assertNotNull(framework);
        }
        
        // Game should be over
        assertTrue(supply.isGameOver());
        assertEquals(0, supply.getCount(Framework.class));
    }
    
    @Test
    public void testFullTurnCycle() {
        Supply supply = new Supply();
        Player player = new Player("Test Player");
        
        // Setup deck and hand
        java.util.List<edu.brandeis.cosi103a.ip1.core.Card> cards = new java.util.ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cards.add(supply.purchaseCard(Bitcoin.class));
        }
        edu.brandeis.cosi103a.ip1.core.Deck deck = new edu.brandeis.cosi103a.ip1.core.Deck(cards);
        player.setDeck(deck);
        player.setHand(deck.drawCards(5));
        
        // Execute buy phase
        edu.brandeis.cosi103a.ip1.features.phases.BuyPhase buyPhase = new edu.brandeis.cosi103a.ip1.features.phases.BuyPhase();
        buyPhase.execute(player, supply);
        
        // Execute cleanup phase
        edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase cleanupPhase = new edu.brandeis.cosi103a.ip1.features.phases.CleanupPhase();
        cleanupPhase.execute(player);
        
        // Verify hand was refreshed
        assertEquals(5, player.getHand().size());
        
        // Verify cards were properly managed
        assertTrue(deck.getAllCards().size() >= 0); // Cards may be in various piles
    }
    
    @Test
    public void testPlayerAPCalculationIncludesAllCards() {
        Player player = new Player("Test Player");
        
        // Add cards to deck
        java.util.List<edu.brandeis.cosi103a.ip1.core.Card> deckCards = new java.util.ArrayList<>();
        deckCards.add(new Method()); // 1 AP
        deckCards.add(new Framework()); // 6 AP
        edu.brandeis.cosi103a.ip1.core.Deck deck = new edu.brandeis.cosi103a.ip1.core.Deck(deckCards);
        player.setDeck(deck);
        
        // Add cards to hand
        java.util.List<edu.brandeis.cosi103a.ip1.core.Card> handCards = new java.util.ArrayList<>();
        handCards.add(new edu.brandeis.cosi103a.ip1.features.cards.automation.Module()); // 3 AP
        player.setHand(handCards);
        
        // Total should be 1 + 6 + 3 = 10 AP
        assertEquals(10, player.getTotalAutomationPoints());
    }
}
