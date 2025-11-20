package org.example.labyrinthgenerator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button generateButton;

    @FXML
    private Button saveButton;

    private Stage primaryStage;

    private int fieldHeight = 12;
    private int fieldWidth = 12;

    @FXML
    private void initialize() {

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void  handleGenerate() {
        clear();
        System.out.println("Generating");
        boolean[][] walls_horizontal = new boolean[fieldHeight-1][fieldWidth];
        boolean[][] walls_vertical = new boolean[fieldHeight][fieldWidth-1];

        //filling arrays with true
        for (boolean[] booleans : walls_horizontal) {
            Arrays.fill(booleans, true);
        }
        for (boolean[] booleans : walls_vertical) {
            Arrays.fill(booleans, true);
        }



        int startPos = (int) (Math.random() * fieldWidth);
        int endPos = (int) (Math.random() * fieldWidth);

        final int OFFSET = 10;

        PathGenerator pathGenerator = new PathGenerator(fieldHeight, fieldWidth, startPos, endPos);
        pathGenerator.generatePath();
        PathGenerator.Direction[] solvablePath = pathGenerator.getPath();

        MazeGenerator generator = new MazeGenerator(fieldHeight, fieldWidth, solvablePath, startPos, endPos);
        generator.generateMaze();

        walls_horizontal = generator.getHorizontal_walls();
        walls_vertical = generator.getVertical_walls();



        Rectangle startRect = new Rectangle();
        startRect.setX(startPos*50+OFFSET+10);
        startRect.setY((fieldHeight-1)*50+OFFSET+10);
        startRect.setWidth(30);
        startRect.setHeight(30);
        startRect.setFill(Color.GREEN);
        mainPane.getChildren().add(startRect);

        Rectangle endRect = new Rectangle();
        endRect.setX(endPos*50+OFFSET+10);
        endRect.setY(OFFSET+10);
        endRect.setWidth(30);
        endRect.setHeight(30);
        endRect.setFill(Color.BLACK);
        mainPane.getChildren().add(endRect);

        //drawing horizontal walls
        for(int y = 0; y < walls_horizontal.length; y++){
            for(int x = 0; x < walls_horizontal[y].length; x++){
                Line line = new Line();
                line.setStartX(x*50+OFFSET);
                line.setEndX((x+1)*50+OFFSET);
                line.setStartY((y+1)*50+OFFSET);
                line.setEndY((y+1)*50+OFFSET);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                if(walls_horizontal[y][x])mainPane.getChildren().add(line);
            }
        }

        //drawing vertical walls
        for(int y = 0; y < walls_vertical.length; y++){
            for(int x = 0; x < walls_vertical[y].length; x++){
                Line line = new Line();
                line.setStartX((x+1)*50+OFFSET);
                line.setEndX((x+1)*50+OFFSET);
                line.setStartY(y*50+OFFSET);
                line.setEndY((y+1)*50+OFFSET);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(2);
                if(walls_vertical[y][x])mainPane.getChildren().add(line);
            }
        }

        //draw horizontal edges
        for(int x = 0;  x < fieldWidth; x++){
            Line line = new Line();
            line.setStartX(x*50+OFFSET);
            line.setStartY(OFFSET);
            line.setEndX((x+1)*50+OFFSET);
            line.setEndY(OFFSET);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            if(x != endPos)mainPane.getChildren().add(line);

            Line line2 = new Line();
            line2.setStartX(x*50+ OFFSET);
            line2.setStartY(fieldHeight * 50 + OFFSET);
            line2.setEndX((x+1)*50 + OFFSET);
            line2.setEndY(fieldHeight * 50 + OFFSET);
            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(2);
            if(x != startPos)mainPane.getChildren().add(line2);
        }

        //draw vertical edges
        for(int y = 0;  y < fieldHeight; y++){
            Line line = new Line();
            line.setStartX(OFFSET);
            line.setStartY((y) * 50 + OFFSET);
            line.setEndX(OFFSET);
            line.setEndY((y+1) * 50 + OFFSET);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            mainPane.getChildren().add(line);

            Line line2 = new Line();
            line2.setStartX(fieldWidth*50 + OFFSET);
            line2.setStartY((y) * 50 + OFFSET);
            line2.setEndX(fieldWidth*50 + OFFSET);
            line2.setEndY((y+1) * 50 + OFFSET);
            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(2);
            mainPane.getChildren().add(line2);
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

        leftSideBar.getChildren().setAll(topSpacer, generateButton, saveButton, bottomSpacer);


        mainPane.setPrefWidth(LabyrinthApplication.DEFAULT_WINDOW_WIDTH);
        mainPane.setPrefHeight(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT);
        mainPane.getStyleClass().add("mainPane_bg");

        generateButton.setLayoutX(20);
        generateButton.setLayoutY(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT*0.25);

        saveButton.setLayoutX(20);
        generateButton.setLayoutY(LabyrinthApplication.DEFAULT_WINDOW_HEIGHT*0.75);
    }
}