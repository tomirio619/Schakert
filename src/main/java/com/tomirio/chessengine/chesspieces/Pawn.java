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
package com.tomirio.chessengine.chesspieces;

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.MoveDetails;
import com.tomirio.chessengine.chessboard.PieceType;
import com.tomirio.chessengine.chessboard.Position;
import com.tomirio.chessengine.moves.CaptureMove;
import com.tomirio.chessengine.moves.Move;
import com.tomirio.chessengine.moves.NormalMove;
import com.tomirio.chessengine.moves.PromotionMove;
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class Pawn extends ChessPiece {

    /**
     * This constructor <b>MUST</b> be used when the chessboard is not known
     * when a new chess piece is made. The chessboard must be set later on with
     * <code>setBoard()</code>. It is called with a <code>PiecePosition</code>.
     *
     * @param colour The colour of the chess piece.
     * @param pos The position of the chess piece.
     */
    public Pawn(ChessColour colour, Position pos) {
        super(PieceType.Pawn, colour, pos);
    }

    /**
     *
     * @param colour The colour of the piece.
     * @param pos The positon on the board.
     * @param chessBoard The chess board.
     */
    public Pawn(ChessColour colour, Position pos, ChessBoard chessBoard) {
        super(PieceType.Pawn, colour, pos, chessBoard);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * Get the capture moves
     *
     * @return
     */
    private MoveDetails getCaptureMoves() {
        MoveDetails moveDetails = new MoveDetails();
        int direction = (getColour() == ChessColour.Black) ? 1 : -1;
        Position left = new Position(getRow() + direction * 1, getColumn() - 1);
        Position right = new Position(getRow() + direction * 1, getColumn() + 1);

        ArrayList<Position> positions = new ArrayList();
        positions.add(left);
        positions.add(right);

        for (Position pos : positions) {
            if (pos.isValid()) {
                if (chessBoard.isOccupiedPosition(pos)) {
                    if (chessBoard.getColour(pos) == getColour()) {
                        // Friendly piece is covered
                        moveDetails.coveredFriendlyPieces.add(pos);
                    } else {
                        // Enemy piece
                        switch (getColour()) {
                            case Black:
                                if (pos.getRow() == 7) {
                                    // Black pawn promotes
                                    PromotionMove promotionMove = new PromotionMove(this, pos);
                                    moveDetails.moves.add(promotionMove);
                                } else {
                                    // A capture move without promotion
                                    CaptureMove captureMove = new CaptureMove(this, pos);
                                    moveDetails.moves.add(captureMove);
                                }
                            case White:
                                if (pos.getRow() == 0) {
                                    // White pawn promotes
                                    PromotionMove promotionMove = new PromotionMove(this, pos);
                                    moveDetails.moves.add(promotionMove);
                                } else {
                                    // A capture move without promotion
                                    CaptureMove captureMove = new CaptureMove(this, pos);
                                    moveDetails.moves.add(captureMove);
                                }
                        }
                    }
                }
            }
        }
        return moveDetails;
    }

    @Override
    public ArrayList<Position> getCoveredPositions() {
        return getPawnMoves().coveredFriendlyPieces;
    }

    /**
     * Get the intial non capture moves.
     *
     * @return
     */
    private ArrayList<Move> getInitialNonCaptureMoves() {
        ArrayList<Move> initialMoves = new ArrayList();
        int direction = (getColour() == ChessColour.Black) ? 1 : -1;
        Position singleStep = new Position(getRow() + direction * 1, getColumn());
        if (chessBoard.isOccupiedPosition(singleStep)) {
            return initialMoves;
        } else {
            NormalMove singleStepMove = new NormalMove(this, singleStep);
            initialMoves.add(singleStepMove);

            Position doubleStep = new Position(getRow() + direction * 2, getColumn());
            if (chessBoard.isOccupiedPosition(doubleStep)) {
                return initialMoves;
            } else {
                NormalMove doubleStepMove = new NormalMove(this, doubleStep);
                initialMoves.add(doubleStepMove);
                return initialMoves;
            }
        }
    }

    /**
     * Get the non capture moves.
     *
     * @return
     */
    private ArrayList<Move> getNonCaptureMoves() {
        ArrayList<Move> initialMoves = new ArrayList();
        int direction = (getColour() == ChessColour.Black) ? 1 : -1;
        Position singleStep = new Position(getRow() + direction * 1, getColumn());

        if (chessBoard.isOccupiedPosition(singleStep)) {
            // Position in front of pawn is occupied
            return initialMoves;
        } else {
            // Position in front of pawn is free

            switch (getColour()) {
                case Black:
                    if (singleStep.getRow() == 7) {
                        // A black pawn promotes
                        PromotionMove blackPromotionMove = new PromotionMove(this, singleStep);
                        initialMoves.add(blackPromotionMove);
                    } else {
                        // Normal move black pawn
                        NormalMove normalMove = new NormalMove(this, singleStep);
                        initialMoves.add(normalMove);
                    }
                    return initialMoves;
                case White:
                    if (singleStep.getRow() == 0) {
                        // A white pawn promotes
                        PromotionMove whitePromotionMove = new PromotionMove(this, singleStep);
                        initialMoves.add(whitePromotionMove);
                    } else {
                        // Normal move white pawn
                        NormalMove normalMove = new NormalMove(this, singleStep);
                        initialMoves.add(normalMove);
                    }
                default:
                    return initialMoves;
            }
        }

    }

    /**
     * Get the pawn moves.
     *
     * @return
     */
    private MoveDetails getPawnMoves() {
        MoveDetails moveDetails = new MoveDetails();
        moveDetails.add(getCaptureMoves());
        if (getPos().getRow() == 1 || getPos().getRow() == 6) {
            moveDetails.moves.addAll(getInitialNonCaptureMoves());
            return moveDetails;
        } else {
            moveDetails.moves.addAll(getNonCaptureMoves());
            return moveDetails;
        }
    }

    @Override
    public ArrayList<Move> getPossibleMoves() {
        return filterMoves(getPawnMoves().moves);
    }

    @Override
    public ArrayList<Move> getRawPossibleMoves() {
        return getPawnMoves().moves;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean posIsCovered(Position pos) {
        return getPawnMoves().coveredFriendlyPieces.contains(pos);
    }

}
