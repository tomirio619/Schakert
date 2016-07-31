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
package com.tomirio.schakert.moves;

import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.chesspieces.ChessPiece;
import com.tomirio.schakert.chesspieces.Pawn;
import com.tomirio.schakert.chesspieces.Queen;

/**
 *
 * @author S4ndmann
 */
public class CapturePromotionMove extends CaptureMove {

    /**
     *
     * @param capturingPiece The piece that made the capture move.
     * @param newPos The new positon after the capture took place.
     */
    public CapturePromotionMove(ChessPiece capturingPiece, Position newPos) {
        super(capturingPiece, newPos);
    }

    @Override
    public void doMove() {
        super.doMove();
        // Create a queen with the same colour and position of the pawn we just moved.
        Queen q = new Queen(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
        chessBoard.setPiece(q);
        // Set piece to the queen we just created
        movedPiece = q;
        chessBoard.setEnPassantTargetSquare(null);
        chessBoard.updateKingStatus();
    }

    @Override
    public boolean isCaptureMove() {
        return true;
    }


    @Override
    public String toString() {
        String prefix = "";
        if (!getAmbiguousPieces().isEmpty()) {
            prefix += this.getUniquePrefix(getAmbiguousPieces());
        }
        if (movePutsEnemyKingInCheckmate()) {
            return prefix + "x" + newPos.toString() + "=Q" + "#";
        } else if (movePutsEnemyKingInCheck()) {
            return prefix + "x" + newPos.toString() + "=Q" + "+";
        } else {
            return prefix + "x" + newPos.toString() + "=Q";
        }
    }
    @Override
    public void undoMove() {
        super.undoMove();
        /*
        Create a new pawn with the position of queen we just moved back.
        Note that we cannot use orgPos here, it will give the wrong position.
        Might be due to the reference being passed to the queen, which could be
        modified if the move is done and undone multiple times.
        */
        Pawn p = new Pawn(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
        chessBoard.setPiece(p);
    }

}
