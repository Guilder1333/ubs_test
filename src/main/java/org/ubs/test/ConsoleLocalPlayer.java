package org.ubs.test;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Local player to play using console.
 */
public class ConsoleLocalPlayer implements Player {
    @Nonnull
    private final String playerName_;
    @Nonnull
    private final Callable<Integer> requestChoiceCallback_;

    public ConsoleLocalPlayer(@Nonnull String playerName, @Nonnull Callable<Integer> requestChoiceCallback) {
        this.playerName_ = playerName;
        this.requestChoiceCallback_ = requestChoiceCallback;
    }

    @Override
    public void GameStarted() {

    }

    @Override
    @Nonnull
    public Future<Integer> RequestChoice() {
        FutureTask<Integer> future = new FutureTask<>(requestChoiceCallback_);
        future.run();
        return future;
    }

    @Override
    @Nonnull
    public String GetPlayerName() {
        return playerName_;
    }
}
