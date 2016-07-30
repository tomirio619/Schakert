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
import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.chessboard.MoveDetails;
import com.tomirio.schakert.chessboard.PieceType;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.moves.CaptureMove;
import com.tomirio.schakert.moves.EnPassantMove;
import com.tomirio.schakert.moves.Move;
import com.tomirio.schakert.moves.NormalMove;
import com.tomirio.schakert.moves.PromotionMove;
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

    /**
     * Get the capture moves for pawns not standing on their initial position.
     *
     * @return  The movedetails for this pawn.
     */
    private MoveDetails getCaptureMoves() {
        MoveDetails moveDetails = new MoveDetails();
        int rowShift = (getColour() == ChessColour.Black) ? 1 : -1;
        Position left = new Position(getRow() + rowShift, getColumn() - 1);
        Position right = new Position(getRow() + rowShift, getColumn() + 1);

        ArrayList<Position> positions = new ArrayList();
        positions.add(left);
        positions.add(right);

        for (Position newPos : positions) {
            if (newPos.isValid()) {
                if (chessBoard.isOccupiedPosition(newPos)) {
                    if (chessBoard.getColour(newPos) == getColour()) {
                        // Friendly piece is covered
                        moveDetails.coveredFriendlyPieces.add(newPos);
                    } else {
                        // Enemy piece
                        switch (getColour()) {
                            case Black:
                                if (newPos.getRow() == 7) {
                                    // Black pawn promotes
                                    PromotionMove promotionMove = new PromotionMove(this, newPos);
                                    moveDetails.moves.add(promotionMove);
                                } else {
                                    // A capture move without promotion
                                    CaptureMove captureMove = new CaptureMove(this, newPos);
                                    moveDetails.moves.add(captureMove);
                                }
                            case White:
                                if (newPos.getRow() == 0) {
                                    // White pawn promotes
                                    PromotionMove promotionMove = new PromotionMove(this, newPos);
                                    moveDetails.moves.add(promotionMove);
                                } else {
                                    // A capture move without promotion
                                    CaptureMove captureMove = new CaptureMove(this, newPos);
                                    moveDetails.moves.add(captureMove);
                                }
                        }
                    }
                }
                else{
                    // New position is not occupied.
                    if (newPos.equals(chessBoard.getEnPassantTargetSquare())) {
                    /*
                    To prevent pieces of similar colours trying to execute an
                    enPassant on a friendly piece, we must check if there is an enemy
                    on the row above or below the vulnerable position.
                     */
                    // Inverse the rowshift, making it negative for black and positive for white pieces.
                    rowShift = -rowShift;
                    Position enPassantAttackSquare = chessBoard.getEnPassantTargetSquare();
                    if (chessBoard.isOccupiedPosition(enPassantAttackSquare.getRow() + rowShift,
                            enPassantAttackSquare.getColumn())) {
                        // The position is occupied.
                        ChessPiece p = chessBoard.getPiece(enPassantAttackSquare.getRow() + rowShift,
                                enPassantAttackSquare.getColumn());
                        if (p.getColour() != getColour() && p.getType() == PieceType.Pawn) {
                            // Enemy pawn, we can execute enPassant on it
                            ChessPiece chessPiece = getCapturedEnPassantPawn();
                            EnPassantMove enPassantMove = new EnPassantMove(this, newPos, chessPiece);
                            moveDetails.moves.add(enPassantMove);
                        }
                    }
                }
                }
            }
        }
        return moveDetails;
    }

    /**
     * Return the pawn that will be captured with an enPassant move.
     *
     * @return The pawn captured by the enPassant move.
     */
    private ChessPiece getCapturedEnPassantPawn() {
        ChessPiece enPassantPawn = chessBoard.getPiece(getRow(), chessBoard.getEnPassantTargetSquare().getColumn());
        return enPassantPawn;
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
        Position singleStep = new Position(getRow() + direction, getColumn());
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
        Position singleStep = new Position(getRow() + direction, getColumn());
        if (!singleStep.isValid() || chessBoard.isOccupiedPosition(singleStep)) {
            // Position in front of pawn is occupied or position is not valid.
            return initialMoves;
        } else {
            // Position in front of pawn is free.
            switch (getColour()) {
                case Black:
                    if (singleStep.getRow() == 7) {
                        // A black pawn promotes.
                        PromotionMove blackPromotionMove = new PromotionMove(this, singleStep);
                        initialMoves.add(blackPromotionMove);
                    } else {
                        // Normal move black pawn.
                        NormalMove normalMove = new NormalMove(this, singleStep);
                        initialMoves.add(normalMove);
                    }
                    return initialMoves;
                case White:
                    if (singleStep.getRow() == 0) {
                        // A white pawn promotes.
                        PromotionMove whitePromotionMove = new PromotionMove(this, singleStep);
                        initialMoves.add(whitePromotionMove);
                    } else {
                        // Normal move white pawn.
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
        switch (getColour()) {
            case Black:
                if (getPos().getRow() == 1) {
                    moveDetails.moves.addAll(getInitialNonCaptureMoves());
                } else {
                    moveDetails.moves.addAll(getNonCaptureMoves());
                }
                break;
            case White:
                if (getPos().getRow() == 6) {
                    moveDetails.moves.addAll(getInitialNonCaptureMoves());
                } else {
                    moveDetails.moves.addAll(getNonCaptureMoves());
                }
                break;
        }
        return moveDetails;
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
    public boolean posCanBeCaptured(Position p) {
        int rowShift = (getColour() == ChessColour.Black) ? 1 : -1;
        Position left = new Position(getRow() + rowShift, getColumn() - 1);
        Position right = new Position(getRow() + rowShift, getColumn() + 1);
        return (p.equals(left) || p.equals(right));
    }

    @Override
    public boolean posIsCovered(Position p) {
        return getPawnMoves().coveredFriendlyPieces.contains(p);
    }

}
