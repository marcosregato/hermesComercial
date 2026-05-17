# Configuração de Logging - Hermes Comercial

## Visão Geral

O Hermes Comercial usa Log4j 2 para logging estruturado. Existem três arquivos de configuração disponíveis:

1. `log4j2.xml` - Configuração atual (XML)
2. `log4j2-structured.xml` - Configuração estruturada com múltiplos appenders
3. `log4j2.properties` - Configuração alternativa (Properties)

## Ativar Logging Estruturado

### Opção 1: Renomear arquivo

```bash
# Renomear para ativar logging estruturado
mv src/main/resources/log4j2.xml src/main/resources/log4j2-old.xml
mv src/main/resources/log4j2-structured.xml src/main/resources/log4j2.xml
```

### Opção 2: Configuração JVM

Adicione ao comando de execução:

```bash
java -Dlog4j.configurationFile=log4j2-structured.xml -jar hermespdv-standalone.jar
```

### Opção 3: Configuração Maven

Adicione ao `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <systemPropertyVariables>
            <log4j.configurationFile>log4j2-structured.xml</log4j.configurationFile>
        </systemPropertyVariables>
    </configuration>
</plugin>
```

## Estrutura de Logging Estruturado

### Appenders

1. **Console** - Saída para console (stdout)
2. **RollingFile** - Logs gerais com rotação
3. **ErrorFile** - Apenas erros com stack trace
4. **AuditFile** - Logs de auditoria

### Loggers

- **Root** - Nível info, console e rolling file
- **com.br.hermescomercial** - Nível debug, console e rolling file
- **com.br.hermescomercial.service** - Nível debug, console e rolling file
- **com.br.hermescomercial.dao** - Nível debug, apenas rolling file
- **com.br.hermescomercial.repository** - Nível debug, apenas rolling file
- **com.br.hermescomercial.controller** - Nível info, apenas rolling file
- **error** - Nível error, apenas error file
- **audit** - Nível info, apenas audit file

### Níveis de Logging

- **TRACE** - Detalhes muito finos
- **DEBUG** - Informações de depuração
- **INFO** - Informações gerais
- **WARN** - Avisos
- **ERROR** - Erros
- **FATAL** - Erros fatais

## Uso no Código

### Importar Logger

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```

### Criar Logger

```java
private static final Logger logger = LogManager.getLogger(SuaClasse.class);
```

### Usar Logger

```java
logger.debug("Mensagem de debug: {}", valor);
logger.info("Mensagem informativa");
logger.warn("Aviso: {}", mensagem);
logger.error("Erro ocorreu", exception);
```

### Logging Estruturado

Para logging estruturado, use parâmetros:

```java
logger.info("Usuário {} realizou ação {} no recurso {}", 
    usuario, acao, recurso);
```

### Logging de Auditoria

Para logs de auditoria, use o logger "audit":

```java
Logger auditLogger = LogManager.getLogger("audit");
auditLogger.info("Usuário {} acessou recurso {}", usuario, recurso);
```

### Logging de Erro

Para logs de erro, use o logger "error":

```java
Logger errorLogger = LogManager.getLogger("error");
errorLogger.error("Erro ao processar pedido: {}", pedidoId, exception);
```

## Arquivos de Log

### Localização

```
logs/
├── hermes-comercial.log              # Logs gerais
├── hermes-comercial-error.log        # Apenas erros
├── hermes-comercial-audit.log        # Logs de auditoria
├── hermes-comercial-2024-05-16-1.log.gz  # Logs rotacionados
└── ...
```

### Rotação

- **Time-based:** Rotação diária
- **Size-based:** Rotação quando arquivo atinge 10MB
- **Max files:** Mantém até 30 arquivos

## Configuração por Ambiente

### Desenvolvimento

```xml
<Root level="debug">
    <AppenderRef ref="Console"/>
    <AppenderRef ref="RollingFile"/>
</Root>
```

### Produção

```xml
<Root level="info">
    <AppenderRef ref="RollingFile"/>
</Root>
```

### Testes

```xml
<Root level="warn">
    <AppenderRef ref="Console"/>
</Root>
```

## Troubleshooting

### Logs não aparecem

1. Verifique se o arquivo de configuração está correto
2. Verifique o nível de logging
3. Verifique se o diretório `logs/` existe

### Logs muito grandes

1. Aumente o tamanho de rotação
2. Reduza o número de arquivos mantidos
3. Ajuste o nível de logging para menos verbose

### Performance

1. Use logging assíncrono em produção
2. Evite logging em loops apertados
3. Use parâmetros em vez de concatenação

## Melhores Práticas

1. **Use níveis apropriados** - DEBUG para dev, INFO para produção
2. **Evite logging em loops** - Pode degradar performance
3. **Use parâmetros** - `logger.info("Valor: {}", valor)` em vez de `logger.info("Valor: " + valor)`
4. **Logue exceções com stack trace** - `logger.error("Erro", exception)`
5. **Use logging estruturado** - Facilita análise de logs
6. **Não logue dados sensíveis** - Senhas, tokens, etc.

## Recursos

- [Log4j 2 Documentation](https://logging.apache.org/log4j/2.x/)
- [Log4j 2 Configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html)
- [Log4j 2 Appenders](https://logging.apache.org/log4j/2.x/manual/appenders.html)
