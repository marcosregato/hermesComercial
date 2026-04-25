package com.br.hermescomercial.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    @DisplayName("Deve criar ValidationException com mensagem")
    void testValidationExceptionComMensagem() {
        String mensagem = "Mensagem de erro de validação";
        ValidationException exception = new ValidationException(mensagem);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar ValidationException com mensagem e causa")
    void testValidationExceptionComMensagemECausa() {
        String mensagem = "Mensagem de erro de validação";
        Throwable causa = new RuntimeException("Causa raiz");
        ValidationException exception = new ValidationException(mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Deve ser RuntimeException")
    void testValidationExceptionHeranca() {
        ValidationException exception = new ValidationException("Teste");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve preservar stack trace")
    void testValidationExceptionStackTrace() {
        String mensagem = "Teste de stack trace";
        ValidationException exception = new ValidationException(mensagem);
        
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("Deve suportar mensagem nula")
    void testValidationExceptionMensagemNula() {
        ValidationException exception = new ValidationException(null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve suportar causa nula")
    void testValidationExceptionCausaNula() {
        String mensagem = "Mensagem de teste";
        ValidationException exception = new ValidationException(mensagem, null);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve manter mensagem quando causa é fornecida")
    void testValidationExceptionMensagemComCausa() {
        String mensagem = "Mensagem principal";
        Throwable causa = new IllegalArgumentException("Causa específica");
        ValidationException exception = new ValidationException(mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
        assertEquals("Causa específica", exception.getCause().getMessage());
    }

    @Test
    @DisplayName("Deve funcionar em bloco try-catch")
    void testValidationExceptionTryCatch() {
        String mensagemEsperada = "Erro de validação capturado";
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            throw new ValidationException(mensagemEsperada);
        });
        
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @Test
    @DisplayName("Deve suportar encadeamento de exceções")
    void testValidationExceptionEncadeamento() {
        RuntimeException causaRaiz = new IllegalStateException("Erro original");
        ValidationException exception1 = new ValidationException("Primeiro nível", causaRaiz);
        ValidationException exception2 = new ValidationException("Segundo nível", exception1);
        
        assertEquals("Segundo nível", exception2.getMessage());
        assertEquals(exception1, exception2.getCause());
        assertEquals(causaRaiz, exception2.getCause().getCause());
    }
}
