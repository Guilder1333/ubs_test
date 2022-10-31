package org.ubs.test;

import javax.annotation.Nonnull;
import java.util.concurrent.Future;

/**
 * Base player interface.
 */
public interface Player {
    /**
     * Notifies player that game started.
     */
    void GameStarted();

    /**
     * Requests player choice for the next round.
     * @return returns future which will return selected column.
     */
    @Nonnull
    Future<Integer> RequestChoice();

    /**
     * Returns player name.
     */
    @Nonnull
    String GetPlayerName();
}
