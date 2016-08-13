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
import com.tomirio.schakert.chesspieces.King;
import com.tomirio.schakert.chesspieces.Rook;

/**
 *
 * @author Tom Sandmann
 */
public class CastlingMove extends NormalMove {

    /**
     * The rook involved in the castling move.
     */
    private final Rook rook;
    /**
     * The new position of the rook after the move will be applied
     */
    private final Position rookNewPos;
    /**
     * The original position of the rook before the move was applied
     */
    private final Position rookOrgPos;

    /**
     *
     * @param king The king involved in castling.
     * @param kingNewPos The new position of the king.
     * @param rook The rook involved in castling.
     * @param rookNewPos The new position of the rook.
     */
    public CastlingMove(King king, Position kingNewPos,
            Rook rook, Position rookNewPos) {
        super(king, kingNewPos);
        this.rook = rook;
        this.rookOrgPos = rook.getPos().deepClone();
        this.rookNewPos = rookNewPos;
    }

    @Override
    public void doMove() {
        super.doMove();
        King king = (King) movedPiece;
        king.setCastlingPossible(false);
        rook.setCastlingPossible(false);
        chessBoard.silentMovePiece(rook, rookNewPos);
        movedPiece = king;
        chessBoard.setEnPassantTargetSquare(null);
        chessBoard.updateKingStatus();
    }

    @Override
    public boolean isCaptureMove() {
        return false;
    }

    @Override
    public String toString() {
        int colDist = Math.abs(rookOrgPos.getColumn() - orgPos.getColumn());
        if (colDist == 3) {
            // King side rook, castling short. 
            if (this.movePutsEnemyKingInCheckmate()) {
                return "O-O" + "#";
            } else if (this.movePutsEnemyKingInCheck()) {
                return "O-O" + "+";
            } else {
                return "O-O";
            }

        } else // Queen side rook, castling long.
         if (this.movePutsEnemyKingInCheckmate()) {
                return "O-O-O" + "#";
            } else if (this.movePutsEnemyKingInCheck()) {
                return "O-O-O" + "+";
            } else {
                return "O-O-O";
            }
    }

    @Override
    public void undoMove() {
        super.undoMove();
        King king = (King) movedPiece;
        king.setCastlingPossible(true);
        rook.setCastlingPossible(true);
        chessBoard.silentMovePiece(rook, rookOrgPos);
        movedPiece = king;
        chessBoard.updateKingStatus();
    }

}
