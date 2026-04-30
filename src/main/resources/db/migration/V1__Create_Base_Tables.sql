-- V1__Create_Base_Tables.sql
-- Criação das tabelas base do sistema
-- Hermes Comercial PDV v2.3.0

-- Criar tabela de produtos
CREATE TABLE IF NOT EXISTS produto (
    codigo VARCHAR(20) PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    estoque INTEGER DEFAULT 0,
    categoria VARCHAR(100),
    observacoes TEXT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar tabela de clientes
CREATE TABLE IF NOT EXISTS cliente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE,
    telefone VARCHAR(20),
    email VARCHAR(100),
    endereco TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Criar tabela de vendas
CREATE TABLE IF NOT EXISTS venda (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente_id INTEGER,
    valor_total DECIMAL(10,2) NOT NULL,
    forma_pagamento VARCHAR(50),
    status VARCHAR(20) DEFAULT 'CONCLUIDA',
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

-- Criar tabela de itens de venda
CREATE TABLE IF NOT EXISTS item_venda (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    venda_id INTEGER NOT NULL,
    produto_codigo VARCHAR(20) NOT NULL,
    quantidade INTEGER NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES venda(id),
    FOREIGN KEY (produto_codigo) REFERENCES produto(codigo)
);

-- Criar tabela de métricas do dashboard
CREATE TABLE IF NOT EXISTS dashboard_metrics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    unidade VARCHAR(50),
    data_referencia DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    categoria VARCHAR(100),
    valor_anterior DECIMAL(15,2),
    meta DECIMAL(15,2),
    descricao TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar tabela de movimentações de caixa
CREATE TABLE IF NOT EXISTS caixa_movimentacoes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tipo VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    descricao TEXT,
    data_movimentacao DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar tabela de notificações
CREATE TABLE IF NOT EXISTS notificacao (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lida BOOLEAN NOT NULL DEFAULT FALSE,
    usuario_destino VARCHAR(100),
    prioridade VARCHAR(50) NOT NULL DEFAULT 'MEDIA'
);

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_produto_categoria ON produto(categoria);
CREATE INDEX IF NOT EXISTS idx_produto_ativo ON produto(ativo);
CREATE INDEX IF NOT EXISTS idx_cliente_cpf ON cliente(cpf_cnpj);
CREATE INDEX IF NOT EXISTS idx_venda_data ON venda(data_venda);
CREATE INDEX IF NOT EXISTS idx_item_venda_venda ON item_venda(venda_id);
CREATE INDEX IF NOT EXISTS idx_dashboard_metrics_data ON dashboard_metrics(data_referencia);
CREATE INDEX IF NOT EXISTS idx_dashboard_metrics_tipo ON dashboard_metrics(tipo);
CREATE INDEX IF NOT EXISTS idx_caixa_movimentacoes_data ON caixa_movimentacoes(data_movimentacao);
CREATE INDEX IF NOT EXISTS idx_caixa_movimentacoes_tipo ON caixa_movimentacoes(tipo);
CREATE INDEX IF NOT EXISTS idx_notificacao_data ON notificacao(data_criacao);
CREATE INDEX IF NOT EXISTS idx_notificacao_usuario ON notificacao(usuario_destino);
CREATE INDEX IF NOT EXISTS idx_notificacao_lida ON notificacao(lida);
