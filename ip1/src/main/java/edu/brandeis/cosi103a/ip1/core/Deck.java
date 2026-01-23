package edu.brandeis.cosi103a.ip1.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Manages deck operations: drawing, shuffling, discarding.
 */
public class Deck {
    private List<Card> drawPile;
    private List<Card> discardPile;
    private Random random;
    
    public Deck(List<Card> initialCards) {
        this.drawPile = new ArrayList<>(initialCards);
        this.discardPile = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Draws a card from the draw pile.
     * If draw pile is empty, shuffles discard pile and makes it the draw pile.
     * @return the drawn card, or null if no cards available
     */
    public Card drawCard() {
        if (drawPile.isEmpty() && !discardPile.isEmpty()) {
            shuffleDiscardIntoDraw();
        }
        
        if (drawPile.isEmpty()) {
            return null;
        }
        
        return drawPile.remove(0);
    }
    
    /**
     * Draws multiple cards.
     * @param count number of cards to draw
     * @return list of drawn cards
     */
    public List<Card> drawCards(int count) {
        List<Card> drawn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Card card = drawCard();
            if (card == null) {
                break; // No more cards available
            }
            drawn.add(card);
        }
        return drawn;
    }
    
    /**
     * Adds cards to the discard pile.
     * @param cards cards to discard
     */
    public void discard(List<Card> cards) {
        if (cards != null) {
            discardPile.addAll(cards);
        }
    }
    
    /**
     * Shuffles the discard pile and makes it the draw pile.
     */
    public void shuffleDiscardIntoDraw() {
        Collections.shuffle(discardPile, random);
        drawPile.addAll(discardPile);
        discardPile.clear();
    }
    
    /**
     * Gets the current size of the draw pile.
     * @return number of cards in draw pile
     */
    public int getDrawPileSize() {
        return drawPile.size();
    }
    
    /**
     * Gets the current size of the discard pile.
     * @return number of cards in discard pile
     */
    public int getDiscardPileSize() {
        return discardPile.size();
    }
    
    /**
     * Gets all cards in the draw pile (for AP calculation).
     * @return list of cards in draw pile
     */
    public List<Card> getDrawPileCards() {
        return new ArrayList<>(drawPile);
    }
    
    /**
     * Gets all cards in the discard pile (for AP calculation).
     * @return list of cards in discard pile
     */
    public List<Card> getDiscardPileCards() {
        return new ArrayList<>(discardPile);
    }
    
    /**
     * Gets all cards in the deck (draw pile + discard pile).
     * @return list of all cards
     */
    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(drawPile);
        allCards.addAll(discardPile);
        return allCards;
    }
}
