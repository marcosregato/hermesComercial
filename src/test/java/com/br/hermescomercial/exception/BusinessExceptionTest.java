package com.br.hermescomercial.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    @DisplayName("Deve criar BusinessException com mensagem")
    void testBusinessExceptionComMensagem() {
        String mensagem = "Erro de regra de negócio";
        BusinessException exception = new BusinessException(mensagem);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve criar BusinessException com mensagem e causa")
    void testBusinessExceptionComMensagemECausa() {
        String mensagem = "Erro de negócio com causa";
        Throwable causa = new RuntimeException("Causa da exceção");
        BusinessException exception = new BusinessException(mensagem, causa);
        
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    @DisplayName("Deve ser RuntimeException")
    void testBusinessExceptionHeranca() {
        BusinessException exception = new BusinessException("Teste");
        
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Deve preservar stack trace")
    void testBusinessExceptionStackTrace() {
        String mensagem = "Teste de stack trace";
        BusinessException exception = new BusinessException(mensagem);
        
        StackTraceElement[] stackTrace = exception.getStackTrace();
        assertNotNull(stackTrace);
        assertTrue(stackTrace.length > 0);
    }

    @Test
    @DisplayName("Deve suportar mensagem nula")
    void testBusinessExceptionMensagemNula() {
        BusinessException exception = new BusinessException(null);
        
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve suportar causa nula")
    void testBusinessExceptionCausaNula() {
        String mensagem = "Mensagem de negócio";
        BusinessException exception = new BusinessException(mensagem, null);
        
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Deve funcionar em bloco try-catch")
    void testBusinessExceptionTryCatch() {
        String mensagemEsperada = "Erro de negócio capturado";
        
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            throw new BusinessException(mensagemEsperada);
        });
        
        assertEquals(mensagemEsperada, exception.getMessage());
    }

    @Test
    @DisplayName("Deve suportar encadeamento de exceções")
    void testBusinessExceptionEncadeamento() {
        RuntimeException causaRaiz = new IllegalStateException("Erro de sistema");
        BusinessException exception1 = new BusinessException("Erro de negócio primário", causaRaiz);
        BusinessException exception2 = new BusinessException("Erro de negócio secundário", exception1);
        
        assertEquals("Erro de negócio secundário", exception2.getMessage());
        assertEquals(exception1, exception2.getCause());
        assertEquals(causaRaiz, exception2.getCause().getCause());
    }

    @Test
    @DisplayName("Deve manter tipo específico quando capturada")
    void testBusinessExceptionTipoEspecifico() {
        Exception exception = assertThrows(Exception.class, () -> {
            throw new BusinessException("Erro específico de negócio");
        });
        
        assertTrue(exception instanceof BusinessException);
        assertEquals("Erro específico de negócio", exception.getMessage());
    }
}
