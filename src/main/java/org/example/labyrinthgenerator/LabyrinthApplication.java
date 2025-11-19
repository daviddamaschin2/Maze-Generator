package org.example.labyrinthgenerator;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LabyrinthApplication extends javafx.application.Application {

    public static final int DEFAULT_WINDOW_WIDTH = 800;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LabyrinthApplication.class.getResource("mainStage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
        primaryStageController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}