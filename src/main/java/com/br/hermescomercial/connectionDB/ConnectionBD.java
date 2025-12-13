package com.br.hermescomercial.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.br.hermescomercial.util.ConfigProperties;

public class ConnectionBD {
	
	private static String URL_POSTGRES = ConfigProperties.getProperty("URL_POSTGRES");
    private static String USER_POSTGRES = ConfigProperties.getProperty("USER_POSTGRES");
    private static String SENHA_POSTGRES = ConfigProperties.getProperty("PASSWORD_POSTGRES");
    
    private Connection conectaSQLite;
    private static String URL_SQLITE = ConfigProperties.getProperty("PATH_SQLITE_DB");
    
    private static String URL_MYSQL = ConfigProperties.getProperty("URL_MYSQL");
    private static String USER_MYSQL = ConfigProperties.getProperty("USERL_MYSQL");
    private static String SENHA_MYSQL = ConfigProperties.getProperty("PASSWORD_MYSQL");


    private String nomeBanco;

    public ConnectionBD(String nomeBanco){
        this.nomeBanco = nomeBanco;
    }

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    /*public Connection getConnectionPostgres() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
            System.out.println("Conectado com sucesso Postgres server .");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return con;
    }*/
    
    
	/*public Connection getConnectionSQLite(){
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
	}*/
	
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    /*public Connection getConnectionMySQL() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
            System.out.println("Connected to the PostgreSQL server successfully.");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }*/

    public Connection getConnection(){
        try {
            Connection con = null;
            if("Postgres".equals(nomeBanco)){
                con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
                System.out.println("Conectado com sucesso Postgres server .");
                return con;
            } else if ("SQLite".equals(nomeBanco)) {
                Class.forName("org.sqlite.JDBC");
                conectaSQLite =  DriverManager.getConnection(URL_SQLITE);
                System.out.println("Conectado com sucesso SQLite server.");
                return conectaSQLite;
            }else  if ("MySQL".equals(nomeBanco)){
                con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
                System.out.println("CConectado com sucesso MySQL server.");
                return con;
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Problemas na hora de registrar driver");
            System.err.println("Saindo...");
            System.exit(1);
        }
        return null;
    }
	

}
