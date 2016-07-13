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
import com.tomirio.chessengine.view.ImageLoader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Observable;
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom Sandmann
 */
public class ChessBoard extends Observable implements Serializable {

    /**
     * The number of rows of the board.
     */
    public static final int ROWS = 8;

    /**
     * The number of columns of the board.
     */
    public static final int COLS = 8;

    /**
     * The board containing all the chess pieces.
     */
    private final ChessPiece[][] board;

    /**
     * The log.
     */
    private transient final Log log;

    /**
     * The state.
     */
    private final State state;

    /**
     * The image loader.
     */
    public transient ImageLoader imageLoader;

    /**
     * Constructor.
     *
     * @param log The log.
     * @param state The state.
     */
    public ChessBoard(Log log, State state) {
        this.state = state;
        this.log = log;
        board = new ChessPiece[ROWS][COLS];
        imageLoader = new ImageLoader();
        initializeBoard();
        setChessBoardForPieces();
    }

    /**
     * Initializes the chess board with all its pieces.
     */
    private void initializeBoard() {
        // Black pawns
        for (int col = 0; col < COLS; col++) {
            board[1][col] = new Pawn(ChessTypes.Pawn, ChessColour.Black,
                    new PiecePosition(1, col), imageLoader.blackPawn);
        }

        // Black castles
        board[0][0] = new Castle(ChessTypes.Castle, ChessColour.Black,
                new PiecePosition(0, 0), imageLoader.blackCastle);
        board[0][7] = new Castle(ChessTypes.Castle, ChessColour.Black,
                new PiecePosition(0, 7), imageLoader.blackCastle);

        // Black knights
        board[0][1] = new Knight(ChessTypes.Knight, ChessColour.Black,
                new PiecePosition(0, 1), imageLoader.blackKnight);
        board[0][6] = new Knight(ChessTypes.Knight, ChessColour.Black,
                new PiecePosition(0, 6), imageLoader.blackKnight);

        // Black Rooks
        board[0][2] = new Bishop(ChessTypes.Bishop, ChessColour.Black,
                new PiecePosition(0, 2), imageLoader.blackBishop);
        board[0][5] = new Bishop(ChessTypes.Bishop, ChessColour.Black,
                new PiecePosition(0, 5), imageLoader.blackBishop);

        // Black King and Queen
        board[0][3] = new Queen(ChessTypes.Queen, ChessColour.Black,
                new PiecePosition(0, 3), imageLoader.blackQueen);
        board[0][4] = new King(ChessTypes.King, ChessColour.Black,
                new PiecePosition(0, 4), imageLoader.blackKing);

        // White pawns
        for (int col = 0; col < COLS; col++) {
            board[6][col] = new Pawn(ChessTypes.Pawn, ChessColour.White,
                    new PiecePosition(6, col), imageLoader.whitePawn);
        }

        // White castles
        board[7][0] = new Castle(ChessTypes.Castle, ChessColour.White,
                new PiecePosition(7, 0), imageLoader.whiteCastle);
        board[7][7] = new Castle(ChessTypes.Castle, ChessColour.White,
                new PiecePosition(7, 7), imageLoader.whiteCastle);

        // White knights
        board[7][1] = new Knight(ChessTypes.Knight, ChessColour.White,
                new PiecePosition(7, 1), imageLoader.whiteKnight);
        board[7][6] = new Knight(ChessTypes.Knight, ChessColour.White,
                new PiecePosition(7, 6), imageLoader.whiteKnight);

        // White Rooks
        board[7][2] = new Bishop(ChessTypes.Bishop, ChessColour.White,
                new PiecePosition(7, 2), imageLoader.whiteBishop);
        board[7][5] = new Bishop(ChessTypes.Bishop, ChessColour.White,
                new PiecePosition(7, 5), imageLoader.whiteBishop);

        // White King and Queen
        board[7][3] = new Queen(ChessTypes.Queen, ChessColour.White,
                new PiecePosition(7, 3), imageLoader.whiteQueen);
        board[7][4] = new King(ChessTypes.King, ChessColour.White,
                new PiecePosition(7, 4), imageLoader.whiteKing);
    }

    /**
     * Makes sure all the chess pieces have a instance of this class.
     */
    private void setChessBoardForPieces() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (isOccupiedPosition(row, col)) {
                    ChessPiece p = getPiece(row, col);
                    p.setBoard(this);
                }
            }
        }
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
    public ChessPiece getPiece(PiecePosition p) {
        return board[p.getRow()][p.getColumn()];
    }

    /**
     * Move piece on the chess board and update view.
     *
     * @param piece The chess piece that needs to be moved.
     * @param newRow The new row of the chess piece.
     * @param newColumn The new column of the chess piece.
     */
    public void movePiece(ChessPiece piece, int newRow, int newColumn) {
        log.write(piece + "\t -> \t" + new PiecePosition(newRow, newColumn));
        checkEnPassantMove(piece, newRow, newColumn);
        checkCastling(piece, newRow, newColumn);
        silentMovePiece(piece, newRow, newColumn);
        updateEnPassantMoves(piece);
        checkPawnPromotion(piece.getColour());
        updateKingStatus();
        state.update(this);
        setChanged();
        notifyObservers(this);
    }

    /**
     * Move piece on the chess board and <b>don't</b> update view.
     *
     * @param pos The piece position of the chess piece to move.
     * @param newRow The new row of the chess piece.
     * @param newColumn The new column of the chess piece.
     */
    public void movePiece(PiecePosition pos, int newRow, int newColumn) {
        if (!isOccupiedPosition(pos)) {
            System.out.println("The position " + pos + " does not contain a chess piece!");
            return;
        }
        ChessPiece piece = getPiece(pos);
        log.write(piece + "\t -> \t" + new PiecePosition(newRow, newColumn));
        checkEnPassantMove(piece, newRow, newColumn);
        checkCastling(piece, newRow, newColumn);
        silentMovePiece(piece, newRow, newColumn);
        updateEnPassantMoves(piece);
        checkPawnPromotion(piece.getColour());
        updateKingStatus();
        state.update(this);
        setChanged();
        notifyObservers(this);
    }

    /**
     * Move piece on the chess board and <code>don't</code> update view.
     *
     * @param piece The chess piece that needs to be moved.
     * @param newRow The new row of the chess piece.
     * @param newColumn The new column of the chess piece.
     */
    public void movePieceAgent(ChessPiece piece, int newRow, int newColumn) {
        checkEnPassantMove(piece, newRow, newColumn);
        checkCastling(piece, newRow, newColumn);
        silentMovePiece(piece, newRow, newColumn);
        updateEnPassantMoves(piece);
        checkPawnPromotion(piece.getColour());
        updateKingStatus();
        state.update(this);
    }

    /**
     * This function checks if an enPassant move took place. This means that a
     * pawn moved diagonal without capturing another piece. It makes sure the
     * right piece is captured. The pawn that executed the enPassant move will
     * not be moved!
     *
     * @param piece the chess piece
     * @param newRow the new row of the chess piece
     * @param newColumn the new column of the chess piece
     */
    private void checkEnPassantMove(ChessPiece piece, int newRow, int newColumn) {
        int distRow = abs(newRow - piece.getRow());
        int distCol = abs(newColumn - piece.getColumn());
        if (!isOccupiedPosition(newRow, newColumn) && distRow == 1
                && distCol == 1 && piece.getType() == ChessTypes.Pawn) {
            // Determine by colour which piece will be captured
            switch (piece.getColour()) {
                case White: {
                    /*
                     A black piece is captured, the new position of the black
                     piece is one ROW below the position of the captured piece.
                     */
                    board[newRow + 1][newColumn] = null;
                    break;
                }
                case Black: {
                    /*
                     A white piece is captured, the new position of the black
                     piece is one ROW above the position of the captured piece.
                     */
                    board[newRow - 1][newColumn] = null;
                    break;
                }
            }
        }
    }

    /**
     * Castling is done when a king suddenly moves two squares. We detect if
     * this is the case and set the right position for the castle involved.
     *
     * @param piece The piece.
     * @param newRow The new row.
     * @param newColumn The new column.
     */
    private void checkCastling(ChessPiece piece, int newRow, int newColumn) {
        int distMoved = abs(piece.getColumn() - newColumn);
        if (piece instanceof King && distMoved == 2) {
            // A castling move took place
            switch (piece.getColour()) {
                case Black:
                    if (newColumn - piece.getColumn() > 0) {
                        // King is moving to the right, so top right castle will be moved
                        ChessPiece castle = getPiece(0, 7);
                        silentMovePiece(castle, 0, 5);
                        break;
                    } else {
                        // King is moving to the left, so top left castle is involved
                        ChessPiece castle = getPiece(0, 0);
                        silentMovePiece(castle, 0, 3);
                        break;
                    }
                case White:
                    if (newColumn - piece.getColumn() > 0) {
                        // King is moving to the right, so bottom right castle will be moved
                        ChessPiece castle = getPiece(7, 7);
                        silentMovePiece(castle, 7, 5);
                        break;
                    } else {
                        // King is moving to the left, so bottom left castle is involved
                        ChessPiece castle = getPiece(7, 0);
                        silentMovePiece(castle, 7, 3);
                        break;
                    }
            }
        }

    }

    /**
     * If a pawn reaches the other side of the board, if will become a queen.
     * This is called promotion.
     */
    private void checkPawnPromotion(ChessColour colour) {
        switch (colour) {
            case Black: {
                int row = 7;
                for (int col = 0; col < COLS; col++) {
                    if (isOccupiedPosition(row, col)) {
                        ChessPiece p = getPiece(row, col);
                        if (p instanceof Pawn && p.getColour() == colour) {
                            // Promote the black pawn into a black queen
                            board[row][col] = new Queen(ChessTypes.Queen,
                                    ChessColour.Black, new PiecePosition(row, col),
                                    imageLoader.blackQueen,
                                    this);
                        }
                    }
                }
                break;
            }

            case White: {
                int row = 0;
                for (int col = 0; col < COLS; col++) {
                    if (isOccupiedPosition(row, col)) {
                        ChessPiece p = getPiece(row, col);
                        if (p instanceof Pawn && p.getColour() == colour) {
                            // Promote the black pawn into a black queen
                            board[row][col] = new Queen(ChessTypes.Queen,
                                    ChessColour.White, new PiecePosition(row, col),
                                    imageLoader.whiteQueen,
                                    this);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     *
     * @param piece The chess piece that will be put on the board.
     */
    public void setPiece(ChessPiece piece) {
        board[piece.getRow()][piece.getColumn()] = piece;
    }

    /**
     *
     * @param pos The position on the board.
     * @return <code>True</code> if the specified position contains a chess
     * piece <code>False</code> otherwise.
     */
    public boolean isOccupiedPosition(PiecePosition pos) {
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
     *
     * @param pos The position on the board.
     * @return The colour of the chess piece on that position. The position must
     * contain a chess piece, otherwise this will generate a null pointer
     * exception.
     */
    public ChessColour getColour(PiecePosition pos) {
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
     * @param piece The chess piece that just moved called this function with
     * itself as argument. This piece will be excluded from the the call to
     * <code>setEnPassantMove(false)</code> to all the other pieces.
     */
    private void updateEnPassantMoves(ChessPiece piece) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (isOccupiedPosition(r, c) && getPiece(r, c) instanceof Pawn) {
                    Pawn p = (Pawn) getPiece(r, c);
                    if (!getPiece(r, c).equals(piece)) {
                        p.setEnPassantMove(false);
                    }
                }
            }
        }
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
                if (isOccupiedPosition(r, c) && getPiece(r, c) instanceof King) {
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
     * Get all the castles of a specific colour.
     *
     * @param colour The colour of castles.
     * @return All whiteKing castles on the current board.
     */
    public ArrayList<Castle> getCastles(ChessColour colour) {
        ArrayList<Castle> castles = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c) && getPiece(r, c) instanceof Castle) {
                    Castle castle = (Castle) getPiece(r, c);
                    if (castle.getColour() == colour) {
                        castles.add(castle);
                    }
                }
            }
        }
        return castles;
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
                if (isOccupiedPosition(r, c) && getPiece(r, c) instanceof Queen) {
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
     * Updates the check status of both kings.
     */
    private void updateKingStatus() {
        boolean whiteKingChanged = false;
        boolean blackKingChanged = false;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (isOccupiedPosition(r, c)) {
                    ChessPiece p = getPiece(r, c);
                    if (p.getPossibleMoves().contains(getKing(ChessColour.White).getPos())) {
                        getKing(ChessColour.White).setCheck(true);
                        whiteKingChanged = true;
                    } else if (p.getPossibleMoves().contains(getKing(ChessColour.Black).getPos())) {
                        getKing(ChessColour.Black).setCheck(true);
                        blackKingChanged = true;
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

    /**
     * For two specified positions in the same row, determine if all the
     * intermediate positions are free positions (do not contain a chess piece).
     *
     * @param begin The initial position.
     * @param end The final position.
     * @return <code> True </code> if there is no piece in between the rows of
     * the initial and the final position. <code>False</code> otherwise.
     */
    public boolean isEmptySubRow(PiecePosition begin, PiecePosition end) {
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
     * Determines for a specific player if it can make any move.
     *
     * @param colour The colour of the player.
     * @return <code>True</code> if the player with the specified colour can
     * make a legal move. <code>False</code> otherwise.
     */
    public boolean canMakeAMove(ChessColour colour) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
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
     * Moves the chess piece to the specified row and column. This done
     * <b>without</b> updating the current game state.
     *
     * @param piece The chess piece.
     * @param newRow The new row of the chess piece.
     * @param newColumn The new column of the chess piece.
     */
    public void silentMovePiece(ChessPiece piece, int newRow, int newColumn) {
        board[piece.getRow()][piece.getColumn()] = null;
        piece.setPosition(newRow, newColumn);
        board[newRow][newColumn] = piece;
    }

    /**
     * Restores the chess piece to the specified row and column
     *
     * @param piece The chess piece.
     * @param orgRow The original row.
     * @param orgColumn The original column.
     */
    public void silentRestorePiece(ChessPiece piece, int orgRow, int orgColumn) {
        board[piece.getRow()][piece.getColumn()] = null;
        piece.setPosition(orgRow, orgColumn);
        board[orgRow][orgColumn] = piece;
    }

    /**
     * Silently updates the current state of the chessboard. This method will be
     * used by an agent calculating the best move possible. Make sure this
     * method is <b>ONLY</b> called on a deep copy of the board!
     */
    public void silentUpdateState() {
        state.update(this);
    }

    /**
     * Determines for a given chess piece if a new position
     * <code>(row, col)</code> is a valid move for this chess piece. We assume
     * this new position is in the set of possible moves for the given chess
     * piece. Note that we <b>MUST</b> make a copy of our previous board before
     * making this test move. A move can also make other pieces captured, which
     * are not restored when just calling <code>silentRestorePiece()</code>.
     *
     * @param piece The chess piece.
     * @param newRow The new row.
     * @param newColumn The new column.
     * @return <code>True</code> if this position is a valid move for the given
     * chess piece. <code>False</code> otherwise.
     */
    public boolean isValidMove(ChessPiece piece, int newRow, int newColumn) {
        // Create new object of the original position
        PiecePosition orgPos = new PiecePosition(piece.getRow(), piece.getColumn());
        ChessPiece possiblyCapturedPiece = null;
        if (isOccupiedPosition(newRow, newColumn)) {
            possiblyCapturedPiece = getPiece(newRow, newColumn);
        }
        silentMovePiece(piece, newRow, newColumn);
        King k = getKing(piece.getColour());
        boolean valid = k.isSafePosition(k.getPos());
        silentRestorePiece(piece, orgPos.getRow(), orgPos.getColumn());
        if (possiblyCapturedPiece != null) {
            board[newRow][newColumn] = possiblyCapturedPiece;
        }
        return valid;
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
        return col >= 0 && col <= 7 && row >= 0 && row <= 7;
    }

    /**
     * Evaluates the chessboard based on the given colour.
     *
     * @param colour The colour of the pieces used in the evaluation
     * @return An approximation of the relative score of the position of the
     * pieces for the player with the given colour.
     */
    public int evaluateBoard(ChessColour colour) {
        int sum = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != null) {
                    ChessPiece p = getPiece(row, col);
                    if (p.getColour() == colour) {
                        sum += p.evaluatePosition();
                    }
                }
            }
        }
        return sum;
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
     *
     * @return The state
     */
    public State getState() {
        return state;
    }

    /**
     *
     * @return String representation of this ChessBoard.
     */
    @Override
    public String toString() {
        StringBuilder bld = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
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
}
