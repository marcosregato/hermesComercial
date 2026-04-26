package com.br.hermescomercial.exception;

/**
 * Exceção de sistema para erros técnicos e infraestrutura
 * Diferencia de BusinessException que é para regras de negócio
 */
public class SystemException extends RuntimeException {
    
    public SystemException(String message) {
        super(message);
    }
    
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor para criar SystemException a partir de uma exceção genérica
     * @param operation nome da operação que falhou
     * @param cause exceção original
     * @return SystemException com mensagem formatada
     */
    public static SystemException from(String operation, Throwable cause) {
        String message = String.format("Erro interno do sistema na operação: %s", operation);
        return new SystemException(message, cause);
    }
}
