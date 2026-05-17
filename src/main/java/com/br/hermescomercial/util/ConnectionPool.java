package com.br.hermescomercial.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pool de Conexões Simples e Eficiente para Hermes Comercial PDV
 * Reduz overhead de criação de conexões com banco de dados
 */
public class ConnectionPool {
    
    private static final int MAX_POOL_SIZE = 10;
    private static final int INITIAL_POOL_SIZE = 3;
    private static final long CONNECTION_TIMEOUT_MS = 5000; // 5 segundos
    
    private static final BlockingQueue<Connection> connectionPool = new LinkedBlockingQueue<>(MAX_POOL_SIZE);
    private static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final AtomicInteger totalConnectionsCreated = new AtomicInteger(0);
    
    private static String url;
    private static String username;
    private static String password;
    private static boolean initialized = false;
    
    /**
     * Inicializa o pool de conexões
     */
    public static synchronized void initialize(String dbUrl, String dbUser, String dbPassword) {
        if (initialized) return;
        
        url = dbUrl;
        username = dbUser;
        password = dbPassword;
        
        // Criar conexões iniciais
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                Connection conn = createNewConnection();
                if (conn != null) {
                    connectionPool.offer(conn);
                }
            } catch (SQLException e) {
                System.err.println("Erro ao criar conexão inicial: " + e.getMessage());
            }
        }
        
        initialized = true;
        System.out.println("🔗 ConnectionPool inicializado com " + connectionPool.size() + " conexões");
    }
    
    /**
     * Obtém uma conexão do pool
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new SQLException("ConnectionPool não inicializado");
        }
        
        try {
            // Tentar obter conexão existente
            Connection conn = connectionPool.poll(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            
            if (conn != null && !conn.isClosed()) {
                activeConnections.incrementAndGet();
                return conn;
            }
            
            // Se não houver conexão disponível e não atingiu o limite, criar nova
            if (totalConnectionsCreated.get() < MAX_POOL_SIZE) {
                Connection newConn = createNewConnection();
                if (newConn != null) {
                    activeConnections.incrementAndGet();
                    return newConn;
                }
            }
            
            // Esperar por uma conexão disponível
            conn = connectionPool.take(); // Bloqueia até ter uma conexão
            if (conn != null && !conn.isClosed()) {
                activeConnections.incrementAndGet();
                return conn;
            }
            
            throw new SQLException("Não foi possível obter conexão do pool");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrompido ao esperar por conexão", e);
        }
    }
    
    /**
     * Devolve uma conexão ao pool
     */
    public static void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    connectionPool.offer(conn);
                } else {
                    // Se a conexão foi fechada, criar uma nova para substituir
                    Connection newConn = createNewConnection();
                    if (newConn != null) {
                        connectionPool.offer(newConn);
                    }
                }
                activeConnections.decrementAndGet();
            } catch (SQLException e) {
                System.err.println("Erro ao devolver conexão ao pool: " + e.getMessage());
                activeConnections.decrementAndGet();
            }
        }
    }
    
    /**
     * Cria uma nova conexão com o banco
     */
    private static Connection createNewConnection() throws SQLException {
        try {
            // Carregar driver PostgreSQL explicitamente
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            totalConnectionsCreated.incrementAndGet();
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL não encontrado: " + e.getMessage());
            throw new SQLException("Driver não encontrado", e);
        } catch (SQLException e) {
            System.err.println("Erro ao criar nova conexão: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Fecha todas as conexões do pool
     */
    public static synchronized void shutdown() {
        if (!initialized) return;
        
        System.out.println("🔗 Fechando ConnectionPool...");
        
        // Fechar todas as conexões do pool
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        
        connectionPool.clear();
        activeConnections.set(0);
        totalConnectionsCreated.set(0);
        initialized = false;
        
        System.out.println("✅ ConnectionPool fechado com sucesso");
    }
    
    /**
     * Obtém estatísticas do pool
     */
    public static String getStatistics() {
        return String.format(
            "📊 ConnectionPool Stats:\n" +
            "• Pool Size: %d/%d\n" +
            "• Active: %d\n" +
            "• Total Created: %d\n" +
            "• Available: %d",
            connectionPool.size(), MAX_POOL_SIZE,
            activeConnections.get(),
            totalConnectionsCreated.get(),
            connectionPool.size()
        );
    }
    
    /**
     * Verifica se o pool está inicializado
     */
    public static boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Obtém o número de conexões ativas
     */
    public static int getActiveConnections() {
        return activeConnections.get();
    }
    
    /**
     * Obtém o número de conexões disponíveis
     */
    public static int getAvailableConnections() {
        return connectionPool.size();
    }
}
