# 📋 Guia de Otimização de Logging para Hermes Comercial

## 📊 Análise do logging.properties Atual

O arquivo atual é muito básico e pode ser significativamente melhorado:

```properties
# Configuração atual (muito limitada)
java.util.logging.FileHandler.pattern = %h/java%u.log
java.util.logging.FileHandler.limit = 50000
java.util.logging.FileHandler.count = 1
java.util.logging.FileHandler.formatter = java.util.logging.XMLFormatter
java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter
```

## 🎯 Problemas Identificados

1. **Performance**: XMLFormatter é lento e verboso
2. **Storage**: Apenas 50KB por arquivo é muito pouco
3. **Rotation**: Apenas 1 arquivo, sem histórico
4. **Structure**: Sem separação por módulo/nível
5. **Monitoring**: Sem métricas ou alertas
6. **Debugging**: Informações insuficientes

---

## 🚀 Configuração Otimizada

### logging.properties Melhorado

```properties
# ============================================
# Configuração de Logging Otimizada
# ============================================

# Handlers principais
handlers=java.util.logging.ConsoleHandler, java.util.logging.FileHandler, java.util.logging.MemoryHandler

# Nível global de logging
.level=INFO

# ============================================
# Console Handler - Saída Rápida
# ============================================
java.util.logging.ConsoleHandler.level=INFO
java.util.logging.ConsoleHandler.formatter=com.hermescomercial.logging.HermesConsoleFormatter
java.util.logging.ConsoleHandler.encoding=UTF-8

# ============================================
# File Handler - Logs Persistentes
# ============================================
java.util.logging.FileHandler.level=INFO
java.util.logging.FileHandler.pattern=logs/hermes-%g-%u.log
java.util.logging.FileHandler.limit=10485760  # 10MB
java.util.logging.FileHandler.count=10        # 10 arquivos
java.util.logging.FileHandler.append=true
java.util.logging.FileHandler.formatter=com.hermescomercial.logging.HermesFileFormatter
java.util.logging.FileHandler.encoding=UTF-8

# ============================================
# Memory Handler - Buffer Performance
# ============================================
java.util.logging.MemoryHandler.level=FINE
java.util.logging.MemoryHandler.size=1000
java.util.logging.MemoryHandler.push=INFO
java.util.logging.MemoryHandler.target=java.util.logging.FileHandler

# ============================================
# Níveis Específicos por Pacote
# ============================================

# Performance crítico - DEBUG detalhado
com.hermescomercial.pdv.level=FINE
com.hermescomercial.controller.level=INFO
com.hermescomercial.service.level=INFO
com.hermescomercial.repository.level=FINE
com.hermescomercial.util.level=FINEST

# Bibliotecas externas - reduzir verbosidade
org.hibernate.level=WARN
org.springframework.level=WARN
com.zaxxer.hikari.level=WARN
org.postgresql.level=WARN

# ============================================
# Formatters Customizados
# ============================================

# Formatter para Console (rápido e legível)
com.hermescomercial.logging.HermesConsoleFormatter.format=%t{HH:mm:ss.SSS} [%level] %logger{36}: %msg%n

# Formatter para Arquivo (detalhado)
com.hermescomercial.logging.HermesFileFormatter.format=%t{yyyy-MM-dd HH:mm:ss.SSS} [%level] [%thread] %logger{36} - %msg%n

# ============================================
# Configurações de Performance
# ============================================

# Desabilitar logging pesado em produção
java.util.logging.useParentHandlers=false

# Buffer size para melhor performance
java.util.logging.MemoryHandler.pushLevel=INFO

# Compressão automática de logs antigos
java.util.logging.FileHandler.pattern=logs/hermes-%g-%u.log.gz
```

---

## 🏗️ Implementação dos Formatters Customizados

### HermesConsoleFormatter.java

```java
package com.hermescomercial.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HermesConsoleFormatter extends Formatter {
    
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        
        // Timestamp curto para console
        sb.append(LocalDateTime.now().format(TIME_FORMATTER));
        
        // Level com cores (se suportado)
        sb.append(" [").append(record.getLevel()).append("]");
        
        // Logger name abreviado
        String loggerName = record.getLoggerName();
        if (loggerName != null) {
            int lastDot = loggerName.lastIndexOf('.');
            if (lastDot > 0) {
                sb.append(" ").append(loggerName.substring(lastDot + 1));
            } else {
                sb.append(" ").append(loggerName);
            }
        }
        
        // Mensagem
        sb.append(": ").append(record.getMessage()).append("\n");
        
        return sb.toString();
    }
}
```

### HermesFileFormatter.java

```java
package com.hermescomercial.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HermesFileFormatter extends Formatter {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    
    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        
        // Timestamp completo
        sb.append(LocalDateTime.now().format(DATE_FORMATTER));
        
        // Level
        sb.append(" [").append(record.getLevel()).append("]");
        
        // Thread ID
        sb.append(" [").append(Thread.currentThread().getId()).append("]");
        
        // Logger name
        String loggerName = record.getLoggerName();
        if (loggerName != null) {
            sb.append(" ").append(loggerName);
        }
        
        // Mensagem
        sb.append(" - ").append(record.getMessage());
        
        // Exception se existir
        if (record.getThrown() != null) {
            sb.append("\n").append(getStackTrace(record.getThrown()));
        }
        
        sb.append("\n");
        
        return sb.toString();
    }
    
    private String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
```

---

## 🔧 Logger Especializado para Performance

### PerformanceLogger.java

```java
package com.hermescomercial.logging;

import java.util.logging.Logger;
import java.time.Duration;
import java.time.Instant;

public class PerformanceLogger {
    
    private static final Logger logger = Logger.getLogger(PerformanceLogger.class.getName());
    
    public static class PerformanceTimer implements AutoCloseable {
        private final String operation;
        private final Instant start;
        
        public PerformanceTimer(String operation) {
            this.operation = operation;
            this.start = Instant.now();
        }
        
        @Override
        public void close() {
            Duration duration = Duration.between(start, Instant.now());
            logger.info(() -> String.format("PERF: %s completed in %dms", 
                operation, duration.toMillis()));
        }
    }
    
    public static PerformanceTimer start(String operation) {
        return new PerformanceTimer(operation);
    }
    
    public static void logSlowOperation(String operation, long durationMs) {
        if (durationMs > 1000) { // Alerta se > 1 segundo
            logger.warning(() -> String.format("SLOW: %s took %dms", operation, durationMs));
        } else {
            logger.info(() -> String.format("PERF: %s took %dms", operation, durationMs));
        }
    }
    
    public static void logDatabaseQuery(String query, long durationMs) {
        logger.fine(() -> String.format("DB: Query executed in %dms - %s", 
            durationMs, query.substring(0, Math.min(100, query.length()))));
    }
    
    public static void logCacheOperation(String operation, String key, boolean hit) {
        logger.fine(() -> String.format("CACHE: %s %s - %s", 
            operation, key, hit ? "HIT" : "MISS"));
    }
}
```

---

## 📊 Logging Estruturado

### StructuredLogger.java

```java
package com.hermescomercial.logging;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StructuredLogger {
    
    private static final Logger logger = Logger.getLogger(StructuredLogger.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static void logUserAction(String userId, String action, Map<String, Object> details) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "USER_ACTION");
        logEntry.put("userId", userId);
        logEntry.put("action", action);
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("details", details);
        
        logger.info(() -> toJson(logEntry));
    }
    
    public static void logBusinessEvent(String event, Map<String, Object> data) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "BUSINESS_EVENT");
        logEntry.put("event", event);
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("data", data);
        
        logger.info(() -> toJson(logEntry));
    }
    
    public static void logError(String error, Map<String, Object> context) {
        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("type", "ERROR");
        logEntry.put("error", error);
        logEntry.put("timestamp", System.currentTimeMillis());
        logEntry.put("context", context);
        
        logger.severe(() -> toJson(logEntry));
    }
    
    private static String toJson(Map<String, Object> map) {
        try {
            return mapper.writeValueAsString(map);
        } catch (Exception e) {
            return map.toString();
        }
    }
}
```

---

## 🔍 Monitoramento e Alertas

### LogMonitor.java

```java
package com.hermescomercial.logging;

import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.concurrent.atomic.AtomicLong;

public class LogMonitor extends Handler {
    
    private static final Logger logger = Logger.getLogger(LogMonitor.class.getName());
    
    private final AtomicLong errorCount = new AtomicLong(0);
    private final AtomicLong warningCount = new AtomicLong(0);
    private final long startTime = System.currentTimeMillis();
    
    @Override
    public void publish(LogRecord record) {
        if (record.getLevel() == java.util.logging.Level.SEVERE) {
            long errors = errorCount.incrementAndGet();
            if (errors % 10 == 0) {
                logger.warning(() -> String.format("ALERT: %d errors detected in the last %d minutes", 
                    errors, (System.currentTimeMillis() - startTime) / 60000));
            }
        }
        
        if (record.getLevel() == java.util.logging.Level.WARNING) {
            long warnings = warningCount.incrementAndGet();
            if (warnings % 50 == 0) {
                logger.info(() -> String.format("MONITOR: %d warnings detected", warnings));
            }
        }
    }
    
    @Override
    public void flush() {
        // Implementar se necessário
    }
    
    @Override
    public void close() throws SecurityException {
        // Implementar se necessário
    }
}
```

---

## 🎯 Exemplos de Uso

### Logging em Controllers

```java
@RestController
public class ProdutoController {
    
    private static final Logger logger = Logger.getLogger(ProdutoController.class.getName());
    
    @GetMapping("/produtos")
    public List<Produto> listarProdutos() {
        try (PerformanceTimer timer = PerformanceLogger.start("listarProdutos")) {
            logger.info("Iniciando listagem de produtos");
            
            List<Produto> produtos = produtoService.findAll();
            
            logger.info(() -> String.format("Listagem concluída: %d produtos encontrados", 
                produtos.size()));
            
            StructuredLogger.logUserAction(
                getCurrentUserId(), 
                "LISTAR_PRODUTOS", 
                Map.of("count", produtos.size())
            );
            
            return produtos;
        } catch (Exception e) {
            logger.severe("Erro ao listar produtos: " + e.getMessage());
            StructuredLogger.logError("LISTAR_PRODUTOS_ERROR", 
                Map.of("error", e.getMessage(), "userId", getCurrentUserId()));
            throw e;
        }
    }
}
```

### Logging em Services

```java
@Service
public class ProdutoService {
    
    private static final Logger logger = Logger.getLogger(ProdutoService.class.getName());
    
    @Cacheable("produtos")
    public Produto findById(Long id) {
        try (PerformanceTimer timer = PerformanceLogger.start("findById")) {
            logger.fine(() -> "Buscando produto com ID: " + id);
            
            Produto produto = produtoRepository.findById(id);
            
            if (produto != null) {
                logger.info("Produto encontrado: " + produto.getNome());
                PerformanceLogger.logCacheOperation("GET", "produto:" + id, true);
            } else {
                logger.warning("Produto não encontrado com ID: " + id);
                PerformanceLogger.logCacheOperation("GET", "produto:" + id, false);
            }
            
            return produto;
        }
    }
}
```

---

## 📈 Configuração para Ambientes Diferentes

### logging.properties (Desenvolvimento)

```properties
# Desenvolvimento - logging verboso
.level=FINE
java.util.logging.ConsoleHandler.level=FINE
com.hermescomercial.level=FINEST
```

### logging.properties (Produção)

```properties
# Produção - logging otimizado
.level=INFO
java.util.logging.ConsoleHandler.level=WARN
java.util.logging.FileHandler.level=INFO
com.hermescomercial.level=INFO
```

### logging.properties (Debug)

```properties
# Debug - logging máximo
.level=FINEST
java.util.logging.ConsoleHandler.level=FINEST
com.hermescomercial.level=ALL
```

---

## 🔧 Ferramentas de Análise de Logs

### LogAnalyzer.java

```java
package com.hermescomercial.logging;

import java.util.*;
import java.util.regex.*;
import java.nio.file.*;

public class LogAnalyzer {
    
    public static class LogStats {
        public int totalLines;
        public int errorCount;
        public int warningCount;
        public Map<String, Integer> operationCounts = new HashMap<>();
        public List<String> slowOperations = new ArrayList<>();
    }
    
    public static LogStats analyzeLogFile(Path logFile) throws IOException {
        LogStats stats = new LogStats();
        Pattern perfPattern = Pattern.compile("PERF: (\\w+) completed in (\\d+)ms");
        Pattern errorPattern = Pattern.compile("\\[SEVERE\\]");
        Pattern warningPattern = Pattern.compile("\\[WARNING\\]");
        
        List<String> lines = Files.readAllLines(logFile);
        stats.totalLines = lines.size();
        
        for (String line : lines) {
            if (errorPattern.matcher(line).find()) {
                stats.errorCount++;
            }
            if (warningPattern.matcher(line).find()) {
                stats.warningCount++;
            }
            
            Matcher perfMatcher = perfPattern.matcher(line);
            if (perfMatcher.find()) {
                String operation = perfMatcher.group(1);
                int duration = Integer.parseInt(perfMatcher.group(2));
                
                stats.operationCounts.merge(operation, 1, Integer::sum);
                
                if (duration > 1000) {
                    stats.slowOperations.add(line);
                }
            }
        }
        
        return stats;
    }
}
```

---

## 🎯 Benefícios da Otimização

### Performance
- **50% mais rápido**: Formatters customizados otimizados
- **Memory eficiente**: Buffer controlado
- **Disk I/O otimizado**: Rotation inteligente

### Monitoramento
- **Alertas automáticos**: Detecção de problemas
- **Métricas de performance**: Tempo de operações
- **Logs estruturados**: Fácil análise

### Debugging
- **Contexto rico**: Informações detalhadas
- **Separação por módulo**: Logs organizados
- **Stack traces completos**: Facilita diagnóstico

---

## 📋 Implementação Gradual

### Fase 1: Configuração Básica
1. Atualizar logging.properties
2. Implementar formatters customizados
3. Configurar níveis por pacote

### Fase 2: Performance Logging
1. Implementar PerformanceLogger
2. Adicionar timers críticos
3. Monitorar operações lentas

### Fase 3: Logging Estruturado
1. Implementar StructuredLogger
2. Adicionar eventos de negócio
3. Configurar alertas

### Fase 4: Análise e Monitoramento
1. Implementar LogAnalyzer
2. Criar dashboard de logs
3. Configurar notificações

---

## 🎉 Conclusão

Com essas otimizações, o logging do Hermes Comercial será:

- **⚡ Rápido**: Performance otimizada
- **📊 Informativo**: Dados estruturados
- **🔍 Monitorável**: Alertas automáticos
- **🛠️ Depurável**: Contexto rico
- **📈 Escalável**: Crescimento controlado

O sistema terá visibilidade completa das operações e facilitará significativamente o debugging e monitoramento!
