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
import com.tomirio.schakert.chesspieces.Bishop;
import com.tomirio.schakert.chesspieces.ChessPiece;
import com.tomirio.schakert.chesspieces.Knight;
import com.tomirio.schakert.chesspieces.Pawn;
import com.tomirio.schakert.chesspieces.PieceType;
import static com.tomirio.schakert.chesspieces.PieceType.Bishop;
import com.tomirio.schakert.chesspieces.Queen;
import com.tomirio.schakert.chesspieces.Rook;
import java.util.NoSuchElementException;

/**
 *
 * @author S4ndmann
 */
public class CapturePromotionMove extends CaptureMove {

    /**
     * Type of chess piece the pawn will promoto into.
     */
    private final PieceType typeToPromoteTo;

    /**
     *
     * @param capturingPiece The piece that made the capture move.
     * @param newPos The new positon after the capture took place.
     * @param typeToPromoteTo The type of the chess Piece the pawn will promote
     * to.
     */
    public CapturePromotionMove(ChessPiece capturingPiece, Position newPos, PieceType typeToPromoteTo) {
        super(capturingPiece, newPos);
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
        return true;
    }

    @Override
    public String toString() {
        String prefix = "";
        if (!getAmbiguousPieces().isEmpty()) {
            prefix += this.getUniquePrefix(getAmbiguousPieces());
        }
        if (movePutsEnemyKingInCheckmate()) {
            return prefix + "x" + newPos.toString() + "=Q" + "#";
        } else if (movePutsEnemyKingInCheck()) {
            return prefix + "x" + newPos.toString() + "=Q" + "+";
        } else {
            return prefix + "x" + newPos.toString() + "=Q";
        }
    }

    @Override
    public void undoMove() {
        super.undoMove();
        /*
        Create a new pawn with the position of queen we just moved back.
        Note that we cannot use orgPos here, it will give the wrong position.
        Might be due to the reference being passed to the new chesspiece, 
        which could be modified if the move is done and undone multiple times.
         */
        Pawn p = new Pawn(movedPiece.getColour(), movedPiece.getPos(), chessBoard);
        chessBoard.setPiece(p);
    }
    
    public PieceType getPromotionType(){
        return typeToPromoteTo;
    }

}
