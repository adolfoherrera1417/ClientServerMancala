import java.util.Scanner;
import java.util.*;

public class GamePlay {

  // Integer stating which key the last marble landed on
  private int key_lastLoc = 2;

  // Side of board with empty pieces
  private int emptySide = -1;

  // Establish a new game board
  GameBoard kalahBoard = new GameBoard();

  // Set up adjacent spaces for piece stealing
  private HashMap<Integer, Integer> adjacentSpace = new HashMap<Integer, Integer>();

  // Helper variable for indexing spaces 1 - 6 on the board for player 2
  private HashMap<Integer, Integer> spaceIndexP2 = new HashMap<Integer,Integer>();

  // Establish default unused players
  Player player1 = new Player(false, -1); // Human Player 1
  Player player2 = new Player(false, -1); // Human Player 2
  AI compPlayer = new AI(-1); // Computer Player

  // Integer deciding game type
  int gameType = 2; // 1 indicated PvE, 2 indicated PvP

  /**
  * Default Constructor
  */
  public GamePlay() {
    // Set up spaces across from each other
    SpaceIndexing();
    AdjacentSpace();
  }

  /**
  * Game running function, will move through all moves until game is
  * completed
  */
  public void PlayGame() {
//    // Set up game type
//    EstablishPlayers();
//
//    kalahBoard.PrintBoard(); // Initial board
//
//    boolean endGame = false;
//    while (!endGame) {
//      // Player 1 Move
//      do {
//        MovePickHelper(1); // Pick a piece
//        if (CanStealPieces(1)) { // Check if a piece can be stolen
//          StealPieces(1);
//        }
//        kalahBoard.PrintBoard(); // Print the current board state
//        endGame = IsGameOver(); // Check if the game is over
//      } while (HasExtraTurn(1) && !endGame);
//
//      if (endGame) {
//        break;
//      }
//
//      // Player 2 Move
//      do {
//        MovePickHelper(2);
//        if (CanStealPieces(2)) {
//          StealPieces(2);
//        }
//        kalahBoard.PrintBoard(); // Print the current board state
//        endGame = IsGameOver();
//      } while (HasExtraTurn(2) && !endGame);
//
//      //endGame = true;
//    }
//
//    RemainingPieces();
//    kalahBoard.PrintBoard();
//    int gameWinner = CalculateScore();
//    PrintWinner(gameWinner);
  }

  public void ShowBoard(){
    kalahBoard.PrintBoard();
  }
  /**
   * Take move by method call
   */

  public boolean playerMove(int player, int move) {
    MovePickHelper(player, move);
    if (CanStealPieces(player)) { // Check if a piece can be stolen
      StealPieces(player);
    }
    kalahBoard.PrintBoard();
    if (HasExtraTurn(player)) {
      return true;
    }
    return false;
  }

  /**
  * Set up the game type and its players
  */
  private void EstablishPlayers() {
    Scanner playerInput = new Scanner(System.in);

    do {
      // Game messages
      System.out.println("Select Game Type: ");
      System.out.println("1. Player vs Computer");
      System.out.println("2. Player vs Player\n");

      gameType = playerInput.nextInt();

    } while (gameType < 1 || gameType > 2);

    switch(gameType) {
      case 1: /* PLAYER VERSUS COMPUTER */
        player1.SetPlayer(1);
        compPlayer.SetPlayer(2);
        break;
      case 2: /* PLAYER VERSUS PLAYER */
        player1.SetPlayer(1);
        player2.SetPlayer(2);
        break;
    }
  }

  /**
  * Allow the player to select a space
  */
  private void PlayerPick(int player, int position) {
    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();
    //Scanner playerInput = new Scanner(System.in);

//    // Prompt Player
//    switch (player) {
//      case 1:
//        System.out.println("Player 1: ");
//        break;
//      case 2:
//        System.out.println("Player 2: ");
//        break;
//    }

    // Retrieve location and wait for good input
    //boolean goodSpot = false;
    //do {
     // System.out.println("Pick a spot 1 - 6: ");
      //position = playerInput.nextInt();

      // Bad Index
      //if (position > 0 && position < 7) {
       // goodSpot = true;

        // Empty space
//        switch(player) {
//          case 1:
//            if (gameBoard.get(position + 1) == 0) {
//              //goodSpot = false;
//            }
//            break;
//          case 2:
//            if (gameBoard.get(spaceIndexP2.get(position + 1)) == 0) {
//              //goodSpot = false;
//            }
//            break;
       // }
      //}

    //} while (!goodSpot);

    // Actual index of the board position
    int boardIndex = position + 1;
    DistributePieces(player, boardIndex, gameBoard.get(boardIndex));
  }

  /**
  * Allow the computer to select a space
  */
  private void ComputerPick(int player) {
    /*
    TODO: Sprint 1 - Rand Function | Sprint 2 - MiniMax Variation
      Prompt computer for a valid move
      - If marbles exists: Distribute marbles
      - else: Prompt for a new space
    */
  }

  /**
  * Do action of distributing pieces from selected position
  * Inputs: The current player doing the move, the chosen position,
  * and the number of pieces to distribute
  */
  private void DistributePieces(int player, int boardSpace, int numPieces) {
    // Retrieve the board
    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    switch (player) {
      case 1:
        // Set current slot to zero
        gameBoard.put(boardSpace, 0);
        boardSpace++;

        // Distribute
        for (int piece = 0; piece < numPieces; piece++) {
          if (boardSpace == 15) { // Off the board
            boardSpace = 2;
          }
          gameBoard.put(boardSpace, gameBoard.get(boardSpace) + 1);
          boardSpace++;
        }
        break;
      case 2:
        // Retrieve equivalent space
        boardSpace = spaceIndexP2.get(boardSpace);
        numPieces = gameBoard.get(boardSpace);

        // Set current slot to zero
        gameBoard.put(boardSpace, 0);
        boardSpace++;

        // Distribute
        for (int piece = 0; piece < numPieces; piece++) {
          if (boardSpace == 8) { // Player 1's Kalah
            boardSpace++;
          }
          else if (boardSpace == 15) { // Off the board
            boardSpace = 1;
          }

          gameBoard.put(boardSpace, gameBoard.get(boardSpace) + 1);
          boardSpace++;
        }
        break;
    }

    // Establish last key slot: loops above will move it one ahead
    key_lastLoc = boardSpace - 1;

    // UpdateBoard
    kalahBoard.UpdateBoard(gameBoard);
  }

  /**
  * Check to see if pieces are able to be stolen and added to the kalah
  * Output: Boolean stating whether steal is done
  */
  private boolean CanStealPieces(int player) {
    boolean canStealPieces = false;

    // Collect board
    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    switch (player) {
      case 1:
        // In player 1's spaces
        if (1 < key_lastLoc && key_lastLoc < 8) {
          // Last space was an empty space
          if (gameBoard.get(key_lastLoc) == 1) {
            canStealPieces = true;
          }
        }
        break;
      case 2:
        // In player 2's spaces
        if (8 < key_lastLoc && key_lastLoc < 15) {
          // Last space was an empty space
          if (gameBoard.get(key_lastLoc) == 1) {
            canStealPieces = true;
          }
        }
        break;
    }

    return canStealPieces;
  }

  /**
  * Do the action of stealing the opponents pieces and adding to
  * current player's Kalah
  * Input: Current current player performing steal
  */
  private void StealPieces(int player) {
    // Grab current board state
    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    // Index of front adjacent spot
    int spotInFront = adjacentSpace.get(key_lastLoc);

    // Check to see if space in front isnt zero
    if (gameBoard.get(spotInFront) != 0) {
      // Total captured
      int capturedCount = gameBoard.get(key_lastLoc) + gameBoard.get(spotInFront);

      // Set spots to zero
      gameBoard.put(spotInFront, 0);
      gameBoard.put(key_lastLoc, 0);

      // Steal according to player
      switch (player) {
        case 1:
          gameBoard.put(8, capturedCount + gameBoard.get(8));
          break;
        case 2:
          gameBoard.put(1, capturedCount + gameBoard.get(8));
          break;
      }
    }

    // Update the board
    kalahBoard.UpdateBoard(gameBoard);
  }

  /**
  * Determine whether an extra turn is given
  * Output: Return whether or not an extra turn is given
  */
  private boolean HasExtraTurn(int player) {
    boolean hasExtraTurn = false;

    switch (player) {
      case 1:
        if (key_lastLoc == 8) {
          hasExtraTurn = true;
        }
        break;
      case 2:
        if (key_lastLoc == 1) {
          hasExtraTurn = true;
        }
        break;
    }

    return hasExtraTurn;
  }

  /**
  * Check to see if the game is in a state where no more moves
  * are possible
  * Output: Boolean stating whether the game has more possible moves
  */
  private boolean IsGameOver() {
    boolean isGameOver = false;
    int iterations = 16;

    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    // Check player one's side for remaining pieces
    int counter = 0;
    for (int space = 2; space < iterations; space++) {
      switch (space) {
        case 8: // Player 1's Kalah index
          if (counter == 6) {
            isGameOver = true;
            emptySide = 1;
          }
          counter = 0;
          break;
        case 15: // After Player 2's spaces
          if (counter == 6) {
            isGameOver = true;
            emptySide = 2;
          }
          break;
        default:
          if(gameBoard.get(space) == 0) {
            counter++;
          }
          break;
      }
    }

    return isGameOver;
  }

  /**
  * Game Rule: When the game is over, all remaining pieces on one player's
  * side of the board are added to their Kalah
  */
  private void RemainingPieces() {
    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    int captureCount = 0;
    switch (emptySide) {
      case 1: // Player 1 side empty
        for(int space = 9; space < 15; space++) {
          captureCount += gameBoard.get(space);
          gameBoard.put(space, 0);
        }
        gameBoard.put(1, captureCount); // Add to P2 Kalah
        break;
      case 2: // Player 2 side empty
        for(int space = 2; space < 8; space++) {
          captureCount += gameBoard.get(space);
          gameBoard.put(space, 0);
        }
        gameBoard.put(8, captureCount); // Add to P1 Kalah
        break;
    }

    kalahBoard.UpdateBoard(gameBoard);
  }

  /**
  * Determine the winner of the game
  * Output: Integer: 0 - Indicating Player 1 Win, 1 - Player 2 Win
  */
  private int CalculateScore() {
    int gameWinner = 0;

    HashMap<Integer, Integer> gameBoard = kalahBoard.GetBoard();

    int playerOneScore = gameBoard.get(8);
    int playerTwoScore = gameBoard.get(1);

    if (playerOneScore > playerTwoScore) {
      gameWinner = 1;
    }
    else if (playerTwoScore > playerOneScore) {
      gameWinner = 2;
    }
    else {
      gameWinner = 3;
    }

    return gameWinner;
  }

  /**
  * Print message saying who won
  * Input: Integer indicating which player won
  */
  private void PrintWinner(int winner) {
    switch (winner) {
      case 1:
        System.out.println("Player 1 Wins!");
        break;
      case 2:
        System.out.println("Player 2 Wins!");
        break;
      default:
        System.out.println("It's a Tie!");
        break;
    }
  }

  /**
  * Helper for picking the next piece pick based on game type
  * Input: Number indicating which player is currently acting
  */
  private void MovePickHelper(int player, int move) {
    // Move based on game type
    switch (gameType) {
      case 1:
        if (player == 1) {
          PlayerPick(player,move);
        }
        else {
          ComputerPick(player);
        }
        break;
      case 2:
        if (player == 1) {
          PlayerPick(1,move);
        }
        else {
          PlayerPick(2,move);
        }
        break;
    }
  }

  /**
  * Set up 1 - 6 index for player 2
  */
  private void SpaceIndexing() {
    int lastSpace = 14; // Player 2's last position

    // Insert equivalences
    for (int space = 2; space < 8; space++) {
      spaceIndexP2.put(space, space + 7);
    }
  }

  /**
  * Set up adjacent spaces for space stealing
  */
  private void AdjacentSpace() {
    int lastSpaceP2 = 14; // Player 2's last position

    // Space across from key: Player 1 side
    for (int key = 2; key < 8; key++) {
      adjacentSpace.put(key, lastSpaceP2 - key + 2);
    }

    // Spce across from key: Player 2 side
    int index = 1;
    for (int key = 9; key < 15; key++) {
      adjacentSpace.put(key, key - (2 * index));
      index++;
    }
  }

} // End class
