package ml.mixko.shopkoapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    public static final String URL = "jdbc:mysql://mysql.cslab.bsthun.com:1205/shopko";
    public static final String USERNAME = "mixko";
    public static final String PASSWORD = "0820608908";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
