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
public class Knight extends ChessPiece {

    /**
     * Constructor for new knight, use when the board is known at this moment.
     * The board MUST be manually set using the setBoard method.
     *
     * @param type The chess type.
     * @param colour The colour.
     * @param pos The position.
     * @param chessImage The image that belongs to this chess piece.
     */
    public Knight(ChessTypes type, ChessColour colour, PiecePosition pos, Image chessImage) {
        super(type, colour, pos, chessImage);
        pieceValue = 320;
    }

    /**
     *
     * @return A pair containing all of the possible moves and all of the
     * covered positions.
     */
    public Pair getKnightPositions() {
        Pair pair = new Pair();
        int orgRow = getRow();
        int orgCol = getColumn();
        for (int r = orgRow - 2; r <= orgRow + 2; r++) {
            for (int c = orgCol - 2; c <= orgCol + 2; c++) {
                /*
                 difference in row is 1 and in column is 2
                 or
                 difference in row is 2 and in column is 1
                 */
                if (board.isValidCoordinate(r, c)) {
                    PiecePosition p = new PiecePosition(r, c);
                    int distRow = Math.abs(orgRow - r);
                    int distCol = Math.abs(orgCol - c);
                    if ((distRow == 1 && distCol == 2) || (distRow == 2 && distCol == 1)) {
                        if (!board.isOccupiedPosition(p)) {
                            pair.moves.add(p);
                        } else if (board.getColour(p) != getColour()) {
                            pair.moves.add(p);
                        } else {
                            pair.covered.add(p);
                        }
                    }
                }
            }
        }
        return pair;
    }

    @Override
    public boolean posCanBeCaptured(PiecePosition p) {
        return this.getKnightPositions().moves.contains(p);
    }

    @Override
    public boolean posIsCovered(PiecePosition p) {
        return getKnightPositions().covered.contains(p);
    }

    @Override
    public ArrayList<PiecePosition> getPossibleMoves() {
        return filterMoves(getKnightPositions().moves);
    }

    @Override
    public int evaluatePosition() {
        int weight = 0;
        switch (this.getColour()) {
            case White:
                weight = pieceSquareTables.knight_table[getPos().getRow()][getPos().getColumn()];
                break;
            case Black:
                //mirrored access
                weight = pieceSquareTables.knight_table[7 - getPos().getRow()][getPos().getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return pieceValue + weight;
    }
}
