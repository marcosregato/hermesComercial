package com.br.hermescomercial.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilitário para configuração e teste de conexão com banco de dados
 * Versão 2.0 - Suporte para MySQL, PostgreSQL e H2
 */
public class DatabaseConfig {
    
    private static final String CONFIG_FILE = "database-config.properties";
    private static Properties config;
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        config = new Properties();
        File configFile = new File(CONFIG_FILE);
        
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                config.load(fis);
            } catch (IOException e) {
                System.err.println("Erro ao carregar configuração: " + e.getMessage());
            }
        } else {
            // Configuração padrão
            setDefaultConfig();
            saveConfig();
        }
    }
    
    private static void setDefaultConfig() {
        config.setProperty("database.type", "H2");
        config.setProperty("database.host", "localhost");
        config.setProperty("database.port", "9092");
        config.setProperty("database.name", "hermes_comercial");
        config.setProperty("database.user", "sa");
        config.setProperty("database.password", "");
        config.setProperty("database.description", "Banco de dados H2 em memória (modo offline)");
    }
    
    public static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            config.store(fos, "Hermes Comercial - Configuração de Banco de Dados");
        } catch (IOException e) {
            System.err.println("Erro ao salvar configuração: " + e.getMessage());
        }
    }
    
    public static String getProperty(String key) {
        return config.getProperty(key);
    }
    
    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }
    
    public static void setProperty(String key, String value) {
        config.setProperty(key, value);
    }
    
    public static boolean testConnection(String type, String host, String port, 
                                       String database, String user, String password) {
        String url = buildConnectionUrl(type, host, port, database);
        
        try {
            // Carregar driver específico
            Class.forName(getDriverClassName(type));
            
            // Tentar conexão
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                return conn.isValid(5); // Testar por 5 segundos
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver não encontrado: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.err.println("Erro de conexão: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean testConnectionWithFallback(String type, String host, String port, 
                                                     String database, String user, String password) {
        // Primeiro tentar o banco solicitado
        if (testConnection(type, host, port, database, user, password)) {
            return true;
        }
        
        // Se falhar, não tentar fallback
        if (!"H2".equals(type)) {
            System.out.println("Conexão falhou, sem fallback configurado");
            return false;
        }
        
        return false;
    }
    
    public static Connection getConnection() throws SQLException {
        String type = getProperty("database.type");
        String host = getProperty("database.host");
        String port = getProperty("database.port");
        String database = getProperty("database.name");
        String user = getProperty("database.user");
        String password = getProperty("database.password");
        
        // Tentar conexão principal
        try {
            String url = buildConnectionUrl(type, host, port, database);
            Class.forName(getDriverClassName(type));
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new SQLException("Falha na conexão: " + e.getMessage());
        }
    }
    
    private static String buildConnectionUrl(String type, String host, String port, String database) {
        switch (type.toUpperCase()) {
            case "MYSQL":
                return "jdbc:mysql://" + host + ":" + port + "/" + database;
            case "POSTGRESQL":
                return "jdbc:postgresql://" + host + ":" + port + "/" + database;
            case "H2":
                return "jdbc:h2:mem:" + database + ";DB_CLOSE_DELAY=-1";
            case "SQLITE":
                return "jdbc:sqlite:" + database;
            default:
                throw new IllegalArgumentException("Tipo de banco não suportado: " + type);
        }
    }
    
    private static String getDriverClassName(String type) {
        switch (type.toUpperCase()) {
            case "MYSQL":
                return "com.mysql.cj.jdbc.Driver";
            case "POSTGRESQL":
                return "org.postgresql.Driver";
            case "H2":
                return "org.h2.Driver";
            case "SQLITE":
                return "org.sqlite.JDBC";
            default:
                throw new IllegalArgumentException("Tipo de banco não suportado: " + type);
        }
    }
    
    public static String getDatabaseType() {
        return getProperty("database.type");
    }
    
    public static String getDatabaseDescription() {
        return getProperty("database.description");
    }
}
