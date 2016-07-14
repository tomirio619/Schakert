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
     * black castle image.
     */
    public static Image blackCastle;

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
     * black bishop image.
     */
    public static Image whiteBishop;

    /**
     * white castle image.
     */
    public static Image whiteCastle;

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
     * application icon.
     */
    public static Image icon;

    /**
     * Path resources.
     */
    static String path = "PNG";

    /**
     * Initialize
     */
    public final static void initialize() {
        icon = new Image("Chess_Icon.png");

        blackBishop = new Image(path + "/Black/bishop.png");
        blackCastle = new Image(path + "/Black/castle.png");
        blackKing = new Image(path + "/Black/king.png");
        blackKnight = new Image(path + "/Black/knight.png");
        blackPawn = new Image(path + "/Black/pawn.png");
        blackQueen = new Image(path + "/Black/queen.png");

        whiteBishop = new Image(path + "/White/bishop.png");
        whiteCastle = new Image(path + "/White/castle.png");
        whiteKing = new Image(path + "/White/king.png");
        whiteKnight = new Image(path + "/White/knight.png");
        whitePawn = new Image(path + "/White/pawn.png");
        whiteQueen = new Image(path + "/White/queen.png");
    }

}
