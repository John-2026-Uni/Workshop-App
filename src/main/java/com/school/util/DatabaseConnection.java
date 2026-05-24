package com.school.util;

import com.school.config.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        String host     = EnvConfig.get("DB_HOST");
        String port     = EnvConfig.get("DB_PORT");
        String dbName   = EnvConfig.get("DB_NAME");
        String user     = EnvConfig.get("DB_USER");
        String password = EnvConfig.get("DB_PASSWORD");

        // Supabase requires SSL
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName + "?sslmode=require";

        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("[DB] Connected to Supabase!");
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
