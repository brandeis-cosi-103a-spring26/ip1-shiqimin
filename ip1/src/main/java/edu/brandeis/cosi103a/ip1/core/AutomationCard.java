package edu.brandeis.cosi103a.ip1.core;

/**
 * Abstract base class for automation cards.
 * Automation cards have a cost and an Automation Point (AP) value.
 */
public abstract class AutomationCard implements Card {
    protected int cost;
    protected int value; // AP value
    
    /**
     * Gets the cost of the card.
     * @return the cost in cryptocoins
     */
    @Override
    public int getCost() {
        return cost;
    }
    
    /**
     * Gets the Automation Point value of the card.
     * @return the AP value
     */
    public int getValue() {
        return value;
    }
}
