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

import com.tomirio.chessengine.agent.PieceSquareTables;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.ChessTypes;
import com.tomirio.chessengine.chessboard.Direction;
import com.tomirio.chessengine.chessboard.Pair;
import com.tomirio.chessengine.chessboard.PiecePosition;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class Bishop extends ChessPiece {

    /**
     * Constructor for new bishop, use when the board is not known. The board
     * MUST be manually set using the setBoard method.
     *
     * @param type
     * @param colour
     * @param pos
     */
    public Bishop(ChessTypes type, ChessColour colour, PiecePosition pos) {
        super(type, colour, pos);
        pieceValue = 330;
    }

    /**
     *
     * @return All the possible moves for the bishop.
     */
    public Pair getBishopPositions() {
        Pair pair = new Pair();
        pair.add(getPositionsInDirection(Direction.NE));
        pair.add(getPositionsInDirection(Direction.SE));
        pair.add(getPositionsInDirection(Direction.SW));
        pair.add(getPositionsInDirection(Direction.NW));
        return pair;
    }

    @Override
    public boolean posCanBeCaptured(PiecePosition p) {
        return getBishopPositions().moves.contains(p);
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getBishopPositions().moves);
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        return getBishopPositions().covered.contains(p);
    }

    @Override
    public int evaluatePosition() {
        int weight = 0;
        switch (this.getColour()) {
            case White:
                weight = PieceSquareTables.BISHOP_TABLE[getPos().getRow()][getPos().getColumn()];
                break;
            case Black:
                //mirrored access
                weight = PieceSquareTables.BISHOP_TABLE[7 - getPos().getRow()][getPos().getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return pieceValue + weight;
    }

}
