-- =====================================================
-- HERMES COMERCIAL - MELHORIAS NOS RELACIONAMENTOS v3.0.1
-- Correções e otimizações nos relacionamentos existentes
-- Data: 06/05/2026
-- =====================================================

-- Esta migration será executada APÓS a V3_0_2 para garantir que todas as tabelas existam

-- Índices para performance (com schema correto)
CREATE INDEX IF NOT EXISTS idx_pagamentos_venda ON hermes_pdv.pagamentos(id_venda);
CREATE INDEX IF NOT EXISTS idx_pagamentos_status ON hermes_pdv.pagamentos(status);
CREATE INDEX IF NOT EXISTS idx_pagamentos_forma ON hermes_pdv.pagamentos(forma_pagamento);

CREATE INDEX IF NOT EXISTS idx_orcamentos_cliente ON hermes_pdv.orcamentos(id_cliente);
CREATE INDEX IF NOT EXISTS idx_orcamentos_status ON hermes_pdv.orcamentos(status);
CREATE INDEX IF NOT EXISTS idx_orcamentos_data_expiracao ON hermes_pdv.orcamentos(data_expiracao);

CREATE INDEX IF NOT EXISTS idx_movimentacoes_produto ON hermes_pdv.movimentacoes_estoque(id_produto);
CREATE INDEX IF NOT EXISTS idx_movimentacoes_tipo ON hermes_pdv.movimentacoes_estoque(tipo_movimentacao);
CREATE INDEX IF NOT EXISTS idx_movimentacoes_data ON hermes_pdv.movimentacoes_estoque(data_movimentacao);

CREATE INDEX IF NOT EXISTS idx_configuracoes_modulo ON hermes_pdv.configuracoes(modulo);
CREATE INDEX IF NOT EXISTS idx_configuracoes_chave ON hermes_pdv.configuracoes(chave);

CREATE INDEX IF NOT EXISTS idx_sistema_logs_data ON hermes_pdv.sistema_logs(data_registro);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_nivel ON hermes_pdv.sistema_logs(nivel);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_modulo ON hermes_pdv.sistema_logs(modulo);

-- Views adicionais para relatórios
CREATE OR REPLACE VIEW vw_resumo_vendas AS
SELECT 
    v.id,
    v.numero_venda,
    v.data_venda,
    c.nome as cliente_nome,
    v.valor_total,
    v.valor_desconto,
    v.valor_final,
    v.status,
    COUNT(iv.id) as quantidade_itens,
    SUM(iv.quantidade) as total_produtos,
    COALESCE(SUM(p.valor_pago), 0) as valor_pago,
    (v.valor_final - COALESCE(SUM(p.valor_pago), 0)) as saldo_restante
FROM hermes_pdv.vendas v
LEFT JOIN hermes_pdv.clientes c ON v.id_cliente = c.id
LEFT JOIN hermes_pdv.itens_venda iv ON v.id = iv.id_venda
LEFT JOIN hermes_pdv.pagamentos p ON v.id = p.id_venda
GROUP BY v.id, v.numero_venda, v.data_venda, c.nome, v.valor_total, 
         v.valor_desconto, v.valor_final, v.status
ORDER BY v.data_venda DESC;

CREATE OR REPLACE VIEW vw_movimentacoes_estoque_resumo AS
SELECT 
    me.id,
    me.data_movimentacao,
    p.nome as produto_nome,
    p.codigo_barras,
    me.tipo_movimentacao,
    me.quantidade,
    me.saldo_anterior,
    me.saldo_novo,
    me.motivo,
    me.usuario_responsavel,
    CASE 
        WHEN me.tipo_movimentacao = 'ENTRADA' THEN 'Reposição'
        WHEN me.tipo_movimentacao = 'SAIDA' THEN 'Venda'
        WHEN me.tipo_movimentacao = 'AJUSTE' THEN 'Ajuste Manual'
        WHEN me.tipo_movimentacao = 'PERDA' THEN 'Perda/Dano'
        WHEN me.tipo_movimentacao = 'DEVOLUCAO' THEN 'Devolução'
        ELSE me.tipo_movimentacao
    END as descricao_tipo
FROM hermes_pdv.movimentacoes_estoque me
JOIN hermes_pdv.produtos p ON me.id_produto = p.id
ORDER BY me.data_movimentacao DESC;

COMMIT;
