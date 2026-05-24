package com.school.controller;

import com.school.dao.UserDAO;
import com.school.util.DatabaseConnection;
import com.school.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        checkConnection();
    }

    @FXML
    private void handleLogin() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        new Thread(() -> {
            try {
                boolean success = userDAO.login(user, pass);
                Platform.runLater(() -> {
                    if (success) {
                        try {
                            SceneManager.switchTo("dashboard.fxml", "Login Database — Dashboard");
                        } catch (IOException e) {
                            showError("Could not load dashboard.");
                        }
                    } else {
                        showError("Invalid username or password.");
                        passwordField.clear();
                    }
                });
            } catch (SQLException e) {
                Platform.runLater(() -> showError("Database error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleGoToRegister() {
        try {
            SceneManager.switchTo("register.fxml", "Login Database — Register");
        } catch (IOException e) {
            showError("Could not load register screen.");
        }
    }

    private void checkConnection() {
        statusLabel.setText("Connecting...");
        new Thread(() -> {
            try {
                DatabaseConnection.getInstance();
                Platform.runLater(() -> {
                    statusLabel.setText("● Connected");
                    statusLabel.setStyle("-fx-text-fill: #338833;");
                });
            } catch (SQLException e) {
                Platform.runLater(() -> {
                    statusLabel.setText("⚠ Not connected — check your .env");
                    statusLabel.setStyle("-fx-text-fill: #cc0000;");
                });
            }
        }).start();
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }
}
