package edu.brandeis.cosi103a.ip1.features.phases;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.CryptocurrencyCard;
import edu.brandeis.cosi103a.ip1.core.Player;
import edu.brandeis.cosi103a.ip1.core.Supply;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Module;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Dogecoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Ethereum;

import java.util.ArrayList;
import java.util.List;

/**
 * Buy phase: player plays cryptocoins from their hand and can buy up to 1 card.
 * Bought cards go directly into the player's discard pile.
 */
public class BuyPhase {
    
    /**
     * Executes the buy phase for a player.
     * @param player the current player
     * @param supply the card supply
     */
    public void execute(Player player, Supply supply) {
        // Move cryptocurrency cards from hand to playedCards
        List<Card> hand = new ArrayList<>(player.getHand());
        List<Card> cryptoCards = new ArrayList<>();
        
        for (Card card : hand) {
            if (card instanceof CryptocurrencyCard) {
                cryptoCards.add(card);
                player.playCard(card);
            }
        }
        
        // Calculate total cryptocoins from played cryptocurrency cards
        int totalCoins = calculateCryptocoins(cryptoCards);
        System.out.println("  Plays " + totalCoins + " cryptocoins.");
        
        // Player can buy up to 1 card using the cryptocoins
        Card purchasedCard = buyBestAffordableCard(totalCoins, supply);
        
        if (purchasedCard != null) {
            System.out.println("  Buys " + purchasedCard.getName() + ".");
            // Add purchased card to discard pile
            player.addToDiscardPile(purchasedCard);
        } else {
            System.out.println("  Buys nothing.");
        }
    }
    
    /**
     * Calculates the total cryptocoins from cryptocurrency cards.
     * @param cryptoCards list of cryptocurrency cards
     * @return total cryptocoins value
     */
    private int calculateCryptocoins(List<Card> cryptoCards) {
        int total = 0;
        
        for (Card card : cryptoCards) {
            if (card instanceof CryptocurrencyCard) {
                CryptocurrencyCard cryptoCard = (CryptocurrencyCard) card;
                total += cryptoCard.getValue();
            }
        }
        
        return total;
    }
    
    /**
     * Simple AI: buys the best affordable card.
     * Priority: Framework (8) > Module (5) > Method (2) > Dogecoin (6) > Ethereum (3) > Bitcoin (0)
     * @param totalCoins available cryptocoins
     * @param supply the card supply
     * @return purchased card, or null if none affordable
     */
    private Card buyBestAffordableCard(int totalCoins, Supply supply) {
        // Try to buy in priority order
        if (totalCoins >= 8 && supply.isAvailable(Framework.class)) {
            return supply.purchaseCard(Framework.class);
        }
        if (totalCoins >= 5 && supply.isAvailable(Module.class)) {
            return supply.purchaseCard(Module.class);
        }
        if (totalCoins >= 6 && supply.isAvailable(Dogecoin.class)) {
            return supply.purchaseCard(Dogecoin.class);
        }
        if (totalCoins >= 3 && supply.isAvailable(Ethereum.class)) {
            return supply.purchaseCard(Ethereum.class);
        }
        if (totalCoins >= 2 && supply.isAvailable(Method.class)) {
            return supply.purchaseCard(Method.class);
        }
        if (totalCoins >= 0 && supply.isAvailable(Bitcoin.class)) {
            return supply.purchaseCard(Bitcoin.class);
        }
        
        return null;
    }
}
