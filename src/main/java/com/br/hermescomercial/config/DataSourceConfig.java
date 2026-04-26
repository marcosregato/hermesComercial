package com.br.hermescomercial.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Configuração de DataSource com HikariCP para pool de conexões
 * Melhora performance e estabilidade das conexões com banco de dados
 * @author system
 */
public class DataSourceConfig {
    private static final Logger logger = LogManager.getLogger(DataSourceConfig.class);
    
    private static HikariDataSource dataSource;
    private static final DataSourceConfig instance = new DataSourceConfig();
    
    private DataSourceConfig() {
        initializeDataSource();
    }
    
    public static DataSourceConfig getInstance() {
        return instance;
    }
    
    /**
     * Inicializa o pool de conexões HikariCP
     */
    private void initializeDataSource() {
        try {
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            
            HikariConfig config = new HikariConfig();
            
            // Configurações básicas de conexão
            switch (dbConfig.getDatabaseType()) {
                case POSTGRESQL:
                    config.setJdbcUrl(EnvironmentConfig.getPostgresUrl());
                    config.setUsername(EnvironmentConfig.getPostgresUser());
                    config.setPassword(EnvironmentConfig.getPostgresPassword());
                    config.setDriverClassName(EnvironmentConfig.getPostgresDriver());
                    break;
                    
                case MYSQL:
                    config.setJdbcUrl(EnvironmentConfig.getMysqlUrl());
                    config.setUsername(EnvironmentConfig.getMysqlUser());
                    config.setPassword(EnvironmentConfig.getMysqlPassword());
                    config.setDriverClassName(EnvironmentConfig.getMysqlDriver());
                    break;
                    
                case SQLITE:
                    config.setJdbcUrl(EnvironmentConfig.getSqliteUrl());
                    config.setDriverClassName(EnvironmentConfig.getSqliteDriver());
                    break;
                    
                default:
                    logger.warn("Tipo de banco não configurado. Usando PostgreSQL como padrão.");
                    config.setJdbcUrl(EnvironmentConfig.getPostgresUrl());
                    config.setUsername(EnvironmentConfig.getPostgresUser());
                    config.setPassword(EnvironmentConfig.getPostgresPassword());
                    config.setDriverClassName(EnvironmentConfig.getPostgresDriver());
                    break;
            }
            
            // Configurações do pool
            config.setMaximumPoolSize(EnvironmentConfig.getDbPoolSize());
            config.setMinimumIdle(2);
            config.setConnectionTimeout(EnvironmentConfig.getDbPoolTimeout());
            config.setIdleTimeout(600000); // 10 minutos
            config.setMaxLifetime(1800000); // 30 minutos
            config.setLeakDetectionThreshold(60000); // 1 minuto
            
            // Configurações de performance
            config.setPoolName("HermesComercial-Pool");
            config.setAutoCommit(true);
            config.setConnectionTestQuery("SELECT 1");
            
            // Configurações de logging
            config.setRegisterMbeans(true);
            
            // Criar o DataSource
            dataSource = new HikariDataSource(config);
            
            logger.info("Pool de conexões HikariCP inicializado com sucesso");
            logger.info("Configurações - Pool Size: {}, Timeout: {}ms", 
                       config.getMaximumPoolSize(), config.getConnectionTimeout());
            
        } catch (Exception e) {
            logger.error("Erro ao inicializar pool de conexões: " + e.getMessage(), e);
            throw new RuntimeException("Falha na configuração do DataSource", e);
        }
    }
    
    /**
     * Obtém uma conexão do pool
     * @return Connection conexão ativa
     * @throws SQLException em caso de erro
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource não inicializado");
        }
        
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Conexão obtida do pool - Conexões ativas: {}, Ociosas: {}", 
                         dataSource.getHikariPoolMXBean().getActiveConnections(),
                         dataSource.getHikariPoolMXBean().getIdleConnections());
            return connection;
            
        } catch (SQLException e) {
            logger.error("Erro ao obter conexão do pool: " + e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Obtém o DataSource configurado
     * @return DataSource configurado
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException("DataSource não inicializado");
        }
        return dataSource;
    }
    
    /**
     * Fecha o pool de conexões
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Fechando pool de conexões HikariCP");
            dataSource.close();
            dataSource = null;
        }
    }
    
    /**
     * Verifica o status do pool
     * @return String com informações do pool
     */
    public String getPoolStatus() {
        if (dataSource == null || dataSource.isClosed()) {
            return "Pool não inicializado ou fechado";
        }
        
        return String.format(
            "Pool Status - Ativas: %d, Ociosas: %d, Total: %d, Aguardando: %d",
            dataSource.getHikariPoolMXBean().getActiveConnections(),
            dataSource.getHikariPoolMXBean().getIdleConnections(),
            dataSource.getHikariPoolMXBean().getTotalConnections(),
            dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
        );
    }
    
    /**
     * Testa a conexão com o pool
     * @return boolean true se conexão funcionar
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null && !connection.isClosed()) {
                logger.info("Teste de conexão com pool realizado com sucesso");
                return true;
            }
        } catch (SQLException e) {
            logger.error("Teste de conexão com pool falhou: " + e.getMessage(), e);
        }
        return false;
    }
}
