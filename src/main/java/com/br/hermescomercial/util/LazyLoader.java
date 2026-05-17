package com.br.hermescomercial.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Lazy Loader para Componentes Pesados do Hermes Comercial PDV
 * Carrega componentes apenas quando necessário para melhorar performance
 */
public class LazyLoader {
    
    private static final Map<String, LazyObject<?>> lazyObjects = new ConcurrentHashMap<>();
    
    /**
     * Objeto lazy que carrega o valor apenas na primeira utilização
     */
    private static class LazyObject<T> {
        private volatile T value;
        private volatile boolean loaded = false;
        private final Supplier<T> supplier;
        private final Object lock = new Object();
        
        LazyObject(Supplier<T> supplier) {
            this.supplier = supplier;
        }
        
        public T get() {
            if (!loaded) {
                synchronized (lock) {
                    if (!loaded) {
                        value = supplier.get();
                        loaded = true;
                    }
                }
            }
            return value;
        }
        
        public boolean isLoaded() {
            return loaded;
        }
        
        public void reset() {
            synchronized (lock) {
                value = null;
                loaded = false;
            }
        }
    }
    
    /**
     * Registra um componente lazy
     */
    public static <T> void register(String key, Supplier<T> supplier) {
        lazyObjects.put(key, new LazyObject<>(supplier));
    }
    
    /**
     * Obtém um componente lazy
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        LazyObject<T> lazyObject = (LazyObject<T>) lazyObjects.get(key);
        return lazyObject != null ? lazyObject.get() : null;
    }
    
    /**
     * Verifica se um componente já foi carregado
     */
    public static boolean isLoaded(String key) {
        LazyObject<?> lazyObject = lazyObjects.get(key);
        return lazyObject != null && lazyObject.isLoaded();
    }
    
    /**
     * Força o carregamento de um componente
     */
    public static void preload(String key) {
        get(key); // Apenas chamar get() força o carregamento
    }
    
    /**
     * Reseta um componente (será recarregado na próxima utilização)
     */
    public static void reset(String key) {
        LazyObject<?> lazyObject = lazyObjects.get(key);
        if (lazyObject != null) {
            lazyObject.reset();
        }
    }
    
    /**
     * Remove um componente do cache lazy
     */
    public static void remove(String key) {
        lazyObjects.remove(key);
    }
    
    /**
     * Limpa todos os componentes lazy
     */
    public static void clear() {
        lazyObjects.clear();
    }
    
    /**
     * Obtém estatísticas dos componentes lazy
     */
    public static String getStatistics() {
        int total = lazyObjects.size();
        int loaded = 0;
        
        for (LazyObject<?> lazyObject : lazyObjects.values()) {
            if (lazyObject.isLoaded()) {
                loaded++;
            }
        }
        
        return String.format("📊 LazyLoader Stats: %d total, %d carregados, %d pendentes", 
                           total, loaded, total - loaded);
    }
    
    /**
     * Factory para componentes UI pesados
     */
    public static class UIFactory {
        
        /**
         * Cria um painel complexo lazy
         */
        public static <T extends javax.swing.JPanel> void registerPanel(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando painel pesado: " + key);
                long start = System.currentTimeMillis();
                T panel = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Painel " + key + " carregado em " + (end - start) + "ms");
                return panel;
            });
        }
        
        /**
         * Cria uma tabela complexa lazy
         */
        public static <T extends javax.swing.JTable> void registerTable(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando tabela complexa: " + key);
                long start = System.currentTimeMillis();
                T table = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Tabela " + key + " carregada em " + (end - start) + "ms");
                return table;
            });
        }
        
        /**
         * Cria um relatório complexo lazy
         */
        public static <T> void registerReport(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando relatório complexo: " + key);
                long start = System.currentTimeMillis();
                T report = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Relatório " + key + " carregado em " + (end - start) + "ms");
                return report;
            });
        }
    }
    
    /**
     * Factory para dados de negócio
     */
    public static class DataFactory {
        
        /**
         * Regista lista de produtos lazy
         */
        public static <T> void registerProductList(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando lista de produtos: " + key);
                long start = System.currentTimeMillis();
                T data = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Lista de produtos " + key + " carregada em " + (end - start) + "ms");
                return data;
            });
        }
        
        /**
         * Regista lista de clientes lazy
         */
        public static <T> void registerCustomerList(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando lista de clientes: " + key);
                long start = System.currentTimeMillis();
                T data = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Lista de clientes " + key + " carregada em " + (end - start) + "ms");
                return data;
            });
        }
        
        /**
         * Regista dados financeiros lazy
         */
        public static <T> void registerFinancialData(String key, Supplier<T> supplier) {
            register(key, () -> {
                System.out.println("🔄 Carregando dados financeiros: " + key);
                long start = System.currentTimeMillis();
                T data = supplier.get();
                long end = System.currentTimeMillis();
                System.out.println("✅ Dados financeiros " + key + " carregados em " + (end - start) + "ms");
                return data;
            });
        }
    }
}
