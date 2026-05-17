-- =====================================================
-- Hermes Comercial v3.6.8 - Migration
-- Criar tabela de Estoque Mínimo
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Estoque Mínimo
CREATE TABLE IF NOT EXISTS estoque_minimo (
    id SERIAL PRIMARY KEY,
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    nome_produto VARCHAR(200) NOT NULL,
    categoria_produto VARCHAR(100),
    unidade_medida VARCHAR(10) DEFAULT 'UN',
    
    -- Controle de Estoque
    estoque_atual DECIMAL(15,3) NOT NULL DEFAULT 0.00 CHECK (estoque_atual >= 0),
    estoque_minimo DECIMAL(15,3) NOT NULL DEFAULT 0.00 CHECK (estoque_minimo >= 0),
    estoque_maximo DECIMAL(15,3) DEFAULT 0.00 CHECK (estoque_maximo >= 0),
    ponto_reposicao DECIMAL(15,3) DEFAULT 0.00 CHECK (ponto_reposicao >= 0),
    
    -- Valores Financeiros
    preco_custo DECIMAL(15,2) DEFAULT 0.00 CHECK (preco_custo >= 0),
    preco_venda DECIMAL(15,2) DEFAULT 0.00 CHECK (preco_venda >= 0),
    valor_total_estoque DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_total_estoque >= 0),
    
    -- Controle de Fornecedor
    id_fornecedor INTEGER REFERENCES fornecedores(id),
    nome_fornecedor VARCHAR(100),
    codigo_fornecedor VARCHAR(50),
    
    -- Status e Controle
    status_produto VARCHAR(20) NOT NULL DEFAULT 'ATIVO', -- ATIVO, INATIVO, DESCONTINUADO, EM_FALTA
    status_estoque VARCHAR(20) NOT NULL DEFAULT 'NORMAL', -- NORMAL, BAIXO, CRITICO, ZERADO, EXCEDENTE
    prioridade_reposicao VARCHAR(20) DEFAULT 'NORMAL', -- BAIXA, NORMAL, ALTA, URGENTE
    
    -- Controle de Tempo
    ultima_compra DATE,
    ultima_venda DATE,
    ultima_reposicao DATE,
    tempo_medio_reposicao INTEGER, -- Tempo médio em dias
    
    -- Localização
    localizacao VARCHAR(100),
    deposito VARCHAR(50) DEFAULT 'Principal',
    prateleira VARCHAR(50),
    
    -- Campos Adicionais
    margem_lucro DECIMAL(5,2) DEFAULT 0.00 CHECK (margem_lucro >= 0),
    markup DECIMAL(5,2) DEFAULT 0.00 CHECK (markup >= 0),
    giro_estoque DECIMAL(10,2) DEFAULT 0.00 CHECK (giro_estoque >= 0),
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_produto_estoque_minimo CHECK (status_produto IN ('ATIVO', 'INATIVO', 'DESCONTINUADO', 'EM_FALTA')),
    CONSTRAINT chk_status_estoque_estoque_minimo CHECK (status_estoque IN ('NORMAL', 'BAIXO', 'CRITICO', 'ZERADO', 'EXCEDENTE')),
    CONSTRAINT chk_prioridade_reposicao_estoque_minimo CHECK (prioridade_reposicao IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE')),
    CONSTRAINT chk_valores_nao_negativos_estoque_minimo CHECK (estoque_atual >= 0 AND estoque_minimo >= 0 AND estoque_maximo >= 0 AND ponto_reposicao >= 0),
    CONSTRAINT chk_valores_financeiros_nao_negativos_estoque_minimo CHECK (preco_custo >= 0 AND preco_venda >= 0 AND valor_total_estoque >= 0),
    CONSTRAINT uq_codigo_produto_estoque_minimo UNIQUE (codigo_produto)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_produto ON estoque_minimo(id_produto);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_codigo ON estoque_minimo(codigo_produto);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_categoria ON estoque_minimo(categoria_produto);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_status_produto ON estoque_minimo(status_produto);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_status_estoque ON estoque_minimo(status_estoque);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_prioridade ON estoque_minimo(prioridade_reposicao);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_fornecedor ON estoque_minimo(id_fornecedor);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_deposito ON estoque_minimo(deposito);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_localizacao ON estoque_minimo(localizacao);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_ultima_compra ON estoque_minimo(ultima_compra);
CREATE INDEX IF NOT EXISTS idx_estoque_minimo_ultima_venda ON estoque_minimo(ultima_venda);

-- Criar tabela de Histórico de Movimentação de Estoque Mínimo
CREATE TABLE IF NOT EXISTS historico_estoque_minimo (
    id SERIAL PRIMARY KEY,
    id_estoque_minimo INTEGER REFERENCES estoque_minimo(id),
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    nome_produto VARCHAR(200) NOT NULL,
    
    -- Tipo de Movimentação
    tipo_movimentacao VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA, AJUSTE, PERDA, TRANSFERENCIA
    
    -- Valores da Movimentação
    quantidade_anterior DECIMAL(15,3) NOT NULL,
    quantidade_movimentada DECIMAL(15,3) NOT NULL,
    quantidade_atual DECIMAL(15,3) NOT NULL,
    
    -- Detalhes
    motivo_movimentacao VARCHAR(200),
    documento_referencia VARCHAR(50),
    tipo_documento VARCHAR(20), -- NOTA_FISCAL, NOTA_DEVOLUCAO, ORDEM_COMPRA, AJUSTE_MANUAL
    
    -- Controle de Localização
    origem_localizacao VARCHAR(100),
    destino_localizacao VARCHAR(100),
    origem_deposito VARCHAR(50),
    destino_deposito VARCHAR(50),
    
    -- Valores Financeiros
    valor_unitario DECIMAL(15,2) DEFAULT 0.00,
    valor_total DECIMAL(15,2) DEFAULT 0.00,
    
    -- Auditoria
    usuario_movimentacao VARCHAR(50) NOT NULL,
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_movimentacao_estoque_minimo CHECK (tipo_movimentacao IN ('ENTRADA', 'SAIDA', 'AJUSTE', 'PERDA', 'TRANSFERENCIA')),
    CONSTRAINT chk_tipo_documento_estoque_minimo CHECK (tipo_documento IN ('NOTA_FISCAL', 'NOTA_DEVOLUCAO', 'ORDEM_COMPRA', 'AJUSTE_MANUAL'))
);

-- Índices para histórico
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_estoque ON historico_estoque_minimo(id_estoque_minimo);
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_produto ON historico_estoque_minimo(id_produto);
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_codigo ON historico_estoque_minimo(codigo_produto);
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_tipo ON historico_estoque_minimo(tipo_movimentacao);
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_data ON historico_estoque_minimo(data_movimentacao);
CREATE INDEX IF NOT EXISTS idx_historico_estoque_minimo_usuario ON historico_estoque_minimo(usuario_movimentacao);

-- Criar tabela de Alertas de Estoque Mínimo
CREATE TABLE IF NOT EXISTS alertas_estoque_minimo (
    id SERIAL PRIMARY KEY,
    id_estoque_minimo INTEGER REFERENCES estoque_minimo(id),
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    nome_produto VARCHAR(200) NOT NULL,
    
    -- Tipo de Alerta
    tipo_alerta VARCHAR(20) NOT NULL, -- ESTOQUE_BAIXO, ESTOQUE_ZERADO, ESTOQUE_EXCEDENTE, PRODUTO_VENCIDO
    
    -- Detalhes do Alerta
    mensagem_alerta TEXT,
    nivel_prioridade VARCHAR(20) DEFAULT 'MEDIA', -- BAIXA, MEDIA, ALTA, CRITICA
    
    -- Controle
    alerta_ativo BOOLEAN DEFAULT TRUE,
    data_alerta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_notificacao TIMESTAMP,
    quantidade_notificacoes INTEGER DEFAULT 0,
    
    -- Resolução
    alerta_resolvido BOOLEAN DEFAULT FALSE,
    data_resolucao TIMESTAMP,
    usuario_resolucao VARCHAR(50),
    observacao_resolucao TEXT,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_alerta_estoque_minimo CHECK (tipo_alerta IN ('ESTOQUE_BAIXO', 'ESTOQUE_ZERADO', 'ESTOQUE_EXCEDENTE', 'PRODUTO_VENCIDO')),
    CONSTRAINT chk_nivel_prioridade_estoque_minimo CHECK (nivel_prioridade IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA'))
);

-- Índices para alertas
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_estoque ON alertas_estoque_minimo(id_estoque_minimo);
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_produto ON alertas_estoque_minimo(id_produto);
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_tipo ON alertas_estoque_minimo(tipo_alerta);
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_prioridade ON alertas_estoque_minimo(nivel_prioridade);
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_ativo ON alertas_estoque_minimo(alerta_ativo);
CREATE INDEX IF NOT EXISTS idx_alertas_estoque_minimo_data ON alertas_estoque_minimo(data_alerta);

-- Comentários nas tabelas
COMMENT ON TABLE estoque_minimo IS 'Tabela de controle de estoque mínimo dos produtos';
COMMENT ON COLUMN estoque_minimo.id IS 'ID único do registro';
COMMENT ON COLUMN estoque_minimo.id_produto IS 'ID do produto relacionado';
COMMENT ON COLUMN estoque_minimo.codigo_produto IS 'Código do produto';
COMMENT ON COLUMN estoque_minimo.nome_produto IS 'Nome do produto';
COMMENT ON COLUMN estoque_minimo.categoria_produto IS 'Categoria do produto';
COMMENT ON COLUMN estoque_minimo.unidade_medida IS 'Unidade de medida do produto';
COMMENT ON COLUMN estoque_minimo.estoque_atual IS 'Quantidade atual em estoque';
COMMENT ON COLUMN estoque_minimo.estoque_minimo IS 'Quantidade mínima de estoque';
COMMENT ON COLUMN estoque_minimo.estoque_maximo IS 'Quantidade máxima de estoque';
COMMENT ON COLUMN estoque_minimo.ponto_reposicao IS 'Ponto ideal para reposição';
COMMENT ON COLUMN estoque_minimo.preco_custo IS 'Preço de custo do produto';
COMMENT ON COLUMN estoque_minimo.preco_venda IS 'Preço de venda do produto';
COMMENT ON COLUMN estoque_minimo.valor_total_estoque IS 'Valor total do estoque atual';
COMMENT ON COLUMN estoque_minimo.id_fornecedor IS 'ID do fornecedor principal';
COMMENT ON COLUMN estoque_minimo.nome_fornecedor IS 'Nome do fornecedor principal';
COMMENT ON COLUMN estoque_minimo.codigo_fornecedor IS 'Código do fornecedor';
COMMENT ON COLUMN estoque_minimo.status_produto IS 'Status do produto';
COMMENT ON COLUMN estoque_minimo.status_estoque IS 'Status do estoque';
COMMENT ON COLUMN estoque_minimo.prioridade_reposicao IS 'Prioridade para reposição';
COMMENT ON COLUMN estoque_minimo.ultima_compra IS 'Data da última compra';
COMMENT ON COLUMN estoque_minimo.ultima_venda IS 'Data da última venda';
COMMENT ON COLUMN estoque_minimo.ultima_reposicao IS 'Data da última reposição';
COMMENT ON COLUMN estoque_minimo.tempo_medio_reposicao IS 'Tempo médio para reposição em dias';
COMMENT ON COLUMN estoque_minimo.localizacao IS 'Localização física do produto';
COMMENT ON COLUMN estoque_minimo.deposito IS 'Depósito onde o produto está armazenado';
COMMENT ON COLUMN estoque_minimo.prateleira IS 'Prateleira específica';
COMMENT ON COLUMN estoque_minimo.margem_lucro IS 'Margem de lucro percentual';
COMMENT ON COLUMN estoque_minimo.markup IS 'Markup percentual';
COMMENT ON COLUMN estoque_minimo.giro_estoque IS 'Giro de estoque';

COMMENT ON TABLE historico_estoque_minimo IS 'Histórico de movimentações de estoque';
COMMENT ON COLUMN historico_estoque_minimo.id IS 'ID único da movimentação';
COMMENT ON COLUMN historico_estoque_minimo.id_estoque_minimo IS 'ID do registro de estoque mínimo';
COMMENT ON COLUMN historico_estoque_minimo.tipo_movimentacao IS 'Tipo da movimentação';
COMMENT ON COLUMN historico_estoque_minimo.quantidade_anterior IS 'Quantidade antes da movimentação';
COMMENT ON COLUMN historico_estoque_minimo.quantidade_movimentada IS 'Quantidade movimentada';
COMMENT ON COLUMN historico_estoque_minimo.quantidade_atual IS 'Quantidade após movimentação';
COMMENT ON COLUMN historico_estoque_minimo.motivo_movimentacao IS 'Motivo da movimentação';
COMMENT ON COLUMN historico_estoque_minimo.documento_referencia IS 'Documento de referência';
COMMENT ON COLUMN historico_estoque_minimo.tipo_documento IS 'Tipo do documento';
COMMENT ON COLUMN historico_estoque_minimo.usuario_movimentacao IS 'Usuário que realizou a movimentação';
COMMENT ON COLUMN historico_estoque_minimo.data_movimentacao IS 'Data e hora da movimentação';

COMMENT ON TABLE alertas_estoque_minimo IS 'Alertas de estoque mínimo';
COMMENT ON COLUMN alertas_estoque_minimo.id IS 'ID único do alerta';
COMMENT ON COLUMN alertas_estoque_minimo.tipo_alerta IS 'Tipo do alerta';
COMMENT ON COLUMN alertas_estoque_minimo.mensagem_alerta IS 'Mensagem do alerta';
COMMENT ON COLUMN alertas_estoque_minimo.nivel_prioridade IS 'Nível de prioridade';
COMMENT ON COLUMN alertas_estoque_minimo.alerta_ativo IS 'Alerta está ativo';
COMMENT ON COLUMN alertas_estoque_minimo.data_alerta IS 'Data de criação do alerta';
COMMENT ON COLUMN alertas_estoque_minimo.alerta_resolvido IS 'Alerta foi resolvido';
COMMENT ON COLUMN alertas_estoque_minimo.data_resolucao IS 'Data da resolução';
COMMENT ON COLUMN alertas_estoque_minimo.usuario_resolucao IS 'Usuário que resolveu o alerta';
