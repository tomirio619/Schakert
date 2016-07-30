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

import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public enum Direction {

    /**
     * North
     */
    N,
    /**
     * North East
     */
    NE,
    /**
     * East
     */
    E,
    /**
     * South East
     */
    SE,
    /**
     * South
     */
    S,
    /**
     * South West
     */
    SW,
    /**
     * West
     */
    W,
    /**
     * North West
     */
    NW;

    /**
     *
     * @return String representation of this Direction
     */
    @Override
    public String toString() {
        switch (this) {
            case N:
                return "North";
            case NE:
                return "North East";
            case E:
                return "East";
            case SE:
                return "South East";
            case S:
                return "South";
            case SW:
                return "South West";
            case W:
                return "West";
            case NW:
                return "North West";
            default:
                throw new NoSuchElementException();
        }
    }
}
