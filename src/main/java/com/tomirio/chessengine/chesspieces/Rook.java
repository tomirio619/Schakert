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
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Direction;
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PiecePosition;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class Rook extends ChessPiece {

    private boolean castlingPossible;

    /**
     * Constructor for new rook, use when the board is known at this moment.
     * The board MUST be manually set using the setBoard method.

* @param colour The colour of this chess piece
     * @param pos The position of this chess piece
     */
    public Rook(ChessColour colour, PiecePosition pos) {
        super(PieceType.Rook, colour, pos);
        castlingPossible = true;
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getRookPositions());
    }

    private ArrayList<PiecePosition> getRookPositions() {
        ArrayList<PiecePosition> moves = new ArrayList<>();
        moves.addAll(getPositionsInDirection(Direction.N).moves);
        moves.addAll(getPositionsInDirection(Direction.E).moves);
        moves.addAll(getPositionsInDirection(Direction.S).moves);
        moves.addAll(getPositionsInDirection(Direction.W).moves);
        return moves;
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        MoveDetails moveDetails = new MoveDetails();
        moveDetails.coveredFriendlyPieces.addAll(getPositionsInDirection(Direction.N).coveredFriendlyPieces);
        moveDetails.coveredFriendlyPieces.addAll(getPositionsInDirection(Direction.E).coveredFriendlyPieces);
        moveDetails.coveredFriendlyPieces.addAll(getPositionsInDirection(Direction.S).coveredFriendlyPieces);
        moveDetails.coveredFriendlyPieces.addAll(getPositionsInDirection(Direction.W).coveredFriendlyPieces);
        return moveDetails.coveredFriendlyPieces.contains(p);
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
     * @return <code>True</code> if castling is possible, <code>False</code>
     * otherwise.
     */
    public boolean castlingPossible() {
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
        return getRookPositions().contains(pos);
    }
}
