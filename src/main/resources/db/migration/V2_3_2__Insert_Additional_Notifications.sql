-- V2_3_2__Insert_Additional_Notifications.sql
-- Inserção de notificações adicionais para demonstração completa
-- Hermes Comercial PDV v2.3.0

-- Inserir notificações adicionais para melhor demonstração
INSERT INTO notificacao (
    id, titulo, mensagem, tipo, data_criacao, lida, usuario_destino, prioridade
) VALUES 
-- Notificações de sistema adicionais
(13, 'Atualização do Sistema Concluída', 
 'O sistema foi atualizado para a versão 2.3.0 com novos recursos de Dashboard e Notificações.', 
 'SISTEMA', datetime('now', '-3 hours'), FALSE, 'admin', 'MEDIA'),

(14, 'Manutenção Programada', 
 'Manutenção programada para amanhã às 02:00. Previsão de duração: 30 minutos.', 
 'SISTEMA', datetime('now', '-6 hours'), FALSE, 'admin', 'BAIXA'),

(15, 'Novo Funcionário Cadastrado', 
 'Funcionário João Silva foi cadastrado no sistema com permissões de operador.', 
 'SISTEMA', datetime('now', '-12 hours'), TRUE, 'admin', 'BAIXA'),

-- Notificações de vendas adicionais
(16, 'Venda Recorde do Dia!', 
 'Nova venda recorde registrada: R$ 5.670,00 - Cliente Empresa ABC Ltda', 
 'VENDA', datetime('now', '-2 hours'), FALSE, 'admin', 'ALTA'),

(17, 'Meta Diária Atingida', 
 'Parabéns! Meta diária de vendas foi atingida. Total: R$ 22.340,00', 
 'VENDA', datetime('now', '-4 hours'), FALSE, 'admin', 'MEDIA'),

(18, 'Venda Cancelada', 
 'Venda #1234 foi cancelada pelo cliente. Valor: R$ 890,00', 
 'VENDA', datetime('now', '-8 hours'), TRUE, 'admin', 'MEDIA'),

(19, 'Promoção Ativa', 
 'Promoção de 20% em produtos de informática está gerando ótimos resultados!', 
 'VENDA', datetime('now', '-1 day'), FALSE, 'admin', 'BAIXA'),

(20, 'Cliente Premium', 
 'Cliente Maria Santos atingiu status Premium com compras acima de R$ 10.000,00', 
 'VENDA', datetime('now', '-2 days'), TRUE, 'admin', 'MEDIA'),

-- Notificações de estoque adicionais
(21, 'Estoque Crítico - Produto X', 
 'URGENTE: Produto X está com apenas 1 unidade em estoque. Reposição imediata necessária!', 
 'ESTOQUE', datetime('now', '-1 hour'), FALSE, 'admin', 'URGENTE'),

(22, 'Reposição de Estoque', 
 'Fornecedor confirmou entrega de 50 unidades para amanhã.', 
 'ESTOQUE', datetime('now', '-5 hours'), FALSE, 'admin', 'MEDIA'),

(23, 'Produto Esgotado', 
 'Produto Y está esgotado. 15 clientes na lista de espera.', 
 'ESTOQUE', datetime('now', '-9 hours'), FALSE, 'admin', 'ALTA'),

(24, 'Novo Produto em Estoque', 
 'Produto Z (Notebook Gamer) chegou ao estoque com 10 unidades.', 
 'ESTOQUE', datetime('now', '-1 day'), TRUE, 'admin', 'BAIXA'),

(25, 'Validade Próxima', 
 'Atenção: 5 produtos vencem em 7 dias. Verificar lista.', 
 'ESTOQUE', datetime('now', '-2 days'), FALSE, 'admin', 'MEDIA'),

-- Notificações de clientes adicionais
(26, 'Aniversariante do Dia', 
 'Cliente Pedro Oliveira faz aniversário hoje! Oferecer desconto especial.', 
 'CLIENTE', datetime('now', '-3 hours'), FALSE, 'admin', 'BAIXA'),

(27, 'Cliente Inativo Reativado', 
 'Cliente Ana Costa retornou após 6 meses sem compras.', 
 'CLIENTE', datetime('now', '-7 hours'), TRUE, 'admin', 'MEDIA'),

(28, 'Novo Cadastro em Massa', 
 '15 novos clientes cadastrados durante a promoção do fim de semana.', 
 'CLIENTE', datetime('now', '-1 day'), FALSE, 'admin', 'BAIXA'),

(29, 'Reclamação Registrada', 
 'Cliente registrou reclamação sobre demora na entrega. Status: Em análise.', 
 'CLIENTE', datetime('now', '-2 days'), FALSE, 'admin', 'ALTA'),

(30, 'Feedback Positivo', 
 'Cliente deixou avaliação 5 estrelas no Google! "Excelente atendimento."', 
 'CLIENTE', datetime('now', '-3 days'), TRUE, 'admin', 'BAIXA'),

-- Notificações financeiras adicionais
(31, 'Boleto Vence Hoje', 
 'Atenção: 3 boletos vencem hoje. Total: R$ 4.500,00', 
 'FINANCEIRO', datetime('now', '-30 minutes'), FALSE, 'admin', 'ALTA'),

(32, 'Transferência Recebida', 
 'Transferência de R$ 12.000,00 recebida do Cliente XYZ Ltda.', 
 'FINANCEIRO', datetime('now', '-2 hours'), TRUE, 'admin', 'MEDIA'),

(33, 'Aluguel a Pagar', 
 'Aluguel do mês no valor de R$ 3.500,00 vence em 5 dias.', 
 'FINANCEIRO', datetime('now', '-6 hours'), FALSE, 'admin', 'MEDIA'),

(34, 'Investimento Realizado', 
 'Investimento de R$ 8.000,00 em novos equipamentos aprovado.', 
 'FINANCEIRO', datetime('now', '-1 day'), TRUE, 'admin', 'BAIXA'),

(35, 'Economia de Energia', 
 'Conta de energia 15% menor que mês anterior. Economia: R$ 340,00', 
 'FINANCEIRO', datetime('now', '-2 days'), FALSE, 'admin', 'BAIXA'),

(36, 'Imposto a Recolher', 
 'PIS/COFINS do mês: R$ 2.340,00. Vencimento: 20/05/2026', 
 'FINANCEIRO', datetime('now', '-3 days'), FALSE, 'admin', 'ALTA'),

(37, 'Empréstimo Aprovado', 
 'Empréstimo de R$ 25.000,00 aprovado pelo banco. Taxa: 1,5% a.m.', 
 'FINANCEIRO', datetime('now', '-4 days'), TRUE, 'admin', 'MEDIA'),

(38, 'Meta Financeira Batida', 
 'Meta de faturamento mensal superada em 18%! Parabéns equipe!', 
 'FINANCEIRO', datetime('now', '-5 days'), FALSE, 'admin', 'ALTA');
