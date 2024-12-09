package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// mysql
public class Database {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanly_nhatu", "your_username", "your_password");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
