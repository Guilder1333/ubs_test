package org.ubs.test;

import javax.annotation.Nonnull;
import javax.naming.OperationNotSupportedException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.UnexpectedException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Game renderer which uses console for UI.
 */
public class ConsoleGameRenderer implements Renderer {
    @Nonnull
    private final Game game_;
    @Nonnull
    private final Rules rules_;
    @Nonnull
    private final Player[] players_;
    private static final String[] PLAYER_LETTERS = new String[] {" ", "G", "R"};
    private static final String[] PLAYER_NAMES = new String[] {"GREEN", "RED"};

    public ConsoleGameRenderer(@Nonnull Game game, @Nonnull Rules rules) {
        game_ = game;
        rules_ = rules;
        if (rules.GetPlayersNumber() != 2) {
            throw new IllegalArgumentException("Rules contain invalid value PlayersNumber = '" + rules.GetPlayersNumber() + "'");
        }
        players_ = new Player[rules.GetPlayersNumber()];
        Callable<Integer> readInputCallback_ = this::ReadInput;
        for(int i = 0; i < players_.length; i++) {
            players_[i] = new ConsoleLocalPlayer(PLAYER_NAMES[i], readInputCallback_);
        }
    }

    public void RunGame() throws UnexpectedException {
        game_.StartGame();

        for(Player player : players_) {
            player.GameStarted();
        }

        while (game_.IsPlaying()) {
            Render();
            Player player = players_[game_.GetCurrentPlayer()];
            Future<Integer> choiceFuture = player.RequestChoice();
            int input = 0;
            try {
                input = choiceFuture.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new UnexpectedException("Failed to read input from console.", e);
            }
            if (input == -1)
                break;
            try {
                game_.MakeTurn(input);
            } catch (OperationNotSupportedException e) {
                throw new UnexpectedException("Game was terminated unexpectedly", e);
            }
        }

        if (game_.GetWinner() >= 0) {
            Render();
            System.out.println("Player "+ (game_.GetWinner() + 1) + " [" + players_[game_.GetWinner()].GetPlayerName() + "] wins!");
        } else {
            System.out.println("Game is ended");
        }
    }

    private int ReadInput() throws IOException {
        final int playerIndex = game_.GetCurrentPlayer();
        System.out.print("Player " + (playerIndex + 1) + " [" + players_[playerIndex].GetPlayerName() + "] - choose column (1-7):");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String input = reader.readLine();
            if (input.equals("x")) {
                return -1;
            }
            int column = Integer.parseInt(input) - 1;
            if (game_.CanSelectColumn(column)) {
                return column;
            }
            System.out.println("Please select proper column");
        }
    }

    private void Render() {
        int width = rules_.GetBoardWidth();
        int height = rules_.GetBoardHeight();
        for(int x = 0; x < width; x++) {
            System.out.print(' ');
            System.out.print(x + 1);
        }
        System.out.println();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                System.out.print('|');
                int boardValue = game_.GetBoardValue(x, y);
                System.out.print(PLAYER_LETTERS[boardValue + 1]);
            }
            System.out.println('|');
        }
    }
}
