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
import javafx.application.Platform;
import org.apache.commons.lang3.SerializationUtils;

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
    public Pair<Node, Double> negaMax(Node node, int depth, double alpha, double beta, ChessColour colour) {
        /*
        Negamax with alpha beta pruning.
        https://en.wikipedia.org/wiki/Negamax
         */
        if (depth == 0 || (node.chessBoard.getState().getWinner() != null)) {
            int boardValue = node.chessBoard.evaluateBoard(colour);
            double result = (colour == playerColour) ? boardValue : -1 * boardValue;
            return new Pair<>(node, result);
        }
        ArrayList<Node> childNodes = generateChildNodes(node, colour);
        double bestValue = Double.NEGATIVE_INFINITY;
        Node bestNode = node;
        PiecePosition move = node.move;
        boolean finished = false;
        for (int i = 0; i < childNodes.size() && !finished; i++) {
            Node child = childNodes.get(i);
            Pair<Node, Double> result = negaMax(child, depth - 1, -beta, -alpha, colour.getOpposite(colour));
            double v = -1 * result.second;
            if (v >= bestValue) {
                bestValue = v;
                bestNode = result.first;
            }
            bestValue = Math.max(bestValue, v);
            alpha = Math.max(alpha, v);
            if (alpha >= beta) {
                finished = true;
            }
        }
        return new Pair<>(bestNode, bestValue);
    }

    /**
     * Generate the next moves.
     *
     * @param node The root node.
     * @param colour The colour for which the moves have to generated.
     * @return ArrayList containing all the future chess boards.
     */
    private ArrayList<Node> generateChildNodes(Node node, ChessColour colour) {
        ArrayList<Node> childNodes = new ArrayList<>();
        ArrayList<ChessPiece> pieces = node.chessBoard.getPieces(colour);
        for (ChessPiece piece : pieces) {
            ArrayList<PiecePosition> moves = piece.getPossibleMoves();
            for (PiecePosition move : moves) {
                /**
                 * Store the score of the succecors of the root chessboard in a
                 * hash tabel indexed by (previousHash, move). previousHash is
                 * the hash of the chessboard on which the move was applied. The
                 * score belongs to the chessboard that resulted from the
                 * applied move on the previous chessboard
                 */
                ChessBoard chessBoardCopy = node.chessBoard.deepClone();
                ChessPiece pieceToMove = chessBoardCopy.getPiece(piece.getPos());
                ChessPiece pieceCopy = SerializationUtils.clone(pieceToMove);
                chessBoardCopy.movePieceAgent(pieceToMove, move.getRow(), move.getColumn());
                int hash = chessBoardCopy.getHash();
                if (!visitedNodes.contains(hash)) {
                    visitedNodes.add(hash);
                    Node childNode = new Node(chessBoardCopy, node, chessBoardCopy.evaluateBoard(colour), move, pieceCopy);
                    childNodes.add(childNode);
                } else {
                    skippedNodes++;
                }
            }
        }
        return childNodes;
    }

    /**
     * Techniques to add: - Null Move Pruning - Check Extension - Late Move
     * Reduction - NegaMax search - Quiescence search - Move Ordering
     */
    /**
     * The prevailing search routine is negamax, and for good reason. It's
     * clear, simple, and can be extended from something very basic all the way
     * to parallel search. Once you get it working the next step is replace the
     * evaluation call with quiescence search at the leaf nodes (to prevent
     * terrible, terrible blunders because of an abrupt search horizon)
     */
    /**
     * SEE PERFORMANCE OF METHODES: - Go to the tab "Profile" - Select "Profile
     * Project" - Click on the arrow pointing down in the button "profile" -
     * Select "Method" - Click on the button "profile"
     */
    // Also see VisualVM, which is a free and good java profiler.
    // http://stackoverflow.com/questions/17379849/simple-minimax-evaluation-function-for-chess-position
    @Override
    public void play() {
        long startTime = System.nanoTime();
        Node rootNode = new Node(chessBoard, null, chessBoard.evaluateBoard(playerColour));
        Pair<Node, Double> toPlay = negaMax(rootNode, 3, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, playerColour);
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / Math.pow(10, 9);
        System.out.println("Searched nodes:" + visitedNodes.size());
        System.out.println("Skipped nodes:" + skippedNodes);
        System.out.println("Elapsed time:" + elapsedTime);
        System.out.println("Nodes per second:" + skippedNodes / elapsedTime);
        skippedNodes = 0;
        PiecePosition move = toPlay.first.getRootMove();
        ChessPiece p = toPlay.first.getRootPiece();
        /*
        Because the icon field is transient in the chess piece, 
        we must move the piece based on the piece position.
         */
        Platform.runLater(() -> chessBoard.movePiece(p.getPos(), move.getRow(), move.getColumn()));
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
