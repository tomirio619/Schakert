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

import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.ChessTypes;
import com.tomirio.chessengine.chessboard.Pair;
import com.tomirio.chessengine.chessboard.PiecePosition;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import javafx.scene.image.Image;

/**
 *
 * @author Tom Sandmann
 */
public class Pawn extends ChessPiece {

    /**
     * For the pawn only, indicates if an enPassant can be done on this pawn.
     */
    protected boolean enPassantPossible;

    /**
     * This constructor <b>MUST</b> be used when the chessboard is not known
     * when a new chess piece is made. The chessboard must be set later on with
     * <code>setBoard()</code>. It is called with a <code>PiecePosition</code>.
     *
     * @param type The type of the chess piece.
     * @param colour The colour of the chess piece.
     * @param pos The position of the chess piece.
     * @param chessImage The image that belongs to this chess piece.
     */
    public Pawn(ChessTypes type, ChessColour colour, PiecePosition pos, Image chessImage) {
        super(type, colour, pos, chessImage);
        enPassantPossible = false;
        pieceValue = 100;

    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getPawnPositions().moves);
    }

    private Pair getPawnPositions() {
        Pair pair = new Pair();
        switch (getColour()) {
            case White: {
                if (getRow() == 6) {
                    // White pawn is on its original position
                    PiecePosition p1 = new PiecePosition(getRow() - 1, getColumn());
                    if (!board.isOccupiedPosition(p1)) {
                        // Position in front of pawn is free
                        pair.moves.add(p1);
                    }
                    if (!board.isOccupiedPosition(p1)) {
                        // Position in front of pawn is free
                        PiecePosition p2 = new PiecePosition(getRow() - 2, getColumn());
                        if (!board.isOccupiedPosition(p2)) {
                            // Pawn can move two tiles
                            pair.moves.add(p2);
                        }
                    }
                }
                for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                    PiecePosition p1 = new PiecePosition(getRow() - 1, column);
                    if (p1.isValid()) {
                        if (column == getColumn() && !board.isOccupiedPosition(p1)) {
                            pair.moves.add(p1);
                        } else if (board.isOccupiedPosition(p1) && board.getColour(p1) != getColour() && p1.getColumn() != getColumn()) {
                            pair.moves.add(p1);
                        } else if (board.isOccupiedPosition(p1) && board.getColour(p1) == getColour() && p1.getColumn() != getColumn()) {
                            pair.covered.add(p1);
                        }
                    }
                }
                // Determine if enPassant move is possible
                for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                    PiecePosition p1 = new PiecePosition(getRow(), column);
                    if (p1.isValid()) {
                        if (board.isOccupiedPosition(p1) && board.getColour(p1) != getColour() && board.getPiece(p1) instanceof Pawn) {
                            Pawn pawn = (Pawn) board.getPiece(p1);
                            if (pawn.getEnPassantMovePossible() && p1.getRow() == getRow() && !board.isOccupiedPosition(p1.getRow() - 1, p1.getColumn())) {
                                // Both pawns are on the same row and behind the black pawn is a free tile, so we can add this as a possible move
                                pair.moves.add(new PiecePosition(p1.getRow() - 1, p1.getColumn()));
                            }
                        }
                    }

                }
                return pair;
            }

            case Black: {
                if (getRow() == 1) {
                    PiecePosition p1 = new PiecePosition(getRow() + 1, getColumn());
                    if (!board.isOccupiedPosition(p1)) {
                        pair.moves.add(p1);
                    }
                    if (!board.isOccupiedPosition(p1)) {
                        PiecePosition p2 = new PiecePosition(getRow() + 2, getColumn());
                        if (!board.isOccupiedPosition(p2)) {
                            pair.moves.add(p2);
                        }
                    }
                }
                for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                    PiecePosition p1 = new PiecePosition(getRow() + 1, column);
                    if (p1.isValid()) {
                        if (column == getColumn() && !board.isOccupiedPosition(p1)) {
                            pair.moves.add(p1);
                        } else if (board.isOccupiedPosition(p1) && board.getColour(p1) != getColour() && p1.getColumn() != getColumn()) {
                            pair.moves.add(p1);
                        } else if (board.isOccupiedPosition(p1) && board.getColour(p1) == getColour() && p1.getColumn() != getColumn()) {
                            pair.covered.add(p1);
                        }
                    }
                }

                // Determine if enPassant move is possible
                for (int column = getColumn() - 1; column <= getColumn() + 1; column++) {
                    PiecePosition p1 = new PiecePosition(getRow(), column);
                    if (p1.isValid()) {
                        if (board.isOccupiedPosition(p1) && board.getColour(p1) != getColour() && board.getPiece(p1) instanceof Pawn) {
                            Pawn pawn = (Pawn) board.getPiece(p1);
                            if (pawn.getEnPassantMovePossible() && p1.getRow() == getRow() && !board.isOccupiedPosition(p1.getRow() + 1, p1.getColumn())) {
                                // Both pawns are on the same row and behind the white pawn is a free tile, so we can add this as a possible move
                                pair.moves.add(new PiecePosition(p1.getRow() + 1, p1.getColumn()));
                            }
                        }
                    }
                }
                return pair;
            }
            default:
                throw new NoSuchElementException(getColour().toString());
        }
    }

    /**
     *
     * @return The value of enPassantPossible
     */
    public boolean getEnPassantMovePossible() {
        return enPassantPossible;
    }

    @Override
    public boolean posIsCovered(PiecePosition pos) {
        return getPawnPositions().covered.contains(pos);
    }

    /**
     *
     * @param newRow The new row of the piece
     */
    public void determineEnPasant(int newRow) {
        if (enPassantPossible) {
            /*
             enPassant move was possible and now the pawn is moving, so the enPassant
             move is not possible anymore for this pawn
             */
            enPassantPossible = false;
        } else {
            //The pawn just moved 2 tiles, this indicaties that the enPassant move is possible
            enPassantPossible = Math.abs(newRow - getRow()) == 2;
        }
    }

    /**
     *
     * @param row The row of the new position of the piece.
     * @param column The column of the new position of the piece.
     */
    @Override
    public void move(int row, int column) {
        determineEnPasant(row);
        super.move(row, column);
    }

    /**
     * Setter for the enPassantPossible value.
     *
     * @param value
     */
    public void setEnPassantMove(boolean value) {
        this.enPassantPossible = value;
    }

    @Override
    public boolean posCanBeCaptured(PiecePosition pos) {
        int distRow = Math.abs(pos.getRow() - getRow());
        int distCol = Math.abs(pos.getColumn() - getColumn());
        if (distRow > 1 || distCol > 1 || pos.getRow() == getRow() || pos.getColumn() == getColumn()) {
            return false;
        } else {
            switch (getColour()) {
                case Black: {
                    return (pos.getRow() > getRow());
                }
                case White: {
                    return (pos.getRow() < getRow());
                }
                default:
                    throw new NoSuchElementException(getColour().toString());

            }
        }
    }

    @Override
    public int evaluatePosition() {
        int weight = 0;
        switch (this.getColour()) {
            case White:
                weight = pieceSquareTables.pawn_table[getPos().getRow()][getPos().getColumn()];
                break;
            case Black:
                weight = pieceSquareTables.pawn_table[7 - getPos().getRow()][getPos().getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return pieceValue + weight;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.enPassantPossible ? 1 : 0);
        return hash;
    }

}
