-- =====================================================
-- Hermes Comercial v3.6.3 - Migration
-- Criar tabela de Carteira Fidelidade
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Carteira Fidelidade
CREATE TABLE IF NOT EXISTS carteira_fidelidade (
    id SERIAL PRIMARY KEY,
    id_cliente INTEGER REFERENCES clientes(id),
    numero_cartao VARCHAR(20) UNIQUE NOT NULL,
    data_emissao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_validade TIMESTAMP NOT NULL,
    pontos_acumulados INTEGER NOT NULL DEFAULT 0,
    pontos_resgatados INTEGER NOT NULL DEFAULT 0,
    pontos_saldo INTEGER NOT NULL DEFAULT 0,
    nivel_cartao VARCHAR(20) NOT NULL, -- BRONZE, PRATA, OURO, PLATINA, DIAMANTE
    status_cartao VARCHAR(20) NOT NULL DEFAULT 'ATIVO', -- ATIVO, BLOQUEADO, CANCELADO, EXPIRADO
    ultimo_acesso TIMESTAMP,
    total_compras DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    desconto_acumulado DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    cashback_acumulado DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_nivel_cartao CHECK (nivel_cartao IN ('BRONZE', 'PRATA', 'OURO', 'PLATINA', 'DIAMANTE')),
    CONSTRAINT chk_status_cartao CHECK (status_cartao IN ('ATIVO', 'BLOQUEADO', 'CANCELADO', 'EXPIRADO')),
    CONSTRAINT chk_pontos_nao_negativos CHECK (pontos_acumulados >= 0 AND pontos_resgatados >= 0 AND pontos_saldo >= 0),
    CONSTRAINT chk_valores_nao_negativos CHECK (total_compras >= 0 AND desconto_acumulado >= 0 AND cashback_acumulado >= 0),
    CONSTRAINT uq_numero_cartao UNIQUE (numero_cartao)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_carteira_cliente ON carteira_fidelidade(id_cliente);
CREATE INDEX IF NOT EXISTS idx_carteira_numero_cartao ON carteira_fidelidade(numero_cartao);
CREATE INDEX IF NOT EXISTS idx_carteira_nivel ON carteira_fidelidade(nivel_cartao);
CREATE INDEX IF NOT EXISTS idx_carteira_status ON carteira_fidelidade(status_cartao);
CREATE INDEX IF NOT EXISTS idx_carteira_data_emissao ON carteira_fidelidade(data_emissao);
CREATE INDEX IF NOT EXISTS idx_carteira_data_validade ON carteira_fidelidade(data_validade);
CREATE INDEX IF NOT EXISTS idx_carteira_pontos_saldo ON carteira_fidelidade(pontos_saldo);

-- Comentários na tabela
COMMENT ON TABLE carteira_fidelidade IS 'Tabela de carteira de fidelidade de clientes';
COMMENT ON COLUMN carteira_fidelidade.id IS 'ID único do registro da carteira';
COMMENT ON COLUMN carteira_fidelidade.id_cliente IS 'ID do cliente titular da carteira';
COMMENT ON COLUMN carteira_fidelidade.numero_cartao IS 'Número único do cartão fidelidade';
COMMENT ON COLUMN carteira_fidelidade.data_emissao IS 'Data de emissão do cartão';
COMMENT ON COLUMN carteira_fidelidade.data_validade IS 'Data de validade do cartão';
COMMENT ON COLUMN carteira_fidelidade.pontos_acumulados IS 'Total de pontos acumulados pelo cliente';
COMMENT ON COLUMN carteira_fidelidade.pontos_resgatados IS 'Total de pontos já resgatados pelo cliente';
COMMENT ON COLUMN carteira_fidelidade.pontos_saldo IS 'Saldo atual de pontos disponíveis';
COMMENT ON COLUMN carteira_fidelidade.nivel_cartao IS 'Nível do cartão de fidelidade';
COMMENT ON COLUMN carteira_fidelidade.status_cartao IS 'Status atual do cartão';
COMMENT ON COLUMN carteira_fidelidade.ultimo_acesso IS 'Data e hora do último acesso ao cartão';
COMMENT ON COLUMN carteira_fidelidade.total_compras IS 'Valor total acumulado em compras';
COMMENT ON COLUMN carteira_fidelidade.desconto_acumulado IS 'Valor total acumulado em descontos';
COMMENT ON COLUMN carteira_fidelidade.cashback_acumulado IS 'Valor total acumulado em cashback';
