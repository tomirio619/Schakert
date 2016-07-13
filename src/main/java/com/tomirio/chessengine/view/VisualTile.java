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
import com.tomirio.chessengine.chessboard.ChessPiece;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;

/**
 *
 * @author Tom Sandmann
 * http://stackoverflow.com/questions/24082063/chessboard-with-automatic-resizing
 *
 */
public class VisualTile extends ToggleButton {

    /**
     * The chess piece contained in the visual tile
     */
    public ChessPiece chessPiece;

    /**
     * The row.
     */
    public final int row;

    /**
     * The column.
     */
    public final int column;

    /**
     * The WIDTH of the visual tile.
     */
    public static final int WIDTH = 47;

    /**
     * The HEIGHT of the visual tile.
     */
    public static final int HEIGHT = 47;

    /**
     * The colour of the visual tile.
     */
    public ChessColour tileColour;

    /**
     * The imageloader.
     */
    public ImageLoader imageLoader;

    /**
     * The chess board
     */
    public ChessBoard chessBoard;

    /**
     * This constructor will be used when a new game is started.
     *
     * @param row The row.
     * @param column The column.
     * @param chessBoard The chess board.
     */
    public VisualTile(int row, int column, ChessBoard chessBoard) {
        super();
        setPadding(Insets.EMPTY);        // Remove button insets
        setMinSize(WIDTH, WIDTH);
        setPrefSize(WIDTH, HEIGHT);      // Image WIDTH + 2.0, Image HEIGHT + 2.0
        setAlignment(Pos.CENTER);
        this.row = row;
        this.column = column;
        imageLoader = new ImageLoader();
        this.chessBoard = chessBoard;
        setTileColour();
    }

    /**
     * This constructor will be used when the game has already begun
     *
     * @param row The row.
     * @param column The column.
     * @param piece The chess piece.
     * @param chessBoard The chess board.
     */
    public VisualTile(int row, int column, ChessPiece piece, ChessBoard chessBoard) {
        super();
        setPadding(Insets.EMPTY);
        setMinSize(WIDTH, HEIGHT);
        setPrefSize(WIDTH, HEIGHT);
        setAlignment(Pos.CENTER);
        this.row = row;
        this.column = column;
        setTileColour();
        chessPiece = piece;
    }

    /**
     *
     * @param row The row.
     * @param column The column.
     * @param piece The chess piece.
     * @param currentWidth The width.
     * @param currentHeight The height.
     */
    public VisualTile(int row, int column, ChessPiece piece, double currentWidth, double currentHeight) {
        super();
        setPadding(Insets.EMPTY);
        setMinSize(WIDTH, HEIGHT);
        setPrefSize(currentWidth, currentHeight);
        setAlignment(Pos.CENTER);
        this.row = row;
        this.column = column;
        setTileColour();
        chessPiece = piece;
    }

    /**
     * Set the colour of the tile.
     */
    private void setTileColour() {
        if ((row + column) % 2 == 0) {
            setBackground(new Background(new BackgroundFill(
                    Paint.valueOf("BURLYWOOD"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            tileColour = ChessColour.White;
        } else {
            setBackground(new Background(new BackgroundFill(
                    Paint.valueOf("SADDLEBROWN"),
                    CornerRadii.EMPTY,
                    Insets.EMPTY)));
            tileColour = ChessColour.Black;
        }
    }

    /**
     * Sets the images and the pieces of the tiles for the first launch.
     */
    public void setInitialTileImageAndChessPiece() {
        for (int row = 0; row < chessBoard.ROWS; row++) {
            for (int col = 0; col < chessBoard.COLS; col++) {
                ImageView view = new ImageView();
                if (chessPiece != null) {
                    view.setImage(chessPiece.getIcon().getImage());
                    setGraphic(view);
                }
            }
        }
    }

    /**
     * Updates the tile image according to the current value of the chessPiece.
     *
     * @param newSize The new size.
     */
    public void updateTileImage(double newSize) {
        setGraphic(null);
        if (chessPiece != null) {
            setGraphic(chessPiece.getIcon());
            setMinSize(WIDTH, HEIGHT);
            setPrefSize(newSize, newSize);
        }
        setPrefSize(newSize, newSize);
        setMinSize(WIDTH, HEIGHT);
    }

    @Override
    public String toString() {
        switch (column) {
            case 0:
                return "(" + "a" + "," + (8 - row) + ")";
            case 1:
                return "(" + "b" + "," + (8 - row) + ")";
            case 2:
                return "(" + "c" + "," + (8 - row) + ")";
            case 3:
                return "(" + "d" + "," + (8 - row) + ")";
            case 4:
                return "(" + "e" + "," + (8 - row) + ")";
            case 5:
                return "(" + "f" + "," + (8 - row) + ")";
            case 6:
                return "(" + "g" + "," + (8 - row) + ")";
            case 7:
                return "(" + "h" + "," + (8 - row) + ")";
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Shows that the tile is selected when it was clicked by the user. This is
     * done by setting the background to dark green.
     */
    public void highLightTile() {
        setStyle("-fx-background-color: #006600; -fx-border-color: #404040;");
    }

    /**
     * Removes the highlight of the tile when the user selected another tile.
     * This is simply done by setting the style to the empty string, thereby
     * removing all the previous set styles.
     */
    public void removeHighlightTile() {
        setStyle("");
    }

    /**
     * Show that this tile is a possible move by painting the background light
     * green.
     */
    public void showAsPossibleMove() {
        setStyle("-fx-background-color: #00FF00; -fx-border-color: #404040;");
    }

    /**
     * Remove the indication that this tile is a possible move by setting the
     * background to its original colour.
     */
    public void removeAsPossibleMove() {
        switch (tileColour) {
            case Black:
                setBackground(new Background(new BackgroundFill(
                        Paint.valueOf("SADDLEBROWN"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                setStyle("");
                break;
            case White:
                setBackground(new Background(new BackgroundFill(
                        Paint.valueOf("BURLYWOOD"),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
                setStyle("");
                break;
        }
    }
}
