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

/**
 *
 * @author S4ndmann
 */
public class CaptureMove extends NormalMove {

    ChessPiece capturedPiece;

    /**
     *
     * @param piece The piece that made the capture move.
     * @param newPos The new positon after the capture took place.
     */
    public CaptureMove(ChessPiece piece, Position newPos) {
        super(piece, newPos);
        capturedPiece = chessBoard.getPiece(newPos);
    }

    @Override
    public void undoMove() {
        super.undoMove();
        chessBoard.setPiece(capturedPiece);
        chessBoard.setVulnerableEnPassantPos(this.orgVulnerableEnPassantPos);
        chessBoard.updateKingStatus();
    }
}
