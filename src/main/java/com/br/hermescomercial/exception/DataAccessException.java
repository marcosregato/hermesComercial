package com.br.hermescomercial.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Exceção de acesso a dados para erros de banco de dados
 * Segue padrões de arquitetura definidos em docs/ARQUITETURA.md
 */
public class DataAccessException extends RuntimeException {
    
    private final String errorCode;
    private final Map<String, Object> context;
    private final LocalDateTime timestamp;
    private final String operation;
    
    public DataAccessException(String message) {
        this("DATA_001", message, null, null);
    }
    
    public DataAccessException(String message, Throwable cause) {
        this("DATA_001", message, cause, null);
    }
    
    public DataAccessException(String errorCode, String message) {
        this(errorCode, message, null, null);
    }
    
    public DataAccessException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, null);
    }
    
    public DataAccessException(String errorCode, String message, String operation) {
        this(errorCode, message, null, operation);
    }
    
    public DataAccessException(String errorCode, String message, Throwable cause, String operation) {
        super(message, cause);
        this.errorCode = errorCode;
        this.operation = operation;
        this.context = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Adiciona informação de contexto à exceção
     */
    public DataAccessException addContext(String key, Object value) {
        context.put(key, value);
        return this;
    }
    
    /**
     * Adiciona múltiplos contextos
     */
    public DataAccessException addContext(Map<String, Object> context) {
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
     * Factory para exceção de query SQL
     */
    public static DataAccessException queryError(String query, Throwable cause) {
        return new DataAccessException("DATA_QUERY_001", 
            "Erro ao executar query SQL", cause, "SQL_QUERY")
            .addContext("query", query)
            .addContext("errorType", "query");
    }
    
    /**
     * Factory para exceção de conexão
     */
    public static DataAccessException connectionError(String dataSource, Throwable cause) {
        return new DataAccessException("DATA_CONN_001", 
            "Erro ao conectar ao banco de dados", cause, "CONNECTION")
            .addContext("dataSource", dataSource)
            .addContext("errorType", "connection");
    }
    
    /**
     * Factory para exceção de transação
     */
    public static DataAccessException transactionError(String operation, Throwable cause) {
        return new DataAccessException("DATA_TX_001", 
            "Erro na transação: " + operation, cause, "TRANSACTION")
            .addContext("operation", operation)
            .addContext("errorType", "transaction");
    }
    
    /**
     * Factory para exceção de constraint
     */
    public static DataAccessException constraintError(String constraintName, String tableName) {
        return new DataAccessException("DATA_CONST_001", 
            "Violação de constraint: " + constraintName + " na tabela: " + tableName)
            .addContext("constraintName", constraintName)
            .addContext("tableName", tableName)
            .addContext("errorType", "constraint");
    }
    
    @Override
    public String toString() {
        return String.format("DataAccessException{errorCode='%s', message='%s', operation='%s', timestamp=%s}", 
            errorCode, getMessage(), operation, timestamp);
    }
}
