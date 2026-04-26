package com.br.hermescomercial.util;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Utilitário de Logging para o Sistema Hermes Comercial PDV
 * Fornece funcionalidades completas de registro de eventos
 */
public class LoggerUtil {
    
    private static final Lock logLock = new ReentrantLock();
    private static final String LOG_DIR = System.getProperty("user.home") + "/hermes-pdv-logs";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // Níveis de Log
    public enum LogLevel {
        DEBUG("DEBUG"),
        INFO("INFO"),
        WARNING("WARNING"),
        ERROR("ERROR"),
        CRITICAL("CRITICAL");
        
        private final String label;
        
        LogLevel(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    /**
     * Registra mensagem de nível DEBUG
     */
    public static void debug(String message) {
        log(LogLevel.DEBUG, message, null);
    }
    
    /**
     * Registra mensagem de nível DEBUG com exceção
     */
    public static void debug(String message, Throwable throwable) {
        log(LogLevel.DEBUG, message, throwable);
    }
    
    /**
     * Registra mensagem de nível INFO
     */
    public static void info(String message) {
        log(LogLevel.INFO, message, null);
    }
    
    /**
     * Registra mensagem de nível INFO com exceção
     */
    public static void info(String message, Throwable throwable) {
        log(LogLevel.INFO, message, throwable);
    }
    
    /**
     * Registra mensagem de nível WARNING
     */
    public static void warning(String message) {
        log(LogLevel.WARNING, message, null);
    }
    
    /**
     * Registra mensagem de nível WARNING com exceção
     */
    public static void warning(String message, Throwable throwable) {
        log(LogLevel.WARNING, message, throwable);
    }
    
    /**
     * Registra mensagem de nível ERROR
     */
    public static void error(String message) {
        log(LogLevel.ERROR, message, null);
    }
    
    /**
     * Registra mensagem de nível ERROR com exceção
     */
    public static void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message, throwable);
    }
    
    /**
     * Registra mensagem de nível CRITICAL
     */
    public static void critical(String message) {
        log(LogLevel.CRITICAL, message, null);
    }
    
    /**
     * Registra mensagem de nível CRITICAL com exceção
     */
    public static void critical(String message, Throwable throwable) {
        log(LogLevel.CRITICAL, message, throwable);
    }
    
    /**
     * Registra operação de usuário
     */
    public static void logUserAction(String usuario, String acao, String detalhes) {
        String message = String.format("USER_ACTION: [%s] %s - %s", usuario, acao, detalhes);
        log(LogLevel.INFO, message, null);
    }
    
    /**
     * Registra operação de sistema
     */
    public static void logSystemOperation(String operacao, String resultado) {
        String message = String.format("SYSTEM_OPERATION: %s - %s", operacao, resultado);
        log(LogLevel.INFO, message, null);
    }
    
    /**
     * Registra erro de negócio
     */
    public static void logBusinessError(String modulo, String operacao, String erro) {
        String message = String.format("BUSINESS_ERROR: [%s] %s - %s", modulo, operacao, erro);
        log(LogLevel.ERROR, message, null);
    }
    
    /**
     * Registra transação financeira
     */
    public static void logFinancialTransaction(String tipo, BigDecimal valor, String detalhes) {
        String message = String.format("FINANCIAL: %s R$ %.2f - %s", tipo, valor, detalhes);
        log(LogLevel.INFO, message, null);
    }
    
    /**
     * Método principal de logging
     */
    private static void log(LogLevel level, String message, Throwable throwable) {
        logLock.lock();
        try {
            // Criar diretório de logs se não existir
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // Nome do arquivo baseado na data
            String fileName = "hermes-pdv-" + LocalDateTime.now().format(FILE_DATE_FORMAT) + ".log";
            File logFile = new File(logDir, fileName);
            
            // Formatar mensagem de log
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String threadName = Thread.currentThread().getName();
            String formattedMessage = String.format("[%s] [%s] [%s] %s", 
                timestamp, level.getLabel(), threadName, message);
            
            // Escrever no arquivo
            try (FileWriter fw = new FileWriter(logFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {
                
                pw.println(formattedMessage);
                
                // Se houver exceção, escrever stack trace
                if (throwable != null) {
                    pw.println("Exception Stack Trace:");
                    throwable.printStackTrace(pw);
                    pw.println("--- End of Stack Trace ---");
                }
                
                pw.flush();
            }
            
            // Para erros críticos, também escrever em arquivo separado
            if (level == LogLevel.CRITICAL || level == LogLevel.ERROR) {
                writeErrorLog(level, formattedMessage, throwable);
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao escrever log: " + e.getMessage());
            e.printStackTrace();
        } finally {
            logLock.unlock();
        }
    }
    
    /**
     * Escreve logs de erro em arquivo separado
     */
    private static void writeErrorLog(LogLevel level, String message, Throwable throwable) {
        try {
            String fileName = "hermes-pdv-errors-" + LocalDateTime.now().format(FILE_DATE_FORMAT) + ".log";
            File errorFile = new File(LOG_DIR, fileName);
            
            try (FileWriter fw = new FileWriter(errorFile, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter pw = new PrintWriter(bw)) {
                
                pw.println(message);
                
                if (throwable != null) {
                    pw.println("Exception Stack Trace:");
                    throwable.printStackTrace(pw);
                    pw.println("--- End of Stack Trace ---");
                }
                
                pw.flush();
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever log de erros: " + e.getMessage());
        }
    }
    
    /**
     * Limpa logs antigos (mantém apenas últimos 30 dias)
     */
    public static void cleanOldLogs() {
        logLock.lock();
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) return;
            
            File[] logFiles = logDir.listFiles();
            if (logFiles == null) return;
            
            long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
            
            for (File file : logFiles) {
                if (file.lastModified() < thirtyDaysAgo) {
                    if (file.delete()) {
                        info("Log antigo removido: " + file.getName());
                    }
                }
            }
        } finally {
            logLock.unlock();
        }
    }
    
    /**
     * Obtém estatísticas dos logs
     */
    public static String getLogStatistics() {
        File logDir = new File(LOG_DIR);
        if (!logDir.exists()) {
            return "Nenhum log encontrado.";
        }
        
        File[] logFiles = logDir.listFiles();
        if (logFiles == null) {
            return "Nenhum arquivo de log encontrado.";
        }
        
        long totalSize = 0;
        int fileCount = logFiles.length;
        
        for (File file : logFiles) {
            totalSize += file.length();
        }
        
        return String.format("Estatísticas dos Logs:\n" +
                           "- Total de arquivos: %d\n" +
                           "- Tamanho total: %.2f MB\n" +
                           "- Diretório: %s",
                           fileCount, totalSize / (1024.0 * 1024.0), LOG_DIR);
    }
    
    /**
     * Inicializa o sistema de logs
     */
    public static void initialize() {
        info("=== SISTEMA HERMES COMERCIAL PDV INICIADO ===");
        info("Versão: 2.0 - Premium Swing");
        info("Data/Hora: " + LocalDateTime.now().format(TIMESTAMP_FORMAT));
        info("Usuário: " + System.getProperty("user.name"));
        info("Sistema Operacional: " + System.getProperty("os.name"));
        info("Java Version: " + System.getProperty("java.version"));
        info("Diretório de Logs: " + LOG_DIR);
        info("===============================================");
        
        // Limpar logs antigos
        cleanOldLogs();
    }
    
    /**
     * Finaliza o sistema de logs
     */
    public static void shutdown() {
        info("=== SISTEMA HERMES COMERCIAL PDV ENCERRADO ===");
        info("Data/Hora: " + LocalDateTime.now().format(TIMESTAMP_FORMAT));
        info("===============================================");
    }
}
