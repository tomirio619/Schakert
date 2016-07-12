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
import java.util.ArrayList;

/**
 *
 * @author Tom Sandmann
 */
public class Node {

    /**
     * The data
     */
    public ChessBoard chessBoard;

    /**
     * The parent.
     */
    public Node parent;

    /**
     * The heuristic value.
     */
    public int heuristicValue;

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
    }

    /**
     *
     * @param data The data that this node contains.
     * @param parent The parent of this node.
     * @param children The children of this node.
     * @param heuristicValue The heuristic value of the board.
     */
    public Node(ChessBoard data, Node parent, ArrayList<Node> children, int heuristicValue) {
        this.chessBoard = data;
        this.parent = parent;
        this.heuristicValue = heuristicValue;
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
            return parent.toTrace() + "\n" + toString();
        }
    }
}
