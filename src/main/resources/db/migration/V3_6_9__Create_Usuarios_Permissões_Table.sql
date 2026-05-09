-- =====================================================
-- Hermes Comercial v3.6.9 - Migration
-- Criar tabelas de Usuários e Permissões
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nome_usuario VARCHAR(100) NOT NULL,
    login_usuario VARCHAR(50) UNIQUE NOT NULL,
    senha_usuario VARCHAR(255) NOT NULL,
    email_usuario VARCHAR(100),
    cpf_usuario VARCHAR(14),
    rg_usuario VARCHAR(20),
    
    -- Dados Pessoais
    data_nascimento DATE,
    telefone_usuario VARCHAR(20),
    celular_usuario VARCHAR(20),
    endereco_usuario VARCHAR(200),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    cep VARCHAR(9),
    
    -- Dados Profissionais
    cargo_usuario VARCHAR(50),
    departamento VARCHAR(50),
    matricula VARCHAR(20),
    data_admissao DATE,
    data_demissao DATE,
    salario DECIMAL(10,2),
    
    -- Controle de Acesso
    perfil_usuario VARCHAR(20) NOT NULL DEFAULT 'OPERADOR', -- ADMINISTRADOR, GERENTE, OPERADOR, VISUALIZADOR
    status_usuario VARCHAR(20) NOT NULL DEFAULT 'ATIVO', -- ATIVO, INATIVO, BLOQUEADO, SUSPENSO
    nivel_acesso INTEGER DEFAULT 1 CHECK (nivel_acesso >= 1 AND nivel_acesso <= 10),
    
    -- Controle de Sessão
    ultimo_acesso TIMESTAMP,
    ultimo_ip VARCHAR(45),
    sessao_ativa BOOLEAN DEFAULT FALSE,
    token_sessao VARCHAR(255),
    expiracao_token TIMESTAMP,
    
    -- Configurações
    tema_preferencia VARCHAR(20) DEFAULT 'CLARO', -- CLARO, ESCURO, AUTO
    idioma_preferencia VARCHAR(10) DEFAULT 'PT-BR',
    notificacoes_email BOOLEAN DEFAULT TRUE,
    notificacoes_sistema BOOLEAN DEFAULT TRUE,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_perfil_usuario CHECK (perfil_usuario IN ('ADMINISTRADOR', 'GERENTE', 'OPERADOR', 'VISUALIZADOR')),
    CONSTRAINT chk_status_usuario CHECK (status_usuario IN ('ATIVO', 'INATIVO', 'BLOQUEADO', 'SUSPENSO')),
    CONSTRAINT chk_tema_preferencia CHECK (tema_preferencia IN ('CLARO', 'ESCURO', 'AUTO')),
    CONSTRAINT chk_nivel_acesso_usuario CHECK (nivel_acesso >= 1 AND nivel_acesso <= 10),
    CONSTRAINT uq_login_usuario UNIQUE (login_usuario),
    CONSTRAINT uq_email_usuario UNIQUE (email_usuario),
    CONSTRAINT uq_cpf_usuario UNIQUE (cpf_usuario)
);

-- Criar tabela de Perfis de Acesso
CREATE TABLE IF NOT EXISTS perfis_acesso (
    id SERIAL PRIMARY KEY,
    nome_perfil VARCHAR(50) UNIQUE NOT NULL,
    descricao_perfil TEXT,
    nivel_acesso INTEGER NOT NULL CHECK (nivel_acesso >= 1 AND nivel_acesso <= 10),
    
    -- Status
    status_perfil VARCHAR(20) NOT NULL DEFAULT 'ATIVO', -- ATIVO, INATIVO
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_perfil CHECK (status_perfil IN ('ATIVO', 'INATIVO')),
    CONSTRAINT uq_nome_perfil UNIQUE (nome_perfil)
);

-- Criar tabela de Permissões
CREATE TABLE IF NOT EXISTS permissoes (
    id SERIAL PRIMARY KEY,
    nome_permissao VARCHAR(100) UNIQUE NOT NULL,
    descricao_permissao TEXT,
    modulo_permissao VARCHAR(50) NOT NULL, -- VENDAS, PRODUTOS, CLIENTES, ESTOQUE, FINANCEIRO, RELATORIOS, CONFIGURACOES
    tipo_permissao VARCHAR(20) NOT NULL, -- LEITURA, ESCRITA, ALTERACAO, EXCLUSAO, TOTAL
    recurso_permissao VARCHAR(100), -- Recurso específico (ex: NOVA_VENDA, EDITAR_PRODUTO)
    
    -- Status
    status_permissao VARCHAR(20) NOT NULL DEFAULT 'ATIVO', -- ATIVO, INATIVO
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_permissao CHECK (status_permissao IN ('ATIVO', 'INATIVO')),
    CONSTRAINT chk_modulo_permissao CHECK (modulo_permissao IN ('VENDAS', 'PRODUTOS', 'CLIENTES', 'ESTOQUE', 'FINANCEIRO', 'RELATORIOS', 'CONFIGURACOES')),
    CONSTRAINT chk_tipo_permissao CHECK (tipo_permissao IN ('LEITURA', 'ESCRITA', 'ALTERACAO', 'EXCLUSAO', 'TOTAL')),
    CONSTRAINT uq_nome_permissao UNIQUE (nome_permissao)
);

-- Criar tabela de Permissões por Perfil
CREATE TABLE IF NOT EXISTS perfil_permissoes (
    id SERIAL PRIMARY KEY,
    id_perfil INTEGER REFERENCES perfis_acesso(id) ON DELETE CASCADE,
    id_permissao INTEGER REFERENCES permissoes(id) ON DELETE CASCADE,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT uq_perfil_permissao UNIQUE (id_perfil, id_permissao)
);

-- Criar tabela de Permissões Específicas por Usuário
CREATE TABLE IF NOT EXISTS usuario_permissoes (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
    id_permissao INTEGER REFERENCES permissoes(id) ON DELETE CASCADE,
    
    -- Tipo de Concessão
    tipo_concessao VARCHAR(20) NOT NULL, -- CONCEDIDA, NEGADA
    motivo_concessao TEXT,
    
    -- Vigência
    data_inicio_vigencia DATE,
    data_fim_vigencia DATE,
    
    -- Auditoria
    usuario_concessao VARCHAR(50) NOT NULL,
    data_concessao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_concessao CHECK (tipo_concessao IN ('CONCEDIDA', 'NEGADA')),
    CONSTRAINT uq_usuario_permissao UNIQUE (id_usuario, id_permissao)
);

-- Criar tabela de Histórico de Acesso dos Usuários
CREATE TABLE IF NOT EXISTS historico_acesso_usuario (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES usuarios(id) ON DELETE CASCADE,
    
    -- Dados do Acesso
    data_hora_acesso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_acesso VARCHAR(45),
    hostname_acesso VARCHAR(100),
    user_agent VARCHAR(255),
    
    -- Resultado do Acesso
    resultado_acesso VARCHAR(20) NOT NULL, -- SUCESSO, FALHA, BLOQUEADO, EXPIRADO
    motivo_falha VARCHAR(200),
    
    -- Duração da Sessão
    data_logout TIMESTAMP,
    duracao_sessao INTEGER, -- Em minutos
    
    -- Auditoria
    usuario_registro VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_resultado_acesso CHECK (resultado_acesso IN ('SUCESSO', 'FALHA', 'BLOQUEADO', 'EXPIRADO'))
);

-- Criar tabela de Tentativas de Acesso
CREATE TABLE IF NOT EXISTS tentativas_acesso (
    id SERIAL PRIMARY KEY,
    login_tentado VARCHAR(50) NOT NULL,
    ip_tentativa VARCHAR(45),
    data_hora_tentativa TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Resultado
    resultado_tentativa VARCHAR(20) NOT NULL, -- SUCESSO, FALHA, BLOQUEADO
    motivo_falha VARCHAR(200),
    
    -- Controle de Bloqueio
    tentativas_falhas INTEGER DEFAULT 1,
    bloqueado_ate TIMESTAMP,
    
    -- Auditoria
    usuario_registro VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_resultado_tentativa CHECK (resultado_tentativa IN ('SUCESSO', 'FALHA', 'BLOQUEADO'))
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_usuarios_login ON usuarios(login_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_cpf ON usuarios(cpf_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_perfil ON usuarios(perfil_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_status ON usuarios(status_usuario);
CREATE INDEX IF NOT EXISTS idx_usuarios_ultimo_acesso ON usuarios(ultimo_acesso);
CREATE INDEX IF NOT EXISTS idx_usuarios_sessao_ativa ON usuarios(sessao_ativa);

CREATE INDEX IF NOT EXISTS idx_perfis_acesso_nome ON perfis_acesso(nome_perfil);
CREATE INDEX IF NOT EXISTS idx_perfis_acesso_nivel ON perfis_acesso(nivel_acesso);
CREATE INDEX IF NOT EXISTS idx_perfis_acesso_status ON perfis_acesso(status_perfil);

CREATE INDEX IF NOT EXISTS idx_permissoes_nome ON permissoes(nome_permissao);
CREATE INDEX IF NOT EXISTS idx_permissoes_modulo ON permissoes(modulo_permissao);
CREATE INDEX IF NOT EXISTS idx_permissoes_tipo ON permissoes(tipo_permissao);
CREATE INDEX IF NOT EXISTS idx_permissoes_status ON permissoes(status_permissao);

CREATE INDEX IF NOT EXISTS idx_perfil_permissoes_perfil ON perfil_permissoes(id_perfil);
CREATE INDEX IF NOT EXISTS idx_perfil_permissoes_permissao ON perfil_permissoes(id_permissao);

CREATE INDEX IF NOT EXISTS idx_usuario_permissoes_usuario ON usuario_permissoes(id_usuario);
CREATE INDEX IF NOT EXISTS idx_usuario_permissoes_permissao ON usuario_permissoes(id_permissao);
CREATE INDEX IF NOT EXISTS idx_usuario_permissoes_tipo ON usuario_permissoes(tipo_concessao);

CREATE INDEX IF NOT EXISTS idx_historico_acesso_usuario_id ON historico_acesso_usuario(id_usuario);
CREATE INDEX IF NOT EXISTS idx_historico_acesso_data ON historico_acesso_usuario(data_hora_acesso);
CREATE INDEX IF NOT EXISTS idx_historico_acesso_resultado ON historico_acesso_usuario(resultado_acesso);

CREATE INDEX IF NOT EXISTS idx_tentativas_acesso_login ON tentativas_acesso(login_tentado);
CREATE INDEX IF NOT EXISTS idx_tentativas_acesso_ip ON tentativas_acesso(ip_tentativa);
CREATE INDEX IF NOT EXISTS idx_tentativas_acesso_data ON tentativas_acesso(data_hora_tentativa);
CREATE INDEX IF NOT EXISTS idx_tentativas_acesso_resultado ON tentativas_acesso(resultado_tentativa);

-- Inserir dados padrão de perfis
INSERT INTO perfis_acesso (nome_perfil, descricao_perfil, nivel_acesso) VALUES
('ADMINISTRADOR', 'Acesso total ao sistema com todas as permissões', 10),
('GERENTE', 'Acesso gerencial com permissões de supervisão', 7),
('OPERADOR', 'Acesso operacional para funções do dia a dia', 5),
('VISUALIZADOR', 'Acesso apenas para consulta e visualização', 1)
ON CONFLICT DO NOTHING;

-- Inserir permissões básicas do sistema
INSERT INTO permissoes (nome_permissao, descricao_permissao, modulo_permissao, tipo_permissao, recurso_permissao) VALUES
-- Vendas
('ACESSAR_VENDAS', 'Acessar módulo de vendas', 'VENDAS', 'LEITURA', 'MODULO_VENDAS'),
('NOVA_VENDA', 'Criar novas vendas', 'VENDAS', 'ESCRITA', 'NOVA_VENDA'),
('EDITAR_VENDA', 'Editar vendas existentes', 'VENDAS', 'ALTERACAO', 'EDITAR_VENDA'),
('EXCLUIR_VENDA', 'Excluir vendas', 'VENDAS', 'EXCLUSAO', 'EXCLUIR_VENDA'),
('RELATORIO_VENDAS', 'Gerar relatórios de vendas', 'VENDAS', 'LEITURA', 'RELATORIO_VENDAS'),

-- Produtos
('ACESSAR_PRODUTOS', 'Acessar módulo de produtos', 'PRODUTOS', 'LEITURA', 'MODULO_PRODUTOS'),
('CADASTRAR_PRODUTO', 'Cadastrar novos produtos', 'PRODUTOS', 'ESCRITA', 'CADASTRAR_PRODUTO'),
('EDITAR_PRODUTO', 'Editar produtos existentes', 'PRODUTOS', 'ALTERACAO', 'EDITAR_PRODUTO'),
('EXCLUIR_PRODUTO', 'Excluir produtos', 'PRODUTOS', 'EXCLUSAO', 'EXCLUIR_PRODUTO'),
('GERENCIAR_ESTOQUE', 'Gerenciar controle de estoque', 'PRODUTOS', 'ALTERACAO', 'GERENCIAR_ESTOQUE'),

-- Clientes
('ACESSAR_CLIENTES', 'Acessar módulo de clientes', 'CLIENTES', 'LEITURA', 'MODULO_CLIENTES'),
('CADASTRAR_CLIENTE', 'Cadastrar novos clientes', 'CLIENTES', 'ESCRITA', 'CADASTRAR_CLIENTE'),
('EDITAR_CLIENTE', 'Editar clientes existentes', 'CLIENTES', 'ALTERACAO', 'EDITAR_CLIENTE'),
('EXCLUIR_CLIENTE', 'Excluir clientes', 'CLIENTES', 'EXCLUSAO', 'EXCLUIR_CLIENTE'),

-- Estoque
('ACESSAR_ESTOQUE', 'Acessar módulo de estoque', 'ESTOQUE', 'LEITURA', 'MODULO_ESTOQUE'),
('MOVIMENTAR_ESTOQUE', 'Movimentar estoque', 'ESTOQUE', 'ALTERACAO', 'MOVIMENTAR_ESTOQUE'),
('TRANSFERIR_ESTOQUE', 'Transferir produtos entre locais', 'ESTOQUE', 'ALTERACAO', 'TRANSFERIR_ESTOQUE'),
('INVENTARIAR_ESTOQUE', 'Realizar inventário de estoque', 'ESTOQUE', 'ALTERACAO', 'INVENTARIAR_ESTOQUE'),

-- Financeiro
('ACESSAR_FINANCEIRO', 'Acessar módulo financeiro', 'FINANCEIRO', 'LEITURA', 'MODULO_FINANCEIRO'),
('GERENCIAR_CONTAS', 'Gerenciar contas a pagar e receber', 'FINANCEIRO', 'ALTERACAO', 'GERENCIAR_CONTAS'),
('FECHAR_CAIXA', 'Fechar caixa do dia', 'FINANCEIRO', 'ALTERACAO', 'FECHAR_CAIXA'),
('RELATORIO_FINANCEIRO', 'Gerar relatórios financeiros', 'FINANCEIRO', 'LEITURA', 'RELATORIO_FINANCEIRO'),

-- Relatórios
('ACESSAR_RELATORIOS', 'Acessar módulo de relatórios', 'RELATORIOS', 'LEITURA', 'MODULO_RELATORIOS'),
('GERAR_RELATORIOS', 'Gerar relatórios diversos', 'RELATORIOS', 'ESCRITA', 'GERAR_RELATORIOS'),
('EXPORTAR_DADOS', 'Exportar dados do sistema', 'RELATORIOS', 'ESCRITA', 'EXPORTAR_DADOS'),

-- Configurações
('ACESSAR_CONFIGURACOES', 'Acessar módulo de configurações', 'CONFIGURACOES', 'LEITURA', 'MODULO_CONFIGURACOES'),
('GERENCIAR_USUARIOS', 'Gerenciar usuários e permissões', 'CONFIGURACOES', 'ALTERACAO', 'GERENCIAR_USUARIOS'),
('CONFIGURAR_SISTEMA', 'Configurar parâmetros do sistema', 'CONFIGURACOES', 'ALTERACAO', 'CONFIGURAR_SISTEMA'),
('BACKUP_SISTEMA', 'Realizar backup do sistema', 'CONFIGURACOES', 'ESCRITA', 'BACKUP_SISTEMA')
ON CONFLICT DO NOTHING;

-- Associar permissões aos perfis
-- Administrador tem todas as permissões
INSERT INTO perfil_permissoes (id_perfil, id_permissao)
SELECT p.id, perm.id 
FROM perfis_acesso p, permissoes perm 
WHERE p.nome_perfil = 'ADMINISTRADOR'
ON CONFLICT DO NOTHING;

-- Gerente tem a maioria das permissões (exceto configurações críticas)
INSERT INTO perfil_permissoes (id_perfil, id_permissao)
SELECT p.id, perm.id 
FROM perfis_acesso p, permissoes perm 
WHERE p.nome_perfil = 'GERENTE' 
AND perm.recurso_permissao NOT IN ('GERENCIAR_USUARIOS', 'CONFIGURAR_SISTEMA', 'BACKUP_SISTEMA')
ON CONFLICT DO NOTHING;

-- Operador tem permissões operacionais
INSERT INTO perfil_permissoes (id_perfil, id_permissao)
SELECT p.id, perm.id 
FROM perfis_acesso p, permissoes perm 
WHERE p.nome_perfil = 'OPERADOR' 
AND perm.recurso_permissao IN (
    'ACESSAR_VENDAS', 'NOVA_VENDA', 'EDITAR_VENDA', 'RELATORIO_VENDAS',
    'ACESSAR_PRODUTOS', 'GERENCIAR_ESTOQUE',
    'ACESSAR_CLIENTES', 'CADASTRAR_CLIENTE', 'EDITAR_CLIENTE',
    'ACESSAR_ESTOQUE', 'MOVIMENTAR_ESTOQUE',
    'ACESSAR_FINANCEIRO', 'GERENCIAR_CONTAS', 'FECHAR_CAIXA',
    'ACESSAR_RELATORIOS', 'GERAR_RELATORIOS'
)
ON CONFLICT DO NOTHING;

-- Visualizador tem apenas acesso de leitura
INSERT INTO perfil_permissoes (id_perfil, id_permissao)
SELECT p.id, perm.id 
FROM perfis_acesso p, permissoes perm 
WHERE p.nome_perfil = 'VISUALIZADOR' 
AND perm.tipo_permissao = 'LEITURA'
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE usuarios IS 'Tabela de usuários do sistema';
COMMENT ON COLUMN usuarios.id IS 'ID único do usuário';
COMMENT ON COLUMN usuarios.nome_usuario IS 'Nome completo do usuário';
COMMENT ON COLUMN usuarios.login_usuario IS 'Login de acesso ao sistema';
COMMENT ON COLUMN usuarios.senha_usuario IS 'Senha criptografada do usuário';
COMMENT ON COLUMN usuarios.email_usuario IS 'Email do usuário';
COMMENT ON COLUMN usuarios.cpf_usuario IS 'CPF do usuário';
COMMENT ON COLUMN usuarios.perfil_usuario IS 'Perfil de acesso do usuário';
COMMENT ON COLUMN usuarios.status_usuario IS 'Status do usuário';
COMMENT ON COLUMN usuarios.nivel_acesso IS 'Nível de acesso do usuário (1-10)';
COMMENT ON COLUMN usuarios.ultimo_acesso IS 'Data e hora do último acesso';
COMMENT ON COLUMN usuarios.sessao_ativa IS 'Indica se o usuário tem sessão ativa';
COMMENT ON COLUMN usuarios.token_sessao IS 'Token da sessão ativa';
COMMENT ON COLUMN usuarios.tema_preferencia IS 'Tema de preferência do usuário';
COMMENT ON COLUMN usuarios.idioma_preferencia IS 'Idioma de preferência do usuário';

COMMENT ON TABLE perfis_acesso IS 'Tabela de perfis de acesso do sistema';
COMMENT ON COLUMN perfis_acesso.id IS 'ID único do perfil';
COMMENT ON COLUMN perfis_acesso.nome_perfil IS 'Nome do perfil de acesso';
COMMENT ON COLUMN perfis_acesso.descricao_perfil IS 'Descrição detalhada do perfil';
COMMENT ON COLUMN perfis_acesso.nivel_acesso IS 'Nível de acesso do perfil (1-10)';

COMMENT ON TABLE permissoes IS 'Tabela de permissões do sistema';
COMMENT ON COLUMN permissoes.id IS 'ID único da permissão';
COMMENT ON COLUMN permissoes.nome_permissao IS 'Nome da permissão';
COMMENT ON COLUMN permissoes.descricao_permissao IS 'Descrição detalhada da permissão';
COMMENT ON COLUMN permissoes.modulo_permissao IS 'Módulo ao qual a permissão pertence';
COMMENT ON COLUMN permissoes.tipo_permissao IS 'Tipo da permissão';
COMMENT ON COLUMN permissoes.recurso_permissao IS 'Recurso específico da permissão';

COMMENT ON TABLE perfil_permissoes IS 'Tabela de associação entre perfis e permissões';
COMMENT ON TABLE usuario_permissoes IS 'Tabela de permissões específicas por usuário';
COMMENT ON TABLE historico_acesso_usuario IS 'Histórico de acessos dos usuários';
COMMENT ON TABLE tentativas_acesso IS 'Tabela de tentativas de acesso ao sistema';
