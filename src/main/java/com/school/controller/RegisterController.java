package com.school.controller;

import com.school.dao.UserDAO;
import com.school.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Label successLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        successLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirm  = confirmPasswordField.getText().trim();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        if (username.length() < 3) {
            showError("Username must be at least 3 characters.");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            confirmPasswordField.clear();
            return;
        }

        new Thread(() -> {
            try {
                if (userDAO.usernameExists(username)) {
                    Platform.runLater(() -> showError("Username already taken."));
                    return;
                }
                boolean success = userDAO.register(username, password);
                Platform.runLater(() -> {
                    if (success) {
                        successLabel.setText("✓ Account created! You can now log in.");
                        successLabel.setVisible(true);
                        errorLabel.setVisible(false);
                        usernameField.clear();
                        passwordField.clear();
                        confirmPasswordField.clear();
                    } else {
                        showError("Registration failed. Try again.");
                    }
                });
            } catch (SQLException e) {
                Platform.runLater(() -> showError("Database error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleBackToLogin() {
        try {
            SceneManager.switchTo("login.fxml", "Login Database");
        } catch (IOException e) {
            showError("Could not go back to login.");
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
        successLabel.setVisible(false);
    }
}
