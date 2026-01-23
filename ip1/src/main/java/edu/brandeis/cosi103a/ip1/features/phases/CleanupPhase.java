package edu.brandeis.cosi103a.ip1.features.phases;

import edu.brandeis.cosi103a.ip1.core.Card;
import edu.brandeis.cosi103a.ip1.core.Deck;
import edu.brandeis.cosi103a.ip1.core.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Cleanup phase: player discards their hand and all played cards,
 * then deals a new hand from their deck.
 */
public class CleanupPhase {
    
    /**
     * Executes the cleanup phase for a player.
     * @param player the current player
     */
    public void execute(Player player) {
        Deck deck = player.getDeck();
        if (deck == null) {
            return;
        }
        
        // Get hand and played cards (make copies to avoid concurrent modification)
        List<Card> hand = new ArrayList<>(player.getHand());
        List<Card> playedCards = new ArrayList<>(player.getPlayedCards());
        
        // Add hand and played cards to discard pile
        deck.discard(hand);
        deck.discard(playedCards);
        
        // Clear hand and played cards
        player.getHand().clear();
        player.getPlayedCards().clear();
        
        // Deal new hand (5 cards)
        // If draw pile is empty, deck.drawCards() will automatically shuffle discard into draw
        List<Card> newHand = deck.drawCards(5);
        player.setHand(newHand);
        
        System.out.println("  Cleanup: discards hand and played cards, draws 5.");
    }
}
