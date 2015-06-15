package cs3500.hw05;

/**
 * An interface for playing a coin game. The rules of a particular coin game
 * will be implemented by classes that implement this interface.
 * <p>
 */
public interface CoinGameModel {
    /**
     * Returns the integer representation of the current player. When gameOver returns true, this
     * method will return the winner of the game.
     *
     * @return the current player
     */
    String getCurrentPlayer();

    /**
     * Returns the winner of the game (if the game is over)
     *
     * @return the winner of the game
     * @throws IllegalStateException if the method is called before the game is over
     */
    String getWinner();

    /**
     * Enacts the next turn, and attempts to make a move
     *
     * @param coinIndex   the index of the coin the player is attempting to move
     * @param newPosition the new location of the coin
     * @return the number of the player who just played
     * @throws IllegalStateException                          if the game is already over
     * @throws cs3500.hw05.CoinGameModel.IllegalMoveException if the move was invalid
     */
    String playerTurn(int coinIndex, int newPosition);

    /**
     * Increments the next turn, changing the currentPlayer to the next player
     * this does not enact a move, it simply rotates the players so that the current player changes
     */
    void nextTurn();

    /**
     * adds a player to the game. In terms of turn order the new player will go last, and all other
     * players will keep their turn order
     *
     * @param name      the name of the new player (non-null)
     * @param turnOrder the  location in the turn order of the new player
     *                  (counting players starting at zero)
     * @throws IllegalArgumentException if the turnOrder >= the number of players or turnOrder <= 0
     * @throws NullPointerException     if the name is null
     */
    void addPlayer(String name, int turnOrder);

    /**
     * adds a player to the game. In terms of turn order the new player will go last, and all other
     * players will keep their turn order
     *
     * @param name the name of the new player (non-null)
     * @throws IllegalArgumentException if the name is null
     */
    void addPlayer(String name);

    /**
     * Gets the size of the board (the number of squares)
     *
     * @return the board size
     */
    int boardSize();

    /**
     * Gets the number of coins.
     *
     * @return the number of coins
     */
    int coinCount();

    /**
     * Gets the (zero-based) position of coin number {@code coinIndex}.
     *
     * @param coinIndex which coin to look up
     * @return the coin's position
     * @throws IllegalArgumentException if there is no coin with the requested index
     */
    int getCoinPosition(int coinIndex);

    /**
     * Returns whether the current game is over. The game is over if there are
     * no valid moves.
     *
     * @return whether the game is over
     */
    boolean isGameOver();

    /**
     * Moves coin number {@code coinIndex} to position {@code newPosition}.
     * Throws {@code IllegalMoveException} if the requested move is illegal,
     * which can happen in several ways:
     * <p>
     * <ul>
     * <li>There is no coin with the requested index.
     * <li>The new position is occupied by another coin.
     * <li>There is some other reason the move is illegal,
     * as specified by the concrete game class.
     * </ul>
     * <p>
     * Note that {@code coinIndex} refers to the coins as numbered from 0
     * to {@code coinCount() - 1}, not their absolute position on the board.
     * However, coins have no identity, so if one coin passes another, their
     * indices are exchanged. The leftmost coin is always coin 0, the next
     * leftmost is coin 1, and so on.
     *
     * @param coinIndex   which coin to move (numbered from the left)
     * @param newPosition where to move it to
     * @throws IllegalMoveException the move is illegal
     */
    void move(int coinIndex, int newPosition);

    /**
     * The exception thrown by {@code move} when the requested move is illegal.
     * <p>
     * <p>(Implementation Note: Implementing this interface doesn't require
     * "implementing" the {@code IllegalMoveException} class—it's already
     * implemented right here. Nesting a class within an interface is a way to
     * strongly associate that class with the interface, which makes sense here
     * because the exception is intended to be used specifically by
     * implementations and clients of this interface.)
     */
    static class IllegalMoveException extends IllegalArgumentException {
        /**
         * Constructs a illegal move exception with no description.
         */
        public IllegalMoveException() {
            super();
        }

        /**
         * Constructs a illegal move exception with the given description.
         *
         * @param msg the description
         */
        public IllegalMoveException(String msg) {
            super(msg);
        }
    }
}
