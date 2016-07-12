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
import com.tomirio.chessengine.chessboard.Direction;
import com.tomirio.chessengine.chessboard.Pair;
import com.tomirio.chessengine.chessboard.PiecePosition;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import javafx.scene.image.Image;

/**
 *
 * @author Tom Sandmann
 */
public class Castle extends ChessPiece {

    private boolean castlingPossible;

    /**
     * Constructor for new castle, use when the board is known at this moment.
     * The board MUST be manually set using the setBoard method.
     *
     * @param type The type of this chess piece, must always be
     * ChessTypes.Castle
     * @param colour The colour of this chess piece
     * @param pos The position of this chess piece
     * @param chessImage The image that belongs to this chess piece
     */
    public Castle(ChessTypes type, ChessColour colour, PiecePosition pos, Image chessImage) {
        super(type, colour, pos, chessImage);
        castlingPossible = true;
        pieceValue = 500;
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getCastlePositions());
    }

    private ArrayList<PiecePosition> getCastlePositions() {
        ArrayList<PiecePosition> moves = new ArrayList<>();
        moves.addAll(getPositionsInDirection(Direction.N).moves);
        moves.addAll(getPositionsInDirection(Direction.E).moves);
        moves.addAll(getPositionsInDirection(Direction.S).moves);
        moves.addAll(getPositionsInDirection(Direction.W).moves);
        return moves;
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        Pair pair = new Pair();
        pair.covered.addAll(getPositionsInDirection(Direction.N).covered);
        pair.covered.addAll(getPositionsInDirection(Direction.E).covered);
        pair.covered.addAll(getPositionsInDirection(Direction.S).covered);
        pair.covered.addAll(getPositionsInDirection(Direction.W).covered);
        return pair.covered.contains(p);
    }

    @Override
    public void move(int row, int column) {
        castlingPossible = false;
        super.move(row, column);
    }

    /**
     * Returns the variable castlingPossible
     *
     * @return <code>True</code> if castling is possible <code>False</code>
     * otherwise.
     */
    public boolean getCastlingPossible() {
        return castlingPossible;
    }

    /**
     *
     * @param o The object.
     * @return <code>True</code> if the object is equal to the instance of this
     * class. <code>False</code> otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.castlingPossible ? 1 : 0);
        return hash;
    }

    @Override
    public boolean posCanBeCaptured(PiecePosition pos) {
        return getCastlePositions().contains(pos);
    }

    @Override
    public int evaluatePosition() {
        int weight = 0;
        switch (getColour()) {
            case White:
                weight = pieceSquareTables.castle_table[getPos().getRow()][getPos().getColumn()];
                break;
            case Black:
                //mirrored access
                weight = pieceSquareTables.castle_table[7 - getPos().getRow()][getPos().getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return pieceValue + weight;
    }

}
