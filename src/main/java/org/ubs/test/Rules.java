package org.ubs.test;

/**
 * Provides basic parameters for the game.
 */
public interface Rules {
    /**
     * Returns starting player index.
     */
    int GetFirstPlayer();

    /**
     * Returns number of players.
     */
    int GetPlayersNumber();

    /**
     * Returns board width.
     */
    int GetBoardWidth();

    /**
     * Returns board height.
     */
    int GetBoardHeight();

    /**
     * Returns line size required for winning game.
     */
    int GetWinLineSize();
}
