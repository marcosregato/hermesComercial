package com.br.hermescomercial.pdv.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL
 */
public class DatabaseConnection {
    private static final String PROPERTIES_FILE = "database-config.properties";
    private static Properties config;
    
    static {
        loadConfiguration();
    }
    
    /**
     * Carrega as configurações do arquivo properties
     */
    private static void loadConfiguration() {
        try {
            config = new Properties();
            config.load(DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (Exception e) {
            System.err.println("Erro ao carregar configurações do banco: " + e.getMessage());
            // Valores padrão para fallback
            config = new Properties();
            config.setProperty("database.host", "localhost");
            config.setProperty("database.port", "5432");
            config.setProperty("database.name", "hermescomercialdb");
            config.setProperty("database.user", "postgres");
            config.setProperty("database.password", "postgres");
        }
    }
    
    /**
     * Obtém uma conexão com o banco de dados PostgreSQL
     * @return Connection objeto de conexão
     * @throws SQLException em caso de erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s",
                config.getProperty("database.host"),
                config.getProperty("database.port"),
                config.getProperty("database.name"));
            
            String user = config.getProperty("database.user");
            String password = config.getProperty("database.password");
            
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false); // Gerenciamento manual de transações
            
            return conn;
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco PostgreSQL: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Fecha a conexão com o banco de dados
     * @param conn conexão a ser fechada
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    /**
     * Executa rollback em uma transação
     * @param conn conexão ativa
     */
    public static void rollback(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao executar rollback: " + e.getMessage());
            }
        }
    }
    
    /**
     * Executa commit em uma transação
     * @param conn conexão ativa
     */
    public static void commit(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.commit();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao executar commit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Testa a conexão com o banco de dados
     * @return true se a conexão for bem-sucedida
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Teste de conexão falhou: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}
