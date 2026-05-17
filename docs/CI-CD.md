# CI/CD Pipeline

## Visão Geral

Este projeto utiliza GitHub Actions para automação de CI/CD (Continuous Integration/Continuous Deployment), garantindo qualidade de código e automação de testes.

## Workflow do GitHub Actions

O pipeline CI/CD é executado automaticamente nos seguintes eventos:
- **Push** para branches `main` e `develop`
- **Pull Requests** para branches `main` e `develop`
- **Manual** via `workflow_dispatch`

## Jobs do Pipeline

### 1. Build and Test

Executa o build completo e suite de testes do projeto.

**Passos:**
- Checkout do código
- Configuração do JDK 21
- Cache de dependências Maven
- Compilação do projeto
- Execução de testes com JUnit 5
- Geração de relatório de cobertura com JaCoCo
- Upload de artefatos (resultados de testes, relatórios de cobertura)
- Build do JAR
- Upload do JAR como artefato

**Cobertura de Testes:**
- Cobertura mínima geral: 40%
- Cobertura mínima em arquivos modificados: 60%
- Relatório gerado automaticamente em PRs

### 2. Code Quality

Executa análise estática de código para garantir qualidade.

**Passos:**
- Checkout do código
- Configuração do JDK 21
- Execução do Checkstyle (Google Java Style Guide)
- Execução do SpotBugs (detecção de bugs)
- Upload de relatórios de qualidade

**Ferramentas:**
- **Checkstyle**: Análise de estilo de código
- **SpotBugs**: Detecção de bugs comuns

### 3. Security Scan

Executa varredura de segurança no código.

**Passos:**
- Checkout do código
- Execução do Trivy (scanner de vulnerabilidades)
- Upload de resultados para GitHub Security

### 4. Deploy

Deploy automático para staging (apenas em branch main).

**Passos:**
- Download do JAR artefato
- Deploy para ambiente de staging

**Condição:**
- Apenas em push para branch `main`
- Após sucesso dos jobs anteriores

## Plugins Maven Configurados

### JaCoCo (Cobertura de Testes)

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
```

**Comandos:**
- `mvn jacoco:prepare-agent` - Prepara o agente de cobertura
- `mvn jacoco:report` - Gera relatório de cobertura

**Exclusões:**
- Classes de modelo (`**/model/**`)
- DTOs (`**/dto/**`)
- Classes de configuração (`**/config/**`)

### Checkstyle (Análise de Estilo)

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.1</version>
</plugin>
```

**Configuração:**
- Google Java Style Guide
- Não falha o build em caso de violações
- Relatório em console e XML

**Comando:**
- `mvn checkstyle:check` - Executa análise de estilo

### SpotBugs (Detecção de Bugs)

```xml
<plugin>
    <groupId>com.github.spotbugs</groupId>
    <artifactId>spotbugs-maven-plugin</artifactId>
    <version>4.8.3</version>
</plugin>
```

**Configuração:**
- Esforço máximo (`Max`)
- Threshold baixo (`Low`)
- Exclusões definidas em `spotbugs-exclude.xml`

**Comando:**
- `mvn spotbugs:check` - Executa detecção de bugs

## Execução Local

Para executar os testes e gerar relatórios localmente:

```bash
# Executar testes
mvn test

# Gerar relatório de cobertura
mvn jacoco:report

# Executar análise de estilo
mvn checkstyle:check

# Executar detecção de bugs
mvn spotbugs:check

# Executar todos os checks
mvn clean verify
```

## Relatórios

### Cobertura de Testes

Os relatórios de cobertura são gerados em:
- `target/site/jacoco/index.html` - Relatório HTML
- `target/site/jacoco/jacoco.xml` - Relatório XML (para CI/CD)

### Qualidade de Código

Os relatórios de qualidade são gerados em:
- `target/checkstyle-result.xml` - Relatório Checkstyle
- `target/spotbugs/*.xml` - Relatórios SpotBugs

### Resultados de Testes

Os resultados dos testes são salvos em:
- `target/surefire-reports/*.xml` - Resultados JUnit

## Configuração de Thresholds

### Cobertura de Testes

- **Geral**: 40% mínimo
- **Arquivos Modificados**: 60% mínimo
- **Classes Críticas**: 80% recomendado

### Qualidade de Código

- **Checkstyle**: Apenas warnings, não falha build
- **SpotBugs**: Apenas warnings, não falha build

## Branches

### Main
- Deploy automático para staging
- Todos os checks obrigatórios
- Merge only após aprovação

### Develop
- Execução completa do pipeline
- Sem deploy automático
- Branch de desenvolvimento

### Feature Branches
- Execução via Pull Request
- Feedback automático de qualidade
- Merge após aprovação

## Troubleshooting

### Falha nos Testes

1. Verifique os logs no GitHub Actions
2. Execute os testes localmente: `mvn test`
3. Verifique se há dependências faltando

### Cobertura Insuficiente

1. Execute `mvn jacoco:report` localmente
2. Abra `target/site/jacoco/index.html`
3. Identifique classes sem cobertura
4. Adicione testes para as classes identificadas

### Violações de Estilo

1. Execute `mvn checkstyle:check` localmente
2. Corrija as violações indicadas
3. Siga o Google Java Style Guide

### Bugs Detectados pelo SpotBugs

1. Execute `mvn spotbugs:check` localmente
2. Revise os bugs reportados
3. Corrija ou justifique a exclusão em `spotbugs-exclude.xml`

## Melhorias Futuras

- [ ] Adicionar integração com SonarQube
- [ ] Configurar deploy automático para produção
- [ ] Adicionar testes de integração com banco de dados
- [ ] Configurar notificações de Slack/Discord
- [ ] Adicionar performance tests
- [ ] Configurar rollback automático em caso de falha

## Recursos

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Checkstyle Documentation](https://checkstyle.sourceforge.io/)
- [SpotBugs Documentation](https://spotbugs.github.io/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
