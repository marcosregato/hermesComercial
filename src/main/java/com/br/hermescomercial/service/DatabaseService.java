package com.br.hermescomercial.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Singleton Pattern para serviço de banco de dados
 * Garante uma única instância e gerencia conexões com o banco
 * Versão 1.0.0 - Design Pattern Implementation
 */
public class DatabaseService {
    
    private static volatile DatabaseService instance;
    private static final Object lock = new Object();
    
    private Connection connection;
    private final AtomicBoolean isConnected = new AtomicBoolean(false);
    private Properties connectionProperties;
    
    // Configurações padrão PostgreSQL
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/hermes_comercial";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "admin";
    
    // Construtor privado para Singleton
    private DatabaseService() {
        initializeConnectionProperties();
    }
    
    /**
     * Método estático para obter a instância única (Double-Checked Locking)
     * @return Instância única do DatabaseService
     */
    public static DatabaseService getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DatabaseService();
                }
            }
        }
        return instance;
    }
    
    /**
     * Inicializa propriedades de conexão
     */
    private void initializeConnectionProperties() {
        connectionProperties = new Properties();
        connectionProperties.setProperty("user", DEFAULT_USER);
        connectionProperties.setProperty("password", DEFAULT_PASSWORD);
        connectionProperties.setProperty("ssl", "false");
        connectionProperties.setProperty("useSSL", "false");
        connectionProperties.setProperty("autoReconnect", "true");
        connectionProperties.setProperty("socketTimeout", "30");
        connectionProperties.setProperty("connectTimeout", "10");
    }
    
    /**
     * Conecta ao banco de dados
     * @return true se conectou com sucesso, false caso contrário
     */
    public boolean connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("📦 DatabaseService: Já conectado ao banco de dados");
                return true;
            }
            
            System.out.println("🔌 DatabaseService: Conectando ao PostgreSQL...");
            connection = DriverManager.getConnection(DEFAULT_URL, connectionProperties);
            isConnected.set(true);
            
            System.out.println("✅ DatabaseService: Conectado com sucesso ao PostgreSQL!");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌ DatabaseService: Erro ao conectar ao banco de dados: " + e.getMessage());
            isConnected.set(false);
            return false;
        }
    }
    
    /**
     * Desconecta do banco de dados
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                isConnected.set(false);
                System.out.println("🔌 DatabaseService: Desconectado do banco de dados");
            }
        } catch (SQLException e) {
            System.err.println("❌ DatabaseService: Erro ao desconectar: " + e.getMessage());
        }
    }
    
    /**
     * Obtém a conexão atual
     * @return Connection ou null se não conectado
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("🔄 DatabaseService: Reconectando ao banco de dados...");
                connect();
            }
            return connection;
        } catch (Exception e) {
            System.err.println("❌ DatabaseService: Erro ao obter conexão: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica se está conectado
     * @return true se conectado, false caso contrário
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed() && isConnected.get();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Executa um teste de conexão
     * @return true se o teste passou, false caso contrário
     */
    public boolean testConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
            
            // Teste simples de consulta
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery("SELECT 1");
            boolean hasResult = resultSet.next();
            resultSet.close();
            statement.close();
            
            return hasResult;
            
        } catch (SQLException e) {
            System.err.println("❌ DatabaseService: Erro no teste de conexão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Atualiza configurações de conexão
     * @param url URL do banco
     * @param user Usuário
     * @param password Senha
     */
    public void updateConnectionConfig(String url, String user, String password) {
        boolean wasConnected = isConnected();
        
        if (wasConnected) {
            disconnect();
        }
        
        connectionProperties.setProperty("user", user);
        connectionProperties.setProperty("password", password);
        
        if (wasConnected) {
            connect();
        }
    }
    
    /**
     * Obtém informações da conexão
     * @return String com informações da conexão
     */
    public String getConnectionInfo() {
        try {
            if (connection == null || connection.isClosed()) {
                return "Não conectado";
            }
            
            var metaData = connection.getMetaData();
            return String.format(
                "Conectado a: %s %s (versão %s) | Usuário: %s | URL: %s",
                metaData.getDatabaseProductName(),
                metaData.getDatabaseProductVersion(),
                metaData.getDriverVersion(),
                metaData.getUserName(),
                DEFAULT_URL
            );
            
        } catch (SQLException e) {
            return "Erro ao obter informações: " + e.getMessage();
        }
    }
    
    /**
     * Limpa recursos (para uso em shutdown)
     */
    public void cleanup() {
        disconnect();
        instance = null;
        System.out.println("🧹 DatabaseService: Recursos limpos");
    }
}
