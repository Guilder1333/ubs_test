package org.ubs.test.tests;

import org.junit.Assert;
import org.junit.Test;
import org.ubs.test.Game;
import org.ubs.test.Rules;

import javax.naming.OperationNotSupportedException;

public class GameTests {
    public static class TestRules implements Rules {

        @Override
        public int GetFirstPlayer() {
            return 0;
        }

        @Override
        public int GetPlayersNumber() {
            return 2;
        }

        @Override
        public int GetBoardWidth() {
            return 7;
        }

        @Override
        public int GetBoardHeight() {
            return 6;
        }

        @Override
        public int GetWinLineSize() {
            return 4;
        }
    }

    @Test
    public void StartingState() {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        Assert.assertFalse(game.IsPlaying());

        boolean MakeTurnFailed = true;
        try {
            game.MakeTurn(0);
            MakeTurnFailed = false;
        } catch (OperationNotSupportedException e) {
            // Expected
        }
        Assert.assertTrue(MakeTurnFailed);
    }

    @Test
    public void StartGame() {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        Assert.assertTrue(game.IsPlaying());
        Assert.assertEquals(game.GetWinner(), Game.INVALID);
        Assert.assertEquals(game.GetCurrentPlayer(), rules.GetFirstPlayer());
    }

    @Test
    public void CanSelectColumnValid() {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        for(int i = 0; i < rules.GetBoardWidth(); i++) {
            Assert.assertTrue(game.CanSelectColumn(i));
        }
    }

    @Test
    public void MakeFirstTurn() throws OperationNotSupportedException {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        Assert.assertTrue(game.CanSelectColumn(0));
        int playerIndex = game.GetCurrentPlayer();
        game.MakeTurn(0);

        Assert.assertEquals(playerIndex, game.GetBoardValue(0, rules.GetBoardHeight() - 1));
    }

    @Test
    public void CanSelectColumnInvalid() throws OperationNotSupportedException {
        Rules rules = new TestRules() {
            @Override
            public int GetBoardHeight() {
                return 1;
            }
        };
        Game game = new Game(rules);
        game.StartGame();

        Assert.assertTrue(game.CanSelectColumn(0));
        int playerIndex = game.GetCurrentPlayer();
        game.MakeTurn(0);

        Assert.assertEquals(playerIndex, game.GetBoardValue(0, rules.GetBoardHeight() - 1));

        Assert.assertFalse(game.CanSelectColumn(0));
    }

    @Test
    public void WinGameVertical() throws OperationNotSupportedException {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        // turn 1
        game.MakeTurn(0);
        game.MakeTurn(1);
        // turn 2
        game.MakeTurn(0);
        game.MakeTurn(1);
        // turn 3
        game.MakeTurn(0);
        game.MakeTurn(1);
        // turn 4
        game.MakeTurn(0);

        Assert.assertEquals(game.GetWinner(), 0);
        Assert.assertEquals(game.GetWinLane(), Game.WIN_LANE_VERTICAL);
    }

    @Test
    public void WinGameHorizontal() throws OperationNotSupportedException {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        // turn 1
        game.MakeTurn(0);
        game.MakeTurn(0);
        // turn 2
        game.MakeTurn(1);
        game.MakeTurn(1);
        // turn 3
        game.MakeTurn(2);
        game.MakeTurn(2);
        // turn 4
        game.MakeTurn(3);

        Assert.assertEquals(game.GetWinner(), 0);
        Assert.assertEquals(game.GetWinLane(), Game.WIN_LANE_HORIZONTAL);
    }

    @Test
    public void WinGameDiagonalFromTop() throws OperationNotSupportedException {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        // turn 1
        game.MakeTurn(0);
        game.MakeTurn(0);
        // turn 2
        game.MakeTurn(0);
        game.MakeTurn(0);
        // turn 3
        game.MakeTurn(0);
        game.MakeTurn(1);
        // turn 4
        game.MakeTurn(1);
        game.MakeTurn(1);
        // turn 5
        game.MakeTurn(1);
        game.MakeTurn(2);
        // turn 6
        game.MakeTurn(2);
        game.MakeTurn(3);
        // turn 7
        game.MakeTurn(2);
        game.MakeTurn(5);
        // turn 8
        game.MakeTurn(3);

        Assert.assertEquals(game.GetWinner(), 0);
        Assert.assertEquals(game.GetWinLane(), Game.WIN_LANE_DIAGONAL_TO_BOTTOM);
    }

    @Test
    public void WinGameDiagonalFromBottom() throws OperationNotSupportedException {
        Rules rules = new TestRules();
        Game game = new Game(rules);
        game.StartGame();

        // turn 1
        game.MakeTurn(0);
        game.MakeTurn(1);
        // turn 2
        game.MakeTurn(1);
        game.MakeTurn(2);
        // turn 3
        game.MakeTurn(2);
        game.MakeTurn(3);
        // turn 4
        game.MakeTurn(2);
        game.MakeTurn(3);
        // turn 5
        game.MakeTurn(4);
        game.MakeTurn(3);
        // turn 6
        game.MakeTurn(3);

        Assert.assertEquals(game.GetWinner(), 0);
        Assert.assertEquals(game.GetWinLane(), Game.WIN_LANE_DIAGONAL_TO_TOP);
    }
}
