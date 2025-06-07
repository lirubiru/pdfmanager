package com.pdfmanager.core.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseInterface {
  public Connection getConnection();
  public void initialize() throws SQLException;
}

