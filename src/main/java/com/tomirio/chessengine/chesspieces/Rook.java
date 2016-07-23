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
import com.tomirio.chessengine.chessboard.Direction;
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.moves.Move;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class Rook extends ChessPiece {

    private boolean castlingPossible;

    /**
     * Constructor for new rook, use when the board is known at this moment. The
     * board MUST be manually set using the setBoard method.
     *
     * @param colour The colour of this chess piece
     * @param pos The position of this chess piece
     */
    public Rook(ChessColour colour, Position pos) {
        super(PieceType.Rook, colour, pos);
        castlingPossible = true;
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
     * @return <code>True</code> if castling is possible, <code>False</code>
     * otherwise.
     */
    public boolean getCastlingPossible() {
        return castlingPossible;
    }

    /**
     * Set new value for castling possible variable.
     *
     * @param newValue
     */
    public void setCastlingPossible(boolean newValue) {
        castlingPossible = newValue;
    }

    @Override
    public ArrayList<Position> getCoveredPositions() {
        return getRookMoves().coveredFriendlyPieces;
    }

    @Override
    public ArrayList<Move> getPossibleMoves() {
        return filterMoves(getRookMoves().moves);
    }

    @Override
    public ArrayList<Move> getRawPossibleMoves() {
        return getRookMoves().moves;
    }

    private MoveDetails getRookMoves() {
        MoveDetails moveDetails = new MoveDetails();
        moveDetails.add(getPositionsInDirection(Direction.N));
        moveDetails.add(getPositionsInDirection(Direction.E));
        moveDetails.add(getPositionsInDirection(Direction.S));
        moveDetails.add(getPositionsInDirection(Direction.W));
        return moveDetails;
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
    public boolean posIsCovered(Position p) {
        return getRookMoves().coveredFriendlyPieces.contains(p);
    }

    @Override
    public String toString() {
        return super.toString() + " castlingPossible: " + this.castlingPossible;
    }
}
