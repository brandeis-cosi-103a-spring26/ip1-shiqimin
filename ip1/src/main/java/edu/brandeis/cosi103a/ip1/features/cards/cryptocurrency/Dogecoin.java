package edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency;

import edu.brandeis.cosi103a.ip1.core.CryptocurrencyCard;

/**
 * Dogecoin card: cost 6, value 3 cryptocoins
 */
public class Dogecoin extends CryptocurrencyCard {
    public Dogecoin() {
        this.cost = 6;
        this.value = 3;
    }
    
    @Override
    public String getName() {
        return "Dogecoin";
    }
}
