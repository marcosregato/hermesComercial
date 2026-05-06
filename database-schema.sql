-- =====================================================
-- Hermes Comercial PDV - Schema do Banco de Dados PostgreSQL
-- Criado em: 06/05/2026
-- =====================================================

-- Limpar schema existente (apenas para desenvolvimento)
DROP SCHEMA IF EXISTS hermes_pdv CASCADE;
CREATE SCHEMA hermes_pdv;
SET search_path TO hermes_pdv, public;

-- =====================================================
-- Tabela de Usuários
-- =====================================================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nome_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Clientes
-- =====================================================
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(10),
    ativo BOOLEAN DEFAULT true,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Categorias de Produtos
-- =====================================================
CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT,
    ativa BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Fornecedores
-- =====================================================
CREATE TABLE fornecedores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    ativo BOOLEAN DEFAULT true,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Produtos
-- =====================================================
CREATE TABLE produtos (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    codigo_barras VARCHAR(20) UNIQUE,
    preco_venda DECIMAL(10,2) NOT NULL,
    preco_custo DECIMAL(10,2),
    estoque_atual INTEGER DEFAULT 0,
    estoque_minimo INTEGER DEFAULT 0,
    unidade_medida VARCHAR(10) DEFAULT 'UN',
    id_categoria INTEGER REFERENCES categorias(id),
    id_fornecedor INTEGER REFERENCES fornecedores(id),
    ativo BOOLEAN DEFAULT true,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Vendas
-- =====================================================
CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    numero_venda VARCHAR(20) UNIQUE NOT NULL,
    id_cliente INTEGER REFERENCES clientes(id),
    valor_total DECIMAL(10,2) NOT NULL,
    valor_desconto DECIMAL(10,2) DEFAULT 0,
    valor_final DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'PENDENTE',
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_vendedor VARCHAR(50)
);

-- =====================================================
-- Tabela de Itens de Venda
-- =====================================================
CREATE TABLE itens_venda (
    id SERIAL PRIMARY KEY,
    id_venda INTEGER REFERENCES vendas(id) ON DELETE CASCADE,
    id_produto INTEGER REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Pagamentos
-- =====================================================
CREATE TABLE pagamentos (
    id SERIAL PRIMARY KEY,
    id_venda INTEGER REFERENCES vendas(id),
    valor_pago DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(50) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    data_pagamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT
);

-- =====================================================
-- Tabela de Orçamentos
-- =====================================================
CREATE TABLE orcamentos (
    id SERIAL PRIMARY KEY,
    numero_orcamento VARCHAR(20) UNIQUE NOT NULL,
    id_cliente INTEGER REFERENCES clientes(id),
    valor_total DECIMAL(10,2) NOT NULL,
    validade_dias INTEGER DEFAULT 30,
    status VARCHAR(20) DEFAULT 'ABERTO',
    data_orcamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_expiracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP + INTERVAL '30 days',
    usuario_vendedor VARCHAR(50)
);

-- =====================================================
-- Tabela de Itens de Orçamento
-- =====================================================
CREATE TABLE itens_orcamento (
    id SERIAL PRIMARY KEY,
    id_orcamento INTEGER REFERENCES orcamentos(id) ON DELETE CASCADE,
    id_produto INTEGER REFERENCES produtos(id),
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Inserir Dados de Exemplo
-- =====================================================

-- Usuários
INSERT INTO usuarios (username, password, nome_completo, email) VALUES
('admin', 'admin123', 'Administrador do Sistema', 'admin@hermes.comercial'),
('vendedor1', 'vendedor123', 'Vendedor Principal', 'vendedor1@hermes.comercial'),
('caixa1', 'caixa123', 'Operador de Caixa', 'caixa1@hermes.comercial'),
('gerente1', 'gerente123', 'Gerente de Loja', 'gerente1@hermes.comercial');

-- Categorias
INSERT INTO categorias (nome, descricao) VALUES
('Eletrônicos', 'Produtos eletrônicos em geral'),
('Roupas', 'Vestuário e acessórios'),
('Alimentos', 'Produtos alimentícios'),
('Móveis', 'Móveis e decoração'),
('Livros', 'Livros e material escolar');

-- Fornecedores
INSERT INTO fornecedores (nome, cnpj, telefone, email) VALUES
('Fornecedor A', '12345678900123', '(11) 1111-1111', 'fornecedor.a@email.com'),
('Fornecedor B', '98765432100123', '(11) 2222-2222', 'fornecedor.b@email.com'),
('Fornecedor C', '45678912300123', '(11) 3333-3333', 'fornecedor.c@email.com');

-- Clientes
INSERT INTO clientes (nome, cpf_cnpj, telefone, email, cidade, estado) VALUES
('João Silva', '12345678901', '(11) 9999-8888', 'joao@email.com', 'São Paulo', 'SP'),
('Maria Santos', '98765432101', '(11) 8888-7777', 'maria@email.com', 'São Paulo', 'SP'),
('Pedro Oliveira', '45678912301', '(11) 7777-6666', 'pedro@email.com', 'Rio de Janeiro', 'RJ'),
('Ana Costa', '78912345601', '(11) 6666-5555', 'ana@email.com', 'Belo Horizonte', 'MG'),
('Carlos Ferreira', '32165498701', '(11) 5555-4444', 'carlos@email.com', 'Brasília', 'DF');

-- Produtos
INSERT INTO produtos (nome, descricao, codigo_barras, preco_venda, preco_custo, estoque_atual, estoque_minimo, id_categoria, id_fornecedor) VALUES
('Notebook Dell', 'Notebook Dell Core i5 8GB 256GB SSD', '7891234567890', 3500.00, 2800.00, 15, 5, 1, 1),
('Smartphone Samsung', 'Samsung Galaxy A54 128GB', '7891234567891', 2200.00, 1800.00, 25, 10, 1, 2),
('Camiseta Polo', 'Camiseta Polo Masculina Azul M', '7891234567892', 89.90, 45.00, 100, 20, 2, 3),
('Calça Jeans', 'Calça Jeans Masculina Azul 40', '7891234567893', 159.90, 85.00, 50, 15, 2, 3),
('Tênis Nike', 'Tênis Nike Corrida Masculino 42', '7891234567894', 399.90, 250.00, 30, 8, 2, 2),
('Cadeira Escritório', 'Cadeira Executiva Giratória', '7891234567895', 890.00, 550.00, 12, 3, 4, 1),
('Mesa Escritório', 'Mesa de Escritório 1,20x0,60m', '7891234567896', 450.00, 280.00, 8, 2, 4, 1),
('Livro Java', 'Programming Java for Beginners', '7891234567897', 125.00, 75.00, 40, 10, 5, 3),
('Café 500g', 'Café Torrado Premium 500g', '7891234567898', 35.90, 22.00, 80, 25, 3, 3),
('Arroz 5kg', 'Arroz Branco Tipo 1 5kg', '7891234567899', 25.90, 18.00, 60, 20, 3, 3);

-- Vendas de Exemplo
INSERT INTO vendas (numero_venda, id_cliente, valor_total, valor_desconto, valor_final, forma_pagamento, status, usuario_vendedor) VALUES
('V001', 1, 3500.00, 0.00, 3500.00, 'Cartão Crédito', 'CONCLUIDA', 'vendedor1'),
('V002', 2, 2200.00, 100.00, 2100.00, 'PIX', 'CONCLUIDA', 'vendedor1'),
('V003', 3, 489.80, 0.00, 489.80, 'Dinheiro', 'CONCLUIDA', 'caixa1'),
('V004', 4, 1340.00, 50.00, 1290.00, 'Cartão Débito', 'CONCLUIDA', 'vendedor1'),
('V005', 5, 890.00, 0.00, 890.00, 'PIX', 'PENDENTE', 'caixa1');

-- Itens de Venda
INSERT INTO itens_venda (id_venda, id_produto, quantidade, valor_unitario, valor_total) VALUES
(1, 1, 1, 3500.00, 3500.00),
(2, 2, 1, 2200.00, 2200.00),
(3, 3, 2, 89.90, 179.80),
(3, 4, 2, 155.00, 310.00),
(4, 5, 1, 399.90, 399.90),
(4, 6, 1, 890.00, 890.00),
(5, 7, 1, 890.00, 890.00);

-- Pagamentos
INSERT INTO pagamentos (id_venda, valor_pago, forma_pagamento, status) VALUES
(1, 3500.00, 'Cartão Crédito', 'PAGO'),
(2, 2100.00, 'PIX', 'PAGO'),
(3, 489.80, 'Dinheiro', 'PAGO'),
(4, 1290.00, 'Cartão Débito', 'PAGO'),
(5, 890.00, 'PIX', 'PENDENTE');

-- Orçamentos
INSERT INTO orcamentos (numero_orcamento, id_cliente, valor_total, status, usuario_vendedor) VALUES
('O001', 1, 4500.00, 'ABERTO', 'vendedor1'),
('O002', 2, 1800.00, 'ABERTO', 'vendedor1'),
('O003', 3, 750.00, 'FECHADO', 'caixa1');

-- Itens de Orçamento
INSERT INTO itens_orcamento (id_orcamento, id_produto, quantidade, valor_unitario, valor_total) VALUES
(1, 1, 1, 3500.00, 3500.00),
(1, 2, 1, 1000.00, 1000.00),
(2, 3, 5, 89.90, 449.50),
(2, 4, 3, 155.00, 465.00),
(2, 8, 1, 125.00, 125.00),
(3, 9, 2, 35.90, 71.80),
(3, 10, 3, 25.90, 77.70);

-- =====================================================
-- Criar Índices para Melhor Performance
-- =====================================================

CREATE INDEX idx_produtos_ativos ON produtos(ativo);
CREATE INDEX idx_produtos_categoria ON produtos(id_categoria);
CREATE INDEX idx_vendas_data ON vendas(data_venda);
CREATE INDEX idx_vendas_status ON vendas(status);
CREATE INDEX idx_clientes_ativos ON clientes(ativo);
CREATE INDEX idx_itens_venda_produto ON itens_venda(id_produto);

-- =====================================================
-- Criar Views para Consultas Comuns
-- =====================================================

CREATE VIEW vw_produtos_estoque AS
SELECT 
    p.id, p.nome, p.codigo_barras, p.preco_venda, 
    p.estoque_atual, p.estoque_minimo,
    c.nome as categoria,
    CASE WHEN p.estoque_atual <= p.estoque_minimo THEN 'CRÍTICO'
         WHEN p.estoque_atual <= (p.estoque_minimo * 2) THEN 'BAIXO'
         ELSE 'NORMAL' END as status_estoque
FROM produtos p
LEFT JOIN categorias c ON p.id_categoria = c.id
WHERE p.ativo = true;

CREATE VIEW vw_vendas_resumo AS
SELECT 
    v.id, v.numero_venda, v.valor_total, v.valor_final,
    c.nome as cliente,
    v.status, v.data_venda, v.usuario_vendedor
FROM vendas v
LEFT JOIN clientes c ON v.id_cliente = c.id;

CREATE VIEW vw_clientes_compras AS
SELECT 
    c.id, c.nome, c.cpf_cnpj, c.telefone, c.email,
    COUNT(v.id) as total_compras,
    COALESCE(SUM(v.valor_final), 0) as valor_total_compras
FROM clientes c
LEFT JOIN vendas v ON c.id = v.id_cliente AND v.status = 'CONCLUIDA'
GROUP BY c.id, c.nome, c.cpf_cnpj, c.telefone, c.email;

-- =====================================================
-- Finalizar
-- =====================================================

-- Mostrar resumo das tabelas criadas
SELECT 
    schemaname,
    tablename,
    tableowner
FROM pg_tables 
WHERE schemaname = 'hermes_pdv'
ORDER BY tablename;

-- Mostrar quantidade de registros em cada tabela
SELECT 
    'clientes' as tabela, COUNT(*) as registros FROM clientes
UNION ALL
SELECT 'produtos' as tabela, COUNT(*) as registros FROM produtos
UNION ALL
SELECT 'vendas' as tabela, COUNT(*) as registros FROM vendas
UNION ALL
SELECT 'categorias' as tabela, COUNT(*) as registros FROM categorias
UNION ALL
SELECT 'fornecedores' as tabela, COUNT(*) as registros FROM fornecedores
UNION ALL
SELECT 'usuarios' as tabela, COUNT(*) as registros FROM usuarios
ORDER BY tabela;
