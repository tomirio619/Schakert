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
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.ChessPiece;
import com.tomirio.chessengine.chessboard.PiecePosition;
import com.tomirio.chessengine.game.Player;
import java.util.ArrayList;
import java.util.HashSet;


/**
 *
 * @author Tom Sandmann
 */
public class AI extends Player implements Runnable {

    public static int skippedNodes = 0;

    private final HashSet<Integer> visitedNodes;

    public AI(ChessColour playerColour, ChessBoard chessBoard) {
        super(playerColour, chessBoard);
        visitedNodes = new HashSet();
    }

    /**
     * Perform NegaMax search .
     *
     * @param node The node we currently check.
     * @param depth The depth.
     * @param alpha Alpha used in alpha-beta pruning.
     * @param beta Beta used in alpha-beta pruning.
     * @param colour The color of the player that can make a move.
     * @return The best value possible
     */
    public double negaMax(Node node, int depth, double alpha, double beta, ChessColour colour) {
        /*
        Negamax with alpha beta pruning.
        https://en.wikipedia.org/wiki/Negamax
         */
        if (depth == 0 || (node.chessBoard.getState().getWinner() != null)) {
            int boardValue = node.chessBoard.evaluateBoard(colour);
            int result = (colour == playerColour) ? boardValue : -1 * boardValue;
            return result;
        }
        ArrayList<Node> childNodes = generateChildNodes(node, colour);
        double bestValue = Double.NEGATIVE_INFINITY;
        boolean finished = false;
        for (int i = 0; i < childNodes.size() && !finished; i++) {
            Node child = childNodes.get(i);
            double v = -1 * negaMax(child, depth - 1, -beta, -alpha, colour.getOpposite(colour));

//            if (v >= bestValue) {
//                bestValue = v;
//                // TODO Keep track of node belonging to bestValue
//            }
            bestValue = Math.max(bestValue, v);
            alpha = Math.max(alpha, v);
            if (alpha >= beta) {
                finished = true;
            }
        }
        return bestValue;
    }

    /**
     * Generate the next moves.
     * @param node      The root node.
     * @param colour    The colour for which the moves have to generated.
     * @return          ArrayList containing all the future chess boards.
     */
    private ArrayList<Node> generateChildNodes(Node node, ChessColour colour) {
        ArrayList<Node> childNodes = new ArrayList<>();
        ArrayList<ChessPiece> pieces = node.chessBoard.getPieces(colour);
        for (ChessPiece p : pieces) {
            ArrayList<PiecePosition> moves = p.getPossibleMoves();
            for (PiecePosition move : moves) {
                ChessBoard chessBoardCopy = node.chessBoard.deepClone();
                ChessPiece pieceToMove = chessBoardCopy.getPiece(p.getPos());
                chessBoardCopy.movePieceAgent(pieceToMove, move.getRow(), move.getColumn());
                int hash = chessBoardCopy.getHash();
                if (!visitedNodes.contains(hash)) {
                    visitedNodes.add(hash);
                    Node childNode = new Node(chessBoardCopy, node, chessBoardCopy.evaluateBoard(colour));
                    childNodes.add(childNode);
                } else {
                    skippedNodes++;
                }
            }
        }
        return childNodes;
    }

    @Override
    public void play() {
        long startTime = System.nanoTime();
        Node rootNode = new Node(chessBoard, null, chessBoard.evaluateBoard(playerColour));
        double bestPlay = negaMax(rootNode, 4, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, playerColour);
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / Math.pow(10, 9);
        System.out.println("Searched nodes:" + visitedNodes.size());
        System.out.println("Skipped nodes:" + skippedNodes);
        System.out.println("Elapsed time:" + elapsedTime);
        System.out.println("Nodes per second:" + skippedNodes / elapsedTime);
        skippedNodes = 0;
        /**
         * Techniques to add: - Null Move Pruning - Check Extension - Late Move
         * Reduction - NegaMax search - Quiescence search - Move Ordering
         */
        /**
         * The prevailing search routine is negamax, and for good reason. It's
         * clear, simple, and can be extended from something very basic all the
         * way to parallel search. Once you get it working the next step is
         * replace the evaluation call with quiescence search at the leaf nodes
         * (to prevent terrible, terrible blunders because of an abrupt search
         * horizon)
         */
        /**
         * SEE PERFORMANCE OF METHODES:
         * - Go to the tab "Profile"
         * - Select "Profile Project"
         * - Click on the arrow pointing down in the button "profile"
         * - Select "Method"
         * - Click on the button "profile"
         */
        // Also see VisualVM, which is a free and good java profiler.
        // http://stackoverflow.com/questions/17379849/simple-minimax-evaluation-function-for-chess-position
    }

    @Override
    public void makeMove(ChessPiece piece, int row, int col) {
        // Only necessary for human players
        throw new UnsupportedOperationException("Agents do not implement this method!");
    }

    @Override
    public void run() {
        play();
    }

}
