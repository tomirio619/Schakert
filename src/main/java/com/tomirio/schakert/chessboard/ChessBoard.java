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

import com.tomirio.schakert.chesspieces.King;
import com.tomirio.schakert.chesspieces.Queen;
import com.tomirio.schakert.chesspieces.Rook;
import com.tomirio.schakert.game.FENParser;
import com.tomirio.schakert.moves.Move;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 *
 * @author Tom Sandmann
 */
public class ChessBoard {

    /**
     * The number of columns of the board.
     */
    public static final int COLS = 8;
    /**
     * The number of rows of the board.
     */
    public static final int ROWS = 8;
    public static final String START_POSITION
            = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    /**
     * The black king.
     */
    private King blackKing;
    /**
     * The board containing all the chess pieces.
     */
    private final ChessPiece[][] board;
    /**
     * The enPassant target square.
     */
    private Position enPassantTargetSquare;
    /**
     * FEN parser.
     */
    private FENParser fenParser;
    /**
     * Colour of the player having turn.
     */
    private Colour hasTurn;
    /**
     * The white king.
     */
    private King whiteKing;

    /**
     * Constructor.
     */
    public ChessBoard() {
        board = new ChessPiece[ROWS][COLS];
        fenParser = new FENParser(START_POSITION, this);
        fenParser.parse();
        hasTurn = fenParser.getHasTurn();
    }

    /**
     * Determines for a specific player if it can make any move.
     *
     * @param colour The colour of the player.
     * @return <code>True</code> if the player with the specified colour can
     * make a legal move. <code>False</code> otherwise.
     */
    public boolean canMakeAMove(Colour colour) {
        for (ChessPiece p : getPieces(colour)) {
            if (!p.getPossibleMoves().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the chess board.
     */
    public void clearBoard() {
        for (int index = 0; index < board.length; index++) {
            for (int inner = 0; inner < board[index].length; inner++) {
                board[index][inner] = null;
            }
        }
    }

    /**
     * Delete a chess piece on a specified position
     *
     * @param pos The position of the chess piece that has to be deleted.
     */
    public void deletePieceOnPos(Position pos) {
        board[pos.getRow()][pos.getColumn()] = null;
    }

    /**
     *
     * @return <code>True</code> if the game is finished, <code>False</code>
     * otherwise.
     */
    public boolean gameIsFinished() {
        return inCheckmate(Colour.Black) || inCheckmate(Colour.White)
                || inStalemate();
    }

    /**
     * The black king.
     *
     * @param k
     */
    public void setBlackKing(King k) {
        this.blackKing = k;
    }

    private String getCastlingAvailability() {
        ArrayList<Rook> blackRooks = this.getRooks(Colour.Black);
        ArrayList<Rook> whiteRooks = this.getRooks(Colour.White);
        ArrayList<Rook> rooks = new ArrayList();
        rooks.addAll(whiteRooks);
        rooks.addAll(blackRooks);
        StringBuilder castlingAvailability = new StringBuilder();
        for (Rook rook : rooks) {
            if (rook.getCastlingPossible()) {
                // Rook was able to perform castling
                King k = this.getKing(rook.getColour());
                if (k.getCastlingPossible()) {
                    // King was able to perform castling
                    if (rook.getColumn() > k.getColumn()) {
                        // King side rook
                        castlingAvailability.append(k.toShortString());
                    } else {
                        // Queen side rook
                        String queenSide = (k.getColour() == Colour.White) ? "Q" : "q";
                        castlingAvailability.append(queenSide);
                    }
                }
            }
        }
        if (castlingAvailability.length() == 0) {
            return "-";
        }
        return castlingAvailability.toString();
    }

    /**
     *
     * @param pos The position on the board.
     * @return The colour of the chess piece on that position. The position must
     * contain a chess piece, otherwise this will generate a null pointer
     * exception.
     */
    public Colour getColour(Position pos) {
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
    public Colour getColour(int row, int column) {
        return (board[row][column]).getColour();
    }

    private String getEnPassantFile() {
        if (this.enPassantTargetSquare == null) {
            return "-";
        } else {
            return enPassantTargetSquare.toString();
        }
    }

    /**
     * Get the position of the pawn that currently is vulnerable for an
     * enPassant capture.
     *
     * @return The position of the pawn vulnerable for an enPassant capture.
     */
    public Position getEnPassantTargetSquare() {
        return enPassantTargetSquare;
    }

    /**
     * Update the position of the pawn that could be captured with an enPassant
     * move.
     *
     * @param newPos The position of the pawn.
     */
    public void setEnPassantTargetSquare(Position newPos) {
        enPassantTargetSquare = newPos;
    }

    public String getFEN() {
        StringBuilder FEN = new StringBuilder();
        for (int row = 0; row < ChessBoard.ROWS; row++) {
            for (int col = 0; col < ChessBoard.COLS; col++) {
                if (isOccupiedPosition(row, col)) {
                    // Add SAN string of chess piece on this position.
                    ChessPiece p = this.getPiece(row, col);
                    FEN.append(p.toShortString());
                } else {
                    // Add number of free squares.
                    int nrOfEmptySquares = nrOfEmptySquares(row, col, 0);
                    FEN.append(nrOfEmptySquares);
                    // Col will be incremented after this iteration, adjust with - 1
                    col += nrOfEmptySquares - 1;
                }
            }
            if (row != ChessBoard.ROWS - 1) {
                FEN.append("/");
            }
        }

        FEN.append(" ").append(hasTurn.toShortString());
        FEN.append(" ").append(getCastlingAvailability());
        FEN.append(" ").append(getEnPassantFile());

        return FEN.toString();
    }

    /**
     * Get the FEN parser.
     *
     * @return The FEN parser.
     */
    public FENParser getFENParser() {
        return fenParser;
    }

    /**
     *
     * @return The colour of the player having turn.
     */
    public Colour getHasTurn() {
        return hasTurn;
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
     * Get the king based on a given colour
     *
     * @param colour The colour.
     * @return The king that has the specified colour.
     */
    public King getKing(Colour colour) {
        return (colour == Colour.White) ? whiteKing : blackKing;
    }

    /**
     * <b>Assuming</b> the rook is on its initial position, return the kingside
     * rook.
     *
     * @param colour The colour of the rook.
     * @return The kingside rook with the given colour.
     */
    public Rook getKingSideRook(Colour colour) {
        switch (colour) {
            case Black:
                return (Rook) board[0][7];
            case White:
                return (Rook) board[7][7];
            default:
                throw new NoSuchElementException();
        }
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
        return piece;
    }

    /**
     *
     * @param piece The chess piece that will be put on the board. A reference
     * to the chess board is also passed to the chess piece.
     */
    public void setPiece(ChessPiece piece) {
        piece.setChessBoard(this);
        board[piece.getRow()][piece.getColumn()] = piece;
    }

    /**
     * Get all the pieces having a specific colour.
     *
     * @param colour The colour of the pieces to retrieve.
     * @return An ArrayList with all the chess pieces of the given color.
     */
    public ArrayList<ChessPiece> getPieces(Colour colour) {
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
     * <b>Assuming</b> the rook is on its initial position, return the queenside
     * rook.
     *
     * @param colour The colour of the rook.
     * @return The queenside rook with the given colour.
     */
    public Rook getQueenSideRook(Colour colour) {
        switch (colour) {
            case Black:
                return (Rook) board[0][0];
            case White:
                return (Rook) board[7][0];
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     *
     * @param colour The colour of the queen to get.
     * @return List containing the current Queen on the board.
     */
    public ArrayList<Queen> getQueens(Colour colour) {
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
    public ArrayList<Rook> getRooks(Colour colour) {
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
     * Set the white king.
     *
     * @param k The white king.
     */
    public void setWhiteKing(King k) {
        this.whiteKing = k;
    }

    /**
     *
     * @param playerColour The colour of the player
     * @return  <code>True</code> if the colour of the player is check mate,
     * <code>False</code> otherwise.
     */
    public boolean inCheckmate(Colour playerColour) {
        switch (playerColour) {
            case Black:
                return blackKing.inCheck()
                        && blackKing.getPossibleMoves().isEmpty()
                        && !canMakeAMove(Colour.Black);

            case White:
                return whiteKing.inCheck()
                        && whiteKing.getPossibleMoves().isEmpty()
                        && !canMakeAMove(Colour.White);
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     * @return <code>True</code> if the current state of the chess board is
     * stalemate. <code>False</code> otherwise.
     */
    public boolean inStalemate() {
        switch (hasTurn) {
            case Black:
                return blackKing.getPossibleMoves().isEmpty()
                        && !blackKing.inCheck()
                        && !canMakeAMove(Colour.Black);
            default:
                //White:
                return whiteKing.getPossibleMoves().isEmpty()
                        && !whiteKing.inCheck()
                        && !canMakeAMove(Colour.White);
        }
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
     * Checks whether a move puts the own king in check, which would be illegal.
     *
     * @param move The move.
     * @return <code>True</code> if the move puts the own king in check,
     * <code>False</code> otherwise.
     */
    public boolean doesNotPutOwnKingInCheck(Move move) {
        // Apply the move.
        move.doMove();
        // Check whether the move puts his king in check.
        Colour pieceColour = move.getInvolvedPiece().getColour();
        King k = getKing(pieceColour);
        boolean inCheck = k.inCheck();
        // Undo the move.
        move.undoMove();
        return !inCheck;
    }

    /**
     * Load a FEN string.
     *
     * @param FEN The FEN string.
     */
    public void loadFEN(String FEN) {
        fenParser = new FENParser(FEN, this);
        fenParser.parse();
        hasTurn = fenParser.getHasTurn();
    }

    /**
     * Get the number of succesive empty squares in a given row from a specified
     * column.
     *
     * @param row The row.
     * @param col The column.
     * @param nrOfEmptySquares The number of empty squares.
     * @return The number of succesive empty squares.
     */
    private int nrOfEmptySquares(int row, int col, int nrOfEmptySquares) {
        if (col == ChessBoard.COLS || isOccupiedPosition(row, col)) {
            return nrOfEmptySquares;
        }
        return nrOfEmptySquares(row, col + 1, nrOfEmptySquares + 1);
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
        bld.append("\n").append("  +------------------------+").append("\n");
        for (int r = 0; r < ROWS; r++) {
            bld.append(8 - r).append(" |");
            for (int c = 0; c < COLS; c++) {
                if (!isOccupiedPosition(r, c)) {
                    bld.append(" . ");
                } else {
                    ChessPiece piece = getPiece(r, c);
                    bld.append(" ").append(piece.toShortString()).append(" ");
                }
            }
            bld.append("|\n");
        }
        bld.append("  +------------------------+").append("\n");
        bld.append("    a  b  c  d  e  f  g  h").append("\n");
        bld.append("Turn: ").append(hasTurn).append("\n");
        bld.append("Castling: ").append(getCastlingAvailability()).append("\n");
        bld.append("E.p. file: ").append(getEnPassantFile()).append("\n");
        // bld.append("FEN: ").append(this.getFEN()).append("\n");
        return bld.toString();
    }

    /**
     * Updates the check status of both kings.
     */
    public void updateKingStatus() {
        ArrayList<ChessPiece> allPieces = new ArrayList();
        allPieces.addAll(getPieces(Colour.Black));
        allPieces.addAll(getPieces(Colour.White));
        boolean whiteKingChanged = false;
        boolean blackKingChanged = false;
        for (ChessPiece piece : allPieces) {
            switch (piece.getColour()) {
                case Black:
                    /*
                    Check if this black piece can capture the position
                    of the white king
                     */
                    if (piece.posCanBeCaptured(whiteKing.getPos())) {
                        whiteKing.setCheck(true);
                        whiteKingChanged = true;
                    }
                    break;
                case White:
                    /*
                    Check if this white piece can capture the position
                    of the black king
                     */
                    if (piece.posCanBeCaptured(blackKing.getPos())) {
                        blackKing.setCheck(true);
                        blackKingChanged = true;
                    }
                default:
                    break;
            }
            if (!whiteKingChanged) {
                whiteKing.setCheck(false);
            }
            if (!blackKingChanged) {
                blackKing.setCheck(false);
            }
        }
    }

    /**
     * Update the turn
     */
    public void updateTurn() {
        this.hasTurn = (hasTurn == Colour.White) ? Colour.Black : Colour.White;
    }

}
