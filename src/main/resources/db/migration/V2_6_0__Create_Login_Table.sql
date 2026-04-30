-- =====================================================
-- HERMES COMERCIAL - CRIAÇÃO DA TABELA LOGIN
-- Versão: 2.6.0
-- Data: 2026-04-29
-- =====================================================

-- Criar tabela de login do sistema
CREATE TABLE IF NOT EXISTS login (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    login VARCHAR(50) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    fk_usuario INTEGER NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (fk_usuario) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Inserir login para o usuário administrador
INSERT INTO login (login, senha, fk_usuario) VALUES
('admin', 'admin', 1)
ON CONFLICT (login) DO NOTHING;

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_login_login ON login(login);
CREATE INDEX IF NOT EXISTS idx_login_usuario ON login(fk_usuario);
CREATE INDEX IF NOT EXISTS idx_login_ativo ON login(ativo);
