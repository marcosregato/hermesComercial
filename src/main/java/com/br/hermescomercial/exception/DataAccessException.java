package com.br.hermescomercial.exception;

/**
 * Exceção de acesso a dados para erros de banco de dados
 */
public class DataAccessException extends RuntimeException {
    
    public DataAccessException(String message) {
        super(message);
    }
    
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
