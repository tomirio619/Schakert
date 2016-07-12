/*
 * Copyright (C) 2016 Tom Sandmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.tomirio.chessengine.chessboard;

import com.tomirio.chessengine.chesspieces.King;
import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class State implements Serializable {

    /**
     * The number of games won by player black.
     */
    private int gamesWonBlack;

    /**
     * The number of games won by player white.
     */
    private int gamesWonWhite;

    /**
     * Indicates if player black is currently in check.
     */
    private boolean blackIsCheck;

    /**
     * Indicates if player white is currently in check.
     */
    private boolean whiteIsCheck;

    /**
     * Indicates if player black is checkmate.
     */
    private boolean blackIsCheckMate;

    /**
     * Indicates if player white is checkmate.
     */
    private boolean whiteIsCheckMate;

    /**
     * Indicates if it is stalemate.
     */
    private boolean staleMate; // ofwel pat

    /**
     * The winner.
     */
    private ChessColour winner;

    /**
     * The colour that has turn.
     */
    private ChessColour hasTurn;

    /**
     * Constructor of State.
     */
    public State() {
        gamesWonBlack = 0;
        gamesWonWhite = 0;
        blackIsCheck = false;
        whiteIsCheck = false;
        blackIsCheckMate = false;
        whiteIsCheckMate = false;
        staleMate = false;
        winner = null;
        hasTurn = ChessColour.White;
    }

    /**
     * Updates the game state
     *
     * @param board the board
     */
    public void update(ChessBoard board) {
        King blackKing = board.getKing(ChessColour.Black);
        King whiteKing = board.getKing(ChessColour.White);

        if (whiteKing.getPossibleMoves().isEmpty()
                && !whiteKing.isCheck()
                && !board.canMakeAMove(ChessColour.White)
                || blackKing.getPossibleMoves().isEmpty()
                && !blackKing.isCheck()
                && !board.canMakeAMove(ChessColour.Black)) {
            staleMate = true;
        } else if (whiteKing.isCheck()
                && whiteKing.getPossibleMoves().isEmpty()
                && !board.canMakeAMove(ChessColour.White)) {
            winner = ChessColour.Black;
        } else if (blackKing.isCheck()
                && blackKing.getPossibleMoves().isEmpty()
                && !board.canMakeAMove(ChessColour.Black)) {
            winner = ChessColour.White;
        }
        updateTurn();
        updateKings(board);

        if (winner != null) {
            updateScores();
        }
    }

    /**
     * Update the turn.
     */
    public void updateTurn() {
        hasTurn = (hasTurn == ChessColour.White) ? ChessColour.Black : ChessColour.White;
    }

    /**
     * Return for a given colour if this colour is checkmate.
     *
     * @param colour The colour of the player.
     * @return The corresponding checkmate value belonging to that colour.
     */
    public boolean isCheckMate(ChessColour colour) {
        switch (colour) {
            case Black:
                return blackIsCheckMate;
            case White:
                return whiteIsCheckMate;
            default:
                throw new NoSuchElementException();
        }
    }

    public boolean isDraw() {
        return staleMate;
    }

    /**
     *
     * @return <code> True </code> if there is a winner, otherwise <code> False
     * </code>.
     */
    public boolean weHaveAWinner() {
        return winner != null;
    }

    /**
     *
     * @return The value of the variable winner. This value is <code>null</code>
     * if there is no winner at this specific moment.
     */
    public ChessColour getWinner() {
        return winner;
    }

    public ChessColour getTurnColour() {
        return hasTurn;
    }

    /**
     * Updates the status of both kings after a move.
     *
     * @param board the chessboard
     */
    private void updateKings(ChessBoard board) {
        blackIsCheck = board.getKing(ChessColour.Black).isCheck();
        whiteIsCheck = board.getKing(ChessColour.White).isCheck();
        blackIsCheckMate = (winner == ChessColour.Black);
        whiteIsCheckMate = (winner == ChessColour.White);
    }

    /**
     * Updates the scores.
     */
    private void updateScores() {
        switch (winner) {
            case Black:
                gamesWonBlack++;
                break;
            case White:
                gamesWonWhite++;
                break;
        }
    }

}
