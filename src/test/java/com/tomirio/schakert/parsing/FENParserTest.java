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
package com.tomirio.schakert.parsing;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.chesspieces.King;
import com.tomirio.schakert.game.FENParser;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Tom Sandmann
 */
public class FENParserTest {

    private final List<String> FENtestingStrings;

    public FENParserTest() {
        // see http://mathieupage.com/?p=65 for a FEN database
        FENtestingStrings = Arrays.asList(
                "8/​1p6/​1P1p4/​1B1Pk2p/​8/​7K/​8/​4r3 b - - 0 52"
        );

    }

    /**
     * Checks whether castling availability is correctly set for a given
     * fenparser.
     *
     * @param fenParser The FEN parser.
     * @return <code>True</code> if castling availability is correctly set,
     * <code>False</code> otherwise.
     */
    private boolean castlingAvailabilityIsCorrectlySet(FENParser fenParser) {
        ChessBoard chessBoard = fenParser.getChessBoard();
        String state = fenParser.getFENstateString();
        // Get castling availaibility part of the FEN string.
        String castlingAvailability = state.split(" ")[1];

        if (castlingAvailability.equals("-")) {
            // The kings should not be able to castle.
            King blackKing = chessBoard.getKing(Colour.Black);
            King whiteKing = chessBoard.getKing(Colour.White);
            return !blackKing.getCastlingPossible()
                    && !whiteKing.getCastlingPossible();
        } else {
            for (int i = 0; i < castlingAvailability.length(); i++) {
                Character c = castlingAvailability.charAt(i);
                switch (c) {
                    case 'K':
                        // White should be able to castle kingside.
                        return chessBoard.getKing(Colour.White).getCastlingPossible()
                                && chessBoard.getKingSideRook(Colour.White).getCastlingPossible();
                    case 'Q':
                        // White should be able to castle queenside.
                        return chessBoard.getKing(Colour.White).getCastlingPossible()
                                && chessBoard.getQueenSideRook(Colour.White).getCastlingPossible();

                    case 'k':
                        // black should be able to castle kingside.
                        return chessBoard.getKing(Colour.Black).getCastlingPossible()
                                && chessBoard.getKingSideRook(Colour.Black).getCastlingPossible();
                    case 'q':
                        // Black should be able to castle queenside.
                        return chessBoard.getKing(Colour.Black).getCastlingPossible()
                                && chessBoard.getQueenSideRook(Colour.Black).getCastlingPossible();
                }
            }
        }
        return true;
    }

    @Test
    public void testInitialFENParse() {
        ChessBoard chessBoard = new ChessBoard();
        FENParser fenParser = new FENParser(ChessBoard.START_POSITION, chessBoard);
        fenParser.parse();
        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < ChessBoard.COLS; col++) {
                assertEquals(chessBoard.isOccupiedPosition(row, col), false);
            }
        }
        assertTrue(castlingAvailabilityIsCorrectlySet(fenParser));
    }
}
