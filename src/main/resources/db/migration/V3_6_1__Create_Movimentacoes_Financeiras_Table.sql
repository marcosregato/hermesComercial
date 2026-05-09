-- =====================================================
-- Hermes Comercial v3.6.1 - Migration
-- Criar tabela de Movimentações Financeiras
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Movimentações Financeiras
CREATE TABLE IF NOT EXISTS movimentacoes_financeiras (
    id SERIAL PRIMARY KEY,
    data_movimentacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_movimentacao VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA, TRANSFERENCIA, SANGRIA, SUPRIMENTO, AJUSTE
    categoria VARCHAR(50) NOT NULL, -- VENDAS, COMPRAS, SERVICOS, ALUGUEL, AGUA, LUZ, TELEFONE, INTERNET, MATERIAL_ESCRITORIO, EQUIPAMENTOS, IMPOSTOS, MARKETING, TRANSPORTE, SALARIOS, OUTROS
    descricao TEXT NOT NULL,
    valor DECIMAL(15,2) NOT NULL CHECK (valor >= 0),
    forma_pagamento VARCHAR(30), -- DINHEIRO, TRANSFERENCIA, TED, DOC, BOLETO, CHEQUE, CARTAO_CREDITO, CARTAO_DEBITO, PIX, OUTROS
    conta_caixa VARCHAR(50), -- Conta ou caixa de origem/destino
    documento_referencia VARCHAR(100), -- NF, Nota Fiscal, Recibo, etc.
    responsavel VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, CONFIRMADO, CANCELADO, PROCESSANDO
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_movimentacao CHECK (tipo_movimentacao IN ('ENTRADA', 'SAIDA', 'TRANSFERENCIA', 'SANGRIA', 'SUPRIMENTO', 'AJUSTE')),
    CONSTRAINT chk_status CHECK (status IN ('PENDENTE', 'CONFIRMADO', 'CANCELADO', 'PROCESSANDO')),
    CONSTRAINT chk_valor_positivo CHECK (valor >= 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_data ON movimentacoes_financeiras(data_movimentacao);
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_tipo ON movimentacoes_financeiras(tipo_movimentacao);
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_categoria ON movimentacoes_financeiras(categoria);
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_status ON movimentacoes_financeiras(status);
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_responsavel ON movimentacoes_financeiras(responsavel);
CREATE INDEX IF NOT EXISTS idx_mov_financeiras_conta ON movimentacoes_financeiras(conta_caixa);

-- Comentários na tabela
COMMENT ON TABLE movimentacoes_financeiras IS 'Tabela de movimentações financeiras do sistema';
COMMENT ON COLUMN movimentacoes_financeiras.id IS 'ID único da movimentação';
COMMENT ON COLUMN movimentacoes_financeiras.data_movimentacao IS 'Data e hora da movimentação';
COMMENT ON COLUMN movimentacoes_financeiras.tipo_movimentacao IS 'Tipo da movimentação financeira';
COMMENT ON COLUMN movimentacoes_financeiras.categoria IS 'Categoria da movimentação para controle financeiro';
COMMENT ON COLUMN movimentacoes_financeiras.descricao IS 'Descrição detalhada da movimentação';
COMMENT ON COLUMN movimentacoes_financeiras.valor IS 'Valor monetário da movimentação';
COMMENT ON COLUMN movimentacoes_financeiras.forma_pagamento IS 'Forma de pagamento utilizada';
COMMENT ON COLUMN movimentacoes_financeiras.conta_caixa IS 'Conta ou caixa relacionado';
COMMENT ON COLUMN movimentacoes_financeiras.documento_referencia IS 'Documento de referência (NF, recibo, etc.)';
COMMENT ON COLUMN movimentacoes_financeiras.responsavel IS 'Responsável pela movimentação';
COMMENT ON COLUMN movimentacoes_financeiras.status IS 'Status atual da movimentação';
