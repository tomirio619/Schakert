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
package com.tomirio.schakert.controller;

import com.tomirio.schakert.agent.AI;
import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.game.Game;
import com.tomirio.schakert.moves.Move;
import com.tomirio.schakert.view.View;
import com.tomirio.schakert.view.VisualTile;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Tom Sandmann
 */
public class MouseListener implements EventHandler<MouseEvent> {

    /**
     * Currently selected visual tile.
     */
    private VisualTile currentlySelectedVisualTile;
    /**
     * The game.
     */
    public Game game;
    /**
     * The possibleMoves of the currently selected visual tile, initially null.
     */
    public ArrayList<Move> possibleMoves;
    /**
     * The previous selected visual tile, initially null.
     */
    private VisualTile previousSelectedVisualTile;

    /**
     * The view.
     */
    public View view;

    /**
     *
     * @param view The view.
     * @param game The game.
     */
    public MouseListener(View view, Game game) {
        this.view = view;
        this.game = game;
        previousSelectedVisualTile = null;
        possibleMoves = new ArrayList<>();
    }

    private void checkNonCaptureMove() {
        if (possibleMoves.isEmpty()) {
            /*
            There were no possible moves, so the selected tile is not
            a possible move for the previous selected chess piece
             */
            previousSelectedVisualTile = currentlySelectedVisualTile;
            currentlySelectedVisualTile.highLightTile();
        } else {
            Move move = getMove(new Position(currentlySelectedVisualTile.row, currentlySelectedVisualTile.column));
            if (move != null) {
                /*
                The tile is a possible move of the previous selected chess piece.
                Make the move, remove the previous possibleMoves on the screen
                and empty the list of possible moves.
                 */
                removeAvailableMoves(possibleMoves);
                possibleMoves.clear();
                game.humanPlay(move);

            } else {

                removeAvailableMoves(possibleMoves);
                currentlySelectedVisualTile.highLightTile();
                possibleMoves.clear();
                previousSelectedVisualTile = currentlySelectedVisualTile;
            }
        }

    }

    /**
     * See if a position is the new position of a move.
     *
     * @param newPos The new position.
     * @return  <code>Move</code> if the given position was indeed an end
     * position of the move. <code>null</code> otherwise.
     */
    private Move getMove(Position newPos) {
        for (Move move : possibleMoves) {
            if (move.getNewPos().equals(newPos)) {
                return move;
            }
        }
        return null;
    }

    @Override
    public void handle(MouseEvent event) {
        currentlySelectedVisualTile = (VisualTile) event.getSource();
        removePreviousHighlight();

        if (currentlySelectedVisualTile.getChessPiece() == null) {
            // The move is not a capture move
            checkNonCaptureMove();
        } else // The move could be a capture move
         if (possibleMoves.isEmpty()) {
                // The move is not a capture move
                showNewPossibleMoves();
            } else {
                // The move could still be a capture move
                Move move = getMove(new Position(currentlySelectedVisualTile.row, currentlySelectedVisualTile.column));
                if (move != null) {
                    // The move did exists, apply the move
                    previousSelectedVisualTile = null;
                    removeAvailableMoves(possibleMoves);
                    possibleMoves.clear();
                    game.humanPlay(move);

                } else {
                    // There was no move with this end position
                    removeAvailableMoves(possibleMoves);
                    previousSelectedVisualTile = currentlySelectedVisualTile;
                    if (currentlySelectedVisualTile.getChessPiece().getColour() == game.getTurnColour()) {
                        currentlySelectedVisualTile.highLightTile();
                        possibleMoves = currentlySelectedVisualTile.getChessPiece().getPossibleMoves();
                        showAvailableMoves(possibleMoves);
                    }
                }
            }

    }

    /**
     * Removes all the shown possible moves for the previously selected chess
     * piece.
     *
     * @param possibleMoves The positions of the previously visual tiles
     * currently shown as possible moves.
     */
    private void removeAvailableMoves(ArrayList<Move> possibleMoves) {
        if (possibleMoves != null) {
            view.removeTilesAsMoves(possibleMoves);
        }
    }

    private void removePreviousHighlight() {
        // Remove the highlight of the previous tile if this tile is not null
        if (previousSelectedVisualTile != null) {
            previousSelectedVisualTile.removeHighlightTile();
        }
    }

    /**
     * Shows All the possible moves if a visualTile with a chess piece is
     * selected.
     *
     * @param possibleMoves The positions of the visual tiles that are possible
     * moves for the selected piece.
     *
     */
    private void showAvailableMoves(ArrayList<Move> possibleMoves) {
        if (possibleMoves != null) {
            view.showTilesAsMoves(possibleMoves);
        }
    }

    /**
     * Show the possible moves for a chess piece.
     */
    private void showNewPossibleMoves() {
        /*
        There where no possible moves so no chess piece can be captured.
        Show the possible moves of the chesspiece in this tile.
        Only do this when the player is human
         */
        Colour playerColour = currentlySelectedVisualTile.getChessPiece().getColour();
        if (playerColour == game.getTurnColour() && !(game.getPlayer(playerColour) instanceof AI)) {
            possibleMoves = currentlySelectedVisualTile.getChessPiece().getPossibleMoves();
            previousSelectedVisualTile = currentlySelectedVisualTile;
            currentlySelectedVisualTile.highLightTile();
            showAvailableMoves(possibleMoves);
        }
    }

}
