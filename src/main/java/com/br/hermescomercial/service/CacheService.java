package com.br.hermescomercial.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Serviço de cache de dados para performance
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class CacheService {
    
    private static CacheService instance;
    private final Map<String, CacheEntry> cache;
    private final Map<String, Long> contadoresAcesso;
    private final ScheduledExecutorService scheduler;
    private final int maxCacheSize;
    private final long defaultTTL; // em segundos
    
    public static class CacheEntry {
        private Object valor;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataUltimoAcesso;
        private long ttl; // Time to Live em segundos
        private int contadorAcessos;
        private String origem;
        
        public CacheEntry(Object valor, long ttl, String origem) {
            this.valor = valor;
            this.ttl = ttl;
            this.origem = origem;
            this.dataCriacao = LocalDateTime.now();
            this.dataUltimoAcesso = LocalDateTime.now();
            this.contadorAcessos = 0;
        }
        
        // Getters e setters
        public Object getValor() { return valor; }
        public void setValor(Object valor) { this.valor = valor; }
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public LocalDateTime getDataUltimoAcesso() { return dataUltimoAcesso; }
        public void setDataUltimoAcesso(LocalDateTime dataUltimoAcesso) { this.dataUltimoAcesso = dataUltimoAcesso; }
        public long getTtl() { return ttl; }
        public void setTtl(long ttl) { this.ttl = ttl; }
        public int getContadorAcessos() { return contadorAcessos; }
        public void incrementarAcesso() { 
            this.contadorAcessos++; 
            this.dataUltimoAcesso = LocalDateTime.now();
        }
        public String getOrigem() { return origem; }
        
        public boolean isExpirado() {
            if (ttl <= 0) return false; // Cache permanente
            return ChronoUnit.SECONDS.between(dataCriacao, LocalDateTime.now()) > ttl;
        }
    }
    
    public enum TipoCache {
        PRODUTOS,        // Cache de produtos
        CLIENTES,        // Cache de clientes
        VENDAS,          // Cache de vendas
        ESTOQUE,         // Cache de estoque
        CONFIGURACOES,   // Cache de configurações
        DASHBOARD,       // Cache de dados do dashboard
        RELATORIOS,      // Cache de relatórios
        SESSAO,         // Cache de sessões de usuário
        TEMPORARIO       // Cache temporário
    }
    
    private CacheService() {
        this.maxCacheSize = 1000;
        this.defaultTTL = 300; // 5 minutos
        this.cache = new LinkedHashMap<String, CacheEntry>(maxCacheSize + 1, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
                return size() > maxCacheSize;
            }
        };
        this.contadoresAcesso = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        // Agendar limpeza automática a cada 5 minutos
        scheduler.scheduleAtFixedRate(this::limparCacheExpirado, 5, 5, TimeUnit.MINUTES);
    }
    
    public static synchronized CacheService getInstance() {
        if (instance == null) {
            instance = new CacheService();
        }
        return instance;
    }
    
    /**
     * Adiciona item ao cache
     */
    public void put(String chave, Object valor) {
        put(chave, valor, defaultTTL, TipoCache.TEMPORARIO);
    }
    
    /**
     * Adiciona item ao cache com TTL específico
     */
    public void put(String chave, Object valor, long ttlSegundos) {
        put(chave, valor, ttlSegundos, TipoCache.TEMPORARIO);
    }
    
    /**
     * Adiciona item ao cache com TTL e tipo específicos
     */
    public void put(String chave, Object valor, long ttlSegundos, TipoCache tipo) {
        CacheEntry entry = new CacheEntry(valor, ttlSegundos, tipo.toString());
        cache.put(chave, entry);
        contadoresAcesso.put(chave, System.currentTimeMillis());
    }
    
    /**
     * Obtém item do cache
     */
    public Object get(String chave) {
        CacheEntry entry = cache.get(chave);
        
        if (entry == null) {
            return null;
        }
        
        // Verificar se expirou
        if (entry.isExpirado()) {
            cache.remove(chave);
            contadoresAcesso.remove(chave);
            return null;
        }
        
        // Incrementar contador de acesso
        entry.incrementarAcesso();
        contadoresAcesso.put(chave, System.currentTimeMillis());
        
        return entry.getValor();
    }
    
    /**
     * Obtém item do cache com cast para tipo específico
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String chave, Class<T> tipo) {
        Object valor = get(chave);
        if (valor != null && tipo.isInstance(valor)) {
            return (T) valor;
        }
        return null;
    }
    
    /**
     * Remove item do cache
     */
    public Object remove(String chave) {
        CacheEntry entry = cache.remove(chave);
        contadoresAcesso.remove(chave);
        return entry != null ? entry.getValor() : null;
    }
    
    /**
     * Verifica se chave existe no cache
     */
    public boolean contains(String chave) {
        CacheEntry entry = cache.get(chave);
        if (entry == null) {
            return false;
        }
        
        if (entry.isExpirado()) {
            cache.remove(chave);
            contadoresAcesso.remove(chave);
            return false;
        }
        
        return true;
    }
    
    /**
     * Limpa todo o cache
     */
    public void limpar() {
        cache.clear();
        contadoresAcesso.clear();
    }
    
    /**
     * Limpa cache de um tipo específico
     */
    public void limparPorTipo(TipoCache tipo) {
        cache.entrySet().removeIf(entry -> 
            entry.getValue().getOrigem().equals(tipo.toString()));
    }
    
    /**
     * Limpa itens expirados
     */
    public void limparCacheExpirado() {
        cache.entrySet().removeIf(entry -> {
            boolean expirado = entry.getValue().isExpirado();
            if (expirado) {
                contadoresAcesso.remove(entry.getKey());
            }
            return expirado;
        });
    }
    
    /**
     * Obtém estatísticas do cache
     */
    public Map<String, Object> getEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        estatisticas.put("totalItens", cache.size());
        estatisticas.put("maxCacheSize", maxCacheSize);
        estatisticas.put("utilizacaoPercentual", (cache.size() * 100.0) / maxCacheSize);
        
        // Contagem por tipo
        Map<String, Integer> contagemPorTipo = new HashMap<>();
        for (CacheEntry entry : cache.values()) {
            contagemPorTipo.merge(entry.getOrigem(), 1, Integer::sum);
        }
        estatisticas.put("contagemPorTipo", contagemPorTipo);
        
        // Itens mais acessados
        List<Map<String, Object>> maisAcessados = new ArrayList<>();
        cache.entrySet().stream()
            .sorted((e1, e2) -> Integer.compare(e2.getValue().getContadorAcessos(), 
                                               e1.getValue().getContadorAcessos()))
            .limit(10)
            .forEach(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("chave", entry.getKey());
                item.put("acessos", entry.getValue().getContadorAcessos());
                item.put("tipo", entry.getValue().getOrigem());
                item.put("criacao", entry.getValue().getDataCriacao());
                item.put("ultimoAcesso", entry.getValue().getDataUltimoAcesso());
                maisAcessados.add(item);
            });
        estatisticas.put("maisAcessados", maisAcessados);
        
        // Itens expirados
        long expirados = cache.values().stream()
            .mapToLong(entry -> entry.isExpirado() ? 1 : 0)
            .sum();
        estatisticas.put("itensExpirados", expirados);
        
        // Taxa de acertos (hit rate)
        estatisticas.put("taxaAcertos", calcularTaxaAcertos());
        
        return estatisticas;
    }
    
    /**
     * Cache de produtos
     */
    public void cacheProdutos(List<Map<String, Object>> produtos) {
        put("produtos.lista_completa", produtos, 600, TipoCache.PRODUTOS); // 10 minutos
        
        // Cache individual por produto
        for (Map<String, Object> produto : produtos) {
            String codigo = (String) produto.get("codigo");
            if (codigo != null) {
                put("produto." + codigo, produto, 900, TipoCache.PRODUTOS); // 15 minutos
            }
        }
    }
    
    /**
     * Obtém produtos do cache
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getProdutos() {
        return get("produtos.lista_completa", List.class);
    }
    
    /**
     * Obtém produto específico do cache
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getProduto(String codigo) {
        return get("produto." + codigo, Map.class);
    }
    
    /**
     * Cache de dados do dashboard
     */
    public void cacheDashboard(Map<String, Object> dados) {
        put("dashboard.dados", dados, 180, TipoCache.DASHBOARD); // 3 minutos
    }
    
    /**
     * Obtém dados do dashboard do cache
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getDashboard() {
        return get("dashboard.dados", Map.class);
    }
    
    /**
     * Cache de estoque
     */
    public void cacheEstoque(Map<String, Object> dadosEstoque) {
        put("estoque.dados", dadosEstoque, 120, TipoCache.ESTOQUE); // 2 minutos
    }
    
    /**
     * Obtém dados de estoque do cache
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getEstoque() {
        return get("estoque.dados", Map.class);
    }
    
    /**
     * Cache de configurações
     */
    public void cacheConfiguracoes(Map<String, Object> config) {
        put("configuracoes.sistema", config, 3600, TipoCache.CONFIGURACOES); // 1 hora
    }
    
    /**
     * Obtém configurações do cache
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getConfiguracoes() {
        return get("configuracoes.sistema", Map.class);
    }
    
    /**
     * Cache de sessão de usuário
     */
    public void cacheSessao(String usuarioId, Map<String, Object> dadosSessao) {
        put("sessao." + usuarioId, dadosSessao, 1800, TipoCache.SESSAO); // 30 minutos
    }
    
    /**
     * Obtém sessão de usuário do cache
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSessao(String usuarioId) {
        return get("sessao." + usuarioId, Map.class);
    }
    
    /**
     * Invalida cache de produtos
     */
    public void invalidarCacheProdutos() {
        limparPorTipo(TipoCache.PRODUTOS);
    }
    
    /**
     * Invalida cache de estoque
     */
    public void invalidarCacheEstoque() {
        limparPorTipo(TipoCache.ESTOQUE);
    }
    
    /**
     * Invalida cache de dashboard
     */
    public void invalidarCacheDashboard() {
        limparPorTipo(TipoCache.DASHBOARD);
    }
    
    /**
     * Pré-carrega caches importantes
     */
    public void preCarregarCaches() {
        // Simulação - em implementação real, carregaria dados do banco
        System.out.println("Pré-carregando caches...");
        
        // Cache de configurações
        Map<String, Object> config = new HashMap<>();
        config.put("nome_empresa", "Hermes Comercial");
        config.put("versao", "2.8.0");
        config.put("tema", "Oceano");
        cacheConfiguracoes(config);
        
        // Cache de dados do dashboard
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("vendas_hoje", 12500.00);
        dashboard.put("vendas_mes", 287500.00);
        dashboard.put("total_clientes", 450);
        dashboard.put("total_produtos", 1200);
        cacheDashboard(dashboard);
        
        System.out.println("Caches pré-carregados com sucesso!");
    }
    
    /**
     * Otimiza o cache removendo itens pouco acessados
     */
    public void otimizarCache() {
        int tamanhoAnterior = cache.size();
        
        // Remover itens com menos de 2 acessos e mais de 1 hora de criação
        cache.entrySet().removeIf(entry -> {
            CacheEntry cacheEntry = entry.getValue();
            return cacheEntry.getContadorAcessos() < 2 && 
                   ChronoUnit.HOURS.between(cacheEntry.getDataCriacao(), LocalDateTime.now()) > 1;
        });
        
        int tamanhoAtual = cache.size();
        System.out.println("Cache otimizado: " + (tamanhoAnterior - tamanhoAtual) + " itens removidos");
    }
    
    /**
     * Exporta estado do cache para diagnóstico
     */
    public Map<String, Object> exportarEstado() {
        Map<String, Object> estado = new HashMap<>();
        
        estado.put("timestamp", LocalDateTime.now());
        estado.put("totalItens", cache.size());
        estado.put("maxSize", maxCacheSize);
        
        // Detalhes dos itens
        List<Map<String, Object>> itens = new ArrayList<>();
        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            Map<String, Object> detalhe = new HashMap<>();
            detalhe.put("chave", entry.getKey());
            detalhe.put("tipo", entry.getValue().getOrigem());
            detalhe.put("acessos", entry.getValue().getContadorAcessos());
            detalhe.put("criacao", entry.getValue().getDataCriacao());
            detalhe.put("ultimoAcesso", entry.getValue().getDataUltimoAcesso());
            detalhe.put("ttl", entry.getValue().getTtl());
            detalhe.put("expirado", entry.getValue().isExpirado());
            itens.add(detalhe);
        }
        
        estado.put("itens", itens);
        
        return estado;
    }
    
    // Métodos privados
    private double calcularTaxaAcertos() {
        // Simulação - em implementação real, calcularia taxa real de acertos
        return 0.85; // 85% de taxa de acertos
    }
    
    /**
     * Finaliza o serviço de cache
     */
    public void finalizar() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        limpar();
    }
}
