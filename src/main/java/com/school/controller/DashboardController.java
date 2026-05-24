package com.school.controller;

import com.school.dao.StudentDAO;
import com.school.model.Student;
import com.school.util.SceneManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DashboardController {

    @FXML private TableView<Student>            studentTable;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String>  colFirstName;
    @FXML private TableColumn<Student, String>  colLastName;
    @FXML private TableColumn<Student, String>  colEmail;
    @FXML private TableColumn<Student, String>  colGrade;

    @FXML private TextField tfFirstName;
    @FXML private TextField tfLastName;
    @FXML private TextField tfEmail;
    @FXML private TextField tfGrade;
    @FXML private TextField searchField;

    @FXML private Label totalLabel;
    @FXML private Label statusLabel;

    private final StudentDAO dao = new StudentDAO();
    private final ObservableList<Student> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        studentTable.setItems(list);
        loadStudents();
    }

    private void loadStudents() {
        new Thread(() -> {
            try {
                List<Student> students = dao.getAllStudents();
                int total = dao.count();
                Platform.runLater(() -> {
                    list.setAll(students);
                    totalLabel.setText("Total: " + total);
                    statusLabel.setText("Loaded " + students.size() + " students.");
                });
            } catch (SQLException e) {
                Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) { loadStudents(); return; }
        new Thread(() -> {
            try {
                List<Student> results = dao.search(keyword);
                Platform.runLater(() -> list.setAll(results));
            } catch (SQLException e) {
                Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleAdd() {
        String fn = tfFirstName.getText().trim();
        String ln = tfLastName.getText().trim();
        String em = tfEmail.getText().trim();
        String gr = tfGrade.getText().trim();

        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty()) {
            statusLabel.setText("⚠ First name, last name, and email are required.");
            return;
        }

        new Thread(() -> {
            try {
                dao.add(new Student(0, fn, ln, em, gr));
                Platform.runLater(() -> {
                    tfFirstName.clear(); tfLastName.clear();
                    tfEmail.clear(); tfGrade.clear();
                    statusLabel.setText("✓ Student added.");
                    loadStudents();
                });
            } catch (SQLException e) {
                Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleDelete() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) { statusLabel.setText("⚠ Select a student first."); return; }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete " + selected.getFullName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                new Thread(() -> {
                    try {
                        dao.delete(selected.getId());
                        Platform.runLater(() -> { loadStudents(); statusLabel.setText("✓ Deleted."); });
                    } catch (SQLException e) {
                        Platform.runLater(() -> statusLabel.setText("Error: " + e.getMessage()));
                    }
                }).start();
            }
        });
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadStudents();
    }

    @FXML
    private void handleLogout() {
        try {
            SceneManager.switchTo("login.fxml", "Login Database");
        } catch (IOException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
