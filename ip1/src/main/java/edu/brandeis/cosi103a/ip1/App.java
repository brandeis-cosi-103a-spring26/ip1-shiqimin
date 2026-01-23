package edu.brandeis.cosi103a.ip1;

import edu.brandeis.cosi103a.ip1.game.GameOrchestrator;

/**
 * Main entry point for Automation: The Game (ATG).
 * Delegates to GameOrchestrator to start the game.
 */
public class App 
{
    /**
     * Main game entry point
     */
    public static void main( String[] args )
    {
        GameOrchestrator.main(args);
    }
}
