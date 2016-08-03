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
package com.tomirio.schakert.chesspieces;

import com.tomirio.schakert.chessboard.Direction;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.moves.Move;
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
    public Bishop(Colour colour, Position pos) {
        super(PieceType.Bishop, colour, pos);
    }

    /**
     *
     * @return All the possible moves for the bishop.
     */
    public MoveDetails getBishopMoves() {
        MoveDetails moveDetails = new MoveDetails();
        moveDetails.add(getPositionsInDirection(Direction.NE));
        moveDetails.add(getPositionsInDirection(Direction.SE));
        moveDetails.add(getPositionsInDirection(Direction.SW));
        moveDetails.add(getPositionsInDirection(Direction.NW));
        return moveDetails;
    }

    @Override
    public ArrayList<Position> getCoveredPositions() {
        return getBishopMoves().coveredFriendlyPieces;
    }

    @Override
    public ArrayList<Move> getPossibleMoves() {
        return filterMoves(getBishopMoves().moves);
    }

    @Override
    public ArrayList<Move> getRawPossibleMoves() {
        return getBishopMoves().moves;
    }

    @Override
    public boolean posIsCovered(Position p) {
        return getCoveredPositions().contains(p);
    }

}
