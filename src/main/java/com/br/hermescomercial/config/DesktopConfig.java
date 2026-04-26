package com.br.hermescomercial.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Configurações específicas para ambiente desktop
 * Gerencia diretórios locais, banco embedded e configurações do usuário
 */
public class DesktopConfig {
    private static final Logger logger = LogManager.getLogger(DesktopConfig.class);
    
    // Diretórios base para desktop
    private static final String APP_NAME = "HermesComercial";
    private static final String APP_DATA_DIR = System.getProperty("user.home") + File.separator + APP_NAME;
    private static final String DB_DIR = APP_DATA_DIR + File.separator + "database";
    private static final String LOG_DIR = APP_DATA_DIR + File.separator + "logs";
    private static final String CONFIG_DIR = APP_DATA_DIR + File.separator + "config";
    private static final String BACKUP_DIR = APP_DATA_DIR + File.separator + "backups";
    private static final String REPORTS_DIR = APP_DATA_DIR + File.separator + "reports";
    
    // Nome do banco embedded
    private static final String EMBEDDED_DB_NAME = "hermes_comercial.db";
    private static final String EMBEDDED_DB_PATH = DB_DIR + File.separator + EMBEDDED_DB_NAME;
    
    private static DesktopConfig instance;
    private Properties properties;
    
    private DesktopConfig() {
        initializeDesktopEnvironment();
        loadDesktopConfig();
    }
    
    public static synchronized DesktopConfig getInstance() {
        if (instance == null) {
            instance = new DesktopConfig();
        }
        return instance;
    }
    
    /**
     * Inicializa o ambiente desktop criando diretórios necessários
     */
    private void initializeDesktopEnvironment() {
        try {
            // Criar diretórios se não existirem
            createDirectoryIfNotExists(APP_DATA_DIR);
            createDirectoryIfNotExists(DB_DIR);
            createDirectoryIfNotExists(LOG_DIR);
            createDirectoryIfNotExists(CONFIG_DIR);
            createDirectoryIfNotExists(BACKUP_DIR);
            createDirectoryIfNotExists(REPORTS_DIR);
            
            logger.info("Ambiente desktop inicializado com sucesso");
            logger.info("Diretório de dados: {}", APP_DATA_DIR);
            
        } catch (Exception e) {
            logger.error("Erro ao inicializar ambiente desktop: {}", e.getMessage(), e);
            throw new RuntimeException("Falha na inicialização do ambiente desktop", e);
        }
    }
    
    private void createDirectoryIfNotExists(String path) throws IOException {
        Path dirPath = Paths.get(path);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
            logger.debug("Diretório criado: {}", path);
        }
    }
    
    /**
     * Carrega configurações desktop
     */
    private void loadDesktopConfig() {
        properties = new Properties();
        
        // Configurações padrão para desktop
        properties.setProperty("app.name", APP_NAME);
        properties.setProperty("app.version", "1.2.0");
        properties.setProperty("app.data.dir", APP_DATA_DIR);
        properties.setProperty("app.db.dir", DB_DIR);
        properties.setProperty("app.log.dir", LOG_DIR);
        properties.setProperty("app.config.dir", CONFIG_DIR);
        properties.setProperty("app.backup.dir", BACKUP_DIR);
        properties.setProperty("app.reports.dir", REPORTS_DIR);
        
        // Configurações de banco para desktop
        properties.setProperty("database.embedded.path", EMBEDDED_DB_PATH);
        properties.setProperty("database.embedded.enabled", "true");
        properties.setProperty("database.type", "SQLITE"); // Padrão para desktop
        
        // Configurações de UI para desktop
        properties.setProperty("ui.theme", "system"); // system, light, dark
        properties.setProperty("ui.font.size", "12");
        properties.setProperty("ui.fullscreen", "false");
        properties.setProperty("ui.remember.window.position", "true");
        
        // Configurações de performance para desktop
        properties.setProperty("performance.cache.enabled", "true");
        properties.setProperty("performance.cache.size", "500");
        properties.setProperty("performance.pool.size", "5"); // Menor para desktop
        
        // Configurações de backup
        properties.setProperty("backup.auto.enabled", "true");
        properties.setProperty("backup.auto.interval", "24"); // horas
        properties.setProperty("backup.max.files", "7"); // dias
        
        logger.info("Configurações desktop carregadas");
    }
    
    // Getters para configurações
    
    public String getAppDataDir() {
        return properties.getProperty("app.data.dir");
    }
    
    public String getDatabaseDir() {
        return properties.getProperty("app.db.dir");
    }
    
    public String getLogDir() {
        return properties.getProperty("app.log.dir");
    }
    
    public String getConfigDir() {
        return properties.getProperty("app.config.dir");
    }
    
    public String getBackupDir() {
        return properties.getProperty("app.backup.dir");
    }
    
    public String getReportsDir() {
        return properties.getProperty("app.reports.dir");
    }
    
    public String getEmbeddedDbPath() {
        return properties.getProperty("database.embedded.path");
    }
    
    public boolean isEmbeddedDbEnabled() {
        return Boolean.parseBoolean(properties.getProperty("database.embedded.enabled"));
    }
    
    public String getDatabaseType() {
        return properties.getProperty("database.type");
    }
    
    public String getTheme() {
        return properties.getProperty("ui.theme");
    }
    
    public int getFontSize() {
        return Integer.parseInt(properties.getProperty("ui.font.size"));
    }
    
    public boolean isFullscreen() {
        return Boolean.parseBoolean(properties.getProperty("ui.fullscreen"));
    }
    
    public boolean isRememberWindowPosition() {
        return Boolean.parseBoolean(properties.getProperty("ui.remember.window.position"));
    }
    
    public boolean isCacheEnabled() {
        return Boolean.parseBoolean(properties.getProperty("performance.cache.enabled"));
    }
    
    public int getCacheSize() {
        return Integer.parseInt(properties.getProperty("performance.cache.size"));
    }
    
    public int getPoolSize() {
        return Integer.parseInt(properties.getProperty("performance.pool.size"));
    }
    
    public boolean isAutoBackupEnabled() {
        return Boolean.parseBoolean(properties.getProperty("backup.auto.enabled"));
    }
    
    public int getBackupInterval() {
        return Integer.parseInt(properties.getProperty("backup.auto.interval"));
    }
    
    public int getMaxBackupFiles() {
        return Integer.parseInt(properties.getProperty("backup.max.files"));
    }
    
    // Setters para configurações
    
    public void setTheme(String theme) {
        properties.setProperty("ui.theme", theme);
        logger.info("Tema alterado para: {}", theme);
    }
    
    public void setFontSize(int size) {
        properties.setProperty("ui.font.size", String.valueOf(size));
        logger.info("Tamanho da fonte alterado para: {}", size);
    }
    
    public void setDatabaseType(String type) {
        properties.setProperty("database.type", type);
        logger.info("Tipo de banco alterado para: {}", type);
    }
    
    /**
     * Verifica se é a primeira execução
     */
    public boolean isFirstRun() {
        return !Files.exists(Paths.get(getConfigDir(), "desktop.properties"));
    }
    
    /**
     * Salva configurações desktop
     */
    public void saveConfig() {
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(
                getConfigDir() + File.separator + "desktop.properties");
            properties.store(fos, "Hermes Comercial - Configurações Desktop");
            fos.close();
            logger.info("Configurações desktop salvas");
        } catch (IOException e) {
            logger.error("Erro ao salvar configurações desktop: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Retorna informações do ambiente desktop
     */
    public String getEnvironmentInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== Ambiente Desktop ===\n");
        info.append("Sistema Operacional: ").append(System.getProperty("os.name")).append("\n");
        info.append("Versão Java: ").append(System.getProperty("java.version")).append("\n");
        info.append("Diretório de Dados: ").append(getAppDataDir()).append("\n");
        info.append("Banco de Dados: ").append(getDatabaseType()).append("\n");
        info.append("Caminho do Banco: ").append(getEmbeddedDbPath()).append("\n");
        info.append("Tema: ").append(getTheme()).append("\n");
        info.append("Cache: ").append(isCacheEnabled() ? "Ativado" : "Desativado").append("\n");
        info.append("Backup Automático: ").append(isAutoBackupEnabled() ? "Ativado" : "Desativado").append("\n");
        info.append("========================");
        return info.toString();
    }
}
