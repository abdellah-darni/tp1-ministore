package com.abdellah.tp1ministore.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static void initDataSource() {
        String dbUrl = System.getenv("MYSQL_URL");
        String dbUser = System.getenv("MYSQL_USER");
        String dbPass = System.getenv("MYSQL_PASSWORD");

        try {
            logger.info("--- Initializing Data Source ---");
            logger.debug("Target URL (Spy): {}", dbUrl);
            logger.debug("User: {}", dbUser);

            HikariConfig config = new HikariConfig();

            Class.forName("com.p6spy.engine.spy.P6SpyDriver");

            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPass);
            config.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);

            dataSource = new HikariDataSource(config);

            try (Connection conn = dataSource.getConnection()) {
                logger.info("--- SUCCESS: Connected to MySQL via P6Spy! ---");
            }

        } catch (Exception e) {
            logger.error("FATAL: Could not connect to MySQL Database", e);
            throw new RuntimeException("Database Connection Failed", e);
        }

        initDatabase();
    }

    public static Connection getConnection() throws SQLException {
        logger.trace("Connection requested from pool");
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

            logger.debug("Checking/Creating tables...");
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createProductTable);
            logger.info("--- MySQL Tables Verified/Created ---");

        } catch (SQLException e) {
            logger.error("Database Table Initialization Failed", e);
        }
    }
}