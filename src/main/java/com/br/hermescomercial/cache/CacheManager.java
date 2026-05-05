package com.br.hermescomercial.cache;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import java.util.function.Supplier;

/**
 * Cache Manager Refatorado
 * Implementa cache com múltiplas estratégias e TTL
 * Versão 2.0.0 - Cache Pattern Implementation
 */
public class CacheManager {
    
    private static volatile CacheManager instance;
    private static final Object lock = new Object();
    
    // Estratégias de cache
    public enum EvictionPolicy {
        LRU,        // Least Recently Used
        LFU,        // Least Frequently Used
        FIFO,       // First In First Out
        TTL         // Time To Live
    }
    
    // Entrada do cache
    private static class CacheEntry<V> {
        final V value;
        final long creationTime;
        volatile long lastAccessTime;
        volatile long accessCount;
        volatile boolean expired;
        
        CacheEntry(V value) {
            this.value = value;
            this.creationTime = System.currentTimeMillis();
            this.lastAccessTime = this.creationTime;
            this.accessCount = 1;
            this.expired = false;
        }
        
        void access() {
            this.lastAccessTime = System.currentTimeMillis();
            this.accessCount++;
        }
        
        boolean isExpired(long ttlMillis) {
            return expired || (ttlMillis > 0 && (System.currentTimeMillis() - creationTime) > ttlMillis);
        }
    }
    
    // Estrutura do cache
    private final Map<String, CacheEntry<Object>> cache = new ConcurrentHashMap<>();
    private final PriorityQueue<String> accessQueue = new PriorityQueue<>(
        Comparator.comparingLong(key -> cache.get(key).lastAccessTime)
    );
    private final Map<String, Long> frequencyMap = new ConcurrentHashMap<>();
    private final Queue<String> insertionOrder = new ConcurrentLinkedQueue<>();
    
    // Configurações
    private int maxSize = 1000;
    private long defaultTtl = 300000; // 5 minutos
    private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;
    private final ScheduledExecutorService cleanupExecutor;
    
    // Estatísticas
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    
    // Listeners
    private final List<CacheEventListener> listeners = new CopyOnWriteArrayList<>();
    
    // Construtor privado para Singleton
    private CacheManager() {
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Cache-Cleanup");
            t.setDaemon(true);
            return t;
        });
        
        // Inicia cleanup periódico
        cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredEntries, 
                                         1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * Obtém instância singleton
     */
    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Interface para listeners de eventos do cache
     */
    public interface CacheEventListener {
        void onCacheHit(String key, Object value);
        void onCacheMiss(String key);
        void onCacheEviction(String key, Object value);
        void onCacheExpired(String key, Object value);
    }
    
    /**
     * Configura o tamanho máximo do cache
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        evictIfNecessary();
    }
    
    /**
     * Configura o TTL padrão
     */
    public void setDefaultTtl(long ttlMillis) {
        this.defaultTtl = ttlMillis;
    }
    
    /**
     * Configura a política de evicção
     */
    public void setEvictionPolicy(EvictionPolicy policy) {
        this.evictionPolicy = policy;
    }
    
    /**
     * Adiciona item ao cache com TTL padrão
     */
    public void put(String key, Object value) {
        put(key, value, defaultTtl);
    }
    
    /**
     * Adiciona item ao cache com TTL específico
     */
    public void put(String key, Object value, long ttlMillis) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key e value não podem ser nulos");
        }
        
        CacheEntry<Object> entry = new CacheEntry<>(value);
        cache.put(key, entry);
        frequencyMap.put(key, frequencyMap.getOrDefault(key, 0L) + 1);
        insertionOrder.offer(key);
        
        // Atualiza estruturas de acordo com a política
        updateEvictionStructures(key);
        
        // Evita que o cache cresça indefinidamente
        evictIfNecessary();
    }
    
    /**
     * Obtém item do cache
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        if (key == null) {
            return null;
        }
        
        CacheEntry<Object> entry = cache.get(key);
        
        if (entry == null) {
            misses.incrementAndGet();
            notifyCacheMiss(key);
            return null;
        }
        
        // Verifica se expirou
        if (entry.isExpired(defaultTtl)) {
            cache.remove(key);
            frequencyMap.remove(key);
            misses.incrementAndGet();
            notifyCacheExpired(key, entry.value);
            return null;
        }
        
        // Atualiza acesso
        entry.access();
        hits.incrementAndGet();
        notifyCacheHit(key, entry.value);
        
        return (T) entry.value;
    }
    
    /**
     * Obtém item do cache com valor padrão
     */
    public <T> T get(String key, T defaultValue) {
        T value = get(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Verifica se chave existe no cache
     */
    public boolean containsKey(String key) {
        if (key == null) {
            return false;
        }
        
        CacheEntry<Object> entry = cache.get(key);
        return entry != null && !entry.isExpired(defaultTtl);
    }
    
    /**
     * Remove item do cache
     */
    public Object remove(String key) {
        if (key == null) {
            return null;
        }
        
        CacheEntry<Object> entry = cache.remove(key);
        if (entry != null) {
            frequencyMap.remove(key);
            notifyCacheEviction(key, entry.value);
        }
        
        return entry != null ? entry.value : null;
    }
    
    /**
     * Limpa todo o cache
     */
    public void clear() {
        cache.clear();
        frequencyMap.clear();
        insertionOrder.clear();
        accessQueue.clear();
    }
    
    /**
     * Obtém tamanho do cache
     */
    public int size() {
        return cache.size();
    }
    
    /**
     * Verifica se cache está vazio
     */
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    /**
     * Obtém todas as chaves do cache
     */
    public Set<String> keySet() {
        return new HashSet<>(cache.keySet());
    }
    
    /**
     * Obtém estatísticas do cache
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("hits", hits.get());
        stats.put("misses", misses.get());
        stats.put("evictions", evictions.get());
        stats.put("size", cache.size());
        stats.put("maxSize", maxSize);
        stats.put("hitRatio", getHitRatio());
        stats.put("defaultTtl", defaultTtl);
        stats.put("evictionPolicy", evictionPolicy.name());
        return stats;
    }
    
    /**
     * Obtém hit ratio do cache
     */
    public double getHitRatio() {
        long totalRequests = hits.get() + misses.get();
        return totalRequests > 0 ? (double) hits.get() / totalRequests : 0.0;
    }
    
    /**
     * Adiciona listener de eventos
     */
    public void addListener(CacheEventListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove listener de eventos
     */
    public void removeListener(CacheEventListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Warm-up do cache com dados iniciais
     */
    public void warmup(Map<String, Object> initialData) {
        for (Map.Entry<String, Object> entry : initialData.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Pre-carrega dados assincronamente
     */
    public CompletableFuture<Void> preloadAsync(Supplier<Map<String, Object>> dataSupplier) {
        return CompletableFuture.runAsync(() -> {
            try {
                Map<String, Object> data = dataSupplier.get();
                warmup(data);
            } catch (Exception e) {
                System.err.println("Erro no preload do cache: " + e.getMessage());
            }
        });
    }
    
    /**
     * Atualiza estruturas de evicção
     */
    private void updateEvictionStructures(String key) {
        switch (evictionPolicy) {
            case LRU:
                accessQueue.remove(key);
                accessQueue.offer(key);
                break;
            case LFU:
                frequencyMap.put(key, frequencyMap.getOrDefault(key, 0L) + 1);
                break;
            case FIFO:
                // Já mantido por insertionOrder
                break;
            case TTL:
                // TTL é verificado no acesso
                break;
        }
    }
    
    /**
     * Evicção se necessário
     */
    private void evictIfNecessary() {
        while (cache.size() >= maxSize) {
            String keyToEvict = selectKeyToEvict();
            if (keyToEvict != null) {
                CacheEntry<Object> entry = cache.remove(keyToEvict);
                frequencyMap.remove(keyToEvict);
                insertionOrder.remove(keyToEvict);
                accessQueue.remove(keyToEvict);
                
                if (entry != null) {
                    evictions.incrementAndGet();
                    notifyCacheEviction(keyToEvict, entry.value);
                }
            } else {
                break;
            }
        }
    }
    
    /**
     * Seleciona chave para evicção baseada na política
     */
    private String selectKeyToEvict() {
        switch (evictionPolicy) {
            case LRU:
                return accessQueue.poll();
            case LFU:
                return frequencyMap.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            case FIFO:
                return insertionOrder.poll();
            case TTL:
                return cache.entrySet().stream()
                    .filter(entry -> entry.getValue().isExpired(defaultTtl))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            default:
                return null;
        }
    }
    
    /**
     * Limpa entradas expiradas
     */
    private void cleanupExpiredEntries() {
        Iterator<Map.Entry<String, CacheEntry<Object>>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CacheEntry<Object>> entry = iterator.next();
            if (entry.getValue().isExpired(defaultTtl)) {
                iterator.remove();
                String key = entry.getKey();
                frequencyMap.remove(key);
                insertionOrder.remove(key);
                accessQueue.remove(key);
                notifyCacheExpired(key, entry.getValue().value);
            }
        }
    }
    
    /**
     * Notifica listeners de cache hit
     */
    private void notifyCacheHit(String key, Object value) {
        for (CacheEventListener listener : listeners) {
            try {
                listener.onCacheHit(key, value);
            } catch (Exception e) {
                System.err.println("Erro no listener de cache hit: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifica listeners de cache miss
     */
    private void notifyCacheMiss(String key) {
        for (CacheEventListener listener : listeners) {
            try {
                listener.onCacheMiss(key);
            } catch (Exception e) {
                System.err.println("Erro no listener de cache miss: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifica listeners de evicção
     */
    private void notifyCacheEviction(String key, Object value) {
        for (CacheEventListener listener : listeners) {
            try {
                listener.onCacheEviction(key, value);
            } catch (Exception e) {
                System.err.println("Erro no listener de evicção: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifica listeners de expiração
     */
    private void notifyCacheExpired(String key, Object value) {
        for (CacheEventListener listener : listeners) {
            try {
                listener.onCacheExpired(key, value);
            } catch (Exception e) {
                System.err.println("Erro no listener de expiração: " + e.getMessage());
            }
        }
    }
    
    /**
     * Shutdown do cache manager
     */
    public void shutdown() {
        cleanupExecutor.shutdown();
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Cache Builder para configuração fluente
     */
    public static class CacheBuilder {
        private int maxSize = 1000;
        private long defaultTtl = 300000;
        private EvictionPolicy evictionPolicy = EvictionPolicy.LRU;
        
        public CacheBuilder maxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }
        
        public CacheBuilder defaultTtl(long ttlMillis) {
            this.defaultTtl = ttlMillis;
            return this;
        }
        
        public CacheBuilder evictionPolicy(EvictionPolicy policy) {
            this.evictionPolicy = policy;
            return this;
        }
        
        public void build() {
            CacheManager cache = CacheManager.getInstance();
            cache.setMaxSize(maxSize);
            cache.setDefaultTtl(defaultTtl);
            cache.setEvictionPolicy(evictionPolicy);
        }
    }
}
