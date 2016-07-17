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
package com.tomirio.chessengine.chessboard;

import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class MoveDetails {

    /**
     * The moves
     */
    public ArrayList<PiecePosition> moves;
    /**
     * The covered positions
     */
    public ArrayList<PiecePosition> coveredFriendlyPieces;

    /**
     * initializes both lists
     */
    public MoveDetails() {
        moves = new ArrayList<>();
        coveredFriendlyPieces = new ArrayList<>();
    }

    public MoveDetails(ArrayList<PiecePosition> moves, ArrayList<PiecePosition> coveredFriendlyPieces) {
        this.moves = moves;
        this.coveredFriendlyPieces = coveredFriendlyPieces;
    }

    /**
     * Add new possible moves.
     *
     * @param newMoves List of possible moves.
     */
    public void addMoves(ArrayList<PiecePosition> newMoves) {
        moves.addAll(newMoves);
    }

    /**
     * Add new covered pieces
     *
     * @param newCoveredFriendlyPieces List of covered friendly pieces.
     */
    public void addCoveredPieces(ArrayList<PiecePosition> newCoveredFriendlyPieces) {
        coveredFriendlyPieces.addAll(coveredFriendlyPieces);
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

}
