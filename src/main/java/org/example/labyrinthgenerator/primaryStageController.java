package org.example.labyrinthgenerator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;

public class primaryStageController {
    @FXML
    private Label welcomeText;


    @FXML
    private VBox leftSideBar;

    @FXML
    private Pane mainPane;

    @FXML
    private HBox mainBox;

    @FXML
    private Button generateButton;

    @FXML
    private TextField fieldWidthInput;

    @FXML
    private TextField fieldHeightInput;

    @FXML
    private Button saveButton;

    private Stage primaryStage;

    private int fieldHeight = 12;
    private int fieldWidth = 12;
    private int startPos;
    private int endPos;
    private boolean[][] walls_horizontal;
    private boolean[][] walls_vertical;
    private static final double OFFSET = 10.0;

    @FXML
    private void initialize() {
        //Listen to width/height changes (works during layout/resizing)
        mainPane.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            double w = newB.getWidth();
            double h = newB.getHeight();
            if (w > 0 && h > 0) {
                redraw(w, h);
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void  handleGenerate() {
        fieldWidth = Integer.parseInt(fieldWidthInput.getText());
        fieldHeight = Integer.parseInt(fieldHeightInput.getText());


        clear();
        System.out.println("Generating");
        walls_horizontal = new boolean[fieldHeight-1][fieldWidth];
        walls_vertical = new boolean[fieldHeight][fieldWidth-1];

        //filling arrays with true
        for (boolean[] booleans : walls_horizontal) {
            Arrays.fill(booleans, true);
        }
        for (boolean[] booleans : walls_vertical) {
            Arrays.fill(booleans, true);
        }



        startPos = (int) (Math.random() * fieldWidth);
        endPos = (int) (Math.random() * fieldWidth);

        PathGenerator pathGenerator = new PathGenerator(fieldHeight, fieldWidth, startPos, endPos);
        pathGenerator.generatePath();
        PathGenerator.Direction[] solvablePath = pathGenerator.getPath();

        MazeGenerator generator = new MazeGenerator(fieldHeight, fieldWidth, solvablePath, startPos, endPos);
        generator.generateMaze();

        walls_horizontal = generator.getHorizontal_walls();
        walls_vertical = generator.getVertical_walls();

        redraw(mainPane.getBoundsInParent().getWidth(), mainPane.getBoundsInParent().getHeight());
    }

    private void redraw(double paneW, double paneH) {
        clear();
        if (walls_horizontal == null || walls_vertical == null) return;

        double usableW = paneW - 2 * OFFSET;
        double usableH = paneH - 2 * OFFSET;
        if (usableW <= 0 || usableH <= 0) return;

        double cellW = usableW / fieldWidth;
        double cellH = usableH / fieldHeight;

        double leftX = OFFSET;
        double topY = OFFSET;
        double rightX = paneW - OFFSET;
        double bottomY = paneH - OFFSET;

        // Start cell (bottom entrance)
        Rectangle startRect = new Rectangle();
        startRect.setX(leftX + startPos * cellW + cellW * 0.2);
        startRect.setY(topY + (fieldHeight - 1) * cellH + cellH * 0.2);
        startRect.setWidth(cellW * 0.6);
        startRect.setHeight(cellH * 0.6);
        startRect.setFill(Color.WHITE);
        startRect.setStyle("-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.35), 12, 0.25, 2, 0)");
        mainPane.getChildren().add(startRect);

        // End cell (top exit)
        Rectangle endRect = new Rectangle();
        endRect.setX(leftX + endPos * cellW + cellW * 0.2);
        endRect.setY(topY + cellH * 0.2);
        endRect.setWidth(cellW * 0.6);
        endRect.setHeight(cellH * 0.6);
        endRect.setFill(Color.rgb(238, 70, 64));
        endRect.setStyle("-fx-effect: dropshadow(gaussian, rgba(238,70,64,0.35), 12, 0.25, 2, 0)");
        mainPane.getChildren().add(endRect);

        // Horizontal internal walls
        for (int y = 0; y < walls_horizontal.length; y++) {
            double yPos = topY + (y + 1) * cellH;
            for (int x = 0; x < walls_horizontal[y].length; x++) {
                if (!walls_horizontal[y][x]) continue;
                double xStart = leftX + x * cellW;
                double xEnd = leftX + (x + 1) * cellW;
                Line line = new Line(xStart, yPos, xEnd, yPos);
                line.getStyleClass().add("maze_wall");
                mainPane.getChildren().add(line);
            }
        }

        // Vertical internal walls
        for (int y = 0; y < walls_vertical.length; y++) {
            double yStart = topY + y * cellH;
            double yEnd = topY + (y + 1) * cellH;
            for (int x = 0; x < walls_vertical[y].length; x++) {
                if (!walls_vertical[y][x]) continue;
                double xPos = leftX + (x + 1) * cellW;
                Line line = new Line(xPos, yStart, xPos, yEnd);
                line.getStyleClass().add("maze_wall");
                mainPane.getChildren().add(line);
            }
        }

        // Top and bottom border (skip exit/entrance openings)
        for (int x = 0; x < fieldWidth; x++) {
            double xStart = leftX + x * cellW;
            double xEnd = leftX + (x + 1) * cellW;

            if (x != endPos) {
                Line topLine = new Line(xStart, topY, xEnd, topY);
                topLine.getStyleClass().add("maze_wall");
                mainPane.getChildren().add(topLine);
            }
            if (x != startPos) {
                Line bottomLine = new Line(xStart, bottomY, xEnd, bottomY);
                bottomLine.getStyleClass().add("maze_wall");
                mainPane.getChildren().add(bottomLine);
            }
        }

        // Left and right borders
        for (int y = 0; y < fieldHeight; y++) {
            double yStart = topY + y * cellH;
            double yEnd = topY + (y + 1) * cellH;

            Line leftLine = new Line(leftX, yStart, leftX, yEnd);
            leftLine.getStyleClass().add("maze_wall");
            mainPane.getChildren().add(leftLine);

            Line rightLine = new Line(rightX, yStart, rightX, yEnd);
            rightLine.getStyleClass().add("maze_wall");
            mainPane.getChildren().add(rightLine);
        }
    }

    private void clear(){
        mainPane.getChildren().clear();
    }

    @FXML private void handleSave(){
        System.out.println("Saving");
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;

        double sideBarWidth = LabyrinthApplication.DEFAULT_WINDOW_WIDTH*0.2;
        leftSideBar.setMaxWidth(sideBarWidth);
        leftSideBar.setPrefWidth(sideBarWidth);
        leftSideBar.setMinWidth(sideBarWidth);
        leftSideBar.setPrefHeight(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT);
        leftSideBar.getStyleClass().add("sideBar_bg");

        leftSideBar.setAlignment(Pos.CENTER);
        leftSideBar.setSpacing(10);

        Region topSpacer = new Region();
        Region bottomSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        mainBox.getStyleClass().add("hBox_bg");

        leftSideBar.getChildren().setAll(topSpacer, fieldWidthInput, fieldHeightInput, generateButton, saveButton, bottomSpacer);


        mainPane.setPrefWidth(LabyrinthApplication.DEFAULT_WINDOW_WIDTH);
        mainPane.setPrefHeight(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT);

        generateButton.setLayoutX(20);
        generateButton.setLayoutY(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT*0.25);

        saveButton.setLayoutX(20);
        generateButton.setLayoutY(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT*0.75);
    }
}