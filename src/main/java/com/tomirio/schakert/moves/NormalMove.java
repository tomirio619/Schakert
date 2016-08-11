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
     * @param movedPiece The chess piece.
     * @param newPos The new position.
     */
    public NormalMove(ChessPiece movedPiece, Position newPos) {
        super(movedPiece, newPos);
    }

    @Override
    public void doMove() {
        updateVulnerableEnPassantPosition();
        updateCastlingValues();
        chessBoard.silentMovePiece(movedPiece, newPos);
        chessBoard.updateKingStatus();
        chessBoard.updateTurn();
    }

    @Override
    public boolean isCaptureMove() {
        return false;
    }

    @Override
    public String toString() {
        String prefix = "";
        if (!getAmbiguousPieces().isEmpty()) {
            prefix += this.getUniquePrefix(getAmbiguousPieces());
        }
        if (movedPiece.getType() == PieceType.Pawn) {
            // Also need to check for ambuigity
            String pos = newPos.toString();
            if (this.movePutsEnemyKingInCheckmate()) {
                return prefix + pos + "#";
            } else if (movePutsEnemyKingInCheck()) {
                return prefix + pos + "+";
            } else {
                return prefix + pos;
            }
        } else {
            String type = movedPiece.getType().toShortString();
            String move = prefix + newPos.toString();
            if (movePutsEnemyKingInCheckmate()) {
                return type + move + "#";
            } else if (movePutsEnemyKingInCheck()) {
                return type + move + "+";
            } else {
                return type + move;
            }
        }
    }

    @Override
    public void undoMove() {
        chessBoard.silentMovePiece(movedPiece, orgPos);
        restoreVulnerableEnPassantPosition();
        restorePreviousCastlingValues();
        chessBoard.updateKingStatus();
        chessBoard.updateTurn();
    }

}
