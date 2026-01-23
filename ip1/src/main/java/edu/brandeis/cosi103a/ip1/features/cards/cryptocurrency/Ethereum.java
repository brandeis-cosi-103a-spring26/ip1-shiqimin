package edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency;

import edu.brandeis.cosi103a.ip1.core.CryptocurrencyCard;

/**
 * Ethereum card: cost 3, value 2 cryptocoins
 */
public class Ethereum extends CryptocurrencyCard {
    public Ethereum() {
        this.cost = 3;
        this.value = 2;
    }
    
    @Override
    public String getName() {
        return "Ethereum";
    }
}
