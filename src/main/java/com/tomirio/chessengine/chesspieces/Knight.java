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
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.moves.CaptureMove;
import com.tomirio.chessengine.moves.Move;
import com.tomirio.chessengine.moves.NormalMove;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class Knight extends ChessPiece {

    /**
     * Constructor for new knight, use when the board is known at this moment.
     * The board MUST be manually set using the setBoard method.
     *
     * @param colour The colour.
     * @param pos The position.
     */
    public Knight(ChessColour colour, Position pos) {
        super(PieceType.Knight, colour, pos);
    }

    @Override
    public ArrayList<Position> getCoveredPositions() {
        return getKnightPositions().coveredFriendlyPieces;
    }

    /**
     *
     * @return A pair containing all of the possible moves and all of the
     * covered positions.
     */
    private MoveDetails getKnightPositions() {
        MoveDetails moveDetails = new MoveDetails();
        int orgRow = getRow();
        int orgCol = getColumn();
        for (int r = orgRow - 2; r <= orgRow + 2; r++) {
            for (int c = orgCol - 2; c <= orgCol + 2; c++) {
                /*
                 difference in row is 1 and in column is 2
                 or
                 difference in row is 2 and in column is 1
                 */
                if (chessBoard.isValidCoordinate(r, c)) {
                    Position p = new Position(r, c);
                    int distRow = Math.abs(orgRow - r);
                    int distCol = Math.abs(orgCol - c);
                    if ((distRow == 1 && distCol == 2) || (distRow == 2 && distCol == 1)) {
                        if (!chessBoard.isOccupiedPosition(p)) {
                            // Normal move
                            NormalMove normalMove = new NormalMove(this, p);
                            moveDetails.moves.add(normalMove);
                        } else if (chessBoard.getColour(p) != getColour()) {
                            // Capture move
                            CaptureMove captureMove = new CaptureMove(this, p);
                            moveDetails.moves.add(captureMove);
                        } else {
                            moveDetails.coveredFriendlyPieces.add(p);
                        }
                    }
                }
            }
        }
        return moveDetails;
    }

    @Override
    public ArrayList<Move> getPossibleMoves() {
        return filterMoves(getKnightPositions().moves);
    }

    @Override
    public ArrayList<Move> getRawPossibleMoves() {
        return getKnightPositions().moves;
    }

    @Override
    public boolean posIsCovered(Position p) {
        return getKnightPositions().coveredFriendlyPieces.contains(p);
    }

}
