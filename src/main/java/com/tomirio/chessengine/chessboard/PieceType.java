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

import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public enum PieceType {

    /**
     * King
     *//**
     * King
     */
    King,
    /**
     * Queen
     */
    Queen,
    /**
     * Rook
     */
    Rook,
    /**
     * Bishop
     */
    Bishop,
    /**
     * Knight
     */
    Knight,
    /**
     * Pawn
     */
    Pawn;

    /**
     *
     * @return String representation of this ChessType
     */
    @Override
    public String toString() {
        switch (this) {
            case King:
                return "King";
            case Queen:
                return "Queen";
            case Rook:
                return "Rook";
            case Bishop:
                return "Bishop";
            case Knight:
                return "Knight";
            case Pawn:
                return "Pawn";
            default:
                throw new NoSuchElementException();
        }
    }

    public String toShortString() {
        switch (this) {
            case King:
                return "K";
            case Queen:
                return "Q";
            case Rook:
                return "R";
            case Bishop:
                return "B";
            case Knight:
                return "N";
            case Pawn:
                return "P";
            default:
                throw new NoSuchElementException();
        }

    }
}
