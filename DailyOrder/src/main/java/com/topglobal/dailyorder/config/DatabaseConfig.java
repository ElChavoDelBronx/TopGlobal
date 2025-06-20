package com.topglobal.dailyorder.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String PROPERTIES_FILE = "/db.properties";

    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        try {
            Properties props = new Properties();
            props.load(DatabaseConfig.class.getResourceAsStream(PROPERTIES_FILE));

            url = props.getProperty("db.url");
            user = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");

            Class.forName(driver);  // Carga el driver JDBC
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
