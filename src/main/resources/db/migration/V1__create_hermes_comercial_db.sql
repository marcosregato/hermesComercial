-- =====================================================
-- HERMES COMERCIAL PDV v2.0 - ESTRUTURA DO BANCO DE DADOS
-- Versão: 1.0 - Estrutura Base
-- Data: 2026-04-26
-- Compatível: PostgreSQL, MySQL, H2
-- =====================================================

-- =====================================================
-- TABELA PRINCIPAL: PRODUTO
-- =====================================================

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

-- =====================================================
-- TABELA DE CATEGORIAS
-- =====================================================

CREATE TABLE IF NOT EXISTS categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- TABELA DE CLIENTES
-- =====================================================

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

-- =====================================================
-- TABELA DE VENDAS
-- =====================================================

CREATE TABLE IF NOT EXISTS venda (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER REFERENCES cliente(id),
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL CHECK (valor_total >= 0),
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    observacoes TEXT,
    usuario_venda VARCHAR(50) DEFAULT 'system',
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABELA DE ITENS DA VENDA
-- =====================================================

CREATE TABLE IF NOT EXISTS venda_item (
    id SERIAL PRIMARY KEY,
    venda_id INTEGER REFERENCES venda(id) ON DELETE CASCADE,
    produto_codigo VARCHAR(20) REFERENCES produto(codigo),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABELA DE USUÁRIOS DO SISTEMA
-- =====================================================

CREATE TABLE IF NOT EXISTS usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    perfil VARCHAR(20) DEFAULT 'USER',
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- ÍNDICES PARA PERFORMANCE
-- =====================================================

-- Índices para tabela produto
CREATE INDEX IF NOT EXISTS idx_produto_descricao ON produto(descricao);
CREATE INDEX IF NOT EXISTS idx_produto_categoria ON produto(categoria);
CREATE INDEX IF NOT EXISTS idx_produto_ativo ON produto(ativo);
CREATE INDEX IF NOT EXISTS idx_produto_preco ON produto(preco);
CREATE INDEX IF NOT EXISTS idx_produto_estoque ON produto(estoque);

-- Índices para tabela cliente
CREATE INDEX IF NOT EXISTS idx_cliente_nome ON cliente(nome);
CREATE INDEX IF NOT EXISTS idx_cliente_cpf_cnpj ON cliente(cpf_cnpj);
CREATE INDEX IF NOT EXISTS idx_cliente_ativo ON cliente(ativo);

-- Índices para tabela venda
CREATE INDEX IF NOT EXISTS idx_venda_data ON venda(data_venda);
CREATE INDEX IF NOT EXISTS idx_venda_cliente ON venda(cliente_id);
CREATE INDEX IF NOT EXISTS idx_venda_status ON venda(status);
CREATE INDEX IF NOT EXISTS idx_venda_usuario ON venda(usuario_venda);

-- Índices para tabela venda_item
CREATE INDEX IF NOT EXISTS idx_venda_item_venda ON venda_item(venda_id);
CREATE INDEX IF NOT EXISTS idx_venda_item_produto ON venda_item(produto_codigo);

-- Índices para tabela categoria
CREATE INDEX IF NOT EXISTS idx_categoria_nome ON categoria(nome);
CREATE INDEX IF NOT EXISTS idx_categoria_ativo ON categoria(ativo);

-- =====================================================
-- VIEWS (VISÕES) PARA RELATÓRIOS
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

-- View de resumo de vendas diárias
CREATE OR REPLACE VIEW vw_vendas_resumo_diario AS
SELECT 
    DATE(v.data_venda) as data,
    COUNT(v.id) as total_vendas,
    SUM(v.valor_total) as valor_total,
    AVG(v.valor_total) as ticket_medio
FROM venda v
WHERE v.status = 'CONCLUIDA'
GROUP BY DATE(v.data_venda)
ORDER BY data DESC;

-- View de produtos mais vendidos
CREATE OR REPLACE VIEW vw_produtos_mais_vendidos AS
SELECT 
    p.codigo,
    p.descricao,
    p.categoria,
    COALESCE(SUM(vi.quantidade), 0) as total_vendido,
    COALESCE(SUM(vi.subtotal), 0) as valor_total_vendido
FROM produto p
LEFT JOIN venda_item vi ON p.codigo = vi.produto_codigo
LEFT JOIN venda v ON vi.venda_id = v.id AND v.status = 'CONCLUIDA'
WHERE p.ativo = TRUE
GROUP BY p.codigo, p.descricao, p.categoria
ORDER BY total_vendido DESC;

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

-- Função para contar produtos por categoria
CREATE OR REPLACE FUNCTION fn_produtos_por_categoria(p_categoria VARCHAR)
RETURNS INTEGER AS $$
DECLARE
    total_produtos INTEGER;
BEGIN
    SELECT COUNT(*) INTO total_produtos
    FROM produto
    WHERE categoria = p_categoria AND ativo = TRUE;
    
    RETURN total_produtos;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- TRIGGERS AUTOMÁTICOS
-- =====================================================

-- Trigger para atualizar data_atualizacao na tabela produto
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

-- Trigger para atualizar data_atualizacao na tabela cliente
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

-- Trigger para atualizar data_atualizacao na tabela venda
CREATE OR REPLACE FUNCTION trg_venda_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_venda_atualizar_timestamp
    BEFORE UPDATE ON venda
    FOR EACH ROW
    EXECUTE FUNCTION trg_venda_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao na tabela categoria
CREATE OR REPLACE FUNCTION trg_categoria_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_categoria_atualizar_timestamp
    BEFORE UPDATE ON categoria
    FOR EACH ROW
    EXECUTE FUNCTION trg_categoria_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao na tabela usuario
CREATE OR REPLACE FUNCTION trg_usuario_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_usuario_atualizar_timestamp
    BEFORE UPDATE ON usuario
    FOR EACH ROW
    EXECUTE FUNCTION trg_usuario_atualizar_timestamp();

-- =====================================================
-- INSERÇÃO DE DADOS INICIAIS
-- =====================================================

-- Inserir categorias padrão
INSERT INTO categoria (nome, descricao) VALUES
('Informática', 'Computadores, notebooks e periféricos'),
('Periféricos', 'Mouse, teclado, fones de ouvido'),
('Monitores', 'Monitores e telas diversos'),
('Acessórios', 'Webcams, câmeras, suportes e outros'),
('Móveis', 'Mesas, cadeiras, armários de escritório'),
('Rede', 'Roteadores, switches, cabos'),
('Armazenamento', 'HDs, SSDs, pendrives, memórias'),
('Software', 'Programas e licenças'),
('Impressão', 'Impressoras e suprimentos'),
('Gamer', 'Produtos específicos para games')
ON CONFLICT (nome) DO NOTHING;

-- Inserir produtos de exemplo
INSERT INTO produto (codigo, descricao, preco, estoque, categoria, observacoes) VALUES
('001', 'Notebook Dell Inspire 15', 3500.00, 10, 'Informática', 'Notebook Intel i5, 8GB RAM, 256GB SSD'),
('002', 'Mouse Wireless Logitech MX3', 89.90, 50, 'Periféricos', 'Mouse sem fio alta precisão'),
('003', 'Teclado Mecânico RGB Gamer', 250.00, 25, 'Periféricos', 'Teclado mecânico com iluminação RGB'),
('004', 'Monitor 24" LED Full HD', 899.00, 15, 'Monitores', 'Monitor LED 24" Full HD 60Hz'),
('005', 'Webcam HD 1080p com Microfone', 150.00, 30, 'Acessórios', 'Webcam Full HD com microfone'),
('006', 'Cadeira Gamer Premium', 450.00, 8, 'Móveis', 'Cadeira ergonômica com ajuste de altura'),
('007', 'Headset Bluetooth 5.0', 120.00, 40, 'Periféricos', 'Headset sem fio com cancelamento de ruído'),
('008', 'SSD 480GB NVMe', 280.00, 20, 'Armazenamento', 'SSD NVMe M.2 480GB alta performance'),
('009', 'Suporte para Monitor Articulado', 45.00, 35, 'Móveis', 'Suporte ajustável para monitor até 32"'),
('010', 'Mouse Pad Gamer XXL', 35.00, 60, 'Periféricos', 'Mouse pad grande 900x400mm'),
('011', 'Roteador WiFi 6', 320.00, 12, 'Rede', 'Roteador WiFi 6 Gigabit'),
('012', 'HD Externo 1TB USB', 180.00, 22, 'Armazenamento', 'HD externo portátil 1TB USB 3.0'),
('013', 'Fone de Ouvido Bluetooth', 95.00, 45, 'Periféricos', 'Fone ouvido sem fio com bass'),
('014', 'Mesa para Escritório', 380.00, 6, 'Móveis', 'Mesa escritório 1.20x60cm'),
('015', 'Switch 8 Portas Gigabit', 150.00, 18, 'Rede', 'Switch gerenciável 8 portas')
ON CONFLICT (codigo) DO NOTHING;

-- Inserir clientes de exemplo
INSERT INTO cliente (nome, cpf_cnpj, email, telefone, endereco) VALUES
('João Silva', '12345678901', 'joao.silva@email.com', '(11) 98765-4321', 'Rua das Flores, 123 - São Paulo/SP'),
('Maria Santos', '98765432100', 'maria.santos@email.com', '(11) 91234-5678', 'Avenida Paulista, 456 - São Paulo/SP'),
('Pedro Oliveira', '45678912345', 'pedro.oliveira@email.com', '(21) 98765-4321', 'Rua Copacabana, 789 - Rio de Janeiro/RJ'),
('Ana Costa', '78912345678', 'ana.costa@email.com', '(31) 97654-3210', 'Rua Afonso Pena, 321 - Belo Horizonte/MG'),
('Carlos Ferreira', '32165498701', 'carlos.ferreira@email.com', '(41) 96543-2109', 'Rua XV de Novembro, 654 - Curitiba/PR')
ON CONFLICT (cpf_cnpj) DO NOTHING;

-- Inserir usuário administrador padrão
INSERT INTO usuario (nome, login, senha, email, perfil) VALUES
('Administrador', 'admin', 'admin123', 'admin@hermescomercial.com', 'ADMIN')
ON CONFLICT (login) DO NOTHING;

-- =====================================================
-- RELATÓRIOS E CONSULTAS ÚTEIS (COMENTADOS)
-- =====================================================

/*
-- Produtos mais vendidos (últimos 30 dias)
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

-- Top 5 clientes por valor de compra
SELECT 
    c.nome,
    COUNT(v.id) as total_compras,
    SUM(v.valor_total) as valor_total
FROM cliente c
JOIN venda v ON c.id = v.cliente_id
WHERE v.status = 'CONCLUIDA'
GROUP BY c.id, c.nome
ORDER BY valor_total DESC
LIMIT 5;
*/

-- =====================================================
-- ESTATÍSTICAS INICIAIS
-- =====================================================

-- Mostrar resumo das tabelas
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

-- Mostrar estatísticas do estoque
SELECT 
    'Estoque Total: ' || fn_estoque_total() as info
UNION ALL
SELECT 
    'Valor Total Estoque: R$ ' || fn_valor_total_estoque() as info
UNION ALL
SELECT 
    'Categorias Cadastradas: ' || COUNT(*) FROM categoria WHERE ativo = TRUE;

COMMIT;
