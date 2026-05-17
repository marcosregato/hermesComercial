package com.br.hermescomercial.pdv.patterns;

import com.br.hermescomercial.util.SystemLogger;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Singleton Pattern - Gerenciador Central de Configurações
 * Garante uma única instância de configurações em todo o sistema
 */
public class ConfigurationManager {
    
    private static volatile ConfigurationManager instance;
    private Properties configurations;
    private static final String CONFIG_FILE = "system.properties";
    
    // Configurações padrão do sistema
    private static final Properties DEFAULT_CONFIG = new Properties();
    static {
        DEFAULT_CONFIG.setProperty("app.name", "Hermes Comercial PDV");
        DEFAULT_CONFIG.setProperty("app.version", "3.2.0");
        DEFAULT_CONFIG.setProperty("database.url", "jdbc:mysql://localhost:3306/hermes_pdv");
        DEFAULT_CONFIG.setProperty("database.username", "root");
        DEFAULT_CONFIG.setProperty("theme.primary", "#3498db");
        DEFAULT_CONFIG.setProperty("theme.secondary", "#2ecc71");
        DEFAULT_CONFIG.setProperty("validation.required", "true");
        DEFAULT_CONFIG.setProperty("logging.level", "INFO");
        DEFAULT_CONFIG.setProperty("cache.enabled", "true");
        DEFAULT_CONFIG.setProperty("auto.save", "true");
    }
    
    /**
     * Construtor privado para Singleton
     */
    private ConfigurationManager() {
        configurations = new Properties();
        loadConfigurations();
        SystemLogger.ui("ConfigurationManager inicializado (Singleton)");
    }
    
    /**
     * Obtém a instância única (Double-Checked Locking)
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Carrega configurações do arquivo
     */
    private void loadConfigurations() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            configurations.load(fis);
            SystemLogger.ui("Configurações carregadas do arquivo: " + CONFIG_FILE);
        } catch (IOException e) {
            SystemLogger.ui("Arquivo de configurações não encontrado, usando padrões");
            configurations = new Properties(DEFAULT_CONFIG);
            saveConfigurations();
        }
    }
    
    /**
     * Salva configurações no arquivo
     */
    public void saveConfigurations() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            configurations.store(fos, "Hermes Comercial PDV - Configurações do Sistema");
            SystemLogger.ui("Configurações salvas no arquivo: " + CONFIG_FILE);
        } catch (IOException e) {
            SystemLogger.error("Erro ao salvar configurações", e);
        }
    }
    
    /**
     * Obtém uma configuração específica
     */
    public String getProperty(String key) {
        return configurations.getProperty(key, DEFAULT_CONFIG.getProperty(key));
    }
    
    /**
     * Obtém uma configuração com valor padrão
     */
    public String getProperty(String key, String defaultValue) {
        return configurations.getProperty(key, defaultValue);
    }
    
    /**
     * Define uma configuração
     */
    public void setProperty(String key, String value) {
        configurations.setProperty(key, value);
        SystemLogger.ui("Configuração atualizada: " + key + " = " + value);
        
        // Auto-salvar se habilitado
        if ("true".equals(getProperty("auto.save"))) {
            saveConfigurations();
        }
    }
    
    /**
     * Obtém configuração como inteiro
     */
    public int getIntProperty(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException e) {
            return Integer.parseInt(DEFAULT_CONFIG.getProperty(key));
        }
    }
    
    /**
     * Obtém configuração como booleano
     */
    public boolean getBooleanProperty(String key) {
        return "true".equalsIgnoreCase(getProperty(key));
    }
    
    /**
     * Reseta para configurações padrão
     */
    public void resetToDefaults() {
        configurations.clear();
        configurations.putAll(DEFAULT_CONFIG);
        saveConfigurations();
        SystemLogger.ui("Configurações resetadas para os padrões do sistema");
    }
    
    /**
     * Lista todas as configurações atuais
     */
    public void listAllProperties() {
        SystemLogger.ui("=== CONFIGURAÇÕES ATUAIS ===");
        configurations.forEach((key, value) -> 
            SystemLogger.ui(key + " = " + value));
    }
    
    /**
     * Obtém todas as configurações como Properties
     */
    public Properties getAllProperties() {
        Properties copy = new Properties();
        copy.putAll(configurations);
        return copy;
    }
}
