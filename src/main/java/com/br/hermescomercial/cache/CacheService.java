package com.br.hermescomercial.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Serviço de cache centralizado usando Caffeine
 * Melhora performance de consultas frequentes e reduz carga no banco
 * @author system
 */
public class CacheService {
    private static final Logger logger = LogManager.getLogger(CacheService.class);
    
    // Cache para produtos (consultas frequentes)
    private final Cache<String, Object> produtoCache;
    
    // Cache para clientes (dados de acesso rápido)
    private final Cache<String, Object> clienteCache;
    
    // Cache para configurações (mudam raramente)
    private final Cache<String, Object> configCache;
    
    // Cache para resultados de queries complexas
    private final Cache<String, Object> queryCache;
    
    private static final CacheService instance = new CacheService();
    
    private CacheService() {
        // Cache de produtos - 10 minutos, 1000 itens
        produtoCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .recordStats()
            .build();
            
        // Cache de clientes - 15 minutos, 500 itens
        clienteCache = Caffeine.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .maximumSize(500)
            .recordStats()
            .build();
            
        // Cache de configurações - 1 hora, 100 itens
        configCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(100)
            .recordStats()
            .build();
            
        // Cache de queries - 5 minutos, 200 itens
        queryCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(200)
            .recordStats()
            .build();
            
        logger.info("CacheService inicializado com 4 caches configurados");
    }
    
    public static CacheService getInstance() {
        return instance;
    }
    
    /**
     * Obtém ou calcula valor do cache de produtos
     */
    @SuppressWarnings("unchecked")
    public <T> T getProduto(String key, Function<String, T> loader) {
        try {
            T value = (T) produtoCache.get(key, k -> loader.apply(k));
            logger.debug("Cache hit para produto: {}", key);
            return value;
        } catch (Exception e) {
            logger.error("Erro ao acessar cache de produtos: {}", e.getMessage(), e);
            return loader.apply(key); // Fallback direto
        }
    }
    
    /**
     * Obtém ou calcula valor do cache de clientes
     */
    @SuppressWarnings("unchecked")
    public <T> T getCliente(String key, Function<String, T> loader) {
        try {
            T value = (T) clienteCache.get(key, k -> loader.apply(k));
            logger.debug("Cache hit para cliente: {}", key);
            return value;
        } catch (Exception e) {
            logger.error("Erro ao acessar cache de clientes: {}", e.getMessage(), e);
            return loader.apply(key); // Fallback direto
        }
    }
    
    /**
     * Obtém ou calcula valor do cache de configurações
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String key, Function<String, T> loader) {
        try {
            T value = (T) configCache.get(key, k -> loader.apply(k));
            logger.debug("Cache hit para configuração: {}", key);
            return value;
        } catch (Exception e) {
            logger.error("Erro ao acessar cache de configurações: {}", e.getMessage(), e);
            return loader.apply(key); // Fallback direto
        }
    }
    
    /**
     * Obtém ou calcula valor do cache de queries
     */
    @SuppressWarnings("unchecked")
    public <T> T getQuery(String key, Function<String, T> loader) {
        try {
            T value = (T) queryCache.get(key, k -> loader.apply(k));
            logger.debug("Cache hit para query: {}", key);
            return value;
        } catch (Exception e) {
            logger.error("Erro ao acessar cache de queries: {}", e.getMessage(), e);
            return loader.apply(key); // Fallback direto
        }
    }
    
    /**
     * Invalida um item específico do cache de produtos
     */
    public void invalidateProduto(String key) {
        produtoCache.invalidate(key);
        logger.debug("Cache de produtos invalidado para chave: {}", key);
    }
    
    /**
     * Invalida um item específico do cache de clientes
     */
    public void invalidateCliente(String key) {
        clienteCache.invalidate(key);
        logger.debug("Cache de clientes invalidado para chave: {}", key);
    }
    
    /**
     * Invalida um item específico do cache de configurações
     */
    public void invalidateConfig(String key) {
        configCache.invalidate(key);
        logger.debug("Cache de configurações invalidado para chave: {}", key);
    }
    
    /**
     * Invalida um item específico do cache de queries
     */
    public void invalidateQuery(String key) {
        queryCache.invalidate(key);
        logger.debug("Cache de queries invalidado para chave: {}", key);
    }
    
    /**
     * Limpa todos os caches
     */
    public void clearAll() {
        produtoCache.invalidateAll();
        clienteCache.invalidateAll();
        configCache.invalidateAll();
        queryCache.invalidateAll();
        logger.info("Todos os caches foram limpos");
    }
    
    /**
     * Obtém estatísticas do cache de produtos
     */
    public CacheStats getProdutoStats() {
        return produtoCache.stats();
    }
    
    /**
     * Obtém estatísticas do cache de clientes
     */
    public CacheStats getClienteStats() {
        return clienteCache.stats();
    }
    
    /**
     * Obtém estatísticas do cache de configurações
     */
    public CacheStats getConfigStats() {
        return configCache.stats();
    }
    
    /**
     * Obtém estatísticas do cache de queries
     */
    public CacheStats getQueryStats() {
        return queryCache.stats();
    }
    
    /**
     * Retorna relatório completo de estatísticas
     */
    public String getStatsReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Relatório de Cache ===\n");
        
        report.append("Produtos: ").append(formatStats(produtoCache.stats())).append("\n");
        report.append("Clientes: ").append(formatStats(clienteCache.stats())).append("\n");
        report.append("Configurações: ").append(formatStats(configCache.stats())).append("\n");
        report.append("Queries: ").append(formatStats(queryCache.stats())).append("\n");
        
        return report.toString();
    }
    
    private String formatStats(CacheStats stats) {
        return String.format(
            "Hit Rate: %.2f%%, Misses: %d",
            stats.hitRate() * 100,
            stats.missCount()
        );
    }
    
    /**
     * Pré-carrega dados importantes no cache
     */
    public void preloadCache() {
        logger.info("Iniciando pré-carregamento do cache...");
        
        try {
            // Aqui poderíamos pré-carregar produtos mais vendidos
            // clientes frequentes, configurações, etc.
            
            logger.info("Pré-carregamento do cache concluído");
        } catch (Exception e) {
            logger.error("Erro no pré-carregamento do cache: {}", e.getMessage(), e);
        }
    }
}
