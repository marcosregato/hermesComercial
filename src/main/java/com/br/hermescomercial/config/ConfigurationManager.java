package com.br.hermescomercial.config;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.prefs.Preferences;

/**
 * Configuration Manager Refatorado
 * Gerencia configurações do sistema com suporte a múltiplas fontes
 * Versão 2.0.0 - Configuration Pattern Implementation
 */
public class ConfigurationManager {
    
    private static volatile ConfigurationManager instance;
    private static final Object lock = new Object();
    
    // Fontes de configuração
    public enum ConfigSource {
        SYSTEM_PROPERTIES,
        ENVIRONMENT_VARIABLES,
        CONFIG_FILE,
        DATABASE,
        USER_PREFERENCES,
        DEFAULT_VALUES
    }
    
    // Tipos de configuração
    public enum ConfigType {
        STRING, INTEGER, DOUBLE, BOOLEAN, LIST, MAP
    }
    
    // Estrutura de configuração
    private static class ConfigEntry {
        final Object value;
        final ConfigSource source;
        final ConfigType type;
        final String description;
        final boolean isSensitive;
        
        ConfigEntry(Object value, ConfigSource source, ConfigType type, String description, boolean isSensitive) {
            this.value = value;
            this.source = source;
            this.type = type;
            this.description = description;
            this.isSensitive = isSensitive;
        }
    }
    
    // Armazenamento de configurações
    private final Map<String, ConfigEntry> configurations = new ConcurrentHashMap<>();
    private final List<ConfigurationListener> listeners = new CopyOnWriteArrayList<>();
    private final Properties defaultProperties = new Properties();
    private final Preferences userPreferences;
    
    // Arquivo de configuração
    private static final String CONFIG_FILE = "hermes-comercial.properties";
    private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".hermes";
    
    // Construtor privado para Singleton
    private ConfigurationManager() {
        this.userPreferences = Preferences.userNodeForPackage(ConfigurationManager.class);
        loadDefaultConfigurations();
        loadConfigurations();
    }
    
    /**
     * Obtém instância singleton
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Interface para listeners de mudanças de configuração
     */
    public interface ConfigurationListener {
        void onConfigurationChanged(String key, Object oldValue, Object newValue);
    }
    
    /**
     * Carrega configurações padrão
     */
    private void loadDefaultConfigurations() {
        // Configurações de Banco de Dados
        setDefault("database.url", "jdbc:postgresql://localhost:5432/hermes_comercial", 
                   "URL de conexão com o banco de dados");
        setDefault("database.username", "postgres", "Usuário do banco de dados");
        setDefault("database.password", "admin", "Senha do banco de dados", true);
        setDefault("database.max_connections", "20", "Número máximo de conexões");
        setDefault("database.connection_timeout", "30", "Timeout de conexão (segundos)");
        
        // Configurações da Aplicação
        setDefault("app.name", "Hermes Comercial PDV/ERP", "Nome da aplicação");
        setDefault("app.version", "3.1.0", "Versão da aplicação");
        setDefault("app.theme", "default", "Tema da interface");
        setDefault("app.language", "pt_BR", "Idioma da aplicação");
        setDefault("app.auto_save", "true", "Salvamento automático");
        setDefault("app.backup_interval", "3600", "Intervalo de backup (segundos)");
        
        // Configurações de PDV
        setDefault("pdv.auto_print_receipt", "false", "Impressão automática de cupom");
        setDefault("pdv.require_customer_data", "false", "Exigir dados do cliente");
        setDefault("pdv.max_items_per_sale", "100", "Máximo de itens por venda");
        setDefault("pdv.default_payment_method", "dinheiro", "Método de pagamento padrão");
        
        // Configurações de ERP
        setDefault("erp.inventory_alert_threshold", "10", "Limite de alerta de estoque");
        setDefault("erp.financial_report_period", "monthly", "Período de relatórios financeiros");
        setDefault("erp.user_session_timeout", "3600", "Timeout de sessão do usuário");
        
        // Configurações de Cache
        setDefault("cache.enabled", "true", "Cache habilitado");
        setDefault("cache.max_size", "1000", "Tamanho máximo do cache");
        setDefault("cache.ttl", "300", "Time to live do cache (segundos)");
        
        // Configurações de Logging
        setDefault("logging.level", "INFO", "Nível de log");
        setDefault("logging.file_enabled", "true", "Log em arquivo");
        setDefault("logging.console_enabled", "true", "Log no console");
        setDefault("logging.max_file_size", "10MB", "Tamanho máximo do arquivo de log");
    }
    
    /**
     * Carrega configurações de múltiplas fontes
     */
    private void loadConfigurations() {
        // 1. Carrega do arquivo de configuração
        loadFromFile();
        
        // 2. Carrega das variáveis de ambiente
        loadFromEnvironment();
        
        // 3. Carrega das propriedades do sistema
        loadFromSystemProperties();
        
        // 4. Carrega das preferências do usuário
        loadFromUserPreferences();
    }
    
    /**
     * Carrega configurações do arquivo
     */
    private void loadFromFile() {
        File configFile = new File(CONFIG_DIR, CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                Properties fileProps = new Properties();
                fileProps.load(fis);
                
                for (String key : fileProps.stringPropertyNames()) {
                    setConfiguration(key, fileProps.getProperty(key), ConfigSource.CONFIG_FILE);
                }
            } catch (IOException e) {
                System.err.println("Erro ao carregar arquivo de configuração: " + e.getMessage());
            }
        }
    }
    
    /**
     * Carrega configurações das variáveis de ambiente
     */
    private void loadFromEnvironment() {
        Map<String, String> envVars = System.getenv();
        
        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            if (entry.getKey().startsWith("HERMES_")) {
                String configKey = entry.getKey().substring(7).toLowerCase().replace("_", ".");
                setConfiguration(configKey, entry.getValue(), ConfigSource.ENVIRONMENT_VARIABLES);
            }
        }
    }
    
    /**
     * Carrega configurações das propriedades do sistema
     */
    private void loadFromSystemProperties() {
        Properties systemProps = System.getProperties();
        
        for (String key : systemProps.stringPropertyNames()) {
            if (key.startsWith("hermes.")) {
                String configKey = key.substring(7);
                setConfiguration(configKey, systemProps.getProperty(key), ConfigSource.SYSTEM_PROPERTIES);
            }
        }
    }
    
    /**
     * Carrega configurações das preferências do usuário
     */
    private void loadFromUserPreferences() {
        try {
            for (String key : userPreferences.keys()) {
                String value = userPreferences.get(key, null);
                if (value != null) {
                    setConfiguration(key, value, ConfigSource.USER_PREFERENCES);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar preferências do usuário: " + e.getMessage());
        }
    }
    
    /**
     * Define configuração padrão
     */
    private void setDefault(String key, String value, String description) {
        setDefault(key, value, description, false);
    }
    
    /**
     * Define configuração padrão
     */
    private void setDefault(String key, String value, String description, boolean isSensitive) {
        defaultProperties.setProperty(key, value);
        configurations.putIfAbsent(key, new ConfigEntry(value, ConfigSource.DEFAULT_VALUES, 
                           ConfigType.STRING, description, isSensitive));
    }
    
    /**
     * Define uma configuração
     */
    public void setConfiguration(String key, Object value, ConfigSource source) {
        ConfigType type = determineType(value);
        ConfigEntry oldEntry = configurations.get(key);
        Object oldValue = oldEntry != null ? oldEntry.value : null;
        
        ConfigEntry newEntry = new ConfigEntry(value, source, type, 
                                          getDescription(key), isSensitive(key));
        configurations.put(key, newEntry);
        
        // Notifica listeners
        if (!Objects.equals(oldValue, value)) {
            notifyListeners(key, oldValue, value);
        }
        
        // Salva automaticamente se for preferência do usuário
        if (source == ConfigSource.USER_PREFERENCES) {
            saveToUserPreferences(key, value);
        }
    }
    
    /**
     * Obtém configuração como String
     */
    public String getString(String key) {
        ConfigEntry entry = configurations.get(key);
        return entry != null ? String.valueOf(entry.value) : null;
    }
    
    /**
     * Obtém configuração como String com valor padrão
     */
    public String getString(String key, String defaultValue) {
        String value = getString(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como Integer
     */
    public Integer getInteger(String key) {
        ConfigEntry entry = configurations.get(key);
        if (entry != null) {
            try {
                return Integer.valueOf(String.valueOf(entry.value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Obtém configuração como Integer com valor padrão
     */
    public Integer getInteger(String key, Integer defaultValue) {
        Integer value = getInteger(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como Long
     */
    public Long getLong(String key) {
        ConfigEntry entry = configurations.get(key);
        if (entry != null) {
            try {
                return Long.valueOf(String.valueOf(entry.value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Obtém configuração como Long com valor padrão
     */
    public Long getLong(String key, Long defaultValue) {
        Long value = getLong(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como Double
     */
    public Double getDouble(String key) {
        ConfigEntry entry = configurations.get(key);
        if (entry != null) {
            try {
                return Double.valueOf(String.valueOf(entry.value));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Obtém configuração como Double com valor padrão
     */
    public Double getDouble(String key, Double defaultValue) {
        Double value = getDouble(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como Boolean
     */
    public Boolean getBoolean(String key) {
        ConfigEntry entry = configurations.get(key);
        if (entry != null) {
            return Boolean.valueOf(String.valueOf(entry.value));
        }
        return null;
    }
    
    /**
     * Obtém configuração como Boolean com valor padrão
     */
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean value = getBoolean(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Obtém configuração como List
     */
    @SuppressWarnings("unchecked")
    public List<String> getList(String key) {
        ConfigEntry entry = configurations.get(key);
        if (entry != null && entry.type == ConfigType.LIST) {
            return (List<String>) entry.value;
        }
        return null;
    }
    
    /**
     * Define configuração
     */
    public void set(String key, String value) {
        setConfiguration(key, value, ConfigSource.USER_PREFERENCES);
    }
    
    /**
     * Define configuração
     */
    public void set(String key, Integer value) {
        setConfiguration(key, value, ConfigSource.USER_PREFERENCES);
    }
    
    /**
     * Define configuração
     */
    public void set(String key, Double value) {
        setConfiguration(key, value, ConfigSource.USER_PREFERENCES);
    }
    
    /**
     * Define configuração
     */
    public void set(String key, Boolean value) {
        setConfiguration(key, value, ConfigSource.USER_PREFERENCES);
    }
    
    /**
     * Salva configurações no arquivo
     */
    public void saveToFile() {
        File configFile = new File(CONFIG_DIR, CONFIG_FILE);
        configFile.getParentFile().mkdirs();
        
        Properties fileProps = new Properties();
        
        for (Map.Entry<String, ConfigEntry> entry : configurations.entrySet()) {
            if (entry.getValue().source == ConfigSource.USER_PREFERENCES || 
                entry.getValue().source == ConfigSource.CONFIG_FILE) {
                fileProps.setProperty(entry.getKey(), String.valueOf(entry.getValue().value));
            }
        }
        
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            fileProps.store(fos, "Hermes Comercial - Configuration File");
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo de configuração: " + e.getMessage());
        }
    }
    
    /**
     * Salva nas preferências do usuário
     */
    private void saveToUserPreferences(String key, Object value) {
        try {
            userPreferences.put(key, String.valueOf(value));
            userPreferences.flush();
        } catch (Exception e) {
            System.err.println("Erro ao salvar preferência do usuário: " + e.getMessage());
        }
    }
    
    /**
     * Adiciona listener de mudanças de configuração
     */
    public void addListener(ConfigurationListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove listener de mudanças de configuração
     */
    public void removeListener(ConfigurationListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notifica listeners sobre mudanças
     */
    private void notifyListeners(String key, Object oldValue, Object newValue) {
        for (ConfigurationListener listener : listeners) {
            try {
                listener.onConfigurationChanged(key, oldValue, newValue);
            } catch (Exception e) {
                System.err.println("Erro no listener de configuração: " + e.getMessage());
            }
        }
    }
    
    /**
     * Determina o tipo da configuração
     */
    private ConfigType determineType(Object value) {
        if (value instanceof List) {
            return ConfigType.LIST;
        } else if (value instanceof Map) {
            return ConfigType.MAP;
        } else if (value instanceof Boolean) {
            return ConfigType.BOOLEAN;
        } else if (value instanceof Number) {
            return ConfigType.DOUBLE;
        } else {
            return ConfigType.STRING;
        }
    }
    
    /**
     * Obtém descrição da configuração
     */
    private String getDescription(String key) {
        ConfigEntry entry = configurations.get(key);
        return entry != null ? entry.description : "";
    }
    
    /**
     * Verifica se configuração é sensível
     */
    private boolean isSensitive(String key) {
        ConfigEntry entry = configurations.get(key);
        return entry != null ? entry.isSensitive : false;
    }
    
    /**
     * Lista todas as configurações
     */
    public Map<String, Object> getAllConfigurations() {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, ConfigEntry> entry : configurations.entrySet()) {
            if (!entry.getValue().isSensitive) {
                result.put(entry.getKey(), entry.getValue().value);
            }
        }
        return result;
    }
    
    /**
     * Obtém fonte da configuração
     */
    public ConfigSource getSource(String key) {
        ConfigEntry entry = configurations.get(key);
        return entry != null ? entry.source : null;
    }
    
    /**
     * Reseta configuração para valor padrão
     */
    public void reset(String key) {
        String defaultValue = defaultProperties.getProperty(key);
        if (defaultValue != null) {
            setConfiguration(key, defaultValue, ConfigSource.DEFAULT_VALUES);
        }
    }
    
    /**
     * Reseta todas as configurações para valores padrão
     */
    public void resetAll() {
        for (String key : defaultProperties.stringPropertyNames()) {
            reset(key);
        }
    }
}
