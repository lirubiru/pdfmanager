package com.pdfmanager.core.db;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:" +
        Paths.get(System.getProperty("user.home"),"pdfmanager.db").toString();
    private static Connection connection;

    public static void initialize() throws SQLException {
        connection = DriverManager.getConnection(URL);
        runMigrations();
    }

    public static  Connection getConnection() {
        return connection;
    }

    private static void runMigrations() {
        String[] migrations = {};

        try (Statement stmt = connection.createStatement()) {
           for (String sql : migrations) {
                stmt.execute(sql);
           } 
        } catch (SQLException e) {
            System.err.println("Erro ao executar migration:" + e.getMessage());
        }

    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
}
