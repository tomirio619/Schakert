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
package com.tomirio.schakert.agent;

/**
 *
 * @author Tom Sandmann
 */
public class TranspositionEntry {

    private final int alpha;
    private boolean ancient;
    private final int beta;
    private final int depth;
    private final int hash;
    private final int score;

    public TranspositionEntry(int hash, int depth, int score, boolean ancient, int alpha, int beta) {
        this.hash = hash;
        this.depth = depth;
        this.score = score;
        this.ancient = ancient;
        this.alpha = alpha;
        this.beta = beta;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }

    public int getDepth() {
        return depth;
    }

    public int getHash() {
        return hash;
    }

    public int getScore() {
        return score;
    }

    public boolean isAncient() {
        return ancient;
    }

    public void setAncient(boolean ancient) {
        this.ancient = ancient;
    }

}
