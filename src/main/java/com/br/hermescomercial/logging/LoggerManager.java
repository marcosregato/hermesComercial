package com.br.hermescomercial.logging;

import com.br.hermescomercial.config.ConfigurationManager;
import com.br.hermescomercial.event.EventSystem;

import java.util.logging.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Gerenciador de Logging Unificado - Versão Refatorada
 * Centraliza configuração e gerenciamento de logs em todo o sistema
 * Versão 2.0.0 - Logging Pattern Implementation
 */
public class LoggerManager {
    
    private static volatile LoggerManager instance;
    private static final Object lock = new Object();
    
    // Níveis de log customizados
    public enum LogLevel {
        TRACE(Level.FINEST, "TRACE"),
        DEBUG(Level.FINE, "DEBUG"),
        INFO(Level.INFO, "INFO"),
        WARN(Level.WARNING, "WARN"),
        ERROR(Level.SEVERE, "ERROR"),
        FATAL(Level.SEVERE, "FATAL");
        
        private final Level julLevel;
        private final String name;
        
        LogLevel(Level julLevel, String name) {
            this.julLevel = julLevel;
            this.name = name;
        }
        
        public Level getJulLevel() {
            return julLevel;
        }
        
        public String getName() {
            return name;
        }
    }
    
    // Formatters customizados
    public enum LogFormat {
        SIMPLE("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%4$s] %5$s - %6$s%7$s"),
        DETAILED("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL [%4$s] %5$s - %6$s%7$s"),
        JSON("{\"timestamp\":\"%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tL\",\"level\":\"%4$s\",\"logger\":\"%5$s\",\"message\":\"%6$s\",\"thread\":\"%3$s\",\"exception\":\"%7$s\"}");
        
        private final String pattern;
        
        LogFormat(String pattern) {
            this.pattern = pattern;
        }
        
        public String getPattern() {
            return pattern;
        }
    }
    
    // Configurações
    private final ConfigurationManager configManager;
    private final EventSystem eventSystem;
    private final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    private final Map<String, LogLevel> loggerLevels = new ConcurrentHashMap<>();
    private LogLevel globalLevel = LogLevel.INFO;
    private LogFormat format = LogFormat.DETAILED;
    private boolean enableConsole = true;
    private boolean enableFile = true;
    private boolean enableEvents = true;
    
    // File handler
    private FileHandler fileHandler;
    private ConsoleHandler consoleHandler;
    
    // Construtor privado para Singleton
    private LoggerManager() {
        this.configManager = ConfigurationManager.getInstance();
        this.eventSystem = EventSystem.getInstance();
        
        initializeConfiguration();
        setupGlobalHandlers();
    }
    
    /**
     * Obtém instância singleton
     */
    public static LoggerManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LoggerManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Obtém logger para uma classe específica
     */
    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
    
    /**
     * Obtém logger para um nome específico
     */
    public Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, this::createLogger);
    }
    
    /**
     * Cria logger com configurações personalizadas
     */
    private Logger createLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        logger.setLevel(globalLevel.getJulLevel());
        
        // Adicionar handlers
        if (enableConsole) {
            logger.addHandler(consoleHandler);
        }
        
        if (enableFile) {
            logger.addHandler(fileHandler);
        }
        
        // Configurar nível específico se existir
        LogLevel specificLevel = loggerLevels.get(name);
        if (specificLevel != null) {
            logger.setLevel(specificLevel.getJulLevel());
        }
        
        return logger;
    }
    
    /**
     * Inicializa configurações do ConfigurationManager
     */
    private void initializeConfiguration() {
        // Nível global
        String levelConfig = configManager.getString("logging.level", "INFO");
        globalLevel = LogLevel.valueOf(levelConfig.toUpperCase());
        
        // Formato
        String formatConfig = configManager.getString("logging.format", "DETAILED");
        format = LogFormat.valueOf(formatConfig.toUpperCase());
        
        // Habilitar/desabilitar outputs
        enableConsole = configManager.getBoolean("logging.console.enabled", true);
        enableFile = configManager.getBoolean("logging.file.enabled", true);
        enableEvents = configManager.getBoolean("logging.events.enabled", true);
        
        // Configurações de arquivo (lidas mas não utilizadas atualmente)
        // String logFile = configManager.getString("logging.file.path", "logs/hermes-comercial.log");
        // String maxFileSize = configManager.getString("logging.file.max_size", "10MB");
        // int fileCount = configManager.getInteger("logging.file.count", 5);
        
        // Níveis específicos por logger
        Map<String, Object> allConfigs = configManager.getAllConfigurations();
        for (Map.Entry<String, Object> entry : allConfigs.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("logging.level.") && entry.getValue() instanceof String) {
                String loggerName = key.substring("logging.level.".length());
                try {
                    LogLevel level = LogLevel.valueOf(((String) entry.getValue()).toUpperCase());
                    loggerLevels.put(loggerName, level);
                } catch (IllegalArgumentException e) {
                    System.err.println("Nível de log inválido para " + loggerName + ": " + entry.getValue());
                }
            }
        }
    }
    
    /**
     * Configura handlers globais
     */
    private void setupGlobalHandlers() {
        // Console Handler
        if (enableConsole) {
            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new HermesFormatter(format));
            consoleHandler.setLevel(globalLevel.getJulLevel());
        }
        
        // File Handler
        if (enableFile) {
            try {
                String logFile = configManager.getString("logging.file.path", "logs/hermes-comercial.log");
                String maxFileSize = configManager.getString("logging.file.max_size", "10MB");
                int fileCount = configManager.getInteger("logging.file.count", 5);
                
                // Criar diretório de logs se não existir
                java.io.File logDir = new java.io.File(logFile).getParentFile();
                if (logDir != null && !logDir.exists()) {
                    logDir.mkdirs();
                }
                
                fileHandler = new FileHandler(logFile, parseFileSize(maxFileSize), fileCount, true);
                fileHandler.setFormatter(new HermesFormatter(format));
                fileHandler.setLevel(globalLevel.getJulLevel());
                
            } catch (IOException e) {
                System.err.println("Erro ao configurar file handler: " + e.getMessage());
            }
        }
    }
    
    /**
     * Parse do tamanho do arquivo
     */
    private int parseFileSize(String sizeStr) {
        sizeStr = sizeStr.toUpperCase();
        if (sizeStr.endsWith("KB")) {
            return Integer.parseInt(sizeStr.substring(0, sizeStr.length() - 2)) * 1024;
        } else if (sizeStr.endsWith("MB")) {
            return Integer.parseInt(sizeStr.substring(0, sizeStr.length() - 2)) * 1024 * 1024;
        } else if (sizeStr.endsWith("GB")) {
            return Integer.parseInt(sizeStr.substring(0, sizeStr.length() - 2)) * 1024 * 1024 * 1024;
        } else {
            return Integer.parseInt(sizeStr);
        }
    }
    
    /**
     * Configura nível para um logger específico
     */
    public void setLoggerLevel(String loggerName, LogLevel level) {
        loggerLevels.put(loggerName, level);
        Logger logger = loggers.get(loggerName);
        if (logger != null) {
            logger.setLevel(level.getJulLevel());
        }
    }
    
    /**
     * Configura nível global
     */
    public void setGlobalLevel(LogLevel level) {
        globalLevel = level;
        
        // Atualizar todos os loggers
        for (Logger logger : loggers.values()) {
            logger.setLevel(level.getJulLevel());
        }
        
        // Atualizar handlers
        if (consoleHandler != null) {
            consoleHandler.setLevel(level.getJulLevel());
        }
        if (fileHandler != null) {
            fileHandler.setLevel(level.getJulLevel());
        }
    }
    
    /**
     * Configura formato de log
     */
    public void setFormat(LogFormat format) {
        this.format = format;
        
        // Reconfigurar formatters
        HermesFormatter formatter = new HermesFormatter(format);
        if (consoleHandler != null) {
            consoleHandler.setFormatter(formatter);
        }
        if (fileHandler != null) {
            fileHandler.setFormatter(formatter);
        }
    }
    
    /**
     * Log de auditoria
     */
    public void audit(String action, String user, String details) {
        Logger auditLogger = getLogger("AUDIT");
        auditLogger.info(String.format("AUDIT: %s | User: %s | %s", action, user, details));
        
        // Publicar evento de auditoria
        if (enableEvents) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("action", action);
            metadata.put("user", user);
            metadata.put("details", details);
            metadata.put("timestamp", LocalDateTime.now());
            
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "audit.log", 
                metadata,
                "LoggerManager"
            ));
        }
    }
    
    /**
     * Log de performance
     */
    public void performance(String operation, long durationMs, Map<String, Object> context) {
        Logger perfLogger = getLogger("PERFORMANCE");
        String contextStr = context != null ? context.toString() : "";
        perfLogger.info(String.format("PERF: %s | Duration: %dms | %s", operation, durationMs, contextStr));
        
        // Publicar evento de performance
        if (enableEvents) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("operation", operation);
            metadata.put("durationMs", durationMs);
            metadata.put("context", context);
            metadata.put("timestamp", LocalDateTime.now());
            
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "performance.log", 
                metadata,
                "LoggerManager"
            ));
        }
    }
    
    /**
     * Log de erro com contexto
     */
    public void error(String operation, Throwable error, Map<String, Object> context) {
        Logger errorLogger = getLogger("ERROR");
        String contextStr = context != null ? context.toString() : "";
        errorLogger.severe(String.format("ERROR: %s | %s | %s", operation, error.getMessage(), contextStr));
        
        // Publicar evento de erro
        if (enableEvents) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("operation", operation);
            metadata.put("errorType", error.getClass().getSimpleName());
            metadata.put("errorMessage", error.getMessage());
            metadata.put("context", context);
            metadata.put("timestamp", LocalDateTime.now());
            
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "error.log", 
                metadata,
                "LoggerManager"
            ));
        }
    }
    
    /**
     * Log de informação
     */
    public void info(String operation, String message) {
        Logger infoLogger = getLogger("INFO");
        infoLogger.info(String.format("INFO: %s | %s", operation, message));
    }
    
    /**
     * Log de aviso
     */
    public void warn(String operation, String message) {
        Logger warnLogger = getLogger("WARN");
        warnLogger.warning(String.format("WARN: %s | %s", operation, message));
    }
    
    /**
     * Formatter customizado para logs Hermes
     */
    private static class HermesFormatter extends Formatter {
        
        private final LogFormat format;
        
        public HermesFormatter(LogFormat format) {
            this.format = format;
        }
        
        @Override
        public String format(LogRecord record) {
            LocalDateTime timestamp = LocalDateTime.now();
            String threadName = Thread.currentThread().getName();
            String loggerName = record.getLoggerName();
            String level = record.getLevel().getName();
            String message = record.getMessage();
            
            // Obter stack trace se houver exceção
            String exception = "";
            if (record.getThrown() != null) {
                exception = "\n" + getStackTrace(record.getThrown());
            }
            
            // Formatar baseado no tipo
            switch (format) {
                case SIMPLE:
                    return String.format(format.getPattern(), 
                        timestamp, threadName, loggerName, level, message, exception);
                case DETAILED:
                    return String.format(format.getPattern(), 
                        timestamp, threadName, loggerName, level, message, exception);
                case JSON:
                    // Escapar aspas para JSON
                    String escapedMessage = message.replace("\"", "\\\"");
                    String escapedException = exception.replace("\"", "\\\"").replace("\n", "\\n");
                    return String.format(format.getPattern(), 
                        timestamp, threadName, loggerName, level, escapedMessage, escapedException);
                default:
                    return String.format("%s [%s] %s - %s%s", 
                        timestamp, level, loggerName, message, exception);
            }
        }
        
        private String getStackTrace(Throwable throwable) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
    
    /**
     * Obtém estatísticas de logging
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLoggers", loggers.size());
        stats.put("globalLevel", globalLevel.getName());
        stats.put("format", format.name());
        stats.put("consoleEnabled", enableConsole);
        stats.put("fileEnabled", enableFile);
        stats.put("eventsEnabled", enableEvents);
        stats.put("specificLevels", loggerLevels.size());
        return stats;
    }
    
    /**
     * Shutdown do logger manager
     */
    public void shutdown() {
        if (fileHandler != null) {
            fileHandler.close();
        }
        if (consoleHandler != null) {
            consoleHandler.close();
        }
    }
}
