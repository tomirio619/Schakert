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
import com.tomirio.chessengine.chessboard.State;
import com.tomirio.chessengine.moves.Move;
import com.tomirio.chessengine.view.View;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class Game {

    /**
     * Black player
     */
    public Player blackPlayer;
    private final ChessBoard chessBoard;

    /**
     * The colour having turn
     */
    private ChessColour hasTurn;

    /**
     * All the moves that led to the current state of the chess board.
     */
    private final ArrayList<Move> moveList;

    private final View view;
    /**
     * White player
     */
    public Player whitePlayer;

    /**
     * Constructor
     *
     * @param state The state.
     * @param chessBoard The chessboard.
     * @param view
     */
    public Game(State state, ChessBoard chessBoard, View view) {
        this.chessBoard = chessBoard;
        this.view = view;
        moveList = new ArrayList<>();
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
     */
    public void humanPlay(Move move) {
        moveList.add(move);
        updateTurn();
        switch (hasTurn) {
            case Black:
                blackPlayer.makeMove(move);
                break;
            case White:
                whitePlayer.makeMove(move);
                break;
        }
        view.update(chessBoard);
        updatePlayers();
    }

    /**
     * Notifies the right player after a move has been made. If the next player
     * is a human player, he will just be able to make his move by interacting
     * with the GUI.
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
     * Update the player colour having turn.
     */
    public void updateTurn() {
        hasTurn = (hasTurn == ChessColour.White) ? ChessColour.Black : ChessColour.White;
    }
}
