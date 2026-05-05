package com.br.hermescomercial.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Exceção de sistema para erros técnicos e infraestrutura - Versão Refatorada
 * Diferencia de BusinessException que é para regras de negócio
 * Versão 2.0.0 - Exception Handling Enhancement
 */
public class SystemException extends RuntimeException {
    
    private final String errorCode;
    private final Map<String, Object> context;
    private final LocalDateTime timestamp;
    private final String operation;
    private final String component;
    
    public SystemException(String message) {
        this("SYS_001", message, null, null, null);
    }
    
    public SystemException(String message, Throwable cause) {
        this("SYS_001", message, cause, null, null);
    }
    
    public SystemException(String errorCode, String message) {
        this(errorCode, message, null, null, null);
    }
    
    public SystemException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, null, null);
    }
    
    public SystemException(String errorCode, String message, String operation) {
        this(errorCode, message, null, operation, null);
    }
    
    public SystemException(String errorCode, String message, Throwable cause, String operation) {
        this(errorCode, message, cause, operation, null);
    }
    
    public SystemException(String errorCode, String message, Throwable cause, String operation, String component) {
        super(message, cause);
        this.errorCode = errorCode;
        this.operation = operation;
        this.component = component;
        this.context = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Adiciona informação de contexto à exceção
     */
    public SystemException addContext(String key, Object value) {
        context.put(key, value);
        return this;
    }
    
    /**
     * Adiciona múltiplos contextos
     */
    public SystemException addContext(Map<String, Object> context) {
        this.context.putAll(context);
        return this;
    }
    
    /**
     * Obtém código de erro
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Obtém contexto da exceção
     */
    public Map<String, Object> getContext() {
        return new HashMap<>(context);
    }
    
    /**
     * Obtém timestamp da exceção
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * Obtém operação que falhou
     */
    public String getOperation() {
        return operation;
    }
    
    /**
     * Obtém componente que falhou
     */
    public String getComponent() {
        return component;
    }
    
    /**
     * Verifica se é erro de banco de dados
     */
    public boolean isDatabaseError() {
        return errorCode.startsWith("SYS_DB_");
    }
    
    /**
     * Verifica se é erro de rede
     */
    public boolean isNetworkError() {
        return errorCode.startsWith("SYS_NET_");
    }
    
    /**
     * Verifica se é erro de I/O
     */
    public boolean isIOError() {
        return errorCode.startsWith("SYS_IO_");
    }
    
    /**
     * Verifica se é erro de configuração
     */
    public boolean isConfigurationError() {
        return errorCode.startsWith("SYS_CONF_");
    }
    
    /**
     * Verifica se é erro de memória
     */
    public boolean isMemoryError() {
        return errorCode.startsWith("SYS_MEM_");
    }
    
    /**
     * Factory para exceção de banco de dados
     */
    public static SystemException databaseError(String operation, Throwable cause) {
        return new SystemException("SYS_DB_001", 
            "Erro de banco de dados na operação: " + operation, cause, operation, "Database")
            .addContext("operation", operation)
            .addContext("component", "Database")
            .addContext("errorType", "database");
    }
    
    /**
     * Factory para exceção de rede
     */
    public static SystemException networkError(String operation, String endpoint, Throwable cause) {
        return new SystemException("SYS_NET_001", 
            "Erro de rede na operação: " + operation, cause, operation, "Network")
            .addContext("operation", operation)
            .addContext("endpoint", endpoint)
            .addContext("component", "Network")
            .addContext("errorType", "network");
    }
    
    /**
     * Factory para exceção de I/O
     */
    public static SystemException ioError(String operation, String resource, Throwable cause) {
        return new SystemException("SYS_IO_001", 
            "Erro de I/O na operação: " + operation + " com recurso: " + resource, cause, operation, "IO")
            .addContext("operation", operation)
            .addContext("resource", resource)
            .addContext("component", "IO")
            .addContext("errorType", "io");
    }
    
    /**
     * Factory para exceção de configuração
     */
    public static SystemException configurationError(String configKey, String value) {
        return new SystemException("SYS_CONF_001", 
            "Erro de configuração na chave: " + configKey + " com valor: " + value)
            .addContext("configKey", configKey)
            .addContext("configValue", value)
            .addContext("component", "Configuration")
            .addContext("errorType", "configuration");
    }
    
    /**
     * Factory para exceção de memória
     */
    public static SystemException memoryError(String operation, long requiredMemory) {
        return new SystemException("SYS_MEM_001", 
            "Erro de memória na operação: " + operation + " - memória requerida: " + requiredMemory + " bytes")
            .addContext("operation", operation)
            .addContext("requiredMemory", requiredMemory)
            .addContext("component", "Memory")
            .addContext("errorType", "memory");
    }
    
    /**
     * Factory para exceção de timeout
     */
    public static SystemException timeoutError(String operation, long timeoutMs) {
        return new SystemException("SYS_TIMEOUT_001", 
            "Timeout na operação: " + operation + " - timeout: " + timeoutMs + "ms")
            .addContext("operation", operation)
            .addContext("timeoutMs", timeoutMs)
            .addContext("component", "Timeout")
            .addContext("errorType", "timeout");
    }
    
    /**
     * Factory para exceção de serviço indisponível
     */
    public static SystemException serviceUnavailable(String serviceName) {
        return new SystemException("SYS_SVC_001", 
            "Serviço indisponível: " + serviceName)
            .addContext("serviceName", serviceName)
            .addContext("component", "Service")
            .addContext("errorType", "service");
    }
    
    /**
     * Construtor para criar SystemException a partir de uma exceção genérica
     * @param operation nome da operação que falhou
     * @param cause exceção original
     * @return SystemException com mensagem formatada
     */
    public static SystemException from(String operation, Throwable cause) {
        String message = String.format("Erro interno do sistema na operação: %s", operation);
        return new SystemException("SYS_001", message, cause, operation, "System")
            .addContext("operation", operation)
            .addContext("originalException", cause.getClass().getSimpleName())
            .addContext("errorType", "system");
    }
    
    @Override
    public String toString() {
        return String.format("SystemException{errorCode='%s', message='%s', operation='%s', component='%s', timestamp=%s}", 
            errorCode, getMessage(), operation, component, timestamp);
    }
}
