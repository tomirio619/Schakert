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
package com.tomirio.chessengine.game;

import com.tomirio.chessengine.agent.AI;
import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chesspieces.King;
import com.tomirio.chessengine.moves.Move;
import com.tomirio.chessengine.view.Log;
import com.tomirio.chessengine.view.View;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class Game {

    /**
     * Index of the move we have done.
     */
    private int appliedMove;
    /**
     * Indicates if black is check mate.
     */
    private final boolean blackIsCheckMate;
    /**
     * Black player.
     */
    public Player blackPlayer;
    /**
     * The chess board.
     */
    private final ChessBoard chessBoard;
    /**
     * The colour having turn.
     */
    private ChessColour hasTurn;

    /**
     * The log.
     */
    private final Log log;
    /**
     * All the moves that led to the current state of the chess board.
     */
    private final ArrayList<Move> moveList;
    /**
     * Indicates if game is in a stale mate state.
     */
    private boolean staleMate;
    /**
     * The view.
     */
    private final View view;
    /**
     * Indicates if white is check mate.
     */
    private final boolean whiteIsCheckMate;
    /**
     * White player
     */
    public Player whitePlayer;
    /**
     * The colour of the player that wins.
     */
    private ChessColour winner;

    /**
     * Constructor
     *
     * @param chessBoard The chessboard.
     * @param view The view.
     */
    public Game(ChessBoard chessBoard, View view, Log log) {
        this.log = log;
        whiteIsCheckMate = false;
        blackIsCheckMate = false;
        staleMate = false;

        this.chessBoard = chessBoard;
        this.view = view;
        moveList = new ArrayList<>();
        // We have applied any move, so the index of the applied move is -1
        appliedMove = -1;
        hasTurn = ChessColour.White;
        whitePlayer = new HumanPlayer(ChessColour.White, chessBoard);
        blackPlayer = new HumanPlayer(ChessColour.Black, chessBoard);
    }

    /**
     * Calls the right method if an agent has turn.
     */
    public void agentPlay() {
        switch (hasTurn) {
            case Black:
                new Thread((Runnable) blackPlayer).start();
                break;
            case White:
                new Thread((Runnable) whitePlayer).start();
                break;
        }
    }

    /**
     * Do the move.
     */
    public void doMove() {
        if (moveList.size() > 0) {
            // There are moves in the list
            if (appliedMove + 1 < moveList.size()) {
                // There is a next move in the list we can apply
                Move move = moveList.get(appliedMove + 1);
                move.doMove();
                appliedMove++;
                updateTurn();

                view.update(chessBoard);
            }
        }
    }

    /**
     * Get the player that belongs to the given color.
     *
     * @param playerColour The color of the player.
     * @return The player that plays with this color.
     */
    public Player getPlayer(ChessColour playerColour) {
        switch (playerColour) {
            case Black:
                return blackPlayer;
            case White:
                return whitePlayer;
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     * Get the colour of the player that currently has turn.
     *
     * @return ChessColour having turn.
     */
    public ChessColour getTurnColour() {
        return hasTurn;
    }

    /**
     * Handles a chess move done by a human player.
     *
     * @param move The move.
     */
    public void humanPlay(Move move) {
        updateTurn();
        removeObsoleteMoves();
        moveList.add(move);
        log.addMove(move);
        appliedMove++;
        // Only apply the move when we have the String representation
        switch (hasTurn) {
            case Black:
                blackPlayer.makeMove(move);
                break;
            case White:
                whitePlayer.makeMove(move);
                break;
        }
        updateGameStatus();
        view.update(chessBoard);
        updatePlayers();
    }

    /**
     * For a given colour, return if this colour is checkmate.
     *
     * @param colour The colour of the player.
     * @return <code>True</code> if the player with that colour is checkmate,
     * <code>False</code> otherwise.
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

    /**
     *
     * @return <code>True</code> if the current game is in a stale mate state.
     * <code>False</code> otherwise.
     */
    public boolean isStaleMate() {
        return staleMate;
    }

    /**
     * When the makes a move, while there are other moves avaiable in the move
     * list, we have to remove those 'obsolete' move. That is exactly what this
     * function does.
     */
    public void removeObsoleteMoves() {
        if (moveList.size() - 1 != appliedMove) {
            // We are not looking at the most 'recent' move
            if (appliedMove == -1) {
                // We are in our initial position, clear all the moves.
                moveList.clear();
            } else if (!moveList.isEmpty()) {
                // Remove the obsolete moves.
                moveList.subList(appliedMove + 1, moveList.size()).clear();
            }
        }
    }

    /**
     * Undo the move
     */
    public void undoMove() {
        if (appliedMove > -1) {
            // There is a move to undo
            if (appliedMove < moveList.size()) {
                // we can index the move
                Move move = moveList.get(appliedMove);
                move.undoMove();

                appliedMove--;
                updateTurn();

                view.update(chessBoard);
            }
        }
    }

    /**
     * Update the status of by updating the variables indicating that a certain
     * colour is check mate. Also sets the value for stalemate.
     */
    public void updateGameStatus() {
        King blackKing = chessBoard.getKing(ChessColour.Black);
        King whiteKing = chessBoard.getKing(ChessColour.White);

        if (whiteKing.getPossibleMoves().isEmpty()
                && !whiteKing.isCheck()
                && !chessBoard.canMakeAMove(ChessColour.White)
                || blackKing.getPossibleMoves().isEmpty()
                && !blackKing.isCheck()
                && !chessBoard.canMakeAMove(ChessColour.Black)) {
            staleMate = true;
            log.gameFinished(chessBoard);
        } else if (whiteKing.isCheck()
                && whiteKing.getPossibleMoves().isEmpty()
                && !chessBoard.canMakeAMove(ChessColour.White)) {
            winner = ChessColour.Black;
            log.gameFinished(chessBoard);
        } else if (blackKing.isCheck()
                && blackKing.getPossibleMoves().isEmpty()
                && !chessBoard.canMakeAMove(ChessColour.Black)) {
            winner = ChessColour.White;
            log.gameFinished(chessBoard);
        }
    }

    /**
     * Notifies the right player after a move has been made. If the next player
     * is a human player, he will be able to make his move by interacting with
     * the GUI.
     */
    public void updatePlayers() {
        switch (hasTurn) {
            case Black:
                if (blackPlayer instanceof AI) {
                    new Thread((Runnable) blackPlayer).start();
                }
                break;
            case White:
                if (whitePlayer instanceof AI) {
                    new Thread((Runnable) whitePlayer).start();
                }
                break;
        }
    }

    /**
     * Update the colour of the player having turn.
     */
    public void updateTurn() {
        hasTurn = (hasTurn == ChessColour.White) ? ChessColour.Black : ChessColour.White;
    }

    /**
     *
     * @return <code>True</code> if the current chess game has a winner.
     * <code>False</code> otherwise.
     */
    public boolean weHaveAWinner() {
        return winner != null;
    }

}
