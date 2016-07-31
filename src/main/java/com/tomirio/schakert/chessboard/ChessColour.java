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
public enum ChessColour {

    /**
     * The colour black.
     */
    Black,
    /**
     * The colour white.
     */
    White;

    /**
     *
     * @return The opposite colour of this chess colour.
     */
    public ChessColour getOpposite() {
        switch (this) {
            case Black:
                return ChessColour.White;
            case White:
                return ChessColour.Black;
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     *
     * @return String representation of ChessColour.
     */
    @Override
    public String toString() {
        switch (this) {
            case Black:
                return "Black";

            case White:
                return "White";
            default:
                throw new NoSuchElementException();
        }
    }

}