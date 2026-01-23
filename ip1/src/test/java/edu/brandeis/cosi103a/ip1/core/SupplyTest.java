package edu.brandeis.cosi103a.ip1.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.brandeis.cosi103a.ip1.features.cards.automation.Framework;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Method;
import edu.brandeis.cosi103a.ip1.features.cards.automation.Module;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Bitcoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Dogecoin;
import edu.brandeis.cosi103a.ip1.features.cards.cryptocurrency.Ethereum;

/**
 * Unit tests for Supply class.
 */
public class SupplyTest {
    private Supply supply;
    
    @Before
    public void setUp() {
        supply = new Supply();
    }
    
    @Test
    public void testInitialization() {
        assertEquals(14, supply.getCount(Method.class));
        assertEquals(8, supply.getCount(Module.class));
        assertEquals(8, supply.getCount(Framework.class));
        assertEquals(60, supply.getCount(Bitcoin.class));
        assertEquals(40, supply.getCount(Ethereum.class));
        assertEquals(30, supply.getCount(Dogecoin.class));
    }
    
    @Test
    public void testIsAvailable() {
        assertTrue(supply.isAvailable(Method.class));
        assertTrue(supply.isAvailable(Framework.class));
        assertTrue(supply.isAvailable(Bitcoin.class));
    }
    
    @Test
    public void testIsAvailableAfterPurchase() {
        supply.purchaseCard(Method.class);
        assertTrue(supply.isAvailable(Method.class)); // Still 13 left
        assertEquals(13, supply.getCount(Method.class));
    }
    
    @Test
    public void testIsAvailableWhenEmpty() {
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.purchaseCard(Framework.class);
        }
        assertFalse(supply.isAvailable(Framework.class));
        assertEquals(0, supply.getCount(Framework.class));
    }
    
    @Test
    public void testPurchaseCard() {
        Card card = supply.purchaseCard(Method.class);
        assertNotNull(card);
        assertTrue(card instanceof Method);
        assertEquals(13, supply.getCount(Method.class));
    }
    
    @Test
    public void testPurchaseCardMultiple() {
        supply.purchaseCard(Method.class);
        supply.purchaseCard(Method.class);
        supply.purchaseCard(Method.class);
        assertEquals(11, supply.getCount(Method.class));
    }
    
    @Test
    public void testPurchaseCardWhenUnavailable() {
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.purchaseCard(Framework.class);
        }
        
        Card card = supply.purchaseCard(Framework.class);
        assertNull(card);
        assertEquals(0, supply.getCount(Framework.class));
    }
    
    @Test
    public void testPurchaseAllCardTypes() {
        Card method = supply.purchaseCard(Method.class);
        Card module = supply.purchaseCard(Module.class);
        Card framework = supply.purchaseCard(Framework.class);
        Card bitcoin = supply.purchaseCard(Bitcoin.class);
        Card ethereum = supply.purchaseCard(Ethereum.class);
        Card dogecoin = supply.purchaseCard(Dogecoin.class);
        
        assertNotNull(method);
        assertNotNull(module);
        assertNotNull(framework);
        assertNotNull(bitcoin);
        assertNotNull(ethereum);
        assertNotNull(dogecoin);
        
        assertTrue(method instanceof Method);
        assertTrue(module instanceof Module);
        assertTrue(framework instanceof Framework);
        assertTrue(bitcoin instanceof Bitcoin);
        assertTrue(ethereum instanceof Ethereum);
        assertTrue(dogecoin instanceof Dogecoin);
    }
    
    @Test
    public void testIsGameOverFalse() {
        assertFalse(supply.isGameOver());
    }
    
    @Test
    public void testIsGameOverTrue() {
        // Purchase all Framework cards
        for (int i = 0; i < 8; i++) {
            supply.purchaseCard(Framework.class);
        }
        assertTrue(supply.isGameOver());
    }
    
    @Test
    public void testIsGameOverAfterPartialPurchase() {
        // Purchase some but not all Framework cards
        supply.purchaseCard(Framework.class);
        supply.purchaseCard(Framework.class);
        assertFalse(supply.isGameOver());
        assertEquals(6, supply.getCount(Framework.class));
    }
    
    @Test
    public void testGetCount() {
        assertEquals(14, supply.getCount(Method.class));
        supply.purchaseCard(Method.class);
        assertEquals(13, supply.getCount(Method.class));
    }
    
    @Test
    public void testGetCountUnknownCard() {
        // Test with a class that's not in supply
        class UnknownCard implements Card {
            public int getCost() { return 0; }
            public String getName() { return "Unknown"; }
        }
        assertEquals(0, supply.getCount(UnknownCard.class));
        assertFalse(supply.isAvailable(UnknownCard.class));
    }
}
