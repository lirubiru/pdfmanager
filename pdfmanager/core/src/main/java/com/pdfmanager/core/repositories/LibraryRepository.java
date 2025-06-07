package com.pdfmanager.core.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pdfmanager.core.db.DatabaseInterface;
import com.pdfmanager.core.entities.Library;

public class LibraryRepository extends BaseRepository<Library> {

    public LibraryRepository(DatabaseInterface db) {
        super(db);
    }

    @Override
    public void create(Library lib) {
        String sql = "INSERT INTO " + getTableName() + " (name, path) VALUES (?, ?)";
        try (
            Connection conn = this.db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, lib.getName());
            stmt.setString(2, lib.getPath());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao criar biblioteca: " + e.getMessage());
        }
    }

    @Override
    public void updateById(int id, Library lib) {
        String sql = "UPDATE " + getTableName() + " SET name = ?, path = ? WHERE id = ?";
        try (
            Connection conn = this.db.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
        ) {

            stmt.setString(1, lib.getName());
            stmt.setString(2, lib.getPath());
            stmt.setInt(3, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar biblioteca: " + e.getMessage());
        }
    }

    @Override
    protected String getTableName() {
        return "library";
    }

    @Override
    protected Library mapResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String path = rs.getString("path");
        return new Library(id, name, path);
    }

}
