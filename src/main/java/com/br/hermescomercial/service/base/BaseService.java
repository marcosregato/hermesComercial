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
 * Service Base abstrato
 * Fornece funcionalidades comuns para todos os services
 * Versão 2.0.0 - Service Layer Refactoring
 */
public abstract class BaseService {
    
    protected final Logger logger;
    protected final CacheManager cacheManager;
    protected final ConfigurationManager configManager;
    protected final EventSystem eventSystem;
    
    protected BaseService() {
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
            
            // Publica evento de sucesso
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "service." + operationName + ".success", 
                result,
                "BaseService"
            ));
            
            return result;
            
        } catch (BusinessException e) {
            logger.log(Level.WARNING, "Erro de negócio na operação: " + operationName, e);
            
            // Publica evento de erro de negócio
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "service." + operationName + ".business_error", 
                e.getMessage(),
                "BaseService"
            ));
            
            throw e;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro de sistema na operação: " + operationName, e);
            
            // Publica evento de erro de sistema
            eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
                "service." + operationName + ".system_error", 
                e.getMessage(),
                "BaseService"
            ));
            
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
     * Invalida cache por padrão
     */
    protected void invalidateCacheByPattern(String pattern) {
        // Implementação simples - remove todas as chaves que contém o padrão
        cacheManager.keySet().stream()
            .filter(key -> key.contains(pattern))
            .forEach(cacheManager::remove);
        
        logger.info("Cache invalidado por padrão: " + pattern);
    }
    
    /**
     * Verifica se operação está habilitada via configuração
     */
    protected boolean isOperationEnabled(String operationName) {
        return configManager.getBoolean("service." + operationName + ".enabled", true);
    }
    
    /**
     * Obtém timeout configurado para operação
     */
    protected long getOperationTimeout(String operationName) {
        return configManager.getLong("service." + operationName + ".timeout", 30000L);
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
    
    /**
     * Formata mensagem de log com contexto
     */
    protected String formatLogMessage(String message, Object... context) {
        StringBuilder sb = new StringBuilder(message);
        
        if (context.length > 0) {
            sb.append(" | Contexto: ");
            for (int i = 0; i < context.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(context[i]);
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Publica evento de auditoria
     */
    protected void publishAuditEvent(String action, Object entity, String user) {
        java.util.Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("action", action);
        metadata.put("entity", entity.getClass().getSimpleName());
        metadata.put("user", user);
        metadata.put("timestamp", java.time.LocalDateTime.now());
        
        eventSystem.publish(new com.br.hermescomercial.event.EventSystem.Event(
            "audit." + action.toLowerCase(),
            entity,
            "BaseService"
        ));
        
        logger.info("Auditoria: " + action + " em " + entity.getClass().getSimpleName() + " por " + user);
    }
}
