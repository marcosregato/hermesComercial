package com.br.hermescomercial.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe utilitária para carregar configurações de ambiente e .env
 * @author system
 */
public class EnvironmentConfig {
    private static final Logger logger = LogManager.getLogger(EnvironmentConfig.class);
    private static final String ENV_FILE = ".env";
    private static Properties properties;
    
    static {
        loadEnvironment();
    }
    
    private static void loadEnvironment() {
        properties = new Properties();
        
        // Primeiro tenta carregar do arquivo .env
        try (FileInputStream fis = new FileInputStream(ENV_FILE)) {
            properties.load(fis);
            logger.info("Arquivo .env carregado com sucesso");
        } catch (IOException e) {
            logger.warn("Arquivo .env não encontrado. Usando variáveis de ambiente do sistema.");
        }
        
        // Sobrescreve com variáveis de ambiente do sistema se existirem
        loadSystemEnvironment();
    }
    
    private static void loadSystemEnvironment() {
        // PostgreSQL
        String postgresUrl = System.getenv("POSTGRES_URL");
        if (postgresUrl != null) {
            properties.setProperty("POSTGRES_URL", postgresUrl);
        }
        
        String postgresUser = System.getenv("POSTGRES_USER");
        if (postgresUser != null) {
            properties.setProperty("POSTGRES_USER", postgresUser);
        }
        
        String postgresPassword = System.getenv("POSTGRES_PASSWORD");
        if (postgresPassword != null) {
            properties.setProperty("POSTGRES_PASSWORD", postgresPassword);
        }
        
        // MySQL
        String mysqlUrl = System.getenv("MYSQL_URL");
        if (mysqlUrl != null) {
            properties.setProperty("MYSQL_URL", mysqlUrl);
        }
        
        String mysqlUser = System.getenv("MYSQL_USER");
        if (mysqlUser != null) {
            properties.setProperty("MYSQL_USER", mysqlUser);
        }
        
        String mysqlPassword = System.getenv("MYSQL_PASSWORD");
        if (mysqlPassword != null) {
            properties.setProperty("MYSQL_PASSWORD", mysqlPassword);
        }
        
        // SQLite
        String sqliteUrl = System.getenv("SQLITE_URL");
        if (sqliteUrl != null) {
            properties.setProperty("SQLITE_URL", sqliteUrl);
        }
        
        // Logging
        String logLevel = System.getenv("LOG_LEVEL");
        if (logLevel != null) {
            properties.setProperty("LOG_LEVEL", logLevel);
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.warn("Valor inválido para propriedade " + key + ": " + value + ". Usando padrão: " + defaultValue);
            return defaultValue;
        }
    }
    
    // Métodos específicos para banco de dados
    public static String getPostgresUrl() {
        return getProperty("POSTGRES_URL", "jdbc:postgresql://localhost:5432/hermescomercialdb");
    }
    
    public static String getPostgresUser() {
        return getProperty("POSTGRES_USER", "hermesuser");
    }
    
    public static String getPostgresPassword() {
        return getProperty("POSTGRES_PASSWORD", "hermespass");
    }
    
    public static String getPostgresDriver() {
        return getProperty("POSTGRES_DRIVER", "org.postgresql.Driver");
    }
    
    public static String getMysqlUrl() {
        return getProperty("MYSQL_URL", "jdbc:mysql://localhost/hermescomercialdb");
    }
    
    public static String getMysqlUser() {
        return getProperty("MYSQL_USER", "root");
    }
    
    public static String getMysqlPassword() {
        return getProperty("MYSQL_PASSWORD", "admin123");
    }
    
    public static String getMysqlDriver() {
        return getProperty("MYSQL_DRIVER", "com.mysql.cj.jdbc.Driver");
    }
    
    public static String getSqliteUrl() {
        return getProperty("SQLITE_URL", "jdbc:sqlite:hermescomercial.db");
    }
    
    public static String getSqliteDriver() {
        return getProperty("SQLITE_DRIVER", "org.sqlite.JDBC");
    }
    
    public static String getLogLevel() {
        return getProperty("LOG_LEVEL", "INFO");
    }
    
    public static int getDbPoolSize() {
        return getIntProperty("DB_POOL_SIZE", 10);
    }
    
    public static int getDbPoolTimeout() {
        return getIntProperty("DB_POOL_TIMEOUT", 30000);
    }
}
