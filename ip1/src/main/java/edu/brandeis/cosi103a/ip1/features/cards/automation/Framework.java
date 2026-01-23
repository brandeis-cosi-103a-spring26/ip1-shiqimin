package edu.brandeis.cosi103a.ip1.features.cards.automation;

import edu.brandeis.cosi103a.ip1.core.AutomationCard;

/**
 * Framework card: cost 8, value 6 AP
 */
public class Framework extends AutomationCard {
    public Framework() {
        this.cost = 8;
        this.value = 6;
    }
    
    @Override
    public String getName() {
        return "Framework";
    }
}
