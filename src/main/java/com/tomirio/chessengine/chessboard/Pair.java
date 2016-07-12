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
public class Pair {

    /**
     * The moves
     */
    public ArrayList<PiecePosition> moves;
    /**
     * The covered positions
     */
    public ArrayList<PiecePosition> covered;

    /**
     * initializes both lists
     */
    public Pair() {
        moves = new ArrayList<>();
        covered = new ArrayList<>();
    }

    /**
     * Add the result of the specified pair to this pair.
     *
     * @param p The pair
     */
    public void add(Pair p) {
        moves.addAll(p.moves);
        covered.addAll(p.covered);
    }

}
