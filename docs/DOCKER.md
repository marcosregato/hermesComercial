# Docker - Guia de Containerização

## Visão Geral

O sistema Hermes Comercial foi containerizado com Docker para facilitar deploy, consistência de ambientes e isolamento de dependências.

## Estrutura

```
.
├── Dockerfile              # Definição da imagem Docker
├── docker-compose.yml      # Orquestração de containers
├── .dockerignore          # Arquivos ignorados no build
└── docs/DOCKER.md         # Este documento
```

## Pré-requisitos

- Docker 20.10+
- Docker Compose 2.0+
- Maven 3.8+

## Build da Imagem

### Build Local

```bash
# Build do JAR com Maven
mvn clean package -DskipTests

# Build da imagem Docker
docker build -t hermes-comercial:3.2.0 .
```

### Build com Docker Compose

```bash
# Build e start em um comando
docker-compose up --build
```

## Executar a Aplicação

### Com Docker Compose (Recomendado)

```bash
# Iniciar containers
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar containers
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### Com Docker Run

```bash
docker run -d \
  --name hermes-pdv \
  -p 8080:8080 \
  -v hermes-data:/app/data \
  -e JAVA_OPTS="-Xmx1024m -Xms512m" \
  hermes-comercial:3.2.0
```

## Volumes

### Banco de Dados

O banco de dados SQLite é persistido no volume `hermes-data`:

```bash
# Ver volume
docker volume inspect hermes-data

# Backup do volume
docker run --rm -v hermes-data:/data -v $(pwd):/backup alpine tar czf /backup/hermes-backup.tar.gz /data

# Restore do volume
docker run --rm -v hermes-data:/data -v $(pwd):/backup alpine tar xzf /backup/hermes-backup.tar.gz -C /
```

### Logs

Logs são persistidos no volume `hermes-logs`:

```bash
# Ver logs do container
docker logs hermes-pdv

# Logs em tempo real
docker logs -f hermes-pdv
```

## Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| JAVA_OPTS | Opções da JVM | -Xmx1024m -Xms512m |
| SPRING_PROFILES_ACTIVE | Profile Spring | production |
| DISPLAY | Display X11 (para GUI) | :0 |

## Health Check

O container possui health check automático:

```bash
# Ver status do health check
docker inspect --format='{{.State.Health.Status}}' hermes-pdv

# Ver health check detalhado
docker inspect hermes-pdv --format='{{json .State.Health}}' | jq
```

## Troubleshooting

### Container não inicia

```bash
# Ver logs
docker logs hermes-pdv

# Entrar no container
docker exec -it hermes-pdv sh

# Ver processos
docker top hermes-pdv
```

### Problemas de memória

Ajuste a variável `JAVA_OPTS`:

```bash
# docker-compose.yml
environment:
  - JAVA_OPTS=-Xmx2048m -Xms1024m
```

### Problemas de permissão

```bash
# Ajustar permissões do volume
docker-compose exec hermes-pdv chown -R appuser:appuser /app/data
```

## Deploy em Produção

### Docker Registry

```bash
# Tag para registry
docker tag hermes-comercial:3.2.0 registry.example.com/hermes-comercial:3.2.0

# Push para registry
docker push registry.example.com/hermes-comercial:3.2.0
```

### Kubernetes

Ver documento de infraestrutura para configuração Kubernetes.

## Performance

### Otimizações Aplicadas

1. **Multi-stage Build** (futuro): Reduz tamanho da imagem
2. **Alpine Linux**: Imagem base pequena
3. **JVM Tuning**: Heap size configurado
4. **Health Check**: Monitoramento automático

### Melhorias Futuras

- [ ] Multi-stage build para reduzir tamanho
- [ ] GraalVM native image
- [ ] JMX para monitoramento
- [ ] Profiling integrado

## Segurança

### Boas Práticas

1. **Imagem base oficial**: eclipse-temurin
2. **Usuário não-root** (futuro): Rodar como usuário não-root
3. **Scan de vulnerabilidades**: Integrar no CI/CD
4. **Secrets**: Usar Docker Secrets para dados sensíveis

### Scan de Vulnerabilidades

```bash
# Trivy scan
trivy image hermes-comercial:3.2.0

# Docker Scout
docker scout quickview hermes-comercial:3.2.0
```

## CI/CD Integration

O Docker está integrado no pipeline CI/CD:

```yaml
# .github/workflows/ci-cd.yml
- name: Build Docker image
  run: docker build -t hermes-comercial:${{ github.sha }} .

- name: Push to Registry
  run: docker push registry.example.com/hermes-comercial:${{ github.sha }}
```

## Referências

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Best Practices for Dockerfiles](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
