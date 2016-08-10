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
package com.tomirio.schakert.view;

import com.tomirio.schakert.chessboard.ChessBoard;
import com.tomirio.schakert.chesspieces.Colour;
import com.tomirio.schakert.moves.Move;
import java.util.ArrayList;
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
     * The chess board.
     */
    private ChessBoard chessBoard;
    /**
     * A move consists of a turn by each player
     */
    private int moveCounter;
    /**
     * The labels for the number of the move.
     */
    private final ArrayList<Label> moveNumbers;
    /**
     * The labels for the String representation of the moves. A score label will
     * also be added to this list.
     */
    private final ArrayList<Label> moveStrings;
    /**
     * A ply refers to one turn taken bij one of the players.
     */
    private int plyCounter;

    /**
     * Constructor of the log.
     *
     * @param chessBoard The chess board.
     */
    public Log(ChessBoard chessBoard) {
        super();
        this.chessBoard = chessBoard;
        moveNumbers = new ArrayList();
        moveStrings = new ArrayList();
        plyCounter = 0;
        moveCounter = 1;
        this.setPrefWidth(150);
        this.setHgap(10);
        this.setVgap(10);
        this.vgapProperty().add(5);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    /**
     * Add a move to the log
     *
     * @param move  The move.
     * @param SAN   The SAN of the move.
     */
    public void addMove(Move move, String SAN) {
        if (plyCounter == 0) {
            /*
            Base case.
            We are making the first half move at the very beginning of the game, 
            add correct label and move to the log.
             */
            Label moveNumber = new Label(Integer.toString(moveCounter) + ".");
            moveNumber.setTextFill(Color.BLACK);

            this.add(moveNumber, 0, moveCounter);
            this.moveNumbers.add(moveNumber);

            Label newMove = new Label(SAN);
            newMove.setTextFill(Color.BLACK);

            this.add(newMove, 1, moveCounter);
            this.moveStrings.add(newMove);

            plyCounter++;
        } else // Non base case.
         if (plyCounter % 2 == 0) {
                // Enemy made half-move, so we are in a new move.
                moveCounter++;

                Label moveNumber = new Label(Integer.toString(moveCounter) + ".");
                moveNumber.setTextFill(Color.BLACK);
                this.add(moveNumber, 0, moveCounter);
                this.moveNumbers.add(moveNumber);

                Label newMove = new Label(SAN);
                newMove.setTextFill(Color.BLACK);
                this.add(newMove, 1, moveCounter);
                this.moveStrings.add(newMove);

                plyCounter++;
            } else {
                // I'm making the second half-move
                Label newMove = new Label(SAN);
                newMove.setTextFill(Color.BLACK);
                this.add(newMove, 2, moveCounter);
                this.moveStrings.add(newMove);
                plyCounter++;
            }
    }

    /**
     * Checks if the chess board is in an end state. If this is the case, the
     * log is updated accordingly.
     *
     * @return <code>True</code> if the chess board is in an end state,
     * <code>False</code> otherwise.
     */
    public boolean gameFinished() {
        if (chessBoard.inStalemate()) {
            // It is stale mate
            Label staleMate = new Label("½-½");
            staleMate.setTextFill(Color.BLACK);
            this.add(staleMate, 0, moveCounter + 1, 2, 1);
            this.moveStrings.add(staleMate);
            return true;
        } else if (chessBoard.inCheckmate(Colour.White)) {
            // Winner is black.
            Label blackIsWinner = new Label("0-1");
            blackIsWinner.setTextFill(Color.BLACK);
            this.add(blackIsWinner, 0, moveCounter + 1, 2, 1);
            this.moveStrings.add(blackIsWinner);
            return true;
        } else if (chessBoard.inCheckmate(Colour.Black)) {
            // Winner is white.
            Label whiteIsWinner = new Label("1-0");
            whiteIsWinner.setTextFill(Color.BLACK);
            this.add(whiteIsWinner, 0, moveCounter + 1, 2, 1);
            this.moveStrings.add(whiteIsWinner);
            return true;
        }
        return false;
    }

    public void setBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    /**
     * Undo the move.
     */
    public void undoMove() {
        if (plyCounter > 0) {
            // There is something to undo
            if (chessBoard.gameIsFinished()) {
                /*
                Remove score label, undo last half move, lower ply counter
                A Quick set of moves for testing checkmate:
                W	B
                f4	e5
                g4	Qh4#
                0-1
                 */
                Label scoreLabel = this.moveStrings.remove(moveStrings.size() - 1);
                this.getChildren().remove(scoreLabel);
                Label previousHalfMove = this.moveStrings.remove(moveStrings.size() - 1);
                this.getChildren().remove(previousHalfMove);

                plyCounter--;

            } else if (plyCounter % 2 == 0) {
                // This ply is the second one in this move, remove only this move
                Label previousHalfMove = this.moveStrings.remove(moveStrings.size() - 1);
                this.getChildren().remove(previousHalfMove);

                plyCounter--;
            } else {
                /*
                This ply was the beginning of a new move, 
                delete this half move and the move label.
                 */
                Label moveNumber = this.moveNumbers.remove(moveNumbers.size() - 1);
                this.getChildren().remove(moveNumber);
                Label previousHalfMove = this.moveStrings.remove(moveStrings.size() - 1);
                this.getChildren().remove(previousHalfMove);

                plyCounter--;
                if (moveCounter > 1) {
                    // The move counter must always be equal or greater than 1.
                    moveCounter--;
                }
            }
        }
    }

}
