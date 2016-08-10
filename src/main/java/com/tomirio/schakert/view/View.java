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
import com.tomirio.schakert.chessboard.Position;
import com.tomirio.schakert.chesspieces.ChessPiece;
import com.tomirio.schakert.controller.MouseListener;
import com.tomirio.schakert.game.Game;
import com.tomirio.schakert.moves.Move;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

/**
 *
 * @author Tom Sandmann
 */
public final class View {

    /**
     * The borderpane.
     */
    public BorderPane borderPane;

    /**
     * The chessboard.
     */
    public ChessBoard chessBoard;

    /**
     * The gridpane.
     */
    public GridPane chessboardGrid;
    /**
     * Do move button.
     */
    public Button doMoveBtn;
    /**
     * The game
     */
    public Game game;

    /**
     * Get the current FEN.
     */
    public Button getFEN;

    /**
     * The imageloader
     */
    public ImageLoader imageLoader;

    /**
     * The labels
     */
    public LinkedList<Label> labels;
    public Button loadFEN;
    /**
     * The log.
     */
    public Log log;
    /**
     * the mainWindow.
     */
    public Stage mainWindow;
    /**
     * The mouse listener, will be set on mouse clicked to every visual tile.
     */
    public MouseListener mouseListener;
    /**
     * The stackpane.
     */
    public StackPane root;
    /**
     * Undo move button.
     */
    public Button undoMoveBtn;
    /**
     * The visualBoard contains all the visual tiles.
     */
    public VisualTile[][] visualBoard;

    /**
     *
     * @param primaryStage The primary stage.
     */
    public View(Stage primaryStage) {
        imageLoader = new ImageLoader();
        createMainWindow(primaryStage);
    }

    /**
     * Adds listeners to the width and height property of the visual
     * representation of the chessboard, which essentially is a GridPane .
     */
    private void addResizeHandlers() {
        chessboardGrid.widthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) -> {
            handleResize(chessboardGrid.getWidth(), chessboardGrid.getHeight());
        });
        chessboardGrid.heightProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) -> {
            handleResize(chessboardGrid.getWidth(), chessboardGrid.getHeight());
        });

    }

    private void createButtons() {
        // Set pref size
        doMoveBtn.setPrefSize(50, 30);
        undoMoveBtn.setPrefSize(50, 30);
        loadFEN.setPrefSize(50, 30);
        getFEN.setPrefSize(50, 30);

        doMoveBtn.setTooltip(new Tooltip("Make the next move."));
        undoMoveBtn.setTooltip(new Tooltip("Undo the last move."));
        loadFEN.setTooltip(new Tooltip("Load a FEN string."));
        getFEN.setTooltip(new Tooltip("Get FEN string of the current board."));

        // Create SVG images containg left and right arrows.
        SVGPath doSVG = new SVGPath();
        SVGPath undoSVG = new SVGPath();
        SVGPath loadSVG = new SVGPath();
        SVGPath getSVG = new SVGPath();
        /*
        https://skills421.wordpress.com/2014/08/05/svg-icons-in-javafx8/
         */
        undoSVG.setContent("M0.133,8.367 L6.073,13.713 C6.307,13.924 6.687,13.922 "
                + "6.922,13.711 L6.927,9.983 L14.951,9.983 C15.504,9.983 15.951,"
                + "9.544 15.951,9.001 L15.951,7.035 C15.951,6.492 15.504,6.053 "
                + "14.951,6.053 L6.931,6.053 L6.936,2.243 C6.705,2.034 "
                + "6.324,2.035 6.088,2.246 L0.134,7.603 C-0.099,7.816 -0.102,"
                + "8.156 0.133,8.367 L0.133,8.367 Z");

        doSVG.setContent("M16.818,7.646 L10.878,2.206 C10.644,1.992 10.264,1.993 "
                + "10.029,2.208 L10.024,6.001 L2,6.001 C1.447,6.001 1,6.448 1,7.001 "
                + "L1,9.001 C1,9.554 1.447,10.001 2,10.001 L10.019,10.001 "
                + "L10.013,13.878 C10.245,14.091 10.626,14.09 10.862,13.875 "
                + "L16.816,8.423 C17.049,8.206 17.052,7.859 16.818,7.646 L16.818,7.646 Z");
        loadSVG.setContent("M14,8.047 L14,12.047 L2,12.047 L2,8.047 L0,8.047 L0,15 L15.969,15 L15.969,8.047 L14,8.047 Z"
                + "M7.997,0 L5,3.963 L7.016,3.984 L7.016,8.969 L8.953,8.969 L8.953,3.984 L10.953,3.984 L7.997,0 Z");

        getSVG.setContent("M14.031,8.016 L14.031,12.016 L2,12.016 L2,8.016 L0,8.016 L0,15 L15.938,15 L15.938,8.016 L14.031,8.016 Z"
                + "M8.072,8.947 L10.982,5.071 L8.968,5.05 L8.968,0.065 L7.03,0.065 L7.03,5.05 L5.03,5.05 L8.072,8.947 Z");

        doSVG.setFill(Color.web("#434343"));
        undoSVG.setFill(Color.web("#434343"));
        loadSVG.setFill(Color.web("#434343"));
        getSVG.setFill(Color.web("#434343"));

        // Properly scale the SVG image.
        doSVG.scaleXProperty().bind(doMoveBtn.widthProperty().divide(30));
        doSVG.scaleYProperty().bind(doMoveBtn.heightProperty().divide(20));

        undoSVG.scaleXProperty().bind(undoMoveBtn.widthProperty().divide(30));
        undoSVG.scaleYProperty().bind(undoMoveBtn.heightProperty().divide(20));

        loadSVG.scaleXProperty().bind(loadFEN.widthProperty().divide(30));
        loadSVG.scaleYProperty().bind(loadFEN.heightProperty().divide(20));

        getSVG.scaleXProperty().bind(getFEN.widthProperty().divide(30));
        getSVG.scaleYProperty().bind(getFEN.heightProperty().divide(20));

        // Set button images.
        doMoveBtn.setGraphic(doSVG);
        undoMoveBtn.setGraphic(undoSVG);
        loadFEN.setGraphic(loadSVG);
        getFEN.setGraphic(getSVG);

        // Add mouse click listeners.
        doMoveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.doMove();
            }
        });

        undoMoveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                game.undoMove();
            }
        });

        loadFEN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // See http://code.makery.ch/blog/javafx-dialogs-official/
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Load custom FEN");
                alert.setHeaderText("Enter the FEN string in the textbox below.");

                Label label = new Label("FEN:");
                TextField textField = new TextField();
                textField.setPrefWidth(450);

                HBox content = new HBox();
                content.setSpacing(10);
                content.getChildren().addAll(label, textField);
                alert.getDialogPane().setExpandableContent(content);
                alert.getDialogPane().setExpanded(true);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    String FEN = textField.getText();
                    if (!"".equals(FEN)) {
                        game.loadFEN(FEN);
                    }
                } else {
                    // user chose CANCEL or closed the dialog
                }
            }
        });

        getFEN.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // See http://code.makery.ch/blog/javafx-dialogs-official/
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Get current FEN");
                alert.setHeaderText("In the textfield below you find the FEN string for the current board.");
                TextField textField = new TextField();
                textField.setText(chessBoard.getFEN());
                textField.setPrefWidth(450);
                alert.getDialogPane().setExpandableContent(textField);
                alert.getDialogPane().setExpanded(true);
                alert.showAndWait();
            }
        });

    }

    private void createMainWindow(Stage primaryStage) {
        // Initialize Imageloader
        doMoveBtn = new Button();
        undoMoveBtn = new Button();
        loadFEN = new Button();
        getFEN = new Button();

        mainWindow = primaryStage;
        visualBoard = new VisualTile[8][8];
        chessBoard = new ChessBoard();

        game = new Game(chessBoard, this);
        log = game.getLog();
        chessBoard = game.getBoard();

        mouseListener = new MouseListener(this, game);

        // Root will contain every visual aspect
        root = new StackPane();
        borderPane = new BorderPane();
        chessboardGrid = new GridPane();
        labels = new LinkedList<>();

        // Initialize
        createVisualTiles();
        addResizeHandlers();
        setLabels();

        // Create graphical structure
        BorderPane visualChessBoard = new BorderPane();
        chessboardGrid.setAlignment(Pos.CENTER);
        visualChessBoard.setCenter(chessboardGrid);
        visualChessBoard.setPadding(new Insets(10, 10, 10, 10));

        // Create the buttons with SVG images
        createButtons();

        // Create horizontal box with buttons
        HBox buttonBar = new HBox();
        buttonBar.getChildren().addAll(getFEN, undoMoveBtn, doMoveBtn, loadFEN);
        buttonBar.setAlignment(Pos.TOP_CENTER);
        visualChessBoard.setBottom(buttonBar);
        visualChessBoard.autosize();

        borderPane.setCenter(visualChessBoard);

        // Make log scrollable
        ScrollPane scrollableLog = new ScrollPane();
        scrollableLog.setContent(log);
        scrollableLog.heightProperty().add(chessboardGrid.getHeight());
        scrollableLog.autosize();
        scrollableLog.setFitToWidth(true);
        scrollableLog.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        // Make sure it automatically scrolls down.
        scrollableLog.vvalueProperty().bind(log.heightProperty());

        borderPane.setRight(scrollableLog);
        root.getChildren().add(borderPane);
        Scene mainWindowScene = new Scene(root);

        mainWindow.getIcons().add(ImageLoader.ICON);
        mainWindow.setTitle("Schakert");
        mainWindow.centerOnScreen();
        mainWindow.setScene(mainWindowScene);
        mainWindow.sizeToScene();

        // Currently resizing is disabled.
        mainWindow.setResizable(false);
        mainWindow.show();
        mainWindow.setMinWidth(primaryStage.getWidth());
        mainWindow.setMinHeight(primaryStage.getHeight());
    }

    /**
     * Create the visual tiles. Also add the mouse listener to the visual tile.
     */
    private void createVisualTiles() {
        for (int row = 0; row < ChessBoard.ROWS; row++) {
            for (int column = 0; column < ChessBoard.COLS; column++) {
                VisualTile t;
                if (chessBoard.isOccupiedPosition(row, column)) {
                    ChessPiece p = chessBoard.getPiece(row, column);
                    t = new VisualTile(row, column, p);
                } else {
                    t = new VisualTile(row, column);
                }
                t.setInitialTileImageAndChessPiece();
                t.setOnMouseClicked(mouseListener);
                visualBoard[row][column] = t;
                chessboardGrid.add(t, column + 1, row + 1);
            }
        }
    }

    public void disableMoveButtons() {
        doMoveBtn.setDisable(true);
        undoMoveBtn.setDisable(true);
    }

    /**
     * Draws the current board on the screen.
     */
    public void drawBoard() {
        removeChessboard();
        double currentSize = visualBoard[0][0].getPrefWidth();
        visualBoard = new VisualTile[ChessBoard.ROWS][ChessBoard.COLS];
        for (int row = 0; row < ChessBoard.ROWS; row++) {
            for (int column = 0; column < ChessBoard.COLS; column++) {
                VisualTile t = new VisualTile(row, column,
                        chessBoard.getPiece(row, column), currentSize, currentSize);
                t.updateTileImage(currentSize);
                t.setOnMouseClicked(mouseListener);
                visualBoard[row][column] = t;
                chessboardGrid.add(t, column + 1, row + 1);
            }
        }
        updateLabelSize(currentSize);
    }

    public void enableMoveButtons() {
        doMoveBtn.setDisable(false);
        undoMoveBtn.setDisable(false);
    }

    /**
     * Handles resize of the scene. It makes sure that the chessboard structure
     * maintains a square ratio.
     *
     * @param gridWidth The current width of the gridpane
     * @param gridHeight The current height of the gridpane
     */
    private void handleResize(double gridWidth, double gridHeight) {
        double gridMinWidthHeight = Math.min(gridWidth, gridHeight);
        double newSquareSize = Math.floor(gridMinWidthHeight / 9.0);
        if (newSquareSize < visualBoard[0][0].getMinWidth()) {
            // Debugging purposes.
        } else {
            for (VisualTile[] columns : visualBoard) {
                for (VisualTile visualTile : columns) {
                    visualTile.setPrefSize(newSquareSize, newSquareSize);
                }
            }
            labels.stream().forEach((l) -> {
                l.setPrefSize(newSquareSize, newSquareSize);
            });
        }
    }

    /**
     * Removes the chessboard structure from the gridpane. This must be done
     * when a new chessboard structure will be added to the gridpane.
     */
    public void removeChessboard() {
        for (VisualTile[] columns : visualBoard) {
            for (VisualTile visualTile : columns) {
                chessboardGrid.getChildren().remove(visualTile);
            }
        }
    }

    /**
     *
     * @param possibleMoves The possible moves for this chess piece.
     */
    public void removeTilesAsMoves(ArrayList<Move> possibleMoves) {
        possibleMoves.stream().forEach((move) -> {
            Position newPos = move.getNewPos();
            visualBoard[newPos.getRow()][newPos.getColumn()].removeAsPossibleMove();
        });
    }

    /**
     * Set the labels in the GUI. This is called when the program is first
     * started.
     */
    private void setLabels() {

        Label padding = new Label("");
        padding.setContentDisplay(ContentDisplay.TEXT_ONLY);
        padding.setStyle("-fx-background-color: #FFFFFF; "
                // top right bottom left
                + "-fx-border-color: transparent transparent black black;");
        padding.setTextFill(Paint.valueOf("white"));
        padding.setPrefSize(VisualTile.WIDTH / 2, VisualTile.HEIGHT / 2);
        padding.setMinSize(VisualTile.WIDTH / 2, VisualTile.HEIGHT / 2);
        labels.add(padding);
        chessboardGrid.add(padding, 0, 9);

        // Adding the file labels
        for (int column = 0; column < ChessBoard.COLS; column++) {
            char c = Character.toChars(97 + column)[0];
            Label file = new Label(String.valueOf(c));
            file.setContentDisplay(ContentDisplay.TEXT_ONLY);
            file.setAlignment(Pos.CENTER);
            file.setPrefSize(VisualTile.WIDTH, VisualTile.HEIGHT / 2);
            file.setMinSize(VisualTile.WIDTH, VisualTile.HEIGHT / 2);
            if (column == 7) {
                file.setStyle("-fx-background-color: #FFFFFF;"
                        // top right bottom left
                        + "-fx-border-color: transparent black black transparent;");
            } else {
                file.setStyle("-fx-background-color: #FFFFFF;"
                        // top right bottom left
                        + "-fx-border-color: transparent transparent black transparent;");
            }

            file.setTextFill(Paint.valueOf("black"));
            labels.add(file);
            chessboardGrid.add(file, column + 1, 9);
        }

        // Adding the rank labels
        for (int row = 0; row < ChessBoard.ROWS; row++) {
            Label rank = new Label(Integer.toString(8 - row));
            rank.setContentDisplay(ContentDisplay.TEXT_ONLY);
            rank.setAlignment(Pos.CENTER);
            rank.setPrefSize(VisualTile.WIDTH / 2, VisualTile.HEIGHT);
            rank.setMinSize(VisualTile.WIDTH / 2, VisualTile.HEIGHT);
            if (row == 0) {
                rank.setStyle("-fx-background-color: #FFFFFF;"
                        // top right bottom left
                        + "-fx-border-color: black transparent transparent black;");
            } else {
                rank.setStyle("-fx-background-color: #FFFFFF;"
                        // top right bottom left
                        + "-fx-border-color: transparent transparent transparent black;");
            }
            rank.setTextFill(Paint.valueOf("black"));
            labels.add(rank);
            chessboardGrid.add(rank, 0, row + 1);
        }
    }

    /**
     *
     * @param possibleMoves The possible moves for this chess piece.
     */
    public void showTilesAsMoves(ArrayList<Move> possibleMoves) {
        possibleMoves.stream().forEach((move) -> {
            Position newPos = move.getNewPos();
            visualBoard[newPos.getRow()][newPos.getColumn()].showAsPossibleMove();
        });
    }

    /**
     * Update the view.
     *
     * @param chessBoard The chess board.
     */
    public void update(ChessBoard chessBoard) {
        drawBoard();
    }

    /**
     * Updates all the labels to the new size.
     *
     * @param newSize The new size of the labels.
     */
    private void updateLabelSize(double newSize) {
        labels.stream().forEach((l) -> {
            l.setPrefSize(newSize, newSize);
        });
    }

}
