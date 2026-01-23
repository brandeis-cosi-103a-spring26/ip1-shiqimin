package edu.brandeis.cosi103a.ip1.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;

/**
 * Unit tests for Deck class.
 */
public class DeckTest {
    private Deck deck;
    private List<Card> initialCards;
    
    @Before
    public void setUp() {
        initialCards = new ArrayList<>();
        // Create 5 cards for testing
        for (int i = 0; i < 3; i++) {
            initialCards.add(new Bitcoin());
        }
        for (int i = 0; i < 2; i++) {
            initialCards.add(new Method());
        }
        deck = new Deck(initialCards);
    }
    
    @Test
    public void testInitialState() {
        assertEquals(5, deck.getDrawPileSize());
        assertEquals(0, deck.getDiscardPileSize());
    }
    
    @Test
    public void testDrawCard() {
        Card card = deck.drawCard();
        assertNotNull(card);
        assertEquals(4, deck.getDrawPileSize());
        assertEquals(0, deck.getDiscardPileSize());
    }
    
    @Test
    public void testDrawCards() {
        List<Card> drawn = deck.drawCards(3);
        assertEquals(3, drawn.size());
        assertEquals(2, deck.getDrawPileSize());
    }
    
    @Test
    public void testDrawCardsMoreThanAvailable() {
        List<Card> drawn = deck.drawCards(10);
        assertEquals(5, drawn.size()); // Only 5 cards available
        assertEquals(0, deck.getDrawPileSize());
    }
    
    @Test
    public void testDiscard() {
        List<Card> toDiscard = new ArrayList<>();
        toDiscard.add(new Bitcoin());
        toDiscard.add(new Method());
        
        deck.discard(toDiscard);
        assertEquals(5, deck.getDrawPileSize());
        assertEquals(2, deck.getDiscardPileSize());
    }
    
    @Test
    public void testShuffleDiscardIntoDraw() {
        // Draw all cards
        List<Card> drawn = deck.drawCards(5);
        assertEquals(0, deck.getDrawPileSize());
        
        // Discard them
        deck.discard(drawn);
        assertEquals(0, deck.getDrawPileSize());
        assertEquals(5, deck.getDiscardPileSize());
        
        // Shuffle discard into draw
        deck.shuffleDiscardIntoDraw();
        assertEquals(5, deck.getDrawPileSize());
        assertEquals(0, deck.getDiscardPileSize());
    }
    
    @Test
    public void testDrawCardWithEmptyDrawPile() {
        // Draw all cards
        deck.drawCards(5);
        assertEquals(0, deck.getDrawPileSize());
        
        // Discard some cards
        List<Card> toDiscard = new ArrayList<>();
        toDiscard.add(new Bitcoin());
        deck.discard(toDiscard);
        
        // Draw should automatically shuffle discard into draw
        Card card = deck.drawCard();
        assertNotNull(card);
        assertEquals(0, deck.getDrawPileSize()); // All cards drawn again
        assertEquals(0, deck.getDiscardPileSize());
    }
    
    @Test
    public void testDrawCardWhenCompletelyEmpty() {
        // Draw all cards
        deck.drawCards(5);
        // No discard, so deck is completely empty
        Card card = deck.drawCard();
        assertNull(card);
    }
    
    @Test
    public void testDrawCardsWithShuffle() {
        // Draw all cards
        List<Card> drawn1 = deck.drawCards(5);
        // Discard them
        deck.discard(drawn1);
        
        // Draw should automatically shuffle
        List<Card> drawn2 = deck.drawCards(3);
        assertEquals(3, drawn2.size());
        assertEquals(2, deck.getDrawPileSize());
    }
}
