-- =====================================================
-- HERMES COMERCIAL - BANCO DE DADOS POSTGRESQL
-- Nome do Banco: hermescomercialdb
-- Versão: 1.0
-- Data: 2026-04-26
-- =====================================================

-- Criar banco de dados (executar como superusuário)
-- CREATE DATABASE hermescomercialdb 
--     WITH OWNER = postgres
--     ENCODING = 'UTF8'
--     TABLESPACE = pg_default
--     CONNECTION LIMIT = -1;

-- Conectar ao banco
-- \c hermescomercialdb

-- =====================================================
-- ESTRUTURA DAS TABELAS
-- =====================================================

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produto (
    codigo VARCHAR(20) PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    estoque INTEGER NOT NULL DEFAULT 0 CHECK (estoque >= 0),
    categoria VARCHAR(100) NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT 'system',
    usuario_atualizacao VARCHAR(50) DEFAULT 'system',
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Categorias (opcional - para gerenciamento de categorias)
CREATE TABLE IF NOT EXISTS categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Clientes
CREATE TABLE IF NOT EXISTS cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE,
    email VARCHAR(255),
    telefone VARCHAR(20),
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Vendas
CREATE TABLE IF NOT EXISTS venda (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES cliente(id),
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL CHECK (valor_total >= 0),
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    observacoes TEXT,
    usuario_venda VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Itens da Venda
CREATE TABLE IF NOT EXISTS venda_item (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES venda(id) ON DELETE CASCADE,
    produto_codigo VARCHAR(20) REFERENCES produto(codigo),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Usuários do Sistema
CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, -- Em produção, usar hash
    email VARCHAR(255),
    perfil VARCHAR(20) DEFAULT 'USER', -- ADMIN, USER, OPERADOR
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- ÍNDICES
-- =====================================================

-- Índices para tabela produto
CREATE INDEX IF NOT EXISTS idx_produto_descricao ON produto(descricao);
CREATE INDEX IF NOT EXISTS idx_produto_categoria ON produto(categoria);
CREATE INDEX IF NOT EXISTS idx_produto_ativo ON produto(ativo);
CREATE INDEX IF NOT EXISTS idx_produto_preco ON produto(preco);

-- Índices para tabela cliente
CREATE INDEX IF NOT EXISTS idx_cliente_nome ON cliente(nome);
CREATE INDEX IF NOT EXISTS idx_cliente_cpf_cnpj ON cliente(cpf_cnpj);
CREATE INDEX IF NOT EXISTS idx_cliente_ativo ON cliente(ativo);

-- Índices para tabela venda
CREATE INDEX IF NOT EXISTS idx_venda_data ON venda(data_venda);
CREATE INDEX IF NOT EXISTS idx_venda_cliente ON venda(cliente_id);
CREATE INDEX IF NOT EXISTS idx_venda_status ON venda(status);

-- Índices para tabela venda_item
CREATE INDEX IF NOT EXISTS idx_venda_item_venda ON venda_item(venda_id);
CREATE INDEX IF NOT EXISTS idx_venda_item_produto ON venda_item(produto_codigo);

-- =====================================================
-- TRIGGERS
-- =====================================================

-- Trigger para atualizar data_atualizacao da tabela produto
CREATE OR REPLACE FUNCTION trg_produto_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_produto_atualizar_timestamp
    BEFORE UPDATE ON produto
    FOR EACH ROW
    EXECUTE FUNCTION trg_produto_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela cliente
CREATE OR REPLACE FUNCTION trg_cliente_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_cliente_atualizar_timestamp
    BEFORE UPDATE ON cliente
    FOR EACH ROW
    EXECUTE FUNCTION trg_cliente_atualizar_timestamp();

-- =====================================================
-- VIEWS (VISÕES)
-- =====================================================

-- View de produtos com estoque baixo
CREATE OR REPLACE VIEW vw_produtos_estoque_baixo AS
SELECT 
    codigo,
    descricao,
    categoria,
    estoque,
    preco,
    data_atualizacao
FROM produto 
WHERE estoque <= 10 AND ativo = TRUE
ORDER BY estoque ASC, descricao;

-- View de vendas por período
CREATE OR REPLACE VIEW vw_vendas_resumo AS
SELECT 
    DATE(v.data_venda) as data,
    COUNT(v.id) as total_vendas,
    SUM(v.valor_total) as valor_total,
    AVG(v.valor_total) as ticket_medio
FROM venda v
WHERE v.status = 'CONCLUIDA'
GROUP BY DATE(v.data_venda)
ORDER BY data DESC;

-- =====================================================
-- DADOS DE EXEMPLO
-- =====================================================

-- Inserir categorias de exemplo
INSERT INTO categoria (nome, descricao) VALUES
('Informática', 'Computadores, notebooks e periféricos'),
('Periféricos', 'Mouse, teclado, fones de ouvido'),
('Monitores', 'Monitores e telas'),
('Acessórios', 'Webcams, câmeras, suportes'),
('Móveis', 'Mesas, cadeiras, armários')
ON CONFLICT (nome) DO NOTHING;

-- Inserir produtos de exemplo
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes) VALUES
('001', 'Notebook Dell Inspire 15', 3500.00, 10, 'Informática', 'Notebook com processador Intel i5, 8GB RAM, 256GB SSD'),
('002', 'Mouse Wireless Logitech MX3', 89.90, 50, 'Periféricos', 'Mouse sem fio com precisão avançada'),
('003', 'Teclado Mecânico RGB Gamer', 250.00, 25, 'Periféricos', 'Teclado mecânico com iluminação RGB'),
('004', 'Monitor 24" LED Full HD', 899.00, 15, 'Monitores', 'Monitor LED 24 polegadas Full HD'),
('005', 'Webcam HD 1080p com Microfone', 150.00, 30, 'Acessórios', 'Webcam Full HD com microfone integrado'),
('006', 'Cadeira Gamer Premium', 450.00, 8, 'Móveis', 'Cadeira ergonômica com ajuste de altura'),
('007', 'Headset Bluetooth', 120.00, 40, 'Periféricos', 'Headset sem fio com cancelamento de ruído'),
('008', 'SSD 480GB NVMe', 280.00, 20, 'Informática', 'Armazenamento SSD de alta performance'),
('009', 'Suporte para Monitor', 45.00, 35, 'Móveis', 'Suporte ajustável para monitor'),
('010', 'Mouse Pad Gamer XXL', 35.00, 60, 'Periféricos', 'Mouse pad grande com superfície otimizada')
ON CONFLICT (codigo) DO NOTHING;

-- Inserir clientes de exemplo
INSERT INTO cliente (nome, cpf_cnpj, email, telefone, endereco) VALUES
('João Silva', '12345678901', 'joao.silva@email.com', '(11) 98765-4321', 'Rua das Flores, 123 - São Paulo/SP'),
('Maria Santos', '98765432100', 'maria.santos@email.com', '(11) 91234-5678', 'Avenida Paulista, 456 - São Paulo/SP'),
('Pedro Oliveira', '45678912345', 'pedro.oliveira@email.com', '(21) 98765-4321', 'Rua Copacabana, 789 - Rio de Janeiro/RJ')
ON CONFLICT (cpf_cnpj) DO NOTHING;

-- Inserir usuário administrador
INSERT INTO usuario (nome, login, senha, email, perfil) VALUES
('Administrador', 'admin', '$2b$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj6ukx.LrUpm', 'admin@hermescomercial.com', 'ADMIN')
ON CONFLICT (login) DO NOTHING;

-- =====================================================
-- FUNÇÕES ÚTEIS
-- =====================================================

-- Função para calcular estoque total
CREATE OR REPLACE FUNCTION fn_estoque_total()
RETURNS INTEGER AS $$
DECLARE
    total_estoque INTEGER;
BEGIN
    SELECT COALESCE(SUM(estoque), 0) INTO total_estoque
    FROM produto
    WHERE ativo = TRUE;
    
    RETURN total_estoque;
END;
$$ LANGUAGE plpgsql;

-- Função para calcular valor total do estoque
CREATE OR REPLACE FUNCTION fn_valor_total_estoque()
RETURNS DECIMAL(10,2) AS $$
DECLARE
    valor_total DECIMAL(10,2);
BEGIN
    SELECT COALESCE(SUM(preco * estoque), 0) INTO valor_total
    FROM produto
    WHERE ativo = TRUE;
    
    RETURN valor_total;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- PROCEDIMENTOS ARMAZENADOS
-- =====================================================

-- Procedimento para registrar venda
CREATE OR REPLACE PROCEDURE sp_registrar_venda(
    p_cliente_id INTEGER,
    p_itens JSON, -- Formato: [{"produto_codigo": "001", "quantidade": 2, "preco_unitario": 3500.00}, ...]
    p_forma_pagamento VARCHAR(50),
    p_usuario VARCHAR(50) DEFAULT 'system'
)
LANGUAGE plpgsql AS $$
DECLARE
    v_venda_id INTEGER;
    v_item JSON;
    v_produto_codigo VARCHAR(20);
    v_quantidade INTEGER;
    v_preco_unitario DECIMAL(10,2);
    v_subtotal DECIMAL(10,2);
    v_valor_total DECIMAL(10,2) := 0;
    v_estoque_atual INTEGER;
BEGIN
    -- Calcular valor total
    FOR v_item IN SELECT * FROM json_array_elements(p_itens)
    LOOP
        v_produto_codigo := v_item->>'produto_codigo';
        v_quantidade := (v_item->>'quantidade')::INTEGER;
        v_preco_unitario := (v_item->>'preco_unitario')::DECIMAL(10,2);
        v_subtotal := v_quantidade * v_preco_unitario;
        v_valor_total := v_valor_total + v_subtotal;
    END LOOP;
    
    -- Inserir venda
    INSERT INTO venda (cliente_id, valor_total, forma_pagamento, usuario_venda)
    VALUES (p_cliente_id, v_valor_total, p_forma_pagamento, p_usuario)
    RETURNING id INTO v_venda_id;
    
    -- Inserir itens e atualizar estoque
    FOR v_item IN SELECT * FROM json_array_elements(p_itens)
    LOOP
        v_produto_codigo := v_item->>'produto_codigo';
        v_quantidade := (v_item->>'quantidade')::INTEGER;
        v_preco_unitario := (v_item->>'preco_unitario')::DECIMAL(10,2);
        v_subtotal := v_quantidade * v_preco_unitario;
        
        -- Verificar estoque
        SELECT estoque INTO v_estoque_atual
        FROM produto
        WHERE codigo = v_produto_codigo AND ativo = TRUE
        FOR UPDATE;
        
        IF v_estoque_atual < v_quantidade THEN
            RAISE EXCEPTION 'Estoque insuficiente para o produto %', v_produto_codigo;
        END IF;
        
        -- Inserir item da venda
        INSERT INTO venda_item (venda_id, produto_codigo, quantidade, preco_unitario, subtotal)
        VALUES (v_venda_id, v_produto_codigo, v_quantidade, v_preco_unitario, v_subtotal);
        
        -- Atualizar estoque
        UPDATE produto
        SET estoque = estoque - v_quantidade
        WHERE codigo = v_produto_codigo;
    END LOOP;
    
    COMMIT;
END;
$$;

-- =====================================================
-- RELATÓRIOS E CONSULTAS ÚTEIS
-- =====================================================

-- Produtos mais vendidos
SELECT 
    p.codigo,
    p.descricao,
    SUM(vi.quantidade) as total_vendido,
    SUM(vi.subtotal) as valor_total
FROM produto p
JOIN venda_item vi ON p.codigo = vi.produto_codigo
JOIN venda v ON vi.venda_id = v.id
WHERE v.status = 'CONCLUIDA'
    AND v.data_venda >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.codigo, p.descricao
ORDER BY total_vendido DESC
LIMIT 10;

-- Vendas por categoria
SELECT 
    p.categoria,
    COUNT(DISTINCT v.id) as total_vendas,
    SUM(vi.subtotal) as valor_total
FROM produto p
JOIN venda_item vi ON p.codigo = vi.produto_codigo
JOIN venda v ON vi.venda_id = v.id
WHERE v.status = 'CONCLUIDA'
    AND v.data_venda >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY p.categoria
ORDER BY valor_total DESC;

-- =====================================================
-- LIMPEZA E MANUTENÇÃO
-- =====================================================

-- Limpar dados antigos (opcional - executar com cuidado)
-- DELETE FROM venda WHERE data_venda < CURRENT_DATE - INTERVAL '2 years';
-- DELETE FROM venda_item WHERE venda_id NOT IN (SELECT id FROM venda);

-- =====================================================
-- PERMISSÕES (opcional)
-- =====================================================

-- Criar usuário para aplicação
-- CREATE USER hermes_app WITH PASSWORD 'senha_segura';
-- GRANT CONNECT ON DATABASE hermescomercialdb TO hermes_app;
-- GRANT USAGE ON SCHEMA public TO hermes_app;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO hermes_app;
-- GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO hermes_app;

-- =====================================================
-- FINALIZAÇÃO
-- =====================================================

-- Mostrar estatísticas do banco
SELECT 
    'produto' as tabela, COUNT(*) as total_registros FROM produto
UNION ALL
SELECT 
    'categoria' as tabela, COUNT(*) as total_registros FROM categoria
UNION ALL
SELECT 
    'cliente' as tabela, COUNT(*) as total_registros FROM cliente
UNION ALL
SELECT 
    'venda' as tabela, COUNT(*) as total_registros FROM venda
UNION ALL
SELECT 
    'venda_item' as tabela, COUNT(*) as total_registros FROM venda_item
UNION ALL
SELECT 
    'usuario' as tabela, COUNT(*) as total_registros FROM usuario;

-- Mostrar funções disponíveis
SELECT 
    'Estoque Total: ' || fn_estoque_total() as info
UNION ALL
SELECT 
    'Valor Total Estoque: R$ ' || fn_valor_total_estoque() as info;

COMMIT;
