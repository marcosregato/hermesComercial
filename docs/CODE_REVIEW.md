# Guidelines de Code Review - Hermes Comercial

## Visão Geral

Este documento define as diretrizes para code review no projeto Hermes Comercial PDV.

## Princípios Fundamentais

1. **Respeito e Colaboração:** Reviews devem ser construtivos e respeitosos
2. **Foco na Qualidade:** Priorizar qualidade do código sobre velocidade
3. **Aprendizado Contínuo:** Reviews são oportunidades de aprendizado
4. **Consistência:** Manter consistência com o código existente

## Checklist de Code Review

### Funcionalidade

- [ ] O código implementa a funcionalidade requerida?
- [ ] Edge cases são tratados adequadamente?
- [ ] Validação de entrada está presente?
- [ ] Tratamento de erros é apropriado?

### Qualidade de Código

- [ ] Código é legível e fácil de entender?
- [ ] Nomes de variáveis/métodos são descritivos?
- [ ] Complexidade ciclomática é aceitável?
- [ ] Duplicação de código foi evitada?
- [ ] Comentários são necessários e úteis?

### Arquitetura e Design

- [ ] Segue os padrões de arquitetura do projeto?
- [ ] Separação de responsabilidades é mantida?
- [ ] Acoplamento é baixo?
- [ ] Coesão é alta?

### Performance

- [ ] Algoritmos são eficientes?
- [ ] Consultas de banco de dados são otimizadas?
- [ ] Cache é usado onde apropriado?
- [ ] Não há vazamentos de memória?

### Segurança

- [ ] Entrada de usuário é validada e sanitizada?
- [ ] SQL injection é prevenido?
- [ ] XSS é prevenido?
- [ ] Dados sensíveis são protegidos?

### Testes

- [ ] Testes unitários foram adicionados/atualizados?
- [ ] Testes cobrem os casos principais?
- [ ] Testes são independentes e reproduzíveis?
- [ ] Mocks são usados apropriadamente?

### Documentação

- [ ] JavaDoc está presente para classes/métodos públicos?
- [ ] Comentários complexos são explicados?
- [ ] README foi atualizado se necessário?

## Processo de Code Review

### 1. Preparação

**Autor:**
- Revise seu próprio código antes de submeter
- Certifique-se que o build passa
- Execute testes localmente
- Adicione descrição clara do PR

**Revisor:**
- Entenda o contexto da mudança
- Leia a descrição do PR
- Revise os arquivos modificados

### 2. Execução do Review

**Passo 1: Visão Geral**
- Entender o propósito da mudança
- Verificar se a abordagem é adequada
- Identificar questões de arquitetura

**Passo 2: Detalhe**
- Revisar cada arquivo modificado
- Verificar qualidade do código
- Identificar bugs potenciais

**Passo 3: Testes**
- Verificar se testes foram adicionados
- Executar testes se possível
- Verificar cobertura de testes

### 3. Feedback

**Boas Práticas:**
- Seja específico e construtivo
- Explique o "porquê" dos comentários
- Sugira melhorias, não apenas aponte problemas
- Reconheça bom trabalho

**Exemplos de Feedback:**

✅ **Bom:**
"Considere extrair este método para melhorar legibilidade. O método está fazendo múltiplas responsabilidades."

❌ **Ruim:**
"Este código está ruim."

✅ **Bom:**
"Adicione validação de entrada aqui para prevenir SQL injection."

❌ **Ruim:**
"Adicione validação."

### 4. Resolução

**Autor:**
- Responda a todos os comentários
- Faça as mudanças necessárias
- Solicite novo review se necessário

**Revisor:**
- Aproveve se estiver satisfeito
- Solicite mudanças se necessário
- Seja razoável com pequenas questões

## Critérios de Aprovação

### Mínimo Obrigatório

- [ ] Build passa sem erros
- [ ] Todos os testes passam
- [ ] Cobertura de testes não diminuiu
- [ ] Checkstyle não tem erros críticos
- [ ] SpotBugs não tem bugs críticos
- [ ] Validação de entrada está presente
- [ ] Tratamento de erros é apropriado

### Desejável

- [ ] Código segue padrões do projeto
- [ ] Documentação está atualizada
- [ ] Testes cobrem edge cases
- [ ] Performance não foi degradada
- [ ] Segurança foi considerada

## Tipos de Mudanças

### Bug Fixes

- Prioridade: Alta
- Review rápido focado na correção
- Verificar se não introduz novos bugs

### Novas Funcionalidades

- Prioridade: Média
- Review completo
- Verificar arquitetura e design
- Verificar testes

### Refatoração

- Prioridade: Baixa
- Foco em qualidade de código
- Verificar se comportamento não mudou

### Documentação

- Prioridade: Baixa
- Verificar precisão
- Verificar clareza

## Ferramentas

### GitHub PR

Use Pull Requests do GitHub para code review:
- Comentários inline
- Revisão de arquivos
- Aprovação explícita

### SonarQube

Use SonarQube para análise automática:
- Code smells
- Bugs
- Vulnerabilidades
- Duplicação

### Checkstyle

Use Checkstyle para verificar estilo:
- Nomenclatura
- Formatação
- Complexidade

## Tempos de Resposta

### Autor

- Responder a comentários: 24 horas
- Fazer mudanças solicitadas: 48 horas

### Revisor

- Iniciar review: 24 horas após notificação
- Completar review: 48 horas após início

### Emergências

Para bugs críticos em produção:
- Review acelerado
- Aprovação em 4 horas
- Deploy imediato após aprovação

## Conflitos

Se houver discordância:
1. Discutir no PR
2. Envolvimento de terceiro se necessário
3. Decisão final do Tech Lead
4. Documentar a decisão

## Métricas

### Acompanhar

- Tempo médio de review
- Número de comentários por PR
- Taxa de aprovação
- Tempo até merge

### Metas

- Tempo médio de review: < 48 horas
- Taxa de aprovação: > 80%
- Tempo até merge: < 72 horas

## Recursos

- [Google Engineering Practices](https://google.github.io/eng-practices/review/)
- [GitHub Review Guidelines](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/reviewing-changes-in-pull-requests)
- [Clean Code](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)

## Suporte

Para dúvidas sobre code review, contate o Tech Lead ou abra uma discussão no repositório.
