package com.br.hermescomercial.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.br.hermescomercial.util.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionBD {
	
	private static String URL_POSTGRES = ConfigProperties.getProperty("URL_POSTGRES");
    private static String USER_POSTGRES = ConfigProperties.getProperty("USER_POSTGRES");
    private static String SENHA_POSTGRES = ConfigProperties.getProperty("PASSWORD_POSTGRES");
    
    private Connection conectaSQLite;
    private static String URL_SQLITE = ConfigProperties.getProperty("PATH_SQLITE_DB");
    
    private static String URL_MYSQL = ConfigProperties.getProperty("URL_MYSQL");
    private static String USER_MYSQL = ConfigProperties.getProperty("USERL_MYSQL");
    private static String SENHA_MYSQL = ConfigProperties.getProperty("PASSWORD_MYSQL");

    private static final Logger logger = LogManager.getLogger(ConnectionBD.class);

    public Connection getConnection(String nomeBanco){
        try {
            Connection con = null;
            if("Postgres".equals(nomeBanco)){
                con = DriverManager.getConnection(URL_POSTGRES, USER_POSTGRES, SENHA_POSTGRES);
                logger.info("Conectado com sucesso Postgres server .");
                return con;
            } else if ("SQLite".equals(nomeBanco)) {
                Class.forName("org.sqlite.JDBC");
                conectaSQLite =  DriverManager.getConnection(URL_SQLITE);
                logger.info("Conectado com sucesso SQLite server.");
                return conectaSQLite;
            }else  if ("MySQL".equals(nomeBanco)){
                con = DriverManager.getConnection(URL_MYSQL, USER_MYSQL, SENHA_MYSQL);
                logger.info("Conectado com sucesso MySQL server.");
                return con;
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Erro na conexao com banco", e);
            logger.info("Saindo...");
            System.exit(1);
        }
        return null;
    }

}
