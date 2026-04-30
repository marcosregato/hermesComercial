-- =====================================================
-- HERMES COMERCIAL - CRIAÇÃO DA TABELA USUARIO
-- Versão: 2.5.3
-- Data: 2026-04-29
-- =====================================================

-- Criar tabela de usuários do sistema
CREATE TABLE IF NOT EXISTS usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(20),
    telefone VARCHAR(20),
    cnpj VARCHAR(20),
    cpf VARCHAR(20),
    email VARCHAR(255),
    tipo_pessoa VARCHAR(20) DEFAULT 'FISICA'
);

-- Inserir usuário administrador padrão
INSERT INTO usuario (nome, email, tipo_pessoa) VALUES
('Administrador', 'admin@hermescomercial.com', 'FISICA');

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_usuario_nome ON usuario(nome);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON usuario(email);
CREATE INDEX IF NOT EXISTS idx_usuario_tipo_pessoa ON usuario(tipo_pessoa);
