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
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.PiecePosition;

/**
 *
 * @author Tom Sandmann
 */
public class Node {

    /**
     * The data
     */
    public final ChessBoard chessBoard;

    /**
     * The parent.
     */
    public final Node parent;

    /**
     * The move that took place in the parent node which resulted in the current
     * chess board.
     */
    public final PiecePosition move;

    /**
     * The piece that was moved.
     */
    public final ChessPiece chessPiece;

    /**
     * The heuristic value.
     */
    public final int heuristicValue;

    /**
     *
     * @param chessBoard The chess board of this node.
     * @param parent The parent of this node.
     * @param heuristicValue The heuristic value of the board.
     */
    public Node(ChessBoard chessBoard, Node parent, int heuristicValue) {
        this.parent = parent;
        this.chessBoard = chessBoard;
        this.heuristicValue = heuristicValue;
        chessPiece = null;
        move = null;
    }

    /**
     *
     * @param data The data that this node contains.
     * @param parent The parent of this node.
     * @param heuristicValue The heuristic value of the board.
     * @param move The move.
     */
    public Node(ChessBoard data, Node parent, int heuristicValue, PiecePosition move, ChessPiece chessPiece) {
        this.chessBoard = data;
        this.parent = parent;
        this.heuristicValue = heuristicValue;
        this.move = move;
        this.chessPiece = chessPiece;
    }

    /**
     *
     * @return String representation of this node.
     */
    @Override
    public String toString() {
        return chessBoard.toString();
    }

    /**
     * Prints the trace from the root node to this node.
     *
     * @return String of the trace from the rood to this node.
     */
    public String toTrace() {
        if (parent == null) {
            return toString();
        } else {
            return parent.toTrace() + toString();
        }
    }

    /**
     * Get the move that took place in the original chess board.
     *
     * @return The move that took place in the root of the tree, which
     * eventually led to the chess board of this node.
     */
    public PiecePosition getRootMove() {
        if (parent.parent == null) {
            return move;
        } else {
            return parent.getRootMove();
        }
    }

    /**
     * Get the chess piece that was moved in the original chess board.
     *
     * @return The chess piece that was moved in the root of the tree, which
     * eventually led to the chess board of this node.
     */
    public ChessPiece getRootPiece() {
        if (parent.parent == null) {
            return chessPiece;
        } else {
            return parent.getRootPiece();
        }
    }
}
