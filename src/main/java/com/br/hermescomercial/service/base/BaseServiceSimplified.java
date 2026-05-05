package com.br.hermescomercial.service.base;

import com.br.hermescomercial.cache.CacheManager;
import com.br.hermescomercial.config.ConfigurationManager;
import com.br.hermescomercial.event.EventSystem;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.SystemException;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Service Base abstrato - Versão Simplificada
 * Fornece funcionalidades comuns para todos os services
 * Versão 2.0.0 - Service Layer Refactoring (Simplified)
 */
public abstract class BaseServiceSimplified {
    
    protected final Logger logger;
    protected final CacheManager cacheManager;
    protected final ConfigurationManager configManager;
    protected final EventSystem eventSystem;
    
    protected BaseServiceSimplified() {
        this.logger = Logger.getLogger(getClass().getName());
        this.cacheManager = CacheManager.getInstance();
        this.configManager = ConfigurationManager.getInstance();
        this.eventSystem = EventSystem.getInstance();
    }
    
    /**
     * Executa operação com tratamento de exceções padronizado
     */
    protected <T> T executeOperation(Supplier<T> operation, String operationName) {
        try {
            logger.info("Iniciando operação: " + operationName);
            
            long startTime = System.currentTimeMillis();
            T result = operation.get();
            long duration = System.currentTimeMillis() - startTime;
            
            logger.info("Operação concluída: " + operationName + " em " + duration + "ms");
            
            return result;
            
        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Erro de negócio na operação: " + operationName, e);
            throw e;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro de sistema na operação: " + operationName, e);
            throw new SystemException("Erro na operação: " + operationName, e);
        }
    }
    
    /**
     * Executa operação assíncrona
     */
    protected <T> CompletableFuture<T> executeAsyncOperation(Supplier<T> operation, String operationName) {
        return CompletableFuture.supplyAsync(() -> executeOperation(operation, operationName));
    }
    
    /**
     * Obtém valor do cache ou executa operação
     */
    protected <T> T getFromCacheOrExecute(String cacheKey, Supplier<T> operation, String operationName) {
        // Tenta do cache primeiro
        @SuppressWarnings("unchecked")
        T cachedValue = cacheManager.get(cacheKey);
        
        if (cachedValue != null) {
            logger.info("Cache hit para operação: " + operationName);
            return cachedValue;
        }
        
        // Executa operação e armazena no cache
        logger.info("Cache miss para operação: " + operationName);
        T result = executeOperation(operation, operationName);
        
        // Armazena no cache com TTL configurável
        long ttl = configManager.getLong("cache." + operationName + ".ttl", 300000L);
        cacheManager.put(cacheKey, result, ttl);
        
        return result;
    }
    
    /**
     * Invalida cache para uma chave específica
     */
    protected void invalidateCache(String cacheKey) {
        cacheManager.remove(cacheKey);
        logger.info("Cache invalidado: " + cacheKey);
    }
    
    /**
     * Verifica se operação está habilitada via configuração
     */
    protected boolean isOperationEnabled(String operationName) {
        return configManager.getBoolean("service." + operationName + ".enabled", true);
    }
    
    /**
     * Valida se objeto não é nulo
     */
    protected void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new BusinessException(fieldName + " não pode ser nulo");
        }
    }
    
    /**
     * Valida se string não está vazia
     */
    protected void requireNonEmpty(String str, String fieldName) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(fieldName + " não pode estar vazio");
        }
    }
    
    /**
     * Valida se número é positivo
     */
    protected void requirePositive(Number number, String fieldName) {
        if (number == null || number.doubleValue() <= 0) {
            throw new BusinessException(fieldName + " deve ser positivo");
        }
    }
    
    /**
     * Valida se número não é negativo
     */
    protected void requireNonNegative(Number number, String fieldName) {
        if (number == null || number.doubleValue() < 0) {
            throw new BusinessException(fieldName + " não pode ser negativo");
        }
    }
}
