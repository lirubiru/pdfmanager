package com.pdfmanager.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database implements DatabaseInterface {
    private final String URL;
    private Connection connection;

    public Database(String url) {
        this.URL = url;
    }

    public void initialize() throws SQLException {
        connection = DriverManager.getConnection(URL);
        runMigrations();
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    private void runMigrations() {
        String[] migrations = {
            "CREATE TABLE IF NOT EXISTS library (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "path TEXT NOT NULL" +
            ");",

            "CREATE TABLE IF NOT EXISTS book (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "dirname TEXT NOT NULL," +
            "library_id INTEGER NOT NULL," +
            "author TEXT NOT NULL," +
            "title TEXT NOT NULL," +
            "subtitle TEXT NOT NULL," +
            "genre TEXT NOT NULL," +
            "editor TEXT," +
            "page_size integer," +
            "publication_year integer," +
            "FOREIGN KEY (library_id) REFERENCES library(id)" +
            ");",

            "CREATE TABLE IF NOT EXISTS note (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "dirname TEXT NOT NULL," +
            "library_id INTEGER NOT NULL," +
            "author TEXT NOT NULL," +
            "title TEXT NOT NULL," +
            "subtitle TEXT NOT NULL," +
            "discipline TEXT NOT NULL," +
            "institution TEXT," +
            "page_size INTEGER," +
            "FOREIGN KEY (library_id) REFERENCES library(id)" +
           ");",

            "CREATE TABLE IF NOT EXISTS slide (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "dirname TEXT NOT NULL," +
            "library_id INTEGER NOT NULL," +
            "author TEXT NOT NULL," +
            "title TEXT NOT NULL," +
            "discipline TEXT NOT NULL," +
            "institution TEXT," +
            "FOREIGN KEY (library_id) REFERENCES library(id)" +
            ");"
        };

        try (Statement stmt = connection.createStatement()) {
           for (String sql : migrations) {
                stmt.execute(sql);
           }
        } catch (SQLException e) {
            System.err.println("Erro ao executar migration:" + e.getMessage());
        }

    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

}
