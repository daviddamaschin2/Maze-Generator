module org.example.labyrinthgenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.labyrinthgenerator to javafx.fxml;
    exports org.example.labyrinthgenerator;
}