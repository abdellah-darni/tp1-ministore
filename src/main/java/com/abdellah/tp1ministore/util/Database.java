package com.abdellah.tp1ministore.util;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:/Users/mac/Documents/CI-GI/S3/javaee/tp-projects/database/tp1-ministore.db";
    private static final SQLiteDataSource dataSource;

    static {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl(URL);
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("catalina.base"));

        initDatabase();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static void initDatabase(){
        String createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id  INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL,
                    password_hash TEXT NOT NULL,
                    created_at TEXT DEFAULT (datetime('now','localtime'))
                );
                """;

        String createProductTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id  INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL NOT NULL,
                    created_at TEXT DEFAULT (datetime('now','localtime'))
                );
                """;
        try  (Connection connection = dataSource.getConnection();
              Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createProductTable);

            System.out.println("Database initialization complete successfully");
        } catch (SQLException e) {
            System.out.println("Database initialization failed: " + e.getMessage());
        }
    }

}
