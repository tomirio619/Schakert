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
import com.tomirio.schakert.chesspieces.Bishop;
import com.tomirio.schakert.chesspieces.Knight;
import com.tomirio.schakert.chesspieces.Pawn;
import com.tomirio.schakert.chesspieces.Queen;
import com.tomirio.schakert.chesspieces.Rook;
import java.util.NoSuchElementException;

/**
 *
 * @author S4ndmann
 */
public class PromotionMove extends NormalMove {

    /**
     * Type of chess piece the pawn will promoto into.
     */
    private final PieceType typeToPromoteTo;

    /**
     * A promotion move. Note that this move also keeps in mind a possible
     * capture.
     *
     * @param movedPawn The moved pawn.
     * @param newPos The new position.
     * @param typeToPromoteTo
     */
    public PromotionMove(ChessPiece movedPawn, Position newPos, PieceType typeToPromoteTo) {
        super(movedPawn, newPos);
        this.typeToPromoteTo = typeToPromoteTo;
    }

    @Override
    public void doMove() {
        super.doMove();
        // Create a chess piece of the correct type with the same colour and position of the pawn we just moved.
        ChessPiece p;
        switch (typeToPromoteTo) {
            case Queen:
                p = new Queen(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
                break;
            case Rook:
                Rook r = new Rook(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
                // Castling is only possible with one of the original rooks.
                r.setCastlingPossible(false);
                p = r;
                break;
            case Knight:
                p = new Knight(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
                break;
            case Bishop:
                p = new Bishop(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
                break;
            default:
                throw new NoSuchElementException();
        }
        chessBoard.setPiece(p);
        // Set piece to the queen we just created
        movedPiece = p;
        chessBoard.setEnPassantTargetSquare(null);
        chessBoard.updateKingStatus();
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

        if (this.movePutsEnemyKingInCheckmate()) {
            return prefix + newPos.toString() + "=Q" + "#";
        } else if (this.movePutsEnemyKingInCheck()) {
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
        Pawn p = new Pawn(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
        chessBoard.setPiece(p);
        // Set the piece to the pawn we just created
        movedPiece = p;
        restoreVulnerableEnPassantPosition();
        chessBoard.updateKingStatus();
    }

    public PieceType getPromotionType() {
        return typeToPromoteTo;
    }

}
