package com.br.hermescomercial.connectionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionBD {
    private static final Logger logger = LogManager.getLogger(ConnectionBD.class.getName());
    
    // Configurações do banco de dados
    private static final String URL = "jdbc:postgresql://localhost:5432/hermescomercialdb";
    private static final String USER = "hermesuser";
    private static final String PASSWORD = "hermespass";
    
    // Driver JDBC
    private static final String DRIVER = "org.postgresql.Driver";
    
    static {
        try {
            Class.forName(DRIVER);
            logger.info("Driver PostgreSQL carregado com sucesso");
        } catch (ClassNotFoundException e) {
            logger.error("Erro ao carregar driver PostgreSQL: " + e.getMessage(), e);
            throw new RuntimeException("Driver PostgreSQL não encontrado", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Conexão com banco de dados estabelecida com sucesso");
            return connection;
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados: " + e.getMessage(), e);
            throw new SQLException("Não foi possível conectar ao banco de dados", e);
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Conexão com banco de dados fechada com sucesso");
            } catch (SQLException e) {
                logger.error("Erro ao fechar conexão com banco de dados: " + e.getMessage(), e);
            }
        }
    }
    
    public static void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null && !connection.isClosed()) {
                logger.info("Teste de conexão realizado com sucesso");
                System.out.println("Conexão com banco de dados funcionando corretamente!");
            }
        } catch (SQLException e) {
            logger.error("Teste de conexão falhou: " + e.getMessage(), e);
            System.err.println("Erro na conexão com banco de dados: " + e.getMessage());
        }
    }
}
