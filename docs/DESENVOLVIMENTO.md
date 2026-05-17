# Guia de Desenvolvimento - Hermes Comercial

## Visão Geral

Este guia fornece instruções e melhores práticas para desenvolvedores trabalhando no projeto Hermes Comercial PDV.

## Pré-requisitos

- JDK 21 ou superior
- Maven 3.8+
- Git
- IDE recomendada: IntelliJ IDEA ou Eclipse

## Configuração do Ambiente

### 1. Clonar o Repositório

```bash
git clone https://github.com/seu-usuario/hermesComercial.git
cd hermesComercial
```

### 2. Configurar Maven

```bash
mvn clean install
```

### 3. Importar no IDE

**IntelliJ IDEA:**
- File → Open → Selecionar o diretório do projeto
- Aguardar indexação do Maven

**Eclipse:**
- File → Import → Maven → Existing Maven Projects
- Selecionar o diretório do projeto

## Estrutura do Projeto

```
hermesComercial/
├── src/
│   ├── main/
│   │   ├── java/com/br/hermescomercial/
│   │   │   ├── controller/      # Controllers Swing
│   │   │   ├── dao/             # Data Access Objects
│   │   │   ├── model/           # Modelos de dados
│   │   │   ├── repository/      # Repositories
│   │   │   ├── service/         # Lógica de negócio
│   │   │   ├── util/            # Utilitários
│   │   │   ├── validation/      # Validação
│   │   │   └── exception/       # Exceções personalizadas
│   │   └── resources/
│   │       ├── db/migration/    # Migrations Flyway
│   │       └── log4j2.xml       # Configuração de logging
│   └── test/
│       └── java/com/br/hermescomercial/
│           └── ...              # Testes
├── docs/                        # Documentação
├── .github/workflows/           # CI/CD
└── pom.xml                      # Configuração Maven
```

## Padrões de Arquitetura

### 1. Repository Pattern

Use Repositories para abstrair acesso a dados:

```java
public interface ProdutoRepository {
    Produto salvar(Produto produto);
    Optional<Produto> buscarPorId(Long id);
    List<Produto> buscarTodos();
    void excluir(Long id);
}
```

### 2. Service Layer

Lógica de negócio deve estar em Services:

```java
@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    
    public Produto salvar(Produto produto) {
        Validator.validarProduto(produto);
        return repository.salvar(produto);
    }
}
```

### 3. Dependency Injection

Use DependencyContainer para injeção de dependências:

```java
DependencyContainer container = DependencyContainer.getInstance();
ProdutoService service = container.resolve(ProdutoService.class);
```

### 4. Validation

Use Validator para validação de entidades:

```java
Validator.validarProduto(produto);
Validator.validarCliente(cliente);
```

### 5. Exception Handling

Use exceções personalizadas com contexto:

```java
throw new BusinessException(
    ErrorCode.PRODUTO_NAO_ENCONTRADO,
    "Produto não encontrado",
    Map.of("produtoId", produtoId)
);
```

## Padrões de Código

### Nomenclatura

- **Classes:** PascalCase (ex: `ProdutoService`)
- **Métodos:** camelCase (ex: `buscarPorId`)
- **Constantes:** UPPER_SNAKE_CASE (ex: `MAX_CACHE_SIZE`)
- **Variáveis:** camelCase (ex: `produtoId`)

### Comentários

Use JavaDoc para classes e métodos públicos:

```java
/**
 * Serviço para gerenciamento de produtos
 * @author Hermes Comercial
 * @version 3.2.0
 */
public class ProdutoService {
    /**
     * Salva um produto no sistema
     * @param produto Produto a ser salvo
     * @return Produto salvo
     * @throws ValidationException Se o produto for inválido
     */
    public Produto salvar(Produto produto) {
        // ...
    }
}
```

### Logging

Use Log4j 2 para logging estruturado:

```java
private static final Logger logger = LogManager.getLogger(ProdutoService.class);

logger.debug("Buscando produto com ID: {}", produtoId);
logger.info("Produto salvo com sucesso: {}", produto.getNome());
logger.error("Erro ao salvar produto", exception);
```

## Testes

### Escrever Testes

Use JUnit 5 e Mockito:

```java
@Test
@DisplayName("Deve salvar produto válido")
void testSalvarProdutoValido() {
    Produto produto = criarProdutoValido();
    
    assertDoesNotThrow(() -> service.salvar(produto));
}
```

### Executar Testes

```bash
# Todos os testes
mvn test

# Teste específico
mvn test -Dtest=ProdutoServiceTest

# Com cobertura
mvn test jacoco:report
```

## Build e Deploy

### Build Local

```bash
# Limpar e compilar
mvn clean compile

# Empacotar
mvn package

# Pular testes
mvn package -DskipTests
```

### Criar Instalador

```bash
# Linux/macOS
./build-installer.sh

# Windows
build-installer.bat
```

### Docker

```bash
# Build imagem
docker build -t hermes-comercial:latest .

# Executar container
docker-compose up -d
```

## Code Quality

### Checkstyle

```bash
mvn checkstyle:check
```

### SpotBugs

```bash
mvn spotbugs:check
```

### SonarQube

```bash
mvn sonar:sonar
```

## Boas Práticas

### 1. Validação de Entrada

Sempre valide entrada de dados:

```java
String nome = InputSanitizer.sanitizeName(input);
Validator.validarStringNaoVazia(nome, "Nome");
```

### 2. Tratamento de Exceções

Capture exceções específicas:

```java
try {
    service.salvar(produto);
} catch (ValidationException e) {
    logger.error("Erro de validação: {}", e.getMessage());
    // Tratar erro de validação
} catch (DataAccessException e) {
    logger.error("Erro de acesso a dados", e);
    // Tratar erro de banco de dados
}
```

### 3. Performance

Use cache para dados frequentemente acessados:

```java
CacheService cache = CacheService.getInstance();
Produto produto = cache.get("produto:" + id, () -> repository.buscarPorId(id));
```

### 4. Segurança

Sempre sanitize entrada de usuário:

```java
String input = InputSanitizer.sanitize(userInput);
```

## Troubleshooting

### Erro de Compilação

```bash
mvn clean install -U
```

### Erro de Testes

```bash
mvn clean test -DskipTests=false
```

### Problemas de Dependência

```bash
mvn dependency:tree
mvn dependency:purge-local-repository
```

## Recursos

- [Documentação de Arquitetura](ARQUITETURA.md)
- [CI/CD](CI-CD.md)
- [Docker](DOCKER.md)
- [jpackage](JPACKAGE.md)
- [Recomendações de Engenharia](RECOMENDACOES_ENGENHARIA.md)

## Suporte

Para dúvidas ou problemas, contate a equipe de desenvolvimento ou abra uma issue no repositório.
