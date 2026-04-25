package com.br.hermescomercial.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessExceptionTest {

    @Test
    @DisplayName("Deve criar DataAccessException com mensagem")
    void testDataAccessExceptionComMensagem() {
        String mensagem = "Erro de acesso a dados";
        DataAccessException exception = new DataAccessException(mensagem);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar DataAccessException com mensagem e causa")
    void testDataAccessExceptionComMensagemECausa() {
        String mensagem = "Erro de banco de dados";
        Throwable causa = new RuntimeException("Falha na conexão");
        DataAccessException exception = new DataAccessException(mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Deve ser RuntimeException")
    void testDataAccessExceptionHeranca() {
        DataAccessException exception = new DataAccessException("Teste");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve preservar stack trace")
    void testDataAccessExceptionStackTrace() {
        String mensagem = "Teste de stack trace";
        DataAccessException exception = new DataAccessException(mensagem);
        
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("Deve suportar mensagem nula")
    void testDataAccessExceptionMensagemNula() {
        DataAccessException exception = new DataAccessException(null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve suportar causa nula")
    void testDataAccessExceptionCausaNula() {
        String mensagem = "Erro de dados";
        DataAccessException exception = new DataAccessException(mensagem, null);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve funcionar em bloco try-catch")
    void testDataAccessExceptionTryCatch() {
        String mensagemEsperada = "Erro de acesso capturado";
        
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            throw new DataAccessException(mensagemEsperada);
        });
        
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @Test
    @DisplayName("Deve suportar encadeamento com SQLException simulado")
    void testDataAccessExceptionEncadeamentoSQL() {
        RuntimeException sqlException = new RuntimeException("Connection timeout");
        DataAccessException exception = new DataAccessException("Falha ao executar query", sqlException);
        
        assertEquals("Falha ao executar query", exception.getMessage());
        assertEquals(sqlException, exception.getCause());
        assertEquals("Connection timeout", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve manter tipo específico quando capturada como Exception")
    void testDataAccessExceptionTipoEspecifico() {
        Exception exception = assertThrows(Exception.class, () -> {
            throw new DataAccessException("Erro específico de dados");
        });
        
        assertTrue(exception instanceof DataAccessException);
        assertEquals("Erro específico de dados", exception.getMessage());
    }

    @Test
    @DisplayName("Deve suportar múltiplos níveis de encadeamento")
    void testDataAccessExceptionMultiplasCamadas() {
        RuntimeException erroOriginal = new RuntimeException("Erro de driver");
        DataAccessException daoException = new DataAccessException("Erro no DAO", erroOriginal);
        BusinessException businessException = new BusinessException("Erro de negócio", daoException);
        
        assertEquals("Erro de negócio", businessException.getMessage());
        assertEquals(daoException, businessException.getCause());
        assertEquals(erroOriginal, businessException.getCause().getCause());
    }
}
