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
import com.tomirio.schakert.chessboard.PieceType;
import com.tomirio.schakert.chessboard.Position;

/**
 *
 * @author S4ndmann
 */
public class CaptureMove extends NormalMove {

    ChessPiece capturedPiece;

    /**
     *
     * @param capturingPiece The piece that made the capture move.
     * @param newPos The new positon after the capture took place.
     */
    public CaptureMove(ChessPiece capturingPiece, Position newPos) {
        super(capturingPiece, newPos);
        capturedPiece = chessBoard.getPiece(newPos);
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
        if (movedPiece.getType() == PieceType.Pawn) {
            if (movePutsEnemyKingInCheckmate()) {
                return prefix + "x" + newPos.toString() + "#";
            } else if (movePutsEnemyKingInCheck()) {
                return prefix + "x" + newPos.toString() + "+";
            } else {
                return prefix + "x" + newPos.toString();
            }

        } else if (this.movePutsEnemyKingInCheckmate()) {
            return movedPiece.getType().toShortString() + prefix + "x" + newPos.toString() + "#";
        } else if (movePutsEnemyKingInCheck()) {
            return movedPiece.getType().toShortString() + prefix + "x" + newPos.toString() + "+";
        } else {
            return movedPiece.getType().toShortString() + prefix + "x" + newPos.toString();
        }
    }

    @Override
    public void undoMove() {
        super.undoMove();
        chessBoard.setPiece(capturedPiece);
        chessBoard.setEnPassantTargetSquare(this.orgVulnerableEnPassantPos);
        chessBoard.updateKingStatus();
    }

}
