package edu.brandeis.cosi103a.ip1.features.cards.automation;

import edu.brandeis.cosi103a.ip1.core.AutomationCard;

/**
 * Module card: cost 5, value 3 AP
 */
public class Module extends AutomationCard {
    public Module() {
        this.cost = 5;
        this.value = 3;
    }
    
    @Override
    public String getName() {
        return "Module";
    }
}
