package org.ubs.test;

import java.util.Random;

/**
 * Default implementation of game rules.
 */
public class DefaultRules implements Rules {

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
