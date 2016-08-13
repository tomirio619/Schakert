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
package com.tomirio.schakert.chessboard;

import com.tomirio.schakert.moves.Move;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class MoveDetails {

    /**
     * The covered positions
     */
    public ArrayList<Position> coveredFriendlyPieces;
    /**
     * The moves
     */
    public ArrayList<Move> moves;

    /**
     * Initializes both lists.
     */
    public MoveDetails() {
        moves = new ArrayList<>();
        coveredFriendlyPieces = new ArrayList<>();
    }

    /**
     *
     * @param moves The moves.
     * @param coveredFriendlyPieces The covered friendly pieces.
     */
    public MoveDetails(ArrayList<Move> moves, ArrayList<Position> coveredFriendlyPieces) {
        this.moves = moves;
        this.coveredFriendlyPieces = coveredFriendlyPieces;
    }

    /**
     * Add the result of the specified pair to this pair.
     *
     * @param moveDetails The pair
     */
    public void add(MoveDetails moveDetails) {
        moves.addAll(moveDetails.moves);
        coveredFriendlyPieces.addAll(moveDetails.coveredFriendlyPieces);
    }

    /**
     * Add new covered pieces
     *
     * @param newCoveredFriendlyPieces List of covered friendly pieces.
     */
    public void addCoveredPieces(ArrayList<Move> newCoveredFriendlyPieces) {
        coveredFriendlyPieces.addAll(coveredFriendlyPieces);
    }

    /**
     * Add new possible moves.
     *
     * @param newMoves List of possible moves.
     */
    public void addMoves(ArrayList<Move> newMoves) {
        moves.addAll(newMoves);
    }

    @Override
    public String toString() {
        String movesString = "";
        for (Move move : moves) {
            movesString += "Move: " + move.getInvolvedPiece() + " to " + move.getNewPos() + "\n";
        }
        return "Number of moves:" + moves.size() + "\n" + movesString;
    }
}
