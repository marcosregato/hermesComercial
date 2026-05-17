package com.br.hermescomercial.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * Testes para exceções personalizadas
 * Verifica funcionamento de BusinessException, ValidationException, DataAccessException e SystemException
 */
public class ExceptionTest {
    
    @Test
    @DisplayName("BusinessException - Deve criar com mensagem simples")
    void testBusinessExceptionSimpleMessage() {
        BusinessException exception = new BusinessException("Erro de negócio");
        
        assertEquals("Erro de negócio", exception.getMessage());
        assertEquals("BIZ_001", exception.getErrorCode());
        assertNotNull(exception.getTimestamp());
    }
    
    @Test
    @DisplayName("BusinessException - Deve criar com código de erro customizado")
    void testBusinessExceptionCustomErrorCode() {
        BusinessException exception = new BusinessException("BIZ_002", "Erro customizado");
        
        assertEquals("BIZ_002", exception.getErrorCode());
        assertEquals("Erro customizado", exception.getMessage());
    }
    
    @Test
    @DisplayName("BusinessException - Deve criar com causa")
    void testBusinessExceptionWithCause() {
        Throwable cause = new RuntimeException("Causa original");
        BusinessException exception = new BusinessException("Erro", cause);
        
        assertEquals("Erro", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("BusinessException - Deve adicionar contexto")
    void testBusinessExceptionAddContext() {
        BusinessException exception = new BusinessException("Erro")
            .addContext("chave1", "valor1")
            .addContext("chave2", 123);
        
        Map<String, Object> context = exception.getContext();
        assertEquals(2, context.size());
        assertEquals("valor1", context.get("chave1"));
        assertEquals(123, context.get("chave2"));
    }
    
    @Test
    @DisplayName("BusinessException - Deve identificar erro de validação")
    void testBusinessExceptionValidationError() {
        BusinessException exception = BusinessException.validationError("nome", "Nome inválido");
        
        assertTrue(exception.isValidationError());
        assertEquals("BIZ_VAL_001", exception.getErrorCode());
        assertTrue(exception.getContext().containsKey("field"));
    }
    
    @Test
    @DisplayName("BusinessException - Deve identificar erro de regra de negócio")
    void testBusinessExceptionRuleError() {
        BusinessException exception = BusinessException.businessRuleError("estoque", "Estoque insuficiente");
        
        assertTrue(exception.isBusinessRuleError());
        assertEquals("BIZ_RULE_001", exception.getErrorCode());
        assertTrue(exception.getContext().containsKey("rule"));
    }
    
    @Test
    @DisplayName("BusinessException - Deve identificar erro de autorização")
    void testBusinessExceptionAuthorizationError() {
        BusinessException exception = BusinessException.authorizationError("DELETE", "usuario1");
        
        assertTrue(exception.isAuthorizationError());
        assertEquals("BIZ_AUTH_001", exception.getErrorCode());
        assertTrue(exception.getContext().containsKey("operation"));
    }
    
    @Test
    @DisplayName("BusinessException - Deve criar erro de entidade não encontrada")
    void testBusinessExceptionEntityNotFound() {
        BusinessException exception = BusinessException.entityNotFound("Produto", 123L);
        
        assertEquals("BIZ_002", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Produto"));
        assertTrue(exception.getMessage().contains("123"));
    }
    
    @Test
    @DisplayName("BusinessException - Deve criar erro de duplicidade")
    void testBusinessExceptionDuplicateEntity() {
        BusinessException exception = BusinessException.duplicateEntity("Cliente", "email", "teste@email.com");
        
        assertEquals("BIZ_003", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("Cliente"));
        assertTrue(exception.getContext().containsKey("duplicateField"));
    }
    
    @Test
    @DisplayName("ValidationException - Deve criar com mensagem")
    void testValidationException() {
        ValidationException exception = new ValidationException("Erro de validação");
        
        assertEquals("Erro de validação", exception.getMessage());
    }
    
    @Test
    @DisplayName("ValidationException - Deve criar com causa")
    void testValidationExceptionWithCause() {
        Throwable cause = new RuntimeException("Causa");
        ValidationException exception = new ValidationException("Erro", cause);
        
        assertEquals("Erro", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar com mensagem simples")
    void testDataAccessExceptionSimpleMessage() {
        DataAccessException exception = new DataAccessException("Erro de dados");
        
        assertEquals("Erro de dados", exception.getMessage());
        assertEquals("DATA_001", exception.getErrorCode());
        assertNotNull(exception.getTimestamp());
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar com código customizado")
    void testDataAccessExceptionCustomErrorCode() {
        DataAccessException exception = new DataAccessException("DATA_QUERY_001", "Erro de query");
        
        assertEquals("DATA_QUERY_001", exception.getErrorCode());
        assertEquals("Erro de query", exception.getMessage());
    }
    
    @Test
    @DisplayName("DataAccessException - Deve adicionar contexto")
    void testDataAccessExceptionAddContext() {
        DataAccessException exception = new DataAccessException("Erro")
            .addContext("query", "SELECT * FROM produtos")
            .addContext("table", "produtos");
        
        Map<String, Object> context = exception.getContext();
        assertEquals(2, context.size());
        assertEquals("SELECT * FROM produtos", context.get("query"));
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar erro de query")
    void testDataAccessExceptionQueryError() {
        Throwable cause = new SQLException("SQL Error");
        DataAccessException exception = DataAccessException.queryError("SELECT * FROM produtos", cause);
        
        assertEquals("DATA_QUERY_001", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getContext().containsKey("query"));
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar erro de conexão")
    void testDataAccessExceptionConnectionError() {
        Throwable cause = new RuntimeException("Connection failed");
        DataAccessException exception = DataAccessException.connectionError("PostgreSQL", cause);
        
        assertEquals("DATA_CONN_001", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getContext().containsKey("dataSource"));
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar erro de transação")
    void testDataAccessExceptionTransactionError() {
        Throwable cause = new RuntimeException("Transaction failed");
        DataAccessException exception = DataAccessException.transactionError("commit", cause);
        
        assertEquals("DATA_TX_001", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getContext().containsKey("operation"));
    }
    
    @Test
    @DisplayName("DataAccessException - Deve criar erro de constraint")
    void testDataAccessExceptionConstraintError() {
        DataAccessException exception = DataAccessException.constraintError("uk_email", "clientes");
        
        assertEquals("DATA_CONST_001", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("uk_email"));
        assertTrue(exception.getContext().containsKey("constraintName"));
    }
    
    @Test
    @DisplayName("SystemException - Deve criar com mensagem simples")
    void testSystemExceptionSimpleMessage() {
        SystemException exception = new SystemException("Erro de sistema");
        
        assertEquals("Erro de sistema", exception.getMessage());
        assertEquals("SYS_001", exception.getErrorCode());
        assertNotNull(exception.getTimestamp());
    }
    
    @Test
    @DisplayName("SystemException - Deve criar com componente")
    void testSystemExceptionWithComponent() {
        SystemException exception = new SystemException("SYS_001", "Erro", null, "operation", "Component");
        
        assertEquals("Component", exception.getComponent());
    }
    
    @Test
    @DisplayName("SystemException - Deve identificar erro de banco de dados")
    void testSystemExceptionDatabaseError() {
        SystemException exception = SystemException.databaseError("query", new RuntimeException());
        
        assertTrue(exception.isDatabaseError());
        assertEquals("SYS_DB_001", exception.getErrorCode());
    }
    
    @Test
    @DisplayName("SystemException - Deve identificar erro de rede")
    void testSystemExceptionNetworkError() {
        SystemException exception = SystemException.networkError("GET", "/api", new RuntimeException());
        
        assertTrue(exception.isNetworkError());
        assertEquals("SYS_NET_001", exception.getErrorCode());
    }
    
    @Test
    @DisplayName("SystemException - Deve identificar erro de I/O")
    void testSystemExceptionIOError() {
        SystemException exception = SystemException.ioError("read", "file.txt", new RuntimeException());
        
        assertTrue(exception.isIOError());
        assertEquals("SYS_IO_001", exception.getErrorCode());
    }
    
    @Test
    @DisplayName("SystemException - Deve identificar erro de configuração")
    void testSystemExceptionConfigurationError() {
        SystemException exception = SystemException.configurationError("db.url", "jdbc:postgresql");
        
        assertTrue(exception.isConfigurationError());
        assertEquals("SYS_CONF_001", exception.getErrorCode());
    }
    
    @Test
    @DisplayName("SystemException - Deve identificar erro de memória")
    void testSystemExceptionMemoryError() {
        SystemException exception = SystemException.memoryError("alloc", 1024);
        
        assertTrue(exception.isMemoryError());
        assertEquals("SYS_MEM_001", exception.getErrorCode());
    }
    
    @Test
    @DisplayName("SystemException - Deve criar erro de timeout")
    void testSystemExceptionTimeoutError() {
        SystemException exception = SystemException.timeoutError("request", 5000);
        
        assertEquals("SYS_TIMEOUT_001", exception.getErrorCode());
        assertTrue(exception.getContext().containsKey("timeoutMs"));
    }
    
    @Test
    @DisplayName("SystemException - Deve criar erro de serviço indisponível")
    void testSystemExceptionServiceUnavailable() {
        SystemException exception = SystemException.serviceUnavailable("PaymentService");
        
        assertEquals("SYS_SVC_001", exception.getErrorCode());
        assertTrue(exception.getMessage().contains("PaymentService"));
    }
    
    @Test
    @DisplayName("SystemException - Deve criar a partir de exceção genérica")
    void testSystemExceptionFrom() {
        Throwable cause = new RuntimeException("Generic error");
        SystemException exception = SystemException.from("operation", cause);
        
        assertEquals("SYS_001", exception.getErrorCode());
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getContext().containsKey("originalException"));
    }
    
    // Classe auxiliar para simular SQLException
    static class SQLException extends RuntimeException {
        public SQLException(String message) {
            super(message);
        }
    }
}
