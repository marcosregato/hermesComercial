-- V2_3_0__Insert_Dashboard_Data.sql
-- Script de inserção de dados iniciais para o Dashboard Analítico
-- Hermes Comercial PDV v2.3.0

-- Inserir métricas de exemplo para o Dashboard
INSERT INTO dashboard_metrics (
    id, nome, valor, unidade, data_referencia, tipo, categoria, 
    valor_anterior, meta, descricao, created_at
) VALUES 
-- Vendas Diárias
(1, 'Vendas Diárias', 18500.00, 'R$', date('now'), 'VENDAS_DIARIAS', 'Vendas', 
 15000.00, 20000.00, 'Total de vendas realizadas no dia', datetime('now')),

-- Total de Clientes  
(2, 'Total de Clientes', 1350, 'clientes', date('now'), 'TOTAL_CLIENTES', 'Clientes',
 1325, 1500, 'Total de clientes cadastrados no sistema', datetime('now')),

-- Produtos Vendidos
(3, 'Produtos Vendidos', 520, 'unidades', date('now'), 'PRODUTOS_VENDIDOS', 'Vendas',
 380, 500, 'Total de produtos vendidos no dia', datetime('now')),

-- Faturamento Mensal
(4, 'Faturamento Mensal', 45000.00, 'R$', date('now', 'start of month'), 'FATURAMENTO', 'Financeiro',
 42000.00, 50000.00, 'Total de faturamento no mês', datetime('now')),

-- Custos Mensais
(5, 'Custos Mensais', 12000.00, 'R$', date('now', 'start of month'), 'CUSTOS', 'Financeiro',
 11500.00, 13000.00, 'Total de custos operacionais no mês', datetime('now')),

-- Vendas Mensais
(6, 'Vendas Mensais', 156, 'transações', date('now', 'start of month'), 'VENDAS_MENSAIS', 'Vendas',
 142, 160, 'Total de transações realizadas no mês', datetime('now')),

-- Ticket Médio
(7, 'Ticket Médio', 288.46, 'R$', date('now'), 'TICKET_MEDIO', 'Vendas',
 295.77, 300.00, 'Valor médio por transação', datetime('now')),

-- Margem de Lucro
(8, 'Margem de Lucro', 73.33, '%', date('now'), 'MARGEM_LUCRO', 'Financeiro',
 72.62, 75.00, 'Percentual de margem de lucro', datetime('now')),

-- Estoque Baixo
(9, 'Estoque Baixo', 12, 'produtos', date('now'), 'ESTOQUE_BAIXO', 'Estoque',
 8, 5, 'Produtos com estoque abaixo do mínimo', datetime('now')),

-- Taxa de Conversão
(10, 'Taxa de Conversão', 68.5, '%', date('now'), 'TAXA_CONVERSAO', 'Vendas',
 65.2, 70.0, 'Percentual de visitantes que compram', datetime('now')),

-- Satisfação do Cliente
(11, 'Satisfação do Cliente', 4.6, 'estrelas', date('now'), 'SATISFACAO_CLIENTE', 'Clientes',
 4.5, 5.0, 'Avaliação média de satisfação dos clientes', datetime('now')),

-- Meta Mensal
(12, 'Meta Mensal', 90.0, '%', date('now'), 'META_MENSAL', 'Performance',
 85.0, 100.0, 'Percentual da meta mensal alcançada', datetime('now'));

-- Inserir movimentações de caixa para resumo financeiro
INSERT INTO caixa_movimentacoes (
    id, tipo, valor, descricao, data_movimentacao, created_at
) VALUES 
-- Entradas recentes
(1, 'ENTRADA', 1250.00, 'Venda - Produto A', date('now', '-1 days'), datetime('now', '-1 days')),
(2, 'ENTRADA', 890.50, 'Venda - Produto B', date('now', '-1 days'), datetime('now', '-1 days')),
(3, 'ENTRADA', 2100.00, 'Venda - Produto C', date('now', '-2 days'), datetime('now', '-2 days')),
(4, 'ENTRADA', 560.00, 'Venda - Produto D', date('now', '-3 days'), datetime('now', '-3 days')),
(5, 'ENTRADA', 1780.30, 'Venda - Produto E', date('now', '-4 days'), datetime('now', '-4 days')),
(6, 'ENTRADA', 920.00, 'Venda - Produto F', date('now', '-5 days'), datetime('now', '-5 days')),
(7, 'ENTRADA', 3450.00, 'Venda - Produto G', date('now', '-6 days'), datetime('now', '-6 days')),
(8, 'ENTRADA', 670.80, 'Venda - Produto H', date('now', '-7 days'), datetime('now', '-7 days')),
(9, 'ENTRADA', 1890.00, 'Venda - Produto I', date('now', '-8 days'), datetime('now', '-8 days')),
(10, 'ENTRADA', 1120.00, 'Venda - Produto J', date('now', '-9 days'), datetime('now', '-9 days')),
(11, 'ENTRADA', 2340.00, 'Venda - Produto K', date('now', '-10 days'), datetime('now', '-10 days')),
(12, 'ENTRADA', 890.00, 'Venda - Produto L', date('now', '-11 days'), datetime('now', '-11 days')),
(13, 'ENTRADA', 1560.00, 'Venda - Produto M', date('now', '-12 days'), datetime('now', '-12 days')),
(14, 'ENTRADA', 780.00, 'Venda - Produto N', date('now', '-13 days'), datetime('now', '-13 days')),
(15, 'ENTRADA', 2980.00, 'Venda - Produto O', date('now', '-14 days'), datetime('now', '-14 days')),
(16, 'ENTRADA', 1450.00, 'Venda - Produto P', date('now', '-15 days'), datetime('now', '-15 days')),
(17, 'ENTRADA', 920.00, 'Venda - Produto Q', date('now', '-16 days'), datetime('now', '-16 days')),
(18, 'ENTRADA', 3120.00, 'Venda - Produto R', date('now', '-17 days'), datetime('now', '-17 days')),
(19, 'ENTRADA', 680.00, 'Venda - Produto S', date('now', '-18 days'), datetime('now', '-18 days')),
(20, 'ENTRADA', 1890.00, 'Venda - Produto T', date('now', '-19 days'), datetime('now', '-19 days')),
(21, 'ENTRADA', 2230.00, 'Venda - Produto U', date('now', '-20 days'), datetime('now', '-20 days')),
(22, 'ENTRADA', 1560.00, 'Venda - Produto V', date('now', '-21 days'), datetime('now', '-21 days')),
(23, 'ENTRADA', 890.00, 'Venda - Produto W', date('now', '-22 days'), datetime('now', '-22 days')),
(24, 'ENTRADA', 3450.00, 'Venda - Produto X', date('now', '-23 days'), datetime('now', '-23 days')),
(25, 'ENTRADA', 1230.00, 'Venda - Produto Y', date('now', '-24 days'), datetime('now', '-24 days')),
(26, 'ENTRADA', 1780.00, 'Venda - Produto Z', date('now', '-25 days'), datetime('now', '-25 days')),
(27, 'ENTRADA', 2340.00, 'Venda - Produto AA', date('now', '-26 days'), datetime('now', '-26 days')),
(28, 'ENTRADA', 980.00, 'Venda - Produto AB', date('now', '-27 days'), datetime('now', '-27 days')),
(29, 'ENTRADA', 1560.00, 'Venda - Produto AC', date('now', '-28 days'), datetime('now', '-28 days')),
(30, 'ENTRADA', 2890.00, 'Venda - Produto AD', date('now', '-29 days'), datetime('now', '-29 days')),

-- Saídas recentes
(31, 'SAIDA', 450.00, 'Compra de material de escritório', date('now', '-2 days'), datetime('now', '-2 days')),
(32, 'SAIDA', 890.00, 'Pagamento de aluguel', date('now', '-5 days'), datetime('now', '-5 days')),
(33, 'SAIDA', 230.00, 'Compra de insumos', date('now', '-7 days'), datetime('now', '-7 days')),
(34, 'SAIDA', 670.00, 'Manutenção de equipamentos', date('now', '-10 days'), datetime('now', '-10 days')),
(35, 'SAIDA', 340.00, 'Serviços de limpeza', date('now', '-12 days'), datetime('now', '-12 days')),
(36, 'SAIDA', 1200.00, 'Pagamento de fornecedores', date('now', '-15 days'), datetime('now', '-15 days')),
(37, 'SAIDA', 560.00, 'Contas de energia', date('now', '-17 days'), datetime('now', '-17 days')),
(38, 'SAIDA', 290.00, 'Internet e telefonia', date('now', '-20 days'), datetime('now', '-20 days')),
(39, 'SAIDA', 450.00, 'Marketing e propaganda', date('now', '-22 days'), datetime('now', '-22 days')),
(40, 'SAIDA', 780.00, 'Impostos e taxas', date('now', '-25 days'), datetime('now', '-25 days')),
(41, 'SAIDA', 320.00, 'Transporte e logística', date('now', '-27 days'), datetime('now', '-27 days')),
(42, 'SAIDA', 1500.00, 'Folha de pagamento', date('now', '-30 days'), datetime('now', '-30 days'));

-- Inserir notificações de exemplo
INSERT INTO notificacao (
    id, titulo, mensagem, tipo, data_criacao, lida, usuario_destino, prioridade
) VALUES 
-- Notificações do sistema
(1, 'Bem-vindo ao Hermes Comercial PDV v2.3.0', 
 'Novos recursos de Dashboard e Notificações estão disponíveis! Acesse pelo menu Operações.', 
 'SISTEMA', datetime('now', '-2 hours'), FALSE, 'admin', 'ALTA'),

(2, 'Backup Automático Realizado', 
 'Backup do banco de dados concluído com sucesso em ' || datetime('now', '-1 day'), 
 'SISTEMA', datetime('now', '-6 hours'), FALSE, 'admin', 'MEDIA'),

-- Notificações de vendas
(3, 'Meta de Vendas Superada!', 
 'Parabéns! A meta mensal de vendas foi superada em 10%. Total: R$ 45.000,00', 
 'VENDA', datetime('now', '-4 hours'), FALSE, 'admin', 'ALTA'),

(4, 'Venda de Grande Valor', 
 'Nova venda registrada: R$ 3.450,00 - Produto G', 
 'VENDA', datetime('now', '-8 hours'), TRUE, 'admin', 'MEDIA'),

-- Notificações de estoque
(5, 'Estoque Baixo - Produto A', 
 'O Produto A está com apenas 3 unidades em estoque. Reposição necessária.', 
 'ESTOQUE', datetime('now', '-12 hours'), FALSE, 'admin', 'ALTA'),

(6, 'Estoque Baixo - Produto B', 
 'O Produto B está com apenas 2 unidades em estoque. Reposição necessária.', 
 'ESTOQUE', datetime('now', '-1 day'), FALSE, 'admin', 'ALTA'),

(7, 'Estoque Baixo - Produto C', 
 'O Produto C está com apenas 4 unidades em estoque. Reposição recomendada.', 
 'ESTOQUE', datetime('now', '-2 days'), TRUE, 'admin', 'MEDIA'),

-- Notificações de clientes
(8, 'Novo Cliente Cadastrado', 
 'Cliente Empresa ABC Ltda foi cadastrado no sistema.', 
 'CLIENTE', datetime('now', '-1 day'), TRUE, 'admin', 'BAIXA'),

(9, 'Cliente Inativo', 
 'O cliente João Silva não realiza compras há 90 dias.', 
 'CLIENTE', datetime('now', '-3 days'), FALSE, 'admin', 'MEDIA'),

-- Notificações financeiras
(10, 'Pagamento Pendente', 
 'Fornecedor XYZ aguarda pagamento de R$ 1.200,00.', 
 'FINANCEIRO', datetime('now', '-18 hours'), FALSE, 'admin', 'ALTA'),

(11, 'Recebimento Confirmado', 
 'Recebimento de R$ 5.670,00 confirmado do Cliente ABC.', 
 'FINANCEIRO', datetime('now', '-2 days'), TRUE, 'admin', 'MEDIA'),

(12, 'Relatório Financeiro Disponível', 
 'Relatório financeiro mensal está disponível para consulta.', 
 'FINANCEIRO', datetime('now', '-4 days'), FALSE, 'admin', 'BAIXA');
