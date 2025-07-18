package com.topglobal.dailyorder.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL      = "jdbc:oracle:thin:@enzf2jb6hhpupolo_low";
    private static final String USER     = "ADMIN";
    private static final String PASSWORD = "!@Victor1993";

    // Obtiene una conexión nueva
    public static Connection getConnection() throws SQLException {
        //Nota: Modificar la ruta del wallet según tu sistema de archivos
        System.setProperty("oracle.net.tns_admin", "C:\\Users\\hdami\\Downloads\\Wallet_ENZF2JB6HHPUPOLO");
        //Obtiene la conexión usando alias, usuario y contraseña
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
