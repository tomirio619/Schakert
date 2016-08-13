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
import com.tomirio.schakert.chessboard.ChessPiece;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.moves.CapturePromotionMove;
import com.tomirio.schakert.moves.EnPassantMove;
import com.tomirio.schakert.moves.Move;
import com.tomirio.schakert.moves.PromotionMove;
import com.tomirio.schakert.utils.NaturalOrderComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Tom Sandmann
 */
public class PerftTest {

    private static final boolean DEBUG = false;

    private final List<String> FENtestingStrings;

    private ChessBoard chessBoard;

    private int nrOfCaptureMoves;
    private int nrOfCheckmates;
    private int nrOfChecks;
    private int nrOfEnPassantMoves;
    private int nrOfNodes;
    private int nrOfPromotions;

    public PerftTest() {
        /**
         * See <a>http://www.chessprogramming.net/perfect-perft/</a>
         * The last numbers respectively mean the search depth (numbers of
         * plies) and the correct number of leaf nodes.
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
        nrOfCheckmates = 0;
        nrOfChecks = 0;
        nrOfEnPassantMoves = 0;
        nrOfPromotions = 0;
        nrOfNodes = 0;
    }

    /**
     * Perform perft test. For perft divide: A good chess engine to compare the
     * results with is "Stockfish"
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
    private int dividePerft(int depth) {
        if (depth == 0) {
            return 1;
        }
        int nodes = 0;
        ArrayList<String> results = new ArrayList();
        ArrayList<ChessPiece> pieces = chessBoard.getPieces(chessBoard.getHasTurn());
        for (ChessPiece piece : pieces) {
            for (Move move : piece.getPossibleMoves()) {
                String position = move.getInvolvedPiece().getPos().toString() + move.getNewPos().toString();
                String suffix = getPromotionSuffix(move);
                position += suffix;
                // doMove also updates hasTurn
                move.doMove();
                int intermediatePerft = perft(depth - 1, chessBoard.getHasTurn());
                results.add(position + " " + intermediatePerft);
                nodes += intermediatePerft;
                move.undoMove();
            }
        }
        System.out.println("\n");
        System.out.println("Results of divide perft at depth " + depth);
        System.out.println("The FEN was as follows: " + chessBoard.getFEN());
        Collections.sort(results, new NaturalOrderComparator());
        results.stream().forEach((result) -> {
            System.out.println(result);
        });
        System.out.println("\n");
        System.out.println("Number of nodes: " + nodes);
        return nodes;
    }

    private String getPromotionSuffix(Move move) {
        if (!(move instanceof PromotionMove || move instanceof CapturePromotionMove)) {
            return "";
        }
        if (move instanceof PromotionMove) {
            PromotionMove promotionMove = (PromotionMove) move;
            switch (move.getInvolvedPiece().getColour()) {
                case Black:
                    return promotionMove.getPromotionType().toShortString().toLowerCase();
                default:
                    // White
                    return promotionMove.getPromotionType().toShortString();
            }
        } else {
            // move instanceof CapturePromotionMove
            CapturePromotionMove promotionMove = (CapturePromotionMove) move;
            switch (move.getInvolvedPiece().getColour()) {
                case Black:
                    return promotionMove.getPromotionType().toShortString().toLowerCase();
                default:
                    // White
                    return promotionMove.getPromotionType().toShortString();
            }
        }
    }

    /**
     * Perft algorithm.
     *
     * @param depth The depth.
     * @param colour The colour of the player
     * @return Number of leaf nodes in the game tree.
     */
    private int perft(int depth, Colour colour) {
        int nodes = 0;
        if (depth == 0) {
            return 1;
        } else if (chessBoard.inStalemate() || chessBoard.inCheckmate(colour)) {
            // We do not count terminal nodes!
            return 0;
        }
        ArrayList<ChessPiece> pieces = chessBoard.getPieces(colour);
        for (ChessPiece piece : pieces) {
            for (Move move : piece.getPossibleMoves()) {
                move.doMove();
                nodes += perft(depth - 1, colour.getOpposite());
                move.undoMove();
            }
        }
        return nodes;

    }

    private void printPerftResults(String FEN, int perftSearchDepth) {
        chessBoard = new ChessBoard();
        chessBoard.loadFEN(FEN);
        dividePerft(perftSearchDepth);
        resetCount();
    }

    public void resetCount() {
        nrOfCaptureMoves = 0;
        nrOfCheckmates = 0;
        nrOfChecks = 0;
        nrOfEnPassantMoves = 0;
        nrOfPromotions = 0;
        nrOfNodes = 0;
    }

    @Test
    public void perftFromCustomFEN() {
        String FEN = "5B2/6P1/1p6/8/1N6/kP6/2K5/8 w - -";
        int depth = 7;
        printPerftResults(FEN, depth);

    }

    private void updateDebugVariables(Move move) {
        if (move.isCaptureMove()) {
            nrOfCaptureMoves++;
        }
        if (move instanceof EnPassantMove) {
            nrOfEnPassantMoves++;
        }
        if (move.inCheckmateMove()) {
            nrOfCheckmates++;
        }
        if (move.inCheckMove()) {
            nrOfChecks++;
        }
        if (move instanceof PromotionMove) {
            nrOfPromotions++;
        }
    }

    @Test
    public void InitialPosition() {
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - ";
        int[] results = {1, 20, 400, 8902, 197281, 4865609};
        assertTrue(verify(results, FEN, 0));
    }

    @Test
    public void Position2() {
        String FEN = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        int[] results = {48, 2039, 97862, 4085603};
        assertTrue(verify(results, FEN, 1));
    }

    @Test
    public void Position3() {
        String FEN = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -";
        int[] results = {14, 191, 2812, 43238, 674624};
        assertTrue(verify(results, FEN, 1));
    }

    @Test
    public void Position4() {
        String FEN = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";
        String mirroredFEN = "r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1";
        int[] results = {6, 264, 9467, 422333};
        assertTrue(verify(results, FEN, 1));
        assertTrue(verify(results, mirroredFEN, 1));
    }

    @Test
    public void Position5() {
        String FEN = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ -";
        int[] results = {44, 1486, 62379, 2103487};
        assertTrue(verify(results, FEN, 1));
    }

    @Test
    public void Position6() {
        String FEN = "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - -";
        int[] results = {1, 46, 2079, 89890, 3894594};
        assertTrue(verify(results, FEN, 0));
    }

    @Test
    public void Position7() {
        // The so-called "Behting Study"
        String FEN = "8/8/7p/3KNN1k/2p4p/8/3P2p1/8 w - -";
        int[] results = {25, 180, 4098, 46270, 936094};
        assertTrue(verify(results, FEN, 1));
    }

    @Test
    public void Position8() {
        // The so-called "Djaja Study"
        String FEN = "6R1/P2k4/r7/5N1P/r7/p7/7K/8 w - -";
        int[] results = {32, 657, 18238, 419717};
        assertTrue(verify(results, FEN, 1));
    }

    @Test
    public void Position9() {
        // HAKMEM 70
        // <a>https://chessprogramming.wikispaces.com/Bill+Gosper</a>
        String FEN = "5B2/6P1/1p6/8/1N6/kP6/2K5/8 w - -";
        int[] results = {18, 27, 524, 1347, 28021, 107618, 2446328};
        assertTrue(verify(results, FEN, 1));
    }

    /**
     * Verifies a FEN string with this chess engine.
     *
     * @param results The perft results for the given FEN.
     * @param FEN The FEN.
     * @param startingDepth The starting depth.
     * @return
     */
    private boolean verify(int[] results, String FEN, int startingDepth) {
        chessBoard = new ChessBoard();
        chessBoard.loadFEN(FEN);
        int depth = startingDepth;
        for (int expectedResult : results) {
            long startTime = System.nanoTime();
            int result = dividePerft(depth);
            if (result != expectedResult) {
                System.out.println("The FEN was as follows: " + FEN);
                System.out.println("At depth " + depth + " , the expected result was " + expectedResult);
                System.out.println("However, divide perft gave us the following result at this depth: " + result);
                resetCount();
                return false;
            }
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            System.out.println("Elapsed time: " + elapsedTime / 1000000000.0 + " s");
            depth++;
        }
        resetCount();
        return true;
    }

}
