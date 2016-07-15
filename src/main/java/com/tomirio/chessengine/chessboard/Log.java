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
package com.tomirio.chessengine.chessboard;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

/**
 *
 * @author Tom Sandmann
 */
public class Log extends ScrollPane {

    /**
     * The list of the log, contains all of the text
     */
    private static TextArea list;

    private static int counter;

    /**
     * Constructor of log
     */
    public Log() {
        super();
        counter = 1;
        setStyle("-fx-background: transparent;");
        list = new TextArea();
        list.setStyle("-fx-background-color: rgb(80,80,80);");
        list.setEditable(false);
        list.setWrapText(true);
        setContent(list);
        this.setFitToWidth(true);
        this.setFitToHeight(true);
        this.setPrefWidth(250);
        list.insertText(list.getLength(), ("# " + "Game Information #" + "\n"));
    }

    /**
     *
     * @param str The string that must be written to the log
     */
    public static void write(String str) {
        list.insertText(list.getLength(), (counter++ + "\t" + str + "\n"));
    }

}
