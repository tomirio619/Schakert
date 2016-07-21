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

import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.chesspieces.King;
import com.tomirio.chessengine.chesspieces.Rook;

/**
 *
 * @author S4ndmann
 */
public class CastlingMove extends NormalMove {

    Rook rook;

    Position rookNewPos;
    Position rookOrgPos;

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
        King king = (King) piece;
        king.setCastlingPossible(false);
        rook.setCastlingPossible(false);
        chessBoard.silentMovePiece(rook, rookNewPos);
        piece = king;
        chessBoard.setVulnerableEnPassantPos(null);
        chessBoard.updateKingStatus();
    }

    @Override
    public String toString() {
        return "Castling move: king moved to " + newPos + "\n"
                + "Castle moved to " + rookNewPos;
    }

    @Override
    public void undoMove() {
        super.undoMove();
        King king = (King) piece;
        king.setCastlingPossible(true);
        rook.setCastlingPossible(true);
        chessBoard.silentMovePiece(rook, rookOrgPos);
        piece = king;
        chessBoard.updateKingStatus();
    }

}
