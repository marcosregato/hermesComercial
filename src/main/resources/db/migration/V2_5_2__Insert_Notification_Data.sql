-- Inserir dados de exemplo para notificações no sistema Hermes Comercial
-- Versão 2.5.2 - Dados de exemplo para teste e demonstração da tela de notificação

-- Limpar notificações existentes (se houver)
DELETE FROM notificacao;

-- Inserir notificações para o usuário admin
INSERT INTO notificacao (titulo, mensagem, tipo, prioridade, usuario_destino, lida) VALUES
-- Notificações de Sistema
('Bem-vindo ao Sistema', 'Olá! Seja bem-vindo ao Hermes Comercial v2.5.0. O sistema está pronto para uso.', 'SISTEMA', 'MEDIA', 'admin', FALSE),
('Atualização do Sistema', 'Nova versão 2.5.1 disponível com correções de performance e novos recursos.', 'SISTEMA', 'ALTA', 'admin', FALSE),
('Backup Realizado', 'Backup automático concluído com sucesso. Arquivo salvo em /backups/', 'SISTEMA', 'BAIXA', 'admin', TRUE),

-- Notificações de Venda
('Nova Venda Realizada', 'Venda #001 registrada no valor de R$ 1.299,99 - Notebook Dell Inspiron 15', 'VENDA', 'MEDIA', 'admin', FALSE),
('Meta de Vendas Atingida', 'Parabéns! Meta mensal de R$ 50.000,00 foi atingida.', 'VENDA', 'ALTA', 'admin', FALSE),
('Venda Cancelada', 'Venda #002 cancelada pelo cliente. Estoque devolvido.', 'VENDA', 'MEDIA', 'admin', TRUE),

-- Notificações de Estoque
('Estoque Baixo', 'Produto INF001 - Notebook Dell Inspiron 15 com estoque crítico: apenas 3 unidades.', 'ESTOQUE', 'ALTA', 'admin', FALSE),
('Produto sem Estoque', 'Produto MON001 - Monitor Samsung 4K está sem estoque.', 'ESTOQUE', 'URGENTE', 'admin', FALSE),
('Reposição de Estoque', 'Novo lote de produtos recebido. 50 unidades adicionadas ao estoque.', 'ESTOQUE', 'MEDIA', 'admin', TRUE),

-- Notificações de Cliente
('Novo Cliente Cadastrado', 'Cliente João Silva cadastrado com sucesso. CPF: 123.456.789-00', 'CLIENTE', 'BAIXA', 'admin', TRUE),
('Cliente Inadimplente', 'Cliente Maria Santos com pagamento em atraso há 15 dias.', 'CLIENTE', 'ALTA', 'admin', FALSE),

-- Notificações Financeiras
('Pagamento Recebido', 'Pagamento de R$ 2.500,00 recebido do cliente ABC Comércio.', 'FINANCEIRO', 'MEDIA', 'admin', FALSE),
('Fatura a Vencer', 'Fatura de energia elétrica vence em 3 dias. Valor: R$ 450,00', 'FINANCEIRO', 'ALTA', 'admin', FALSE),
('Relatório Mensal', 'Relatório financeiro do mês já está disponível para consulta.', 'FINANCEIRO', 'BAIXA', 'admin', TRUE),

-- Notificações de Sistema (continuação)
('Manutenção Programada', 'Sistema passará por manutenção hoje às 23:00. Previsão de retorno: 23:30.', 'SISTEMA', 'MEDIA', 'admin', FALSE),
('Erro de Conexão', 'Falha na conexão com a impressora fiscal. Verifique o equipamento.', 'SISTEMA', 'ALTA', 'admin', FALSE),
('Limpeza Automática', 'Logs antigos foram removidos automaticamente para liberar espaço.', 'SISTEMA', 'BAIXA', 'admin', TRUE),

-- Notificações de Venda (continuação)
('Venda de Grande Valor', 'Venda de R$ 15.000,00 realizada - 10 Notebooks Dell Inspiron.', 'VENDA', 'ALTA', 'admin', FALSE),
('Desconto Aplicado', 'Desconto de 15% aplicado na venda #003. Valor: R$ 194,99.', 'VENDA', 'MEDIA', 'admin', TRUE),

-- Notificações de Estoque (continuação)
('Lote Próximo ao Vencimento', 'Produto ARM002 - SSD 500GB vence em 7 dias.', 'ESTOQUE', 'ALTA', 'admin', FALSE),
('Movimentação de Estoque', 'Saída de 25 unidades do produto INF002 - Mouse Logitech.', 'ESTOQUE', 'MEDIA', 'admin', FALSE),

-- Notificações de Cliente (continuação)
('Cliente Premium', 'Cliente Tech Solutions atingiu status Premium. Total compras: R$ 50.000+', 'CLIENTE', 'MEDIA', 'admin', FALSE),
('Reclamação Recebida', 'Cliente registrou reclamação sobre produto com defeito. Protocolo #12345.', 'CLIENTE', 'ALTA', 'admin', FALSE),

-- Notificações Financeiras (continuação)
('Transferência Recebida', 'Transferência bancária recebida: R$ 5.000,00', 'FINANCEIRO', 'MEDIA', 'admin', FALSE),
('Boleto Gerado', 'Boleto mensal gerado para cliente XYZ Ltda. Valor: R$ 3.200,00', 'FINANCEIRO', 'BAIXA', 'admin', TRUE),

-- Notificações de Sistema (continuação)
('Atualização de Segurança', 'Nova atualização de segurança disponível. Recomendada instalação.', 'SISTEMA', 'ALTA', 'admin', FALSE),
('Performance Melhorada', 'Otimização do banco de dados concluída. Sistema 40% mais rápido.', 'SISTEMA', 'MEDIA', 'admin', FALSE),

-- Notificações Diversas
('Aniversário do Sistema', 'Hermes Comercial completa 1 ano de operação!', 'SISTEMA', 'BAIXA', 'admin', FALSE),
('Meta Diária Atingida', 'Meta de vendas diária atingida: R$ 5.000,00', 'VENDA', 'MEDIA', 'admin', FALSE),
('Novo Funcionário', 'Novo funcionário cadastrado: Pedro Santos - Vendedor', 'SISTEMA', 'BAIXA', 'admin', TRUE),

-- Notificações de Alerta
('Senha Alterada', 'Sua senha foi alterada com sucesso. Se não foi você, contate o suporte.', 'SISTEMA', 'ALTA', 'admin', FALSE),
('Acesso Suspeito', 'Tentativa de acesso suspeita detectada. IP: 192.168.1.100', 'SISTEMA', 'URGENTE', 'admin', FALSE),

-- Notificações de Manutenção
('Banco de Dados', 'Manutenção do banco de dados concluída. Performance otimizada.', 'SISTEMA', 'MEDIA', 'admin', FALSE),
('Serviços Reiniciados', 'Serviços do sistema reiniciados com sucesso.', 'SISTEMA', 'BAIXA', 'admin', TRUE),

-- Notificações de Relatórios
('Relatório de Vendas', 'Relatório semanal de vendas gerado com sucesso.', 'FINANCEIRO', 'MEDIA', 'admin', FALSE),
('Análise de Produtos', 'Análise de produtos mais vendidos disponível.', 'FINANCEIRO', 'BAIXA', 'admin', TRUE),

-- Notificações Finais
('Integração ERP/PDV', 'Sistema de integração ERP/PDV ativado com sucesso.', 'SISTEMA', 'ALTA', 'admin', FALSE),
('Teste de Notificação', 'Esta é uma notificação de teste para validar o sistema.', 'SISTEMA', 'BAIXA', 'admin', TRUE);
