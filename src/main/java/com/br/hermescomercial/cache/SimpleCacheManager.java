package com.br.hermescomercial.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Cache Simples e Eficiente para Hermes Comercial PDV
 * Cache em memória com TTL (Time To Live) para performance
 */
public class SimpleCacheManager {
    
    private static final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL = TimeUnit.MINUTES.toMillis(5); // 5 minutos padrão
    
    // Entrada de cache com TTL
    private static class CacheEntry {
        final Object value;
        final long expiryTime;
        
        CacheEntry(Object value, long ttlMillis) {
            this.value = value;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }
        
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    
    /**
     * Adiciona item ao cache com TTL padrão
     */
    public static void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }
    
    /**
     * Adiciona item ao cache com TTL customizado
     */
    public static void put(String key, Object value, long ttlMillis) {
        cache.put(key, new CacheEntry(value, ttlMillis));
    }
    
    /**
     * Obtém item do cache
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return (T) entry.value;
    }
    
    /**
     * Verifica se item existe e não expirou
     */
    public static boolean containsKey(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return true;
    }
    
    /**
     * Remove item do cache
     */
    public static void remove(String key) {
        cache.remove(key);
    }
    
    /**
     * Limpa todo o cache
     */
    public static void clear() {
        cache.clear();
    }
    
    /**
     * Limpa itens expirados
     */
    public static void cleanupExpired() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    /**
     * Obtém estatísticas do cache
     */
    public static String getStatistics() {
        int totalEntries = cache.size();
        int expiredEntries = 0;
        
        for (CacheEntry entry : cache.values()) {
            if (entry.isExpired()) {
                expiredEntries++;
            }
        }
        
        return String.format("📊 Cache Stats: %d total, %d expirados, %d ativos", 
                           totalEntries, expiredEntries, totalEntries - expiredEntries);
    }
    
    /**
     * Cache específico para produtos
     */
    public static class ProdutoCache {
        private static final String PRODUTO_PREFIX = "produto_";
        
        public static void putProduto(String codigo, Object produto) {
            put(PRODUTO_PREFIX + codigo, produto, TimeUnit.MINUTES.toMillis(10)); // 10 minutos
        }
        
        public static <T> T getProduto(String codigo) {
            return get(PRODUTO_PREFIX + codigo);
        }
        
        public static boolean containsProduto(String codigo) {
            return containsKey(PRODUTO_PREFIX + codigo);
        }
        
        public static void removeProduto(String codigo) {
            remove(PRODUTO_PREFIX + codigo);
        }
    }
    
    /**
     * Cache específico para clientes
     */
    public static class ClienteCache {
        private static final String CLIENTE_PREFIX = "cliente_";
        
        public static void putCliente(String id, Object cliente) {
            put(CLIENTE_PREFIX + id, cliente, TimeUnit.MINUTES.toMillis(15)); // 15 minutos
        }
        
        public static <T> T getCliente(String id) {
            return get(CLIENTE_PREFIX + id);
        }
        
        public static boolean containsCliente(String id) {
            return containsKey(CLIENTE_PREFIX + id);
        }
        
        public static void removeCliente(String id) {
            remove(CLIENTE_PREFIX + id);
        }
    }
    
    /**
     * Cache específico para configurações
     */
    public static class ConfigCache {
        private static final String CONFIG_PREFIX = "config_";
        
        public static void putConfig(String chave, Object valor) {
            put(CONFIG_PREFIX + chave, valor, TimeUnit.HOURS.toMillis(1)); // 1 hora
        }
        
        public static <T> T getConfig(String chave) {
            return get(CONFIG_PREFIX + chave);
        }
        
        public static boolean containsConfig(String chave) {
            return containsKey(CONFIG_PREFIX + chave);
        }
        
        public static void removeConfig(String chave) {
            remove(CONFIG_PREFIX + chave);
        }
    }
}
