-- =====================================================
-- Hermes Comercial v3.6.5 - Migration
-- Criar tabela de Cupons Fiscais
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Cupons Fiscais
CREATE TABLE IF NOT EXISTS cupons_fiscais (
    id SERIAL PRIMARY KEY,
    numero_cupom VARCHAR(50) UNIQUE NOT NULL,
    data_emissao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_cliente INTEGER REFERENCES clientes(id),
    cpf_cnpj_cliente VARCHAR(20),
    nome_cliente VARCHAR(100),
    id_operador INTEGER REFERENCES usuarios(id),
    nome_operador VARCHAR(100) NOT NULL,
    forma_pagamento VARCHAR(30) NOT NULL, -- DINHEIRO, CARTAO_DEBITO, CARTAO_CREDITO, PIX, VALE_ALIMENTACAO, VALE_REFEICAO, CREDIARIO
    status_cupom VARCHAR(20) NOT NULL DEFAULT 'ABERTO', -- ABERTO, EMITIDO, CANCELADO, PAUSADO
    
    -- Totais do Cupom
    subtotal DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (subtotal >= 0),
    desconto DECIMAL(15,2) DEFAULT 0.00 CHECK (desconto >= 0),
    acrescimo DECIMAL(15,2) DEFAULT 0.00 CHECK (acrescimo >= 0),
    total_geral DECIMAL(15,2) NOT NULL CHECK (total_geral >= 0),
    valor_recebido DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_recebido >= 0),
    valor_troco DECIMAL(15,2) DEFAULT 0.00 CHECK (valor_troco >= 0),
    
    -- Campos Fiscais
    ccf VARCHAR(20), -- Código Contador de Cupom Fiscal
    coo VARCHAR(20), -- Código Contador de Operação
    numero_serie_ecf VARCHAR(50), -- Número de Série do ECF
    modelo_ecf VARCHAR(20), -- Modelo da ECF
    versao_software VARCHAR(20), -- Versão do Software
    numero_ordem_operacao VARCHAR(20), -- Número da Ordem de Operação
    
    -- Informações Adicionais
    chave_acesso VARCHAR(44), -- Chave de acesso NFe (se aplicável)
    protocolo_autorizacao VARCHAR(50), -- Protocolo de autorização
    data_hora_autorizacao TIMESTAMP, -- Data e hora da autorização
    codigo_verificacao VARCHAR(20), -- Código de verificação
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_cupom CHECK (status_cupom IN ('ABERTO', 'EMITIDO', 'CANCELADO', 'PAUSADO')),
    CONSTRAINT chk_forma_pagamento CHECK (forma_pagamento IN ('DINHEIRO', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'PIX', 'VALE_ALIMENTACAO', 'VALE_REFEICAO', 'CREDIARIO')),
    CONSTRAINT chk_valores_nao_negativos CHECK (subtotal >= 0 AND total_geral >= 0 AND valor_recebido >= 0 AND valor_troco >= 0),
    CONSTRAINT uq_numero_cupom UNIQUE (numero_cupom)
);

-- Criar tabela de Itens do Cupom Fiscal
CREATE TABLE IF NOT EXISTS itens_cupom_fiscal (
    id SERIAL PRIMARY KEY,
    id_cupom_fiscal INTEGER REFERENCES cupons_fiscais(id) ON DELETE CASCADE,
    id_produto INTEGER REFERENCES produtos(id),
    codigo_produto VARCHAR(50) NOT NULL,
    descricao_produto VARCHAR(200) NOT NULL,
    unidade_medida VARCHAR(10) NOT NULL DEFAULT 'UN',
    quantidade DECIMAL(10,3) NOT NULL CHECK (quantidade > 0),
    valor_unitario DECIMAL(15,2) NOT NULL CHECK (valor_unitario >= 0),
    valor_total DECIMAL(15,2) NOT NULL CHECK (valor_total >= 0),
    
    -- Campos Fiscais dos Itens
    icms_aliquota DECIMAL(5,2) DEFAULT 0.00 CHECK (icms_aliquota >= 0),
    icms_valor DECIMAL(15,2) DEFAULT 0.00 CHECK (icms_valor >= 0),
    ipi_aliquota DECIMAL(5,2) DEFAULT 0.00 CHECK (ipi_aliquota >= 0),
    ipi_valor DECIMAL(15,2) DEFAULT 0.00 CHECK (ipi_valor >= 0),
    pis_aliquota DECIMAL(5,2) DEFAULT 0.00 CHECK (pis_aliquota >= 0),
    pis_valor DECIMAL(15,2) DEFAULT 0.00 CHECK (pis_valor >= 0),
    cofins_aliquota DECIMAL(5,2) DEFAULT 0.00 CHECK (cofins_aliquota >= 0),
    cofins_valor DECIMAL(15,2) DEFAULT 0.00 CHECK (cofins_valor >= 0),
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_quantidade_positiva CHECK (quantidade > 0),
    CONSTRAINT chk_valores_item_nao_negativos CHECK (valor_unitario >= 0 AND valor_total >= 0)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_numero ON cupons_fiscais(numero_cupom);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_data_emissao ON cupons_fiscais(data_emissao);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_cliente ON cupons_fiscais(id_cliente);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_operador ON cupons_fiscais(id_operador);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_status ON cupons_fiscais(status_cupom);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_forma_pagamento ON cupons_fiscais(forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_coo ON cupons_fiscais(coo);
CREATE INDEX IF NOT EXISTS idx_cupons_fiscais_ccf ON cupons_fiscais(ccf);

CREATE INDEX IF NOT EXISTS idx_itens_cupom_cupom ON itens_cupom_fiscal(id_cupom_fiscal);
CREATE INDEX IF NOT EXISTS idx_itens_cupom_produto ON itens_cupom_fiscal(id_produto);
CREATE INDEX IF NOT EXISTS idx_itens_cupom_codigo ON itens_cupom_fiscal(codigo_produto);

-- Comentários nas tabelas
COMMENT ON TABLE cupons_fiscais IS 'Tabela de cupons fiscais emitidos pelo sistema';
COMMENT ON COLUMN cupons_fiscais.id IS 'ID único do cupom fiscal';
COMMENT ON COLUMN cupons_fiscais.numero_cupom IS 'Número sequencial do cupom fiscal';
COMMENT ON COLUMN cupons_fiscais.data_emissao IS 'Data e hora de emissão do cupom';
COMMENT ON COLUMN cupons_fiscais.id_cliente IS 'ID do cliente (opcional)';
COMMENT ON COLUMN cupons_fiscais.cpf_cnpj_cliente IS 'CPF ou CNPJ do cliente';
COMMENT ON COLUMN cupons_fiscais.nome_cliente IS 'Nome do cliente';
COMMENT ON COLUMN cupons_fiscais.id_operador IS 'ID do operador que emitiu';
COMMENT ON COLUMN cupons_fiscais.nome_operador IS 'Nome do operador que emitiu';
COMMENT ON COLUMN cupons_fiscais.forma_pagamento IS 'Forma de pagamento utilizada';
COMMENT ON COLUMN cupons_fiscais.status_cupom IS 'Status atual do cupom';
COMMENT ON COLUMN cupons_fiscais.subtotal IS 'Valor subtotal dos itens';
COMMENT ON COLUMN cupons_fiscais.desconto IS 'Valor total de desconto';
COMMENT ON COLUMN cupons_fiscais.acrescimo IS 'Valor total de acréscimo';
COMMENT ON COLUMN cupons_fiscais.total_geral IS 'Valor total do cupom';
COMMENT ON COLUMN cupons_fiscais.valor_recebido IS 'Valor recebido do cliente';
COMMENT ON COLUMN cupons_fiscais.valor_troco IS 'Valor de troco calculado';
COMMENT ON COLUMN cupons_fiscais.ccf IS 'Código Contador de Cupom Fiscal';
COMMENT ON COLUMN cupons_fiscais.coo IS 'Código Contador de Operação';
COMMENT ON COLUMN cupons_fiscais.numero_serie_ecf IS 'Número de série da ECF';
COMMENT ON COLUMN cupons_fiscais.modelo_ecf IS 'Modelo da ECF';
COMMENT ON COLUMN cupons_fiscais.versao_software IS 'Versão do software';
COMMENT ON COLUMN cupons_fiscais.numero_ordem_operacao IS 'Número da ordem de operação';
COMMENT ON COLUMN cupons_fiscais.chave_acesso IS 'Chave de acesso NFe';
COMMENT ON COLUMN cupons_fiscais.protocolo_autorizacao IS 'Protocolo de autorização';
COMMENT ON COLUMN cupons_fiscais.data_hora_autorizacao IS 'Data e hora da autorização';
COMMENT ON COLUMN cupons_fiscais.codigo_verificacao IS 'Código de verificação';

COMMENT ON TABLE itens_cupom_fiscal IS 'Tabela de itens dos cupons fiscais';
COMMENT ON COLUMN itens_cupom_fiscal.id IS 'ID único do item';
COMMENT ON COLUMN itens_cupom_fiscal.id_cupom_fiscal IS 'ID do cupom fiscal principal';
COMMENT ON COLUMN itens_cupom_fiscal.id_produto IS 'ID do produto';
COMMENT ON COLUMN itens_cupom_fiscal.codigo_produto IS 'Código do produto';
COMMENT ON COLUMN itens_cupom_fiscal.descricao_produto IS 'Descrição do produto';
COMMENT ON COLUMN itens_cupom_fiscal.unidade_medida IS 'Unidade de medida';
COMMENT ON COLUMN itens_cupom_fiscal.quantidade IS 'Quantidade do item';
COMMENT ON COLUMN itens_cupom_fiscal.valor_unitario IS 'Valor unitário do item';
COMMENT ON COLUMN itens_cupom_fiscal.valor_total IS 'Valor total do item';
COMMENT ON COLUMN itens_cupom_fiscal.icms_aliquota IS 'Alíquota de ICMS';
COMMENT ON COLUMN itens_cupom_fiscal.icms_valor IS 'Valor de ICMS';
COMMENT ON COLUMN itens_cupom_fiscal.ipi_aliquota IS 'Alíquota de IPI';
COMMENT ON COLUMN itens_cupom_fiscal.ipi_valor IS 'Valor de IPI';
COMMENT ON COLUMN itens_cupom_fiscal.pis_aliquota IS 'Alíquota de PIS';
COMMENT ON COLUMN itens_cupom_fiscal.pis_valor IS 'Valor de PIS';
COMMENT ON COLUMN itens_cupom_fiscal.cofins_aliquota IS 'Alíquota de COFINS';
COMMENT ON COLUMN itens_cupom_fiscal.cofins_valor IS 'Valor de COFINS';
