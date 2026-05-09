-- =====================================================
-- Hermes Comercial v3.6.4 - Migration
-- Criar tabela de Histórico de Compras
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Histórico de Compras
CREATE TABLE IF NOT EXISTS historico_compras (
    id SERIAL PRIMARY KEY,
    id_cliente INTEGER REFERENCES clientes(id),
    id_produto INTEGER REFERENCES produtos(id),
    data_compra TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    numero_nota_fiscal VARCHAR(50),
    serie_nota_fiscal VARCHAR(20),
    tipo_compra VARCHAR(20) NOT NULL, -- VENDA, COMPRA, DEVOLUCAO, TROCA
    valor_total DECIMAL(15,2) NOT NULL CHECK (valor_total >= 0),
    valor_desconto DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_desconto >= 0),
    valor_acrescimo DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_acrescimo >= 0),
    valor_final DECIMAL(15,2) NOT NULL CHECK (valor_final >= 0),
    forma_pagamento VARCHAR(30), -- DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX, TRANSFERENCIA, BOLETO, CHEQUE
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(15,2) NOT NULL CHECK (preco_unitario >= 0),
    status_compra VARCHAR(20) NOT NULL DEFAULT 'CONCLUIDA', -- PENDENTE, CONCLUIDA, CANCELADA, DEVOLVIDA
    vendedor VARCHAR(100),
    loja VARCHAR(50),
    caixa VARCHAR(50),
    
    -- Campos para controle de fidelidade
    pontos_utilizados INTEGER DEFAULT 0 CHECK (pontos_utilizados >= 0),
    cashback_ganho DECIMAL(15,2) DEFAULT 0.00 CHECK (cashback_ganho >= 0),
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_compra CHECK (tipo_compra IN ('VENDA', 'COMPRA', 'DEVOLUCAO', 'TROCA')),
    CONSTRAINT chk_status_compra CHECK (status_compra IN ('PENDENTE', 'CONCLUIDA', 'CANCELADA', 'DEVOLVIDA')),
    CONSTRAINT chk_valores_positivos CHECK (valor_total >= 0 AND valor_final >= 0 AND preco_unitario >= 0),
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_historico_compras_cliente ON historico_compras(id_cliente);
CREATE INDEX IF NOT EXISTS idx_historico_compras_produto ON historico_compras(id_produto);
CREATE INDEX IF NOT EXISTS idx_historico_compras_data ON historico_compras(data_compra);
CREATE INDEX IF NOT EXISTS idx_historico_compras_tipo ON historico_compras(tipo_compra);
CREATE INDEX IF NOT EXISTS idx_historico_compras_status ON historico_compras(status_compra);
CREATE INDEX IF NOT EXISTS idx_historico_compras_vendedor ON historico_compras(vendedor);
CREATE INDEX IF NOT EXISTS idx_historico_compras_loja ON historico_compras(loja);
CREATE INDEX IF NOT EXISTS idx_historico_compras_caixa ON historico_compras(caixa);
CREATE INDEX IF NOT EXISTS idx_historico_compras_nota_fiscal ON historico_compras(numero_nota_fiscal);

-- Comentários na tabela
COMMENT ON TABLE historico_compras IS 'Tabela de histórico de compras dos clientes';
COMMENT ON COLUMN historico_compras.id IS 'ID único do registro de compra';
COMMENT ON COLUMN historico_compras.id_cliente IS 'ID do cliente que realizou a compra';
COMMENT ON COLUMN historico_compras.id_produto IS 'ID do produto comprado';
COMMENT ON COLUMN historico_compras.data_compra IS 'Data e hora da compra';
COMMENT ON COLUMN historico_compras.numero_nota_fiscal IS 'Número da nota fiscal da compra';
COMMENT ON COLUMN historico_compras.serie_nota_fiscal IS 'Série da nota fiscal';
COMMENT ON COLUMN historico_compras.tipo_compra IS 'Tipo da operação de compra';
COMMENT ON COLUMN historico_compras.valor_total IS 'Valor total da compra sem descontos';
COMMENT ON COLUMN historico_compras.valor_desconto IS 'Valor total de desconto aplicado';
COMMENT ON COLUMN historico_compras.valor_acrescimo IS 'Valor total de acréscimos aplicados';
COMMENT ON COLUMN historico_compras.valor_final IS 'Valor final da compra com descontos/acréscimos';
COMMENT ON COLUMN historico_compras.forma_pagamento IS 'Forma de pagamento utilizada';
COMMENT ON COLUMN historico_compras.quantidade IS 'Quantidade de itens comprados';
COMMENT ON COLUMN historico_compras.preco_unitario IS 'Preço unitário do produto';
COMMENT ON COLUMN historico_compras.status_compra IS 'Status da operação de compra';
COMMENT ON COLUMN historico_compras.vendedor IS 'Nome do vendedor responsável';
COMMENT ON COLUMN historico_compras.loja IS 'Loja onde foi realizada a compra';
COMMENT ON COLUMN historico_compras.caixa IS 'Caixa onde foi realizada a compra';
COMMENT ON COLUMN historico_compras.pontos_utilizados IS 'Pontos de fidelidade utilizados na compra';
COMMENT ON COLUMN historico_compras.cashback_ganho IS 'Valor de cashback ganho na compra';
