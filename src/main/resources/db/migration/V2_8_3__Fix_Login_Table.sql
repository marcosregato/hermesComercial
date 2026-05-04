-- =====================================================
-- HERMES COMERCIAL - MIGRAÇÃO v2.8.3 (CORREÇÃO URGENTE)
-- Criar tabela login para autenticação funcionar
-- Data: 2026-05-04
-- =====================================================

-- Criar tabela de login que estava faltando
CREATE TABLE IF NOT EXISTS login (
    id SERIAL PRIMARY KEY,
    fk_usuario INTEGER REFERENCES usuario(id) ON DELETE CASCADE,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL, -- Em produção, usar hash
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_acesso TIMESTAMP,
    tentativas_falha INTEGER DEFAULT 0,
    data_bloqueio TIMESTAMP,
    UNIQUE(fk_usuario)
);

-- Inserir dados de login para usuários existentes
INSERT INTO login (fk_usuario, login, senha) VALUES
(1, 'admin', 'admin123'),
(2, 'gerente', 'gerente123'),
(3, 'caixa', 'caixa123'),
(4, 'estoque', 'estoque123')
ON CONFLICT (login) DO NOTHING;

-- Criar índices
CREATE INDEX IF NOT EXISTS idx_login_usuario ON login(fk_usuario);
CREATE INDEX IF NOT EXISTS idx_login_login ON login(login);
CREATE INDEX IF NOT EXISTS idx_login_ativo ON login(ativo);
