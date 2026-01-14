package edu.brandeis.cosi103a.ip1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Dice Game App.
 */
public class AppTest 
{
    private PrintStream originalOut;
    private ByteArrayOutputStream outputStream;
    
    @Before
    public void setUp() {
        // Capture System.out for testing output
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }
    
    @After
    public void tearDown() {
        // Restore System.out
        System.setOut(originalOut);
    }
    
    /**
     * Test that rollDie returns values within valid range (1-6)
     */
    @Test
    public void testRollDieRange() {
        for (int i = 0; i < 100; i++) {
            int result = App.rollDie();
            assertTrue("Die value should be at least 1", result >= 1);
            assertTrue("Die value should be at most 6", result <= 6);
        }
    }
    
    /**
     * Test that rollDie returns only integer values 1-6
     */
    @Test
    public void testRollDieValidValues() {
        boolean[] valuesFound = new boolean[7]; // indices 0-6, we care about 1-6
        
        // Roll many times to ensure we get all possible values
        for (int i = 0; i < 1000; i++) {
            int result = App.rollDie();
            assertTrue("Result should be between 1 and 6", result >= 1 && result <= 6);
            valuesFound[result] = true;
        }
        
        // Check that we found at least some of each value (probabilistic test)
        // In 1000 rolls, we should see each value at least once with high probability
        for (int i = 1; i <= 6; i++) {
            assertTrue("Should have rolled " + i + " at least once in 1000 rolls", valuesFound[i]);
        }
    }
    
    /**
     * Test determineWinner when player 1 wins
     */
    @Test
    public void testDetermineWinnerPlayer1Wins() {
        outputStream.reset();
        App.determineWinner("Player 1", 50, "Player 2", 30);
        String output = outputStream.toString();
        
        assertTrue("Output should contain 'Game Over'", output.contains("Game Over"));
        assertTrue("Output should contain Player 1's score", output.contains("Player 1: 50"));
        assertTrue("Output should contain Player 2's score", output.contains("Player 2: 30"));
        assertTrue("Output should declare Player 1 as winner", output.contains("Player 1 wins!"));
        assertFalse("Output should not declare Player 2 as winner", output.contains("Player 2 wins!"));
        assertFalse("Output should not declare a tie", output.contains("tie"));
    }
    
    /**
     * Test determineWinner when player 2 wins
     */
    @Test
    public void testDetermineWinnerPlayer2Wins() {
        outputStream.reset();
        App.determineWinner("Player 1", 25, "Player 2", 45);
        String output = outputStream.toString();
        
        assertTrue("Output should contain 'Game Over'", output.contains("Game Over"));
        assertTrue("Output should contain Player 1's score", output.contains("Player 1: 25"));
        assertTrue("Output should contain Player 2's score", output.contains("Player 2: 45"));
        assertTrue("Output should declare Player 2 as winner", output.contains("Player 2 wins!"));
        assertFalse("Output should not declare Player 1 as winner", output.contains("Player 1 wins!"));
        assertFalse("Output should not declare a tie", output.contains("tie"));
    }
    
    /**
     * Test determineWinner when there is a tie
     */
    @Test
    public void testDetermineWinnerTie() {
        outputStream.reset();
        App.determineWinner("Player 1", 40, "Player 2", 40);
        String output = outputStream.toString();
        
        assertTrue("Output should contain 'Game Over'", output.contains("Game Over"));
        assertTrue("Output should contain Player 1's score", output.contains("Player 1: 40"));
        assertTrue("Output should contain Player 2's score", output.contains("Player 2: 40"));
        assertTrue("Output should declare a tie", output.contains("tie"));
        assertFalse("Output should not declare Player 1 as winner", output.contains("Player 1 wins!"));
        assertFalse("Output should not declare Player 2 as winner", output.contains("Player 2 wins!"));
    }
    
    /**
     * Test displayGameState outputs correct information
     */
    @Test
    public void testDisplayGameState() {
        outputStream.reset();
        App.displayGameState("Alice", 15, "Bob", 23);
        String output = outputStream.toString();
        
        assertTrue("Output should contain 'Current Scores'", output.contains("Current Scores"));
        assertTrue("Output should contain Alice's name and score", output.contains("Alice: 15"));
        assertTrue("Output should contain Bob's name and score", output.contains("Bob: 23"));
    }
    
    /**
     * Test displayGameState with zero scores
     */
    @Test
    public void testDisplayGameStateZeroScores() {
        outputStream.reset();
        App.displayGameState("Player 1", 0, "Player 2", 0);
        String output = outputStream.toString();
        
        assertTrue("Output should contain 'Current Scores'", output.contains("Current Scores"));
        assertTrue("Output should contain Player 1 with score 0", output.contains("Player 1: 0"));
        assertTrue("Output should contain Player 2 with score 0", output.contains("Player 2: 0"));
    }
    
    /**
     * Test that rollDie is used correctly (validates the core die rolling logic)
     */
    @Test
    public void testRollDieConsistency() {
        // Test that rollDie consistently returns valid values
        int dieValue = App.rollDie();
        assertTrue("Die value should be valid", dieValue >= 1 && dieValue <= 6);
        
        // Test multiple consecutive rolls
        for (int i = 0; i < 50; i++) {
            int value = App.rollDie();
            assertTrue("All die values should be valid", value >= 1 && value <= 6);
        }
    }
    
    /**
     * Test that rollDie produces different values over multiple calls
     * (This is a probabilistic test - very unlikely to fail if implementation is correct)
     */
    @Test
    public void testRollDieRandomness() {
        boolean foundDifferent = false;
        int firstValue = App.rollDie();
        
        // Check if we get a different value in the next 20 rolls
        for (int i = 0; i < 20; i++) {
            int nextValue = App.rollDie();
            if (nextValue != firstValue) {
                foundDifferent = true;
                break;
            }
        }
        
        // It's extremely unlikely (1 in 6^20) that all 20 rolls are the same
        assertTrue("Should produce different values over multiple rolls", foundDifferent);
    }
    
    /**
     * Test determineWinner with edge case: very high scores
     */
    @Test
    public void testDetermineWinnerHighScores() {
        outputStream.reset();
        App.determineWinner("Player 1", 1000, "Player 2", 999);
        String output = outputStream.toString();
        
        assertTrue("Output should declare Player 1 as winner", output.contains("Player 1 wins!"));
        assertTrue("Output should contain high scores", output.contains("1000"));
        assertTrue("Output should contain high scores", output.contains("999"));
    }
    
    /**
     * Test determineWinner with edge case: very low scores
     */
    @Test
    public void testDetermineWinnerLowScores() {
        outputStream.reset();
        App.determineWinner("Player 1", 1, "Player 2", 2);
        String output = outputStream.toString();
        
        assertTrue("Output should declare Player 2 as winner", output.contains("Player 2 wins!"));
        assertTrue("Output should contain low scores", output.contains("Player 1: 1"));
        assertTrue("Output should contain low scores", output.contains("Player 2: 2"));
    }
}
