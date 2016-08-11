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
package com.tomirio.schakert.game;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.moves.Move;

/**
 *
 * @author Tom Sandmann
 */
public class HumanPlayer extends Player {

    /**
     *
     * @param playerColour The colour of the player.
     * @param chessBoard The chess board.
     */
    public HumanPlayer(Colour playerColour, ChessBoard chessBoard) {
        super(playerColour, chessBoard);
    }

    @Override
    public Move getPlay() {
        throw new UnsupportedOperationException("Human players do not implement this method!");
    }

    @Override
    public void makeMove(Move move) {
        move.doMove();
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
