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
public class NormalMove extends Move {

    /**
     * A normal move.
     *
     * @param piece The chess piece.
     * @param newPos The new position.
     */
    public NormalMove(ChessPiece piece, Position newPos) {
        super(piece, newPos);
    }

    @Override
    public void doMove() {
        updateVulnerableEnPassantPosition();
        updateCastlingValues();
        chessBoard.silentMovePiece(piece, newPos);
        chessBoard.updateKingStatus();
    }

    ;
    public boolean isCaptureMove() {
        return false;
    }

    @Override
    public String toString() {
        String prefix = "";
        if (!getAmbiguousPieces().isEmpty()) {
            prefix += this.getUniquePrefix(getAmbiguousPieces());
        }
        if (piece.getType() == PieceType.Pawn) {
            // Also need to check for ambuigity
            String pos = newPos.toString();
            if (this.putsEnemyKingInCheckMate()) {
                return prefix + pos + "#";
            } else if (putsEnemyKingInCheck()) {
                return prefix + pos + "+";
            } else {
                return prefix + pos;
            }
        } else {
            String type = piece.getType().toShortString();
            String move = prefix + newPos.toString();
            if (putsEnemyKingInCheckMate()) {
                return type + move + "#";
            } else if (putsEnemyKingInCheck()) {
                return type + move + "+";
            } else {
                return type + move;
            }
        }
    }

    @Override
    public void undoMove() {
        chessBoard.silentMovePiece(piece, orgPos);
        restoreVulnerableEnPassantPosition();
        restorePreviousCastlingValues();
        chessBoard.updateKingStatus();
    }

}
