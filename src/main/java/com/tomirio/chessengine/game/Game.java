/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomirio.chessengine.game;

import com.tomirio.chessengine.agent.AI;
import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.State;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class Game {

    /**
     * White player
     */
    public Player whitePlayer;

    /**
     * Black player
     */
    public Player blackPlayer;

    /**
     * The state
     */
    private final State state;

    /**
     * Constructor
     *
     * @param state The state.
     * @param chessBoard The chessboard.
     */
    public Game(State state, ChessBoard chessBoard) {
        this.state = state;
        whitePlayer = new HumanPlayer(ChessColour.White, chessBoard);
        blackPlayer = new AI(ChessColour.Black, chessBoard);
    }

    /**
     * Handles a chess move done by a human player.
     *
     * @param piece The chess piece.
     * @param row The new row.
     * @param col The new column.
     */
    public void humanPlay(ChessPiece piece, int row, int col) {
        switch (state.getTurnColour()) {
            case Black:
                blackPlayer.makeMove(piece, row, col);
                break;
            case White:
                whitePlayer.makeMove(piece, row, col);
                break;
        }
        updatePlayers();
    }

    /**
     * Calls the right method if an agent has turn.
     */
    public void agentPlay() {
        switch (state.getTurnColour()) {
            case Black:
                new Thread((Runnable) blackPlayer).start();
                break;
            case White:
                new Thread((Runnable) whitePlayer).start();
                break;
        }
        // updatePlayers() // Must be called if the Agent has made a move.
    }

    /**
     * Notifies the right player after a move has been made. If the next player
     * is a human player, he will just be able to make his move by interacting
     * with the GUI.
     */
    public void updatePlayers() {
        switch (state.getTurnColour()) {
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
}
