package com.pdfmanager.cli;

import java.nio.file.Paths;
import java.sql.SQLException;

import com.pdfmanager.core.db.Database;
import com.pdfmanager.core.db.DatabaseInterface;
import com.pdfmanager.core.repositories.LibraryRepository;
import com.pdfmanager.core.services.LibraryService;

public class Main {
    public static void main(String[] args) {
        String DB_URL = "jdbc:sqlite:" + Paths.get(System.getProperty("user.home"),"pdfmanager.db").toString();
        try {
            DatabaseInterface db = new Database(DB_URL);
            db.initialize();
            Cli app = new Cli(new LibraryService(new LibraryRepository(db)));
            app.readArgs(args);
        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }

    }
}