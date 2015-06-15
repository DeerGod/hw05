package cs3500.hw05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * You don't need to implement this class or any concrete subclasses
 * for pset03.
 */
public final class StrictCoinGameModel implements CoinGameModel {
    // (Exercise 2) Declare the fields needed to support the methods in
    // the interface you’ve designed:
    private int currentPlayer;  // the current player
    private ArrayList<String> playerNames;  //list of the player's names with each player's turn
    private boolean[] board;
    //order equal to their index


    // (Exercise 3) Describe, as precisely as you can, your
    // representation’s class invariants:
    //(1):   currentPlayer >= 0
    //(2):   currentPlayer < playerNames.size()
    //(1/2): currentPlayer [0, playerNames.size())
    //(3):   currentPlayer % playerNames.size() = currentPlayer
    //(4):   elements of playerNames cannot be null

    // (Exercise 4) Describe your constructor API here by filling in
    // whatever arguments you need and writing good Javadoc. (You may
    // declare any combination of constructors and static factory
    // methods that you like, but you need not get fancy.)

    // ASSUMPTIONS:
    // (1) The representation for a player is an String
    // (2) There will not be more than Integer.MAX_VALUE players
    // (3) There will always be at least 1 player in the game
    // (4) New players will not go out of turn
    // (5) Players will only be allowed to make a move when it is there turn (i.e. the players go
    //     in a fixed turn order)

    /**
     * Generates a new StrictCoinGameModel out of a string representation of the board
     * the string representation must only contain the charecters '0' and '-' to represent
     * coins and empty spaces respectively.
     *
     * @param board the string representation of the board
     * @throws IllegalArgumentException if the string contains charecters other than '0' and '-'
     */
    private StrictCoinGameModel(String board, String... names) {
        currentPlayer = 0;
        generateBoard(board);
        playerNames = new ArrayList<String>(Arrays.asList(names));
    }

    /**
     * Constructor that allows the client to pick the names of the starting players,
     * and the string representation of the game board
     *
     * @param gameBoard the string representation of the game board
     * @param names     the names of the players in the game, with the turn order corresponding
     *                  to the order of the names
     * @return a new strict coin game model object
     * @throws IllegalArgumentException if the string contains characters other than '0' and '-'
     */
    public static StrictCoinGameModel create(String gameBoard, String... names) {
        return new StrictCoinGameModel(gameBoard, names);
    }

    /**
     * Constructor that defaults the current player to 0, the first player, and defaults all the
     * names to Player X, where x is the player's turn number plus 1 (i.e. Player 1, Player 2, ect.)
     *
     * @param numberPlayers the total number of players in the game
     * @param gameBoard     the string representation of the game
     * @return a new strict coin game model object
     * @throws IllegalArgumentException if the number of players is <= 0
     * @throws IllegalArgumentException if the string contains characters other than '0' and '-'
     */
    public static StrictCoinGameModel create(String gameBoard, int numberPlayers) {
        if (numberPlayers < 0) {
            throw new IllegalArgumentException("numberPlayers must be >= 0");
        }
        String[] names = new String[numberPlayers];
        for (int i = 0; i < numberPlayers; ++i) {
            names[i] = "Player " + (i + 1);
        }
        return new StrictCoinGameModel(gameBoard, names);
    }

    /**
     * Constructor that defaults the number of players to 2, and the current player to 0, and the
     * names of the players to player 1 and player 2
     *
     * @param gameBoard the string representation of the game
     * @return a new strict coin game model object
     * @throws IllegalArgumentException if the string contains characters other than '0' and '-'
     */
    public static StrictCoinGameModel create(String gameBoard) {

        return new StrictCoinGameModel(gameBoard, "Player 1", "Player 2");
    }

    /**
     * This is a redundant constructor, the user can simply change the order of names rather than
     * use this method
     *
     * @param gameBoard the string representation of the starting board for the game
     * @throws IllegalArgumentException if the string contains characters other than '0' and '-'
     * @throws IllegalArgumentException if the number of players is <= 0
     */
    @Deprecated protected StrictCoinGameModel(int currentPlayer, String gameBoard,
        String... names) {// You don't need to implement this constructor.
        throw new UnsupportedOperationException("no need to implement this");
    }

    @Override public String getCurrentPlayer() {
        return playerNames.get(currentPlayer);
    }

    @Override public String getWinner() {
        if (isGameOver()) {
            return getCurrentPlayer();
        } else {
            throw new IllegalStateException("Game is not yet over");
        }
    }

    @Override public String playerTurn(int coinIndex, int newPosition) {
        move(coinIndex, newPosition);
        String currentPlayer = getCurrentPlayer();
        if (!isGameOver()) {
            nextTurn();
        }
        return currentPlayer;
    }

    @Override public void nextTurn() {
        currentPlayer++;
        currentPlayer %= playerNames.size();
    }

    @Override public void addPlayer(String name) {
        Objects.requireNonNull(name);
        playerNames.add(name);
    }

    @Override public void addPlayer(String name, int turnOrder) {
        Objects.requireNonNull(name);
        if (turnOrder > playerNames.size() || turnOrder < 0) {
            throw new IllegalArgumentException(
                "Turn order must be a positive number less than or equal to the number of players");
        }
        playerNames.add(turnOrder, name);
    }

    /**
     * Generates a new StrictCoinGameModel out of a string representation of the board
     * the string representation must only contain the charecters '0' and '-' to represent
     * coins and empty spaces respectively.
     *
     * @param boardString the string representation of the board
     * @throws IllegalArgumentException if the string contains charecters other than '0' and '-'
     */
    private void generateBoard(String boardString) {
        this.board = new boolean[boardString.length()];
        for (int i = 0; i < boardString.length(); i++) {
            if (boardString.charAt(i) == 'O') {
                this.board[i] = true;
            } else if (boardString.charAt(i) != '-') {
                throw new IllegalArgumentException("Can only use \'O\' and \'-\' in constructor");
            }
        }
    }

    @Override public int boardSize() {
        return board.length;
    }

    @Override public int coinCount() {
        int coinCount = 0;
        for (boolean currentCoin : board) {
            if (currentCoin) {
                coinCount++;
            }
        }
        return coinCount;
    }

    @Override public int getCoinPosition(int coinIndex) {
        for (int i = 0; i < boardSize(); i++) {
            if (board[i]) {
                if (coinIndex > 0) {
                    coinIndex--;
                } else {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("No coin with that index");
    }

    /**
     * This is used to check if a move call is ill-formed (i.e. being called in some way that it
     * shouldn't, such as if there is a coin in the new position, the new position is outside of the
     * bounds of the board, if there is no coin with the given index, or if the new position is to
     * the right of the old position.)
     *
     * @param coinIndex   which coin to move
     * @param newPosition where to move it to
     * @throws cs3500.hw05.CoinGameModel.IllegalMoveException if the move is ill formed
     */
    private boolean checkIllFormedMoveCall(int coinIndex, int newPosition) {
        //checks to see if new position is within the bounds of the board
        if (newPosition < 0 || newPosition >= boardSize()) {
            throw new IllegalMoveException("No segment at that position");
        }
        // checks to see if the new position has a coin in it
        else if (board[newPosition]) {
            throw new IllegalMoveException("New position has a coin in it");
        }
        // checks to see if there is a coin in the coin index
        try {
            getCoinPosition(coinIndex);
        } catch (IllegalArgumentException ex) {
            throw new IllegalMoveException("There is no coin that corresponds to that index");
        }
        int oldPosition = getCoinPosition(coinIndex);
        // checks to see if the new position is to the left of the old position
        if (newPosition > oldPosition) {
            throw new IllegalMoveException("Coins can only move left");
        }
        return true;
    }

    @Override public boolean isGameOver() {
        for (int i = 0; i < coinCount(); i++) {
            if (!board[i]) {
                return false;
            }
        }
        return true;
    }

    @Override public void move(int coinIndex, int newPosition) {
        checkIllFormedMoveCall(coinIndex, newPosition);
        int oldPosition = getCoinPosition(coinIndex);
        // checks to see if there are no coins in between the new position and the old position
        for (int i = newPosition; i < oldPosition; i++) {
            if (board[i]) {
                throw new IllegalMoveException("Coins cannot pass through one another");
            }
        }

        board[oldPosition] = false;
        board[newPosition] = true;
    }

    /**
     * Gives the string representation of this CoinGameModel,
     * with O representing a coin, and - representing an empty segment
     *
     * @return the string representation of this CoinGameModel
     */
    @Override public String toString() {
        String boardString = "";
        for (boolean currentSquare : board) {
            if (currentSquare) {
                boardString += "O";
            } else {
                boardString += "-";
            }
        }
        return boardString;
    }
}
