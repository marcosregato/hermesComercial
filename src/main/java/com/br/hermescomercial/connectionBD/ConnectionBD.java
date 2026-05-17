package com.br.hermescomercial.connectionBD;

import com.br.hermescomercial.config.DataSourceConfig;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe responsável por gerenciar conexões com banco de dados
 * Utiliza HikariCP para pool de conexões melhorando performance
 * Suporta múltiplos bancos: PostgreSQL, MySQL, SQLite
 * @author system
 */
public class ConnectionBD {
    private static final Logger logger = LogManager.getLogger(ConnectionBD.class);
    private static final DataSourceConfig dataSourceConfig = DataSourceConfig.getInstance();
    
    /**
     * Obtém conexão com o banco de dados usando pool HikariCP
     * @return Connection conexão ativa do pool
     * @throws SQLException em caso de erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSourceConfig.getConnection();
            logger.debug("Conexão obtida do pool HikariCP");
            return connection;
            
        } catch (SQLException e) {
            logger.error("Erro ao obter conexão do pool: " + e.getMessage(), e);
            throw new SQLException("Não foi possível conectar ao banco de dados", e);
        }
    }
    
    /**
     * Fecha conexão com o banco de dados
     * @param connection conexão a ser fechada
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.debug("Conexão com banco de dados fechada com sucesso");
                }
            } catch (SQLException e) {
                logger.error("Erro ao fechar conexão com banco de dados: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Verifica se uma conexão está aberta e a fecha se necessário
     * @param connection conexão a ser verificada
     */
    public static void ensureConnectionClosed(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    logger.warn("Conexão não foi fechada automaticamente pelo try-with-resources. Fechando manualmente.");
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("Erro ao verificar/fechar conexão: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Testa a conexão com o banco de dados usando pool
     * @return boolean true se conexão funcionar
     */
    public static boolean testConnection() {
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
