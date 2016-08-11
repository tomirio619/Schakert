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

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.game.Player;
import com.tomirio.schakert.moves.Move;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;

/**
 *
 * @author Tom Sandmann
 */
public class AI extends Player implements Callable<Move> {

    /**
     * Skipped nodes
     */
    protected static int skippedNodes = 0;

    /**
     * Evaluation class
     */
    public final Evaluation eval;
    /**
     * The search depth
     */
    public int searchDepth;

    /**
     * Visited nodes
     */
    private final HashSet<Integer> visitedNodes;

    /**
     * The AI
     *
     * @param playerColour The colour the AI plays with.
     * @param chessBoard The chess board.
     */
    public AI(Colour playerColour, ChessBoard chessBoard) {
        super(playerColour, chessBoard);
        visitedNodes = new HashSet();
        searchDepth = 3;
        eval = new Evaluation();
    }

    @Override
    public Move call() throws Exception {
        return getPlay();
    }

    /**
     * Generate the next set moves for the given colour.
     *
     * @param node The root node.
     * @param hasTurn The colour for which we want to generate the moves.
     * @return ArrayList containing all the future chess boards.
     */
    private ArrayList<Node> generateChildNodes(Node parentNode, Colour hasTurn) {
        ArrayList<Node> childNodes = new ArrayList<>();
        ArrayList<ChessPiece> pieces = chessBoard.getPieces(hasTurn);
        for (ChessPiece piece : pieces) {
            ArrayList<Move> succesivePieceMoves = piece.getPossibleMoves();
            for (Move move : succesivePieceMoves) {
                Node childNode = new Node(parentNode, move);
                childNodes.add(childNode);
            }
        }
        return childNodes;
    }

    /**
     *
     * @return The move
     */
    @Override
    public Move getPlay() {
        long startTime = System.nanoTime();
        Node rootNode = new Node();
        Pair<Node, Double> toPlay = negaMax(rootNode, searchDepth, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, playerColour);
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / Math.pow(10, 9);
        System.out.println("Searched nodes:" + visitedNodes.size());
        System.out.println("Skipped nodes:" + skippedNodes);
        System.out.println("Elapsed time:" + elapsedTime);
        System.out.println("Nodes per second:" + skippedNodes / elapsedTime + "\n");
        visitedNodes.clear();
        skippedNodes = 0;
        Move move = toPlay.first.getRootMove();
        return move;
    }

    @Override
    public void makeMove(Move move) {
        // Only necessary for human players.
        throw new UnsupportedOperationException("Agents do not implement this method!");
    }

    /**
     * <head>Perform NegaMax search with alpha beta pruning</head>
     *
     * @see <a href="https://en.wikipedia.org/wiki/Negamax">
     * https://en.wikipedia.org/wiki/Negamax
     * </a>
     *
     * <b>"Negamax always searches for the maximum value for all its nodes"</b>
     * The prevailing search routine is <b>negamax</b>, and for good reason.
     * It's clear, simple, and can be extended from something very basic all the
     * way to parallel search. Once you get it working the next step is replace
     * the evaluation call with <b>quiescence search</b> at the leaf nodes (to
     * prevent terrible, terrible blunders because of an abrupt search horizon)
     *
     * <h1>Techniques to add:</h1>
     * - Null Move Pruning - Check Extension - Late Move Reduction - NegaMax
     * search - Quiescence search - Move Ordering
     *
     * <h1>Monitoring the application</h1>
     * See VisualVM, which is a free and good java profiler.
     * @see
     * <a href="http://stackoverflow.com/questions/17379849/simple-minimax-evaluation-function-for-chess-position">
     * http://stackoverflow.com/questions/17379849/simple-minimax-evaluation-function-for-chess-position
     * </a>
     *
     * <h1>Debugging move generation</h1>
     * <b>Perft</b>, (performance test, move path enumeration) a debugging
     * function to walk the move generation tree of strictly legal moves to
     * count all the leaf nodes of a certain depth, which can be compared to
     * predetermined values and used to isolate bugs.
     *
     * @param node The node we want to evaluate.
     * @param depth The depth.
     * @param alpha Alpha used in alpha-beta pruning.
     * @param beta Beta used in alpha-beta pruning.
     * @param hasTurn The colour of the player.
     * @return The best value possible See
     * http://stackoverflow.com/questions/25615312/negamax-chess-algorithm-how-to-use-final-return
     */
    public Pair<Node, Double> negaMax(Node node, int depth, double alpha, double beta, Colour hasTurn) {
        if (depth == 0 || chessBoard.inStalemate()
                || chessBoard.inCheckmate(Colour.Black)
                || chessBoard.inCheckmate(Colour.White)) {
            return new Pair<>(node, eval.evaluate(chessBoard, playerColour, hasTurn));
        }
        ArrayList<Node> childNodes = generateChildNodes(node, hasTurn);
        double bestValue = Double.NEGATIVE_INFINITY;
        Node bestNode = node;
        for (Node child : childNodes) {
            child.move.doMove();
            /*
            This is the time whether we know what the best play is for the 
            resulting board configuration. If we do know that, we use this value.
            Lets make the hash of the current chessboard the key to get the transpoition entry.
            If we do not know this, we search it.
             */
            Pair<Node, Double> result = negaMax(child, depth - 1, -beta, -alpha, hasTurn.getOpposite());
            child.move.undoMove();
            double v = -result.second;
//            if (child.move.isCaptureMove()) {
//                System.out.println("Het stuk dat werd gecaptured was:" + child.move.getNewPos());
//                System.out.println("De waarde van eval was " + v + " met de volgende kleur aan zet:" + hasTurn);
//                System.out.println("Het bord was als volgt:\n" + chessBoard);
//            }
            if (v >= bestValue) {
                bestValue = v;
                bestNode = result.first;
            }
            alpha = Math.max(alpha, v);
            if (alpha >= beta) {
                return new Pair<>(bestNode, bestValue);
            }
        }
        return new Pair<>(bestNode, bestValue);
    }
}
