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

        PathGenerator generator = new PathGenerator(9, 9, startPos, endPos);
        generator.generatePath();
        PathGenerator.Direction[] solvablePath = generator.getPath();

        if(solvablePath == null){
            System.out.println("Null Path");
        }
        else{
            int x = startPos;
            int y = 8;
            for(int i = 0; i < solvablePath.length; i++){
                switch(solvablePath[i]){
                    case NORTH -> {
                        y--;
                    }
                    case SOUTH -> {
                        y++;
                    }
                    case WEST -> {
                        x--;
                    }
                    case EAST -> {
                        x++;
                    }
                }
                Rectangle rect = new Rectangle();
                rect.setFill(Color.RED);
                rect.setX(x*50+OFFSET+10);
                rect.setY(y*50+OFFSET+10);
                rect.setWidth(30);
                rect.setHeight(30);

                mainPane.getChildren().add(rect);
            }
        }

        Rectangle startRect = new Rectangle();
        startRect.setX(startPos*50+OFFSET+10);
        startRect.setY(8*50+OFFSET+10);
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

        for(int x = 0;  x < 9; x++){
            Line line = new Line();
            line.setStartX(x*50+OFFSET);
            line.setStartY(OFFSET);
            line.setEndX((x+1)*50+OFFSET);
            line.setEndY(OFFSET);
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(2);
            mainPane.getChildren().add(line);

            Line line2 = new Line();
            line2.setStartX(x*50+ OFFSET);
            line2.setStartY(9 * 50 + OFFSET);
            line2.setEndX((x+1)*50 + OFFSET);
            line2.setEndY(9 * 50 + OFFSET);
            line2.setStroke(Color.BLACK);
            line2.setStrokeWidth(2);
            mainPane.getChildren().add(line2);
        }

        for(int y = 0;  y < 9; y++){
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