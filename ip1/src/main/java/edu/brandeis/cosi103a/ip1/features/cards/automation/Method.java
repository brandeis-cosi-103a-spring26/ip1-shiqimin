package edu.brandeis.cosi103a.ip1.features.cards.automation;

import edu.brandeis.cosi103a.ip1.core.AutomationCard;

/**
 * Method card: cost 2, value 1 AP
 */
public class Method extends AutomationCard {
    public Method() {
        this.cost = 2;
        this.value = 1;
    }
    
    @Override
    public String getName() {
        return "Method";
    }
}
