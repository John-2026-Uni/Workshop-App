package com.school;

import com.school.util.DatabaseConnection;
import com.school.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setPrimaryStage(stage);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        SceneManager.switchTo("login.fxml", "Login Database");
    }

    @Override
    public void stop() throws Exception {
        DatabaseConnection.getInstance().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
