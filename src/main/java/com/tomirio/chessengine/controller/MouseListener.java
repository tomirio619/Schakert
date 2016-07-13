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
package com.tomirio.chessengine.controller;

import com.tomirio.chessengine.agent.AI;
import com.tomirio.chessengine.chessboard.ChessColour;
import com.tomirio.chessengine.chessboard.Log;
import com.tomirio.chessengine.chessboard.PiecePosition;
import com.tomirio.chessengine.chessboard.State;
import com.tomirio.chessengine.game.Game;
import com.tomirio.chessengine.view.View;
import com.tomirio.chessengine.view.VisualTile;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Tom Sandmann
 */
public class MouseListener implements EventHandler<MouseEvent> {

    /**
     * The log.
     */
    public Log log;

    /**
     * The previous selected visual tile, initially null.
     */
    public VisualTile previousSelected;

    /**
     * The possibleMoves of the currently selected visual tile, initially null.
     */
    public ArrayList<PiecePosition> possibleMoves;

    /**
     * The view.
     */
    public View view;

    /**
     * The state.
     */
    public State state;

    /**
     * The game.
     */
    public Game game;

    /**
     *
     * @param log The log.
     * @param view The view.
     * @param state The state.
     * @param game The game.
     */
    public MouseListener(Log log, View view, State state, Game game) {
        this.state = state;
        this.view = view;
        this.log = log;
        this.game = game;
        previousSelected = null;
        possibleMoves = new ArrayList<>();
    }

    @Override
    public void handle(MouseEvent event) {
        VisualTile t = (VisualTile) event.getSource();

        // Remove the highlight of the previous tile if this tile is not null
        if (previousSelected != null) {
            previousSelected.removeHighlightTile();
        }

        if (t.chessPiece == null) {
            // A tile was selected that contained no chess piece
            if (possibleMoves.isEmpty()) {
                /*
                There were no possible moves, so the selected tile is not 
                a possible move for the previous selected chess piece
                 */
                previousSelected = t;
                t.highLightTile();
            } else if (possibleMoves.contains(new PiecePosition(t.row, t.column))) {
                /*
                The tile is a possible move of the previous selected chess piece.
                Make the move, remove the previous possibleMoves on the screen
                and empty the list of possible moves.
                 */
                game.humanPlay(previousSelected.chessPiece, t.row, t.column);
                removeAvailableMoves(possibleMoves);
                possibleMoves.clear();
            } else {
                removeAvailableMoves(possibleMoves);
                t.highLightTile();
                possibleMoves.clear();
                previousSelected = t;
            }

        } else // Current tile contains a chess piece
         if (possibleMoves.isEmpty()) {
                /*
                There where no possible moves so no chess piece can be captured.
                Show the possible moves of the chesspiece in this tile.
                Only do this when the player is human
                 */
                ChessColour playerColour = t.chessPiece.getColour();
                if (playerColour == state.getTurnColour()
                        && !state.weHaveAWinner()
                        && !(game.getPlayer(playerColour) instanceof AI)) {
                    possibleMoves = t.chessPiece.getPossibleMoves();
                    previousSelected = t;
                    t.highLightTile();
                    showAvailableMoves(possibleMoves);
                }
            } else if (possibleMoves.contains(new PiecePosition(t.row, t.column))) {
                /*
                A piece is being captured.
                Make the move, remove the possible moves on the screen and
                empty the list of possible moves.
                 */
                game.humanPlay(previousSelected.chessPiece, t.row, t.column);
                previousSelected = null;
                removeAvailableMoves(possibleMoves);
                possibleMoves.clear();
            } else {
                // There were some possible moves, but the chess piece was not in those
                removeAvailableMoves(possibleMoves);
                previousSelected = t;
                if (t.chessPiece.getColour() == state.getTurnColour() && !state.weHaveAWinner()) {
                    t.highLightTile();
                    possibleMoves = t.chessPiece.getPossibleMoves();
                    showAvailableMoves(possibleMoves);
                }
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
    public void showAvailableMoves(ArrayList<PiecePosition> possibleMoves) {
        if (possibleMoves != null) {
            view.showTilesAsMoves(possibleMoves);
        }
    }

    /**
     * Removes all the shown possible moves for the previously selected chess
     * piece.
     *
     * @param possibleMoves The positions of the previously visual tiles
     * currently shown as possible moves.
     */
    public void removeAvailableMoves(ArrayList<PiecePosition> possibleMoves) {
        if (possibleMoves != null) {
            view.removeTilesAsMoves(possibleMoves);
        }
    }
}
