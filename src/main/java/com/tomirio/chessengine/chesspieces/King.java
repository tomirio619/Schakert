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
package com.tomirio.chessengine.chesspieces;

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.ChessTypes;
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PiecePosition;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class King extends ChessPiece {

    private boolean isCheck;

    private boolean castlingPossible;

    /**
     * This constructor MUST be used when the chessboard is not known when a new
     * chess piece is made. The chessboard must be set later on with
     * <code>setBoard()</code>.
     *
     * @param type The type of the chess piece.
     * @param colour The colour of the chess piece.
     * @param pos The position of the chess piece.
     */
    public King(ChessTypes type, ChessColour colour, PiecePosition pos) {
        super(type, colour, pos);
        isCheck = false;
        castlingPossible = true;
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        ArrayList<PiecePosition> possibleMoves = getKingPositions().moves;
        possibleMoves.addAll(getCastlingMoves());
        return filterMoves(possibleMoves);
    }

    @Override
    public void move(int row, int column) {
        castlingPossible = false;
        super.move(row, column);
    }

    @Override
    public void agentMove(int row, int column) {
        castlingPossible = false;
        super.agentMove(row, column);
    }

    /**
     *
     * @return All the possible moves for the king.
     */
    private MoveDetails getKingPositions() {
        MoveDetails moveDetails = new MoveDetails();
        for (int row = getRow() - 1; row <= getRow() + 1; row++) {
            for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                PiecePosition newPos = new PiecePosition(row, column);
                if (chessBoard.isValidCoordinate(row, column) && newPos.isValid()) {
                    if (!(newPos.equals(getPos()))) {
                        if (!chessBoard.isOccupiedPosition(newPos) && isSafePosition(newPos)) {
                            moveDetails.moves.add(newPos);
                        } else if (chessBoard.isOccupiedPosition(newPos)
                                && chessBoard.getColour(newPos) != getColour()
                                && isSafePosition(newPos)) {
                            moveDetails.moves.add(newPos);
                        } else if (chessBoard.isOccupiedPosition(newPos)
                                && chessBoard.getColour(newPos) == getColour()) {
                            moveDetails.coveredFriendlyPieces.add(newPos);
                        }
                    }
                }
            }
        }
        return moveDetails;
    }

    /**
     * @return The possible castling moves for this king.
     */
    private ArrayList<PiecePosition> getCastlingMoves() {
        ArrayList<PiecePosition> castlingMoves = new ArrayList<>();

        if (!castlingPossible) {
            // Castling not possible, return empty list
            return castlingMoves;
        }

        switch (getColour()) {
            case Black:
                ArrayList<Castle> blackCastles = chessBoard.getCastles(getColour());
                if (blackCastles.isEmpty() || isCheck) {
                    return castlingMoves;
                } else {
                    while (!blackCastles.isEmpty()) {
                        Castle castle = blackCastles.remove(blackCastles.size() - 1);
                        if (castle.castlingPossible() && castle.getRow() == getRow()) {
                            if (chessBoard.isEmptySubRow(getPos(), castle.getPos())) {
                                if (getColumn() > castle.getColumn()) {
                                    // King is on the right side of the castle
                                    PiecePosition p = new PiecePosition(getRow(), getColumn() - 2);
                                    if (isSafePosition(p)
                                            && isSafePosition(new PiecePosition(getRow(), getColumn() - 1))) {
                                        castlingMoves.add(p);
                                    }
                                } else {
                                    // King is on the left side of the castle
                                    PiecePosition p = new PiecePosition(getRow(), getColumn() + 2);
                                    if (isSafePosition(p)
                                            && isSafePosition(new PiecePosition(getRow(), getColumn() + 1))) {
                                        castlingMoves.add(p);
                                    }
                                }
                            }

                        }
                    }
                    return castlingMoves;
                }
            case White:
                ArrayList<Castle> whiteCastles = chessBoard.getCastles(getColour());
                if (whiteCastles.isEmpty() || isCheck) {
                    return castlingMoves;
                } else {
                    while (!whiteCastles.isEmpty()) {
                        Castle castle = whiteCastles.remove(whiteCastles.size() - 1);
                        if (castle.castlingPossible() && castle.getRow() == getRow()) {
                            if (chessBoard.isEmptySubRow(getPos(), castle.getPos())) {
                                if (getColumn() > castle.getColumn()) {
                                    // King is on the right side of the castle
                                    PiecePosition p = new PiecePosition(getRow(), getColumn() - 2);
                                    if (isSafePosition(p)
                                            && isSafePosition(new PiecePosition(getRow(), getColumn() - 1))) {
                                        castlingMoves.add(p);
                                    }
                                } else {
                                    // King is on the left side of the castle
                                    PiecePosition p = new PiecePosition(getRow(), getColumn() + 2);
                                    if (isSafePosition(p)
                                            && isSafePosition(new PiecePosition(getRow(), getColumn() + 1))) {
                                        castlingMoves.add(p);
                                    }
                                }
                            }
                        }
                    }
                    return castlingMoves;
                }

            default:
                return null;
        }
    }

    /**
     *
     * @param pos The position.
     * @return <code>True</code> if the position is a safe position. This means
     * that the king cannot be captured on this position and that it is not
     * chess after moving to this position. <code>False</code> otherwise.
     */
    public boolean isSafePosition(PiecePosition pos) {
        for (int row = 0; row < ChessBoard.ROWS; row++) {
            for (int column = 0; column < ChessBoard.COLS; column++) {
                if (chessBoard.isOccupiedPosition(row, column)
                        && chessBoard.getColour(row, column) != getColour()) {
                    ChessPiece piece = chessBoard.getPiece(row, column);
                    if (piece.posCanBeCaptured(pos) || piece.posIsCovered(pos)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the king can capture this position. It can only do so if
     * this position is free and within the range of the kings current position.
     *
     * @param p The position on the board.
     * @return <code>True</code> if the position can be captured by this piece,
     * <code>False</code> otherwise.
     */
    @Override
    public boolean posCanBeCaptured(PiecePosition p) {
        int distRow = Math.abs(p.getRow() - getRow());
        int distCol = Math.abs(p.getColumn() - getColumn());
        if (!p.isValid()) {
            // The position itself is not valid, so we return false
            return false;
        }
        if (distRow > 1 || distCol > 1 || p.equals(getPos())) {
            /*
             The position is not within reach or is the same as the current
             position of the king.
             */
            return false;
        } else if (!chessBoard.isOccupiedPosition(p)) {
            // This position is not occupied and within reach (previous if)
            return true;
        } else {
            /*
             We can only capture a position if the piece on it has a different
             colour than this piece has.
             */
            return chessBoard.getColour(p) != getColour();
        }
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        for (int row = getRow() - 1; row <= getRow() + 1; row++) {
            for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                PiecePosition newPos = new PiecePosition(row, column);
                if (newPos.isValid() && !(newPos.equals(getPos())) && newPos.equals(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the value isCheck.
     */
    public boolean isCheck() {
        return isCheck;
    }

    /**
     *
     * @return <code>True</code> if castling is possible, <code>False</code>
     * otherwise.
     */
    public boolean castlingPossible() {
        return castlingPossible;
    }

    /**
     * Setter for the isCheck value.
     *
     * @param value the new value for isCheck.
     */
    public void setCheck(boolean value) {
        isCheck = value;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.isCheck ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
