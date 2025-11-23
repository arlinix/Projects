package com.myshop.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionFactory {

    // Update these credentials to match your environment
    private static final String URL = System.getenv().getOrDefault("MYSHOP_JDBC_URL",
            "jdbc:mysql://localhost:3306/myshop?useSSL=false&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("MYSHOP_DB_USER", "myshop_admin");
    private static final String PASSWORD = System.getenv().getOrDefault("MYSHOP_DB_PASS", "123456");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load MySQL driver", e);
        }
    }

    private DbConnectionFactory() {}

    /**
     * New Connection (caller is responsible for closing it).
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
