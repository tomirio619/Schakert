/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomirio.chessengine.game;

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;



/**
 *
 * @author Tom Sandmann
 */
public class HumanPlayer extends Player {

    /**
     *
     * @param playerColour  The colour of the player.
     * @param chessBoard    The chess board.
     */
    public HumanPlayer(ChessColour playerColour, ChessBoard chessBoard) {
        super(playerColour, chessBoard);
    }

    @Override
    public void play() {
        throw new UnsupportedOperationException("Human players do not implement this method!");
    }

    @Override
    public void makeMove(ChessPiece piece, int row, int col) {
        piece.move(row, col);
        /*
        P = 100
        N = 320
        B = 330
        R = 500
        Q = 900
        K = 20000
         */
    }

}
