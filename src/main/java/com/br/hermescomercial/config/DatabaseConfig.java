package com.br.hermescomercial.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe para gerenciar configuração do tipo de banco de dados
 * @author marcos
 */
public class DatabaseConfig {
    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class);
    private static final String CONFIG_FILE = "./database-config.properties";
    
    public enum DatabaseType {
        POSTGRESQL("PostgreSQL"),
        MYSQL("MySQL"),
        EXCEL("Excel"),
        SQLITE("SQLite");
        
        private final String displayName;
        
        DatabaseType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private static DatabaseConfig instance;
    private Properties properties;
    
    private DatabaseConfig() {
        loadConfig();
    }
    
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }
    
    private void loadConfig() {
        properties = new Properties();
        
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
            logger.info("Configuração de banco de dados carregada: " + properties.getProperty("database.type"));
        } catch (IOException e) {
            logger.warn("Arquivo de configuração não encontrado. Criando configuração padrão...");
            createDefaultConfig();
        }
    }
    
    private void createDefaultConfig() {
        properties.setProperty("database.type", DatabaseType.POSTGRESQL.name());
        properties.setProperty("database.name", "PostgreSQL");
        properties.setProperty("database.description", "Banco de dados PostgreSQL local");
        
        saveConfig();
        logger.info("Configuração padrão criada: PostgreSQL");
    }
    
    public void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Hermes Comercial - Configuração de Banco de Dados");
            logger.info("Configuração de banco de dados salva");
        } catch (IOException e) {
            logger.error("Erro ao salvar configuração de banco de dados: " + e.getMessage(), e);
        }
    }
    
    public DatabaseType getDatabaseType() {
        String type = properties.getProperty("database.type", DatabaseType.POSTGRESQL.name());
        try {
            return DatabaseType.valueOf(type);
        } catch (IllegalArgumentException e) {
            logger.warn("Tipo de banco inválido: " + type + ". Usando PostgreSQL como padrão.");
            return DatabaseType.POSTGRESQL;
        }
    }
    
    public void setDatabaseType(DatabaseType type) {
        properties.setProperty("database.type", type.name());
        properties.setProperty("database.name", type.getDisplayName());
        
        switch (type) {
            case POSTGRESQL:
                properties.setProperty("database.description", "Banco de dados PostgreSQL local");
                break;
            case MYSQL:
                properties.setProperty("database.description", "Banco de dados MySQL local");
                break;
            case EXCEL:
                properties.setProperty("database.description", "Arquivos Excel como banco de dados");
                break;
            case SQLITE:
                properties.setProperty("database.description", "Banco de dados SQLite local");
                break;
        }
        
        saveConfig();
        logger.info("Tipo de banco alterado para: " + type.getDisplayName());
    }
    
    public String getDatabaseName() {
        return properties.getProperty("database.name", "PostgreSQL");
    }
    
    public String getDatabaseDescription() {
        return properties.getProperty("database.description", "Banco de dados PostgreSQL local");
    }
    
    public boolean isPostgreSQL() {
        return getDatabaseType() == DatabaseType.POSTGRESQL;
    }
    
    public boolean isExcel() {
        return getDatabaseType() == DatabaseType.EXCEL;
    }
    
    public boolean isSQLite() {
        return getDatabaseType() == DatabaseType.SQLITE;
    }
    
    public void testConfiguration() {
        logger.info("=== Teste de Configuração de Banco de Dados ===");
        logger.info("Tipo: " + getDatabaseName());
        logger.info("Descrição: " + getDatabaseDescription());
        logger.info("Arquivo de configuração: " + CONFIG_FILE);
        
        switch (getDatabaseType()) {
            case POSTGRESQL:
                logger.info("Usando PostgreSQL - Configuração via ConnectionBD");
                break;
            case MYSQL:
                logger.info("Usando MySQL - Configuração via ConnectionBD");
                break;
            case EXCEL:
                logger.info("Usando Excel - Arquivos em ./data/excel/");
                break;
            case SQLITE:
                logger.info("Usando SQLite - Arquivo ./hermes_comercial.db");
                break;
        }
        
        logger.info("========================================");
    }
}
