package com.br.hermescomercial.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.br.hermescomercial.util.ConfigProperties;

public class TocarDB {
	
	private static String URL_POSTGRES = ConfigProperties.getProperty("URL_POSTGRES");
    private static String USER_POSTGRES = ConfigProperties.getProperty("USER_POSTGRES");
    private static String SENHA_POSTGRES = ConfigProperties.getProperty("PASSWORD_POSTGRES");
    
    private Connection conectaSQLite;
    private static String URL_SQLITE = ConfigProperties.getProperty("PATH_SQLITE_DB");
    
    private static String URL_MYSQL = ConfigProperties.getProperty("URL_MYSQL");
    private static String USER_MYSQL = ConfigProperties.getProperty("USERL_MYSQL");
    private static String SENHA_MYSQL = ConfigProperties.getProperty("PASSWORD_MYSQL");

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection getConnectionPostgres() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
            System.out.println("Conectado com sucesso Postgres server .");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return con;
    }
    
    
	public Connection getConnectionSQLite(){
		try {
			Class.forName("org.sqlite.JDBC");
			conectaSQLite =  DriverManager.getConnection(URL_SQLITE);
		} catch (SQLException e) {
			System.err.println("Problemas na hora de registrar driver");
			System.err.println("Saindo...");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("conexao feito com sucesso");
		return conectaSQLite;
	}
	
	

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection getConnectionMySQL() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
            System.out.println("Connected to the PostgreSQL server successfully.");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
	

}
