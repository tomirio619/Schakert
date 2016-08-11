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

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.chessboard.PieceType;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.chesspieces.King;
import com.tomirio.schakert.chesspieces.Rook;
import java.util.ArrayList;

/**
 *
 * @author S4ndmann
 */
public abstract class Move {

    /**
     * The chessBoard.
     */
    protected final ChessBoard chessBoard;
    /**
     * The piece involved in the move.
     */
    protected ChessPiece movedPiece;

    /**
     * The position of the piece after the move took place.
     */
    protected final Position newPos;
    /**
     * The position of the piece before the move took place.
     */
    protected final Position orgPos;

    /**
     * Position that a pawn could en Passant capture.
     */
    protected Position orgVulnerableEnPassantPos;
    /**
     * Previous value for the variable that indicates whether the king that was
     * involved in this move is able to perform a castling move.
     */
    protected boolean previousCastlingPossibleKing;
    /**
     * Previous value for the variable that indicates whether the rook that was
     * involved in this move is able to perform a castling move.
     */
    protected boolean previousCastlingPossibleRook;

    /**
     *
     * @param movedPiece The chess piece involved in this move.
     * @param newPos The new position of the chess piece.
     */
    public Move(ChessPiece movedPiece, Position newPos) {
        this.movedPiece = movedPiece;
        this.orgPos = movedPiece.getPos().deepClone();
        this.newPos = newPos.deepClone();
        this.chessBoard = movedPiece.getChessBoard();

        if (chessBoard.getEnPassantTargetSquare() != null) {
            this.orgVulnerableEnPassantPos = chessBoard.getEnPassantTargetSquare().deepClone();
        }
        saveCurrentCastlingValues();
    }

    /**
     * Apply the move.
     */
    public abstract void doMove();

    /**
     * Get Chesspieces of the same type that can move to the same new position
     * as this move.
     *
     * @return List of ambiguous chess pieces.
     */
    protected ArrayList<ChessPiece> getAmbiguousPieces() {
        ArrayList<ChessPiece> ambiguousPieces = new ArrayList();
        if (movedPiece.getType() == PieceType.King) {
            return ambiguousPieces;
        }
        ArrayList<ChessPiece> friendlyPieces = chessBoard.getPieces(movedPiece.getColour());
        for (ChessPiece p : friendlyPieces) {
            if (p.getType() == movedPiece.getType()) {
                // Both pieces are of the same type
                if (!p.getPos().equals(movedPiece.getPos())) {
                    // Other piece is not the same as the piece in this move.
                    for (Move move : p.getPossibleMoves()) {
                        if (move.getNewPos().equals(newPos)) {
                            /*
                            Other piece can move to the same position as
                            the piece that is involved in this move.
                             */
                            ambiguousPieces.add(p);
                        }
                    }
                }
            }
        }
        return ambiguousPieces;
    }

    /**
     * Get the chess piece that was involved in the move
     *
     * @return
     */
    public ChessPiece getInvolvedPiece() {
        return movedPiece;
    }

    /**
     * Get the new position of the chess piece if the move was applied.
     *
     * @return
     */
    public Position getNewPos() {
        return newPos;
    }

    /**
     * When two (or more) identical pieces can move to the same square, the
     * moving piece is uniquely identified by specifying the piece's letter,
     * followed by (in descending order of preference): - the file of departure
     * (if they differ); or - the rank of departure (if the files are the same
     * but the ranks differ); or - both the file and rank (if neither alone is
     * sufficient to identify the piece — which occurs only in rare cases where
     * one or more pawns have promoted, resulting in a player having three or
     * more identical pieces able to reach the same square).
     *
     * @param ambiguousPieces The ambiguous chess pieces.
     * @return The correct prefix to make the representation of the move unique.
     */
    protected String getUniquePrefix(ArrayList<ChessPiece> ambiguousPieces) {
        // Ranks are rows
        // Files are columns
        if (isUniqueColumn(ambiguousPieces)) {
            // file of departure is different
            return Character.toString(movedPiece.getPos().toString().charAt(0));
        } else if (isUniqueRow(ambiguousPieces)) {
            // rank of deperature is different
            return Character.toString(movedPiece.getPos().toString().charAt(1));
        } else {
            return movedPiece.getPos().toString();
        }
    }

    /**
     *
     * @return <code>True</code> if the move puts the enemy player in check.
     * <code>False</code> otherwise.
     */
    public boolean inCheckMove() {
        return movePutsEnemyKingInCheck();
    }

    /**
     *
     * @return <code>True</code> if the move puts the enemy player in check
     * mate. <code>False</code> otherwise.
     */
    public boolean inCheckmateMove() {
        return movePutsEnemyKingInCheckmate();
    }

    /**
     *
     * @return <code>True</code> if the move is a capture move.
     * <code>False</code> otherwise.
     */
    public abstract boolean isCaptureMove();

    /**
     *
     * @return <code>True</code> if the move puts the game in a stale mate
     * state. <code>False</code> otherwise.
     */
    private boolean isStalemateMove() {
        doMove();
        boolean isStalemate = chessBoard.inStalemate();
        undoMove();
        return isStalemate;
    }

    /**
     * For all the pieces of the same type having a move that is the same as the
     * final position of this move, check whether the column of the piece
     * involved in this move is different from the ambiguous pieces.
     *
     * @param ambiguousPieces The ambiguous pieces.
     * @return <code>True</code> if the column is unique, <code>False</code>
     * otherwise.
     */
    private boolean isUniqueColumn(ArrayList<ChessPiece> ambiguousPieces) {
        for (ChessPiece otherPiece : ambiguousPieces) {
            if (otherPiece.getColumn() == movedPiece.getColumn()) {
                // Column is not unique
                return false;
            }
        }
        // Column is unique
        return true;
    }

    /**
     * For all the pieces of the same type having a move that is the same as the
     * final position of this move, check whether the row of the piece involved
     * in this move is different from the ambiguous pieces.
     *
     * @param ambiguousPieces The ambiguous pieces.
     * @return <code>True</code> if the row is unique, <code>False</code>
     * otherwise.
     */
    private boolean isUniqueRow(ArrayList<ChessPiece> ambiguousPieces) {
        for (ChessPiece otherPiece : ambiguousPieces) {
            if (otherPiece.getRow() == movedPiece.getRow()) {
                // Row is not unique
                return false;
            }

        }
        // Row is unique
        return true;
    }

    /**
     *
     * @return If the move is applied and will put the enemy king in check, this
     * function returns <code>True</code>. Otherwise it returns
     * <code>False</code>
     */
    protected boolean movePutsEnemyKingInCheck() {
        doMove();
        boolean inCheck = chessBoard.getKing(movedPiece.getColour().getOpposite()).inCheck();
        undoMove();
        return inCheck;
    }

    /**
     * Checks wheter the move would put the enemy player in check mate position
     * if it was applied.
     *
     * @return <code>True</code> if applying the move would put the enemy in
     * check mate position, <code>False</code> otherwise.
     */
    protected boolean movePutsEnemyKingInCheckmate() {
        doMove();
        boolean putsEnemyKingInCheckmate = chessBoard.inCheckmate(movedPiece.getColour().getOpposite());
        undoMove();
        return putsEnemyKingInCheckmate;
    }

    /**
     * Restore the original variable that indcates if a castlig move was
     * possible
     */
    protected void restorePreviousCastlingValues() {
        if (movedPiece.getType() == PieceType.King) {
            King king = (King) movedPiece;
            king.setCastlingPossible(previousCastlingPossibleKing);
        }
        if (movedPiece.getType() == PieceType.Rook) {
            Rook rook = (Rook) movedPiece;
            rook.setCastlingPossible(previousCastlingPossibleRook);
        }
    }

    /**
     * Restore the position that could be attacked by a pawn to perform an en
     * Passant move.
     */
    protected void restoreVulnerableEnPassantPosition() {
        chessBoard.setEnPassantTargetSquare(orgVulnerableEnPassantPos);

    }

    private void saveCurrentCastlingValues() {
        if (movedPiece.getType() == PieceType.King) {
            King king = (King) movedPiece;
            previousCastlingPossibleKing = king.getCastlingPossible();
        }
        if (movedPiece.getType() == PieceType.Rook) {
            Rook rook = (Rook) movedPiece;
            previousCastlingPossibleRook = rook.getCastlingPossible();
        }
    }

    public boolean stalemateMove() {
        return isStalemateMove();
    }

    /*
    NOTE: this method produces the String representation of the move
    assuming the move has NOT been performed on the chess board.
    It will actually perform the move, check whether it puts the enemy king in 
    check (or mate) position, and undo it.
     */
    @Override
    public abstract String toString();

    /**
     * Undo the move.
     */
    public abstract void undoMove();

    /**
     * Upte the castling values if a move is peformed by either a king or a
     * rook.
     */
    protected void updateCastlingValues() {
        if (movedPiece.getType() == PieceType.King) {
            King king = (King) movedPiece;
            king.setCastlingPossible(false);
        }
        if (movedPiece.getType() == PieceType.Rook) {
            Rook rook = (Rook) movedPiece;
            rook.setCastlingPossible(false);
        }
    }

    /**
     * Update the vulernable enPassant position if a pawn moves to squares
     * forward.
     */
    protected void updateVulnerableEnPassantPosition() {
        if (movedPiece.getType() == PieceType.Pawn) {
            int rowDist = Math.abs(newPos.getRow() - orgPos.getRow());
            if (rowDist == 2) {
                // This move enables enPassant.
                int rowShift = (movedPiece.getColour() == Colour.White) ? 1 : -1;
                Position vulnerableEnPassantPos = new Position(newPos.getRow() + rowShift, newPos.getColumn());
                chessBoard.setEnPassantTargetSquare(vulnerableEnPassantPos.deepClone());
            } else {
                // Move did not enable enPassant.
                chessBoard.setEnPassantTargetSquare(null);
            }
        } else {
            // Move did not involve a pawn, so we reset enPassant square.
            chessBoard.setEnPassantTargetSquare(null);
        }
    }

}
