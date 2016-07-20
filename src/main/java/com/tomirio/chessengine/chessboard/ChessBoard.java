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

import com.tomirio.chessengine.chesspieces.*;
import com.tomirio.chessengine.moves.Move;
import com.tomirio.chessengine.view.ImageLoader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 *
 * @author Tom Sandmann
 */
public class ChessBoard extends Observable implements Serializable {

    /**
     * The number of columns of the board.
     */
    public static final int COLS = 8;
    /**
     * The number of rows of the board.
     */
    public static final int ROWS = 8;

    /**
     * The board containing all the chess pieces.
     */
    private final ChessPiece[][] board;

    /**
     * Note that during a chess game, the maximum number of pawns that could be
     * captured with an enPassant move is 1. Therefore we use a single variable
     * to indicate the position of the pawn that currently is vulnerable for
     * such a capture.
     */
    private Position vulnerableEnPassantPos;

    /**
     * Constructor.
     */
    public ChessBoard() {
        board = new ChessPiece[ROWS][COLS];
        initializeBoard();
        setChessBoardForPieces();
    }

    /**
     * Determines for a specific player if it can make any move.
     *
     * @param colour The colour of the player.
     * @return <code>True</code> if the player with the specified colour can
     * make a legal move. <code>False</code> otherwise.
     */
    public boolean canMakeAMove(ChessColour colour) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c)) {
                    ChessPiece piece = getPiece(r, c);
                    if (piece.getColour() == colour && !piece.getPossibleMoves().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Creates a deep copy of the instance of this class.
     *
     * @return A deep copy of this instance.
     */
    public ChessBoard deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (ChessBoard) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Delete a chess piece on a specified position
     * @param pos The position of the chess piece that has to be deleted.
     */
    public void deletePieceOnPos(Position pos) {
        board[pos.getRow()][pos.getColumn()] = null;
    }

    /**
     *
     * @param pos The position on the board.
     * @return The colour of the chess piece on that position. The position must
     * contain a chess piece, otherwise this will generate a null pointer
     * exception.
     */
    public ChessColour getColour(Position pos) {
        return (board[pos.getRow()][pos.getColumn()]).getColour();
    }

    /**
     * Get the colour from the chess piece on a specific position on the board.
     * Assumes that this position does contain a chess piece.
     *
     * @param row The row of the chess piece.
     * @param column The column of the chess piece.
     * @return The colour of the chess piece on <code>board[row][column]</code>.
     */
    public ChessColour getColour(int row, int column) {
        return (board[row][column]).getColour();
    }

    /**
     * Create a hash for the current representation of the chess board.
     *
     * @return An integer representing the hash for this specific chess board.
     */
    public int getHash() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Get the king with a specific colour.
     *
     * @param colour The colour of the king.
     * @return The king that has the specified colour.
     */
    public King getKing(ChessColour colour) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c) && getPiece(r, c).getType() == PieceType.King) {
                    King k = (King) getPiece(r, c);
                    if (k.getColour() == colour) {
                        return k;
                    }
                }
            }
        }
        return null;
    }

    /**
     *
     * @param row The row of the chess piece.
     * @param column The column of the chess piece.
     * @return The chess piece at <code>(row,col)</code>.
     */
    public ChessPiece getPiece(int row, int column) {
        return board[row][column];
    }

    /**
     *
     * @param p The position on the board.
     * @return The chess piece that is on the specified position.
     */
    public ChessPiece getPiece(Position p) {
        ChessPiece piece = board[p.getRow()][p.getColumn()];
        assert piece != null;
        return piece;
    }

    /**
     *
     * @param piece The chess piece that will be put on the board.
     */
    public void setPiece(ChessPiece piece) {
        board[piece.getRow()][piece.getColumn()] = piece;
    }

    /**
     * Get all the pieces having a specific colour.
     *
     * @param colour The colour of the pieces to retrieve.
     * @return An ArrayList with all the chess pieces of the given color.
     */
    public ArrayList<ChessPiece> getPieces(ChessColour colour) {
        ArrayList<ChessPiece> pieces = new ArrayList<>();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (isOccupiedPosition(row, col)) {
                    ChessPiece p = getPiece(row, col);
                    if (p.getColour() == colour) {
                        pieces.add(p);
                    }
                }
            }
        }
        return pieces;
    }

    /**
     *
     * @param colour The colour of the queen to get.
     * @return List containing the current Queen on the board.
     */
    public ArrayList<Queen> getQueens(ChessColour colour) {
        ArrayList<Queen> q = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c) && getPiece(r, c).getType() == PieceType.Queen) {
                    Queen queen = (Queen) getPiece(r, c);
                    if (queen.getColour() == colour) {
                        q.add(queen);
                    }
                }
            }
        }
        return q;
    }

    /**
     * Get all the rooks of a specific colour.
     *
     * @param colour The colour of rooks to get.
     * @return All rooks on the current board.
     */
    public ArrayList<Rook> getRooks(ChessColour colour) {
        ArrayList<Rook> rooks = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c) && getPiece(r, c).getType() == PieceType.Rook) {
                    Rook rook = (Rook) getPiece(r, c);
                    if (rook.getColour() == colour) {
                        rooks.add(rook);
                    }
                }
            }
        }
        return rooks;
    }

    /**
     * Get the position of the pawn that currently is vulnerable for an enPassant
     * capture.
     * @return  The position of the pawn vulnerable for an enPassant capture.
     */
    public Position getVulnerableEnPassantPos() {
        return vulnerableEnPassantPos;
    }

    /**
     * Update the position of the pawn that could be captured with an enPassant
     * move.
     *
     * @param newPos The position of the pawn.
     */
    public void setVulnerableEnPassantPos(Position newPos) {
        vulnerableEnPassantPos = newPos;
    }

    /**
     * Initializes the chess board with all its pieces.
     */
    private void initializeBoard() {
        // Image loader
        ImageLoader.initialize();
        // Black pawns
        for (int col = 0; col < COLS; col++) {
            board[1][col] = new Pawn(ChessColour.Black, new Position(1, col));
        }

        // Black rooks
        board[0][0] = new Rook(ChessColour.Black, new Position(0, 0));
        board[0][7] = new Rook(ChessColour.Black, new Position(0, 7));

        // Black knights
        board[0][1] = new Knight(ChessColour.Black, new Position(0, 1));
        board[0][6] = new Knight(ChessColour.Black, new Position(0, 6));

        // Black bishops
        board[0][2] = new Bishop(ChessColour.Black, new Position(0, 2));
        board[0][5] = new Bishop(ChessColour.Black, new Position(0, 5));

        // Black King and Queen
        board[0][3] = new Queen(ChessColour.Black, new Position(0, 3));
        board[0][4] = new King(ChessColour.Black, new Position(0, 4));

        // White pawns
        for (int col = 0; col < COLS; col++) {
            board[6][col] = new Pawn(ChessColour.White, new Position(6, col));
        }

        // White rooks
        board[7][0] = new Rook(ChessColour.White, new Position(7, 0));
        board[7][7] = new Rook(ChessColour.White, new Position(7, 7));

        // White knights
        board[7][1] = new Knight(ChessColour.White, new Position(7, 1));
        board[7][6] = new Knight(ChessColour.White, new Position(7, 6));

        // White bishops
        board[7][2] = new Bishop(ChessColour.White, new Position(7, 2));
        board[7][5] = new Bishop(ChessColour.White, new Position(7, 5));

        // White King and Queen
        board[7][3] = new Queen(ChessColour.White, new Position(7, 3));
        board[7][4] = new King(ChessColour.White, new Position(7, 4));
    }

    /**
     * For two specified positions in the same row, determine if all the
     * intermediate positions are free positions (do not contain a chess piece).
     *
     * @param begin The initial position.
     * @param end The final position.
     * @return <code> True </code> if there is no piece in between the rows of
     * the initial and the final position. <code>False</code> otherwise.
     */
    public boolean isEmptySubRow(Position begin, Position end) {
        if (begin.getRow() != end.getRow()) {
            // The rows are not the same, so we throw an exception
            throw new IllegalArgumentException();
        } else if (begin.getColumn() >= end.getColumn()) {
            for (int c = end.getColumn() + 1; c < begin.getColumn(); c++) {
                if (this.isOccupiedPosition(begin.getRow(), c)) {
                    return false;
                }
            }
            return true;
        } else {
            for (int c = begin.getColumn() + 1; c < end.getColumn(); c++) {
                if (this.isOccupiedPosition(begin.getRow(), c)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     *
     * @param pos The position on the board.
     * @return <code>True</code> if the specified position contains a chess
     * piece <code>False</code> otherwise.
     */
    public boolean isOccupiedPosition(Position pos) {
        return (board[pos.getRow()][pos.getColumn()] != null);
    }

    /**
     *
     * @param row The row on the board
     * @param column The column on the board
     * @return <code>True</code> if <code>board[row][column]</code> contains a
     * chess piece. <code>False</code> otherwise
     */
    public boolean isOccupiedPosition(int row, int column) {
        return (board[row][column] != null);
    }

    /**
     * Check if a given coordinate is within the chess board.
     *
     * @param row The row.
     * @param col The column.
     * @return <code>True</code> if the coordinate is within the chess board,
     * <code>False</code> otherwise.
     */
    public boolean isValidCoordinate(int row, int col) {
        return col >= 0 && col < COLS && row >= 0 && row < ROWS;
    }

    /**
     * Determines for a given chess piece if a new position
     * <code>(row, col)</code> is a valid move for this chess piece. We assume
     * this new position is in the set of possible moves for the given chess
     * piece. Note that we <b>MUST</b> make a copy of our previous board before
     * making this test move. A move can also make other pieces captured, which
     * are not restored when just calling <code>silentRestorePiece()</code>.
     *
     * @return <code>True</code> if this position is a valid move for the given
     * chess piece. <code>False</code> otherwise.
     */
    public boolean isValidMove(Move move) {
        System.out.println("Voor mode zag het bord er zo uit:\n" + this);
        move.doMove();
        System.out.println("Na de doMove()\n" + this);

        King k = getKing(move.getInvolvedPiece().getColour());
        boolean valid = k.isSafePosition(k.getPos());

        move.undoMove();
        System.out.println("Na de undoMove\n" + this);
        return valid;
    }

    /**
     * Makes sure all the chess pieces have a instance of this class.
     */
    private void setChessBoardForPieces() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (isOccupiedPosition(row, col)) {
                    ChessPiece p = getPiece(row, col);
                    p.setChessBoard(this);
                }
            }
        }
    }

    /**
     * Moves the chess piece to the specified row and column. This done
     * <b>without</b> updating the current game state.
     *
     * @param piece The chess piece.
     * @param newPos The new position.
     *
     */
    public void silentMovePiece(ChessPiece piece, Position newPos) {
        board[piece.getRow()][piece.getColumn()] = null;
        piece.setPosition(newPos);
        board[newPos.getRow()][newPos.getColumn()] = piece;
    }

    /**
     *
     * @return String representation of this ChessBoard.
     */
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!isOccupiedPosition(r, c)) {
                    bld.append(" - ");
                } else {
                    ChessPiece piece = getPiece(r, c);
                    if (piece.getColour() == ChessColour.Black) {
                        // Black pieces will be printed in lower case
                        bld.append(" ").append(piece.getType().toShortString().toLowerCase()).append(" ");
                    } else {
                        // White pieces will be printed as normal (in higher case)
                        bld.append(" ").append(piece.getType().toShortString()).append(" ");
                    }
                }
            }
            bld.append("\n");
        }
        return bld.toString();
    }

    /**
     * Updates the check status of both kings.
     */
    public void updateKingStatus() {
        boolean whiteKingChanged = false;
        boolean blackKingChanged = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c)) {
                    ChessPiece p = getPiece(r, c);
                    ArrayList<Move> possibleMoves = p.getPossibleMoves();
                    for (Move move : possibleMoves) {
                        if (move.getNewPos().equals(getKing(ChessColour.White).getPos())) {
                            getKing(ChessColour.White).setCheck(true);
                            whiteKingChanged = true;
                        } else if (move.getNewPos().equals(getKing(ChessColour.Black).getPos())) {
                            getKing(ChessColour.Black).setCheck(true);
                            blackKingChanged = true;
                        }
                    }
                }
            }
        }
        if (!whiteKingChanged) {
            getKing(ChessColour.White).setCheck(false);
        }
        if (!blackKingChanged) {
            getKing(ChessColour.Black).setCheck(false);
        }
    }
}
