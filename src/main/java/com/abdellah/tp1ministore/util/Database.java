package com.abdellah.tp1ministore.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static void initDataSource() {
        String dbUrl = System.getenv("MYSQL_URL");
        String dbUser = System.getenv("MYSQL_USER");
        String dbPass = System.getenv("MYSQL_PASSWORD");

        try {
            System.out.println("--- Connecting to Database ---");
            System.out.println("Target: " + dbUrl);

            HikariConfig config = new HikariConfig();

            Class.forName("com.mysql.cj.jdbc.Driver");

            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPass);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);

            dataSource = new HikariDataSource(config);

            try (Connection conn = dataSource.getConnection()) {
                System.out.println("--- SUCCESS: Connected to MySQL! ---");
            }

        } catch (Exception e) {
            System.err.println("!!! FATAL: Could not connect to MySQL Database !!!");
            System.err.println("Error: " + e.getMessage());
            throw new RuntimeException("Database Connection Failed", e);
        }

        initDatabase();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static void initDatabase() {
        String createUserTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                username VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                password_hash VARCHAR(255) NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """;

        String createProductTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                description TEXT,
                price DOUBLE NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            );
            """;

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createProductTable);

            System.out.println("--- MySQL Tables Verified/Created ---");

        } catch (SQLException e) {
            System.err.println("!!! Database Table Initialization Failed !!!");
            e.printStackTrace();
        }
    }
}