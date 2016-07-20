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

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.Position;

/**
 *
 * @author S4ndmann
 */
public abstract class Move {

    /**
     * The chessBoard.
     */
    ChessBoard chessBoard;

    /**
     * The position of the piece after the move took place.
     */
    Position newPos;
    /**
     * The position of the piece before the move took place.
     */
    Position orgPos;
    /**
     * The piece involved in the move
     */
    ChessPiece piece;

    /**
     *
     * @param piece The chess piece involved in this move.
     * @param newPos The new position of the chess piece.
     */
    public Move(ChessPiece piece, Position newPos) {
        this.piece = piece;
        this.orgPos = piece.getPos().deepClone();
        this.newPos = newPos.deepClone();
        this.chessBoard = piece.getChessBoard();
    }

    /**
     * Apply the move.
     */
    public abstract void doMove();

    /**
     * Get the chess piece that was involved in the move
     *
     * @return
     */
    public ChessPiece getInvolvedPiece() {
        return piece;
    }

    /**
     * Get the new position of the chess piece if the move was applied.
     *
     * @return
     */
    public Position getNewPos() {
        return newPos;
    }

    @Override
    public abstract String toString();

    /**
     * Undo the move.
     */
    public abstract void undoMove();

    /**
     * Update the game.
     */
    public void updateGame() {
        chessBoard.updateKingStatus();
    }

}
