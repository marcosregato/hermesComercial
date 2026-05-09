-- =====================================================
-- Hermes Comercial v3.6.7 - Migration
-- Criar tabela de Venda Rápida
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Venda Rápida
CREATE TABLE IF NOT EXISTS venda_rapida (
    id SERIAL PRIMARY KEY,
    numero_venda VARCHAR(50) UNIQUE NOT NULL,
    data_hora_venda TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INTEGER REFERENCES clientes(id),
    nome_cliente VARCHAR(100),
    id_operador INTEGER REFERENCES usuarios(id),
    nome_operador VARCHAR(100) NOT NULL,
    terminal VARCHAR(50) NOT NULL, -- Terminal 01, Terminal 02, etc.
    status_venda VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO', -- EM_ANDAMENTO, CONCLUIDA, CANCELADA, PAUSADA
    forma_pagamento VARCHAR(30), -- DINHEIRO, CARTAO_DEBITO, CARTAO_CREDITO, PIX, VALE_ALIMENTACAO, VALE_REFEICAO, CREDIARIO
    
    -- Totais da Venda Rápida
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (subtotal >= 0),
    desconto DECIMAL(15,2) DEFAULT 0.00 CHECK (desconto >= 0),
    total_geral DECIMAL(15,2) NOT NULL CHECK (total_geral >= 0),
    valor_recebido DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_recebido >= 0),
    valor_troco DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_troco >= 0),
    
    -- Campos Adicionais para Venda Rápida
    tipo_venda VARCHAR(20) DEFAULT 'RAPIDA', -- RAPIDA, BALCAO, DELIVERY, SELF_SERVICE
    numero_mesa INTEGER, -- Para restaurantes
    garcom VARCHAR(100), -- Para restaurantes
    delivery BOOLEAN DEFAULT FALSE,
    endereco_entrega TEXT,
    taxa_entrega DECIMAL(15,2) DEFAULT 0.00 CHECK (taxa_entrega >= 0),
    tempo_preparo INTEGER, -- Tempo médio de preparo em minutos
    prioridade VARCHAR(20) DEFAULT 'NORMAL', -- BAIXA, NORMAL, ALTA, URGENTE
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_venda_rapida CHECK (status_venda IN ('EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA', 'PAUSADA')),
    CONSTRAINT chk_forma_pagamento_venda_rapida CHECK (forma_pagamento IN ('DINHEIRO', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'PIX', 'VALE_ALIMENTACAO', 'VALE_REFEICAO', 'CREDIARIO')),
    CONSTRAINT chk_tipo_venda_rapida CHECK (tipo_venda IN ('RAPIDA', 'BALCAO', 'DELIVERY', 'SELF_SERVICE')),
    CONSTRAINT chk_prioridade_venda_rapida CHECK (prioridade IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE')),
    CONSTRAINT chk_valores_nao_negativos_venda_rapida CHECK (subtotal >= 0 AND total_geral >= 0 AND valor_recebido >= 0 AND valor_troco >= 0),
    CONSTRAINT uq_numero_venda_rapida UNIQUE (numero_venda)
);

-- Criar tabela de Itens da Venda Rápida
CREATE TABLE IF NOT EXISTS itens_venda_rapida (
    id SERIAL PRIMARY KEY,
    id_venda_rapida INTEGER REFERENCES venda_rapida(id) ON DELETE CASCADE,
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    descricao_produto VARCHAR(200) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL DEFAULT 'UN',
    quantidade DECIMAL(10,3) NOT NULL CHECK (quantidade > 0),
    valor_unitario DECIMAL(15,2) NOT NULL CHECK (valor_unitario >= 0),
    valor_total DECIMAL(15,2) NOT NULL CHECK (valor_total >= 0),
    
    -- Campos Adicionais para Venda Rápida
    desconto_item DECIMAL(15,2) DEFAULT 0.00 CHECK (desconto_item >= 0),
    acrescimo_item DECIMAL(15,2) DEFAULT 0.00 CHECK (acrescimo_item >= 0),
    valor_final_item DECIMAL(15,2) NOT NULL CHECK (valor_final_item >= 0),
    observacoes_item TEXT,
    categoria_produto VARCHAR(50), -- Categoria para organização rápida
    tempo_preparo_item INTEGER, -- Tempo de preparo individual do item
    personalizacao TEXT, -- Personalizações do item (ex: sem cebola, extra bacon)
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_quantidade_positiva_venda_rapida CHECK (quantidade > 0),
    CONSTRAINT chk_valores_item_venda_rapida_nao_negativos CHECK (valor_unitario >= 0 AND valor_total >= 0 AND valor_final_item >= 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_venda_rapida_numero ON venda_rapida(numero_venda);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_data_hora ON venda_rapida(data_hora_venda);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_cliente ON venda_rapida(id_cliente);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_operador ON venda_rapida(id_operador);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_status ON venda_rapida(status_venda);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_terminal ON venda_rapida(terminal);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_forma_pagamento ON venda_rapida(forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_tipo_venda ON venda_rapida(tipo_venda);
CREATE INDEX IF NOT EXISTS idx_venda_rapida_prioridade ON venda_rapida(prioridade);

CREATE INDEX IF NOT EXISTS idx_itens_venda_rapida_venda ON itens_venda_rapida(id_venda_rapida);
CREATE INDEX IF NOT EXISTS idx_itens_venda_rapida_produto ON itens_venda_rapida(id_produto);
CREATE INDEX IF NOT EXISTS idx_itens_venda_rapida_codigo ON itens_venda_rapida(codigo_produto);
CREATE INDEX IF NOT EXISTS idx_itens_venda_rapida_categoria ON itens_venda_rapida(categoria_produto);

-- Comentários nas tabelas
COMMENT ON TABLE venda_rapida IS 'Tabela de vendas rápidas realizadas no sistema';
COMMENT ON COLUMN venda_rapida.id IS 'ID único da venda rápida';
COMMENT ON COLUMN venda_rapida.numero_venda IS 'Número sequencial da venda rápida';
COMMENT ON COLUMN venda_rapida.data_hora_venda IS 'Data e hora da venda rápida';
COMMENT ON COLUMN venda_rapida.id_cliente IS 'ID do cliente (opcional)';
COMMENT ON COLUMN venda_rapida.nome_cliente IS 'Nome do cliente';
COMMENT ON COLUMN venda_rapida.id_operador IS 'ID do operador que realizou a venda';
COMMENT ON COLUMN venda_rapida.nome_operador IS 'Nome do operador que realizou a venda';
COMMENT ON COLUMN venda_rapida.terminal IS 'Terminal onde foi realizada a venda';
COMMENT ON COLUMN venda_rapida.status_venda IS 'Status atual da venda';
COMMENT ON COLUMN venda_rapida.forma_pagamento IS 'Forma de pagamento utilizada';
COMMENT ON COLUMN venda_rapida.subtotal IS 'Valor subtotal dos itens';
COMMENT ON COLUMN venda_rapida.desconto IS 'Valor total de desconto';
COMMENT ON COLUMN venda_rapida.total_geral IS 'Valor total da venda';
COMMENT ON COLUMN venda_rapida.valor_recebido IS 'Valor recebido do cliente';
COMMENT ON COLUMN venda_rapida.valor_troco IS 'Valor de troco calculado';
COMMENT ON COLUMN venda_rapida.tipo_venda IS 'Tipo de venda rápida';
COMMENT ON COLUMN venda_rapida.numero_mesa IS 'Número da mesa (para restaurantes)';
COMMENT ON COLUMN venda_rapida.garcom IS 'Nome do garçom (para restaurantes)';
COMMENT ON COLUMN venda_rapida.delivery IS 'Indica se é delivery';
COMMENT ON COLUMN venda_rapida.endereco_entrega IS 'Endereço de entrega';
COMMENT ON COLUMN venda_rapida.taxa_entrega IS 'Taxa de entrega';
COMMENT ON COLUMN venda_rapida.tempo_preparo IS 'Tempo médio de preparo em minutos';
COMMENT ON COLUMN venda_rapida.prioridade IS 'Prioridade da venda';

COMMENT ON TABLE itens_venda_rapida IS 'Tabela de itens das vendas rápidas';
COMMENT ON COLUMN itens_venda_rapida.id IS 'ID único do item';
COMMENT ON COLUMN itens_venda_rapida.id_venda_rapida IS 'ID da venda rápida principal';
COMMENT ON COLUMN itens_venda_rapida.id_produto IS 'ID do produto';
COMMENT ON COLUMN itens_venda_rapida.codigo_produto IS 'Código do produto';
COMMENT ON COLUMN itens_venda_rapida.descricao_produto IS 'Descrição do produto';
COMMENT ON COLUMN itens_venda_rapida.unidade_medida IS 'Unidade de medida';
COMMENT ON COLUMN itens_venda_rapida.quantidade IS 'Quantidade do item';
COMMENT ON COLUMN itens_venda_rapida.valor_unitario IS 'Valor unitário do item';
COMMENT ON COLUMN itens_venda_rapida.valor_total IS 'Valor total do item';
COMMENT ON COLUMN itens_venda_rapida.desconto_item IS 'Desconto aplicado no item';
COMMENT ON COLUMN itens_venda_rapida.acrescimo_item IS 'Acréscimo aplicado no item';
COMMENT ON COLUMN itens_venda_rapida.valor_final_item IS 'Valor final do item com desconto/acréscimo';
COMMENT ON COLUMN itens_venda_rapida.observacoes_item IS 'Observações do item';
COMMENT ON COLUMN itens_venda_rapida.categoria_produto IS 'Categoria do produto para organização';
COMMENT ON COLUMN itens_venda_rapida.tempo_preparo_item IS 'Tempo de preparo individual do item';
COMMENT ON COLUMN itens_venda_rapida.personalizacao IS 'Personalizações do item';
