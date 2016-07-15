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
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 *
 * @author Tom Sandmann
 */
public abstract class ChessPiece implements Serializable {

    /**
     * The type of the chess piece.
     */
    private final ChessTypes type;

    /**
     * The colour of the chess piece.
     */
    private final ChessColour colour;

    /**
     * The current position of the chess piece.
     */
    private final PiecePosition pos;

    /**
     * The board, if a new chess piece is created, this board must be set with
     * the setBoard function, otherwise the value is null!
     */
    protected ChessBoard chessBoard;

    /**
     * The value of this piece. This value will be used in the evaluation
     * function.
     */
    public int pieceValue;

    /**
     * This constructor MUST be used when the chessboard is not known when a new
     * chess piece is made. The chessboard MUST be set later on with setBoard()
     * It is called with a PiecePosition.
     *
     * @param type The type of the chess piece.
     * @param colour The colour of the chess piece.
     * @param pos The position of the chess piece.
     */
    public ChessPiece(ChessTypes type, ChessColour colour, PiecePosition pos) {
        this.type = type;
        this.colour = colour;
        this.pos = pos;
    }

    /**
     * This constructor can be used if the chessboard is known when a new chess
     * piece is made. It is called with a PiecePosition.
     *
     * @param type The chess type.
     * @param colour The colour.
     * @param pos The position.
     * @param board The board.
     */
    public ChessPiece(ChessTypes type, ChessColour colour, PiecePosition pos,
            ChessBoard board) {
        this.type = type;
        this.colour = colour;
        this.pos = pos;
        this.chessBoard = board;
    }

    abstract public int evaluatePosition();

    /**
     *
     * @param newRow The new row.
     * @param newColumn The new column.
     */
    public void setPosition(int newRow, int newColumn) {
        pos.setPosition(newRow, newColumn);
    }

    /**
     * The new piecePosition.
     *
     * @param pos
     */
    public void setPosition(PiecePosition pos) {
        pos.setPosition(pos.getRow(), pos.getColumn());
    }

    /**
     *
     * @return The column of this chess piece.
     */
    public int getColumn() {
        return pos.getColumn();
    }

    /**
     *
     * @return The type of this chess piece.
     */
    public ChessTypes getType() {
        return type;
    }

    /**
     *
     * @return The row of this chess piece.
     */
    public int getRow() {
        return pos.getRow();
    }

    /**
     *
     * @return The position of the piece.
     */
    public PiecePosition getPos() {
        return pos;
    }

    /**
     *
     * @return The colour of this chess piece.
     */
    public ChessColour getColour() {
        return colour;
    }

    /**
     *
     * @param chessBoard The board containing all of the chess pieces,
     * <b>MUST</b>
     * be called when a new chess piece is created, otherwise the current value
     * of board will be <code>null</code>!
     */
    public void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    /**
     *
     * @return The string representation of this chess piece.
     */
    @Override
    public String toString() {
        return colour + "\t | " + pos + " | " + type;
    }

    /**
     * Move the chess piece to a new position.
     *
     * @param row The new row of this chess piece.
     * @param column The new column of this chess piece.
     */
    public void move(int row, int column) {
        chessBoard.movePiece(this, row, column);
    }

    /**
     * Make a move on the chess board without updating the GUI.
     *
     * @param row The new row of this chess piece.
     * @param column The new column of this chess piece.
     */
    public void agentMove(int row, int column) {
        chessBoard.movePieceAgent(this, row, column);
    }

    /**
     * From the set of possible moves, it only returns those moves that are
     * valid (<code>board.isValidMove</code>).
     *
     * @param moves The possible moves for this chess piece.
     * @return All the legal moves for this chess piece.
     */
    protected ArrayList<PiecePosition> filterMoves(ArrayList<PiecePosition> moves) {
        ArrayList<PiecePosition> validMoves = new ArrayList<>();
        while (!moves.isEmpty()) {
            PiecePosition move = moves.remove(moves.size() - 1);
            if (chessBoard.isValidMove(this, move.getRow(), move.getColumn())) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     *
     * @param pos The position.
     * @return <code>True</code> if this position can be captured by this chess
     * piece. <code>False</code> otherwise. Capture means that, if the given
     * position contains a chess piece, this piece could capture this piece. The
     * position indicates the position of the chess piece.
     */
    public abstract boolean posCanBeCaptured(PiecePosition pos);

    /**
     * This function determines if a given position is covered by this piece. A
     * piece covers another piece if this piece could capture this piece of the
     * same colour.
     *
     * @param pos The position
     * @return <code>True</code> if the position is covered by this piece,
     * <code>False</code> otherwise
     */
    public abstract boolean posIsCovered(PiecePosition pos);

    /**
     *
     * @return The possible moves for this chess piece. The chess piece can be
     * be of the following types: Knight, Bishop and Queen. All the other types
     * extend this class and override a couple of functions, including this one.
     */
    public abstract ArrayList<PiecePosition> getPossibleMoves();

    /**
     *
     * @param dir The direction for which you want all the possible moves.
     * @return All the possible moves in a specified direction. When the path
     * becomes blocked by either a friendly or a enemy piece, the functions
     * returns the currently found set of moves. The positions of the moves must
     * lie on the board. This means:      <pre>
     * {@code
     * 0 < = row <= 7  && 0 <= column <= 7
     * }
     * </pre>
     */
    public Pair getPositionsInDirection(Direction dir) {
        Pair pair = new Pair();
        PiecePosition newPos = pos;
        switch (dir) {
            case N: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() - 1, newPos.getColumn());
                    if (newPos.getRow() < 0) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case S: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() + 1, newPos.getColumn());
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case W: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow(), newPos.getColumn() - 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case E: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow(), newPos.getColumn() + 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case NW: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() - 1, newPos.getColumn() - 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case NE: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() - 1, newPos.getColumn() + 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case SW: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() + 1, newPos.getColumn() - 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }

            case SE: {
                Boolean condition = true;
                while (condition) {
                    newPos = new PiecePosition(newPos.getRow() + 1, newPos.getColumn() + 1);
                    if (!newPos.isValid()) {
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) != colour) {
                        pair.moves.add(newPos);
                        condition = false;
                    } else if (chessBoard.isOccupiedPosition(newPos)
                            && chessBoard.getColour(newPos) == colour) {
                        condition = false;
                        pair.covered.add(newPos);
                    } else {
                        pair.moves.add(newPos);
                    }
                }
                return pair;
            }
            default:
                throw new NoSuchElementException(dir.toString());
        }
    }

    /**
     *
     * @return The chess board.
     */
    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    /**
     *
     * @param otherObject The object this chess piece will be compared to.
     * @return <code>True</code> if this object equals this instance of chess
     * piece. <code>False</code> otherwise.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        } else if (getClass() != otherObject.getClass()) {
            return false;
        } else {
            ChessPiece otherPiece = (ChessPiece) otherObject;
            return (otherPiece.colour == colour && otherPiece.type == type
                    && otherPiece.pos == pos);
        }
    }

    /**
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.type);
        hash = 67 * hash + Objects.hashCode(this.colour);
        hash = 67 * hash + Objects.hashCode(this.pos);
        return hash;
    }
}
