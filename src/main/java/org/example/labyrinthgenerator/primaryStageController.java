package org.example.labyrinthgenerator;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
        boolean[][] walls_horizontal = new boolean[8][9];
        boolean[][] walls_vertical = new boolean[9][8];

        int startPos = (int) (Math.random() * 9);
        int endPos = (int) (Math.random() * 9);

        final int OFFSET = 10;

        for (int i = 0; i < walls_horizontal.length; i++) {
            for (int j = 0; j < walls_horizontal[i].length; j++) {
                walls_horizontal[i][j] = Math.random() < 0.5;
            }
        }

        for (int i = 0; i < walls_vertical.length; i++) {
            for (int j = 0; j < walls_vertical[i].length; j++) {
                walls_vertical[i][j] = Math.random() < 0.5;
            }
        }

        PathGenerator generator = new PathGenerator(9, 9, startPos, endPos);
        generator.generateNewPath();
        PathGenerator.Direction[] path = generator.getGeneratedPath();

        int x = startPos;
        int y = 8;

        // draw start pos
        Rectangle startRect = new Rectangle();
        startRect.setX(x * 50 + OFFSET + 10);
        startRect.setY(y * 50 + OFFSET + 10);
        startRect.setWidth(30);
        startRect.setHeight(30);
        startRect.setFill(Color.GREEN);
        mainPane.getChildren().add(startRect);

        //draw end pos
        Rectangle endRect = new Rectangle();
        endRect.setX(endPos * 50 + OFFSET + 10);
        endRect.setY(OFFSET + 10);
        endRect.setWidth(30);
        endRect.setHeight(30);
        endRect.setFill(Color.BLUE);
        mainPane.getChildren().add(endRect);

        // Folge dem Pfad
        for(PathGenerator.Direction dir : path){
            switch (dir){
                case SOUTH -> {
                    if(y < 7){
                        walls_horizontal[y][x] = false;
                        y++;
                    }
                }
                case NORTH -> {
                    if(y > 0){
                        walls_horizontal[y-1][x] = false;
                        y--;
                    }
                }
                case EAST -> {
                    if(x < 7){
                        walls_vertical[y][x] = false;
                        x++;
                    }
                }
                case WEST -> {
                    if(x > 1){
                        walls_vertical[y][x-1] = false;
                        x--;
                    }
                }
            }

            // Zeichne Feld nach Bewegung
            Rectangle rect = new Rectangle();
            rect.setX(x * 50 + OFFSET + 10);
            rect.setY(y * 50 + OFFSET + 10);
            rect.setWidth(30);
            rect.setHeight(30);
            rect.setFill(Color.RED);
            mainPane.getChildren().add(rect);
        }

        for (y = 0; y < 8; y++) {
            for (x = 0; x < 8; x++) {
                if (walls_horizontal[y][x]) {
                    Line line = new Line();
                    line.setStartX((x+1) * 50 + OFFSET);
                    line.setStartY((y+1) * 50 + OFFSET);
                    line.setEndX((x + 2) * 50 + OFFSET);
                    line.setEndY((y+1) * 50 + OFFSET);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(1);
                    mainPane.getChildren().add(line);
                }
                if (walls_vertical[y][x]) {
                    Line line = new Line();
                    line.setStartX((x+1) * 50+OFFSET);
                    line.setStartY((y+1) * 50+OFFSET);
                    line.setEndX((x+1) * 50+OFFSET);
                    line.setEndY((y + 2) * 50+OFFSET);
                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(1);
                    mainPane.getChildren().add(line);
                }
            }
        }

        for(y = 0;  y < 9; y++){
            Line line = new Line();
            line.setStartX(OFFSET);
            line.setStartY((y) * 50 + OFFSET);
            line.setEndX(OFFSET);
            line.setEndY((y+1) * 50 + OFFSET);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            mainPane.getChildren().add(line);

            Line line2 = new Line();
            line2.setStartX(9*50 + OFFSET);
            line2.setStartY((y) * 50 + OFFSET);
            line2.setEndX(9*50 + OFFSET);
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