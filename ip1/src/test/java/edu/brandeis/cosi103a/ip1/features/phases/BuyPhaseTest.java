package edu.brandeis.cosi103a.ip1.features.phases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.Deck;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.core.Supply;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Module;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Dogecoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Ethereum;

/**
 * Unit tests for BuyPhase class.
 */
public class BuyPhaseTest {
    private BuyPhase buyPhase;
    private Player player;
    private Supply supply;
    
    @Before
    public void setUp() {
        buyPhase = new BuyPhase();
        player = new Player("Test Player");
        supply = new Supply();
        
        // Set up a deck for the player (required for discard pile to work)
        List<Card> initialCards = new ArrayList<>();
        Deck deck = new Deck(initialCards);
        player.setDeck(deck);
    }
    
    @Test
    public void testCalculateCryptocoins() {
        // Add cryptocurrency cards to hand
        player.getHand().add(new Bitcoin()); // 1 coin
        player.getHand().add(new Bitcoin()); // 1 coin
        player.getHand().add(new Ethereum()); // 2 coins
        player.getHand().add(new Method()); // 0 coins (not crypto)
        
        // Execute buy phase
        buyPhase.execute(player, supply);
        
        // Verify cryptocurrency cards were moved to playedCards
        assertEquals(3, player.getPlayedCards().size());
        assertEquals(1, player.getHand().size()); // Only Method remains
    }
    
    @Test
    public void testBuyFramework() {
        // Give player enough coins (8) to buy Framework
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Ethereum()); // 2 coins
        // Total: 8 coins
        
        buyPhase.execute(player, supply);
        
        // Verify Framework was purchased
        assertEquals(1, player.getDiscardPile().size());
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Framework);
        
        // Verify supply decreased
        assertEquals(7, supply.getCount(Framework.class));
    }
    
    @Test
    public void testBuyModule() {
        // Give player 5 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Ethereum()); // 2 coins
        // Total: 5 coins
        
        buyPhase.execute(player, supply);
        
        // Verify Module was purchased
        assertEquals(1, player.getDiscardPile().size());
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Module);
    }
    
    @Test
    public void testBuyMethod() {
        // Give player 2 coins
        player.getHand().add(new Bitcoin()); // 1 coin
        player.getHand().add(new Bitcoin()); // 1 coin
        // Total: 2 coins
        
        buyPhase.execute(player, supply);
        
        // Verify Method was purchased
        assertEquals(1, player.getDiscardPile().size());
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Method);
    }
    
    @Test
    public void testBuyPriorityFrameworkOverModule() {
        // Give player 8 coins (can afford both Framework and Module)
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Ethereum()); // 2 coins
        // Total: 8 coins
        
        buyPhase.execute(player, supply);
        
        // Should buy Framework (higher priority), not Module
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Framework);
        assertFalse(purchased instanceof Module);
    }
    
    @Test
    public void testBuyPriorityModuleOverMethod() {
        // Give player 5 coins (can afford Module and Method)
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Ethereum()); // 2 coins
        // Total: 5 coins
        
        buyPhase.execute(player, supply);
        
        // Should buy Module (higher priority), not Method
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Module);
        assertFalse(purchased instanceof Method);
    }
    
    @Test
    public void testCannotAffordAnyCard() {
        // Give player only 1 coin (can't afford anything except Bitcoin)
        player.getHand().add(new Bitcoin()); // 1 coin
        
        buyPhase.execute(player, supply);
        
        // Should buy Bitcoin (costs 0)
        assertEquals(1, player.getDiscardPile().size());
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Bitcoin);
    }
    
    @Test
    public void testBuyOnlyOneCardPerTurn() {
        // Give player 10 coins (can afford multiple cards)
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Bitcoin()); // 1 coin
        // Total: 10 coins
        
        buyPhase.execute(player, supply);
        
        // Should only buy 1 card (Framework)
        assertEquals(1, player.getDiscardPile().size());
    }
    
    @Test
    public void testNoPurchaseWhenSupplyEmpty() {
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.purchaseCard(Framework.class);
        }
        
        // Give player 8 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Dogecoin()); // 3 coins
        player.getHand().add(new Ethereum()); // 2 coins
        
        buyPhase.execute(player, supply);
        
        // Should buy Module instead (Framework unavailable)
        assertEquals(1, player.getDiscardPile().size());
        Card purchased = player.getDiscardPile().get(0);
        assertTrue(purchased instanceof Module);
    }
    
    @Test
    public void testCryptocurrencyCardsMovedToPlayed() {
        player.getHand().add(new Bitcoin());
        player.getHand().add(new Ethereum());
        player.getHand().add(new Method()); // Not crypto
        
        buyPhase.execute(player, supply);
        
        // Cryptocurrency cards should be in playedCards
        assertEquals(2, player.getPlayedCards().size());
        assertTrue(player.getPlayedCards().get(0) instanceof Bitcoin || 
                   player.getPlayedCards().get(0) instanceof Ethereum);
        
        // Method should still be in hand
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().get(0) instanceof Method);
    }
}
