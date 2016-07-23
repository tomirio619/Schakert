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
package com.tomirio.chessengine.view;

import com.tomirio.chessengine.chessboard.ChessBoard;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chesspieces.King;
import com.tomirio.chessengine.moves.Move;
import java.util.NoSuchElementException;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Tom Sandmann
 */
public class Log extends GridPane {

    /**
     * A move consists of a turn by each player
     */
    private int moveCounter;

    /**
     * A ply refers to one turn taken bij one of the players.
     */
    private int plyCounter;

    /**
     * Constructor of the log.
     */
    public Log() {
        super();
        plyCounter = 0;
        moveCounter = 1;
        setStyle("-fx-background: transparent;");
        this.setPrefWidth(170);
        this.setHgap(10);
        this.setVgap(10);
        this.vgapProperty().add(5);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Add a move to the log
     *
     * @param move
     */
    public void addMove(Move move) {
        if (plyCounter == 0) {
            /*
            Base case.
            We are making the first half move at the very beginning of the game, 
            add correct label and move to the log.
             */
            Label moveNumber = new Label(Integer.toString(moveCounter) + ".");
            moveNumber.setTextFill(Color.BLACK);

            this.add(moveNumber, 0, moveCounter);

            Label newMove = new Label(move.toString());
            newMove.setTextFill(Color.BLACK);

            this.add(newMove, 1, moveCounter);

            plyCounter++;
        } else // Non base case.
        {
            if (plyCounter % 2 == 0) {
                // Enemy made half-move, so we are in a new move.
                moveCounter++;
                Label moveNumber = new Label(Integer.toString(moveCounter) + ".");
                moveNumber.setTextFill(Color.BLACK);
                this.add(moveNumber, 0, moveCounter);
                Label newMove = new Label(move.toString());
                newMove.setTextFill(Color.BLACK);
                this.add(newMove, 1, moveCounter);
                plyCounter++;
            } else {
                // I'm making the second half-move
                Label newMove = new Label(move.toString());
                newMove.setTextFill(Color.BLACK);
                this.add(newMove, 2, moveCounter);
                plyCounter++;
            }
        }
    }

    /**
     * Checks if the chess board is in an end state. If this is the case, the
     * log is updated accordingly.
     *
     * @param chessBoard The chess board.
     * @return <code>True</code> if the chess board is in an end state,
     * <code>False</code> otherwise.
     */
    public boolean gameFinished(ChessBoard chessBoard) {
        if (isStaleMate(chessBoard)) {
            // It is stale mate
            Label staleMate = new Label("½-½");
            staleMate.setTextFill(Color.BLACK);
            this.add(staleMate, 0, moveCounter + 1, 2, 1);
            return true;
        } else if (isCheckMate(chessBoard, ChessColour.White)) {
            // Winner is black.
            Label blackIsWinner = new Label("0-1");
            blackIsWinner.setTextFill(Color.BLACK);
            this.add(blackIsWinner, 0, moveCounter + 1, 2, 1);
            return true;
        } else if (isCheckMate(chessBoard, ChessColour.Black)) {
            // Winner is white.
            Label whiteIsWinner = new Label("1-0");
            whiteIsWinner.setTextFill(Color.BLACK);
            this.add(whiteIsWinner, 0, moveCounter + 1, 2, 1);
            return true;
        }
        return false;
    }

    /**
     *
     * @param chessBoard The chess board.
     * @param playerColour The colour of the player
     * @return  <code>True</code> if the colour of the player is check mate,
     * <code>False</code> otherwise.
     */
    public boolean isCheckMate(ChessBoard chessBoard, ChessColour playerColour) {
        King whiteKing = chessBoard.getKing(ChessColour.White);
        King blackKing = chessBoard.getKing(ChessColour.Black);
        switch (playerColour) {
            case Black:
                return blackKing.isCheck()
                        && blackKing.getPossibleMoves().isEmpty()
                        && !chessBoard.canMakeAMove(ChessColour.Black);

            case White:
                return whiteKing.isCheck()
                        && whiteKing.getPossibleMoves().isEmpty()
                        && !chessBoard.canMakeAMove(ChessColour.White);
            default:
                throw new NoSuchElementException();
        }
    }

    /**
     *
     * @param chessBoard The chess board.
     * @return <code>True</code> if the current state of the chess board is
     * stalemate. <code>False</code> otherwise.
     */
    public boolean isStaleMate(ChessBoard chessBoard) {
        King whiteKing = chessBoard.getKing(ChessColour.White);
        King blackKing = chessBoard.getKing(ChessColour.Black);
        boolean staleMate = whiteKing.getPossibleMoves().isEmpty()
                && !whiteKing.isCheck()
                && !chessBoard.canMakeAMove(ChessColour.White)
                || blackKing.getPossibleMoves().isEmpty()
                && !blackKing.isCheck()
                && !chessBoard.canMakeAMove(ChessColour.Black);
        return staleMate;
    }

    public void undoMove() {

    }

}
