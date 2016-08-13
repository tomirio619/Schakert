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
package com.tomirio.schakert.game;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.chesspieces.Bishop;
import com.tomirio.schakert.chesspieces.King;
import com.tomirio.schakert.chesspieces.Knight;
import com.tomirio.schakert.chesspieces.Pawn;
import com.tomirio.schakert.chesspieces.Queen;
import com.tomirio.schakert.chesspieces.Rook;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Tom Sandmann
 */
public class FENParser {

    /**
     * The FEN string
     */
    private final String FEN;

    /**
     * We call the board string the part in front of the first occuring white
     * space in a valid FEN string.
     */
    private String FENboardString;

    /**
     * We call the state string the part after the first occuring white space in
     * a valid FEN string.
     */
    private String FENstateString;

    /**
     * The chess board.
     */
    private final ChessBoard chessBoard;

    /**
     * The colour having turn.
     */
    private Colour hasTurn;

    /**
     * The number of full moves.
     */
    private int nrOfFullMoves;
    /**
     * The number of half moves.
     */
    private int nrOfHalfMoves;

    private final List<Character> ACN = Arrays.asList('p', 'n', 'b', 'r', 'q', 'k',
            'P', 'N', 'B', 'R', 'Q', 'K');
    private final List<Character> sideToMove = Arrays.asList('w', 'b');
    private final List<Character> digits = Arrays.asList('1', '2', '3', '4', '5', '6', '7');
    private final List<Character> castlingAbility = Arrays.asList('-', 'K', 'Q', 'k', 'q');
    private final List<Character> epFileLetter = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    private final List<Character> epRank = Arrays.asList('3', '6');

    /**
     *
     * @param FEN The FEN string.
     * @param chessBoard The chess board.
     */
    public FENParser(String FEN, ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        chessBoard.clearBoard();
        this.FEN = FEN;
    }

    /**
     * Parses the FEN string, assuming it is valid.
     */
    public void parse() {
        FENboardString = FEN.substring(0, FEN.indexOf(" "));
        FENstateString = FEN.substring(FEN.indexOf(" ") + 1);
        // Parse the fen
        parseFEN(FEN, 0);
        // Update the status of both kings.
        chessBoard.updateKingStatus();
    }

    public boolean isValidFEN(String newFEN) {
        int forwardSlashCount = newFEN.length() - newFEN.replace("/", "").length();
        if (forwardSlashCount != 7) {
            // There should be 7 forward slashes in a FEN string
            return false;
        }
        String board = newFEN.substring(0, newFEN.indexOf(" "));
        String[] ranks = board.split("/");
        for (String rank : ranks) {
            boolean validSquareCount = isValidEmptySquareCount(rank);
            if (!validSquareCount) {
                return false;
            }
            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);
                if (!Character.isDigit(c)) {
                    if (!ACN.contains(c)) {
                        return false;
                    }
                }
            }

        }
        // There should be one black king and one white king
        if (!isValidKingAmount(board)) {
            return false;
        }

        return isValidPawnAmount(board);
    }

    /**
     * Checks whether both sides only have one king.
     *
     * @param board The board.
     * @return <code>True</code> if both sides have exactly one king,
     * <code>False</code> otherwise.
     */
    private boolean isValidKingAmount(String board) {
        if (board.indexOf('k') != board.lastIndexOf('k') || board.indexOf('k') == -1) {
            return false;
        }
        return board.indexOf('K') == board.lastIndexOf('K') && board.indexOf('K') != -1;
    }

    /**
     * Checks whether the amount of pawns is below 8 for both sides.
     *
     * @param board The board.
     * @return <code>True</code> if the pawn amount is below or equal to 8 for
     * each of the players. <code>False</code> otherwise.
     */
    private boolean isValidPawnAmount(String board) {
        int blackPawnCount = board.length() - board.replace("p", "").length();
        int whitePawnCount = board.length() - board.replace("P", "").length();
        return blackPawnCount <= 8 && whitePawnCount <= 8;
    }

    /**
     * Checks whether empty square count and pieces add up to 8.
     *
     * @param rank The rank.
     * @return <code>True</code> if the rank and the empty squares add up to 8,
     * <code>False</code> otherwise.
     */
    private boolean isValidEmptySquareCount(String rank) {
        int count = 0;
        for (int i = 0; i < rank.length(); i++) {
            char c = rank.charAt(i);
            if (Character.isDigit(c)) {
                count += Character.getNumericValue(c);
            } else {
                count++;
            }
        }
        return count == 8;
    }

    /**
     * Each chess piece is identified by a single letter taken from the standard
     * English names (pawn = "P", knight = "N", bishop = "B", rook = "R", queen
     * = "Q" and king = "K"). White pieces are designated using upper-case
     * letters ("PNBRQK") while black pieces use lowercase ("pnbrqk"). This
     * function creates a chess piece based on this single letter.
     * <b>NOTE</b> that by default, we set "castling possible" values
     * <code>False</code>.
     *
     * @see
     * <a href="http://chess.stackexchange.com/questions/1482/how-to-know-when-a-fen-position-is-legal">
     * http://chess.stackexchange.com/questions/1482/how-to-know-when-a-fen-position-is-legal</a>
     *
     * @param c The letter representing the chess piece.
     * @param row The row of the chess piece.
     * @param column The column.
     */
    private void createPiece(char c, int row, int column) {
        switch (c) {
            // White pieces.
            case 'B': {
                Bishop b = new Bishop(Colour.White, new Position(row, column));
                chessBoard.setPiece(b);
                break;
            }
            case 'K': {
                King k = new King(Colour.White, new Position(row, column));
                k.setCastlingPossible(false);
                chessBoard.setPiece(k);
                chessBoard.setWhiteKing(k);
                break;
            }
            case 'N': {
                Knight n = new Knight(Colour.White, new Position(row, column));
                chessBoard.setPiece(n);
                break;
            }
            case 'P': {
                Pawn p = new Pawn(Colour.White, new Position(row, column));
                chessBoard.setPiece(p);
                break;
            }
            case 'Q': {
                Queen q = new Queen(Colour.White, new Position(row, column));
                chessBoard.setPiece(q);
                break;
            }
            case 'R': {
                Rook r = new Rook(Colour.White, new Position(row, column));
                r.setCastlingPossible(false);
                chessBoard.setPiece(r);
                break;
            }

            // Black pieces.
            case 'b': {
                Bishop b = new Bishop(Colour.Black, new Position(row, column));
                chessBoard.setPiece(b);
                break;
            }
            case 'k': {
                King k = new King(Colour.Black, new Position(row, column));
                k.setCastlingPossible(false);
                chessBoard.setPiece(k);
                chessBoard.setBlackKing(k);
                break;
            }
            case 'n': {
                Knight n = new Knight(Colour.Black, new Position(row, column));
                chessBoard.setPiece(n);
                break;
            }
            case 'p': {
                Pawn p = new Pawn(Colour.Black, new Position(row, column));
                chessBoard.setPiece(p);
                break;
            }
            case 'q': {
                Queen q = new Queen(Colour.Black, new Position(row, column));
                chessBoard.setPiece(q);
                break;
            }
            case 'r': {
                Rook r = new Rook(Colour.Black, new Position(row, column));
                r.setCastlingPossible(false);
                chessBoard.setPiece(r);
                break;
            }
            default:
                throw new IllegalArgumentException("A piece with letter " + c + " does not exist!");
        }
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public String getFEN() {
        return FEN;
    }

    public String getFENboardString() {
        return this.FENboardString;
    }

    public String getFENstateString() {
        return this.FENstateString;
    }

    public Colour getHasTurn() {
        return hasTurn;
    }

    public int getNrOfFullMoves() {
        return nrOfFullMoves;
    }

    public int getNrOfHalfMoves() {
        return nrOfHalfMoves;
    }

    private void parseCastlingAvailability(String castlingAvailability) {
        if (!castlingAvailability.equals("-")) {
            for (int i = 0; i < castlingAvailability.length(); i++) {
                char c = castlingAvailability.charAt(i);
                switch (c) {
                    case 'K':
                        // White can castle kingside.
                        chessBoard.getKing(Colour.White).setCastlingPossible(true);
                        chessBoard.getKingSideRook(Colour.White).setCastlingPossible(true);
                        break;
                    case 'Q':
                        // White can castle queenside.
                        chessBoard.getKing(Colour.White).setCastlingPossible(true);
                        chessBoard.getQueenSideRook(Colour.White).setCastlingPossible(true);
                        break;
                    case 'k':
                        // black can castle kingside.
                        chessBoard.getKing(Colour.Black).setCastlingPossible(true);
                        chessBoard.getKingSideRook(Colour.Black).setCastlingPossible(true);
                        break;
                    case 'q':
                        // Black can castle queenside.
                        chessBoard.getKing(Colour.Black).setCastlingPossible(true);
                        chessBoard.getQueenSideRook(Colour.Black).setCastlingPossible(true);
                        break;
                }
            }
        }

    }

    /**
     * Parse the en Passant target square.
     *
     * @param enPassantTargetSquare Parft of the FEN string possibly containing
     * the algebraic notation of the target enPassant square.
     */
    private void parseEnPassant(String enPassantTargetSquare) {
        if (!enPassantTargetSquare.equals("-")) {
            chessBoard.setEnPassantTargetSquare(new Position(enPassantTargetSquare));
            if (!enPassantTargetSquare.equals(chessBoard.getEnPassantTargetSquare().toString())) {
                throw new IllegalArgumentException("En passant target square was not equal to the parsed position.");
            }
        }
    }

    /**
     * <b>Forsyth–Edwards Notation (FEN)</b> is a standard notation for
     * describing a particular board position of a chess game. The purpose of
     * FEN is to provide all the necessary information to restart a game from a
     * particular position.
     *
     * A FEN "record" defines a particular game position, all in one text line
     * and using only the ASCII character set. A text file with only FEN data
     * records should have the file extension ".fen".
     *
     * <h1> A FEN record contains six fields. The separator between fields is a
     * space.</h1>
     * The fields are:
     *
     * 1) Piece placement (from white's perspective). Each rank is described,
     * starting with rank 8 and ending with rank 1; within each rank, the
     * contents of each square are described from file "a" through file "h".
     * Following the Standard Algebraic Notation (SAN), each piece is identified
     * by a single letter taken from the standard English names (pawn = "P",
     * knight = "N", bishop = "B", rook = "R", queen = "Q" and king = "K").
     * White pieces are designated using upper-case letters ("PNBRQK") while
     * black pieces use lowercase ("pnbrqk"). Empty squares are noted using
     * digits 1 through 8 (the number of empty squares), and "/" separates
     * ranks. Active color. "w" means White moves next, "b" means Black.
     *
     * 2) Castling availability. If neither side can castle, this is "-".
     * Otherwise, this has one or more letters: "K" (White can castle kingside),
     * "Q" (White can castle queenside), "k" (Black can castle kingside), and/or
     * "q" (Black can castle queenside).
     *
     * 3) En passant target square in algebraic notation. If there's no en
     * passant target square, this is "-". If a pawn has just made a two-square
     * move, this is the position "behind" the pawn. This is recorded regardless
     * of whether there is a pawn in position to make an en passant capture.
     *
     * 4) Halfmove clock: This is the number of halfmoves since the last capture
     * or pawn advance. This is used to determine if a draw can be claimed under
     * the fifty-move rule.
     *
     * 5) Fullmove number: The number of the full move. It starts at 1, and is
     * incremented after Black's move.
     *
     * @param FEN The FEN string.
     */
    private void parseFEN(String FEN, int row) {
        if (row == 8) {
            // in our previous call, we parsed rank 1 which is the final rank.
            parseStateInformation(FEN);
            return;
        }
        int forwardSlashIndex = FEN.indexOf("/");
        if (forwardSlashIndex == -1) {
            // Parsing the final rank, which is seperated with a white space
            forwardSlashIndex = FEN.indexOf(" ");
        }
        if (forwardSlashIndex > 8) {
            throw new IllegalArgumentException("A rank cannot contain more then 7 pieces!");
        }
        String rank = FEN.substring(0, forwardSlashIndex);

        int column = 0;
        for (int i = 0; i < rank.length(); i++) {
            char c = rank.charAt(i);
            if (Character.isDigit(c)) {
                // The character specifies the number of empty squares.
                column += Character.getNumericValue(c);
            } else {
                // The character specifices the piece.
                createPiece(c, row, column);
                column++;
            }
        }
        parseFEN(FEN.substring(forwardSlashIndex + 1), row + 1);
    }

    /**
     * Parse the number of full moves from the FEN string.
     *
     * @param fullMoveCounter Part of the FEN string containing the number of
     * full moves that took place.
     */
    private void parseFullMoveCounter(String fullMoveCounter) {
        this.nrOfFullMoves = Integer.parseInt(fullMoveCounter);
    }

    /**
     * Parse the number of half moves from the FEN string.
     *
     * @param halfMoveCounter Part of the FEN string containing the number of
     * half moves that took place.
     */
    private void parseHalfMoveCounter(String halfMoveCounter) {
        this.nrOfHalfMoves = Integer.parseInt(halfMoveCounter);
    }

    /**
     * After all the ranks have been parsed, we parse what we like to call the
     * "state information". This includes the castling availability, the en
     * Passant target square, the player that has turn and the full- and
     * halfmove counters.
     *
     * @param FEN The part of the (X)FEN String containging the information
     * described above.
     *
     */
    private void parseStateInformation(String FEN) {
        String[] stateInformation = FEN.split(" ");

        boolean FENincludesCounters = true;
        if (stateInformation.length == 3) {
            // The FEN string does not contain full move and half move counters.
            FENincludesCounters = false;
        }

        // Parse the colour of the player that has turn.
        String toMove = stateInformation[0];
        switch (toMove) {
            case "b":
                this.hasTurn = Colour.Black;
                break;
            case "w":
                this.hasTurn = Colour.White;
                break;
        }

        String castlingAvailability = stateInformation[1];
        this.parseCastlingAvailability(castlingAvailability);

        // Parse en Passant attack square.
        String enPassantTargetSquare = stateInformation[2];
        this.parseEnPassant(enPassantTargetSquare);

        if (FENincludesCounters) {
            // Parse half move counter.
            String halfMoveCounter = stateInformation[3];
            this.parseHalfMoveCounter(halfMoveCounter);

            // Parse full move counter.
            String fullMoveCounter = stateInformation[4];
            this.parseFullMoveCounter(fullMoveCounter);

        }

        // Parsing completed succesfully!
    }
}
