package org.ubs.test;

import javax.annotation.Nonnull;
import javax.naming.OperationNotSupportedException;
import java.util.concurrent.ExecutionException;

public class Game {
    public static final int INVALID = -1;

    public static final int WIN_LANE_VERTICAL = 1;
    public static final int WIN_LANE_HORIZONTAL = 2;
    public static final int WIN_LANE_DIAGONAL_TO_TOP = 3;
    public static final int WIN_LANE_DIAGONAL_TO_BOTTOM = 4;

    @Nonnull
    private final BoardCell[][] board_;
    @Nonnull
    private final Rules rules_;

    private final int width_;
    private final int height_;
    private final int playersNumber_;
    private int playerIndex_ = 0;
    private boolean winnerChecked_ = false;
    private int winner_ = 0;
    private int winLane_ = 0;

    public Game(@Nonnull Rules rules) {
        rules_ = rules;
        width_ = rules.GetBoardWidth();
        height_ = rules.GetBoardHeight();
        board_ = new BoardCell[width_][height_];
        playersNumber_ = rules.GetPlayersNumber();
    }

    /**
     * Returns player number at the cell.
     * @param column board column index for cell value.
     * @param row board row index for cell value.
     * @return Player number is 0 based. INVALID means empty cell.
     */
    public int GetBoardValue(int column, int row) {
        BoardCell cell = board_[column][row];
        if (cell == null)
            return INVALID;
        return cell.Value;
    }

    /**
     * Returns player index for the next MakeTurn call.
     */
    public int GetCurrentPlayer() {
        return playerIndex_;
    }

    /**
     * Returns winner player index. Or INVALID if not decided yet.
     */
    public int GetWinner() {
        return winner_;
    }

    /**
     * Returns win lane type: vertical, horizontal, diagonal left-top to bottom-right, diagonal bottom-left to top-right.
     */
    public int GetWinLane() {
        return winLane_;
    }

    /**
     * Returns true, if winner is not decided yet.
     */
    public boolean IsPlaying() {
        return winner_ < 0;
    }

    /**
     * Initializes or resets the game.
     */
    public void StartGame() {
        winner_ = INVALID;
        winLane_ = 0;
        playerIndex_ = rules_.GetFirstPlayer();
        for(int x = 0; x < width_; x++) {
            for(int y = 0; y < height_; y++) {
                board_[x][y] = null;
            }
        }
    }

    /**
     * Checks if this column is available for the next turn.
     * I.e. inside range and not full.
     * @param column column index.
     * @return true, if column is available.
     */
    public boolean CanSelectColumn(int column) {
        if (column < 0 || column >= width_)
            return false;

        int row = -1;
        for (BoardCell cell : board_[column]) {
            if (cell != null) {
                break;
            }
            row++;
        }
        return row >= 0;
    }

    /**
     * Make next turn with the provided column.
     * Column must be checked by CanSelectColumn previously.
     * @param column column index.
     */
    public void MakeTurn(int column) throws OperationNotSupportedException {
        if (winner_ >= 0) {
            throw new OperationNotSupportedException("Cant make turn after game ended");
        }

        winnerChecked_ = false;
        if (column < 0 || column >= width_) {
            throw new IllegalArgumentException("Argument 'column' has invalid value '" + column + "'");
        }

        int row = -1;
        for (BoardCell cell : board_[column]) {
            if (cell != null) {
                break;
            }
            row++;
        }

        if (row < 0) {
            throw new IllegalArgumentException("Specified column '" + column + "' is not valid for making turn");
        }

        board_[column][row] = new BoardCell(playerIndex_);
        CheckWinner();

        playerIndex_++;
        if (playerIndex_ >= playersNumber_) {
            playerIndex_ = 0;
        }
    }

    /**
     * Checks game field if winning conditions are met.
     */
    public void CheckWinner() {
        if (winnerChecked_)
            return;

        final int winLineSize = rules_.GetWinLineSize();
        final int lastRow = height_ - 1;

        int winner = INVALID;
        for(int x = 0; x < width_; x++) {
            for(int y = lastRow; y >= 0; y--) {
                BoardCell cell = board_[x][y];
                if (cell == null)
                    break;

                if (x > 0) {
                    BoardCell prev = board_[x - 1][y];
                    if (prev != null && cell.Value == prev.Value) {
                        cell.Horizontal = prev.Horizontal + 1;
                        if (cell.Horizontal >= winLineSize) {
                            winner = cell.Value;
                            winLane_ = WIN_LANE_HORIZONTAL;
                            break;
                        }
                    }
                    if (y > 0) {
                        prev = board_[x - 1][y - 1];
                        if (prev != null && cell.Value == prev.Value) {
                            cell.DiagonalToBottom = prev.DiagonalToBottom + 1;
                            if (cell.DiagonalToBottom >= winLineSize) {
                                winner = cell.Value;
                                winLane_ = WIN_LANE_DIAGONAL_TO_BOTTOM;
                                break;
                            }
                        }
                    }
                    if (y < lastRow) {
                        prev = board_[x - 1][y + 1];
                        if (prev != null && cell.Value == prev.Value) {
                            cell.DiagonalToUp = prev.DiagonalToUp + 1;
                            if (cell.DiagonalToUp >= winLineSize) {
                                winner = cell.Value;
                                winLane_ = WIN_LANE_DIAGONAL_TO_TOP;
                                break;
                            }
                        }
                    }
                }
                if (y < lastRow) {
                    BoardCell prev = board_[x][y + 1];
                    if (prev != null && cell.Value == prev.Value) {
                        cell.Vertical = prev.Vertical + 1;
                        if (cell.Vertical >= winLineSize) {
                            winner = cell.Value;
                            winLane_ = WIN_LANE_VERTICAL;
                            break;
                        }
                    }
                }
            }
            if (winner >= 0) {
                break;
            }
        }

        winner_ = winner;
        winnerChecked_ = true;
    }

    private static class BoardCell {
        public final int Value;
        public int DiagonalToBottom = 1;
        public int DiagonalToUp = 1;
        public int Vertical = 1;
        public int Horizontal = 1;

        BoardCell(int value) {
            Value = value;
        }
    }
}
