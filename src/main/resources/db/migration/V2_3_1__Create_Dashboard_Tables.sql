-- V2_3_1__Create_Dashboard_Tables.sql
-- Criação das tabelas para Dashboard e Notificações
-- Hermes Comercial PDV v2.3.0

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
CREATE INDEX IF NOT EXISTS idx_dashboard_metrics_data ON dashboard_metrics(data_referencia);
CREATE INDEX IF NOT EXISTS idx_dashboard_metrics_tipo ON dashboard_metrics(tipo);
CREATE INDEX IF NOT EXISTS idx_caixa_movimentacoes_data ON caixa_movimentacoes(data_movimentacao);
CREATE INDEX IF NOT EXISTS idx_caixa_movimentacoes_tipo ON caixa_movimentacoes(tipo);
CREATE INDEX IF NOT EXISTS idx_notificacao_data ON notificacao(data_criacao);
CREATE INDEX IF NOT EXISTS idx_notificacao_usuario ON notificacao(usuario_destino);
CREATE INDEX IF NOT EXISTS idx_notificacao_lida ON notificacao(lida);
