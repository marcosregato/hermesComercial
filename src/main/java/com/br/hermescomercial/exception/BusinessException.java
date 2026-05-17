package com.br.hermescomercial.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Exceção de negócio para erros de regras de negócio - Versão Refatorada
 * Diferencia erros de negócio (regras, validações) de erros de sistema
 * Versão 2.0.0 - Exception Handling Enhancement
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    private final Map<String, Object> context;
    private final LocalDateTime timestamp;
    private final String operation;
    
    public BusinessException(String message) {
        this("BIZ_001", message, null, null);
    }
    
    public BusinessException(String message, Throwable cause) {
        this("BIZ_001", message, cause, null);
    }
    
    public BusinessException(String errorCode, String message) {
        this(errorCode, message, null, null);
    }
    
    public BusinessException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, null);
    }
    
    public BusinessException(String errorCode, String message, String operation) {
        this(errorCode, message, null, operation);
    }
    
    public BusinessException(String errorCode, String message, Throwable cause, String operation) {
        super(message, cause);
        this.errorCode = errorCode;
        this.operation = operation;
        this.context = new HashMap<>();
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Adiciona informação de contexto à exceção
     */
    public BusinessException addContext(String key, Object value) {
        context.put(key, value);
        return this;
    }
    
    /**
     * Adiciona múltiplos contextos
     */
    public BusinessException addContext(Map<String, Object> context) {
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
     * Verifica se é erro de validação
     */
    public boolean isValidationError() {
        return errorCode.startsWith("BIZ_VAL_");
    }
    
    /**
     * Verifica se é erro de regra de negócio
     */
    public boolean isBusinessRuleError() {
        return errorCode.startsWith("BIZ_RULE_");
    }
    
    /**
     * Verifica se é erro de autorização
     */
    public boolean isAuthorizationError() {
        return errorCode.startsWith("BIZ_AUTH_");
    }
    
    /**
     * Factory para exceção de validação
     */
    public static BusinessException validationError(String field, String message) {
        return new BusinessException("BIZ_VAL_001", 
            "Erro de validação em " + field + ": " + message)
            .addContext("field", field)
            .addContext("validationType", "field");
    }
    
    /**
     * Factory para exceção de regra de negócio
     */
    public static BusinessException businessRuleError(String rule, String message) {
        return new BusinessException("BIZ_RULE_001", 
            "Violação de regra de negócio: " + message)
            .addContext("rule", rule)
            .addContext("ruleType", "business");
    }
    
    /**
     * Factory para exceção de autorização
     */
    public static BusinessException authorizationError(String operation, String user) {
        return new BusinessException("BIZ_AUTH_001", 
            "Usuário não autorizado para operação: " + operation)
            .addContext("operation", operation)
            .addContext("user", user)
            .addContext("authType", "authorization");
    }
    
    /**
     * Factory para exceção de entidade não encontrada
     */
    public static BusinessException entityNotFound(String entityType, Object id) {
        return new BusinessException("BIZ_002", 
            entityType + " não encontrado(a) com ID: " + id)
            .addContext("entityType", entityType)
            .addContext("entityId", id);
    }
    
    /**
     * Factory para exceção de duplicidade
     */
    public static BusinessException duplicateEntity(String entityType, String field, Object value) {
        return new BusinessException("BIZ_003", 
            entityType + " já existe com " + field + ": " + value)
            .addContext("entityType", entityType)
            .addContext("duplicateField", field)
            .addContext("duplicateValue", value);
    }
    
    @Override
    public String toString() {
        return String.format("BusinessException{errorCode='%s', message='%s', operation='%s', timestamp=%s}", 
            errorCode, getMessage(), operation, timestamp);
    }
}
