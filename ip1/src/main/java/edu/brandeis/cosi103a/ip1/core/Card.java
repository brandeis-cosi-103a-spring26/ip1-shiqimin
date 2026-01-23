package edu.brandeis.cosi103a.ip1.core;

/**
 * Base interface for all cards in the game.
 * All card types must implement this interface.
 */
public interface Card {
    /**
     * Gets the cost of the card in cryptocoins.
     * @return the cost to purchase this card
     */
    int getCost();
    
    /**
     * Gets the name of the card.
     * @return the card's name
     */
    String getName();
}
