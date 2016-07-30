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

import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.chesspieces.Pawn;
import com.tomirio.schakert.chesspieces.Queen;

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
    public PromotionMove(ChessPiece pawn, Position newPos) {
        super(pawn, newPos);
    }

    @Override
    public void doMove() {
        // Check for possible capture 
        if (chessBoard.isOccupiedPosition(newPos)) {
            // Enemy piece is captured
            possibleCapturedPiece = chessBoard.getPiece(newPos);
        }
        // Move the pawn to the new position
        chessBoard.silentMovePiece(piece, newPos);
        // Create a queen with the same colour and position of the pawn we just moved.
        Queen q = new Queen(piece.getColour(), piece.getPos(), chessBoard);
        chessBoard.setPiece(q);
        // Set piece to the queen we just created
        piece = q;
        chessBoard.setEnPassantTargetSquare(null);
        chessBoard.updateKingStatus();
    }

    @Override
    public boolean isCaptureMove() {
        if (chessBoard.isOccupiedPosition(newPos)) {
            return chessBoard.getPiece(newPos).getColour() != piece.getColour();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String prefix = "";
        if (!getAmbiguousPieces().isEmpty()) {
            prefix += this.getUniquePrefix(getAmbiguousPieces());
        }
        if (possibleCapturedPiece != null) {
            if (this.putsEnemyKingInCheckMate()) {
                return prefix + orgPos.toString() + "x" + newPos.toString() + "=Q" + "#";
            } else if (this.putsEnemyKingInCheck()) {
                return prefix + orgPos.toString() + "x" + newPos.toString() + "=Q" + "+";
            } else {
                return prefix + orgPos.toString() + "x" + newPos.toString() + "=Q";
            }

        } else if (this.putsEnemyKingInCheckMate()) {
            return prefix + newPos.toString() + "=Q" + "#";
        } else if (this.putsEnemyKingInCheck()) {
            return prefix + newPos.toString() + "=Q" + "+";
        } else {
            return prefix + newPos.toString() + "=Q";
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
        Pawn p = new Pawn(piece.getColour(), piece.getPos(), chessBoard);
        chessBoard.setPiece(p);
        // Set the piece to the pawn we just created
        piece = p;
        if (possibleCapturedPiece != null) {
            // During the promotion in this move, an enemy piece was captured.
            chessBoard.setPiece(possibleCapturedPiece);
        }
        possibleCapturedPiece = null;
        restoreVulnerableEnPassantPosition();
        chessBoard.updateKingStatus();
    }

}
