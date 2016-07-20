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
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Position;

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
        System.out.println("We kijken naar de methode doMove van het stuk " + piece);

        if (piece.getType() == PieceType.Pawn) {
            if (Math.abs(newPos.getRow() - orgPos.getRow()) == 2) {
                // This move enables enPassant
                chessBoard.setVulnerableEnPassantPos(newPos.deepClone());
            }
        }
        chessBoard.silentMovePiece(piece, newPos);
        //super.updateGame();
    }

    ;
    

    @Override
    public String toString() {
        return "Piece that is moved: " + piece + "\n"
                + "New position: " + newPos;
    }

    @Override
    public void undoMove() {
        System.out.println("We kijken naar de methode undoMove van het stuk " + piece);
        if (piece.getType() == PieceType.Pawn) {
            if (Math.abs(newPos.getRow() - orgPos.getRow()) == 2) {
                // This move disables enPassant
                chessBoard.setVulnerableEnPassantPos(null);
            }
        }
        chessBoard.silentMovePiece(piece, orgPos);
        //super.updateGame();
    }

}
