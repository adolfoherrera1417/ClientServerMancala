import java.util.Scanner;
import java.util.*;

public class GameBoard {

  private HashMap<Integer, Integer> gameBoard = new HashMap<Integer, Integer>();
  private int NUM_PIECES = 4;

  /**
  * Constructor for setting up the game board
  */
  public GameBoard() {
    SetUpBoard();
  }

  /**
  * Allow the user to retrieve the current board status
  * Output: The current game board (Map<Integer, List<Integer>>)
  */
  public HashMap<Integer, Integer> GetBoard() {
    return gameBoard;
  }

  /**
  * Update the board status
  * Input: An updated variation of the game board (Map<Integer, List<Integer>>)
  */
  public void UpdateBoard(HashMap<Integer, Integer> newBoard) {
    gameBoard = newBoard;
  }

  /**
  * Will establish the base players and corresponding
  * spaces on the Kalah board via a hash table
  */
  private void SetUpBoard() {
    // Key 1 is Player 2's Kalah
    gameBoard.put(1, 0);

    // Fill up Player 1's pieces
    for(int space = 2; space < 8; space++) {
      gameBoard.put(space, 4);
    }

    // Key 8 is Player 1's Kalah
    gameBoard.put(8,0);

    // Fill up Player 2's pieces
    for(int space = 9; space  < 15; space++) {
      gameBoard.put(space, 4);
    }
  }

  /**
  * FOR TESTING: Check the current board state
  */
  public void PrintBoard() {
    // Print Player 1's Kalah
    System.out.println("\nPlayer 2 Kalah: " + gameBoard.get(1));

    int playerOneSide = 2; // Player 1 Side: 2 - 7
    int playerTwoSide = 14; // Player 2 Side: 9 - 14

    // Print game spaces
    for (int space = 0; space < 6; space++) {
      System.out.println(gameBoard.get(playerOneSide) + " " + gameBoard.get(playerTwoSide));
      playerOneSide++;
      playerTwoSide--;
    }

    // Print Player 2's Kalah
    System.out.println("Player 1 Kalah: " + gameBoard.get(8) + "\n");
  }
} // End class
