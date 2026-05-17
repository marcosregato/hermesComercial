package com.br.hermescomercial.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utilitário centralizado para tratamento de exceções
 * Padroniza o logging e tratamento de erros no sistema
 */
public class ExceptionHandler {
    
    private static final Logger logger = LogManager.getLogger(ExceptionHandler.class);
    
    /**
     * Trata exceções de negócio de forma padronizada
     * @param operation nome da operação
     * @param e exceção de negócio
     * @throws BusinessException re-lança a exceção original
     */
    public static void handleBusinessException(String operation, BusinessException e) throws BusinessException {
        logger.warn("Erro de negócio em {}: {}", operation, e.getMessage());
        throw e; // Re-lançar para tratamento adequado em camadas superiores
    }
    
    /**
     * Trata exceções de acesso a dados
     * @param operation nome da operação
     * @param e exceção de acesso a dados
     * @throws SystemException converte para SystemException
     */
    public static void handleDataAccessException(String operation, DataAccessException e) throws SystemException {
        logger.error("Erro de acesso a dados em {}: {}", operation, e.getMessage(), e);
        throw SystemException.from(operation, e);
    }
    
    /**
     * Trata exceções genéricas do sistema
     * @param operation nome da operação
     * @param e exceção genérica
     * @throws SystemException converte para SystemException
     */
    public static void handleSystemException(String operation, Exception e) throws SystemException {
        logger.error("Erro de sistema em {}: {}", operation, e.getMessage(), e);
        throw SystemException.from(operation, e);
    }
    
    /**
     * Trata exceções com opção de não relançar (para operações não críticas)
     * @param operation nome da operação
     * @param e exceção
     * @param defaultValue valor padrão a retornar
     * @param <T> tipo do retorno
     * @return valor padrão em caso de erro
     */
    public static <T> T handleWithDefault(String operation, Exception e, T defaultValue) {
        logger.error("Erro em {} (retornando valor padrão): {}", operation, e.getMessage(), e);
        return defaultValue;
    }
    
    /**
     * Trata exceções booleanas (operações de sucesso/falha)
     * @param operation nome da operação
     * @param e exceção
     * @return false em caso de erro
     */
    public static boolean handleBoolean(String operation, Exception e) {
        return handleWithDefault(operation, e, false);
    }
    
    /**
     * Trata exceções para operações que retornam null em caso de erro
     * @param operation nome da operação
     * @param e exceção
     * @param <T> tipo do objeto
     * @return null em caso de erro
     */
    public static <T> T handleNull(String operation, Exception e) {
        return handleWithDefault(operation, e, null);
    }
    
    /**
     * Loga exceção sem relançar (para cleanup ou operações não críticas)
     * @param operation nome da operação
     * @param e exceção
     */
    public static void logOnly(String operation, Exception e) {
        logger.error("Erro em {} (ignorado): {}", operation, e.getMessage(), e);
    }
}
