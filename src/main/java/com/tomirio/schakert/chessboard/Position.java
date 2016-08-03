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

/**
 *
 * @author Tom Sandmann
 */
public class Position {

    /**
     * The column.
     */
    private int column;
    /**
     * The row.
     */
    private int row;

    /**
     *
     * @param row The row where the chess piece is currently residing
     * @param column The column where the chess piece is currently residing
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Create a new position based on algebraic notation.
     *
     * @see
     * <a href="http://www.dummies.com/how-to/content/naming-ranks-and-files-in-chess.html">
     * http://www.dummies.com/how-to/content/naming-ranks-and-files-in-chess.html
     * </a>
     * @param algebraicNotation The algebraic notatin of the position.
     */
    public Position(String algebraicNotation) {
        if (algebraicNotation.length() != 2) {
            throw new IllegalArgumentException("The position " + algebraicNotation + " is not in valid algebraic notation!");
        }
        // To be converted to the column.
        Character file = algebraicNotation.charAt(0);
        // To be converted to the row.
        Character rank = algebraicNotation.charAt(1);
        if (!Character.isDigit(rank)) {
            throw new IllegalArgumentException("The rank should be a numerical value!");
        }

        int rankDigit = Character.getNumericValue(rank);

        if (rankDigit > 8 || rankDigit < 1) {
            throw new IllegalArgumentException("The value of the rank should lie between 1 and 8!");
        }
        if (Character.isDigit(file)) {
            throw new IllegalArgumentException("The file should not be a numerical value!");
        }

        int fileASCII = (int) file;

        if (fileASCII < 97 || fileASCII > 104) {
            throw new IllegalArgumentException("The file should lie between 'a' and 'h'!");
        }
        /*
        This substraction gives a row value of 0 for rank "8" and a column 
        value of 7 for rank "1".
         */
        this.row = 8 - rankDigit;
        /*
        This substraction gives a column value of 0 for file "a" and a column 
        value of 7 for file "h".
         */
        this.column = fileASCII - 97;

    }

    /**
     *
     * @return Deep clone of this PiecePosition.
     */
    public Position deepClone() {
        return new Position(row, column);
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
            Position otherPosition = (Position) otherObject;
            return (otherPosition.column == column && otherPosition.row == row);
        }
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
     * @return The row of the chess piece
     */
    public int getRow() {
        return this.row;
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
     * @param row The new row of the chess piece
     * @param column The new column of the chess piece
     */
    public void setPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     *
     * @return String representation of this PiecePosition
     */
    @Override
    public String toString() {
        int rank = 8 - row;
        String file = Character.toString((char) ('a' + column));
        return file + rank;
    }

}
