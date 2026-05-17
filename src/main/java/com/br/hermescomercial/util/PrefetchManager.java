package com.br.hermescomercial.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Prefetch Manager para Carregamento Inteligente de Dados
 * Antecipa o carregamento de dados baseado em padrões de uso
 */
public class PrefetchManager {
    
    private static final ExecutorService prefetchExecutor = Executors.newFixedThreadPool(2);
    private static final Map<String, PrefetchTask<?>> prefetchTasks = new ConcurrentHashMap<>();
    private static final Map<String, Long> lastAccess = new ConcurrentHashMap<>();
    
    /**
     * Tarefa de prefetch com TTL
     */
    private static class PrefetchTask<T> {
        private volatile T data;
        private volatile boolean loaded = false;
        private volatile long lastPrefetch = 0;
        private final Supplier<T> supplier;
        private final long ttlMillis;
        private final Object lock = new Object();
        
        PrefetchTask(Supplier<T> supplier, long ttlMillis) {
            this.supplier = supplier;
            this.ttlMillis = ttlMillis;
        }
        
        public T get() {
            if (!loaded || isExpired()) {
                synchronized (lock) {
                    if (!loaded || isExpired()) {
                        load();
                    }
                }
            }
            return data;
        }
        
        public void prefetchAsync() {
            if (!loaded || isExpired()) {
                CompletableFuture.runAsync(() -> {
                    synchronized (lock) {
                        if (!loaded || isExpired()) {
                            load();
                        }
                    }
                }, prefetchExecutor);
            }
        }
        
        private void load() {
            try {
                long start = System.currentTimeMillis();
                data = supplier.get();
                loaded = true;
                lastPrefetch = System.currentTimeMillis();
                long end = System.currentTimeMillis();
                System.out.println("🚀 Prefetch carregado em " + (end - start) + "ms");
            } catch (Exception e) {
                System.err.println("Erro no prefetch: " + e.getMessage());
            }
        }
        
        private boolean isExpired() {
            return (System.currentTimeMillis() - lastPrefetch) > ttlMillis;
        }
        
        public boolean isLoaded() {
            return loaded && !isExpired();
        }
        
        public void reset() {
            synchronized (lock) {
                data = null;
                loaded = false;
                lastPrefetch = 0;
            }
        }
    }
    
    /**
     * Registra uma tarefa de prefetch
     */
    public static <T> void register(String key, Supplier<T> supplier, long ttlMillis) {
        prefetchTasks.put(key, new PrefetchTask<>(supplier, ttlMillis));
    }
    
    /**
     * Obtém dados (com prefetch automático)
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        lastAccess.put(key, System.currentTimeMillis());
        
        PrefetchTask<T> task = (PrefetchTask<T>) prefetchTasks.get(key);
        if (task != null) {
            // Dispara prefetch para dados relacionados
            triggerRelatedPrefetch(key);
            return task.get();
        }
        return null;
    }
    
    /**
     * Força prefetch assíncrono
     */
    public static void prefetch(String key) {
        PrefetchTask<?> task = prefetchTasks.get(key);
        if (task != null) {
            task.prefetchAsync();
        }
    }
    
    /**
     * Dispara prefetch para dados relacionados baseado em padrões
     */
    private static void triggerRelatedPrefetch(String accessedKey) {
        // Produtos -> prefetch categorias e fornecedores
        if (accessedKey.contains("produto")) {
            prefetch("categorias");
            prefetch("fornecedores");
        }
        
        // Clientes -> prefetch vendas recentes
        if (accessedKey.contains("cliente")) {
            prefetch("vendas_recentes");
        }
        
        // Vendas -> prefetch produtos e clientes
        if (accessedKey.contains("venda")) {
            prefetch("produtos_populares");
            prefetch("clientes_ativos");
        }
        
        // Relatórios -> prefetch dados agregados
        if (accessedKey.contains("relatorio")) {
            prefetch("estatisticas_vendas");
            prefetch("resumo_financeiro");
        }
    }
    
    /**
     * Verifica se dados estão carregados
     */
    public static boolean isLoaded(String key) {
        PrefetchTask<?> task = prefetchTasks.get(key);
        return task != null && task.isLoaded();
    }
    
    /**
     * Reseta dados específicos
     */
    public static void reset(String key) {
        PrefetchTask<?> task = prefetchTasks.get(key);
        if (task != null) {
            task.reset();
        }
    }
    
    /**
     * Limpa todos os dados
     */
    public static void clear() {
        prefetchTasks.clear();
        lastAccess.clear();
    }
    
    /**
     * Prefetch inteligente baseado em padrões de uso
     */
    public static void smartPrefetch() {
        CompletableFuture.runAsync(() -> {
            // Analisa padrões de acesso recentes
            long currentTime = System.currentTimeMillis();
            long fiveMinutesAgo = currentTime - (5 * 60 * 1000);
            
            // Encontra dados mais acessados recentemente
            lastAccess.entrySet().stream()
                .filter(entry -> entry.getValue() > fiveMinutesAgo)
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .forEach(entry -> {
                    String key = entry.getKey();
                    if (!isLoaded(key)) {
                        System.out.println("🧠 Smart prefetch para: " + key);
                        prefetch(key);
                    }
                });
        }, prefetchExecutor);
    }
    
    /**
     * Obtém estatísticas do prefetch
     */
    public static String getStatistics() {
        int total = prefetchTasks.size();
        int loaded = 0;
        int expired = 0;
        
        for (PrefetchTask<?> task : prefetchTasks.values()) {
            if (task.isLoaded()) {
                loaded++;
            } else if (task.loaded) {
                expired++;
            }
        }
        
        return String.format(
            "📊 PrefetchManager Stats:\n" +
            "• Total Tasks: %d\n" +
            "• Carregados: %d\n" +
            "• Expirados: %d\n" +
            "• Pendentes: %d\n" +
            "• Cache Hit Ratio: %.1f%%",
            total, loaded, expired, total - loaded - expired,
            total > 0 ? (double) loaded / total * 100 : 0
        );
    }
    
    /**
     * Factory para tarefas comuns
     */
    public static class CommonTasks {
        
        /**
         * Registra prefetch para produtos populares
         */
        public static void registerPopularProducts(Supplier<Object[]> supplier) {
            register("produtos_populares", supplier, TimeUnit.MINUTES.toMillis(10));
        }
        
        /**
         * Registra prefetch para clientes ativos
         */
        public static void registerActiveCustomers(Supplier<Object[]> supplier) {
            register("clientes_ativos", supplier, TimeUnit.MINUTES.toMillis(15));
        }
        
        /**
         * Registra prefetch para estatísticas de vendas
         */
        public static void registerSalesStats(Supplier<Object[]> supplier) {
            register("estatisticas_vendas", supplier, TimeUnit.MINUTES.toMillis(5));
        }
        
        /**
         * Registra prefetch para categorias
         */
        public static void registerCategories(Supplier<Object[]> supplier) {
            register("categorias", supplier, TimeUnit.HOURS.toMillis(2));
        }
        
        /**
         * Registra prefetch para fornecedores
         */
        public static void registerSuppliers(Supplier<Object[]> supplier) {
            register("fornecedores", supplier, TimeUnit.HOURS.toMillis(2));
        }
        
        /**
         * Registra prefetch para vendas recentes
         */
        public static void registerRecentSales(Supplier<Object[]> supplier) {
            register("vendas_recentes", supplier, TimeUnit.MINUTES.toMillis(3));
        }
        
        /**
         * Registra prefetch para resumo financeiro
         */
        public static void registerFinancialSummary(Supplier<Object[]> supplier) {
            register("resumo_financeiro", supplier, TimeUnit.MINUTES.toMillis(10));
        }
    }
    
    /**
     * Shutdown do executor
     */
    public static void shutdown() {
        prefetchExecutor.shutdown();
        try {
            if (!prefetchExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                prefetchExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            prefetchExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
