package edu.brandeis.cosi103a.ip1.core;

/**
 * Abstract base class for cryptocurrency cards.
 * Cryptocurrency cards have a cost and a value (cryptocoins) when played.
 */
public abstract class CryptocurrencyCard implements Card {
    protected int cost;
    protected int value; // cryptocoin value when played
    
    /**
     * Gets the cost of the card.
     * @return the cost in cryptocoins
     */
    @Override
    public int getCost() {
        return cost;
    }
    
    /**
     * Gets the cryptocoin value when the card is played.
     * @return the cryptocoin value
     */
    public int getValue() {
        return value;
    }
}
