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

import javafx.scene.image.Image;

/**
 *
 * @author Tom Sandmann
 */
public final class ImageLoader {

    /**
     * black bishop image.
     */
    public static Image blackBishop;

    /**
     * black king image.
     */
    public static Image blackKing;

    /**
     * black knight image.
     */
    public static Image blackKnight;

    /**
     * black pawn image.
     */
    public static Image blackPawn;

    /**
     * black queen image.
     */
    public static Image blackQueen;
    /**
     * black rook image.
     */
    public static Image blackRook;
    /**
     * application icon.
     */
    public static Image icon;
    /**
     * Path resources.
     */
    static String path = "png";

    /**
     * black bishop image.
     */
    public static Image whiteBishop;

    /**
     * white king image.
     */
    public static Image whiteKing;

    /**
     * white knight image.
     */
    public static Image whiteKnight;

    /**
     * white pawn image.
     */
    public static Image whitePawn;

    /**
     * white queen image.
     */
    public static Image whiteQueen;
    /**
     * white rook image.
     */
    public static Image whiteRook;

    /**
     * Initialize
     */
    public final static void initialize() {
        icon = new Image(path + "/Chess_Icon.png");

        blackBishop = new Image(path + "/black/bishop.png");
        blackRook = new Image(path + "/black/rook.png");
        blackKing = new Image(path + "/black/king.png");
        blackKnight = new Image(path + "/black/knight.png");
        blackPawn = new Image(path + "/black/pawn.png");
        blackQueen = new Image(path + "/black/queen.png");

        whiteBishop = new Image(path + "/white/bishop.png");
        whiteRook = new Image(path + "/white/rook.png");
        whiteKing = new Image(path + "/white/king.png");
        whiteKnight = new Image(path + "/white/knight.png");
        whitePawn = new Image(path + "/white/pawn.png");
        whiteQueen = new Image(path + "/white/queen.png");
    }

}
