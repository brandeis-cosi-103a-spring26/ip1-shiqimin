package edu.brandeis.cosi103a.ip1.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Module;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;

/**
 * Unit tests for Player class.
 */
public class PlayerTest {
    private Player player;
    
    @Before
    public void setUp() {
        player = new Player("Test Player");
    }
    
    @Test
    public void testInitialization() {
        assertEquals("Test Player", player.getName());
        assertNull(player.getDeck()); // Deck is null until set
        assertNotNull(player.getHand());
        assertNotNull(player.getDiscardPile());
        assertNotNull(player.getPlayedCards());
        assertTrue(player.getHand().isEmpty());
        assertTrue(player.getDiscardPile().isEmpty());
        assertTrue(player.getPlayedCards().isEmpty());
    }
    
    @Test
    public void testSetHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Bitcoin());
        cards.add(new Method());
        
        player.setHand(cards);
        assertEquals(2, player.getHand().size());
    }
    
    @Test
    public void testAddToDiscardPile() {
        // Player needs a deck for discard pile to work
        Deck deck = new Deck(new ArrayList<>());
        player.setDeck(deck);
        
        Card card = new Bitcoin();
        player.addToDiscardPile(card);
        assertEquals(1, player.getDiscardPile().size());
        assertTrue(player.getDiscardPile().contains(card));
    }
    
    @Test
    public void testPlayCard() {
        Card card = new Bitcoin();
        player.getHand().add(card);
        
        player.playCard(card);
        assertEquals(0, player.getHand().size());
        assertEquals(1, player.getPlayedCards().size());
        assertTrue(player.getPlayedCards().contains(card));
    }
    
    @Test
    public void testPlayCardNotInHand() {
        Card card = new Bitcoin();
        // Card not in hand
        
        player.playCard(card);
        assertEquals(0, player.getHand().size());
        assertEquals(0, player.getPlayedCards().size());
    }
    
    @Test
    public void testGetTotalAutomationPointsEmpty() {
        assertEquals(0, player.getTotalAutomationPoints());
    }
    
    @Test
    public void testGetTotalAutomationPointsFromDeck() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Method()); // 1 AP
        cards.add(new Module()); // 3 AP
        cards.add(new Framework()); // 6 AP
        cards.add(new Bitcoin()); // 0 AP (not automation)
        Deck deck = new Deck(cards);
        player.setDeck(deck);
        
        assertEquals(10, player.getTotalAutomationPoints());
    }
    
    @Test
    public void testGetTotalAutomationPointsFromHand() {
        player.getHand().add(new Method()); // 1 AP
        player.getHand().add(new Module()); // 3 AP
        player.getHand().add(new Bitcoin()); // 0 AP
        
        assertEquals(4, player.getTotalAutomationPoints());
    }
    
    @Test
    public void testGetTotalAutomationPointsFromDiscardPile() {
        // Set up deck and add cards to discard pile
        Deck deck = new Deck(new ArrayList<>());
        player.setDeck(deck);
        
        List<Card> discardCards = new ArrayList<>();
        discardCards.add(new Framework()); // 6 AP
        discardCards.add(new Method()); // 1 AP
        discardCards.add(new Bitcoin()); // 0 AP
        deck.discard(discardCards);
        
        assertEquals(7, player.getTotalAutomationPoints());
    }
    
    @Test
    public void testGetTotalAutomationPointsFromAllSources() {
        // Deck: 1 Method (1 AP)
        List<Card> deckCards = new ArrayList<>();
        deckCards.add(new Method());
        Deck deck = new Deck(deckCards);
        player.setDeck(deck);
        
        // Hand: 1 Module (3 AP)
        player.getHand().add(new Module());
        
        // Discard: 1 Framework (6 AP) - add via discard
        List<Card> discardCards = new ArrayList<>();
        discardCards.add(new Framework());
        deck.discard(discardCards);
        
        // Total: 1 + 3 + 6 = 10 AP
        assertEquals(10, player.getTotalAutomationPoints());
    }
    
    @Test
    public void testGetTotalAutomationPointsMultipleCards() {
        // Add multiple automation cards to deck
        List<Card> deckCards = new ArrayList<>();
        deckCards.add(new Method());
        deckCards.add(new Method());
        deckCards.add(new Method());
        Deck deck = new Deck(deckCards);
        player.setDeck(deck);
        
        // Hand: 2 Modules (6 AP)
        player.getHand().add(new Module());
        player.getHand().add(new Module());
        
        // Discard: 1 Framework (6 AP)
        List<Card> discardCards = new ArrayList<>();
        discardCards.add(new Framework());
        deck.discard(discardCards);
        
        // 3 Methods (3 AP) + 2 Modules (6 AP) + 1 Framework (6 AP) = 15 AP
        assertEquals(15, player.getTotalAutomationPoints());
    }
}
