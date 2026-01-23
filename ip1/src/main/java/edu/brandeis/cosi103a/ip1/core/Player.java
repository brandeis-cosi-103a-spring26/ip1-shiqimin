package edu.brandeis.cosi103a.ip1.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Manages the player's deck, hand, discard pile, and played cards.
 */
public class Player {
    private String name;
    private Deck deck;
    private List<Card> hand;
    private List<Card> playedCards;
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.playedCards = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }
    
    public Deck getDeck() {
        return deck;
    }
    
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
    
    public List<Card> getHand() {
        return hand;
    }
    
    public List<Card> getDiscardPile() {
        if (deck != null) {
            return deck.getDiscardPileCards();
        }
        return new ArrayList<>();
    }
    
    public List<Card> getPlayedCards() {
        return playedCards;
    }
    
    /**
     * Sets the player's hand.
     * @param cards the cards to set as hand
     */
    public void setHand(List<Card> cards) {
        this.hand = new ArrayList<>(cards);
    }
    
    /**
     * Adds a card to the discard pile.
     * @param card the card to discard
     */
    public void addToDiscardPile(Card card) {
        if (card != null && deck != null) {
            List<Card> cards = new ArrayList<>();
            cards.add(card);
            deck.discard(cards);
        }
    }
    
    /**
     * Moves a card from hand to played cards.
     * @param card the card to play
     */
    public void playCard(Card card) {
        if (card != null && hand.contains(card)) {
            hand.remove(card);
            playedCards.add(card);
        }
    }
    
    /**
     * Calculates the total Automation Points in the player's deck.
     * Includes all cards in deck, hand, and discard pile.
     * @return total AP value
     */
    public int getTotalAutomationPoints() {
        int totalAP = 0;
        
        // Count APs from deck (all cards in draw pile and discard pile)
        if (deck != null) {
            List<Card> deckCards = deck.getAllCards();
            for (Card card : deckCards) {
                if (card instanceof AutomationCard) {
                    AutomationCard autoCard = (AutomationCard) card;
                    totalAP += autoCard.getValue();
                }
            }
        }
        
        // Count APs from hand
        for (Card card : hand) {
            if (card instanceof AutomationCard) {
                AutomationCard autoCard = (AutomationCard) card;
                totalAP += autoCard.getValue();
            }
        }
        
        // Count APs from played cards (they're still in the player's possession)
        for (Card card : playedCards) {
            if (card instanceof AutomationCard) {
                AutomationCard autoCard = (AutomationCard) card;
                totalAP += autoCard.getValue();
            }
        }
        
        return totalAP;
    }
    
    /**
     * Gets all cards owned by the player (for AP calculation).
     * This includes cards in deck, hand, discard, and played.
     * @return list of all cards
     */
    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();
        
        // Add cards from hand
        allCards.addAll(hand);
        
        // Add cards from played
        allCards.addAll(playedCards);
        
        // Add cards from deck (draw pile and discard pile)
        if (deck != null) {
            allCards.addAll(deck.getAllCards());
        }
        
        return allCards;
    }
}
