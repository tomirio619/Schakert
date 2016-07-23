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
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.chesspieces.King;
import com.tomirio.chessengine.chesspieces.Rook;
import java.util.ArrayList;
import java.util.NoSuchElementException;

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
     * The piece involved in the move.
     */
    protected ChessPiece piece;

    protected boolean previousCastlingPossibleKing;
    protected boolean previousCastlingPossibleRook;

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

        if (chessBoard.getVulnerableEnPassantPos() != null) {
            this.orgVulnerableEnPassantPos = chessBoard.getVulnerableEnPassantPos().deepClone();
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
        if (piece.getType() == PieceType.King) {
            return ambiguousPieces;
        }
        ArrayList<ChessPiece> friendlyPieces = chessBoard.getPieces(piece.getColour());
        for (ChessPiece p : friendlyPieces) {
            if (p.getType() == piece.getType()) {
                // Both pieces are of the same type
                if (!p.getPos().equals(piece.getPos())) {
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
     * @param ambiguousPieces   The ambiguous chess pieces.
     * @return The correct prefix to make the representation of the move unique.
     */
    protected String getUniquePrefix(ArrayList<ChessPiece> ambiguousPieces) {
        // Ranks are rows
        // Files are columns
        if (isUniqueColumn(ambiguousPieces)) {
            // file of departure is different
            return Character.toString(piece.getPos().toString().charAt(0));
        } else if (isUniqueRow(ambiguousPieces)) {
            // rank of deperature is different
            return Character.toString(piece.getPos().toString().charAt(1));
        } else {
            return piece.getPos().toString();
        }
    }

    /**
     * Checks wheter the player with the specified colour is in a check mate
     * position.
     *
     * @param playerColour The colour of the player.
     * @return <code>True</code> if the player with the colour is in check mate
     * position, <code>False</code> otherwise.
     */
    private boolean isCheckMate(ChessColour playerColour) {
        King whiteKing = chessBoard.getKing(ChessColour.White);
        King blackKing = chessBoard.getKing(ChessColour.Black);
        switch (playerColour) {
            case Black:
                return blackKing.isCheck()
                        && blackKing.getPossibleMoves().isEmpty()
                        && !chessBoard.canMakeAMove(ChessColour.Black);

            case White:
                return whiteKing.isCheck()
                        && whiteKing.getPossibleMoves().isEmpty()
                        && !chessBoard.canMakeAMove(ChessColour.White);
            default:
                throw new NoSuchElementException();
        }
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
            if (otherPiece.getColumn() == piece.getColumn()) {
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
            if (otherPiece.getRow() == piece.getRow()) {
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
    protected boolean putsEnemyKingInCheck() {
        doMove();
        King k = chessBoard.getKing(piece.getColour().getOpposite());
        boolean putsEnemyKingInCheck = k.isCheck();
        undoMove();
        return putsEnemyKingInCheck;
    }

    /**
     * Checks wheter the move would put the enemy player in check mate position
     * if it was applied.
     *
     * @return <code>True</code> if applying the move would put the enemy in
     * check mate position, <code>False</code> otherwise.
     */
    protected boolean putsEnemyKingInCheckMate() {
        doMove();
        boolean putsEnemyKingInCheckMate = isCheckMate(piece.getColour().getOpposite());
        undoMove();
        return putsEnemyKingInCheckMate;
    }

    protected void restorePreviousCastlingValues() {
        if (piece.getType() == PieceType.King) {
            King king = (King) piece;
            king.setCastlingPossible(previousCastlingPossibleKing);
        }
        if (piece.getType() == PieceType.Rook) {
            Rook rook = (Rook) piece;
            rook.setCastlingPossible(previousCastlingPossibleRook);
        }
    }

    protected void restoreVulnerableEnPassantPosition() {
        chessBoard.setVulnerableEnPassantPos(orgVulnerableEnPassantPos);

    }

    private void saveCurrentCastlingValues() {
        if (piece.getType() == PieceType.King) {
            King king = (King) piece;
            previousCastlingPossibleKing = king.getCastlingPossible();
        }
        if (piece.getType() == PieceType.Rook) {
            Rook rook = (Rook) piece;
            previousCastlingPossibleRook = rook.getCastlingPossible();
        }
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

    protected void updateCastlingValues() {
        if (piece.getType() == PieceType.King) {
            King king = (King) piece;
            king.setCastlingPossible(false);
        }
        if (piece.getType() == PieceType.Rook) {
            Rook rook = (Rook) piece;
            rook.setCastlingPossible(false);
        }
    }

    protected void updateVulnerableEnPassantPosition() {
        if (piece.getType() == PieceType.Pawn) {
            if (Math.abs(newPos.getRow() - orgPos.getRow()) == 2) {
                // This move enables enPassant.
                int rowShift = (piece.getColour() == ChessColour.White) ? 1 : -1;
                Position vulnerableEnPassantPos = new Position(newPos.getRow() + rowShift, newPos.getColumn());
                chessBoard.setVulnerableEnPassantPos(vulnerableEnPassantPos.deepClone());
            }
        } else {
            chessBoard.setVulnerableEnPassantPos(null);
        }
    }

}
