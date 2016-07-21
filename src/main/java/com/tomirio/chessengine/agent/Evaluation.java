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
package com.tomirio.chessengine.agent;

import com.tomirio.chessengine.chessboard.ChessBoard;
import static com.tomirio.chessengine.chessboard.ChessBoard.COLS;
import static com.tomirio.chessengine.chessboard.ChessBoard.ROWS;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.Position;
import java.util.NoSuchElementException;

/**
 *
 * @author S4ndmann
 */
public class Evaluation {

    /**
     * Bishop piece value.
     */
    private final int B;
    /**
     * King piece value.
     */
    private final int K;

    /**
     * Knight piece value.
     */
    private final int N;

    /**
     * Pawn piece value.
     */
    private final int P;
    /**
     * Queen piece value.
     */
    private final int Q;

    /**
     * Rook piece value.
     */
    private final int R;

    /**
     * Constructor.
     */
    public Evaluation() {
        N = 300;
        Q = 900;
        P = 100;
        K = 20000;
        R = 500;
        B = 330;
    }

    /**
     * In order for negaMax to work, your Static Evaluation function must return
     * a score relative to the side to being evaluated.
     *
     * The Evaluation Function
     *
     * @param chessBoard The chess board.
     * @param playerColour The colour the AI is playing with
     * @return Value indicating the 'goodness' of the current board for the
     * given colour.
     */
    public double evaluate(ChessBoard chessBoard, ChessColour playerColour) {
        ChessColour hasTurn = null; //TODO MUST FIX THIS.
        double evaluationScore = evaluateBoard(chessBoard, hasTurn);
        double enemyEvaluationScore = evaluateBoard(chessBoard, hasTurn.getOpposite());
        double heuristicValue = evaluationScore - enemyEvaluationScore;
        return (playerColour == hasTurn) ? heuristicValue : -heuristicValue;
    }

    /**
     * Evaluates the chessboard based on the given colour.
     *
     * @param chessBoard The chess board.
     * @param colour The colour of the player.
     * @return An approximation of the relative score of the position of the
     * pieces for the player with the given colour on the given chess board.
     */
    private int evaluateBoard(ChessBoard chessBoard, ChessColour colour) {
        int sum = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (chessBoard.isOccupiedPosition(row, col)) {
                    ChessPiece p = chessBoard.getPiece(row, col);
                    if (p.getColour() == colour) {
                        sum += this.getPieceValue(p, chessBoard);
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Methods for looking up the bonus for specific chess types based on the
     * position on a given chess board.
     */
    private int getBishopBonus(ChessColour colour, Position pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.BISHOP_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = PieceSquareTables.BISHOP_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;

    }

    private int getKingBonus(ChessColour colour, Position pos, ChessBoard chessBoard) {
        int weight = 0;
        int[][] king_table;

        // Determine if we need to use middle game or endgame tables.
        if (chessBoard.getQueens(ChessColour.White).isEmpty()
                && chessBoard.getQueens(ChessColour.Black).isEmpty()) {
            // We are in end game
            king_table = PieceSquareTables.KING_TABLE_END;
        } else {
            king_table = PieceSquareTables.KING_TABLE_MIDDLE;
        }
        switch (colour) {
            case White:

                weight = king_table[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = king_table[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
//        if (chessBoard.getState().isCheckMate(colour)) {
//            return 0;
//        } else {
//            return weight;
//        }
        return weight;
    }

    private int getKnightBonus(ChessColour colour, Position pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.KNIGHT_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = PieceSquareTables.KNIGHT_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;
    }

    private int getPawnBonus(ChessColour colour, Position pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.PAWN_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                weight = PieceSquareTables.PAWN_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;
    }

    /**
     * Get the piece value.
     *
     * @param piece The chess piece.
     * @param chessBoard The chess board.
     * @return
     */
    public int getPieceValue(ChessPiece piece, ChessBoard chessBoard) {
        switch (piece.getType()) {
            case Bishop:
                return B + getBishopBonus(piece.getColour(), piece.getPos());
            case Rook:
                return R + getRookBonus(piece.getColour(), piece.getPos());
            case King:
                return K + getKingBonus(piece.getColour(), piece.getPos(), chessBoard);
            case Knight:
                return N + getKnightBonus(piece.getColour(), piece.getPos());
            case Pawn:
                return P + getPawnBonus(piece.getColour(), piece.getPos());
            case Queen:
                return Q + getQueenBonus(piece.getColour(), piece.getPos());
            default:
                throw new NoSuchElementException();
        }
    }

    private int getQueenBonus(ChessColour colour, Position pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.QUEEN_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = PieceSquareTables.QUEEN_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;
    }

    private int getRookBonus(ChessColour colour, Position pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.ROOK_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = PieceSquareTables.ROOK_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;
    }

}
