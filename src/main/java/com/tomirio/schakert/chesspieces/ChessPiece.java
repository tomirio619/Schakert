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
package com.tomirio.schakert.chesspieces;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.ChessColour;
import com.tomirio.schakert.chessboard.Direction;
import com.tomirio.schakert.chessboard.MoveDetails;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.moves.CaptureMove;
import com.tomirio.schakert.moves.Move;
import com.tomirio.schakert.moves.NormalMove;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 *
 * @author Tom Sandmann
 */
public abstract class ChessPiece implements Serializable {

    /**
     * The board, if a new chess piece is created, this board must be set with
     * the setBoard function, otherwise the value is null!
     */
    protected ChessBoard chessBoard;

    /**
     * The colour of the chess piece.
     */
    private final ChessColour colour;

    /**
     * The current position of the chess piece.
     */
    private final Position pos;
    /**
     * The type of the chess piece.
     */
    private final PieceType type;

    /**
     * This constructor MUST be used when the chessboard is not known when a new
     * chess piece is made. The chessboard MUST be set later on with setBoard()
     * It is called with a PiecePosition.
     *
     * @param type The type of the chess piece.
     * @param colour The colour of the chess piece.
     * @param pos The position of the chess piece.
     */
    public ChessPiece(PieceType type, ChessColour colour, Position pos) {
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
    public ChessPiece(PieceType type, ChessColour colour, Position pos,
            ChessBoard board) {
        this.type = type;
        this.colour = colour;
        this.pos = pos;
        this.chessBoard = board;
    }

    /**
     * Make a move on the chess board without updating the GUI.
     *
     * @param move The move.
     */
    public final void agentMove(Move move) {
        move.doMove();
    }

    /**
     * Get all the positions in the given direction from a intial position.
     *
     * @param moves A list with all the moves
     * @param friendlyCoveredPieces A list with all the friendly covered pieces.
     * @param curPos The current position.
     * @param dir The direction.
     * @return MoveDetails object which contains the moves and covered friendly
     * pieces.
     */
    private MoveDetails allPosInDir(ArrayList<Move> moves, ArrayList<Position> friendlyCoveredPieces, Position curPos, Direction dir) {
        if (!curPos.isValid()) {
            // position not valid, return results
            return new MoveDetails(moves, friendlyCoveredPieces);
        } else if (curPos.equals(pos)) {
            // Same as current position of chess piece
            Position newPos = getNextPos(dir, curPos);
            return allPosInDir(moves, friendlyCoveredPieces, newPos, dir);

        } else if (chessBoard.isOccupiedPosition(curPos)) {
            if (chessBoard.getColour(curPos) == this.colour) {
                // Friendly piece, add it to covered list.
                friendlyCoveredPieces.add(curPos);
                return new MoveDetails(moves, friendlyCoveredPieces);
            } else {
                // Enemy piece, add it to moves list.
                CaptureMove captureMove = new CaptureMove(this, curPos);
                moves.add(captureMove);
                return new MoveDetails(moves, friendlyCoveredPieces);
            }
        } else {
            // position not occupied, add it to move list
            NormalMove normalMove = new NormalMove(this, curPos);
            moves.add(normalMove);
            // get next position
            Position newPos = this.getNextPos(dir, curPos);
            return allPosInDir(moves, friendlyCoveredPieces, newPos, dir);
        }
    }

    /**
     *
     * @param otherObject The object this chess piece will be compared to.
     * @return <code>True</code> if the given object equals is qual to this
     * chess piece. <code>False</code> otherwise.
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
     * From the set of possible moves, it only returns those moves that are
     * valid (<code>board.isValidMove</code>).
     *
     * @param moves The possible moves for this chess piece.
     * @return All the legal moves for this chess piece.
     */
    protected final ArrayList<Move> filterMoves(ArrayList<Move> moves) {
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Move move : moves) {
            if (chessBoard.isValidMove(move)) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     *
     * @return The chess board.
     */
    public final ChessBoard getChessBoard() {
        return chessBoard;
    }

    /**
     *
     * @param chessBoard The board containing all of the chess pieces,
     * <b>MUST</b>
     * be called when a new chess piece is created, otherwise the current value
     * of board will be <code>null</code>!
     */
    public final void setChessBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    /**
     *
     * @return The colour of this chess piece.
     */
    public final ChessColour getColour() {
        return colour;
    }

    /**
     *
     * @return The column of this chess piece.
     */
    public final int getColumn() {
        return pos.getColumn();
    }

    /**
     * Positions containing a friendly chess piece that could be captured if
     * this friendly chess piece was an enemy piece.
     *
     * @return
     */
    public abstract ArrayList<Position> getCoveredPositions();

    /**
     * Get the next position given a current position and a direction.
     *
     * @param dir The direction.
     * @param curPos The current position.
     * @return The next position.
     */
    private Position getNextPos(Direction dir, Position curPos) {
        Position newPos;
        switch (dir) {
            case N:
                newPos = new Position(curPos.getRow() - 1, curPos.getColumn());
                break;
            case S:
                newPos = new Position(curPos.getRow() + 1, curPos.getColumn());
                break;
            case W:
                newPos = new Position(curPos.getRow(), curPos.getColumn() - 1);
                break;
            case E:
                newPos = new Position(curPos.getRow(), curPos.getColumn() + 1);
                break;
            case NW:
                newPos = new Position(curPos.getRow() - 1, curPos.getColumn() - 1);
                break;
            case NE:
                newPos = new Position(curPos.getRow() - 1, curPos.getColumn() + 1);
                break;
            case SW:
                newPos = new Position(curPos.getRow() + 1, curPos.getColumn() - 1);
                break;
            case SE:
                newPos = new Position(curPos.getRow() + 1, curPos.getColumn() + 1);
                break;
            default:
                throw new NoSuchElementException();

        }
        return newPos;
    }

    /**
     *
     * @return The position of the piece.
     */
    public final Position getPos() {
        return pos;
    }

    /**
     * The new piecePosition.
     *
     * @param p The new PiecePosition
     */
    public final void setPosition(Position p) {
        pos.setPosition(p.getRow(), p.getColumn());
    }

    /**
     *
     * @param dir The direction for which you want all the possible moves.
     * @return All the possible moves in a specified direction. When the path
     * becomes blocked by either a friendly or a enemy piece, the functions
     * returns the currently found set of moves.
     */
    protected final MoveDetails getPositionsInDirection(Direction dir) {
        return allPosInDir(new ArrayList<>(), new ArrayList<>(), this.pos, dir);
    }

    /**
     * @return The possible moves for this chess piece.
     */
    public abstract ArrayList<Move> getPossibleMoves();

    /**
     * Get the possible moves that are not filtered on their validity.
     *
     * @return The unfiltered possible moves.
     */
    public abstract ArrayList<Move> getRawPossibleMoves();

    /**
     *
     * @return The row of this chess piece.
     */
    public final int getRow() {
        return pos.getRow();
    }

    /**
     *
     * @return The type of this chess piece.
     */
    public PieceType getType() {
        return type;
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

    protected boolean isUniqueMove(Move move, ArrayList<Move> moveList) {
        return moveList.indexOf(move) == moveList.lastIndexOf(move);
    }

    /**
     * Move the chess piece to a new position.
     *
     * @param move The move.
     */
    public final void move(Move move) {
        move.doMove();
    }

    /**
     *
     * @param pos The position.
     * @return <code>True</code> if this position can be captured by this chess
     * piece. <code>False</code> otherwise. Capture means that, if the given
     * position contains a chess piece, this piece could capture this piece. The
     * position indicates the position of the chess piece.
     */
    public boolean posCanBeCaptured(Position pos) {
        ArrayList<Move> moves = getRawPossibleMoves();
        for (Move move : moves) {
            if (move instanceof NormalMove) {
                if (move.getNewPos().equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function determines if a given position is covered by this piece. A
     * piece covers another piece if this piece could capture the other piece
     * where both pieces have the same colour.
     *
     * @param pos The position
     * @return <code>True</code> if the position is covered by this piece,
     * <code>False</code> otherwise
     */
    public boolean posIsCovered(Position pos) {
        return getCoveredPositions().contains(pos);
    }

    /**
     *
     * @param newRow The new row.
     * @param newColumn The new column.
     */
    public final void setPosition(int newRow, int newColumn) {
        pos.setPosition(newRow, newColumn);
    }


    public String toShortString() {
        return (getColour() == ChessColour.White) ? type.toShortString() : type.toShortString().toLowerCase(Locale.ENGLISH);
    }
    /**
     *
     * @return The string representation of this chess piece.
     */
    @Override
    public String toString() {
        return colour + " | " + pos + " | " + type;
    }

}
