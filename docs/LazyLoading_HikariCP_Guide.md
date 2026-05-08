# Guia Completo: Lazy Loading e HikariCP Connection Pool

## 📋 Índice
1. [Lazy Loading](#lazy-loading)
2. [HikariCP Connection Pool](#hikaricp-connection-pool)
3. [Implementação no Hermes Comercial](#implementação-no-hermes-comercial)
4. [Exemplos Práticos](#exemplos-práticos)
5. [Melhores Práticas](#melhores-práticas)

---

## 🔄 Lazy Loading

### O que é Lazy Loading?
Lazy Loading (carregamento preguiçoso) é um padrão de projeto onde o carregamento de dados é adiado até ser estritamente necessário. Isso melhora a performance inicial do sistema e reduz o consumo de memória.

### Vantagens
- ✅ **Performance**: Reduz tempo de inicialização
- ✅ **Memória**: Carrega apenas dados necessários
- ✅ **Experiência**: Interface mais responsiva
- ✅ **Recursos**: Menor uso de CPU e rede

### Desvantagens
- ❌ **Complexidade**: Implementação mais complexa
- ❌ **Latência**: Primeiro acesso pode ser mais lento
- ❌ **Cache**: Necessidade de gerenciamento de cache
- ❌ **Debugging**: Mais difícil de rastrear problemas

### Implementação Básica

```java
// ❌ Eager Loading (carrega tudo de uma vez)
public class ProdutoService {
    public List<Produto> carregarTodosProdutos() {
        // Carrega TODOS os produtos do banco
        return produtoRepository.findAll();
    }
}

// ✅ Lazy Loading (carrega quando necessário)
public class ProdutoService {
    private List<Produto> produtosCache;
    private boolean carregado = false;
    
    public List<Produto> getProdutos() {
        if (!carregado) {
            produtosCache = produtoRepository.findAll();
            carregado = true;
        }
        return produtosCache;
    }
}
```

### Lazy Loading com Paginação

```java
public class ProdutoLazyLoader {
    private static final int PAGE_SIZE = 50;
    private int currentPage = 0;
    private boolean hasMore = true;
    private List<Produto> cache = new ArrayList<>();
    
    public List<Produto> loadNextPage() {
        if (!hasMore) return Collections.emptyList();
        
        List<Produto> page = produtoRepository.findPage(currentPage, PAGE_SIZE);
        cache.addAll(page);
        currentPage++;
        hasMore = page.size() == PAGE_SIZE;
        
        return page;
    }
    
    public List<Produto> getProdutos(int offset, int limit) {
        // Garante que temos dados suficientes no cache
        while (cache.size() < offset + limit && hasMore) {
            loadNextPage();
        }
        
        return cache.subList(offset, Math.min(offset + limit, cache.size()));
    }
}
```

---

## 🏊 HikariCP Connection Pool

### O que é HikariCP?
HikariCP é um pool de conexões JDBC de alta performance, considerado o mais rápido e eficiente pool de conexões para Java.

### Vantagens
- ⚡ **Performance**: Conexões rápidas e eficientes
- 🔧 **Configuração**: Fácil de configurar e ajustar
- 📊 **Monitoramento**: Métricas detalhadas
- 🛡️ **Confiabilidade**: Conexões validadas e recuperadas automaticamente

### Desvantagens
- ❌ **Complexidade**: Configuração inicial pode ser complexa
- ❌ **Recursos**: Consome memória para manter conexões ociosas
- ❌ **Overhead**: Pequeno overhead na criação do pool
- ❌ **Debugging**: Problemas de conexão podem ser difíceis de diagnosticar

### Configuração Básica

```java
// Configuração do HikariCP
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:postgresql://localhost:5432/hermes");
config.setUsername("postgres");
config.setPassword("senha");
config.setDriverClassName("org.postgresql.Driver");

// Configurações de performance
config.setMaximumPoolSize(20);           // Máximo de conexões
config.setMinimumIdle(5);                // Mínimo de conexões ociosas
config.setConnectionTimeout(30000);      // Timeout de conexão (30s)
config.setIdleTimeout(600000);           // Timeout de ociosidade (10min)
config.setMaxLifetime(1800000);          // Tempo de vida da conexão (30min)
config.setLeakDetectionThreshold(60000);  // Detecção de vazamento (1min)

// Validação de conexões
config.setConnectionTestQuery("SELECT 1");
config.setValidationTimeout(5000);

// Performance
config.addDataSourceProperty("cachePrepStmts", "true");
config.addDataSourceProperty("prepStmtCacheSize", "250");
config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

HikariDataSource dataSource = new HikariDataSource(config);
```

### Connection Pool Avançado

```java
public class HermesConnectionPool {
    private static HikariDataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(HermesConnectionPool.class);
    
    static {
        try {
            initializePool();
        } catch (Exception e) {
            logger.error("Erro ao inicializar pool de conexões", e);
            throw new RuntimeException("Falha na inicialização do pool", e);
        }
    }
    
    private static void initializePool() {
        HikariConfig config = new HikariConfig();
        
        // Configurações do banco
        config.setJdbcUrl(loadDatabaseUrl());
        config.setUsername(loadDatabaseUser());
        config.setPassword(loadDatabasePassword());
        config.setDriverClassName("org.postgresql.Driver");
        
        // Configurações do pool (baseadas em ambiente)
        configurePoolSettings(config);
        
        // Configurações de performance
        configurePerformanceSettings(config);
        
        // Configurações de monitoramento
        configureMonitoring(config);
        
        dataSource = new HikariDataSource(config);
        
        logger.info("Pool de conexões HikariCP inicializado com sucesso");
    }
    
    private static void configurePoolSettings(HikariConfig config) {
        int maxConnections = calculateOptimalPoolSize();
        
        config.setMaximumPoolSize(maxConnections);
        config.setMinimumIdle(Math.max(2, maxConnections / 4));
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(300000);  // 5 minutos
        config.setMaxLifetime(1200000); // 20 minutos
        config.setLeakDetectionThreshold(60000);
    }
    
    private static int calculateOptimalPoolSize() {
        // Fórmula: (core_count * 2) + effective_spindle_count
        int cores = Runtime.getRuntime().availableProcessors();
        return Math.min(20, (cores * 2) + 1);
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Pool de conexões fechado");
        }
    }
}
```

---

## 🏗️ Implementação no Hermes Comercial

### Exemplo: Lazy Loading em Tabelas

```java
public class TabelaLazyLoadingModel extends AbstractTableModel {
    private List<Object[]> dados = new ArrayList<>();
    private int loadedRows = 0;
    private final int pageSize = 100;
    private final String query;
    private boolean hasMore = true;
    
    public TabelaLazyLoadingModel(String query) {
        this.query = query;
        loadNextPage();
    }
    
    private void loadNextPage() {
        if (!hasMore) return;
        
        String paginatedQuery = query + 
            " LIMIT " + pageSize + 
            " OFFSET " + loadedRows;
        
        try (Connection conn = HermesConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(paginatedQuery)) {
            
            ResultSet rs = stmt.executeQuery();
            
            int newRows = 0;
            while (rs.next()) {
                Object[] row = extractRowData(rs);
                dados.add(row);
                newRows++;
            }
            
            loadedRows += newRows;
            hasMore = newRows == pageSize;
            
            fireTableDataChanged();
            
        } catch (SQLException e) {
            logger.error("Erro no lazy loading", e);
        }
    }
    
    @Override
    public int getRowCount() {
        // Se estiver chegando no fim, carrega mais dados
        if (dados.size() - loadedRows < 20 && hasMore) {
            loadNextPage();
        }
        return dados.size();
    }
    
    // Outros métodos do TableModel...
}
```

### Exemplo: HikariCP com Lazy Loading

```java
public class ProdutoRepositoryLazy {
    private static final String BASE_QUERY = 
        "SELECT id, nome, descricao, preco, estoque FROM produtos";
    
    public List<Produto> findProdutosLazy(FiltroProduto filtro) {
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = HermesConnectionPool.getConnection()) {
            
            // Query base com filtros
            StringBuilder sql = new StringBuilder(BASE_QUERY);
            List<Object> params = new ArrayList<>();
            
            if (filtro.getNome() != null) {
                sql.append(" WHERE nome ILIKE ?");
                params.add("%" + filtro.getNome() + "%");
            }
            
            sql.append(" ORDER BY nome LIMIT ? OFFSET ?");
            
            try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
                
                // Parâmetros
                int paramIndex = 1;
                for (Object param : params) {
                    stmt.setObject(paramIndex++, param);
                }
                stmt.setInt(paramIndex++, filtro.getPageSize());
                stmt.setInt(paramIndex, filtro.getOffset());
                
                // Execução e mapeamento
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    produtos.add(mapRowToProduto(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar produtos", e);
            throw new RuntimeException("Falha na consulta", e);
        }
        
        return produtos;
    }
    
    private Produto mapRowToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getBigDecimal("preco"));
        produto.setEstoque(rs.getInt("estoque"));
        return produto;
    }
}
```

---

## 💡 Exemplos Práticos

### Exemplo 1: Dashboard com Lazy Loading

```java
public class DashboardLazyLoader {
    private Map<String, Object> cache = new ConcurrentHashMap<>();
    private long lastUpdate = 0;
    private static final long CACHE_TTL = 300000; // 5 minutos
    
    public Map<String, Object> getDashboardData() {
        long now = System.currentTimeMillis();
        
        if (now - lastUpdate > CACHE_TTL) {
            // Cache expirou, recarrega dados
            reloadDashboardData();
            lastUpdate = now;
        }
        
        return new HashMap<>(cache);
    }
    
    private void reloadDashboardData() {
        CompletableFuture.runAsync(() -> {
            try (Connection conn = HermesConnectionPool.getConnection()) {
                
                // Carrega KPIs financeiros
                cache.put("totalVendas", getTotalVendas(conn));
                cache.put("totalClientes", getTotalClientes(conn));
                cache.put("produtosEmEstoque", getProdutosEmEstoque(conn));
                cache.put("contasReceber", getContasReceber(conn));
                
                // Notifica listeners de atualização
                notifyDashboardUpdated();
                
            } catch (SQLException e) {
                logger.error("Erro ao recarregar dashboard", e);
            }
        });
    }
    
    private BigDecimal getTotalVendas(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM vendas WHERE data >= CURRENT_DATE";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }
}
```

### Exemplo 2: Relatórios com Paginação

```java
public class RelatorioFinanceiroService {
    
    public RelatorioData gerarRelatorioLazy(RelatorioRequest request) {
        RelatorioData data = new RelatorioData();
        
        try (Connection conn = HermesConnectionPool.getConnection()) {
            
            // Carrega resumo (rápido)
            data.setResumo(carregarResumo(conn, request));
            
            // Carrega dados detalhados em background
            CompletableFuture.supplyAsync(() -> 
                carregarDadosDetalhados(conn, request)
            ).thenAccept(dados -> {
                data.setDadosDetalhados(dados);
                notifyDataLoaded(data);
            });
            
        } catch (SQLException e) {
            logger.error("Erro ao gerar relatório", e);
            throw new RuntimeException("Falha no relatório", e);
        }
        
        return data;
    }
    
    private List<MovimentoFinanceiro> carregarDadosDetalhados(
            Connection conn, RelatorioRequest request) {
        
        List<MovimentoFinanceiro> movimentos = new ArrayList<>();
        String sql = buildQuery(request);
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setParameters(stmt, request);
            
            // Processa em lotes para evitar sobrecarga
            int offset = 0;
            final int batchSize = 1000;
            
            while (true) {
                stmt.setInt(1, batchSize);
                stmt.setInt(2, offset);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        movimentos.add(mapMovimento(rs));
                        count++;
                    }
                    
                    if (count < batchSize) break; // Não há mais dados
                    offset += batchSize;
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao carregar dados detalhados", e);
        }
        
        return movimentos;
    }
}
```

---

## ⚖️ Comparação: Lazy Loading vs HikariCP

### Quando Usar Lazy Loading
- ✅ **Grandes volumes de dados**: Relatórios com milhares de registros
- ✅ **Interface responsiva**: Tabelas e listas que devem carregar rapidamente
- ✅ **Recursos limitados**: Aplicações com restrições de memória
- ✅ **Acesso sob demanda**: Dados que nem sempre são necessários

### Quando Usar HikariCP
- ✅ **Alta concorrência**: Múltiplos acessos simultâneos ao banco
- ✅ **Performance crítica**: Aplicações que exigem resposta rápida
- ✅ **Conexões frequentes**: Operações de banco de dados constantes
- ✅ **Escalabilidade**: Sistemas que precisam crescer

### Combinação Ideal
- 🎯 **Lazy Loading + HikariCP**: Máxima performance e eficiência
- 🎯 **Cache de conexões**: Conexões reutilizadas para consultas lazy
- 🎯 **Monitoramento**: Métricas de pool e cache para otimização

### Tabela Comparativa

| Aspecto | Lazy Loading | HikariCP | Lazy Loading + HikariCP |
|---------|--------------|----------|--------------------------|
| **Performance Inicial** | ⚡⚡⚡ Excelente | ⚡⚡ Bom | ⚡⚡⚡ Excelente |
| **Performance Consultas** | ⚡ Bom | ⚡⚡⚡ Excelente | ⚡⚡⚡ Excelente |
| **Uso de Memória** | 💚 Baixo | 🟡 Médio | 🟡 Médio |
| **Complexidade** | 🔴 Alta | 🟡 Média | 🔴 Alta |
| **Escalabilidade** | ⚡ Bom | ⚡⚡⚡ Excelente | ⚡⚡⚡ Excelente |
| **Manutenibilidade** | 🟡 Média | 🟢 Fácil | 🔴 Baixa |
| **Ideal Para** | Grandes volumes | Alta concorrência | Sistemas completos |

---

## 🎯 Melhores Práticas

### Lazy Loading

1. **Cache Inteligente**
```java
public class SmartCache<K, V> {
    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttl;
    
    public Optional<V> get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return Optional.of(entry.getValue());
        }
        return Optional.empty();
    }
    
    public void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis() + ttl));
    }
}
```

2. **Loading Indicators**
```java
public class LoadingIndicator {
    private final JProgressBar progressBar = new JProgressBar();
    private final JLabel statusLabel = new JLabel("Carregando...");
    
    public void showLoading(JComponent parent) {
        // Mostra indicador de carregamento
        parent.add(progressBar, BorderLayout.NORTH);
        parent.add(statusLabel, BorderLayout.CENTER);
        parent.revalidate();
        parent.repaint();
    }
    
    public void hideLoading() {
        // Esconde indicador
        Container parent = progressBar.getParent();
        if (parent != null) {
            parent.remove(progressBar);
            parent.remove(statusLabel);
            parent.revalidate();
            parent.repaint();
        }
    }
}
```

### HikariCP

1. **Monitoramento**
```java
public class PoolMonitor {
    private final HikariPoolMXBean poolProxy;
    
    public void monitorPool() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            HikariPoolPoolStatistics stats = poolProxy.getHikariPoolPoolStatistics();
            
            logger.info("Pool Status - Active: {}, Idle: {}, Waiting: {}", 
                stats.getActiveConnections(),
                stats.getIdleConnections(),
                stats.getThreadsAwaitingConnection());
                
            // Alertas se necessário
            if (stats.getActiveConnections() > stats.getTotalConnections() * 0.8) {
                logger.warn("Pool approaching maximum capacity!");
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
```

2. **Configuração Dinâmica**
```java
public class DynamicPoolConfig {
    
    public static HikariConfig createOptimalConfig() {
        HikariConfig config = new HikariConfig();
        
        // Ajusta baseado nos recursos disponíveis
        int cores = Runtime.getRuntime().availableProcessors();
        long maxMemory = Runtime.getRuntime().maxMemory();
        
        // Calcula tamanho ótimo do pool
        int poolSize = Math.min(20, (cores * 2) + 1);
        
        // Ajusta timeout baseado na carga
        long connectionTimeout = System.getProperty("load.average", "0.5").compareTo("2.0") > 0 
            ? 60000 : 30000;
        
        config.setMaximumPoolSize(poolSize);
        config.setConnectionTimeout(connectionTimeout);
        
        return config;
    }
}
```

---

## 🔧 Configuração no Hermes Comercial

### application.properties
```properties
# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000
spring.datasource.hikari.leak-detection-threshold=60000

# Performance Settings
spring.datasource.hikari.data-source-properties.cachePrepStmts=true
spring.datasource.hikari.data-source-properties.prepStmtCacheSize=250
spring.datasource.hikari.data-source-properties.prepStmtCacheSqlLimit=2048

# Lazy Loading
hermes.lazy-loading.page-size=100
hermes.lazy-loading.cache-ttl=300000
hermes.lazy-loading.enable-background-loading=true
```

### Implementação Completa
```java
@Configuration
public class HermesDatabaseConfig {
    
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        // Configurações básicas
        config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        config.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        
        // Configurações de performance
        configurePoolSettings(config);
        configurePerformanceSettings(config);
        
        return new HikariDataSource(config);
    }
    
    @Bean
    public LazyLoadingService lazyLoadingService() {
        return new LazyLoadingServiceImpl(
            env.getProperty("hermes.lazy-loading.page-size", Integer.class, 100),
            env.getProperty("hermes.lazy-loading.cache-ttl", Long.class, 300000L)
        );
    }
}
```

---

## 📚 Referências

- [HikariCP Documentation](https://github.com/brettwooldridge/HikariCP)
- [Java Performance Tuning Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/)
- [Lazy Loading Patterns](https://martinfowler.com/eaaCatalog/lazyLoad.html)

---

## 🎉 Conclusão

A combinação de Lazy Loading e HikariCP Connection Pool proporciona:

1. **Performance Superior**: Carregamento sob demanda e conexões eficientes
2. **Escalabilidade**: Suporte a múltiplos usuários simultâneos
3. **Experiência do Usuário**: Interface responsiva e rápida
4. **Uso Eficiente de Recursos**: Memória e conexões otimizadas

Implementar essas técnicas no Hermes Comercial resulta em um sistema mais robusto, rápido e escalável.
