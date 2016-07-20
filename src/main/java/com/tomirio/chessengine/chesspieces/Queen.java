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
public class Queen extends ChessPiece {

    /**
     * Constructor for new queen, use when the board is known at this moment.
     * The board MUST be manually set using the setBoard method.
     *
     * @param colour The colour.
     * @param pos The position.
     * @param board The board.
     */
    public Queen(ChessColour colour, Position pos, ChessBoard board) {
        super(PieceType.Queen, colour, pos, board);
    }

    /**
     * Constructor for new queen, use when the board is at this moment.
     *
     * @param colour The colour.
     * @param pos The position.
     */
    public Queen(ChessColour colour, Position pos) {
        super(PieceType.Queen, colour, pos);
    }

    @Override
    public ArrayList<Position> getCoveredPositions() {
        return getQueenMoves().coveredFriendlyPieces;
    }

    @Override
    public ArrayList<Move> getPossibleMoves() {
        return filterMoves(getQueenMoves().moves);
    }

    /**
     * @return All the possible moves for the queen.
     */
    public MoveDetails getQueenMoves() {
        MoveDetails moveDetails = new MoveDetails();
        for (Direction d : Direction.values()) {
            moveDetails.add(getPositionsInDirection(d));
        }
        return moveDetails;
    }

    @Override
    public ArrayList<Move> getRawPossibleMoves() {
        return getQueenMoves().moves;
    }

    @Override
    public boolean posIsCovered(Position p) {
        MoveDetails moveDetails = new MoveDetails();
        for (Direction d : Direction.values()) {
            moveDetails.add(getPositionsInDirection(d));
        }
        return moveDetails.coveredFriendlyPieces.contains(p);
    }

}
