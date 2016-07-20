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
package com.tomirio.chessengine.moves;

import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.chesspieces.Pawn;
import com.tomirio.chessengine.chesspieces.Queen;

/**
 *
 * @author S4ndmann
 */
public class PromotionMove extends NormalMove {

    ChessPiece possibleCapturedPiece;

    /**
     * A promotion move. Note that this move also keeps in mind a possible
     * capture.
     *
     * @param pawn The pawn.
     * @param newPos The new position.
     */
    public PromotionMove(Pawn pawn, Position newPos) {
        super(pawn, newPos);
    }

    @Override
    public void doMove() {
        // Check for possible capture 
        if (chessBoard.isOccupiedPosition(newPos)) {
            // Enemy piece is captured
            possibleCapturedPiece = chessBoard.getPiece(newPos);
        }
        chessBoard.silentMovePiece(piece, newPos);
        Queen q = new Queen(piece.getColour(), piece.getPos(), chessBoard);
        chessBoard.setPiece(q);
        piece = q;
        //super.updateGame();
    }

    @Override
    public void undoMove() {
        super.undoMove();
        Pawn p = new Pawn(piece.getColour(), orgPos, chessBoard);
        chessBoard.setPiece(p);
        piece = p;

        if (possibleCapturedPiece != null) {
            // During the promotion in this move, an enemy piece was captured.
            chessBoard.setPiece(possibleCapturedPiece);
        }
        possibleCapturedPiece = null;
        //super.updateGame();
    }

}
