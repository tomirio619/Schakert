/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomirio.chessengine.game;

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.moves.Move;

/**
 *
 * @author Tom Sandmann
 */
public abstract class Player {

    /**
     * The chess board.
     */
    public ChessBoard chessBoard;
    /**
     * The colour of the player.
     */
    public ChessColour playerColour;

    /**
     *
     * @param playerColour The colour.
     * @param chessBoard The chess board.
     */
    public Player(ChessColour playerColour, ChessBoard chessBoard) {
        this.playerColour = playerColour;
        this.chessBoard = chessBoard;
    }

    /**
     * Make a move as a human player
     *
     * @param move The move.
     */
    public abstract void makeMove(Move move);

    /**
     * Play as a non human player (agent)
     */
    public abstract void play();

}
