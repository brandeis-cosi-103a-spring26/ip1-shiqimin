package edu.brandeis.cosi103a.ip1.core;

import java.util.HashMap;
import java.util.Map;

import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Module;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Dogecoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Ethereum;

/**
 * Manages the supply of cards available for purchase.
 */
public class Supply {
    private Map<Class<? extends Card>, Integer> availableCards;
    
    public Supply() {
        availableCards = new HashMap<>();
        // Initialize supply with card counts as specified
        availableCards.put(Method.class, 14);
        availableCards.put(Module.class, 8);
        availableCards.put(Framework.class, 8);
        availableCards.put(Bitcoin.class, 60);
        availableCards.put(Ethereum.class, 40);
        availableCards.put(Dogecoin.class, 30);
    }
    
    /**
     * Checks if a card type is available in the supply.
     * @param cardType the card class
     * @return true if available (count > 0)
     */
    public boolean isAvailable(Class<? extends Card> cardType) {
        Integer count = availableCards.get(cardType);
        return count != null && count > 0;
    }
    
    /**
     * Gets the count of a card type in the supply.
     * @param cardType the card class
     * @return the count, or 0 if not in supply
     */
    public int getCount(Class<? extends Card> cardType) {
        Integer count = availableCards.get(cardType);
        return count != null ? count : 0;
    }
    
    /**
     * Purchases a card from the supply.
     * @param cardType the card class to purchase
     * @return the purchased card, or null if not available
     */
    public Card purchaseCard(Class<? extends Card> cardType) {
        if (!isAvailable(cardType)) {
            return null;
        }
        
        // Decrement count
        Integer count = availableCards.get(cardType);
        availableCards.put(cardType, count - 1);
        
        // Create and return new instance
        try {
            return cardType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // If instantiation fails, restore count
            availableCards.put(cardType, count);
            return null;
        }
    }
    
    /**
     * Checks if the game should end (all Framework cards purchased).
     * @return true if game should end
     */
    public boolean isGameOver() {
        return getCount(Framework.class) == 0;
    }
}
