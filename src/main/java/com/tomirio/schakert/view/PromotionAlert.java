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

import com.tomirio.schakert.chessboard.Colour;
import com.tomirio.schakert.chessboard.PieceType;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Tom Sandmann
 */
public class PromotionAlert extends Dialog<PieceType> {

    private final ButtonType queen;
    private final ButtonType knight;
    private final ButtonType bishop;
    private final ButtonType rook;

    public PromotionAlert(Colour colour) {
        super();

        // Get the Stage.
        Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
        // Add a custom icon.
        stage.getIcons().add(ImageLoader.ICON);

        this.setTitle("Promotion");
        this.setHeaderText("Choose the type for the pawn to promote into.");

        /**
         * Black button types buttons.
         */
        queen = new ButtonType("Q", ButtonData.OK_DONE);
        rook = new ButtonType("R", ButtonData.OK_DONE);
        knight = new ButtonType("N", ButtonData.OK_DONE);
        bishop = new ButtonType("B", ButtonData.OK_DONE);
        /**
         * White promotion buttons.
         */

        /**
         * Add the possible images along with the buttons to the dialog.
         */
        HBox options = new HBox(35);
        switch (colour) {
            case Black:
                this.getDialogPane().getButtonTypes().addAll(queen, rook, knight, bishop);
                getDialogPane().autosize();
                // Add images.
                options.getChildren().addAll(new ImageView(
                        ImageLoader.BLACK_QUEEN),
                        new ImageView(ImageLoader.BLACK_KNIGHT),
                        new ImageView(ImageLoader.BLACK_ROOK),
                        new ImageView(ImageLoader.BLACK_BISHOP));
                break;
            default:
                //White
                this.getDialogPane().getButtonTypes().addAll(queen, rook, knight, bishop);
                // Add images.
                options.getChildren().addAll(new ImageView(
                        ImageLoader.WHITE_QUEEN),
                        new ImageView(ImageLoader.WHITE_KNIGHT),
                        new ImageView(ImageLoader.WHITE_ROOK),
                        new ImageView(ImageLoader.WHITE_BISHOP));
                break;
        }
        options.setAlignment(Pos.CENTER);
        options.setLayoutX(getDialogPane().getInsets().getLeft());
        this.getDialogPane().setContent(options);
        options.widthProperty().add(getDialogPane().widthProperty());

        /**
         * Specify the result when a button is pressed.
         */
        setResultConverter((ButtonType param) -> {
            switch (param.getText()) {
                case "Q":
                    return PieceType.Queen;
                case "R":
                    return PieceType.Rook;
                case "N":
                    return PieceType.Knight;
                case "B":
                    return PieceType.Bishop;
                default:
                    return null;
            }
        });
    }

}
