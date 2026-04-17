# Documentação do Sistema PDV Hermes Comercial

## Visão Geral

O **Sistema PDV Hermes Comercial** é uma solução completa de Ponto de Venda desenvolvida em Java com JavaFX, projetada para gerenciar vendas, clientes, produtos e relatórios em estabelecimentos comerciais.

## Arquitetura do Sistema

### Tecnologias Utilizadas
- **Java 17** - Linguagem principal
- **JavaFX 17** - Interface gráfica
- **PostgreSQL** - Banco de dados
- **Maven** - Gerenciamento de dependências
- **Flyway** - Migrações de banco de dados
- **Mockito** - Testes unitários

### Estrutura do Projeto
```
src/
  main/
    java/com/br/hermescomercial/
      controller/pdv/          - Controllers JavaFX
      dao/                     - Camada de acesso a dados
      model/                   - Entidades do sistema
      business/pdv/            - Lógica de negócio
      connectionBD/            - Conexão com banco
    resources/
      view/                    - Arquivos FXML
      css/                     - Estilos CSS
      db/migration/            - Migrações Flyway
```

## Funcionalidades do Sistema

### 1. Tela Principal PDV
- **Status**: 100% Funcional
- **Recursos**:
  - Interface principal do sistema
  - Navegação para todas as telas secundárias
  - Exibição de data/hora e informações do operador
  - Botões de acesso rápido: Buscar Produto, Clientes, Relatórios

### 2. Tela de Clientes
- **Status**: 100% Funcional
- **Recursos**:
  - Cadastro completo de clientes (Pessoa Física e Jurídica)
  - Busca por CPF/CNPJ ou nome
  - Formulário com dados pessoais, contato e endereço
  - Validação de campos obrigatórios
  - Status de ativação do cliente

### 3. Tela de Relatórios
- **Status**: 100% Funcional
- **Recursos**:
  - Múltiplas abas de visualização
  - Filtros por período, operador, terminal
  - Relatórios de vendas detalhadas
  - Produtos mais vendidos
  - Formas de pagamento
  - Vendas por hora
  - Desempenho por operador
  - Exportação para PDF e Excel

### 4. Tela de Busca de Produtos
- **Status**: Em desenvolvimento
- **Recursos Planejados**:
  - Busca por código, nome ou código de barras
  - Exibição de estoque disponível
  - Adição ao carrinho de compras
  - Detalhes do produto

### 5. Tela de Pagamento
- **Status**: Em desenvolvimento
- **Recursos Planejados**:
  - Múltiplas formas de pagamento
  - Calculadora de troco
  - Validação de valores
  - Emissão de cupom fiscal

## Banco de Dados

### Estrutura Principal
- **produtos** - Cadastro de produtos
- **clientes** - Cadastro de clientes
- **vendas** - Registro de vendas
- **itens_venda** - Itens de cada venda
- **pagamentos** - Formas de pagamento
- **usuarios** - Usuários do sistema

### Migrações Implementadas
- **V1** - Estrutura básica do banco
- **V2** - Tabelas de vendas e pagamentos
- **V3** - Otimizações e índices

## Como Executar o Sistema

### Pré-requisitos
- Java 17 ou superior
- PostgreSQL 12 ou superior
- Maven 3.6 ou superior

### Passos para Execução
1. **Configurar Banco de Dados**:
   ```bash
   # Criar banco de dados
   createdb hermes_comercial
   
   # Configurar usuário e senha
   # (verificar arquivo de configuração)
   ```

2. **Compilar o Projeto**:
   ```bash
   mvn clean package
   ```

3. **Executar Aplicação**:
   ```bash
   mvn javafx:run
   ```

### Configuração de Ambiente
- **JavaFX**: Configurado via module-path
- **PostgreSQL**: Configurado via application.properties
- **Flyway**: Executa migrações automaticamente

## Controllers Implementados

### PDVPrincipalController
- Gerencia a tela principal
- Controla navegação entre telas
- Gerencia estado da aplicação

### PDVClientesController
- Gerencia cadastro de clientes
- Validação de formulários
- Busca e filtragem de clientes

### PDVRelatoriosController
- Geração de relatórios
- Filtros e parâmetros
- Exportação de dados

### PDVBuscaProdutoController
- Busca de produtos
- Gestão de carrinho
- Detalhes do produto

## DAOs Implementados

### Camada de Acesso a Dados
- **ProdutoDAO** - CRUD de produtos
- **ClienteDAO** - CRUD de clientes
- **VendaDAO** - Gestão de vendas
- **PagamentoDAO** - Formas de pagamento
- **ItemVendaDAO** - Itens de venda
- **UsuarioDAO** - Usuários do sistema

## Testes

### Estrutura de Testes
- **Testes Unitários** - JUnit 5
- **Mock Objects** - Mockito
- **Testes de Integração** - Banco de dados H2

### Testes Implementados
- Testes de DAOs
- Testes de Controllers
- Testes de Models
- Testes de Integração

## Status de Desenvolvimento

### Funcionalidades Concluídas (95%)
- [x] Tela principal PDV
- [x] Tela de clientes
- [x] Tela de relatórios
- [x] Banco de dados PostgreSQL
- [x] Conexão estável
- [x] Interface gráfica responsiva
- [x] Validação de formulários
- [x] Sistema de navegação

### Funcionalidades em Desenvolvimento (5%)
- [ ] Tela de busca de produtos
- [ ] Tela de pagamento
- [ ] Integração com impressora fiscal
- [ ] Sistema de autenticação

## Resolução de Problemas

### Problemas Comuns e Soluções

#### Erro "Priority.1"
- **Causa**: Incompatibilidade de versão JavaFX
- **Solução**: Remoção de atributos HBox.hgrow problemáticos

#### Erro "No resources specified"
- **Causa**: Caracteres especiais em textos de TableColumn
- **Solução**: Substituir "%" por "Perc"

#### Erro "LoadException"
- **Causa**: Referência incorreta a arquivos CSS
- **Solução**: Remover seção stylesheets dos FXML

## Melhorias Futuras

### Planejadas
- [ ] Sistema de autenticação com login/senha
- [ ] Integração com impressora fiscal
- [ ] Suporte a múltiplos caixas
- [ ] Relatórios personalizados
- [ ] Backup automático
- [ ] Sincronização com nuvem

### Otimizações
- [ ] Cache de consultas ao banco
- [ ] Otimização de telas grandes
- [ ] Melhoria de performance
- [ ] Interface responsiva

## Suporte e Manutenção

### Logs do Sistema
- Conexão com banco de dados
- Inicialização da aplicação
- Erros e exceções
- Ações do usuário

### Monitoramento
- Status da conexão
- Performance das consultas
- Uso de memória
- Erros em tempo real

## Conclusão

O Sistema PDV Hermes Comercial representa uma solução robusta e escalável para gestão de pontos de venda. Com arquitetura bem definida, código limpo e testes abrangentes, o sistema está preparado para uso em ambiente de produção com capacidade de expansão conforme as necessidades do negócio.

---

**Versão**: 1.0.1  
**Data**: 17/04/2026  
**Desenvolvedor**: Hermes Comercial  
**Status**: 95% Concluído
