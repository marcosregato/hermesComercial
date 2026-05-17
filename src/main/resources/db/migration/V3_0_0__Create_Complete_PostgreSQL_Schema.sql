-- =====================================================
-- HERMES COMERCIAL PDV - SCHEMA COMPLETO POSTGRESQL v3.0.0
-- Estrutura completa e correta para PostgreSQL
-- Data: 06/05/2026
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
-- Tabela de Perfis (RBAC)
-- =====================================================
CREATE TABLE perfis (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    nivel_acesso INTEGER DEFAULT 1,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Permissões (RBAC)
-- =====================================================
CREATE TABLE permissoes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    modulo VARCHAR(50) NOT NULL,
    acao VARCHAR(50) NOT NULL,
    ativo BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Tabela de Relacionamento Usuário-Perfil
-- =====================================================
CREATE TABLE usuario_perfil (
    usuario_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
    perfil_id INTEGER REFERENCES perfis(id) ON DELETE CASCADE,
    data_atribuicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (usuario_id, perfil_id)
);

-- =====================================================
-- Tabela de Relacionamento Perfil-Permissão
-- =====================================================
CREATE TABLE perfil_permissao (
    perfil_id INTEGER REFERENCES perfis(id) ON DELETE CASCADE,
    permissao_id INTEGER REFERENCES permissoes(id) ON DELETE CASCADE,
    data_concessao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (perfil_id, permissao_id)
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
-- Tabela de Movimentações de Estoque
-- =====================================================
CREATE TABLE movimentacoes_estoque (
    id SERIAL PRIMARY KEY,
    id_produto INTEGER REFERENCES produtos(id),
    tipo_movimentacao VARCHAR(20) NOT NULL, -- ENTRADA, SAIDA, AJUSTE, TRANSFERENCIA
    quantidade INTEGER NOT NULL,
    saldo_anterior INTEGER,
    saldo_novo INTEGER,
    motivo TEXT,
    documento_referencia VARCHAR(50), -- NF, Nota Fiscal, etc.
    data_movimentacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_responsavel VARCHAR(50)
);

-- =====================================================
-- Tabela de Configurações do Sistema
-- =====================================================
CREATE TABLE configuracoes (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(100) UNIQUE NOT NULL,
    valor TEXT,
    tipo VARCHAR(50) NOT NULL DEFAULT 'STRING',
    descricao TEXT,
    modulo VARCHAR(50),
    ativa BOOLEAN DEFAULT true,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT 'system'
);

-- =====================================================
-- Tabela de Logs do Sistema
-- =====================================================
CREATE TABLE sistema_logs (
    id SERIAL PRIMARY KEY,
    nivel VARCHAR(20) NOT NULL, -- INFO, WARN, ERROR, DEBUG
    modulo VARCHAR(50),
    mensagem TEXT NOT NULL,
    dados_adicionais JSONB,
    usuario_id VARCHAR(50),
    ip_address VARCHAR(45),
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- Índices para Performance
-- =====================================================

-- Índices de Usuários
CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_ativo ON usuarios(ativo);

-- Índices de Clientes
CREATE INDEX idx_clientes_cpf_cnpj ON clientes(cpf_cnpj);
CREATE INDEX idx_clientes_nome ON clientes(nome);
CREATE INDEX idx_clientes_ativo ON clientes(ativo);

-- Índices de Produtos
CREATE INDEX idx_produtos_nome ON produtos(nome);
CREATE INDEX idx_produtos_codigo_barras ON produtos(codigo_barras);
CREATE INDEX idx_produtos_categoria ON produtos(id_categoria);
CREATE INDEX idx_produtos_fornecedor ON produtos(id_fornecedor);
CREATE INDEX idx_produtos_ativo ON produtos(ativo);
CREATE INDEX idx_produtos_estoque_atual ON produtos(estoque_atual);

-- Índices de Vendas
CREATE INDEX idx_vendas_numero ON vendas(numero_venda);
CREATE INDEX idx_vendas_cliente ON vendas(id_cliente);
CREATE INDEX idx_vendas_data ON vendas(data_venda);
CREATE INDEX idx_vendas_status ON vendas(status);

-- Índices de Itens de Venda
CREATE INDEX idx_itens_venda_venda ON itens_venda(id_venda);
CREATE INDEX idx_itens_venda_produto ON itens_venda(id_produto);

-- Índices de Movimentações de Estoque
CREATE INDEX idx_movimentacoes_produto ON movimentacoes_estoque(id_produto);
CREATE INDEX idx_movimentacoes_data ON movimentacoes_estoque(data_movimentacao);
CREATE INDEX idx_movimentacoes_tipo ON movimentacoes_estoque(tipo_movimentacao);

-- Índices de Logs
CREATE INDEX idx_logs_data ON sistema_logs(data_registro);
CREATE INDEX idx_logs_nivel ON sistema_logs(nivel);
CREATE INDEX idx_logs_modulo ON sistema_logs(modulo);

-- =====================================================
-- Views para Relatórios
-- =====================================================

-- View de Estoque com Status
CREATE VIEW vw_estoque_status AS
SELECT 
    p.id,
    p.nome,
    p.codigo_barras,
    p.estoque_atual,
    p.estoque_minimo,
    p.preco_venda,
    p.unidade_medida,
    c.nome as categoria_nome,
    f.nome as fornecedor_nome,
    CASE 
        WHEN p.estoque_atual <= 0 THEN 'SEM ESTOQUE'
        WHEN p.estoque_atual <= p.estoque_minimo THEN 'CRÍTICO'
        WHEN p.estoque_atual <= (p.estoque_minimo * 2) THEN 'BAIXO'
        ELSE 'NORMAL'
    END as status_estoque,
    (p.estoque_atual * p.preco_venda) as valor_total_estoque
FROM produtos p
LEFT JOIN categorias c ON p.id_categoria = c.id
LEFT JOIN fornecedores f ON p.id_fornecedor = f.id
WHERE p.ativo = true
ORDER BY 
    CASE 
        WHEN p.estoque_atual <= 0 THEN 1
        WHEN p.estoque_atual <= p.estoque_minimo THEN 2
        WHEN p.estoque_atual <= (p.estoque_minimo * 2) THEN 3
        ELSE 4
    END,
    p.nome;

-- View de Vendas com Detalhes
CREATE VIEW vw_vendas_detalhes AS
SELECT 
    v.id,
    v.numero_venda,
    v.data_venda,
    v.valor_total,
    v.valor_desconto,
    v.valor_final,
    v.forma_pagamento,
    v.status,
    v.usuario_vendedor,
    c.nome as cliente_nome,
    c.cpf_cnpj as cliente_cpf,
    COUNT(iv.id) as quantidade_itens,
    SUM(iv.quantidade) as total_produtos
FROM vendas v
LEFT JOIN clientes c ON v.id_cliente = c.id
LEFT JOIN itens_venda iv ON v.id = iv.id_venda
GROUP BY v.id, v.numero_venda, v.data_venda, v.valor_total, v.valor_desconto, 
         v.valor_final, v.forma_pagamento, v.status, v.usuario_vendedor, 
         c.nome, c.cpf_cnpj
ORDER BY v.data_venda DESC;

-- =====================================================
-- Triggers para Auditoria
-- =====================================================

-- Trigger para atualizar data_ultima_atualizacao
CREATE OR REPLACE FUNCTION atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultima_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger às tabelas relevantes
CREATE TRIGGER trg_usuarios_timestamp 
    BEFORE UPDATE ON usuarios 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

CREATE TRIGGER trg_clientes_timestamp 
    BEFORE UPDATE ON clientes 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

CREATE TRIGGER trg_produtos_timestamp 
    BEFORE UPDATE ON produtos 
    FOR EACH ROW EXECUTE FUNCTION atualizar_timestamp();

-- =====================================================
-- Inserir Dados Iniciais
-- =====================================================

-- Usuários
INSERT INTO usuarios (username, password, nome_completo, email) VALUES
('admin', 'admin123', 'Administrador do Sistema', 'admin@hermes.comercial'),
('vendedor1', 'vendedor123', 'Vendedor Principal', 'vendedor1@hermes.comercial'),
('caixa1', 'caixa123', 'Operador de Caixa', 'caixa1@hermes.comercial'),
('gerente1', 'gerente123', 'Gerente de Loja', 'gerente1@hermes.comercial');

-- Perfis
INSERT INTO perfis (nome, descricao, nivel_acesso) VALUES
('Administrador', 'Acesso completo ao sistema', 4),
('Gerente', 'Acesso gerencial', 3),
('Vendedor', 'Acesso às vendas', 2),
('Caixa', 'Operações de caixa', 1);

-- Permissões
INSERT INTO permissoes (nome, descricao, modulo, acao) VALUES
('USUARIOS_VISUALIZAR', 'Visualizar usuários', 'USUARIOS', 'READ'),
('USUARIOS_CRIAR', 'Criar usuários', 'USUARIOS', 'CREATE'),
('USUARIOS_EDITAR', 'Editar usuários', 'USUARIOS', 'UPDATE'),
('USUARIOS_EXCLUIR', 'Excluir usuários', 'USUARIOS', 'DELETE'),
('PRODUTOS_VISUALIZAR', 'Visualizar produtos', 'PRODUTOS', 'READ'),
('PRODUTOS_CRIAR', 'Criar produtos', 'PRODUTOS', 'CREATE'),
('PRODUTOS_EDITAR', 'Editar produtos', 'PRODUTOS', 'UPDATE'),
('PRODUTOS_EXCLUIR', 'Excluir produtos', 'PRODUTOS', 'DELETE'),
('VENDAS_VISUALIZAR', 'Visualizar vendas', 'VENDAS', 'READ'),
('VENDAS_CRIAR', 'Criar vendas', 'VENDAS', 'CREATE'),
('CLIENTES_VISUALIZAR', 'Visualizar clientes', 'CLIENTES', 'READ'),
('CLIENTES_CRIAR', 'Criar clientes', 'CLIENTES', 'CREATE'),
('CLIENTES_EDITAR', 'Editar clientes', 'CLIENTES', 'UPDATE'),
('CLIENTES_EXCLUIR', 'Excluir clientes', 'CLIENTES', 'DELETE'),
('RELATORIOS_VISUALIZAR', 'Visualizar relatórios', 'RELATORIOS', 'READ'),
('CONFIGURACOES_VISUALIZAR', 'Visualizar configurações', 'CONFIGURACOES', 'READ'),
('CONFIGURACOES_EDITAR', 'Editar configurações', 'CONFIGURACOES', 'UPDATE');

-- Atribuir perfil de Administrador ao usuário admin
INSERT INTO usuario_perfil (usuario_id, perfil_id) 
SELECT u.id, p.id FROM usuarios u, perfis p 
WHERE u.username = 'admin' AND p.nome = 'Administrador';

-- Atribuir todas as permissões ao perfil Administrador
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, perm.id FROM perfis p, permissoes perm
WHERE p.nome = 'Administrador';

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

-- Configurações do Sistema
INSERT INTO configuracoes (chave, valor, tipo, descricao, modulo) VALUES
('NOME_EMPRESA', 'Hermes Comercial PDV', 'STRING', 'Nome da empresa', 'SISTEMA'),
('VERSAO_SISTEMA', '3.0.0', 'STRING', 'Versão atual do sistema', 'SISTEMA'),
('MOEDA_PADRAO', 'BRL', 'STRING', 'Moeda padrão', 'FINANCEIRO'),
('IDIOMA_PADRAO', 'pt_BR', 'STRING', 'Idioma padrão', 'SISTEMA'),
('ESTOQUE_MINIMO_PADRAO', '5', 'NUMBER', 'Estoque mínimo padrão', 'ESTOQUE'),
('LIMITE_CREDITO_CLIENTE', '1000.00', 'DECIMAL', 'Limite de crédito padrão', 'CLIENTES');

COMMIT;
