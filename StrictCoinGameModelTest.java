package cs3500.hw05;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrictCoinGameModelTest {

    @Test(expected = IllegalArgumentException.class) public void testCreateNegativePlayers() {
        StrictCoinGameModel.create("--OO--", -2);
    }

    @Test public void testGetCurrentPlayerDefaultNames() {
        StrictCoinGameModel game = StrictCoinGameModel.create("--OOO-", 3);
        assertEquals("Player 1", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Player 2", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Player 3", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Player 1", game.getCurrentPlayer());
    }

    @Test public void testGetCurrentPlayerListNames() {
        StrictCoinGameModel game = StrictCoinGameModel
            .create("---OOOO-----O", "Sally", "Sigmund", "Steven", "Susan", "Sue", "Stefan");
        assertEquals("Sally", game.getCurrentPlayer());
        assertEquals("Sally", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Sigmund", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Steven", game.getCurrentPlayer());
        game.nextTurn();
        assertEquals("Susan", game.getCurrentPlayer());
    }

    @Test(expected = IllegalStateException.class) public void testGetWinnerGameNotOver() {
        StrictCoinGameModel.create("OO--OO").getWinner();
    }

    @Test public void testGetWinner() {
        StrictCoinGameModel game = StrictCoinGameModel.create("OOO--O", "Rick", "Rolled");
        game.playerTurn(3, 4);
        assertEquals("OOO-O-", game.toString());
        game.playerTurn(3, 3);
        assertEquals("OOOO--", game.toString());
        assertEquals("Rolled", game.getWinner());
    }

    @Test public void testGetWinnerNoMovesToBeginWith() {
        assertEquals("Joe",
            StrictCoinGameModel.create("OOOO----", "Joe", "Steve", "Bob").getWinner());
    }

    @Test public void testPlayerTurn() {
        StrictCoinGameModel game = StrictCoinGameModel.create("--OO--OO", "Belzabub",
            "Freddie Mercury", "James Earl Jones");
        assertEquals("Belzabub", game.playerTurn(0, 1));
        assertEquals("-O-O--OO", game.toString());
        assertEquals("Freddie Mercury", game.playerTurn(2, 5));
        assertEquals("-O-O-O-O", game.toString());
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testPlayerTurnGameOver() {
        StrictCoinGameModel.create("OOOO").playerTurn(1, 2);
    }

    @Test (expected = NullPointerException.class)
        public void testAddNullPlayer() {
            StrictCoinGameModel.create("OO-OO").addPlayer(null);
        }

    @Test (expected = IllegalArgumentException.class)
    public void testAddPlayerOutsideBounds() {
        StrictCoinGameModel.create("OO--OO").addPlayer("Sam", 3);
    }

    @Test public void testAddPlayer() {
        StrictCoinGameModel game = StrictCoinGameModel.create("-----O", "p1", "p2");
        game.addPlayer("Steve Urkle");
        assertEquals("p1", game.playerTurn(0, 4));
        assertEquals("p2", game.playerTurn(0, 3));
        game.addPlayer("Some guy");
        assertEquals("Steve Urkle", game.playerTurn(0, 2));
        assertEquals("Some guy", game.playerTurn(0, 1));
        assertEquals("p1", game.playerTurn(0, 0));
    }

    @Test public void testAddPlayerInMiddle() {
        StrictCoinGameModel game = StrictCoinGameModel.create("----OO", 4);
        assertEquals("Player 1", game.playerTurn(0, 3));
        game.addPlayer("New Player", 1);
        assertEquals("New Player", game.playerTurn(0, 2));
        assertEquals("Player 2", game.playerTurn(0, 1));
    }

    @Test public void testAddPlayerTwoAtOnce() {
        StrictCoinGameModel game = StrictCoinGameModel.create("----OOO");
        assertEquals("Player 1", game.playerTurn(0, 1));
        game.addPlayer("New Player 1", 0);
        game.addPlayer("New Player 2", 1);
        assertEquals("New Player 2", game.playerTurn(0, 0));
        assertEquals("Player 1", game.playerTurn(1, 4));
        assertEquals("Player 2", game.playerTurn(1, 3));
        assertEquals("New Player 1", game.playerTurn(1, 2));
        assertEquals("New Player 2", game.playerTurn(1, 1));
    }

    @Test public void testBoardSize() {
        assertEquals(5, cgm("-O-O-").boardSize());
    }

    @Test public void testBoardSize0() {
        assertEquals(0, cgm("").boardSize());
    }

    @Test public void testCoinCountNoCoins() {
        assertEquals(0, cgm("-----").coinCount());
    }

    @Test public void testCoinCount() {
        assertEquals(3, cgm("--O-O--O---").coinCount());
    }

    @Test public void testCoinCountAllCoints() {
        assertEquals(4, cgm("OOOO").coinCount());
    }

    @Test public void testGetCoinPositionFirstCoin() {
        assertEquals(0, cgm("O---").getCoinPosition(0));
    }

    @Test public void testGetCoinPositionNotFirstCoin() {
        assertEquals(3, cgm("---O-").getCoinPosition(0));
    }

    @Test public void testGetCoinPositionMultipleCoins() {
        assertEquals(3, cgm("-O-O--O").getCoinPosition(1));
    }

    @Test public void testGetCoinPositionMultipleCoins2() {
        assertEquals(6, cgm("-OOO-OO").getCoinPosition(4));
    }

    @Test public void testGetCoinPositionAllCoins() {
        assertEquals(4, cgm("OOOOO").getCoinPosition(4));
    }

    @Test(expected = IllegalArgumentException.class) public void testGetCoinPositionNoCoins() {
        cgm("------").getCoinPosition(0);
    }

    @Test(expected = IllegalArgumentException.class) public void testGetCoinPositionOutOfBounds() {
        cgm("OOO").getCoinPosition(3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCoinPositionNotEnoughCoins() {
        cgm("-O--O-").getCoinPosition(2);
    }

    @Test public void testIsGameOver() {
        assertEquals(true, cgm("OOO----").isGameOver());
    }

    @Test public void testIsGameOverNoCoins() {
        assertEquals(true, cgm("------").isGameOver());
    }

    @Test public void testIsGameOverAllCoins() {
        assertEquals(true, cgm("OOOOO").isGameOver());
    }

    @Test public void testIsGameOverNotDone1() {
        assertEquals(false, cgm("-O---O-O-O").isGameOver());
    }

    @Test public void testIsGameOverBlockOfCoins() {
        assertEquals(false, cgm("-OOOO").isGameOver());
    }

    @Test public void testIsGameOverAlmostDone() {
        assertEquals(false, cgm("OOOOO-O---").isGameOver());
    }

    @Test public void testGenerateBoard() {
        assertEquals("---O-O-O-O-", cgm("---O-O-O-O-").toString());
    }

    @Test public void testGenerateBoardEmpty() {
        assertEquals("----", cgm("----").toString());
    }

    @Test public void testGenerateBoardFull() {
        assertEquals("OOOO", cgm("OOOO").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateBoardIllegalCharecters() {
        cgm("-O--0--");
    }

    @Test public void testMoveSimple() {
        CoinGameModel game = cgm("--O-");
        game.move(0,1);
        assertEquals("-O--", game.toString());
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNoCoins() {
        CoinGameModel game = cgm("-----");
        game.move(0, 1);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveCoinInNewPosition() {
        CoinGameModel game = cgm("-O--O");
        game.move(1, 1);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNewPositionNegative() {
        CoinGameModel game = cgm("O---O--O--OO--");
        game.move(1, -1);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNewPositionOutOfBounds() {
        CoinGameModel game = cgm("OO--OO");
        game.move(3, 10);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNewPositionOutOfBoundsEdgeCase() {
        CoinGameModel game = cgm("OO--OO-");
        game.move(3, 7);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNewPositionToRight() {
        CoinGameModel game = cgm("-O-");
        game.move(0, 2);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveNoCoinAtIndex() {
        CoinGameModel game = cgm("O-OO--");
        game.move(3, 1);
    }

    private CoinGameModel cgm(String board) {
        return StrictCoinGameModel.create(board);
    }

    @Test (expected = CoinGameModel.IllegalMoveException.class)
    public void testMoveThroughCoinStrict() {
        CoinGameModel game = cgm("--O--O");
        game.move(1, 1);
    }

    @Test public void testMultiStepMove() {
        CoinGameModel game = cgm("-O---O");
        game.move(0, 0);
        assertEquals("O----O", game.toString());
        game.move(1, 1);
        assertEquals("OO----", game.toString());
    }
}

