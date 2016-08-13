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

import javafx.scene.image.Image;

/**
 *
 * @author Tom Sandmann
 */
public final class ImageLoader {

    /**
     * Path resources.
     */
    static final String PATH = "images/chess_icons/png";

    static final String IMAGETYPE = ".png";

    /**
     * application icon.
     */
    public static final Image ICON = new Image("images/program_icon/chess_icon.png");

    /**
     * black bishop image.
     */
    protected static final Image BLACK_BISHOP = new Image(PATH + "/black/bishop" + IMAGETYPE);

    /**
     * black king image.
     */
    public static final Image BLACK_KING = new Image(PATH + "/black/king" + IMAGETYPE);

    /**
     * black knight image.
     */
    public static final Image BLACK_KNIGHT = new Image(PATH + "/black/knight" + IMAGETYPE);

    /**
     * black pawn image.
     */
    public static final Image BLACK_PAWN = new Image(PATH + "/black/pawn" + IMAGETYPE);

    /**
     * black queen image.
     */
    public static final Image BLACK_QUEEN = new Image(PATH + "/black/queen" + IMAGETYPE);
    /**
     * black rook image.
     */
    public static final Image BLACK_ROOK = new Image(PATH + "/black/rook" + IMAGETYPE);
    /**
     * black bishop image.
     */
    public static final Image WHITE_BISHOP = new Image(PATH + "/white/bishop" + IMAGETYPE);

    /**
     * white king image.
     */
    public static final Image WHITE_KING = new Image(PATH + "/white/king" + IMAGETYPE);

    /**
     * white knight image.
     */
    public static final Image WHITE_KNIGHT = new Image(PATH + "/white/knight" + IMAGETYPE);

    /**
     * white pawn image.
     */
    public static final Image WHITE_PAWN = new Image(PATH + "/white/pawn" + IMAGETYPE);

    /**
     * white queen image.
     */
    public static final Image WHITE_QUEEN = new Image(PATH + "/white/queen" + IMAGETYPE);
    /**
     * white rook image.
     */
    public static final Image WHITE_ROOK = new Image(PATH + "/white/rook" + IMAGETYPE);

}
