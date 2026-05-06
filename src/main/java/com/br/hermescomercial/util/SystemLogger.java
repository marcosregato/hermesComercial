package com.br.hermescomercial.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Sistema de Logging Simples e Eficiente para Hermes Comercial PDV
 * Versão 1.0 - Logging Centralizado com Arquivos Separados
 */
public class SystemLogger {
    
    private static final ReentrantLock logLock = new ReentrantLock();
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final String LOG_DIR = "logs";
    
    // Níveis de Log
    public enum LogLevel {
        INFO("INFO"),
        WARN("WARN"), 
        ERROR("ERROR"),
        DEBUG("DEBUG"),
        AUDIT("AUDIT");
        
        private final String label;
        
        LogLevel(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    // Módulos do Sistema
    public enum Module {
        AUTH("AUTH"),
        DATABASE("DATABASE"),
        ERP("ERP"),
        PDV("PDV"),
        UI("UI"),
        SYSTEM("SYSTEM"),
        SECURITY("SECURITY");
        
        private final String label;
        
        Module(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    // Arquivos de Log
    private static final String[] LOG_FILES = {
        "app.log",        // Logs gerais
        "auth.log",       // Autenticação
        "database.log",   // Banco de dados
        "error.log",      // Erros
        "audit.log",      // Auditoria
        "erp.log",        // ERP
        "pdv.log",        // PDV
        "ui.log"          // Interface
    };
    
    static {
        // Criar diretório de logs
        createLogDirectory();
    }
    
    private static void createLogDirectory() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("📁 Diretório de logs criado: " + LOG_DIR);
        }
    }
    
    /**
     * Escreve uma mensagem de log
     */
    public static void log(LogLevel level, Module module, String message) {
        log(level, module, message, null);
    }
    
    /**
     * Escreve uma mensagem de log com exceção
     */
    public static void log(LogLevel level, Module module, String message, Throwable throwable) {
        logLock.lock();
        try {
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            String logEntry = String.format("[%s] [%s] [%s] %s", 
                timestamp, level.getLabel(), module.getLabel(), message);
            
            // Escrever no console
            writeToConsole(level, logEntry);
            
            // Escrever no arquivo
            String logFile = getLogFileForLevel(level, module);
            writeToFile(logFile, logEntry);
            
            // Escrever stack trace se houver exceção
            if (throwable != null) {
                writeStackTraceToFile(logFile, throwable);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao escrever log: " + e.getMessage());
        } finally {
            logLock.unlock();
        }
    }
    
    /**
     * Métodos de conveniência por módulo
     */
    public static void auth(String message) {
        log(LogLevel.INFO, Module.AUTH, message);
    }
    
    public static void database(String message) {
        log(LogLevel.INFO, Module.DATABASE, message);
    }
    
    public static void error(String message) {
        log(LogLevel.ERROR, Module.SYSTEM, message);
    }
    
    public static void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, Module.SYSTEM, message, throwable);
    }
    
    public static void warn(String message) {
        log(LogLevel.WARN, Module.SYSTEM, message);
    }
    
    public static void info(String message) {
        log(LogLevel.INFO, Module.SYSTEM, message);
    }
    
    public static void debug(String message) {
        log(LogLevel.DEBUG, Module.SYSTEM, message);
    }
    
    public static void audit(String message) {
        log(LogLevel.AUDIT, Module.SECURITY, message);
    }
    
    public static void erp(String message) {
        log(LogLevel.INFO, Module.ERP, message);
    }
    
    public static void pdv(String message) {
        log(LogLevel.INFO, Module.PDV, message);
    }
    
    public static void ui(String message) {
        log(LogLevel.INFO, Module.UI, message);
    }
    
    // Logs específicos de login
    public static void loginSuccess(String usuario) {
        audit("LOGIN_SUCCESS: " + usuario);
        auth("Login bem-sucedido: " + usuario);
    }
    
    public static void loginFailure(String usuario, String motivo) {
        audit("LOGIN_FAILURE: " + usuario + " - " + motivo);
        auth("Tentativa de login falhou: " + usuario + " - " + motivo);
    }
    
    // Logs de banco de dados
    public static void databaseConnection(String status) {
        database("Conexão com banco: " + status);
    }
    
    public static void databaseQuery(String sql, long duration) {
        debug("Query executada: " + sql + " (" + duration + "ms)");
    }
    
    // Logs de sistema
    public static void systemStartup() {
        info("Sistema Hermes Comercial PDV iniciado");
        audit("SYSTEM_STARTUP");
    }
    
    public static void systemShutdown() {
        info("Sistema Hermes Comercial PDV encerrado");
        audit("SYSTEM_SHUTDOWN");
    }
    
    // Logs de operações
    public static void operation(String operation, String module, String details) {
        audit("OPERATION: " + operation + " | MODULE: " + module + " | DETAILS: " + details);
    }
    
    private static void writeToConsole(LogLevel level, String logEntry) {
        if (level == LogLevel.ERROR) {
            System.err.println(logEntry);
        } else {
            System.out.println(logEntry);
        }
    }
    
    private static String getLogFileForLevel(LogLevel level, Module module) {
        switch (level) {
            case ERROR:
                return LOG_DIR + "/error.log";
            case AUDIT:
                return LOG_DIR + "/audit.log";
            case DEBUG:
                return LOG_DIR + "/app.log";
            default:
                // Por módulo
                switch (module) {
                    case AUTH:
                        return LOG_DIR + "/auth.log";
                    case DATABASE:
                        return LOG_DIR + "/database.log";
                    case ERP:
                        return LOG_DIR + "/erp.log";
                    case PDV:
                        return LOG_DIR + "/pdv.log";
                    case UI:
                        return LOG_DIR + "/ui.log";
                    default:
                        return LOG_DIR + "/app.log";
                }
        }
    }
    
    private static void writeToFile(String fileName, String message) {
        try {
            File file = new File(fileName);
            File parentDir = file.getParentFile();
            
            // Criar diretório se não existir
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                
                out.println(message);
                out.flush();
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo " + fileName + ": " + e.getMessage());
        }
    }
    
    private static void writeStackTraceToFile(String fileName, Throwable throwable) {
        try {
            File file = new File(fileName);
            File parentDir = file.getParentFile();
            
            // Criar diretório se não existir
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                
                out.println("Stack Trace:");
                throwable.printStackTrace(out);
                out.println(); // Linha em branco após stack trace
                out.flush();
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao escrever stack trace no arquivo " + fileName + ": " + e.getMessage());
        }
    }
    
    /**
     * Limpa logs antigos (opcional)
     */
    public static void cleanupOldLogs() {
        File logDir = new File(LOG_DIR);
        if (logDir.exists() && logDir.isDirectory()) {
            File[] files = logDir.listFiles();
            if (files != null) {
                long currentTime = System.currentTimeMillis();
                long sevenDaysAgo = currentTime - (7 * 24 * 60 * 60 * 1000); // 7 dias
                
                for (File file : files) {
                    if (file.lastModified() < sevenDaysAgo) {
                        if (file.delete()) {
                            info("Log antigo removido: " + file.getName());
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Obtém estatísticas dos logs
     */
    public static String getLogStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 Estatísticas de Logs - Hermes Comercial PDV\n");
        stats.append("=".repeat(50)).append("\n");
        
        File logDir = new File(LOG_DIR);
        if (logDir.exists() && logDir.isDirectory()) {
            File[] files = logDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    long size = file.length();
                    String sizeStr = size < 1024 ? size + " B" : 
                                    size < 1024 * 1024 ? (size / 1024) + " KB" : 
                                    (size / (1024 * 1024)) + " MB";
                    
                    stats.append(String.format("📄 %-15s: %8s\n", 
                        file.getName(), sizeStr));
                }
            }
        }
        
        return stats.toString();
    }
}
