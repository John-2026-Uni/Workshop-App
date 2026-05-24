package com.school.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchTo(String fxmlFile, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            SceneManager.class.getResource("/com/school/fxml/" + fxmlFile)
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
            Objects.requireNonNull(
                SceneManager.class.getResource("/com/school/css/styles.css")
            ).toExternalForm()
        );
        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
