-- =====================================================
-- Hermes Comercial v3.6.2 - Migration
-- Criar tabela de Transferências de Estoque
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Transferências de Estoque
CREATE TABLE IF NOT EXISTS transferencias_estoque (
    id SERIAL PRIMARY KEY,
    id_produto INTEGER REFERENCES produtos(id),
    data_transferencia TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    
    -- Origem da Transferência
    id_loja_origem INTEGER REFERENCES lojas(id),
    id_deposito_origem INTEGER REFERENCES depositos(id),
    estoque_anterior_origem INTEGER NOT NULL,
    estoque_apos_origem INTEGER NOT NULL,
    
    -- Destino da Transferência
    id_loja_destino INTEGER REFERENCES lojas(id),
    id_deposito_destino INTEGER REFERENCES depositos(id),
    estoque_anterior_destino INTEGER NOT NULL,
    estoque_apos_destino INTEGER NOT NULL,
    
    -- Documentação e Controle
    documento_referencia VARCHAR(100), -- Nota de transferência, ordem interna, etc.
    motivo_transferencia TEXT NOT NULL,
    tipo_transferencia VARCHAR(20) NOT NULL DEFAULT 'ENTRE_LOJAS', -- ENTRE_LOJAS, ENTRE_DEPOSITOS, LOJA_DEPOSITO
    
    -- Responsáveis
    usuario_solicitante VARCHAR(100) NOT NULL,
    usuario_aprovador VARCHAR(100),
    usuario_transportador VARCHAR(100),
    usuario_recebedor VARCHAR(100),
    
    -- Status e Controle
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE', -- PENDENTE, APROVADA, EM_TRANSITO, CONCLUIDA, CANCELADA
    data_aprovacao TIMESTAMP,
    data_envio TIMESTAMP,
    data_recebimento TIMESTAMP,
    
    -- Observações
    observacoes TEXT,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_transferencia CHECK (tipo_transferencia IN ('ENTRE_LOJAS', 'ENTRE_DEPOSITOS', 'LOJA_DEPOSITO')),
    CONSTRAINT chk_status_transferencia CHECK (status IN ('PENDENTE', 'APROVADA', 'EM_TRANSITO', 'CONCLUIDA', 'CANCELADA')),
    CONSTRAINT chk_diferentes_origem_destino CHECK (
        (id_loja_origem IS NULL OR id_loja_destino IS NULL OR id_loja_origem != id_loja_destino) AND
        (id_deposito_origem IS NULL OR id_deposito_destino IS NULL OR id_deposito_origem != id_deposito_destino)
    )
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_transferencias_data ON transferencias_estoque(data_transferencia);
CREATE INDEX IF NOT EXISTS idx_transferencias_produto ON transferencias_estoque(id_produto);
CREATE INDEX IF NOT EXISTS idx_transferencias_origem ON transferencias_estoque(id_loja_origem, id_deposito_origem);
CREATE INDEX IF NOT EXISTS idx_transferencias_destino ON transferencias_estoque(id_loja_destino, id_deposito_destino);
CREATE INDEX IF NOT EXISTS idx_transferencias_status ON transferencias_estoque(status);
CREATE INDEX IF NOT EXISTS idx_transferencias_solicitante ON transferencias_estoque(usuario_solicitante);
CREATE INDEX IF NOT EXISTS idx_transferencias_documento ON transferencias_estoque(documento_referencia);

-- Comentários na tabela
COMMENT ON TABLE transferencias_estoque IS 'Tabela de transferências de estoque entre lojas e depósitos';
COMMENT ON COLUMN transferencias_estoque.id IS 'ID único da transferência';
COMMENT ON COLUMN transferencias_estoque.id_produto IS 'Produto sendo transferido';
COMMENT ON COLUMN transferencias_estoque.data_transferencia IS 'Data e hora da solicitação da transferência';
COMMENT ON COLUMN transferencias_estoque.quantidade IS 'Quantidade de itens transferidos';
COMMENT ON COLUMN transferencias_estoque.id_loja_origem IS 'Loja de origem da transferência';
COMMENT ON COLUMN transferencias_estoque.id_deposito_origem IS 'Depósito de origem da transferência';
COMMENT ON COLUMN transferencias_estoque.estoque_anterior_origem IS 'Estoque antes da transferência na origem';
COMMENT ON COLUMN transferencias_estoque.estoque_apos_origem IS 'Estoque após a transferência na origem';
COMMENT ON COLUMN transferencias_estoque.id_loja_destino IS 'Loja de destino da transferência';
COMMENT ON COLUMN transferencias_estoque.id_deposito_destino IS 'Depósito de destino da transferência';
COMMENT ON COLUMN transferencias_estoque.estoque_anterior_destino IS 'Estoque antes da transferência no destino';
COMMENT ON COLUMN transferencias_estoque.estoque_apos_destino IS 'Estoque após a transferência no destino';
COMMENT ON COLUMN transferencias_estoque.documento_referencia IS 'Documento de referência da transferência';
COMMENT ON COLUMN transferencias_estoque.motivo_transferencia IS 'Motivo detalhado da transferência';
COMMENT ON COLUMN transferencias_estoque.tipo_transferencia IS 'Tipo de transferência realizada';
COMMENT ON COLUMN transferencias_estoque.usuario_solicitante IS 'Usuário que solicitou a transferência';
COMMENT ON COLUMN transferencias_estoque.usuario_aprovador IS 'Usuário que aprovou a transferência';
COMMENT ON COLUMN transferencias_estoque.usuario_transportador IS 'Usuário responsável pelo transporte';
COMMENT ON COLUMN transferencias_estoque.usuario_recebedor IS 'Usuário que recebeu a transferência';
COMMENT ON COLUMN transferencias_estoque.status IS 'Status atual da transferência';
COMMENT ON COLUMN transferencias_estoque.data_aprovacao IS 'Data da aprovação da transferência';
COMMENT ON COLUMN transferencias_estoque.data_envio IS 'Data de envio da transferência';
COMMENT ON COLUMN transferencias_estoque.data_recebimento IS 'Data de recebimento da transferência';
COMMENT ON COLUMN transferencias_estoque.observacoes IS 'Observações adicionais sobre a transferência';
