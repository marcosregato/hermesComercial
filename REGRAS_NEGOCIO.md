# Regras de Negócio - Hermes Comercial

## Visão Geral

O Hermes Comercial é um sistema de gestão comercial completo desenvolvido em JavaFX com PostgreSQL, projetado para automação comercial de lojas. Este documento define todas as regras de negócio implementadas no sistema.

---

## 1. Módulo de Produtos

### 1.1 Cadastro de Produtos
- **Identificação Única**: Cada produto deve possuir código único e código de barras
- **Campos Obrigatórios**: Nome, categoria, preço de venda, unidade
- **Campos Opcionais**: Marca, subcategoria, código de barras, preço de custo
- **Validação**: Preço de venda deve ser maior que zero
- **Impostos**: Produto pode estar vinculado a um grupo de impostos

### 1.2 Controle de Estoque
- **Quantidade Mínima**: Sistema deve alertar quando estoque atingir quantidade mínima
- **Quantidade Máxima**: Sistema deve controlar capacidade máxima de estoque
- **Baixa Automática**: Estoque é baixado automaticamente após venda concluída
- **Entrada Manual**: Permitir ajustes manuais de estoque com justificativa

---

## 2. Módulo de Clientes

### 2.1 Cadastro de Clientes
- **Tipo Pessoa**: 'F' para Pessoa Física, 'J' para Pessoa Jurídica
- **Campos PF**: CPF, RG, Data de Nascimento
- **Campos PJ**: CNPJ, Nome Fantasia, Inscrição Estadual
- **Campos Comuns**: Nome, telefone, celular, email, endereço completo
- **Status**: Cliente pode ser ativo ou inativo
- **Validação**: CPF/CNPJ deve ser único e válido

### 2.2 Fidelização
- **Histórico de Compras**: Sistema mantém histórico completo de compras do cliente
- **Descontos Especiais**: Clientes cadastrados podem receber descontos
- **Limite de Crédito**: Configuração de limite de crédito para clientes fiéis

---

## 3. Módulo PDV (Ponto de Venda)

### 3.1 Operação do Caixa
- **Sessão PDV**: Operador deve iniciar sessão antes de vender
- **Status do Caixa**: Caixa deve estar aberto para iniciar vendas
- **Terminal**: Cada terminal PDV possui identificação única
- **Modo Treinamento**: Operações em modo treinamento não afetam estoque/caixa

### 3.2 Processo de Venda
- **Carrinho de Compras**: Sistema mantém carrinho temporário por sessão
- **Adição de Itens**: Produto só pode ser adicionado se houver estoque disponível
- **Remoção de Itens**: Itens podem ser removidos antes da finalização
- **Cancelamento**: Venda pode ser cancelada antes da finalização
- **Descontos**: Descontos podem ser aplicados por item ou no total da venda

### 3.3 Pagamento
- **Formas Aceitas**: Dinheiro, Cartão Débito, Cartão Crédito, PIX, Transferência
- **Múltiplos Pagamentos**: Permitir combinação de formas de pagamento
- **Troco**: Sistema calcula troco automaticamente para pagamentos em dinheiro
- **Parcelamento**: Cartão de crédito permite parcelamento
- **Validação**: Valor pago deve ser igual ou maior que valor final

### 3.4 Cupom Fiscal
- **Numeração Única**: Cada venda gera número de cupom único
- **Emissão**: Cupom pode ser impresso ou enviado por email
- **Cancelamento**: Cupom cancelado deve ser registrado com motivo
- **Armazenamento**: XML e PDF do cupom são armazenados para auditoria

---

## 4. Módulo de Usuários e Permissões

### 4.1 Tipos de Usuário
- **Administrador**: Acesso total ao sistema
- **Gerente**: Acesso a relatórios e configurações, exceto usuários
- **Operador**: Acesso apenas ao PDV e consultas básicas
- **Caixa**: Acesso restrito ao PDV

### 4.2 Segurança
- **Login/Senha**: Cada usuário possui credenciais únicas
- **Sessão**: Tempo limite de inatividade (configurável)
- **Auditoria**: Todas as ações críticas são registradas
- **Permissões**: Acesso baseado em perfil de usuário

---

## 5. Módulo Financeiro

### 5.1 Contas a Pagar/Receber
- **Fornecedores**: Cadastro de fornecedores com dados bancários
- **Contas a Pagar**: Registro de despesas e custos operacionais
- **Contas a Receber**: Controle de vendas parceladas
- **Vencimento**: Sistema alerta sobre vencimentos próximos

### 5.2 Fluxo de Caixa
- **Movimentação**: Registro de todas as entradas e saídas
- **Conciliação**: Conciliação bancária diária
- **Relatórios**: Fechamento de caixa por período/operador
- **Sangria/Suprimento**: Operações de ajuste de caixa

---

## 6. Módulo de Relatórios

### 6.1 Relatórios de Vendas
- **Vendas por Período**: Análise de faturamento diário/semanal/mensal
- **Produtos Mais Vendidos**: Ranking de produtos por quantidade/valor
- **Vendas por Operador**: Desempenho individual dos vendedores
- **Margem de Lucro**: Análise de rentabilidade por produto/venda

### 6.2 Relatórios de Estoque
- **Estoque Atual**: Posição atual de todos os produtos
- **Estoque Crítico**: Produtos abaixo do mínimo
- **Movimentação**: Entradas e saídas por período
- **Inventário**: Relatório para contagem física

### 6.3 Relatórios Financeiros
- **Fluxo de Caixa**: Resumo de entradas e saídas
- **Receitas vs Despesas**: Análise de rentabilidade
- **Contas Vencidas**: Relatório de inadimplência
- **Projeções**: Previsão de fluxo para próximos períodos

---

## 7. Módulo Fiscal

### 7.1 Tributação
- **Grupos de Impostos**: Produtos podem ser agrupados por tributação
- **CST/CSOSN**: Classificação fiscal para ICMS
- **Alíquotas**: Configuração de alíquotas (ICMS, PIS, COFINS, IPI)
- **CFOP**: Código Fiscal de Operações e Prestações

### 7.2 Documentos Fiscais
- **NF-e**: Emissão de Nota Fiscal Eletrônica
- **Cupom Fiscal**: Emissão de cupom fiscal para vendas
- **Cancelamento**: Procedimento para cancelamento de documentos
- **Inutilização**: Inutilização de numeração de documentos

---

## 8. Regras de Validação

### 8.1 Validações de Negócio
- **Estoque Negativo**: Sistema não permite estoque negativo
- **Preço Zero**: Produto não pode ter preço de venda zero
- **CPF/CNPJ**: Documento fiscal deve ser válido
- **Data Futura**: Datas não podem ser futuras (exceto previsões)
- **Valor Negativo**: Valores monetários não podem ser negativos

### 8.2 Validações de Integridade
- **Chaves Estrangeiras**: Todas as referências devem existir
- **Dados Duplicados**: Impedir cadastros duplicados
- **Campos Obrigatórios**: Validar preenchimento obrigatório
- **Formato de Dados**: Validar formatos (email, CEP, telefone)

---

## 9. Regras de Processamento

### 9.1 Transações
- **Atomicidade**: Venda deve ser completamente processada ou cancelada
- **Consistência**: Dados devem permanecer consistentes após operação
- **Isolamento**: Transações concorrentes não devem interferir
- **Durabilidade**: Dados confirmados devem ser permanentes

### 9.2 Performance
- **Cache**: Dados frequentemente acessados devem ser cacheados
- **Índices**: Consultas críticas devem ter índices otimizados
- **Batch**: Operações em lote para melhor performance
- **Async**: Processos longos devem ser assíncronos

---

## 10. Configurações do Sistema

### 10.1 Parâmetros PDV
- **Número Terminal**: Identificação do terminal PDV
- **Impressão Cupom**: Configuração de impressão automática
- **Tempo Inatividade**: Timeout para sessão inativa
- **Modo Debug**: Ativação para desenvolvimento/testes

### 10.2 Dados da Empresa
- **CNPJ**: CNPJ da empresa para documentos fiscais
- **Dados Bancários**: Conta para transferências e PIX
- **Endereço**: Endereço fiscal da empresa
- **Regime Tributário**: Configuração do regime tributário

---

## 11. Integrações

### 11.1 Sistemas Externos
- **SEFAZ**: Integração para emissão de NF-e
- **Bancos**: Conciliação bancária automática
- **Gateways Pagamento**: Processamento de cartões/PIX
- **E-commerce**: Sincronização com loja virtual

### 11.2 Hardware
- **Impressoras**: Impressoras de cupom fiscal e não fiscal
- **Leitores**: Leitores de código de barras
- **Gavetas**: Comando para abertura de gaveta de dinheiro
- **Balanças**: Integração com balanças digitais

---

## 12. Backup e Recuperação

### 12.1 Backup Automático
- **Frequência**: Backup diário automático
- **Retenção**: Manter backups por 30 dias
- **Validação**: Verificação integridade dos backups
- **Armazenamento**: Backup local e na nuvem

### 12.2 Recuperação
- **Ponto Restauração**: Múltiplos pontos de restauração
- **Testes**: Testes periódicos de recuperação
- **Documentação**: Procedimentos detalhados de recuperação
- **Tempo RTO**: Tempo máximo de recuperação: 4 horas

---

## 13. Auditoria e Log

### 13.1 Registro de Auditoria
- **Ações Críticas**: Login, vendas, cancelamentos, ajustes
- **Usuário**: Registrar quem realizou cada ação
- **Data/Hora**: Timestamp exato de cada operação
- **Detalhes**: Informações detalhadas da operação

### 13.2 Logs do Sistema
- **Erros**: Registro de todos os erros do sistema
- **Performance**: Monitoramento de performance
- **Acessos**: Registro de tentativas de acesso
- **Alertas**: Sistema de alertas automáticos

---

## 14. Regras Específicas por Setor

### 14.1 Varejo
- **Promoções**: Suporte a promoções e descontos especiais
- **Programa Fidelidade**: Pontuação e recompensas
- **Troca/Devolução**: Processo de troca de mercadorias
- **Venda Casada**: Kits e combos de produtos

### 14.2 Food Service
- **Comanda**: Sistema de comanda para mesas
- **Cozinha**: Integração com sistema de cozinha
- **Delivery**: Gestão de entregas
- **Taxa Serviço**: Cálculo automático de taxa de serviço

---

## 15. Conformidade Legal

### 15.1 LGPD
- **Consentimento**: Obter consentimento para dados pessoais
- **Anonimização**: Anonimizar dados quando possível
- **Direitos do Titular**: Atender solicitações de acesso/exclusão
- **Segurança**: Medidas de segurança para dados pessoais

### 15.2 Fiscal
- **SPED**: Entrega de arquivos fiscais
- **EFD**: Escrituração Fiscal Digital
- **SAT**: Cumprimento de obrigações do SAT
- **Regras Atuais**: Manter-se atualizado com legislação

---

## Conclusão

Este documento define as regras de negócio fundamentais do Hermes Comercial, servindo como guia para desenvolvimento, manutenção e evolução do sistema. Todas as implementações devem seguir estas regras para garantir consistência, segurança e conformidade com as melhores práticas do mercado.

**Versão**: 1.0  
**Data**: 17/04/2026  
**Responsável**: Equipe de Desenvolvimento Hermes Comercial
