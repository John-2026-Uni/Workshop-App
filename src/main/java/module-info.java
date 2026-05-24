module com.school {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires org.postgresql.jdbc;

    opens com.school to javafx.fxml;
    opens com.school.controller to javafx.fxml;
    opens com.school.model to javafx.base;

    exports com.school;
    exports com.school.controller;
    exports com.school.model;
}
