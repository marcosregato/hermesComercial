-- =====================================================
-- Hermes Comercial v3.6.15 - Migration
-- Criar tabela de Parâmetros do Sistema
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Parâmetros
CREATE TABLE IF NOT EXISTS parametros (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_parametro VARCHAR(100) NOT NULL UNIQUE,
    descricao_parametro TEXT,
    codigo_parametro VARCHAR(50) UNIQUE,
    categoria_parametro VARCHAR(50) NOT NULL, -- SISTEMA, PDV, FINANCEIRO, ESTOQUE, VENDAS, CLIENTES, PRODUTOS, IMPRESSAO, BACKUP, SEGURANCA
    subcategoria_parametro VARCHAR(50), -- GERAL, INTERFACE, BANCO_DADOS, EMAIL, LOG, PERFORMANCE, INTEGRACAO, NOTIFICACAO
    
    -- Configurações Gerais
    ativo BOOLEAN DEFAULT TRUE,
    visivel BOOLEAN DEFAULT TRUE,
    editavel BOOLEAN DEFAULT TRUE,
    obrigatorio BOOLEAN DEFAULT FALSE,
    ordem_exibicao INTEGER DEFAULT 0,
    icone_parametro VARCHAR(50),
    
    -- Tipo e Formato
    tipo_parametro VARCHAR(20) NOT NULL, -- STRING, INTEGER, DECIMAL, BOOLEAN, DATE, TIME, DATETIME, JSON, XML, FILE, EMAIL, URL, PASSWORD
    formato_parametro VARCHAR(100), -- Formato específico (ex: DD/MM/YYYY, ##0.00, etc.)
    mascara_parametro VARCHAR(50), -- Máscara para entrada de dados
    valor_padrao TEXT,
    
    -- Valores Possíveis
    valores_possiveis TEXT, -- Valores separados por vírgula para campos do tipo SELECT
    opcoes_combo TEXT, -- Opções em formato JSON para campos complexos
    dominio_valores TEXT, -- Domínio de valores permitidos
    
    -- Validações
    valor_minimo DECIMAL(20,8),
    valor_maximo DECIMAL(20,8),
    tamanho_minimo INTEGER,
    tamanho_maximo INTEGER,
    expressao_validacao VARCHAR(500), -- Expressão regular para validação
    mensagem_erro_validacao VARCHAR(500),
    
    -- Configurações de Interface
    label_parametro VARCHAR(200),
    placeholder_parametro VARCHAR(200),
    tooltip_ajuda TEXT,
    grupo_interface VARCHAR(50), -- Grupo visual na interface
    aba_interface VARCHAR(50), -- Aba onde o parâmetro aparece
    
    -- Configurações de Sistema
    requer_reinicializacao BOOLEAN DEFAULT FALSE, -- Requer reinicialização do sistema
    afeta_performance BOOLEAN DEFAULT FALSE, -- Afeta performance do sistema
    parametro_critico BOOLEAN DEFAULT FALSE, -- Parâmetro crítico do sistema
    backup_automatico BOOLEAN DEFAULT TRUE, -- Backup automático do valor
    
    -- Configurações de Acesso
    nivel_acesso VARCHAR(20) DEFAULT 'ADMIN', -- ADMIN, MANAGER, USER, READONLY
    perfil_acesso TEXT, -- Perfis que podem acessar (separados por vírgula)
    departamento_acesso TEXT, -- Departamentos que podem acessar
    usuario_cadastro VARCHAR(50),
    usuario_ultima_atualizacao VARCHAR(50),
    
    -- Configurações de Versão
    versao_parametro VARCHAR(20), -- Versão em que foi criado/modificado
    compatibilidade_versao VARCHAR(100), -- Versões compatíveis
    depreciado BOOLEAN DEFAULT FALSE,
    data_depreciacao DATE,
    motivo_depreciacao TEXT,
    
    -- Configurações de Ambiente
    ambiente_parametro VARCHAR(20) DEFAULT 'TODOS', -- PRODUCAO, HOMOLOGACAO, DESENVOLVIMENTO, TESTE, TODOS
    valor_producao TEXT,
    valor_homologacao TEXT,
    valor_desenvolvimento TEXT,
    valor_teste TEXT,
    
    -- Configurações de Integração
    integracao_externa BOOLEAN DEFAULT FALSE,
    nome_integracao VARCHAR(100),
    endpoint_integracao VARCHAR(500),
    metodo_sincronizacao VARCHAR(20), -- AUTOMATICO, MANUAL, AGENDADO
    intervalo_sincronizacao INTEGER, -- minutos
    
    -- Configurações de Log
    log_alteracoes BOOLEAN DEFAULT TRUE,
    log_leituras BOOLEAN DEFAULT FALSE,
    nivel_log VARCHAR(20) DEFAULT 'INFO', -- DEBUG, INFO, WARN, ERROR, FATAL
    categoria_log VARCHAR(50) DEFAULT 'PARAMETRO',
    
    -- Configurações de Cache
    cache_ativo BOOLEAN DEFAULT FALSE,
    cache_ttl INTEGER DEFAULT 300, -- segundos
    cache_chave VARCHAR(200),
    
    -- Configurações de Validação de Negócio
    regra_negocio TEXT, -- Regra de negócio em formato JSON
    validacao_customizada TEXT, -- Validação customizada
    script_validacao TEXT, -- Script de validação (JavaScript, Groovy, etc.)
    
    -- Configurações de Transformação
    transformacao_dados BOOLEAN DEFAULT FALSE,
    script_transformacao TEXT, -- Script de transformação
    formato_saida VARCHAR(50), -- Formato de saída após transformação
    
    -- Configurações de Segurança
    criptografado BOOLEAN DEFAULT FALSE,
    algoritmo_criptografia VARCHAR(50), -- AES, RSA, etc.
    chave_criptografia VARCHAR(500),
    mascara_exibicao VARCHAR(50), -- Máscara para exibição de valores sensíveis
    
    -- Configurações de Auditoria
    auditoria_ativa BOOLEAN DEFAULT TRUE,
    campos_auditoria TEXT, -- Campos que devem ser auditados
    evento_auditoria VARCHAR(100), -- Evento que dispara auditoria
    
    -- Configurações de Notificação
    notificacao_alteracao BOOLEAN DEFAULT FALSE,
    canais_notificacao TEXT, -- EMAIL, SMS, PUSH, WEBHOOK
    template_notificacao TEXT,
    destinatarios_notificacao TEXT,
    
    -- Configurações de Dependência
    depende_de_parametro VARCHAR(100), -- Nome do parâmetro dependente
    condicao_dependencia TEXT, -- Condição para habilitar este parâmetro
    parametros_relacionados TEXT, -- Parâmetros relacionados (separados por vírgula)
    
    -- Configurações de Performance
    indice_performance BOOLEAN DEFAULT FALSE,
    metrica_associada VARCHAR(100),
    impacto_performance VARCHAR(20), -- BAIXO, MEDIO, ALTO, CRITICO
    otimizacao_recomendada TEXT,
    
    -- Valores Históricos
    valor_anterior TEXT,
    data_ultima_alteracao TIMESTAMP,
    motivo_alteracao TEXT,
    
    -- Status e Controle
    status_parametro VARCHAR(20) DEFAULT 'ATIVO', -- ATIVO, INATIVO, BLOQUEADO, EM_REVISAO
    motivo_status TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_parametro CHECK (tipo_parametro IN ('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'DATE', 'TIME', 'DATETIME', 'JSON', 'XML', 'FILE', 'EMAIL', 'URL', 'PASSWORD')),
    CONSTRAINT chk_nivel_acesso CHECK (nivel_acesso IN ('ADMIN', 'MANAGER', 'USER', 'READONLY')),
    CONSTRAINT chk_ambiente_parametro CHECK (ambiente_parametro IN ('PRODUCAO', 'HOMOLOGACAO', 'DESENVOLVIMENTO', 'TESTE', 'TODOS')),
    CONSTRAINT chk_metodo_sincronizacao CHECK (metodo_sincronizacao IN ('AUTOMATICO', 'MANUAL', 'AGENDADO')),
    CONSTRAINT chk_nivel_log CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    CONSTRAINT chk_status_parametro CHECK (status_parametro IN ('ATIVO', 'INATIVO', 'BLOQUEADO', 'EM_REVISAO')),
    CONSTRAINT chk_impacto_performance CHECK (impacto_performance IN ('BAIXO', 'MEDIO', 'ALTO', 'CRITICO')),
    CONSTRAINT chk_limites_parametros CHECK (
        valor_minimo IS NULL OR valor_maximo IS NULL OR valor_minimo <= valor_maximo AND
        tamanho_minimo IS NULL OR tamanho_maximo IS NULL OR tamanho_minimo <= tamanho_maximo AND
        tamanho_minimo IS NULL OR tamanho_minimo >= 0 AND
        tamanho_maximo IS NULL OR tamanho_maximo >= 0 AND
        intervalo_sincronizacao IS NULL OR intervalo_sincronizacao > 0 AND
        cache_ttl IS NULL OR cache_ttl > 0 AND
        ordem_exibicao >= 0
    )
);

-- Criar tabela de Categorias de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_categorias (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_categoria VARCHAR(50) NOT NULL UNIQUE,
    descricao_categoria TEXT,
    codigo_categoria VARCHAR(20) UNIQUE,
    
    -- Configurações de Interface
    icone_categoria VARCHAR(50),
    cor_categoria VARCHAR(7) DEFAULT '#007BFF',
    ordem_exibicao INTEGER DEFAULT 0,
    
    -- Configurações de Acesso
    nivel_acesso_minimo VARCHAR(20) DEFAULT 'USER', -- ADMIN, MANAGER, USER, READONLY
    visivel BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Sistema
    requer_reinicializacao BOOLEAN DEFAULT FALSE,
    modulo_sistema VARCHAR(50), -- Módulo do sistema ao qual pertence
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_nivel_acesso_minimo CHECK (nivel_acesso_minimo IN ('ADMIN', 'MANAGER', 'USER', 'READONLY')),
    CONSTRAINT chk_ordem_exibicao_categoria CHECK (ordem_exibicao >= 0)
);

-- Criar tabela de Histórico de Alterações de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_historico (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_parametro INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE,
    
    -- Dados da Alteração
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_alteracao VARCHAR(20) NOT NULL, -- CRIACAO, ATUALIZACAO, EXCLUSAO, RESTAURACAO
    usuario_alteracao VARCHAR(50),
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(255),
    
    -- Valores Antes e Depois
    valor_anterior TEXT,
    valor_novo TEXT,
    campo_alterado VARCHAR(100), -- Campo específico que foi alterado
    
    -- Contexto da Alteração
    motivo_alteracao TEXT,
    origem_alteracao VARCHAR(50), -- INTERFACE, API, BATCH, IMPORTACAO, MIGRACAO
    modulo_origem VARCHAR(50),
    funcionalidade_origem VARCHAR(100),
    
    -- Detalhes Técnicos
    versao_sistema VARCHAR(20),
    ambiente_sistema VARCHAR(20),
    sessao_usuario VARCHAR(100),
    
    -- Metadados
    metadados_json TEXT, -- Metadados adicionais em formato JSON
    tags_alteracao TEXT, -- Tags para classificação
    
    -- Auditoria
    transacao_id VARCHAR(100),
    batch_id VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_tipo_alteracao CHECK (tipo_alteracao IN ('CRIACAO', 'ATUALIZACAO', 'EXCLUSAO', 'RESTAURACAO')),
    CONSTRAINT chk_origem_alteracao CHECK (origem_alteracao IN ('INTERFACE', 'API', 'BATCH', 'IMPORTACAO', 'MIGRACAO'))
);

-- Criar tabela de Validações de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_validacoes (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_parametro INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE,
    
    -- Dados da Validação
    nome_validacao VARCHAR(100) NOT NULL,
    descricao_validacao TEXT,
    tipo_validacao VARCHAR(20) NOT NULL, -- REQUISITO, FORMATO, DOMINIO, CUSTOMIZADA, REGEX, SCRIPT
    
    -- Configurações da Validação
    expressao_validacao VARCHAR(1000), -- Expressão de validação
    mensagem_erro VARCHAR(500),
    severidade_erro VARCHAR(20) DEFAULT 'ERRO', -- INFO, AVISO, ERRO, FATAL
    
    -- Condições de Aplicação
    condicao_aplicacao TEXT, -- Condição para aplicar esta validação
    parametros_condicao TEXT, -- Parâmetros para a condição
    
    -- Configurações de Interface
    exibir_mensagem_interface BOOLEAN DEFAULT TRUE,
    estilo_mensagem VARCHAR(50), -- Estilo CSS da mensagem
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    ordem_execucao INTEGER DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_validacao CHECK (tipo_validacao IN ('REQUISITO', 'FORMATO', 'DOMINIO', 'CUSTOMIZADA', 'REGEX', 'SCRIPT')),
    CONSTRAINT chk_severidade_erro CHECK (severidade_erro IN ('INFO', 'AVISO', 'ERRO', 'FATAL')),
    CONSTRAINT chk_ordem_execucao_validacao CHECK (ordem_execucao >= 0),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_parametro, nome_validacao)
);

-- Criar tabela de Dependências de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_dependencias (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamentos
    id_parametro_principal INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE,
    id_parametro_dependente INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE,
    
    -- Configurações da Dependência
    tipo_dependencia VARCHAR(20) NOT NULL, -- SIMPLES, COMPOSTA, CONDICIONAL, CIRCULAR
    condicao_dependencia TEXT, -- Condição para ativar a dependência
    valor_condicional TEXT, -- Valor que ativa a dependência
    
    -- Configurações de Comportamento
    comportamento_dependencia VARCHAR(50), -- O que acontece quando a dependência é ativada
    acao_dependencia VARCHAR(20), -- HABILITAR, DESABILITAR, EXIBIR, OCULTAR, REQUERER
    
    -- Configurações de Interface
    mensagem_interface TEXT, -- Mensagem exibida na interface
    estilo_interface VARCHAR(50), -- Estilo CSS para destacar dependência
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_dependencia CHECK (tipo_dependencia IN ('SIMPLES', 'COMPOSTA', 'CONDICIONAL', 'CIRCULAR')),
    CONSTRAINT chk_acao_dependencia CHECK (acao_dependencia IN ('HABILITAR', 'DESABILITAR', 'EXIBIR', 'OCULTAR', 'REQUERER')),
    
    -- Constraint para evitar dependência circular
    CHECK (id_parametro_principal != id_parametro_dependente),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_parametro_principal, id_parametro_dependente)
);

-- Criar tabela de Grupos de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_grupos (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_grupo VARCHAR(100) NOT NULL UNIQUE,
    descricao_grupo TEXT,
    codigo_grupo VARCHAR(20) UNIQUE,
    
    -- Configurações de Interface
    icone_grupo VARCHAR(50),
    cor_grupo VARCHAR(7) DEFAULT '#28A745',
    ordem_exibicao INTEGER DEFAULT 0,
    layout_grupo VARCHAR(20) DEFAULT 'GRID', -- GRID, TAB, ACCORDION, WIZARD
    
    -- Configurações de Acesso
    nivel_acesso_minimo VARCHAR(20) DEFAULT 'USER',
    visivel BOOLEAN DEFAULT TRUE,
    expansivel BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Sistema
    modulo_sistema VARCHAR(50),
    requer_reinicializacao BOOLEAN DEFAULT FALSE,
    
    -- Status e Controle
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_nivel_acesso_grupo CHECK (nivel_acesso_minimo IN ('ADMIN', 'MANAGER', 'USER', 'READONLY')),
    CONSTRAINT chk_layout_grupo CHECK (layout_grupo IN ('GRID', 'TAB', 'ACCORDION', 'WIZARD')),
    CONSTRAINT chk_ordem_exibicao_grupo CHECK (ordem_exibicao >= 0)
);

-- Criar tabela de Associação Parâmetro-Grupo
CREATE TABLE IF NOT EXISTS parametros_grupo_associacao (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamentos
    id_grupo INTEGER NOT NULL REFERENCES parametros_grupos(id) ON DELETE CASCADE,
    id_parametro INTEGER NOT NULL REFERENCES parametros(id) ON DELETE CASCADE,
    
    -- Configurações da Associação
    ordem_no_grupo INTEGER DEFAULT 0,
    visivel_no_grupo BOOLEAN DEFAULT TRUE,
    obrigatorio_no_grupo BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Interface
    label_personalizado VARCHAR(200),
    tooltip_personalizado TEXT,
    estilo_personalizado VARCHAR(500), -- Estilo CSS personalizado
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_ordem_no_grupo CHECK (ordem_no_grupo >= 0),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_grupo, id_parametro)
);

-- Criar tabela de Templates de Parâmetros
CREATE TABLE IF NOT EXISTS parametros_templates (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_template VARCHAR(100) NOT NULL UNIQUE,
    descricao_template TEXT,
    categoria_template VARCHAR(50),
    
    -- Configurações do Template
    tipo_template VARCHAR(20) NOT NULL, -- VALOR, LISTA, JSON, XML, SCRIPT
    conteudo_template TEXT NOT NULL,
    variaveis_template TEXT, -- Variáveis do template no formato {VARIAVEL}
    
    -- Configurações de Aplicação
    parametros_aplicaveis TEXT, -- Parâmetros onde este template pode ser aplicado
    condicao_aplicacao TEXT, -- Condição para aplicar o template
    
    -- Configurações de Formato
    formato_saida VARCHAR(50),
    encoding_saida VARCHAR(20) DEFAULT 'UTF-8',
    
    -- Status e Controle
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_template CHECK (tipo_template IN ('VALOR', 'LISTA', 'JSON', 'XML', 'SCRIPT'))
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_parametros_nome_parametro ON parametros(nome_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_codigo_parametro ON parametros(codigo_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_categoria_parametro ON parametros(categoria_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_subcategoria_parametro ON parametros(subcategoria_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_ativo ON parametros(ativo);
CREATE INDEX IF NOT EXISTS idx_parametros_visivel ON parametros(visivel);
CREATE INDEX IF NOT EXISTS idx_parametros_editavel ON parametros(editavel);
CREATE INDEX IF NOT EXISTS idx_parametros_tipo_parametro ON parametros(tipo_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_nivel_acesso ON parametros(nivel_acesso);
CREATE INDEX IF NOT EXISTS idx_parametros_ambiente_parametro ON parametros(ambiente_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_status_parametro ON parametros(status_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_ordem_exibicao ON parametros(ordem_exibicao);
CREATE INDEX IF NOT EXISTS idx_parametros_grupo_interface ON parametros(grupo_interface);
CREATE INDEX IF NOT EXISTS idx_parametros_aba_interface ON parametros(aba_interface);
CREATE INDEX IF NOT EXISTS idx_parametros_data_ultima_atualizacao ON parametros(data_ultima_atualizacao);

CREATE INDEX IF NOT EXISTS idx_parametros_categorias_nome_categoria ON parametros_categorias(nome_categoria);
CREATE INDEX IF NOT EXISTS idx_parametros_categorias_codigo_categoria ON parametros_categorias(codigo_categoria);
CREATE INDEX IF NOT EXISTS idx_parametros_categorias_ativa ON parametros_categorias(ativa);
CREATE INDEX IF NOT EXISTS idx_parametros_categorias_visivel ON parametros_categorias(visivel);
CREATE INDEX IF NOT EXISTS idx_parametros_categorias_modulo_sistema ON parametros_categorias(modulo_sistema);
CREATE INDEX IF NOT EXISTS idx_parametros_categorias_ordem_exibicao ON parametros_categorias(ordem_exibicao);

CREATE INDEX IF NOT EXISTS idx_parametros_historico_id_parametro ON parametros_historico(id_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_historico_data_alteracao ON parametros_historico(data_alteracao);
CREATE INDEX IF NOT EXISTS idx_parametros_historico_tipo_alteracao ON parametros_historico(tipo_alteracao);
CREATE INDEX IF NOT EXISTS idx_parametros_historico_usuario_alteracao ON parametros_historico(usuario_alteracao);
CREATE INDEX IF NOT EXISTS idx_parametros_historico_origem_alteracao ON parametros_historico(origem_alteracao);
CREATE INDEX IF NOT EXISTS idx_parametros_historico_transacao_id ON parametros_historico(transacao_id);

CREATE INDEX IF NOT EXISTS idx_parametros_validacoes_id_parametro ON parametros_validacoes(id_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_validacoes_nome_validacao ON parametros_validacoes(nome_validacao);
CREATE INDEX IF NOT EXISTS idx_parametros_validacoes_tipo_validacao ON parametros_validacoes(tipo_validacao);
CREATE INDEX IF NOT EXISTS idx_parametros_validacoes_ativa ON parametros_validacoes(ativa);
CREATE INDEX IF NOT EXISTS idx_parametros_validacoes_ordem_execucao ON parametros_validacoes(ordem_execucao);

CREATE INDEX IF NOT EXISTS idx_parametros_dependencias_id_parametro_principal ON parametros_dependencias(id_parametro_principal);
CREATE INDEX IF NOT EXISTS idx_parametros_dependencias_id_parametro_dependente ON parametros_dependencias(id_parametro_dependente);
CREATE INDEX IF NOT EXISTS idx_parametros_dependencias_tipo_dependencia ON parametros_dependencias(tipo_dependencia);
CREATE INDEX IF NOT EXISTS idx_parametros_dependencias_ativa ON parametros_dependencias(ativa);

CREATE INDEX IF NOT EXISTS idx_parametros_grupos_nome_grupo ON parametros_grupos(nome_grupo);
CREATE INDEX IF NOT EXISTS idx_parametros_grupos_codigo_grupo ON parametros_grupos(codigo_grupo);
CREATE INDEX IF NOT EXISTS idx_parametros_grupos_ativo ON parametros_grupos(ativo);
CREATE INDEX IF NOT EXISTS idx_parametros_grupos_visivel ON parametros_grupos(visivel);
CREATE INDEX IF NOT EXISTS idx_parametros_grupos_modulo_sistema ON parametros_grupos(modulo_sistema);
CREATE INDEX IF NOT EXISTS idx_parametros_grupos_ordem_exibicao ON parametros_grupos(ordem_exibicao);

CREATE INDEX IF NOT EXISTS idx_parametros_grupo_associacao_id_grupo ON parametros_grupo_associacao(id_grupo);
CREATE INDEX IF NOT EXISTS idx_parametros_grupo_associacao_id_parametro ON parametros_grupo_associacao(id_parametro);
CREATE INDEX IF NOT EXISTS idx_parametros_grupo_associacao_ativa ON parametros_grupo_associacao(ativa);
CREATE INDEX IF NOT EXISTS idx_parametros_grupo_associacao_ordem_no_grupo ON parametros_grupo_associacao(ordem_no_grupo);

CREATE INDEX IF NOT EXISTS idx_parametros_templates_nome_template ON parametros_templates(nome_template);
CREATE INDEX IF NOT EXISTS idx_parametros_templates_categoria_template ON parametros_templates(categoria_template);
CREATE INDEX IF NOT EXISTS idx_parametros_templates_tipo_template ON parametros_templates(tipo_template);
CREATE INDEX IF NOT EXISTS idx_parametros_templates_ativo ON parametros_templates(ativo);

-- Inserir categorias de parâmetros padrão
INSERT INTO parametros_categorias (
    nome_categoria, descricao_categoria, codigo_categoria, icone_categoria, cor_categoria, 
    ordem_exibicao, nivel_acesso_minimo, visivel, modulo_sistema
) VALUES 
('SISTEMA', 'Configurações gerais do sistema', 'SISTEMA', 'cogs', '#007BFF', 1, 'ADMIN', TRUE, 'CORE'),
('PDV', 'Configurações do Ponto de Venda', 'PDV', 'cash-register', '#28A745', 2, 'MANAGER', TRUE, 'PDV'),
('FINANCEIRO', 'Configurações financeiras e contábeis', 'FINANCEIRO', 'dollar-sign', '#FFC107', 3, 'ADMIN', TRUE, 'FINANCEIRO'),
('ESTOQUE', 'Configurações de controle de estoque', 'ESTOQUE', 'boxes', '#17A2B8', 4, 'MANAGER', TRUE, 'ESTOQUE'),
('VENDAS', 'Configurações de vendas e pedidos', 'VENDAS', 'shopping-cart', '#DC3545', 5, 'MANAGER', TRUE, 'VENDAS'),
('CLIENTES', 'Configurações de gestão de clientes', 'CLIENTES', 'users', '#6F42C1', 6, 'USER', TRUE, 'CLIENTES'),
('PRODUTOS', 'Configurações de gestão de produtos', 'PRODUTOS', 'box', '#FD7E14', 7, 'MANAGER', TRUE, 'PRODUTOS'),
('IMPRESSAO', 'Configurações de impressão', 'IMPRESSAO', 'print', '#20C997', 8, 'MANAGER', TRUE, 'IMPRESSAO'),
('BACKUP', 'Configurações de backup e recuperação', 'BACKUP', 'save', '#6C757D', 9, 'ADMIN', TRUE, 'BACKUP'),
('SEGURANCA', 'Configurações de segurança e acesso', 'SEGURANCA', 'shield-alt', '#E83E8C', 10, 'ADMIN', TRUE, 'SEGURANCA'),
('INTEGRACAO', 'Configurações de integrações externas', 'INTEGRACAO', 'plug', '#6610F2', 11, 'ADMIN', TRUE, 'INTEGRACAO'),
('NOTIFICACAO', 'Configurações de notificações', 'NOTIFICACAO', 'bell', '#FF6B6B', 12, 'USER', TRUE, 'NOTIFICACAO'),
('PERFORMANCE', 'Configurações de performance do sistema', 'PERFORMANCE', 'tachometer-alt', '#FFA500', 13, 'ADMIN', TRUE, 'CORE'),
('LOG', 'Configurações de logs e auditoria', 'LOG', 'file-alt', '#6C757D', 14, 'ADMIN', TRUE, 'CORE')
ON CONFLICT DO NOTHING;

-- Inserir grupos de parâmetros padrão
INSERT INTO parametros_grupos (
    nome_grupo, descricao_grupo, codigo_grupo, icone_grupo, cor_grupo, 
    ordem_exibicao, layout_grupo, nivel_acesso_minimo, visivel, modulo_sistema
) VALUES 
('Configurações Gerais', 'Configurações básicas e gerais do sistema', 'CONFIG_GERAL', 'cogs', '#007BFF', 1, 'GRID', 'ADMIN', TRUE, 'CORE'),
('Interface do Usuário', 'Configurações de aparência e comportamento da interface', 'INTERFACE', 'desktop', '#28A745', 2, 'TAB', 'USER', TRUE, 'CORE'),
('Banco de Dados', 'Configurações de conexão e performance do banco de dados', 'BANCO_DADOS', 'database', '#17A2B8', 3, 'ACCORDION', 'ADMIN', TRUE, 'CORE'),
('Email e Comunicação', 'Configurações de envio de e-mails e notificações', 'EMAIL', 'envelope', '#DC3545', 4, 'GRID', 'MANAGER', TRUE, 'CORE'),
('Segurança', 'Configurações de segurança e autenticação', 'SEGURANCA', 'shield-alt', '#FFC107', 5, 'TAB', 'ADMIN', TRUE, 'CORE'),
('Performance', 'Configurações de otimização e performance', 'PERFORMANCE', 'tachometer-alt', '#6F42C1', 6, 'ACCORDION', 'ADMIN', TRUE, 'CORE'),
('Logs e Auditoria', 'Configurações de registro de eventos e auditoria', 'LOGS', 'file-alt', '#FD7E14', 7, 'GRID', 'ADMIN', TRUE, 'CORE'),
('Integrações', 'Configurações de integrações com sistemas externos', 'INTEGRACOES', 'plug', '#20C997', 8, 'TAB', 'ADMIN', TRUE, 'CORE'),
('Backup', 'Configurações de backup e recuperação de dados', 'BACKUP', 'save', '#6C757D', 9, 'GRID', 'ADMIN', TRUE, 'CORE'),
('PDV', 'Configurações específicas do Ponto de Venda', 'PDV_CONFIG', 'cash-register', '#E83E8C', 10, 'TAB', 'MANAGER', TRUE, 'PDV'),
('Relatórios', 'Configurações de geração e exportação de relatórios', 'RELATORIOS', 'chart-bar', '#6610F2', 11, 'GRID', 'MANAGER', TRUE, 'CORE')
ON CONFLICT DO NOTHING;

-- Inserir parâmetros padrão do sistema
INSERT INTO parametros (
    nome_parametro, descricao_parametro, codigo_parametro, categoria_parametro, subcategoria_parametro,
    ativo, visivel, editavel, obrigatorio, ordem_exibicao, icone_parametro,
    tipo_parametro, valor_padrao, label_parametro, tooltip_ajuda, grupo_interface, aba_interface,
    nivel_acesso, ambiente_parametro, requer_reinicializacao, afeta_performance, parametro_critico,
    log_alteracoes, cache_ativo, status_parametro
) VALUES 
-- Configurações Gerais
('Nome da Empresa', 'Nome fantasia da empresa exibido no sistema', 'NOME_EMPRESA', 'SISTEMA', 'GERAL',
 TRUE, TRUE, TRUE, TRUE, 1, 'building',
 'STRING', 'Hermes Comercial', 'Nome da empresa que aparece em relatórios e telas do sistema', 'Configurações Gerais', 'Dados da Empresa',
 'ADMIN', 'TODOS', TRUE, FALSE, TRUE,
 TRUE, TRUE, 'ATIVO'),

('CNPJ da Empresa', 'CNPJ da empresa para emissão de documentos fiscais', 'CNPJ_EMPRESA', 'SISTEMA', 'GERAL',
 TRUE, TRUE, TRUE, TRUE, 2, 'id-card',
 'STRING', '', 'CNPJ da empresa utilizado para notas fiscais e documentos legais', 'Configurações Gerais', 'Dados da Empresa',
 'ADMIN', 'TODOS', FALSE, FALSE, TRUE,
 TRUE, FALSE, 'ATIVO'),

('Endereço da Empresa', 'Endereço completo da empresa', 'ENDERECO_EMPRESA', 'SISTEMA', 'GERAL',
 TRUE, TRUE, TRUE, FALSE, 3, 'map-marker-alt',
 'STRING', '', 'Endereço da empresa para documentos e correspondências', 'Configurações Gerais', 'Dados da Empresa',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Telefone da Empresa', 'Telefone principal da empresa', 'TELEFONE_EMPRESA', 'SISTEMA', 'GERAL',
 TRUE, TRUE, TRUE, FALSE, 4, 'phone',
 'STRING', '', 'Telefone para contato e suporte', 'Configurações Gerais', 'Dados da Empresa',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Interface do Usuário
('Tema da Interface', 'Tema visual da interface do sistema', 'TEMA_INTERFACE', 'SISTEMA', 'INTERFACE',
 TRUE, TRUE, TRUE, FALSE, 1, 'palette',
 'STRING', 'CLARO', 'Escolha entre tema claro ou escuro para a interface', 'Interface do Usuário', 'Aparência',
 'USER', 'TODOS', TRUE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Idioma Padrão', 'Idioma padrão do sistema', 'IDIOMA_PADRAO', 'SISTEMA', 'INTERFACE',
 TRUE, TRUE, TRUE, FALSE, 2, 'language',
 'STRING', 'pt-BR', 'Idioma padrão para novos usuários e interface do sistema', 'Interface do Usuário', 'Aparência',
 'USER', 'TODOS', TRUE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Tamanho da Fonte', 'Tamanho padrão das fontes na interface', 'TAMANHO_FONTE', 'SISTEMA', 'INTERFACE',
 TRUE, TRUE, TRUE, FALSE, 3, 'text-height',
 'INTEGER', '12', 'Tamanho base das fontes utilizado na interface do sistema', 'Interface do Usuário', 'Aparência',
 'USER', 'TODOS', TRUE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Banco de Dados
('URL do Banco de Dados', 'URL de conexão com o banco de dados', 'URL_BANCO_DADOS', 'SISTEMA', 'BANCO_DADOS',
 TRUE, TRUE, TRUE, TRUE, 1, 'database',
 'URL', '', 'URL completa para conexão com o banco de dados PostgreSQL', 'Banco de Dados', 'Conexão',
 'ADMIN', 'TODOS', TRUE, TRUE, TRUE,
 TRUE, FALSE, 'ATIVO'),

('Porta do Banco de Dados', 'Porta de conexão do banco de dados', 'PORTA_BANCO_DADOS', 'SISTEMA', 'BANCO_DADOS',
 TRUE, TRUE, TRUE, TRUE, 2, 'server',
 'INTEGER', '5432', 'Porta TCP para conexão com o banco de dados PostgreSQL', 'Banco de Dados', 'Conexão',
 'ADMIN', 'TODOS', TRUE, FALSE, TRUE,
 TRUE, FALSE, 'ATIVO'),

('Nome do Banco de Dados', 'Nome do schema/banco de dados', 'NOME_BANCO_DADOS', 'SISTEMA', 'BANCO_DADOS',
 TRUE, TRUE, TRUE, TRUE, 3, 'database',
 'STRING', 'hermes_comercial', 'Nome do banco de dados PostgreSQL', 'Banco de Dados', 'Conexão',
 'ADMIN', 'TODOS', TRUE, FALSE, TRUE,
 TRUE, FALSE, 'ATIVO'),

('Pool de Conexões', 'Número máximo de conexões no pool', 'POOL_CONEXOES', 'SISTEMA', 'BANCO_DADOS',
 TRUE, TRUE, TRUE, FALSE, 4, 'layer-group',
 'INTEGER', '20', 'Número máximo de conexões simultâneas com o banco de dados', 'Banco de Dados', 'Performance',
 'ADMIN', 'TODOS', TRUE, TRUE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Email e Comunicação
('Servidor SMTP', 'Endereço do servidor de e-mail', 'SERVIDOR_SMTP', 'SISTEMA', 'EMAIL',
 TRUE, TRUE, TRUE, TRUE, 1, 'envelope',
 'STRING', '', 'Endereço do servidor SMTP para envio de e-mails', 'Email e Comunicação', 'Configurações SMTP',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Porta SMTP', 'Porta do servidor de e-mail', 'PORTA_SMTP', 'SISTEMA', 'EMAIL',
 TRUE, TRUE, TRUE, TRUE, 2, 'envelope-open-text',
 'INTEGER', '587', 'Porta do servidor SMTP (geralmente 587 para TLS, 465 para SSL)', 'Email e Comunicação', 'Configurações SMTP',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Usuário SMTP', 'Usuário para autenticação no servidor SMTP', 'USUARIO_SMTP', 'SISTEMA', 'EMAIL',
 TRUE, TRUE, TRUE, TRUE, 3, 'user',
 'STRING', '', 'Usuário para autenticação no servidor SMTP', 'Email e Comunicação', 'Configurações SMTP',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Senha SMTP', 'Senha para autenticação no servidor SMTP', 'SENHA_SMTP', 'SISTEMA', 'EMAIL',
 TRUE, TRUE, TRUE, TRUE, 4, 'lock',
 'PASSWORD', '', 'Senha para autenticação no servidor SMTP', 'Email e Comunicação', 'Configurações SMTP',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Usar TLS/SSL', 'Utilizar criptografia TLS/SSL no envio de e-mails', 'USAR_TLS_SMTP', 'SISTEMA', 'EMAIL',
 TRUE, TRUE, TRUE, FALSE, 5, 'lock',
 'BOOLEAN', 'TRUE', 'Habilitar criptografia TLS/SSL para comunicação segura com servidor SMTP', 'Email e Comunicação', 'Configurações SMTP',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Segurança
('Tempo de Sessão', 'Tempo em minutos para expiração da sessão do usuário', 'TEMPO_SESSAO', 'SISTEMA', 'SEGURANCA',
 TRUE, TRUE, TRUE, FALSE, 1, 'clock',
 'INTEGER', '30', 'Tempo em minutos que o usuário permanece logado sem atividade', 'Segurança', 'Sessão',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Máximo de Tentativas de Login', 'Número máximo de tentativas de login antes de bloquear', 'MAX_TENTATIVAS_LOGIN', 'SISTEMA', 'SEGURANCA',
 TRUE, TRUE, TRUE, FALSE, 2, 'user-lock',
 'INTEGER', '3', 'Máximo de tentativas de login incorreto antes de bloquear o usuário', 'Segurança', 'Login',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Tempo de Bloqueio', 'Tempo em minutos para bloqueio após falhas de login', 'TEMPO_BLOQUEIO_LOGIN', 'SISTEMA', 'SEGURANCA',
 TRUE, TRUE, TRUE, FALSE, 3, 'ban',
 'INTEGER', '15', 'Tempo em minutos que o usuário ficará bloqueado após falhas de login', 'Segurança', 'Login',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Exigir Senha Forte', 'Exigir critérios fortes para senhas de usuários', 'SENHA_FORTE', 'SISTEMA', 'SEGURANCA',
 TRUE, TRUE, TRUE, FALSE, 4, 'key',
 'BOOLEAN', 'TRUE', 'Exigir senhas com letras maiúsculas, minúsculas, números e caracteres especiais', 'Segurança', 'Política de Senhas',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Performance
('Cache Ativo', 'Habilitar cache de consultas ao banco de dados', 'CACHE_ATIVO', 'SISTEMA', 'PERFORMANCE',
 TRUE, TRUE, TRUE, FALSE, 1, 'memory',
 'BOOLEAN', 'TRUE', 'Habilitar cache para melhorar performance de consultas frequentes', 'Performance', 'Cache',
 'ADMIN', 'TODOS', FALSE, TRUE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('TTL do Cache', 'Tempo de vida do cache em segundos', 'TTL_CACHE', 'SISTEMA', 'PERFORMANCE',
 TRUE, TRUE, TRUE, FALSE, 2, 'clock',
 'INTEGER', '300', 'Tempo em segundos que os dados permanecem no cache', 'Performance', 'Cache',
 'ADMIN', 'TODOS', FALSE, TRUE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Limite de Registros por Página', 'Número máximo de registros exibidos em listagens', 'LIMITE_REGISTROS_PAGINA', 'SISTEMA', 'PERFORMANCE',
 TRUE, TRUE, TRUE, FALSE, 3, 'list',
 'INTEGER', '50', 'Número máximo de registros exibidos por página em listagens e relatórios', 'Performance', 'Interface',
 'USER', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Logs e Auditoria
('Nível de Log', 'Nível mínimo de log registrado pelo sistema', 'NIVEL_LOG', 'SISTEMA', 'LOG',
 TRUE, TRUE, TRUE, FALSE, 1, 'file-alt',
 'STRING', 'INFO', 'Nível mínimo para registro de logs (DEBUG, INFO, WARN, ERROR, FATAL)', 'Logs e Auditoria', 'Configurações de Log',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Dias de Retenção de Logs', 'Dias para manter arquivos de log', 'DIAS_RETENCAO_LOGS', 'SISTEMA', 'LOG',
 TRUE, TRUE, TRUE, FALSE, 2, 'calendar-alt',
 'INTEGER', '30', 'Número de dias para manter arquivos de log antes de excluí-los automaticamente', 'Logs e Auditoria', 'Configurações de Log',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Log de Auditoria Ativo', 'Habilitar log de auditoria de ações do usuário', 'LOG_AUDITORIA_ATIVO', 'SISTEMA', 'LOG',
 TRUE, TRUE, TRUE, FALSE, 3, 'shield-alt',
 'BOOLEAN', 'TRUE', 'Registrar todas as ações dos usuários para fins de auditoria', 'Logs e Auditoria', 'Auditoria',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- Backup
('Backup Automático', 'Habilitar backup automático do banco de dados', 'BACKUP_AUTOMATICO', 'SISTEMA', 'BACKUP',
 TRUE, TRUE, TRUE, FALSE, 1, 'save',
 'BOOLEAN', 'TRUE', 'Executar backup automático do banco de dados periodicamente', 'Backup', 'Configurações de Backup',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Frequência de Backup', 'Frequência do backup automático em horas', 'FREQUENCIA_BACKUP', 'SISTEMA', 'BACKUP',
 TRUE, TRUE, TRUE, FALSE, 2, 'clock',
 'INTEGER', '24', 'Frequência em horas para execução do backup automático', 'Backup', 'Configurações de Backup',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Caminho do Backup', 'Diretório para armazenar arquivos de backup', 'CAMINHO_BACKUP', 'SISTEMA', 'BACKUP',
 TRUE, TRUE, TRUE, FALSE, 3, 'folder',
 'STRING', '/backup', 'Caminho completo do diretório onde os backups serão armazenados', 'Backup', 'Configurações de Backup',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Número de Backups Mantidos', 'Quantidade de backups a manter no diretório', 'NUM_BACKUPS_MANTIDOS', 'SISTEMA', 'BACKUP',
 TRUE, TRUE, TRUE, FALSE, 4, 'copy',
 'INTEGER', '7', 'Número máximo de arquivos de backup a manter (os mais antigos serão excluídos)', 'Backup', 'Configurações de Backup',
 'ADMIN', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

-- PDV
('Caixa Padrão', 'Caixa padrão para novas vendas', 'CAIXA_PADRAO', 'PDV', 'GERAL',
 TRUE, TRUE, TRUE, FALSE, 1, 'cash-register',
 'INTEGER', '1', 'Caixa padrão que será selecionado ao iniciar nova venda no PDV', 'PDV', 'Configurações Gerais',
 'MANAGER', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Operador Padrão', 'Operador padrão para o PDV', 'OPERADOR_PADRAO', 'PDV', 'GERAL',
 TRUE, TRUE, TRUE, FALSE, 2, 'user',
 'STRING', '', 'Operador padrão que será selecionado ao iniciar o PDV', 'PDV', 'Configurações Gerais',
 'MANAGER', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Impressora de Cupom Padrão', 'Impressora padrão para cupons fiscais', 'IMPRESSORA_CUPOM_PADRAO', 'PDV', 'IMPRESSAO',
 TRUE, TRUE, TRUE, FALSE, 3, 'print',
 'STRING', '', 'Impressora padrão utilizada para impressão de cupons fiscais', 'PDV', 'Configurações de Impressão',
 'MANAGER', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO'),

('Mensagem da Rodapé', 'Mensagem exibida no rodapé dos cupons', 'MENSAGEM_RODAPE', 'PDV', 'IMPRESSAO',
 TRUE, TRUE, TRUE, FALSE, 4, 'comment',
 'STRING', 'Obrigado pela preferência!', 'Mensagem personalizada exibida no rodapé dos cupons fiscais', 'PDV', 'Configurações de Impressão',
 'MANAGER', 'TODOS', FALSE, FALSE, FALSE,
 TRUE, FALSE, 'ATIVO')
ON CONFLICT DO NOTHING;

-- Inserir validações de parâmetros padrão
INSERT INTO parametros_validacoes (
    id_parametro, nome_validacao, descricao_validacao, tipo_validacao,
    expressao_validacao, mensagem_erro, severidade_erro, exibir_mensagem_interface,
    ativa, ordem_execucao
)
SELECT 
    p.id, 'VALIDACAO_CNPJ', 'Validação de formato CNPJ', 'REGEX',
    '^[0-9]{2}\.[0-9]{3}\.[0-9]{3}\/[0-9]{4}\-[0-9]{2}$', 'CNPJ inválido. Use o formato XX.XXX.XXX/XXXX-XX', 'ERRO', TRUE,
    TRUE, 1
FROM parametros p 
WHERE p.codigo_parametro = 'CNPJ_EMPRESA'
ON CONFLICT DO NOTHING;

INSERT INTO parametros_validacoes (
    id_parametro, nome_validacao, descricao_validacao, tipo_validacao,
    expressao_validacao, mensagem_erro, severidade_erro, exibir_mensagem_interface,
    ativa, ordem_execucao
)
SELECT 
    p.id, 'VALIDACAO_EMAIL', 'Validação de formato e-mail', 'REGEX',
    '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$', 'E-mail inválido', 'ERRO', TRUE,
    TRUE, 1
FROM parametros p 
WHERE p.codigo_parametro IN ('USUARIO_SMTP', 'SERVIDOR_SMTP')
ON CONFLICT DO NOTHING;

-- Inserir associações de parâmetros com grupos
INSERT INTO parametros_grupo_associacao (
    id_grupo, id_parametro, ordem_no_grupo, visivel_no_grupo, obrigatorio_no_grupo,
    ativa
)
SELECT 
    g.id, p.id, p.ordem_exibicao, TRUE, p.obrigatorio, TRUE
FROM parametros_grupos g, parametros p
WHERE g.codigo_grupo = 'CONFIG_GERAL' 
  AND p.categoria_parametro = 'SISTEMA' 
  AND p.subcategoria_parametro = 'GERAL'
ON CONFLICT DO NOTHING;

INSERT INTO parametros_grupo_associacao (
    id_grupo, id_parametro, ordem_no_grupo, visivel_no_grupo, obrigatorio_no_grupo,
    ativa
)
SELECT 
    g.id, p.id, p.ordem_exibicao, TRUE, p.obrigatorio, TRUE
FROM parametros_grupos g, parametros p
WHERE g.codigo_grupo = 'INTERFACE' 
  AND p.categoria_parametro = 'SISTEMA' 
  AND p.subcategoria_parametro = 'INTERFACE'
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE parametros IS 'Tabela de parâmetros configuráveis do sistema';
COMMENT ON COLUMN parametros.nome_parametro IS 'Nome único do parâmetro';
COMMENT ON COLUMN parametros.descricao_parametro IS 'Descrição detalhada do parâmetro';
COMMENT ON COLUMN parametros.codigo_parametro IS 'Código único do parâmetro';
COMMENT ON COLUMN parametros.categoria_parametro IS 'Categoria principal do parâmetro';
COMMENT ON COLUMN parametros.subcategoria_parametro IS 'Subcategoria específica do parâmetro';
COMMENT ON COLUMN parametros.ativo IS 'Indica se o parâmetro está ativo';
COMMENT ON COLUMN parametros.visivel IS 'Indica se o parâmetro é visível na interface';
COMMENT ON COLUMN parametros.editavel IS 'Indica se o parâmetro pode ser editado';
COMMENT ON COLUMN parametros.obrigatorio IS 'Indica se o parâmetro é obrigatório';
COMMENT ON COLUMN parametros.ordem_exibicao IS 'Ordem de exibição na interface';
COMMENT ON COLUMN parametros.icone_parametro IS 'Ícone representativo do parâmetro';
COMMENT ON COLUMN parametros.tipo_parametro IS 'Tipo de dado do parâmetro';
COMMENT ON COLUMN parametros.formato_parametro IS 'Formato específico do parâmetro';
COMMENT ON COLUMN parametros.mascara_parametro IS 'Máscara para entrada de dados';
COMMENT ON COLUMN parametros.valor_padrao IS 'Valor padrão do parâmetro';
COMMENT ON COLUMN parametros.valores_possiveis IS 'Valores possíveis para campos do tipo SELECT';
COMMENT ON COLUMN parametros.opcoes_combo IS 'Opções em formato JSON para campos complexos';
COMMENT ON COLUMN parametros.dominio_valores IS 'Domínio de valores permitidos';
COMMENT ON COLUMN parametros.valor_minimo IS 'Valor mínimo permitido';
COMMENT ON COLUMN parametros.valor_maximo IS 'Valor máximo permitido';
COMMENT ON COLUMN parametros.tamanho_minimo IS 'Tamanho mínimo permitido';
COMMENT ON COLUMN parametros.tamanho_maximo IS 'Tamanho máximo permitido';
COMMENT ON COLUMN parametros.expressao_validacao IS 'Expressão regular para validação';
COMMENT ON COLUMN parametros.mensagem_erro_validacao IS 'Mensagem de erro para validação';
COMMENT ON COLUMN parametros.label_parametro IS 'Label exibido na interface';
COMMENT ON COLUMN parametros.placeholder_parametro IS 'Placeholder para o campo';
COMMENT ON COLUMN parametros.tooltip_ajuda IS 'Texto de ajuda/exemplo';
COMMENT ON COLUMN parametros.grupo_interface IS 'Grupo visual na interface';
COMMENT ON COLUMN parametros.aba_interface IS 'Aba onde o parâmetro aparece';
COMMENT ON COLUMN parametros.requer_reinicializacao IS 'Requer reinicialização do sistema';
COMMENT ON COLUMN parametros.afeta_performance IS 'Afeta performance do sistema';
COMMENT ON COLUMN parametros.parametro_critico IS 'Parâmetro crítico do sistema';
COMMENT ON COLUMN parametros.backup_automatico IS 'Backup automático do valor';
COMMENT ON COLUMN parametros.nivel_acesso IS 'Nível de acesso mínimo';
COMMENT ON COLUMN parametros.perfil_acesso IS 'Perfis que podem acessar';
COMMENT ON COLUMN parametros.departamento_acesso IS 'Departamentos que podem acessar';
COMMENT ON COLUMN parametros.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN parametros.usuario_ultima_atualizacao IS 'Usuário da última atualização';
COMMENT ON COLUMN parametros.versao_parametro IS 'Versão em que foi criado/modificado';
COMMENT ON COLUMN parametros.compatibilidade_versao IS 'Versões compatíveis';
COMMENT ON COLUMN parametros.depreciado IS 'Indica se o parâmetro está depreciado';
COMMENT ON COLUMN parametros.data_depreciacao IS 'Data de depreciação';
COMMENT ON COLUMN parametros.motivo_depreciacao IS 'Motivo da depreciação';
COMMENT ON COLUMN parametros.ambiente_parametro IS 'Ambiente onde o parâmetro se aplica';
COMMENT ON COLUMN parametros.valor_producao IS 'Valor para ambiente de produção';
COMMENT ON COLUMN parametros.valor_homologacao IS 'Valor para ambiente de homologação';
COMMENT ON COLUMN parametros.valor_desenvolvimento IS 'Valor para ambiente de desenvolvimento';
COMMENT ON COLUMN parametros.valor_teste IS 'Valor para ambiente de teste';
COMMENT ON COLUMN parametros.integracao_externa IS 'Indica se o parâmetro é de integração externa';
COMMENT ON COLUMN parametros.nome_integracao IS 'Nome da integração externa';
COMMENT ON COLUMN parametros.endpoint_integracao IS 'Endpoint da integração';
COMMENT ON COLUMN parametros.metodo_sincronizacao IS 'Método de sincronização';
COMMENT ON COLUMN parametros.intervalo_sincronizacao IS 'Intervalo de sincronização em minutos';
COMMENT ON COLUMN parametros.log_alteracoes IS 'Registrar alterações no log';
COMMENT ON COLUMN parametros.log_leituras IS 'Registrar leituras no log';
COMMENT ON COLUMN parametros.nivel_log IS 'Nível do log';
COMMENT ON COLUMN parametros.categoria_log IS 'Categoria do log';
COMMENT ON COLUMN parametros.cache_ativo IS 'Cache do parâmetro ativo';
COMMENT ON COLUMN parametros.cache_ttl IS 'TTL do cache em segundos';
COMMENT ON COLUMN parametros.cache_chave IS 'Chave do cache';
COMMENT ON COLUMN parametros.regra_negocio IS 'Regra de negócio em formato JSON';
COMMENT ON COLUMN parametros.validacao_customizada IS 'Validação customizada';
COMMENT ON COLUMN parametros.script_validacao IS 'Script de validação';
COMMENT ON COLUMN parametros.transformacao_dados IS 'Transformação de dados ativada';
COMMENT ON COLUMN parametros.script_transformacao IS 'Script de transformação';
COMMENT ON COLUMN parametros.formato_saida IS 'Formato de saída após transformação';
COMMENT ON COLUMN parametros.criptografado IS 'Valor do parâmetro criptografado';
COMMENT ON COLUMN parametros.algoritmo_criptografia IS 'Algoritmo de criptografia';
COMMENT ON COLUMN parametros.chave_criptografia IS 'Chave de criptografia';
COMMENT ON COLUMN parametros.mascara_exibicao IS 'Máscara para exibição de valores sensíveis';
COMMENT ON COLUMN parametros.auditoria_ativa IS 'Auditoria do parâmetro ativa';
COMMENT ON COLUMN parametros.campos_auditoria IS 'Campos que devem ser auditados';
COMMENT ON COLUMN parametros.evento_auditoria IS 'Evento que dispara auditoria';
COMMENT ON COLUMN parametros.notificacao_alteracao IS 'Notificação de alteração ativada';
COMMENT ON COLUMN parametros.canais_notificacao IS 'Canais de notificação';
COMMENT ON COLUMN parametros.template_notificacao IS 'Template de notificação';
COMMENT ON COLUMN parametros.destinatarios_notificacao IS 'Destinatários das notificações';
COMMENT ON COLUMN parametros.depende_de_parametro IS 'Nome do parâmetro dependente';
COMMENT ON COLUMN parametros.condicao_dependencia IS 'Condição para habilitar este parâmetro';
COMMENT ON COLUMN parametros.parametros_relacionados IS 'Parâmetros relacionados';
COMMENT ON COLUMN parametros.indice_performance IS 'Índice de performance';
COMMENT ON COLUMN parametros.metrica_associada IS 'Métrica associada';
COMMENT ON COLUMN parametros.impacto_performance IS 'Impacto na performance';
COMMENT ON COLUMN parametros.otimizacao_recomendada IS 'Otimização recomendada';
COMMENT ON COLUMN parametros.valor_anterior IS 'Valor anterior do parâmetro';
COMMENT ON COLUMN parametros.data_ultima_alteracao IS 'Data da última alteração';
COMMENT ON COLUMN parametros.motivo_alteracao IS 'Motivo da última alteração';
COMMENT ON COLUMN parametros.status_parametro IS 'Status atual do parâmetro';
COMMENT ON COLUMN parametros.motivo_status IS 'Motivo do status atual';
COMMENT ON COLUMN parametros.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN parametros.data_ultima_atualizacao IS 'Data da última atualização';

COMMENT ON TABLE parametros_categorias IS 'Categorias de parâmetros do sistema';
COMMENT ON TABLE parametros_historico IS 'Histórico de alterações dos parâmetros';
COMMENT ON TABLE parametros_validacoes IS 'Validações customizadas dos parâmetros';
COMMENT ON TABLE parametros_dependencias IS 'Dependências entre parâmetros';
COMMENT ON TABLE parametros_grupos IS 'Grupos de organização de parâmetros';
COMMENT ON TABLE parametros_grupo_associacao IS 'Associação entre parâmetros e grupos';
COMMENT ON TABLE parametros_templates IS 'Templates para valores de parâmetros';
