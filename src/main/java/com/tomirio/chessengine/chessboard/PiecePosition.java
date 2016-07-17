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

import java.io.Serializable;

/**
 *
 * @author Tom Sandmann
 */
public class PiecePosition implements Serializable {

    /**
     * The row.
     */
    private int row;

    /**
     * The column.
     */
    private int column;

    /**
     *
     * @param row The row where the chess piece is currently residing
     * @param column The column where the chess piece is currently residing
     */
    public PiecePosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     *
     * @param row The new row of the chess piece
     * @param column The new column of the chess piece
     */
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     *
     * @return The row of the chess piece
     */
    public int getRow() {
        return this.row;
    }

    /**
     *
     * @return The column of the chess piece
     */
    public int getColumn() {
        return this.column;
    }

    /**
     *
     * @return <code>True</code> if the position is within the board:      <pre>
     * {@code 0 <= row <= 7  && 0 <= column <= 7}
     * </pre>. <code>False</code> otherwise.
     *
     */
    public boolean isValid() {
        return column >= 0 && column <= 7 && row >= 0 && row <= 7;
    }

    /**
     *
     * @return Deep clone of this PiecePosition.
     */
    public PiecePosition deepClone() {
        return new PiecePosition(row, column);
    }

    /**
     *
     * @return String representation of this PiecePosition
     */
    @Override
    public String toString() {
        switch (column) {
            case 0:
                return "(" + "a" + "," + (8 - row) + ")";
            case 1:
                return "(" + "b" + "," + (8 - row) + ")";
            case 2:
                return "(" + "c" + "," + (8 - row) + ")";
            case 3:
                return "(" + "d" + "," + (8 - row) + ")";
            case 4:
                return "(" + "e" + "," + (8 - row) + ")";
            case 5:
                return "(" + "f" + "," + (8 - row) + ")";
            case 6:
                return "(" + "g" + "," + (8 - row) + ")";
            case 7:
                return "(" + "h" + "," + (8 - row) + ")";
            default:
                throw new IndexOutOfBoundsException("The position"
                        + "(" + row + ", " + column + ")"
                        + " is not within the board!");
        }
    }

    /**
     *
     * @param otherObject
     * @return <code>True</code> if the otherObject is equal to this instance of
     * PiecePosition, <code>False</code> otherwise.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        } else if (getClass() != otherObject.getClass()) {
            return false;
        } else {
            PiecePosition otherPosition = (PiecePosition) otherObject;
            return (otherPosition.column == column && otherPosition.row == row);
        }
    }

    /**
     *
     * @return The hashcode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.row;
        hash = 23 * hash + this.column;
        return hash;
    }
}
