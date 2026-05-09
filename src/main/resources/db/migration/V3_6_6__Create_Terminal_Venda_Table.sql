-- =====================================================
-- Hermes Comercial v3.6.6 - Migration
-- Criar tabela de Terminal de Venda
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Terminal de Venda
CREATE TABLE IF NOT EXISTS terminal_venda (
    id SERIAL PRIMARY KEY,
    numero_venda VARCHAR(50) UNIQUE NOT NULL,
    data_hora_venda TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INTEGER REFERENCES clientes(id),
    nome_cliente VARCHAR(100),
    id_operador INTEGER REFERENCES usuarios(id),
    nome_operador VARCHAR(100) NOT NULL,
    terminal VARCHAR(50) NOT NULL, -- Terminal 01, Terminal 02, etc.
    status_venda VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO', -- EM_ANDAMENTO, CONCLUIDA, CANCELADA, SUSPENSA
    forma_pagamento VARCHAR(30), -- DINHEIRO, CARTAO_DEBITO, CARTAO_CREDITO, PIX, VALE_ALIMENTACAO, VALE_REFEICAO, CREDIARIO
    
    -- Totais da Venda
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (subtotal >= 0),
    desconto DECIMAL(15,2) DEFAULT 0.00 CHECK (desconto >= 0),
    total_geral DECIMAL(15,2) NOT NULL CHECK (total_geral >= 0),
    valor_recebido DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_recebido >= 0),
    valor_troco DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_troco >= 0),
    
    -- Campos Adicionais
    observacoes TEXT,
    numero_mesa INTEGER, -- Para restaurantes
    garcom VARCHAR(100), -- Para restaurantes
    delivery BOOLEAN DEFAULT FALSE,
    endereco_entrega TEXT,
    taxa_entrega DECIMAL(15,2) DEFAULT 0.00 CHECK (taxa_entrega >= 0),
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_venda CHECK (status_venda IN ('EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA', 'SUSPENSA')),
    CONSTRAINT chk_forma_pagamento_terminal CHECK (forma_pagamento IN ('DINHEIRO', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'PIX', 'VALE_ALIMENTACAO', 'VALE_REFEICAO', 'CREDIARIO')),
    CONSTRAINT chk_valores_nao_negativos_terminal CHECK (subtotal >= 0 AND total_geral >= 0 AND valor_recebido >= 0 AND valor_troco >= 0),
    CONSTRAINT uq_numero_venda_terminal UNIQUE (numero_venda)
);

-- Criar tabela de Itens do Terminal de Venda
CREATE TABLE IF NOT EXISTS itens_terminal_venda (
    id SERIAL PRIMARY KEY,
    id_terminal_venda INTEGER REFERENCES terminal_venda(id) ON DELETE CASCADE,
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    descricao_produto VARCHAR(200) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL DEFAULT 'UN',
    quantidade DECIMAL(10,3) NOT NULL CHECK (quantidade > 0),
    valor_unitario DECIMAL(15,2) NOT NULL CHECK (valor_unitario >= 0),
    valor_total DECIMAL(15,2) NOT NULL CHECK (valor_total >= 0),
    
    -- Campos Adicionais
    desconto_item DECIMAL(15,2) DEFAULT 0.00 CHECK (desconto_item >= 0),
    acrescimo_item DECIMAL(15,2) DEFAULT 0.00 CHECK (acrescimo_item >= 0),
    valor_final_item DECIMAL(15,2) NOT NULL CHECK (valor_final_item >= 0),
    observacoes_item TEXT,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_quantidade_positiva_terminal CHECK (quantidade > 0),
    CONSTRAINT chk_valores_item_terminal_nao_negativos CHECK (valor_unitario >= 0 AND valor_total >= 0 AND valor_final_item >= 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_terminal_venda_numero ON terminal_venda(numero_venda);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_data_hora ON terminal_venda(data_hora_venda);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_cliente ON terminal_venda(id_cliente);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_operador ON terminal_venda(id_operador);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_status ON terminal_venda(status_venda);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_terminal ON terminal_venda(terminal);
CREATE INDEX IF NOT EXISTS idx_terminal_venda_forma_pagamento ON terminal_venda(forma_pagamento);

CREATE INDEX IF NOT EXISTS idx_itens_terminal_venda_venda ON itens_terminal_venda(id_terminal_venda);
CREATE INDEX IF NOT EXISTS idx_itens_terminal_venda_produto ON itens_terminal_venda(id_produto);
CREATE INDEX IF NOT EXISTS idx_itens_terminal_venda_codigo ON itens_terminal_venda(codigo_produto);

-- Comentários nas tabelas
COMMENT ON TABLE terminal_venda IS 'Tabela de vendas realizadas no terminal';
COMMENT ON COLUMN terminal_venda.id IS 'ID único da venda no terminal';
COMMENT ON COLUMN terminal_venda.numero_venda IS 'Número sequencial da venda no terminal';
COMMENT ON COLUMN terminal_venda.data_hora_venda IS 'Data e hora da venda';
COMMENT ON COLUMN terminal_venda.id_cliente IS 'ID do cliente (opcional)';
COMMENT ON COLUMN terminal_venda.nome_cliente IS 'Nome do cliente';
COMMENT ON COLUMN terminal_venda.id_operador IS 'ID do operador que realizou a venda';
COMMENT ON COLUMN terminal_venda.nome_operador IS 'Nome do operador que realizou a venda';
COMMENT ON COLUMN terminal_venda.terminal IS 'Terminal onde foi realizada a venda';
COMMENT ON COLUMN terminal_venda.status_venda IS 'Status atual da venda';
COMMENT ON COLUMN terminal_venda.forma_pagamento IS 'Forma de pagamento utilizada';
COMMENT ON COLUMN terminal_venda.subtotal IS 'Valor subtotal dos itens';
COMMENT ON COLUMN terminal_venda.desconto IS 'Valor total de desconto';
COMMENT ON COLUMN terminal_venda.total_geral IS 'Valor total da venda';
COMMENT ON COLUMN terminal_venda.valor_recebido IS 'Valor recebido do cliente';
COMMENT ON COLUMN terminal_venda.valor_troco IS 'Valor de troco calculado';
COMMENT ON COLUMN terminal_venda.observacoes IS 'Observações da venda';
COMMENT ON COLUMN terminal_venda.numero_mesa IS 'Número da mesa (para restaurantes)';
COMMENT ON COLUMN terminal_venda.garcom IS 'Nome do garçom (para restaurantes)';
COMMENT ON COLUMN terminal_venda.delivery IS 'Indica se é delivery';
COMMENT ON COLUMN terminal_venda.endereco_entrega IS 'Endereço de entrega';
COMMENT ON COLUMN terminal_venda.taxa_entrega IS 'Taxa de entrega';

COMMENT ON TABLE itens_terminal_venda IS 'Tabela de itens das vendas do terminal';
COMMENT ON COLUMN itens_terminal_venda.id IS 'ID único do item';
COMMENT ON COLUMN itens_terminal_venda.id_terminal_venda IS 'ID da venda principal';
COMMENT ON COLUMN itens_terminal_venda.id_produto IS 'ID do produto';
COMMENT ON COLUMN itens_terminal_venda.codigo_produto IS 'Código do produto';
COMMENT ON COLUMN itens_terminal_venda.descricao_produto IS 'Descrição do produto';
COMMENT ON COLUMN itens_terminal_venda.unidade_medida IS 'Unidade de medida';
COMMENT ON COLUMN itens_terminal_venda.quantidade IS 'Quantidade do item';
COMMENT ON COLUMN itens_terminal_venda.valor_unitario IS 'Valor unitário do item';
COMMENT ON COLUMN itens_terminal_venda.valor_total IS 'Valor total do item';
COMMENT ON COLUMN itens_terminal_venda.desconto_item IS 'Desconto aplicado no item';
COMMENT ON COLUMN itens_terminal_venda.acrescimo_item IS 'Acréscimo aplicado no item';
COMMENT ON COLUMN itens_terminal_venda.valor_final_item IS 'Valor final do item com desconto/acréscimo';
COMMENT ON COLUMN itens_terminal_venda.observacoes_item IS 'Observações do item';
