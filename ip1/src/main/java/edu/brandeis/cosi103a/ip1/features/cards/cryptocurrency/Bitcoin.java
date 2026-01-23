package edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency;

import edu.brandeis.cosi103a.ip1.core.CryptocurrencyCard;

/**
 * Bitcoin card: cost 0, value 1 cryptocoin
 */
public class Bitcoin extends CryptocurrencyCard {
    public Bitcoin() {
        this.cost = 0;
        this.value = 1;
    }
    
    @Override
    public String getName() {
        return "Bitcoin";
    }
}
