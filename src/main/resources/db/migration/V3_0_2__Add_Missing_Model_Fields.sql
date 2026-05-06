-- =====================================================
-- HERMES COMERCIAL - ADICIONAR CAMPOS FALTANTES DOS MODELOS v3.0.2
-- Campos necessários para compatibilidade com as classes Java
-- Data: 06/05/2026
-- =====================================================

-- Adicionar campos faltantes na tabela usuarios (compatível com Usuario.java)
ALTER TABLE hermes_pdv.usuarios 
ADD COLUMN IF NOT EXISTS endereco TEXT,
ADD COLUMN IF NOT EXISTS bairro VARCHAR(100),
ADD COLUMN IF NOT EXISTS cidade VARCHAR(100),
ADD COLUMN IF NOT EXISTS estado VARCHAR(50),
ADD COLUMN IF NOT EXISTS cep VARCHAR(20),
ADD COLUMN IF NOT EXISTS telefone VARCHAR(20),
ADD COLUMN IF NOT EXISTS numero_documento VARCHAR(20),
ADD COLUMN IF NOT EXISTS whatsapp VARCHAR(20),
ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(20),
ADD COLUMN IF NOT EXISTS tipo_usuario VARCHAR(50);

-- Adicionar campos faltantes na tabela clientes (compatível com Cliente.java)
ALTER TABLE hermes_pdv.clientes 
ADD COLUMN IF NOT EXISTS tipo_pessoa VARCHAR(20) DEFAULT 'FISICA',
ADD COLUMN IF NOT EXISTS rg VARCHAR(20),
ADD COLUMN IF NOT EXISTS data_nascimento DATE,
ADD COLUMN IF NOT EXISTS celular VARCHAR(20),
ADD COLUMN IF NOT EXISTS numero VARCHAR(20),
ADD COLUMN IF NOT EXISTS complemento VARCHAR(100),
ADD COLUMN IF NOT EXISTS bairro VARCHAR(100),
ADD COLUMN IF NOT EXISTS observacao TEXT,
ADD COLUMN IF NOT EXISTS nome_fantasia VARCHAR(100),
ADD COLUMN IF NOT EXISTS inscricao_estadual VARCHAR(20);

-- Adicionar campos faltantes na tabela vendas (compatível com Venda.java)
ALTER TABLE hermes_pdv.vendas 
ADD COLUMN IF NOT EXISTS numero_venda VARCHAR(20) UNIQUE,
ADD COLUMN IF NOT EXISTS valor_desconto DECIMAL(10,2) DEFAULT 0,
ADD COLUMN IF NOT EXISTS valor_final DECIMAL(10,2) NOT NULL,
ADD COLUMN IF NOT EXISTS usuario_vendedor VARCHAR(50),
ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Adicionar campos faltantes na tabela pagamentos (compatível com Pagamento.java)
ALTER TABLE hermes_pdv.pagamentos 
ADD COLUMN IF NOT EXISTS valor_troco DECIMAL(10,2) DEFAULT 0,
ADD COLUMN IF NOT EXISTS numero_parcelas VARCHAR(10),
ADD COLUMN IF NOT EXISTS bandeira_cartao VARCHAR(50),
ADD COLUMN IF NOT EXISTS numero_autorizacao VARCHAR(50),
ADD COLUMN IF NOT EXISTS nsu VARCHAR(50),
ADD COLUMN IF NOT EXISTS cnpj_estabelecimento VARCHAR(20),
ADD COLUMN IF NOT EXISTS observacoes TEXT;

-- Adicionar campos faltantes na tabela itens_venda (compatível com ItemVenda.java)
ALTER TABLE hermes_pdv.itens_venda 
ADD COLUMN IF NOT EXISTS desconto DECIMAL(10,2) DEFAULT 0,
ADD COLUMN IF NOT EXISTS valor_final DECIMAL(10,2) NOT NULL,
ADD COLUMN IF NOT EXISTS observacao TEXT;

-- Adicionar campos faltantes na tabela produtos (compatível com Produto.java)
ALTER TABLE hermes_pdv.produtos 
ADD COLUMN IF NOT EXISTS codigo_barras VARCHAR(20) UNIQUE,
ADD COLUMN IF NOT EXISTS preco_custo DECIMAL(10,2),
ADD COLUMN IF NOT EXISTS unidade_medida VARCHAR(10) DEFAULT 'UN',
ADD COLUMN IF NOT EXISTS id_categoria INTEGER,
ADD COLUMN IF NOT EXISTS id_fornecedor INTEGER,
ADD COLUMN IF NOT EXISTS ativo BOOLEAN DEFAULT true,
ADD COLUMN IF NOT EXISTS data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Adicionar campos faltantes na tabela orcamentos (compatível com Venda.java - similar)
ALTER TABLE hermes_pdv.orcamentos 
ADD COLUMN IF NOT EXISTS usuario_aprovacao VARCHAR(50),
ADD COLUMN IF NOT EXISTS data_aprovacao TIMESTAMP,
ADD COLUMN IF NOT EXISTS motivo_rejeicao TEXT,
ADD COLUMN IF NOT EXISTS data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Adicionar campos faltantes na tabela itens_orcamento (compatível com ItemVenda.java - similar)
ALTER TABLE hermes_pdv.itens_orcamento 
ADD COLUMN IF NOT EXISTS desconto DECIMAL(10,2) DEFAULT 0,
ADD COLUMN IF NOT EXISTS valor_final DECIMAL(10,2) NOT NULL,
ADD COLUMN IF NOT EXISTS observacao TEXT;

-- Adicionar campos faltantes na tabela movimentacoes_estoque
ALTER TABLE hermes_pdv.movimentacoes_estoque 
ADD COLUMN IF NOT EXISTS localizacao VARCHAR(50),
ADD COLUMN IF NOT EXISTS tipo_documento VARCHAR(20),
ADD COLUMN IF NOT EXISTS numero_documento VARCHAR(50);

-- Adicionar campos faltantes na tabela sistema_logs
ALTER TABLE hermes_pdv.sistema_logs 
ADD COLUMN IF NOT EXISTS usuario_responsavel VARCHAR(50),
ADD COLUMN IF NOT EXISTS ip_origem VARCHAR(45),
ADD COLUMN IF NOT EXISTS dados_complementares JSONB;

-- Adicionar campos faltantes na tabela configuracoes
ALTER TABLE hermes_pdv.configuracoes 
ADD COLUMN IF NOT EXISTS valor_padrao TEXT,
ADD COLUMN IF NOT EXISTS tipo_dado VARCHAR(20) DEFAULT 'STRING',
ADD COLUMN IF NOT EXISTS obrigatorio BOOLEAN DEFAULT false;

-- Adicionar relacionamentos FK se não existirem
DO $$
BEGIN
    -- Adicionar FK para produtos.categorias se não existir
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'produtos_id_categoria_fkey' 
                   AND table_name = 'produtos') THEN
        ALTER TABLE produtos 
        ADD CONSTRAINT produtos_id_categoria_fkey 
        FOREIGN KEY (id_categoria) REFERENCES categorias(id);
    END IF;
    
    -- Adicionar FK para produtos.fornecedores se não existir
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_name = 'produtos_id_fornecedor_fkey' 
                   AND table_name = 'produtos') THEN
        ALTER TABLE produtos 
        ADD CONSTRAINT produtos_id_fornecedor_fkey 
        FOREIGN KEY (id_fornecedor) REFERENCES fornecedores(id);
    END IF;
END $$;

-- Adicionar índices para os novos campos (com schema correto)
CREATE INDEX IF NOT EXISTS idx_usuarios_tipo_usuario ON hermes_pdv.usuarios(tipo_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_telefone ON hermes_pdv.usuarios(telefone);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON hermes_pdv.usuarios(email);

CREATE INDEX IF NOT EXISTS idx_clientes_tipo_pessoa ON hermes_pdv.clientes(tipo_pessoa);
CREATE INDEX IF NOT EXISTS idx_clientes_celular ON hermes_pdv.clientes(celular);
CREATE INDEX IF NOT EXISTS idx_clientes_bairro ON hermes_pdv.clientes(bairro);

CREATE INDEX IF NOT EXISTS idx_produtos_codigo_barras ON hermes_pdv.produtos(codigo_barras);
CREATE INDEX IF NOT EXISTS idx_produtos_categoria ON hermes_pdv.produtos(id_categoria);
CREATE INDEX IF NOT EXISTS idx_produtos_fornecedor ON hermes_pdv.produtos(id_fornecedor);
CREATE INDEX IF NOT EXISTS idx_produtos_ativo ON hermes_pdv.produtos(ativo);

CREATE INDEX IF NOT EXISTS idx_vendas_numero_venda ON hermes_pdv.vendas(numero_venda);
CREATE INDEX IF NOT EXISTS idx_vendas_usuario_vendedor ON hermes_pdv.vendas(usuario_vendedor);

CREATE INDEX IF NOT EXISTS idx_pagamentos_numero_parcelas ON hermes_pdv.pagamentos(numero_parcelas);
CREATE INDEX IF NOT EXISTS idx_pagamentos_bandeira_cartao ON hermes_pdv.pagamentos(bandeira_cartao);

CREATE INDEX IF NOT EXISTS idx_orcamentos_usuario_aprovacao ON hermes_pdv.orcamentos(usuario_aprovacao);

CREATE INDEX IF NOT EXISTS idx_movimentacoes_localizacao ON hermes_pdv.movimentacoes_estoque(localizacao);
CREATE INDEX IF NOT EXISTS idx_movimentacoes_tipo_documento ON hermes_pdv.movimentacoes_estoque(tipo_documento);

-- Adicionar triggers para atualizar campos de timestamp
CREATE OR REPLACE FUNCTION atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultima_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar triggers às tabelas relevantes (com schema correto)
DROP TRIGGER IF EXISTS trg_usuarios_timestamp ON hermes_pdv.usuarios;
CREATE TRIGGER trg_usuarios_timestamp 
    BEFORE UPDATE ON hermes_pdv.usuarios 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

DROP TRIGGER IF EXISTS trg_clientes_timestamp ON hermes_pdv.clientes;
CREATE TRIGGER trg_clientes_timestamp 
    BEFORE UPDATE ON hermes_pdv.clientes 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

DROP TRIGGER IF EXISTS trg_produtos_timestamp ON hermes_pdv.produtos;
CREATE TRIGGER trg_produtos_timestamp 
    BEFORE UPDATE ON hermes_pdv.produtos 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

DROP TRIGGER IF EXISTS trg_vendas_timestamp ON hermes_pdv.vendas;
CREATE TRIGGER trg_vendas_timestamp 
    BEFORE UPDATE ON hermes_pdv.vendas 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

DROP TRIGGER IF EXISTS trg_orcamentos_timestamp ON hermes_pdv.orcamentos;
CREATE TRIGGER trg_orcamentos_timestamp 
    BEFORE UPDATE ON hermes_pdv.orcamentos 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

-- Adicionar constraints de validação para os novos campos (com schema correto)
ALTER TABLE hermes_pdv.usuarios 
ADD CONSTRAINT chk_usuarios_tipo_pessoa CHECK (tipo_documento IN ('CPF', 'CNPJ', 'RG', 'OUTRO'));

ALTER TABLE hermes_pdv.clientes 
ADD CONSTRAINT chk_clientes_tipo_pessoa CHECK (tipo_pessoa IN ('FISICA', 'JURIDICA'));

ALTER TABLE hermes_pdv.pagamentos 
ADD CONSTRAINT chk_pagamentos_tipo_pagamento CHECK (forma_pagamento IN ('DINHEIRO', 'CARTAO_CREDITO', 'CARTAO_DEBITO', 'PIX', 'TRANSFERENCIA', 'BOLETO', 'CHEQUE'));

ALTER TABLE hermes_pdv.movimentacoes_estoque 
ADD CONSTRAINT chk_movimentacoes_tipo_documento CHECK (tipo_documento IN ('NF', 'NFE', 'NFC-E', 'RECIBO', 'NOTA_FISCAL', 'OUTRO'));

-- Atualizar dados existentes para compatibilidade (com schema correto)
UPDATE hermes_pdv.usuarios SET 
    tipo_usuario = 'ADMIN',
    tipo_documento = 'CPF'
WHERE tipo_usuario IS NULL OR tipo_documento IS NULL;

UPDATE hermes_pdv.clientes SET 
    tipo_pessoa = 'FISICA'
WHERE tipo_pessoa IS NULL;

UPDATE hermes_pdv.produtos SET 
    ativo = true,
    unidade_medida = 'UN'
WHERE ativo IS NULL OR unidade_medida IS NULL;

COMMIT;
