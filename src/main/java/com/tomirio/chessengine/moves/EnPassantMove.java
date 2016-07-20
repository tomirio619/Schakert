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
import com.tomirio.chessengine.chesspieces.Pawn;

/**
 *
 * @author S4ndmann
 */
public class EnPassantMove extends CaptureMove {

    /**
     *
     * @param pawn The pawn
     * @param newPos The new position.
     * @param capturedPawn The captured pawn.
     */
    public EnPassantMove(Pawn pawn, Position newPos, Pawn capturedPawn) {
        super(pawn, newPos);
        // super sets the wrong capturedPiece, so we have to update it.
        this.capturedPiece = capturedPawn;
    }

    @Override
    public void doMove() {
        chessBoard.setVulnerableEnPassantPos(null);
        chessBoard.silentMovePiece(piece, newPos);
        chessBoard.deletePieceOnPos(capturedPiece.getPos());
    }

    @Override
    public String toString() {
        return "enPassant move. Pawn that moved: " + piece + "\n"
                + "New position of pawn: " + newPos
                + "Captured piece: " + capturedPiece;
    }
}
