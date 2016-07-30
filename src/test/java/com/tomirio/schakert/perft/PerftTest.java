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
package com.tomirio.schakert.perft;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.ChessColour;
import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.game.FENParser;
import com.tomirio.schakert.moves.EnPassantMove;
import com.tomirio.schakert.moves.Move;
import com.tomirio.schakert.moves.PromotionMove;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author S4ndmann
 */
public class PerftTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    private final List<String> FENtestingStrings;

    private ChessBoard chessBoard;

    private int nrOfCaptureMoves;
    private int nrOfCheckMates;
    private int nrOfChecks;
    private int nrOfEnPassantMoves;
    private int nrOfNodes;
    private int nrOfPromotions;

    public PerftTest() {
        /**
         * See http://www.chessprogramming.net/perfect-perft/ The last numbers
         * repsectively mean the search depth (numbers of plies) and the correct
         * number of leaf nodes.
         *
         */
        FENtestingStrings = Arrays.asList(
                "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 6 119060324",
                "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 5 193690690",
                "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 7 178633661",
                "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1 6 706045033",
                "1k6/1b6/8/8/7R/8/8/4K2R b K - 0 1 5 1063513",
                // TalkChess PERFT Tests (by Martin Sedlak)
                //--Illegal ep move #1
                "3k4/3p4/8/K1P4r/8/8/8/8 b - - 0 1 6 1134888",
                //--Illegal ep move #2
                "8/8/4k3/8/2p5/8/B2P2K1/8 w - - 0 1 6 1015133",
                //--EP Capture Checks Opponent
                "8/8/1k6/2b5/2pP4/8/5K2/8 b - d3 0 1 6 1440467",
                //--Short Castling Gives Check
                "5k2/8/8/8/8/8/8/4K2R w K - 0 1 6 661072",
                //--Long Castling Gives Check
                "3k4/8/8/8/8/8/8/R3K3 w Q - 0 1 6 803711",
                //--Castle Rights
                "r3k2r/1b4bq/8/8/8/8/7B/R3K2R w KQkq - 0 1 4 1274206",
                //--Castling Prevented
                "r3k2r/8/3Q4/8/8/5q2/8/R3K2R b KQkq - 0 1 4 1720476",
                //--Promote out of Check
                "2K2r2/4P3/8/8/8/8/8/3k4 w - - 0 1 6 3821001",
                //--Discovered Check
                "8/8/1P2K3/8/2n5/1q6/8/5k2 b - - 0 1 5 1004658",
                //--Promote to give check
                "4k3/1P6/8/8/8/8/K7/8 w - - 0 1 6 217342",
                //--Under Promote to give check
                "8/P1k5/K7/8/8/8/8/8 w - - 0 1 6 92683",
                //--Self Stalemate
                "K1k5/8/P7/8/8/8/8/8 w - - 0 1 6 2217",
                //--Stalemate & Checkmate
                "8/k1P5/8/1K6/8/8/8/8 w - - 0 1 7 567584",
                //--Stalemate & Checkmate
                "8/8/2k5/5q2/5n2/8/5K2/8 b - - 0 1 4 23527"
        );

        nrOfCaptureMoves = 0;
        nrOfCheckMates = 0;
        nrOfChecks = 0;
        nrOfEnPassantMoves = 0;
        nrOfPromotions = 0;
        nrOfNodes = 0;
    }

    /**
     * Perform perft test. For perft divide:
     *
     * @see
     * <a href="http://www.open-aurec.com/wbforum/viewtopic.php?f=4&t=53226">
     * http://www.open-aurec.com/wbforum/viewtopic.php?f=4&t=53226 </a>
     * @see <a href="https://sites.google.com/site/numptychess/perft">
     * https://sites.google.com/site/numptychess/perft </a>
     * @see <a href="http://www.albert.nu/programs/sharper/perft/">
     * http://www.albert.nu/programs/sharper/perft/ </a>
     * @see <a href="http://www.rocechess.ch/perft.html">
     * http://www.rocechess.ch/perft.html </a>
     * @param depth The depth.
     * @param playerColour The colour of the player having turn.
     * @return The number of leaf nodes for the given depth.
     */
    private int perft(int depth, ChessColour playerColour, boolean showOutput) {
        int nodes = 0;
        if (depth == 0) {
            return 1;
        }
        for (ChessPiece piece : chessBoard.getPieces(playerColour)) {
            for (Move move : piece.getPossibleMoves()) {
                // Mate and stale mate moves are not counted.
                if (!move.isCheckMateMove() && !move.staleMateMove()) {
                    updateDebugVariables(move);
                    if (showOutput && move.isCaptureMove()) {
                        System.out.println("Capture move!");
                        System.out.println(move);
                        System.out.println("Het bord voor de move:\n" + chessBoard);
                    }
                    move.doMove();
                    if (showOutput && move.isCaptureMove()) {
                        System.out.println("Het bord na de move:\n" + chessBoard);
                    }
                    nodes += perft(depth - 1, playerColour.getOpposite(), showOutput);
                    move.undoMove();
                }

            }

        }
        return nodes;
    }

    private void printPerftResults(String FEN, int perftSearchDepth, int expectedNodeCount, boolean showOutput) {
        System.out.println("FEN string: " + FEN);
        System.out.println("ChessBoard:\n" + chessBoard + "\n");
        System.out.println("Perft depth:" + perftSearchDepth);
        nrOfNodes = perft(perftSearchDepth, ChessColour.White, showOutput);
        System.out.println("\n");
        System.out.println("Number of nodes:" + this.nrOfNodes);
        System.out.println("Expected number of nodes:" + expectedNodeCount);
        System.out.println("Number of captures:" + nrOfCaptureMoves);
        System.out.println("Number of checkmates:" + nrOfCheckMates);
        System.out.println("Number of checks:" + nrOfChecks);
        System.out.println("Number of en Passant captures:" + nrOfEnPassantMoves);
        System.out.println("Number of promotions:" + nrOfPromotions);
        System.out.println("\n");
        resetCount();
    }

    public void resetCount() {
        nrOfCaptureMoves = 0;
        nrOfCheckMates = 0;
        nrOfChecks = 0;
        nrOfEnPassantMoves = 0;
        nrOfPromotions = 0;
        nrOfNodes = 0;
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getChessBoard method, of class FENParser.
     */
    @Test
    public void testPerft() {
        for (String FEN : FENtestingStrings) {
            // Split at the whitespaces.
            String[] splittedAtWhiteSpace = FEN.split(" ");
            int length = splittedAtWhiteSpace.length;

            /**
             * Get search depth and expected node count from FEN string.
             */
            String searchDepth = splittedAtWhiteSpace[length - 2];
            String nodeCount = splittedAtWhiteSpace[length - 1];
            int perftSearchDepth = Integer.parseInt(searchDepth);
            int expectedNodeCount = Integer.parseInt(nodeCount);
            FENParser fenParser = new FENParser(FEN);
            this.chessBoard = fenParser.getChessBoard();

            /**
             * Calculate leaf nodes.
             */
            if (expectedNodeCount < 200000) {
                printPerftResults(FEN, perftSearchDepth, expectedNodeCount, false);
            }

        }
    }

    @Test
    public void testPerftFromInitialPosition() {
        String startingPositionFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        FENParser fenParser = new FENParser(startingPositionFEN);
        this.chessBoard = fenParser.getChessBoard();
        for (int searchDepth = 1; searchDepth < 5; searchDepth++) {
            printPerftResults(startingPositionFEN, searchDepth, 0, searchDepth == 4);
        }
    }

    private void updateDebugVariables(Move move) {
        if (move.isCaptureMove()) {
            nrOfCaptureMoves++;
        }
        if (move instanceof EnPassantMove) {
            nrOfEnPassantMoves++;
        }
        if (move.isCheckMateMove()) {
            nrOfCheckMates++;
        }
        if (move.isCheckMove()) {
            nrOfChecks++;
        }
        if (move instanceof PromotionMove) {
            nrOfPromotions++;
        }

    }

}
