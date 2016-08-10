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

import com.tomirio.schakert.chesspieces.Colour;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

/**
 *
 * @author S4ndmann
 */
public class PromotionAlert extends Alert {

    private final Colour colour;
    SVGPath blackRook = new SVGPath();
    SVGPath blackKnight = new SVGPath();
    SVGPath blackBishop = new SVGPath();
    SVGPath blackQueen = new SVGPath();

    SVGPath whiteRook = new SVGPath();
    SVGPath whiteKnight = new SVGPath();
    SVGPath whiteBishop = new SVGPath();
    SVGPath whiteQueen = new SVGPath();

    public PromotionAlert(Colour colour) {
        super(AlertType.NONE);
        this.colour = colour;
        this.setTitle("Promotion");
        this.setHeaderText("Choose the type for the pawn to promote into.");

        HBox options = new HBox();
        switch (colour) {

        }
    }

}
