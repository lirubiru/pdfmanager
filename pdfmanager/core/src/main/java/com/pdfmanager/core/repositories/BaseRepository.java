package com.pdfmanager.core.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.pdfmanager.core.db.DatabaseInterface;


public abstract class BaseRepository<T> {
    DatabaseInterface db;

    public BaseRepository(DatabaseInterface db) {
        this.db = db;
    }

    protected Connection getConnection() throws SQLException {
        return this.db.getConnection();
    }

    protected abstract String getTableName();

    protected abstract T mapResultSet(ResultSet rs) throws SQLException;

    public List<T> getAll() {
        List<T> items = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (
            Connection conn = this.getConnection();
            Statement stmt = conn.createStatement();
        ){
            ResultSet result = stmt.executeQuery(sql);
            while(result.next()) {
                items.add(mapResultSet(result));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar registros: " + e.getMessage());
        }

        return items;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = this.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();

            if (affected > 0) {
                System.out.println("Registro deletado com sucesso.");
            } else {
                System.out.println("Registro não encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar: " + e.getMessage());
        }
    }

    public abstract void create(T entity);

    public abstract void updateById(int id, T entity);
}
