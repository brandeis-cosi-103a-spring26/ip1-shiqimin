package edu.brandeis.cosi103a.ip1;

import java.util.Random;
import java.util.Scanner;

/**
 * A two-player dice game where players alternate turns,
 * roll a 6-sided die with up to 2 re-rolls per turn,
 * and compete over 10 rounds each.
 */
public class App 
{
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Main game entry point
     */
    public static void main( String[] args )
    {
        System.out.println("=== Welcome to the Dice Game! ===\n");
        
        // Initialize players
        String player1Name = "Player 1";
        String player2Name = "Player 2";
        int player1Score = 0;
        int player2Score = 0;
        
        // Play 10 rounds
        for (int round = 1; round <= 10; round++) {
            System.out.println("--- Round " + round + " ---\n");
            
            // Player 1's turn
            System.out.println(player1Name + "'s turn:");
            int turn1Value = playTurn(player1Name, player1Score);
            player1Score += turn1Value;
            System.out.println(player1Name + " scored " + turn1Value + " points this turn.\n");
            
            // Player 2's turn
            System.out.println(player2Name + "'s turn:");
            int turn2Value = playTurn(player2Name, player2Score);
            player2Score += turn2Value;
            System.out.println(player2Name + " scored " + turn2Value + " points this turn.\n");
            
            // Display current scores
            displayGameState(player1Name, player1Score, player2Name, player2Score);
            System.out.println();
        }
        
        // Determine and display winner
        determineWinner(player1Name, player1Score, player2Name, player2Score);
        
        scanner.close();
    }
    
    /**
     * Simulates rolling a 6-sided die
     * @return A random number between 1 and 6 (inclusive)
     */
    static int rollDie() {
        return random.nextInt(6) + 1;
    }
    
    /**
     * Handles a single player's turn, including initial roll and up to 2 re-rolls
     * @param playerName The name of the current player
     * @param playerScore The current score of the player
     * @return The final die value to be added to the player's score
     */
    static int playTurn(String playerName, int playerScore) {
        int dieValue = rollDie();
        System.out.println("You rolled: " + dieValue);
        
        int rerollCount = 0;
        final int MAX_REROLLS = 2;
        
        while (rerollCount < MAX_REROLLS) {
            String response = getPlayerInput("Do you want to re-roll? (yes/no): ");
            
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("y")) {
                dieValue = rollDie();
                rerollCount++;
                System.out.println("You rolled: " + dieValue);
                
                if (rerollCount < MAX_REROLLS) {
                    System.out.println("You have " + (MAX_REROLLS - rerollCount) + " re-roll(s) remaining.");
                } else {
                    System.out.println("You have no more re-rolls remaining.");
                }
            } else if (response.equalsIgnoreCase("no") || response.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        
        return dieValue;
    }
    
    /**
     * Gets user input from the command line
     * @param prompt The message to display to the user
     * @return The user's input as a string
     */
    static String getPlayerInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Displays the current game state with both players' scores
     * @param player1Name Name of player 1
     * @param score1 Current score of player 1
     * @param player2Name Name of player 2
     * @param score2 Current score of player 2
     */
    static void displayGameState(String player1Name, int score1, String player2Name, int score2) {
        System.out.println("=== Current Scores ===");
        System.out.println(player1Name + ": " + score1);
        System.out.println(player2Name + ": " + score2);
    }
    
    /**
     * Determines and displays the winner of the game
     * @param player1Name Name of player 1
     * @param score1 Final score of player 1
     * @param player2Name Name of player 2
     * @param score2 Final score of player 2
     */
    static void determineWinner(String player1Name, int score1, String player2Name, int score2) {
        System.out.println("\n=== Game Over! ===");
        System.out.println("Final Scores:");
        System.out.println(player1Name + ": " + score1);
        System.out.println(player2Name + ": " + score2);
        
        if (score1 > score2) {
            System.out.println("\n" + player1Name + " wins!");
        } else if (score2 > score1) {
            System.out.println("\n" + player2Name + " wins!");
        } else {
            System.out.println("\nIt's a tie!");
        }
    }
}
