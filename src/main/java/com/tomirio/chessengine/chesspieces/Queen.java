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
import com.tomirio.chessengine.chessboard.Direction;
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PiecePosition;
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
     * @param type The chesstype.
     * @param colour The colour.
     * @param pos The position.
     * @param board The board.
     */
    public Queen(ChessTypes type, ChessColour colour, PiecePosition pos, ChessBoard board) {
        super(type, colour, pos, board);
    }

    /**
     * Constructor for new queen, use when the board is at this moment.
     *
     * @param type The chesstype.
     * @param colour The colour.
     * @param pos The position.
     */
    public Queen(ChessTypes type, ChessColour colour, PiecePosition pos) {
        super(type, colour, pos);
    }

    @Override
    public boolean posCanBeCaptured(PiecePosition pos) {
        return getQueenMoves().contains(pos);
    }

    /**
     * @return All the possible moves for the queen.
     */
    public ArrayList<PiecePosition> getQueenMoves() {
        ArrayList<PiecePosition> moves = new ArrayList<>();
        for (Direction d : Direction.values()) {
            moves.addAll(getPositionsInDirection(d).moves);
        }
        return moves;
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        MoveDetails moveDetails = new MoveDetails();
        for (Direction d : Direction.values()) {
            moveDetails.add(getPositionsInDirection(d));
        }
        return moveDetails.coveredFriendlyPieces.contains(p);
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getQueenMoves());
    }

}
