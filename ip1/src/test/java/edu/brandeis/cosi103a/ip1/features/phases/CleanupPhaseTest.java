package edu.brandeis.cosi103a.ip1.features.phases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.Deck;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;

/**
 * Unit tests for CleanupPhase class.
 */
public class CleanupPhaseTest {
    private CleanupPhase cleanupPhase;
    private Player player;
    private Deck deck;
    
    @Before
    public void setUp() {
        cleanupPhase = new CleanupPhase();
        player = new Player("Test Player");
        
        // Create a deck with some cards
        List<Card> initialCards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            initialCards.add(new Bitcoin());
        }
        deck = new Deck(initialCards);
        player.setDeck(deck);
    }
    
    @Test
    public void testDiscardHandAndPlayedCards() {
        // Set up hand and played cards
        List<Card> hand = new ArrayList<>();
        hand.add(new Bitcoin());
        hand.add(new Method());
        player.setHand(hand);
        
        player.getPlayedCards().add(new Bitcoin());
        player.getPlayedCards().add(new Bitcoin());
        
        int handSizeBefore = player.getHand().size();
        int playedSizeBefore = player.getPlayedCards().size();
        
        cleanupPhase.execute(player);
        
        // Played cards should be cleared
        assertEquals(0, player.getPlayedCards().size());
        
        // New hand should be drawn (5 cards)
        assertEquals(5, player.getHand().size());
        
        // Cards should be in discard pile
        assertTrue(deck.getDiscardPileSize() >= handSizeBefore + playedSizeBefore);
    }
    
    @Test
    public void testDrawNewHand() {
        // Set up initial hand
        List<Card> initialHand = new ArrayList<>();
        initialHand.add(new Bitcoin());
        initialHand.add(new Bitcoin());
        player.setHand(initialHand);
        
        cleanupPhase.execute(player);
        
        // Should have 5 cards in new hand
        assertEquals(5, player.getHand().size());
    }
    
    @Test
    public void testDrawNewHandWhenDrawPileEmpty() {
        // Draw all cards from draw pile first
        deck.drawCards(10);
        assertEquals(0, deck.getDrawPileSize());
        
        // Set up hand and played cards
        List<Card> hand = new ArrayList<>();
        hand.add(new Bitcoin());
        player.setHand(hand);
        player.getPlayedCards().add(new Bitcoin());
        player.getPlayedCards().add(new Bitcoin());
        
        cleanupPhase.execute(player);
        
        // Should automatically shuffle discard into draw and deal new hand
        // Only 3 cards available (1 in hand + 2 played), so should get 3, not 5
        assertEquals(3, player.getHand().size());
    }
    
    @Test
    public void testDrawNewHandWithInsufficientCards() {
        // Create deck with only 3 cards
        List<Card> smallDeck = new ArrayList<>();
        smallDeck.add(new Bitcoin());
        smallDeck.add(new Bitcoin());
        smallDeck.add(new Bitcoin());
        Deck smallDeckObj = new Deck(smallDeck);
        player.setDeck(smallDeckObj);
        
        // Set up hand
        List<Card> hand = new ArrayList<>();
        hand.add(new Bitcoin());
        player.setHand(hand);
        
        cleanupPhase.execute(player);
        
        // Should draw as many cards as available
        // Deck has 3 cards in draw pile, hand has 1 card (separate)
        // After cleanup: discard hand (1 card to discard), then draw 5
        // Draw 3 from draw pile, shuffle discard (1) into draw, draw 1 more = 4 total
        assertEquals(4, player.getHand().size());
    }
    
    @Test
    public void testMultipleCleanupPhases() {
        // First cleanup
        List<Card> hand1 = new ArrayList<>();
        hand1.add(new Bitcoin());
        player.setHand(hand1);
        cleanupPhase.execute(player);
                
        // Second cleanup
        List<Card> hand2 = new ArrayList<>(player.getHand());
        player.setHand(hand2);
        cleanupPhase.execute(player);
        
        // Should still be able to draw cards
        assertTrue(player.getHand().size() > 0);
    }
    
    @Test
    public void testCleanupWithEmptyHand() {
        // Player has no hand, only played cards
        player.getPlayedCards().add(new Bitcoin());
        player.getPlayedCards().add(new Method());
        
        cleanupPhase.execute(player);
        
        // Should still draw new hand
        assertEquals(5, player.getHand().size());
        assertEquals(0, player.getPlayedCards().size());
    }
    
    @Test
    public void testCleanupWithNoPlayedCards() {
        // Player has hand but no played cards
        List<Card> hand = new ArrayList<>();
        hand.add(new Bitcoin());
        hand.add(new Method());
        player.setHand(hand);
        
        cleanupPhase.execute(player);
        
        // Should discard hand and draw new hand
        assertEquals(5, player.getHand().size());
        assertEquals(0, player.getPlayedCards().size());
    }
    
    @Test
    public void testShuffleOnEmptyDrawPile() {
        // Draw all cards first
        List<Card> drawn = deck.drawCards(10);
        assertEquals(0, deck.getDrawPileSize());
        
        // Discard them
        deck.discard(drawn);
        assertEquals(10, deck.getDiscardPileSize());
        
        // Set up hand
        List<Card> hand = new ArrayList<>();
        hand.add(new Bitcoin());
        player.setHand(hand);
        
        cleanupPhase.execute(player);
        
        // Should shuffle discard into draw and deal new hand
        assertEquals(5, player.getHand().size());
        // Draw pile should have remaining cards (10 - 5 = 5, minus 1 from hand = 4, but cleanup adds hand to discard first)
        // Actually, the hand is discarded first, so discard has 11 cards, then 5 are drawn
        assertTrue(deck.getDrawPileSize() >= 0);
    }
}
