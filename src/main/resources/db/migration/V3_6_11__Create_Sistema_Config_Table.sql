-- =====================================================
-- Hermes Comercial v3.6.11 - Migration
-- Criar tabela de Configurações do Sistema
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Configurações do Sistema
CREATE TABLE IF NOT EXISTS sistema_config (
    id SERIAL PRIMARY KEY,
    
    -- Configurações Gerais do Sistema
    nome_sistema VARCHAR(100) NOT NULL DEFAULT 'Hermes Comercial',
    versao_sistema VARCHAR(20) NOT NULL DEFAULT '3.6.11',
    nome_empresa VARCHAR(200),
    slogan_empresa VARCHAR(300),
    
    -- Configurações de Interface
    tema_interface VARCHAR(20) DEFAULT 'CLARO', -- CLARO, ESCURO, AUTO
    cor_primaria VARCHAR(7) DEFAULT '#0066CC',
    cor_secundaria VARCHAR(7) DEFAULT '#FF6600',
    cor_acento VARCHAR(7) DEFAULT '#00AA44',
    fonte_interface VARCHAR(50) DEFAULT 'Segoe UI',
    tamanho_fonte INTEGER DEFAULT 12,
    
    -- Configurações de Idioma e Localização
    idioma_padrao VARCHAR(10) DEFAULT 'PT-BR',
    pais_padrao VARCHAR(2) DEFAULT 'BR',
    formato_data VARCHAR(20) DEFAULT 'dd/MM/yyyy',
    formato_hora VARCHAR(10) DEFAULT 'HH:mm:ss',
    formato_numero VARCHAR(20) DEFAULT '#,##0.00',
    formato_moeda VARCHAR(10) DEFAULT 'R$ #,##0.00',
    separador_decimal CHAR(1) DEFAULT ',',
    separador_milhar CHAR(1) DEFAULT '.',
    
    -- Configurações de Segurança
    exigir_senha_supervisor BOOLEAN DEFAULT TRUE,
    tempo_sessao_minutos INTEGER DEFAULT 480, -- 8 horas
    maximo_tentativas_login INTEGER DEFAULT 3,
    tempo_bloqueio_minutos INTEGER DEFAULT 30,
    exigir_troca_senha_dias INTEGER DEFAULT 90,
    complexidade_senha BOOLEAN DEFAULT TRUE,
    historico_senha INTEGER DEFAULT 5, -- não pode repetir últimas 5 senhas
    
    -- Configurações de Backup
    backup_automatico BOOLEAN DEFAULT TRUE,
    frequencia_backup VARCHAR(20) DEFAULT 'DIARIO', -- DIARIO, SEMANAL, MENSAL
    horario_backup TIME DEFAULT '23:00:00',
    diretorio_backup VARCHAR(500),
    dias_retention_backup INTEGER DEFAULT 30,
    compactar_backup BOOLEAN DEFAULT TRUE,
    backup_nuvem BOOLEAN DEFAULT FALSE,
    provedor_nuvem VARCHAR(50), -- GOOGLE_DRIVE, DROPBOX, ONEDRIVE, AWS_S3
    token_nuvem TEXT,
    
    -- Configurações de Log e Auditoria
    nivel_log VARCHAR(20) DEFAULT 'INFO', -- DEBUG, INFO, WARN, ERROR
    arquivo_log VARCHAR(500),
    tamanho_maximo_log_mb INTEGER DEFAULT 100,
    dias_retention_log INTEGER DEFAULT 90,
    log_rotativo BOOLEAN DEFAULT TRUE,
    auditoria_ativa BOOLEAN DEFAULT TRUE,
    log_acessos BOOLEAN DEFAULT TRUE,
    log_alteracoes BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Performance
    cache_ativo BOOLEAN DEFAULT TRUE,
    tempo_cache_minutos INTEGER DEFAULT 60,
    pool_conexoes_minimo INTEGER DEFAULT 5,
    pool_conexoes_maximo INTEGER DEFAULT 20,
    timeout_conexao_segundos INTEGER DEFAULT 30,
    lazy_loading BOOLEAN DEFAULT TRUE,
    virtual_scrolling BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Integração
    api_ativa BOOLEAN DEFAULT FALSE,
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    webhook_url VARCHAR(500),
    integracao_contador BOOLEAN DEFAULT FALSE,
    integracao_banco BOOLEAN DEFAULT FALSE,
    integracao_governo BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Notificações
    notificacoes_email BOOLEAN DEFAULT TRUE,
    notificacoes_sistema BOOLEAN DEFAULT TRUE,
    notificacoes_som BOOLEAN DEFAULT TRUE,
    notificacoes_desktop BOOLEAN DEFAULT TRUE,
    email_smtp VARCHAR(255),
    email_porta INTEGER DEFAULT 587,
    email_usuario VARCHAR(100),
    email_senha VARCHAR(255),
    email_ssl BOOLEAN DEFAULT TRUE,
    
    -- Configurações de PDV
    pdv_numero_padrao INTEGER DEFAULT 1,
    pdv_auto_imprimir BOOLEAN DEFAULT TRUE,
    pdv_exigir_cpf BOOLEAN DEFAULT TRUE,
    pdv_permitir_desconto BOOLEAN DEFAULT TRUE,
    pdv_desconto_maximo_percentual DECIMAL(5,2) DEFAULT 50.00,
    pdv_permitir_cancelamento BOOLEAN DEFAULT TRUE,
    pdv_exigir_supervisor_cancelamento BOOLEAN DEFAULT TRUE,
    pdv_mostrar_estoque BOOLEAN DEFAULT TRUE,
    pdv_bloquear_sem_estoque BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Impressão
    impressora_padrao VARCHAR(100),
    impressora_nao_fiscal VARCHAR(100),
    impressora_etiquetas VARCHAR(100),
    formato_papel VARCHAR(20) DEFAULT 'A4',
    orientacao_impressao VARCHAR(10) DEFAULT 'RETRATO', -- RETRATO, PAISAGEM
    margem_superior DECIMAL(5,2) DEFAULT 10.00,
    margem_inferior DECIMAL(5,2) DEFAULT 10.00,
    margem_esquerda DECIMAL(5,2) DEFAULT 10.00,
    margem_direita DECIMAL(5,2) DEFAULT 10.00,
    
    -- Configurações de Relatórios
    logo_relatorio VARCHAR(500),
    cabecalho_relatorio TEXT,
    rodape_relatorio TEXT,
    mostrar_logo_relatorio BOOLEAN DEFAULT TRUE,
    mostrar_cabecalho_relatorio BOOLEAN DEFAULT TRUE,
    mostrar_rodape_relatorio BOOLEAN DEFAULT TRUE,
    formato_exportacao_padrao VARCHAR(10) DEFAULT 'PDF', -- PDF, EXCEL, CSV
    
    -- Configurações de Estoque
    controle_estoque BOOLEAN DEFAULT TRUE,
    estoque_negativo BOOLEAN DEFAULT FALSE,
    controle_lote BOOLEAN DEFAULT FALSE,
    controle_validade BOOLEAN DEFAULT FALSE,
    dias_alerta_validade INTEGER DEFAULT 30,
    estoque_minimo_padrao INTEGER DEFAULT 10,
    alerta_estoque_baixo BOOLEAN DEFAULT TRUE,
    
    -- Configurações Financeiras
    moeda_padrao VARCHAR(3) DEFAULT 'BRL',
    casas_decimais_moeda INTEGER DEFAULT 2,
    forma_pagamento_padrao VARCHAR(50) DEFAULT 'DINHEIRO',
    permitir_venda_prazo BOOLEAN DEFAULT TRUE,
    prazo_maximo_venda_dias INTEGER DEFAULT 30,
    juros_mes_padrao DECIMAL(5,2) DEFAULT 2.00,
    multa_atraso_padrao DECIMAL(5,2) DEFAULT 5.00,
    
    -- Configurações Fiscais
    ambiente_fiscal VARCHAR(20) DEFAULT 'HOMOLOGACAO', -- HOMOLOGACAO, PRODUCAO
    serie_nf_padrao VARCHAR(3) DEFAULT '001',
    numero_nf_atual INTEGER DEFAULT 0,
    certificado_digital VARCHAR(500),
    senha_certificado VARCHAR(255),
    validade_certificado DATE,
    emitir_nf_automatico BOOLEAN DEFAULT FALSE,
    salvar_xml_nf BOOLEAN DEFAULT TRUE,
    enviar_email_nf BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Sistema Operacional
    so_detectado VARCHAR(50), -- WINDOWS, LINUX, MAC
    arquitetura VARCHAR(20), -- 32, 64
    java_version VARCHAR(50),
    memoria_total_mb INTEGER,
    memoria_disponivel_mb INTEGER,
    disco_total_gb INTEGER,
    disco_disponivel_gb INTEGER,
    
    -- Configurações de Rede
    ip_servidor VARCHAR(45),
    porta_servidor INTEGER DEFAULT 8080,
    protocolo VARCHAR(10) DEFAULT 'HTTP',
    proxy_ativo BOOLEAN DEFAULT FALSE,
    proxy_host VARCHAR(255),
    proxy_porta INTEGER,
    proxy_usuario VARCHAR(100),
    proxy_senha VARCHAR(255),
    
    -- Status e Controle
    sistema_online BOOLEAN DEFAULT TRUE,
    modo_manutencao BOOLEAN DEFAULT FALSE,
    mensagem_manutencao TEXT,
    data_ultimo_backup TIMESTAMP,
    data_ultima_verificacao TIMESTAMP,
    data_instalacao DATE DEFAULT CURRENT_DATE,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tema_interface CHECK (tema_interface IN ('CLARO', 'ESCURO', 'AUTO')),
    CONSTRAINT chk_frequencia_backup CHECK (frequencia_backup IN ('DIARIO', 'SEMANAL', 'MENSAL')),
    CONSTRAINT chk_nivel_log CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR')),
    CONSTRAINT chk_provedor_nuvem CHECK (provedor_nuvem IN ('GOOGLE_DRIVE', 'DROPBOX', 'ONEDRIVE', 'AWS_S3')),
    CONSTRAINT chk_orientacao_impressao CHECK (orientacao_impressao IN ('RETRATO', 'PAISAGEM')),
    CONSTRAINT chk_formato_papel CHECK (formato_papel IN ('A4', 'A3', 'A5', 'LETTER', 'LEGAL')),
    CONSTRAINT chk_formato_exportacao_padrao CHECK (formato_exportacao_padrao IN ('PDF', 'EXCEL', 'CSV')),
    CONSTRAINT chk_moeda_padrao CHECK (moeda_padrao IN ('BRL', 'USD', 'EUR')),
    CONSTRAINT chk_ambiente_fiscal CHECK (ambiente_fiscal IN ('HOMOLOGACAO', 'PRODUCAO')),
    CONSTRAINT chk_protocolo CHECK (protocolo IN ('HTTP', 'HTTPS')),
    CONSTRAINT chk_limites_sistema CHECK (
        tempo_sessao_minutos > 0 AND 
        maximo_tentativas_login > 0 AND 
        tempo_bloqueio_minutos > 0 AND
        exigir_troca_senha_dias > 0 AND
        historico_senha >= 0 AND
        dias_retention_backup > 0 AND
        tamanho_maximo_log_mb > 0 AND
        dias_retention_log > 0 AND
        tempo_cache_minutos > 0 AND
        pool_conexoes_minimo > 0 AND
        pool_conexoes_maximo > 0 AND
        timeout_conexao_segundos > 0 AND
        dias_alerta_validade > 0 AND
        estoque_minimo_padrao >= 0 AND
        casas_decimais_moeda >= 0 AND
        prazo_maximo_venda_dias > 0 AND
        juros_mes_padrao >= 0 AND
        multa_atraso_padrao >= 0 AND
        desconto_maximo_percentual >= 0 AND
        margem_superior >= 0 AND
        margem_inferior >= 0 AND
        margem_esquerda >= 0 AND
        margem_direita >= 0
    )
);

-- Criar tabela de Logs do Sistema
CREATE TABLE IF NOT EXISTS sistema_logs (
    id SERIAL PRIMARY KEY,
    
    -- Dados do Log
    nivel_log VARCHAR(20) NOT NULL, -- DEBUG, INFO, WARN, ERROR, FATAL
    categoria_log VARCHAR(50), -- SISTEMA, BANCO, UI, NEGOCIO, SEGURANCA, PERFORMANCE
    mensagem TEXT NOT NULL,
    detalhe TEXT,
    
    -- Contexto
    modulo VARCHAR(100),
    funcionalidade VARCHAR(100),
    classe VARCHAR(200),
    metodo VARCHAR(100),
    linha_codigo INTEGER,
    
    -- Usuário e Sessão
    usuario VARCHAR(50),
    sessao_id VARCHAR(100),
    ip_address VARCHAR(45),
    hostname VARCHAR(100),
    user_agent TEXT,
    
    -- Dados Técnicos
    exception_class VARCHAR(200),
    stack_trace TEXT,
    parametros_metodo TEXT,
    
    -- Performance
    tempo_execucao_ms INTEGER,
    memoria_utilizada_mb DECIMAL(10,2),
    
    -- Controle
    data_log TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_nivel_log_sistema CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    CONSTRAINT chk_categoria_log_sistema CHECK (categoria_log IN ('SISTEMA', 'BANCO', 'UI', 'NEGOCIO', 'SEGURANCA', 'PERFORMANCE'))
);

-- Criar tabela de Histórico de Versões
CREATE TABLE IF NOT EXISTS sistema_versoes (
    id SERIAL PRIMARY KEY,
    
    -- Dados da Versão
    versao VARCHAR(20) NOT NULL,
    data_lancamento DATE,
    tipo_versao VARCHAR(20), -- MAJOR, MINOR, PATCH, HOTFIX
    descricao_versao TEXT,
    
    -- Detalhes da Atualização
    data_instalacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_instalacao VARCHAR(50),
    metodo_instalacao VARCHAR(50), -- AUTOMATICO, MANUAL, REMOTO
    
    -- Mudanças
    novas_funcionalidades TEXT,
    correcoes TEXT,
    melhorias TEXT,
    breaking_changes TEXT,
    
    -- Status
    status_instalacao VARCHAR(20) DEFAULT 'CONCLUIDO', -- PENDENTE, EM_ANDAMENTO, CONCLUIDO, FALHOU
    mensagem_erro TEXT,
    
    -- Auditoria
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_versao CHECK (tipo_versao IN ('MAJOR', 'MINOR', 'PATCH', 'HOTFIX')),
    CONSTRAINT chk_status_instalacao CHECK (status_instalacao IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDO', 'FALHOU'))
);

-- Criar tabela de Configurações de Módulos
CREATE TABLE IF NOT EXISTS sistema_modulos_config (
    id SERIAL PRIMARY KEY,
    
    -- Identificação do Módulo
    nome_modulo VARCHAR(100) NOT NULL UNIQUE,
    versao_modulo VARCHAR(20),
    status_modulo VARCHAR(20) DEFAULT 'ATIVO', -- ATIVO, INATIVO, MANUTENCAO
    
    -- Configurações do Módulo
    configuracoes JSONB, -- Configurações específicas do módulo em JSON
    dependencias TEXT, -- Módulos dos quais depende
    
    -- Permissões
    requer_admin BOOLEAN DEFAULT FALSE,
    permissoes_necessarias TEXT,
    
    -- Performance
    tempo_carregamento_ms INTEGER,
    memoria_necessaria_mb INTEGER,
    
    -- Controle
    data_ultima_execucao TIMESTAMP,
    total_execucoes INTEGER DEFAULT 0,
    
    -- Auditoria
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_modulo CHECK (status_modulo IN ('ATIVO', 'INATIVO', 'MANUTENCAO'))
);

-- Criar tabela de Alertas do Sistema
CREATE TABLE IF NOT EXISTS sistema_alertas (
    id SERIAL PRIMARY KEY,
    
    -- Dados do Alerta
    tipo_alerta VARCHAR(50) NOT NULL, -- ERRO, AVISO, INFO, SUCESSO, CRITICO
    categoria_alerta VARCHAR(50), -- SISTEMA, BANCO, PERFORMANCE, SEGURANCA, NEGOCIO
    titulo_alerta VARCHAR(200) NOT NULL,
    mensagem_alerta TEXT NOT NULL,
    
    -- Severidade e Prioridade
    severidade VARCHAR(20) DEFAULT 'MEDIA', -- BAIXA, MEDIA, ALTA, CRITICA
    prioridade INTEGER DEFAULT 3, -- 1 (mais alta) a 5 (mais baixa)
    
    -- Contexto
    modulo_origem VARCHAR(100),
    funcionalidade_origem VARCHAR(100),
    usuario_afetado VARCHAR(50),
    
    -- Ações
    acao_recomendada TEXT,
    acao_executada TEXT,
    status_alerta VARCHAR(20) DEFAULT 'ABERTO', -- ABERTO, EM_ANALISE, RESOLVIDO, IGNORADO
    
    -- Controle
    data_alerta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_resolucao TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_resolucao VARCHAR(50),
    
    -- Notificação
    notificado_email BOOLEAN DEFAULT FALSE,
    notificado_sistema BOOLEAN DEFAULT FALSE,
    data_notificacao TIMESTAMP,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_alerta CHECK (tipo_alerta IN ('ERRO', 'AVISO', 'INFO', 'SUCESSO', 'CRITICO')),
    CONSTRAINT chk_categoria_alerta CHECK (categoria_alerta IN ('SISTEMA', 'BANCO', 'PERFORMANCE', 'SEGURANCA', 'NEGOCIO')),
    CONSTRAINT chk_severidade CHECK (severidade IN ('BAIXA', 'MEDIA', 'ALTA', 'CRITICA')),
    CONSTRAINT chk_status_alerta CHECK (status_alerta IN ('ABERTO', 'EM_ANALISE', 'RESOLVIDO', 'IGNORADO')),
    CONSTRAINT chk_prioridade_alerta CHECK (prioridade BETWEEN 1 AND 5)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_sistema_config_nome_sistema ON sistema_config(nome_sistema);
CREATE INDEX IF NOT EXISTS idx_sistema_config_versao ON sistema_config(versao_sistema);
CREATE INDEX IF NOT EXISTS idx_sistema_config_tema_interface ON sistema_config(tema_interface);
CREATE INDEX IF NOT EXISTS idx_sistema_config_idioma_padrao ON sistema_config(idioma_padrao);
CREATE INDEX IF NOT EXISTS idx_sistema_config_sistema_online ON sistema_config(sistema_online);
CREATE INDEX IF NOT EXISTS idx_sistema_config_modo_manutencao ON sistema_config(modo_manutencao);

CREATE INDEX IF NOT EXISTS idx_sistema_logs_data_log ON sistema_logs(data_log);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_nivel_log ON sistema_logs(nivel_log);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_categoria_log ON sistema_logs(categoria_log);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_usuario ON sistema_logs(usuario);
CREATE INDEX IF NOT EXISTS idx_sistema_logs_modulo ON sistema_logs(modulo);

CREATE INDEX IF NOT EXISTS idx_sistema_versoes_versao ON sistema_versoes(versao);
CREATE INDEX IF NOT EXISTS idx_sistema_versoes_data_lancamento ON sistema_versoes(data_lancamento);
CREATE INDEX IF NOT EXISTS idx_sistema_versoes_tipo_versao ON sistema_versoes(tipo_versao);
CREATE INDEX IF NOT EXISTS idx_sistema_versoes_status_instalacao ON sistema_versoes(status_instalacao);

CREATE INDEX IF NOT EXISTS idx_sistema_modulos_config_nome_modulo ON sistema_modulos_config(nome_modulo);
CREATE INDEX IF NOT EXISTS idx_sistema_modulos_config_status_modulo ON sistema_modulos_config(status_modulo);
CREATE INDEX IF NOT EXISTS idx_sistema_modulos_config_data_ultima_execucao ON sistema_modulos_config(data_ultima_execucao);

CREATE INDEX IF NOT EXISTS idx_sistema_alertas_data_alerta ON sistema_alertas(data_alerta);
CREATE INDEX IF NOT EXISTS idx_sistema_alertas_tipo_alerta ON sistema_alertas(tipo_alerta);
CREATE INDEX IF NOT EXISTS idx_sistema_alertas_categoria_alerta ON sistema_alertas(categoria_alerta);
CREATE INDEX IF NOT EXISTS idx_sistema_alertas_severidade ON sistema_alertas(severidade);
CREATE INDEX IF NOT EXISTS idx_sistema_alertas_status_alerta ON sistema_alertas(status_alerta);
CREATE INDEX IF NOT EXISTS idx_sistema_alertas_prioridade ON sistema_alertas(prioridade);

-- Inserir configuração padrão do sistema
INSERT INTO sistema_config (
    nome_sistema, versao_sistema, nome_empresa, slogan_empresa,
    tema_interface, cor_primaria, cor_secundaria, cor_acento,
    fonte_interface, tamanho_fonte, idioma_padrao, pais_padrao,
    formato_data, formato_hora, formato_numero, formato_moeda,
    separador_decimal, separador_milhar, exigir_senha_supervisor,
    tempo_sessao_minutos, maximo_tentativas_login, tempo_bloqueio_minutos,
    exigir_troca_senha_dias, complexidade_senha, historico_senha,
    backup_automatico, frequencia_backup, horario_backup,
    dias_retention_backup, compactar_backup, backup_nuvem,
    nivel_log, tamanho_maximo_log_mb, dias_retention_log,
    log_rotativo, auditoria_ativa, log_acessos, log_alteracoes,
    cache_ativo, tempo_cache_minutos, pool_conexoes_minimo,
    pool_conexoes_maximo, timeout_conexao_segundos,
    lazy_loading, virtual_scrolling, api_ativa,
    notificacoes_email, notificacoes_sistema, notificacoes_som,
    notificacoes_desktop, formato_papel, orientacao_impressao,
    margem_superior, margem_inferior, margem_esquerda, margem_direita,
    mostrar_logo_relatorio, mostrar_cabecalho_relatorio, mostrar_rodape_relatorio,
    formato_exportacao_padrao, controle_estoque, estoque_negativo,
    controle_lote, controle_validade, dias_alerta_validade,
    estoque_minimo_padrao, alerta_estoque_baixo,
    moeda_padrao, casas_decimais_moeda, forma_pagamento_padrao,
    permitir_venda_prazo, prazo_maximo_venda_dias, juros_mes_padrao,
    multa_atraso_padrao, ambiente_fiscal, serie_nf_padrao,
    emitir_nf_automatico, salvar_xml_nf, enviar_email_nf,
    pdv_numero_padrao, pdv_auto_imprimir, pdv_exigir_cpf,
    pdv_permitir_desconto, pdv_desconto_maximo_percentual,
    pdv_permitir_cancelamento, pdv_exigir_supervisor_cancelamento,
    pdv_mostrar_estoque, pdv_bloquear_sem_estoque,
    sistema_online, modo_manutencao, data_instalacao
) VALUES (
    'Hermes Comercial', '3.6.11', 'Sua Empresa Ltda', 'Sistema ERP Completo',
    'CLARO', '#0066CC', '#FF6600', '#00AA44',
    'Segoe UI', 12, 'PT-BR', 'BR',
    'dd/MM/yyyy', 'HH:mm:ss', '#,##0.00', 'R$ #,##0.00',
    ',', '.', TRUE,
    480, 3, 30,
    90, TRUE, 5,
    TRUE, 'DIARIO', '23:00:00',
    30, TRUE, FALSE,
    'INFO', 100, 90,
    TRUE, TRUE, TRUE, TRUE,
    TRUE, 60, 5,
    20, 30,
    TRUE, TRUE, FALSE,
    TRUE, TRUE, TRUE,
    TRUE, 'A4', 'RETRATO',
    10.00, 10.00, 10.00, 10.00,
    TRUE, TRUE, TRUE,
    'PDF', TRUE, FALSE,
    FALSE, FALSE, 30,
    TRUE, 10,
    'BRL', 2, 'DINHEIRO',
    TRUE, 30, 2.00,
    5.00, 'HOMOLOGACAO', '001',
    FALSE, TRUE, FALSE,
    1, TRUE, TRUE,
    TRUE, 50.00,
    TRUE, TRUE,
    TRUE, FALSE, CURRENT_DATE
) ON CONFLICT DO NOTHING;

-- Inserir versão inicial do sistema
INSERT INTO sistema_versoes (
    versao, data_lancamento, tipo_versao, descricao_versao,
    novas_funcionalidades, correcoes, melhorias,
    status_instalacao, usuario_instalacao, metodo_instalacao
) VALUES (
    '3.6.11', CURRENT_DATE, 'MINOR', 
    'Versão com configurações completas do sistema',
    '• Configurações gerais do sistema
     • Configurações de interface e tema
     • Configurações de segurança e backup
     • Configurações de performance e integração
     • Sistema completo de logs e alertas
     • Controle de versões e módulos',
    '• Correção de bugs de performance
     • Melhoria na interface do usuário
     • Otimização de consultas SQL',
    '• Melhoria na performance geral
     • Interface mais responsiva
     • Melhorias no sistema de logs',
    'CONCLUIDO', CURRENT_USER, 'AUTOMATICO'
) ON CONFLICT DO NOTHING;

-- Inserir módulos padrão do sistema
INSERT INTO sistema_modulos_config (
    nome_modulo, versao_modulo, status_modulo, configuracoes,
    requer_admin, permissoes_necessarias, memoria_necessaria_mb
) VALUES 
('VENDAS', '3.6.11', 'ATIVO', '{"auto_save": true, "timeout": 30000}', 
 FALSE, 'VENDER, EDITAR_VENDA, CANCELAR_VENDA', 256),
('PRODUTOS', '3.6.11', 'ATIVO', '{"cache_enabled": true, "batch_size": 100}', 
 FALSE, 'PRODUTOS_CONSULTAR, PRODUTOS_EDITAR', 128),
('CLIENTES', '3.6.11', 'ATIVO', '{"search_enabled": true, "export_formats": ["PDF", "Excel"]}', 
 FALSE, 'CLIENTES_CONSULTAR, CLIENTES_EDITAR', 64),
('ESTOQUE', '3.6.11', 'ATIVO', '{"real_time": true, "alerts_enabled": true}', 
 FALSE, 'ESTOQUE_CONSULTAR, ESTOQUE_EDITAR', 128),
('FINANCEIRO', '3.6.11', 'ATIVO', '{"conciliacao_enabled": true, "reports_enabled": true}', 
 FALSE, 'FINANCEIRO_CONSULTAR, FINANCEIRO_EDITAR', 256),
('RELATORIOS', '3.6.11', 'ATIVO', '{"export_enabled": true, "scheduling_enabled": false}', 
 FALSE, 'RELATORIOS_CONSULTAR, RELATORIOS_EXPORTAR', 512),
('CONFIGURACOES', '3.6.11', 'ATIVO', '{"backup_enabled": true, "security_enabled": true}', 
 TRUE, 'CONFIGURACOES_ADMINISTRAR', 128),
('USUARIOS', '3.6.11', 'ATIVO', '{"permissions_enabled": true, "audit_enabled": true}', 
 TRUE, 'USUARIOS_ADMINISTRAR', 64)
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE sistema_config IS 'Tabela principal de configurações do sistema';
COMMENT ON COLUMN sistema_config.nome_sistema IS 'Nome do sistema ERP';
COMMENT ON COLUMN sistema_config.versao_sistema IS 'Versão atual do sistema';
COMMENT ON COLUMN sistema_config.nome_empresa IS 'Nome da empresa que utiliza o sistema';
COMMENT ON COLUMN sistema_config.slogan_empresa IS 'Slogan ou descrição da empresa';
COMMENT ON COLUMN sistema_config.tema_interface IS 'Tema da interface (CLARO, ESCURO, AUTO)';
COMMENT ON COLUMN sistema_config.cor_primaria IS 'Cor primária da interface';
COMMENT ON COLUMN sistema_config.cor_secundaria IS 'Cor secundária da interface';
COMMENT ON COLUMN sistema_config.cor_acento IS 'Cor de destaque da interface';
COMMENT ON COLUMN sistema_config.fonte_interface IS 'Fonte principal da interface';
COMMENT ON COLUMN sistema_config.tamanho_fonte IS 'Tamanho padrão da fonte';
COMMENT ON COLUMN sistema_config.idioma_padrao IS 'Idioma padrão do sistema';
COMMENT ON COLUMN sistema_config.pais_padrao IS 'País padrão do sistema';
COMMENT ON COLUMN sistema_config.formato_data IS 'Formato de data padrão';
COMMENT ON COLUMN sistema_config.formato_hora IS 'Formato de hora padrão';
COMMENT ON COLUMN sistema_config.formato_numero IS 'Formato de número padrão';
COMMENT ON COLUMN sistema_config.formato_moeda IS 'Formato de moeda padrão';
COMMENT ON COLUMN sistema_config.separador_decimal IS 'Separador decimal';
COMMENT ON COLUMN sistema_config.separador_milhar IS 'Separador de milhar';
COMMENT ON COLUMN sistema_config.exigir_senha_supervisor IS 'Exigir senha de supervisor';
COMMENT ON COLUMN sistema_config.tempo_sessao_minutos IS 'Tempo da sessão em minutos';
COMMENT ON COLUMN sistema_config.maximo_tentativas_login IS 'Máximo de tentativas de login';
COMMENT ON COLUMN sistema_config.tempo_bloqueio_minutos IS 'Tempo de bloqueio em minutos';
COMMENT ON COLUMN sistema_config.exigir_troca_senha_dias IS 'Dias para exigir troca de senha';
COMMENT ON COLUMN sistema_config.complexidade_senha IS 'Exigir complexidade na senha';
COMMENT ON COLUMN sistema_config.historico_senha IS 'Quantidade de senhas no histórico';
COMMENT ON COLUMN sistema_config.backup_automatico IS 'Backup automático ativado';
COMMENT ON COLUMN sistema_config.frequencia_backup IS 'Frequência do backup';
COMMENT ON COLUMN sistema_config.horario_backup IS 'Horário do backup';
COMMENT ON COLUMN sistema_config.diretorio_backup IS 'Diretório de backup';
COMMENT ON COLUMN sistema_config.dias_retention_backup IS 'Dias de retenção do backup';
COMMENT ON COLUMN sistema_config.compactar_backup IS 'Compactar backup';
COMMENT ON COLUMN sistema_config.backup_nuvem IS 'Backup na nuvem';
COMMENT ON COLUMN sistema_config.provedor_nuvem IS 'Provedor de nuvem';
COMMENT ON COLUMN sistema_config.token_nuvem IS 'Token de acesso à nuvem';
COMMENT ON COLUMN sistema_config.nivel_log IS 'Nível de log do sistema';
COMMENT ON COLUMN sistema_config.arquivo_log IS 'Arquivo de log';
COMMENT ON COLUMN sistema_config.tamanho_maximo_log_mb IS 'Tamanho máximo do log em MB';
COMMENT ON COLUMN sistema_config.dias_retention_log IS 'Dias de retenção do log';
COMMENT ON COLUMN sistema_config.log_rotativo IS 'Log rotativo';
COMMENT ON COLUMN sistema_config.auditoria_ativa IS 'Auditoria ativa';
COMMENT ON COLUMN sistema_config.log_acessos IS 'Log de acessos';
COMMENT ON COLUMN sistema_config.log_alteracoes IS 'Log de alterações';
COMMENT ON COLUMN sistema_config.cache_ativo IS 'Cache ativo';
COMMENT ON COLUMN sistema_config.tempo_cache_minutos IS 'Tempo do cache em minutos';
COMMENT ON COLUMN sistema_config.pool_conexoes_minimo IS 'Mínimo de conexões no pool';
COMMENT ON COLUMN sistema_config.pool_conexoes_maximo IS 'Máximo de conexões no pool';
COMMENT ON COLUMN sistema_config.timeout_conexao_segundos IS 'Timeout de conexão em segundos';
COMMENT ON COLUMN sistema_config.lazy_loading IS 'Lazy loading ativado';
COMMENT ON COLUMN sistema_config.virtual_scrolling IS 'Virtual scrolling ativado';
COMMENT ON COLUMN sistema_config.api_ativa IS 'API ativa';
COMMENT ON COLUMN sistema_config.api_key IS 'Chave da API';
COMMENT ON COLUMN sistema_config.api_secret IS 'Segredo da API';
COMMENT ON COLUMN sistema_config.webhook_url IS 'URL do webhook';
COMMENT ON COLUMN sistema_config.integracao_contador IS 'Integração com contador';
COMMENT ON COLUMN sistema_config.integracao_banco IS 'Integração bancária';
COMMENT ON COLUMN sistema_config.integracao_governo IS 'Integração com governo';
COMMENT ON COLUMN sistema_config.notificacoes_email IS 'Notificações por email';
COMMENT ON COLUMN sistema_config.notificacoes_sistema IS 'Notificações do sistema';
COMMENT ON COLUMN sistema_config.notificacoes_som IS 'Notificações com som';
COMMENT ON COLUMN sistema_config.notificacoes_desktop IS 'Notificações no desktop';
COMMENT ON COLUMN sistema_config.email_smtp IS 'Servidor SMTP';
COMMENT ON COLUMN sistema_config.email_porta IS 'Porta do email';
COMMENT ON COLUMN sistema_config.email_usuario IS 'Usuário do email';
COMMENT ON COLUMN sistema_config.email_senha IS 'Senha do email';
COMMENT ON COLUMN sistema_config.email_ssl IS 'SSL do email';
COMMENT ON COLUMN sistema_config.pdv_numero_padrao IS 'Número do PDV padrão';
COMMENT ON COLUMN sistema_config.pdv_auto_imprimir IS 'Auto imprimir no PDV';
COMMENT ON COLUMN sistema_config.pdv_exigir_cpf IS 'Exigir CPF no PDV';
COMMENT ON COLUMN sistema_config.pdv_permitir_desconto IS 'Permitir desconto no PDV';
COMMENT ON COLUMN sistema_config.pdv_desconto_maximo_percentual IS 'Desconto máximo no PDV';
COMMENT ON COLUMN sistema_config.pdv_permitir_cancelamento IS 'Permitir cancelamento no PDV';
COMMENT ON COLUMN sistema_config.pdv_exigir_supervisor_cancelamento IS 'Exigir supervisor para cancelar';
COMMENT ON COLUMN sistema_config.pdv_mostrar_estoque IS 'Mostrar estoque no PDV';
COMMENT ON COLUMN sistema_config.pdv_bloquear_sem_estoque IS 'Bloquear venda sem estoque';
COMMENT ON COLUMN sistema_config.impressora_padrao IS 'Impressora padrão';
COMMENT ON COLUMN sistema_config.impressora_nao_fiscal IS 'Impressora não fiscal';
COMMENT ON COLUMN sistema_config.impressora_etiquetas IS 'Impressora de etiquetas';
COMMENT ON COLUMN sistema_config.formato_papel IS 'Formato do papel';
COMMENT ON COLUMN sistema_config.orientacao_impressao IS 'Orientação da impressão';
COMMENT ON COLUMN sistema_config.margem_superior IS 'Margem superior';
COMMENT ON COLUMN sistema_config.margem_inferior IS 'Margem inferior';
COMMENT ON COLUMN sistema_config.margem_esquerda IS 'Margem esquerda';
COMMENT ON COLUMN sistema_config.margem_direita IS 'Margem direita';
COMMENT ON COLUMN sistema_config.logo_relatorio IS 'Logo dos relatórios';
COMMENT ON COLUMN sistema_config.cabecalho_relatorio IS 'Cabeçalho dos relatórios';
COMMENT ON COLUMN sistema_config.rodape_relatorio IS 'Rodapé dos relatórios';
COMMENT ON COLUMN sistema_config.mostrar_logo_relatorio IS 'Mostrar logo nos relatórios';
COMMENT ON COLUMN sistema_config.mostrar_cabecalho_relatorio IS 'Mostrar cabeçalho nos relatórios';
COMMENT ON COLUMN sistema_config.mostrar_rodape_relatorio IS 'Mostrar rodapé nos relatórios';
COMMENT ON COLUMN sistema_config.formato_exportacao_padrao IS 'Formato de exportação padrão';
COMMENT ON COLUMN sistema_config.controle_estoque IS 'Controle de estoque';
COMMENT ON COLUMN sistema_config.estoque_negativo IS 'Permitir estoque negativo';
COMMENT ON COLUMN sistema_config.controle_lote IS 'Controle de lote';
COMMENT ON COLUMN sistema_config.controle_validade IS 'Controle de validade';
COMMENT ON COLUMN sistema_config.dias_alerta_validade IS 'Dias para alerta de validade';
COMMENT ON COLUMN sistema_config.estoque_minimo_padrao IS 'Estoque mínimo padrão';
COMMENT ON COLUMN sistema_config.alerta_estoque_baixo IS 'Alertar estoque baixo';
COMMENT ON COLUMN sistema_config.moeda_padrao IS 'Moeda padrão';
COMMENT ON COLUMN sistema_config.casas_decimais_moeda IS 'Casas decimais da moeda';
COMMENT ON COLUMN sistema_config.forma_pagamento_padrao IS 'Forma de pagamento padrão';
COMMENT ON COLUMN sistema_config.permitir_venda_prazo IS 'Permitir venda a prazo';
COMMENT ON COLUMN sistema_config.prazo_maximo_venda_dias IS 'Prazo máximo de venda em dias';
COMMENT ON COLUMN sistema_config.juros_mes_padrao IS 'Juros mensal padrão';
COMMENT ON COLUMN sistema_config.multa_atraso_padrao IS 'Multa por atraso padrão';
COMMENT ON COLUMN sistema_config.ambiente_fiscal IS 'Ambiente fiscal';
COMMENT ON COLUMN sistema_config.serie_nf_padrao IS 'Série da nota fiscal padrão';
COMMENT ON COLUMN sistema_config.numero_nf_atual IS 'Número da nota fiscal atual';
COMMENT ON COLUMN sistema_config.certificado_digital IS 'Caminho do certificado digital';
COMMENT ON COLUMN sistema_config.senha_certificado IS 'Senha do certificado';
COMMENT ON COLUMN sistema_config.validade_certificado IS 'Data de validade do certificado';
COMMENT ON COLUMN sistema_config.emitir_nf_automatico IS 'Emitir nota fiscal automaticamente';
COMMENT ON COLUMN sistema_config.salvar_xml_nf IS 'Salvar XML da nota fiscal';
COMMENT ON COLUMN sistema_config.enviar_email_nf IS 'Enviar nota fiscal por email';
COMMENT ON COLUMN sistema_config.so_detectado IS 'Sistema operacional detectado';
COMMENT ON COLUMN sistema_config.arquitetura IS 'Arquitetura do sistema';
COMMENT ON COLUMN sistema_config.java_version IS 'Versão do Java';
COMMENT ON COLUMN sistema_config.memoria_total_mb IS 'Memória total em MB';
COMMENT ON COLUMN sistema_config.memoria_disponivel_mb IS 'Memória disponível em MB';
COMMENT ON COLUMN sistema_config.disco_total_gb IS 'Disco total em GB';
COMMENT ON COLUMN sistema_config.disco_disponivel_gb IS 'Disco disponível em GB';
COMMENT ON COLUMN sistema_config.ip_servidor IS 'IP do servidor';
COMMENT ON COLUMN sistema_config.porta_servidor IS 'Porta do servidor';
COMMENT ON COLUMN sistema_config.protocolo IS 'Protocolo (HTTP/HTTPS)';
COMMENT ON COLUMN sistema_config.proxy_ativo IS 'Proxy ativo';
COMMENT ON COLUMN sistema_config.proxy_host IS 'Host do proxy';
COMMENT ON COLUMN sistema_config.proxy_porta IS 'Porta do proxy';
COMMENT ON COLUMN sistema_config.proxy_usuario IS 'Usuário do proxy';
COMMENT ON COLUMN sistema_config.proxy_senha IS 'Senha do proxy';
COMMENT ON COLUMN sistema_config.sistema_online IS 'Sistema online';
COMMENT ON COLUMN sistema_config.modo_manutencao IS 'Modo de manutenção';
COMMENT ON COLUMN sistema_config.mensagem_manutencao IS 'Mensagem de manutenção';
COMMENT ON COLUMN sistema_config.data_ultimo_backup IS 'Data do último backup';
COMMENT ON COLUMN sistema_config.data_ultima_verificacao IS 'Data da última verificação';
COMMENT ON COLUMN sistema_config.data_instalacao IS 'Data de instalação';
COMMENT ON COLUMN sistema_config.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN sistema_config.usuario_criacao IS 'Usuário de criação';
COMMENT ON COLUMN sistema_config.data_criacao IS 'Data de criação';
COMMENT ON COLUMN sistema_config.data_atualizacao IS 'Data de atualização';
COMMENT ON COLUMN sistema_config.usuario_atualizacao IS 'Usuário de atualização';

COMMENT ON TABLE sistema_logs IS 'Tabela de logs do sistema';
COMMENT ON COLUMN sistema_logs.nivel_log IS 'Nível do log';
COMMENT ON COLUMN sistema_logs.categoria_log IS 'Categoria do log';
COMMENT ON COLUMN sistema_logs.mensagem IS 'Mensagem do log';
COMMENT ON COLUMN sistema_logs.detalhe IS 'Detalhes do log';
COMMENT ON COLUMN sistema_logs.modulo IS 'Módulo de origem';
COMMENT ON COLUMN sistema_logs.funcionalidade IS 'Funcionalidade de origem';
COMMENT ON COLUMN sistema_logs.classe IS 'Classe de origem';
COMMENT ON COLUMN sistema_logs.metodo IS 'Método de origem';
COMMENT ON COLUMN sistema_logs.linha_codigo IS 'Linha do código';
COMMENT ON COLUMN sistema_logs.usuario IS 'Usuário';
COMMENT ON COLUMN sistema_logs.sessao_id IS 'ID da sessão';
COMMENT ON COLUMN sistema_logs.ip_address IS 'Endereço IP';
COMMENT ON COLUMN sistema_logs.hostname IS 'Hostname';
COMMENT ON COLUMN sistema_logs.user_agent IS 'User agent';
COMMENT ON COLUMN sistema_logs.exception_class IS 'Classe da exceção';
COMMENT ON COLUMN sistema_logs.stack_trace IS 'Stack trace';
COMMENT ON COLUMN sistema_logs.parametros_metodo IS 'Parâmetros do método';
COMMENT ON COLUMN sistema_logs.tempo_execucao_ms IS 'Tempo de execução em ms';
COMMENT ON COLUMN sistema_logs.memoria_utilizada_mb IS 'Memória utilizada em MB';
COMMENT ON COLUMN sistema_logs.data_log IS 'Data do log';
COMMENT ON COLUMN sistema_logs.data_registro IS 'Data de registro';

COMMENT ON TABLE sistema_versoes IS 'Tabela de histórico de versões do sistema';
COMMENT ON COLUMN sistema_versoes.versao IS 'Versão';
COMMENT ON COLUMN sistema_versoes.data_lancamento IS 'Data de lançamento';
COMMENT ON COLUMN sistema_versoes.tipo_versao IS 'Tipo da versão';
COMMENT ON COLUMN sistema_versoes.descricao_versao IS 'Descrição da versão';
COMMENT ON COLUMN sistema_versoes.novas_funcionalidades IS 'Novas funcionalidades';
COMMENT ON COLUMN sistema_versoes.correcoes IS 'Correções';
COMMENT ON COLUMN sistema_versoes.melhorias IS 'Melhorias';
COMMENT ON COLUMN sistema_versoes.breaking_changes IS 'Mudanças que quebram compatibilidade';
COMMENT ON COLUMN sistema_versoes.data_instalacao IS 'Data de instalação';
COMMENT ON COLUMN sistema_versoes.usuario_instalacao IS 'Usuário que instalou';
COMMENT ON COLUMN sistema_versoes.metodo_instalacao IS 'Método de instalação';
COMMENT ON COLUMN sistema_versoes.status_instalacao IS 'Status da instalação';
COMMENT ON COLUMN sistema_versoes.mensagem_erro IS 'Mensagem de erro';

COMMENT ON TABLE sistema_modulos_config IS 'Tabela de configurações de módulos';
COMMENT ON COLUMN sistema_modulos_config.nome_modulo IS 'Nome do módulo';
COMMENT ON COLUMN sistema_modulos_config.versao_modulo IS 'Versão do módulo';
COMMENT ON COLUMN sistema_modulos_config.status_modulo IS 'Status do módulo';
COMMENT ON COLUMN sistema_modulos_config.configuracoes IS 'Configurações do módulo';
COMMENT ON COLUMN sistema_modulos_config.dependencias IS 'Dependências do módulo';
COMMENT ON COLUMN sistema_modulos_config.requer_admin IS 'Requer administrador';
COMMENT ON COLUMN sistema_modulos_config.permissoes_necessarias IS 'Permissões necessárias';
COMMENT ON COLUMN sistema_modulos_config.memoria_necessaria_mb IS 'Memória necessária em MB';
COMMENT ON COLUMN sistema_modulos_config.data_ultima_execucao IS 'Data da última execução';
COMMENT ON COLUMN sistema_modulos_config.total_execucoes IS 'Total de execuções';

COMMENT ON TABLE sistema_alertas IS 'Tabela de alertas do sistema';
COMMENT ON COLUMN sistema_alertas.tipo_alerta IS 'Tipo do alerta';
COMMENT ON COLUMN sistema_alertas.categoria_alerta IS 'Categoria do alerta';
COMMENT ON COLUMN sistema_alertas.titulo_alerta IS 'Título do alerta';
COMMENT ON COLUMN sistema_alertas.mensagem_alerta IS 'Mensagem do alerta';
COMMENT ON COLUMN sistema_alertas.severidade IS 'Severidade do alerta';
COMMENT ON COLUMN sistema_alertas.prioridade IS 'Prioridade do alerta';
COMMENT ON COLUMN sistema_alertas.modulo_origem IS 'Módulo de origem';
COMMENT ON COLUMN sistema_alertas.funcionalidade_origem IS 'Funcionalidade de origem';
COMMENT ON COLUMN sistema_alertas.usuario_afetado IS 'Usuário afetado';
COMMENT ON COLUMN sistema_alertas.acao_recomendada IS 'Ação recomendada';
COMMENT ON COLUMN sistema_alertas.acao_executada IS 'Ação executada';
COMMENT ON COLUMN sistema_alertas.status_alerta IS 'Status do alerta';
COMMENT ON COLUMN sistema_alertas.data_alerta IS 'Data do alerta';
COMMENT ON COLUMN sistema_alertas.data_resolucao IS 'Data de resolução';
COMMENT ON COLUMN sistema_alertas.usuario_resolucao IS 'Usuário que resolveu';
COMMENT ON COLUMN sistema_alertas.notificado_email IS 'Notificado por email';
COMMENT ON COLUMN sistema_alertas.notificado_sistema IS 'Notificado no sistema';
COMMENT ON COLUMN sistema_alertas.data_notificacao IS 'Data da notificação';
