package com.br.hermescomercial.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.br.hermescomercial.util.ConfigProperties;

public class ConnectionMySQL {

    //private final String url = "jdbc:mysql://localhost/hermescomercial";
    //private final String user = "root";
    //private final String password = "admin123";
    
    private final String url = ConfigProperties.getProperty("URL_MYSQL");
    private final String user = ConfigProperties.getProperty("USERL_MYSQL");
    private final String password = ConfigProperties.getProperty("PASSWORD_MYSQL");

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
}
