package com.pdfmanager.core.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseInterface {
  public Connection getConnection() throws SQLException;
  public void initialize() throws SQLException;
}

