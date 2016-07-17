/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomirio.chessengine.agent;

import com.tomirio.chessengine.chessboard.ChessBoard;
import static com.tomirio.chessengine.chessboard.ChessBoard.COLS;
import static com.tomirio.chessengine.chessboard.ChessBoard.ROWS;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.PiecePosition;
import com.tomirio.chessengine.chessboard.State;
import java.util.NoSuchElementException;

/**
 *
 * @author S4ndmann
 */
public class Evaluation {

    /**
     * Queen piece value.
     */
    private final int Q;

    /**
     * Knight piece value.
     */
    private final int N;

    /**
     * Pawn piece value.
     */
    private final int P;

    /**
     * King piece value.
     */
    private final int K;

    /**
     * Castle piece value.
     */
    private final int C;

    /**
     * Bishop piece value.
     */
    private final int B;

    public Evaluation() {
        N = 300;
        Q = 900;
        P = 100;
        K = 20000;
        C = 500;
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
        State currentState = chessBoard.getState();
        ChessColour hasTurn = currentState.getTurnColour();
        double evaluationScore = evaluateBoard(chessBoard, hasTurn);
        double enemyEvaluationScore = evaluateBoard(chessBoard, hasTurn.getOpposite());
        double heuristicValue = evaluationScore - enemyEvaluationScore;
        return (playerColour == hasTurn) ? heuristicValue : -heuristicValue;
    }

    public int getPieceValue(ChessPiece piece, ChessBoard chessBoard) {
        switch (piece.getType()) {
            case Bishop:
                return B + getBishopBonus(piece.getColour(), piece.getPos());
            case Castle:
                return C + getCastleBonus(piece.getColour(), piece.getPos());
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
    private int getBishopBonus(ChessColour colour, PiecePosition pos) {
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

    private int getCastleBonus(ChessColour colour, PiecePosition pos) {
        int weight = 0;
        switch (colour) {
            case White:
                weight = PieceSquareTables.CASTLE_TABLE[pos.getRow()][pos.getColumn()];
                break;
            case Black:
                // Mirrored access
                weight = PieceSquareTables.CASTLE_TABLE[7 - pos.getRow()][pos.getColumn()];
                break;
            default:
                throw new NoSuchElementException();
        }
        return weight;
    }

    private int getKingBonus(ChessColour colour, PiecePosition pos, ChessBoard chessBoard) {
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
        if (chessBoard.getState().isCheckMate(colour)) {
            return 0;
        } else {
            return weight;
        }
    }

    private int getKnightBonus(ChessColour colour, PiecePosition pos) {
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

    private int getPawnBonus(ChessColour colour, PiecePosition pos) {
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

    private int getQueenBonus(ChessColour colour, PiecePosition pos) {
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

}
