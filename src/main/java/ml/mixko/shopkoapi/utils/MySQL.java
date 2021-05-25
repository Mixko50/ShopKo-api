package ml.mixko.shopkoapi.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    public static final String URL = "jdbc:mysql://csproject.sit.kmutt.ac.th/db63130500233";
    public static final String USERNAME = "63130500233";
    public static final String PASSWORD = "abcd1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
