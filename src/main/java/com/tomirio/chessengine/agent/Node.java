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
import com.tomirio.chessengine.chessboard.Position;

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
     * The piece that was moved.
     */
    public final ChessPiece chessPiece;

    /**
     * The move that took place in the parent node which resulted in the current
     * chess board.
     */
    public final Position move;
    /**
     * The parent.
     */
    public final Node parent;

    /**
     *
     * @param chessBoard The chess board of this node.
     * @param parent The parent of this node.
     */
    public Node(ChessBoard chessBoard, Node parent) {
        this.parent = parent;
        this.chessBoard = chessBoard;
        chessPiece = null;
        move = null;
    }

    /**
     *
     * @param data The data that this node contains.
     * @param parent The parent of this node.
     * @param move The move.
     * @param chessPiece The chess piece
     */
    public Node(ChessBoard data, Node parent, Position move, ChessPiece chessPiece) {
        this.chessBoard = data;
        this.parent = parent;
        this.move = move;
        this.chessPiece = chessPiece;
    }

    /**
     * Get the move that took place in the original chess board.
     *
     * @return The move that took place in the root of the tree, which
     * eventually led to the chess board of this node.
     */
    public Position getRootMove() {
        toTrace();
        if (parent.parent == null) {
            System.out.println("De move was " + move);
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
            System.out.println("Het stuk was " + chessPiece);
            return chessPiece;
        } else {
            return parent.getRootPiece();
        }
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
}
