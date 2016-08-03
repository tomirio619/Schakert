/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomirio.schakert.game;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chesspieces.Colour;
import com.tomirio.schakert.moves.Move;

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
    public Colour playerColour;

    /**
     *
     * @param playerColour The colour.
     * @param chessBoard The chess board.
     */
    public Player(Colour playerColour, ChessBoard chessBoard) {
        this.playerColour = playerColour;
        this.chessBoard = chessBoard;
    }

    /**
     * Play as a non human player (agent)
     */
    public abstract Move getPlay();

    /**
     * Make a move as a human player
     *
     * @param move The move.
     */
    public abstract void makeMove(Move move);

}
