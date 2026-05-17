# Arquitetura e Padrões - Hermes Comercial

## Visão Geral

O Hermes Comercial segue uma arquitetura em camadas com separação clara de responsabilidades, organizada em módulos funcionais (PDV, ERP) e componentes compartilhados.

## Estrutura de Pacotes

```
com.br.hermescomercial/
├── MainApplication.java              # Ponto de entrada da aplicação
├── config/                           # Configurações do sistema
├── model/                            # Modelos de domínio compartilhados
├── repository/                       # Camada de acesso a dados (Padrão Repository)
│   ├── interfaces/                   # Interfaces Repository
│   └── impl/                        # Implementações Repository (ClienteRepositoryImpl, etc)
├── service/                          # Camada de lógica de negócio
├── controller/                       # Camada de apresentação (Controllers)
├── dao/                              # DAOs legados (em migração para Repository)
├── dto/                              # Data Transfer Objects
├── exception/                        # Exceções personalizadas
├── validation/                       # Validadores de domínio
├── util/                             # Utilitários gerais
├── cache/                            # Gerenciamento de cache
├── event/                            # Sistema de eventos
├── injection/                        # Injeção de dependências (DependencyContainer)
├── command/                          # Padrão Command (CommandSystem)
├── async/                            # Processamento assíncrono
├── pattern/                          # Padrões de projeto implementados
├── factory/                          # Factories para criação de objetos
├── mapper/                           # Mapeamento entre objetos
├── logging/                          # Configuração de logging
├── pdv/                              # Módulo PDV
│   ├── controller/                  # Controllers específicos do PDV
│   ├── service/                     # Serviços específicos do PDV
│   ├── model/                       # Modelos específicos do PDV
│   ├── dao/                         # DAOs específicos do PDV (legado)
│   └── base/                        # Classes base do PDV
├── erp/                             # Módulo ERP
│   ├── controller/                  # Controllers específicos do ERP
│   ├── service/                     # Serviços específicos do ERP
│   └── model/                       # Modelos específicos do ERP
└── shared/                          # Componentes compartilhados
    ├── api/                         # APIs e interfaces compartilhadas
    ├── dao/                         # DAOs compartilhados (legado)
    ├── model/                       # Modelos compartilhados
    ├── service/                     # Serviços compartilhados
    ├── util/                        # Utilitários compartilhados
    └── validator/                   # Validadores compartilhados
```

## Padrões de Arquitetura

### 1. Camada de Apresentação (Controller)

**Responsabilidade:** Gerenciar a interface do usuário e coordenar interações.

**Convenções:**
- Nome: `{Módulo}{Funcionalidade}Controller`
- Exemplos: `PDVPrincipalController`, `ERPProdutoController`
- Localização: `controller/` ou `{modulo}/controller/`
- Deve depender apenas de Services e DTOs
- Não deve conter lógica de negócio

**Exemplo:**
```java
public class ProdutoController {
    private final ProdutoService produtoService;
    
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }
    
    public void salvarProduto(ProdutoDTO dto) {
        produtoService.salvar(dto);
    }
}
```

### 2. Camada de Serviço (Service)

**Responsabilidade:** Conter regras de negócio e coordenar transações.

**Convenções:**
- Nome: `{Entidade}Service`
- Exemplos: `ProdutoService`, `ClienteService`, `VendaService`
- Localização: `service/` ou `{modulo}/service/`
- Deve depender de Repositories e outros Services
- Deve usar DTOs para entrada/saída quando apropriado

**Exemplo:**
```java
public class ProdutoService {
    private static final Logger logger = LogManager.getLogger(ProdutoService.class);
    private final ProdutoRepository produtoRepository;
    private final CacheManager cacheManager;
    
    public ProdutoService(ProdutoRepository produtoRepository, CacheManager cacheManager) {
        this.produtoRepository = produtoRepository;
        this.cacheManager = cacheManager;
    }
    
    public boolean salvar(Produto produto) {
        try {
            logger.debug("Salvando produto: {}", produto.getNome());
            produtoRepository.save(produto);
            cacheManager.evict("produtos");
            return true;
        } catch (Exception e) {
            logger.error("Erro ao salvar produto", e);
            throw new BusinessException("Erro ao salvar produto", e);
        }
    }
}
```

### 3. Camada de Acesso a Dados (Repository)

**Responsabilidade:** Abstrair acesso a dados e fornecer interface orientada a domínio.

**Convenções:**
- Nome: `{Entidade}Repository` (interface) e `{Entidade}RepositoryImpl` (implementação)
- Exemplos: `ProdutoRepository`, `ClienteRepository`
- Localização: `repository/` para interfaces, `repository/impl/` para implementações
- Deve usar padrão Repository (não DAO)
- DAOs legados em `dao/` estão em migração para Repository

**Exemplo:**
```java
public interface ProdutoRepository {
    boolean salvar(Produto produto);
    boolean atualizar(Produto produto);
    boolean remover(Long id);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> listar();
    Produto buscarPorCodigoBarras(String codigoBarras);
}

public class ProdutoRepositoryImpl implements ProdutoRepository {
    private static final Logger logger = LogManager.getLogger(ProdutoRepositoryImpl.class);
    private final ProdutoDao produtoDao;
    
    @Override
    public boolean salvar(Produto produto) {
        try {
            return produtoDao.salvar(produto);
        } catch (Exception e) {
            logger.error("Erro ao salvar produto", e);
            throw new RuntimeException("Erro ao salvar produto", e);
        }
    }
}
```

### 4. Camada de Modelo (Model)

**Responsabilidade:** Representar entidades do domínio.

**Convenções:**
- Nome: `{Entidade}` (singular)
- Exemplos: `Produto`, `Cliente`, `Venda`
- Localização: `model/` ou `{modulo}/model/`
- Deve ser POJOs simples
- Validações via `@Valid` ou validadores separados

**Exemplo:**
```java
public class Produto {
    private Long id;
    private String codigo;
    private String nome;
    private BigDecimal preco;
    private Integer estoque;
    
    // Getters e Setters
    // equals() e hashCode()
    // toString()
}
```

### 5. Camada DTO (Data Transfer Object)

**Responsabilidade:** Transferir dados entre camadas, especialmente para APIs.

**Convenções:**
- Nome: `{Entidade}DTO`
- Exemplos: `ProdutoDTO`, `ClienteDTO`
- Localização: `dto/`
- Deve ser imutável quando possível
- Validações via Bean Validation

**Exemplo:**
```java
public class ProdutoDTO {
    private Long id;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;
    
    // Getters e Setters
    // Construtor
}
```

## Padrões de Nomenclatura

### Classes

- **Controllers:** `{Módulo}{Funcionalidade}Controller`
- **Services:** `{Entidade}Service`
- **Repositories:** `{Entidade}Repository` (interface), `{Entidade}RepositoryImpl` (implementação)
- **Models:** `{Entidade}` (singular)
- **DTOs:** `{Entidade}DTO`
- **Exceptions:** `{Erro}Exception`
- **Validators:** `{Entidade}Validator`
- **Factories:** `{Entidade}Factory`

### Métodos

- **CRUD:** `save()`, `findById()`, `findAll()`, `delete()`, `update()`
- **Busca:** `findBy{Atributo}()`, `searchBy{Critério}()`
- **Ações:** `salvar()`, `atualizar()`, `remover()`, `buscar()`
- **Boolean:** `isValid()`, `hasPermission()`, `canAccess()`

### Variáveis

- **CamelCase** para variáveis locais e parâmetros
- **UPPER_SNAKE_CASE** para constantes
- Nomes descritivos e significativos
- Evitar abreviações não padrão

### Pacotes

- **lowercase** para nomes de pacotes
- Separar por domínio funcional, não por tipo
- Evitar profundidade > 4 níveis

## Padrões de Projeto Utilizados

### 1. Dependency Injection

**Propósito:** Inversão de controle e desacoplamento.

**Implementação:** `DependencyContainer` com suporte a SINGLETON e TRANSIENT.

**Características:**
- Singleton: Uma instância por container (padrão para repositories)
- Transient: Nova instância a cada requisição
- Registro via `registerSingleton()` e `registerTransient()`
- Resolução via `get()` e `getOptional()`
- Exceções personalizadas: `DependencyNotFoundException`, `DependencyCreationException`

**Exemplo:**
```java
// Registro
DependencyContainer container = DependencyContainer.getInstance();
container.registerSingleton(ProdutoRepository.class, ProdutoRepositoryImpl.class);

// Resolução
ProdutoRepository repo = container.get(ProdutoRepository.class);

// Injeção via construtor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }
    
    // Construtor padrão usando container
    public ProdutoService() {
        this(DependencyContainer.getInstance().get(ProdutoRepository.class));
    }
}
```

### 2. Repository Pattern

**Propósito:** Abstrair acesso a dados com interface orientada a domínio.

**Implementação:** Interfaces Repository com implementações separadas.

### 3. Service Layer Pattern

**Propósito:** Separar lógica de negócio da apresentação e acesso a dados.

**Implementação:** Services coordenam Repositories e aplicam regras de negócio.

### 4. Factory Pattern

**Propósito:** Centralizar criação de objetos complexos.

**Implementação:** `ControllerFactory`, `DatabaseFactory`.

### 5. Observer Pattern

**Propósito:** Comunicação entre componentes sem acoplamento direto.

**Implementação:** `EventSystem` para publicar/subscrever eventos.

### 6. Singleton Pattern

**Propósito:** Garantir única instância de classes gerenciadoras.

**Implementação:** Services singletons via DependencyContainer.

**Classes Singleton:**
- `DependencyContainer`
- `CommandSystem`
- `EventSystem`
- Repositories (registrados como singleton)

### 7. Command Pattern

**Propósito:** Encapsular ações como objetos, permitindo undo/redo.

**Implementação:** `CommandSystem` com suporte a execução, desfazer e refazer.

**Características:**
- Registro de comandos via `registerCommand()`
- Execução via `executeCommand()`
- Undo/redo via `undo()` e `redo()`
- Auditoria de comandos executados

**Exemplo:**
```java
CommandSystem cmdSystem = CommandSystem.getInstance();
cmdSystem.registerCommand("salvarProduto", params -> new SalvarProdutoCommand());
cmdSystem.executeCommand("salvarProduto", params);
cmdSystem.undo();
```

## Padrões de Logging

**Framework:** Log4j 2

**Convenções:**
- Declarar logger como: `private static final Logger logger = LogManager.getLogger(Classe.class);`
- Usar níveis apropriados:
  - `ERROR`: Erros que impedem funcionamento
  - `WARN`: Situações anormais mas recuperáveis
  - `INFO`: Eventos significativos do sistema
  - `DEBUG`: Informações detalhadas para debug
- Usar parâmetros para performance: `logger.info("Mensagem {}", parametro)`
- Incluir exceção em erros: `logger.error("Erro", exception)`

**Exemplo:**
```java
public class ProdutoService {
    private static final Logger logger = LogManager.getLogger(ProdutoService.class);
    
    public void salvar(Produto produto) {
        logger.debug("Iniciando salvamento do produto: {}", produto.getNome());
        try {
            // Lógica
            logger.info("Produto salvo com sucesso: {}", produto.getId());
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: {}", produto.getNome(), e);
            throw new BusinessException("Erro ao salvar produto", e);
        }
    }
}
```

## Padrões de Tratamento de Exceções

**Hierarquia:**
- `BusinessException`: Erros de regras de negócio (com códigos de erro e contexto)
- `ValidationException`: Erros de validação de dados
- `DataAccessException`: Erros de acesso a dados (com códigos de erro e contexto)
- `SystemException`: Erros do sistema (com códigos de erro e contexto)

**Convenções:**
- Nunca usar `System.err` - sempre usar `logger.error()`
- Capturar exceções específicas, não `Exception` genérico
- Logar exceções antes de relançar
- Fornecer mensagens descritivas e acionáveis
- Usar códigos de erro padronizados (BIZ_*, DATA_*, SYS_*)
- Adicionar contexto quando relevante

**Exemplo:**
```java
public class ProdutoService {
    private static final Logger logger = LogManager.getLogger(ProdutoService.class);
    
    public void salvar(Produto produto) {
        try {
            validarProduto(produto);
            produtoRepository.salvar(produto);
        } catch (ValidationException e) {
            logger.warn("Validação falhou: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            logger.error("Erro ao persistir produto", e);
            throw new BusinessException("Erro ao salvar produto", e);
        }
    }
}

// Usando exceções com contexto
throw BusinessException.entityNotFound("Produto", id)
    .addContext("operation", "buscar")
    .addContext("userId", currentUser.getId());
```

## Padrões de Validação

**Locais:**
- Validações simples: Anotações no DTO/Model
- Validações complexas: Validators separados
- Validações de negócio: Na camada Service

**Exemplo:**
```java
public class ProdutoDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter 3-100 caracteres")
    private String nome;
    
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;
    
    @Min(value = 0, message = "Estoque não pode ser negativo")
    private Integer estoque;
}
```

## Padrões de Cache

**Framework:** Caffeine

**Convenções:**
- Cache apenas para dados frequentemente acessados e raramente modificados
- Usar chaves descritivas: `"produtos.lista"`, `"cliente.{id}"`
- Invalidar cache após modificações
- Configurar TTL apropriado

**Exemplo:**
```java
public class ProdutoService {
    private final CacheManager cacheManager;
    
    public List<Produto> listar() {
        String cacheKey = "produtos.lista";
        List<Produto> produtos = cacheManager.get(cacheKey);
        
        if (produtos == null) {
            produtos = produtoRepository.findAll();
            cacheManager.put(cacheKey, produtos);
        }
        
        return produtos;
    }
    
    public void salvar(Produto produto) {
        produtoRepository.save(produto);
        cacheManager.evict("produtos.lista");
    }
}
```

## Diretrizes de Refatoração

### DAO vs Repository

**Padrão Atual:** Migrar de DAO para Repository Pattern

**Estado Atual:**
- DAOs legados em `dao/` ainda em uso
- Novos Repositories em `repository/` com implementações em `repository/impl/`
- `DependencyContainer` registra implementações Repository

**Ação:**
- Manter DAOs existentes por enquanto (legado)
- Novos componentes devem usar Repository
- Gradualmente migrar DAOs para Repository
- Implementações Repository envolvem DAOs existentes

**Justificativa:**
- Repository fornece interface mais orientada a domínio
- Facilita testes e mock
- Melhor separação de responsabilidades
- Suporta injeção de dependências

### Services Duplicados

**Padrão Atual:** Eliminar duplicação de services

**Ação:**
- Consolidar services similares em `service/`
- Manter apenas services específicos em módulos (pdv/, erp/)
- Usar herança ou composição para compartilhar lógica

### Controllers

**Padrão Atual:** Organizar controllers por módulo funcional

**Ação:**
- Controllers do PDV em `pdv/controller/`
- Controllers do ERP em `erp/controller/`
- Controllers compartilhados em `controller/`

### Logging

**Padrão Atual:** Padronizar logging com Log4j 2

**Ação:**
- Substituir `System.err` por `logger.error()`
- Usar logger apropriado em todas as classes
- Seguir níveis de logging (ERROR, WARN, INFO, DEBUG)
- Usar parâmetros para performance

### Tratamento de Exceções

**Padrão Atual:** Usar exceções personalizadas com contexto

**Ação:**
- Usar `BusinessException`, `ValidationException`, `DataAccessException`, `SystemException`
- Adicionar códigos de erro padronizados
- Adicionar contexto quando relevante
- Logar exceções antes de relançar

## Checklist de Qualidade

### Novos Componentes

- [ ] Segue convenções de nomenclatura
- [ ] Usa injeção de dependências
- [ ] Implementa logging adequado
- [ ] Trata exceções corretamente
- [ ] Usa DTOs para entrada/saída
- [ ] Possui validação
- [ ] Está no pacote correto
- [ ] Documenta propósito e uso

### Código Legado

- [ ] Identificado para refatoração
- [ ] Possui testes de regressão
- [ ] Documentado com TODO/FIXME se necessário
- [ ] Priorizado em backlog

## Ferramentas e Frameworks

- **Logging:** Log4j 2.20.0
- **Cache:** Caffeine 3.1.8
- **Injeção de Dependências:** DependencyContainer (próprio, com SINGLETON/TRANSIENT)
- **Validação:** Bean Validation + Validators customizados
- **Eventos:** EventSystem (próprio)
- **Command Pattern:** CommandSystem (próprio, com undo/redo)
- **Database:** Hibernate 6.4.1 + HikariCP 5.0.1
- **Migrations:** Flyway 9.22.3
- **Testes:** JUnit 5.10.0 + Mockito 5.8.0
- **CI/CD:** GitHub Actions com JaCoCo, Checkstyle, SpotBugs

## Documentação Adicional

- **CI/CD:** `docs/CI-CD.md` - Pipeline de integração contínua e deploy
- **Testes:** Suite de testes em `src/test/java/`
- **Exemplos:** Consulte testes unitários para exemplos de uso dos padrões

## Referências

- Clean Architecture - Robert C. Martin
- Patterns of Enterprise Application Architecture - Martin Fowler
- Effective Java - Joshua Bloch
