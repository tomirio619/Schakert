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
package com.tomirio.schakert.agent;

import com.tomirio.schakert.moves.Move;

/**
 *
 * @author Tom Sandmann
 */
public class Node {

    /**
     * The move that took place in the parent node which resulted in the current
     * chess board.
     */
    public final Move move;
    /**
     * The parent.
     */
    public final Node parent;

    /**
     * Create a root node. This node does not have a parant and a move.
     */
    public Node() {
        this.parent = null;
        move = null;
    }

    /**
     *
     * @param parent The parent of this node.
     * @param move The move that was applied in this node.
     */
    public Node(Node parent, Move move) {
        this.parent = parent;
        this.move = move;
    }

    /**
     * Get the move that took place in the original chess board.
     *
     * @return The move that took place in the root of the tree, which
     * eventually led to the chess board of this node.
     */
    public Move getRootMove() {
        if (parent.parent == null) {
            return move;
        } else {
            return parent.getRootMove();
        }
    }

    /**
     * Prints the trace from the root node to this node.
     *
     * @return String of the trace from the rood to this node.
     */
    public String toTrace() {
        if (parent == null) {
            return move.toString();
        } else {
            return parent.toTrace() + move.toString();
        }
    }
}
