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
public class Bishop extends ChessPiece {

    /**
     * Constructor for new bishop, use when the board is not known. The board
     * MUST be manually set using the setBoard method.
     *
     * @param colour
     * @param pos
     */
    public Bishop(ChessColour colour, PiecePosition pos) {
        super(PieceType.Bishop, colour, pos);
    }

    /**
     *
     * @return All the possible moves for the bishop.
     */
    public MoveDetails getBishopPositions() {
        MoveDetails moveDetails = new MoveDetails();
        moveDetails.add(getPositionsInDirection(Direction.NE));
        moveDetails.add(getPositionsInDirection(Direction.SE));
        moveDetails.add(getPositionsInDirection(Direction.SW));
        moveDetails.add(getPositionsInDirection(Direction.NW));
        return moveDetails;
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
        return getBishopPositions().coveredFriendlyPieces.contains(p);
    }

}
