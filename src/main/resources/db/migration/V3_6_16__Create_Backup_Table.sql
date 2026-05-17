-- =====================================================
-- Hermes Comercial v3.6.16 - Migration
-- Criar tabela de Backup e Recuperação
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Backup
CREATE TABLE IF NOT EXISTS backup (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_backup VARCHAR(100) NOT NULL,
    descricao_backup TEXT,
    codigo_backup VARCHAR(50) UNIQUE,
    tipo_backup VARCHAR(20) NOT NULL, -- COMPLETO, INCREMENTAL, DIFERENCIAL, AGENDADO, MANUAL
    categoria_backup VARCHAR(30) NOT NULL, -- SISTEMA, BANCO_DADOS, ARQUIVOS, CONFIGURACOES, LOGS, RELATORIOS
    
    -- Configurações Gerais
    ativo BOOLEAN DEFAULT TRUE,
    automatico BOOLEAN DEFAULT FALSE,
    prioritario BOOLEAN DEFAULT FALSE,
    ordem_execucao INTEGER DEFAULT 0,
    icone_backup VARCHAR(50),
    
    -- Configurações de Agendamento
    agendado BOOLEAN DEFAULT FALSE,
    frequencia_horas INTEGER, -- Frequência em horas para backups automáticos
    proxima_execucao TIMESTAMP,
    ultima_execucao TIMESTAMP,
    timezone_execucao VARCHAR(50) DEFAULT 'America/Sao_Paulo',
    
    -- Configurações de Retenção
    dias_retencion INTEGER DEFAULT 30,
    numero_copias INTEGER DEFAULT 7,
    politica_retention VARCHAR(20) DEFAULT 'DIARIO', -- DIARIO, SEMANAL, MENSAL, ANUAL
    compactar_backup BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Destino
    destino_tipo VARCHAR(20) NOT NULL, -- LOCAL, REDE, NUVEM, FTP, SFTP, S3, GOOGLE_DRIVE, DROPBOX
    caminho_destino VARCHAR(500) NOT NULL,
    servidor_destino VARCHAR(200), -- Para backups em rede/nuvem
    porta_destino INTEGER, -- Para FTP/SFTP
    usuario_destino VARCHAR(100), -- Para autenticação em destino
    senha_destino TEXT, -- Criptografada
    chave_acesso TEXT, -- Para serviços de nuvem (AWS, etc)
    bucket_nome VARCHAR(200), -- Para S3/Google Cloud Storage
    pasta_destino VARCHAR(500), -- Subpasta no destino
    
    -- Configurações de Fonte
    fonte_tipo VARCHAR(20) NOT NULL, -- BANCO_DADOS, DIRETORIO, ARQUIVO, SERVICO
    caminho_fonte VARCHAR(500), -- Caminho da fonte de dados
    banco_tipo VARCHAR(20), -- POSTGRESQL, MYSQL, ORACLE, SQLSERVER, H2
    banco_host VARCHAR(200), -- Host do banco de dados
    banco_porta INTEGER, -- Porta do banco de dados
    banco_nome VARCHAR(100), -- Nome do banco/schema
    banco_usuario VARCHAR(100), -- Usuário do banco
    banco_senha TEXT, -- Senha do banco (criptografada)
    tabelas_excluidas TEXT, -- Tabelas a serem excluídas do backup
    tabelas_incluidas TEXT, -- Tabelas específicas para backup
    
    -- Configurações de Compressão
    compressao_tipo VARCHAR(20) DEFAULT 'GZIP', -- GZIP, ZIP, TAR, BZIP2, LZ4, ZSTD
    nivel_compressao INTEGER DEFAULT 6, -- Nível de compressão (1-9)
    senha_descompactacao TEXT, -- Senha para descompactação
    dividir_volumes BOOLEAN DEFAULT FALSE, -- Dividir backup em volumes menores
    tamanho_volume_mb INTEGER DEFAULT 1024, -- Tamanho máximo de cada volume em MB
    
    -- Configurações de Criptografia
    criptografar_backup BOOLEAN DEFAULT FALSE,
    algoritmo_criptografia VARCHAR(20), -- AES256, RSA, BLOWFISH
    chave_criptografia TEXT, -- Chave de criptografia
    modo_criptografia VARCHAR(20), -- ECB, CBC, GCM, CTR
    
    -- Configurações de Validação
    validar_backup BOOLEAN DEFAULT TRUE,
    verificar_integridade BOOLEAN DEFAULT TRUE,
    gerar_checksum BOOLEAN DEFAULT TRUE,
    algoritmo_checksum VARCHAR(20) DEFAULT 'SHA256', -- MD5, SHA1, SHA256, SHA512
    testar_restauracao BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Notificação
    notificar_sucesso BOOLEAN DEFAULT TRUE,
    notificar_erro BOOLEAN DEFAULT TRUE,
    notificar_aviso BOOLEAN DEFAULT TRUE,
    email_destinatarios TEXT, -- Lista de e-mails separados por vírgula
    template_notificacao VARCHAR(100),
    webhook_url VARCHAR(500), -- URL para webhook de notificação
    
    -- Configurações de Performance
    threads_concorrentes INTEGER DEFAULT 2, -- Número de threads para backup
    buffer_size_mb INTEGER DEFAULT 64, -- Tamanho do buffer em MB
    limite_velocidade_mbps INTEGER, -- Limite de velocidade em Mbps
    prioridade_processo VARCHAR(20) DEFAULT 'NORMAL', -- BAIXA, NORMAL, ALTA, TEMPO_REAL
    
    -- Configurações de Logs
    log_detalhado BOOLEAN DEFAULT FALSE,
    log_nivel VARCHAR(20) DEFAULT 'INFO', -- DEBUG, INFO, WARN, ERROR
    arquivo_log VARCHAR(500), -- Caminho do arquivo de log
    log_rotacionar BOOLEAN DEFAULT TRUE,
    log_tamanho_max_mb INTEGER DEFAULT 100,
    log_numero_arquivos INTEGER DEFAULT 5,
    
    -- Configurações de Recuperação
    ponto_restauracao_padrao BOOLEAN DEFAULT FALSE,
    tempo_recuperacao_maximo INTEGER, -- Tempo máximo em minutos
    estrategia_recuperacao VARCHAR(20) DEFAULT 'COMPLETA', -- COMPLETA, INCREMENTAL, SELETIVA
    
    -- Configurações de Ambiente
    ambiente VARCHAR(20) DEFAULT 'PRODUCAO', -- PRODUCAO, HOMOLOGACAO, DESENVOLVIMENTO, TESTE
    servidor_origem VARCHAR(200), -- Servidor onde o backup é executado
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(255),
    
    -- Configurações de Negócio
    empresa_id INTEGER,
    filial_id INTEGER,
    departamento_id INTEGER,
    responsavel_id INTEGER,
    custo_estimado DECIMAL(10,2), -- Custo estimado do backup
    
    -- Metadados
    tags_backup TEXT, -- Tags em formato JSON
    metadados_json TEXT, -- Metadados adicionais em JSON
    versao_sistema VARCHAR(20),
    compatibilidade_versoes TEXT, -- Versões compatíveis
    
    -- Status e Controle
    status_backup VARCHAR(20) DEFAULT 'ATIVO', -- ATIVO, INATIVO, EM_EXECUCAO, ERRO, PAUSADO
    motivo_status TEXT,
    percentual_conclusao DECIMAL(5,2) DEFAULT 0.00, -- Percentual de conclusão atual
    erro_mensagem TEXT,
    erro_codigo VARCHAR(50),
    erro_stack_trace TEXT,
    
    -- Estatísticas
    total_execucoes INTEGER DEFAULT 0,
    sucesso_execucoes INTEGER DEFAULT 0,
    erro_execucoes INTEGER DEFAULT 0,
    ultimo_tamanho_bytes BIGINT DEFAULT 0,
    ultimo_tempo_execucao INTEGER, -- Segundos
    media_tamanho_bytes BIGINT DEFAULT 0,
    media_tempo_execucao INTEGER DEFAULT 0,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_execucao_sucesso TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_execucao VARCHAR(50),
    
    -- Constraints
    CONSTRAINT chk_tipo_backup CHECK (tipo_backup IN ('COMPLETO', 'INCREMENTAL', 'DIFERENCIAL', 'AGENDADO', 'MANUAL')),
    CONSTRAINT chk_categoria_backup CHECK (categoria_backup IN ('SISTEMA', 'BANCO_DADOS', 'ARQUIVOS', 'CONFIGURACOES', 'LOGS', 'RELATORIOS')),
    CONSTRAINT chk_destino_tipo CHECK (destino_tipo IN ('LOCAL', 'REDE', 'NUVEM', 'FTP', 'SFTP', 'S3', 'GOOGLE_DRIVE', 'DROPBOX')),
    CONSTRAINT chk_fonte_tipo CHECK (fonte_tipo IN ('BANCO_DADOS', 'DIRETORIO', 'ARQUIVO', 'SERVICO')),
    CONSTRAINT chk_banco_tipo CHECK (banco_tipo IN ('POSTGRESQL', 'MYSQL', 'ORACLE', 'SQLSERVER', 'H2')),
    CONSTRAINT chk_compressao_tipo CHECK (compressao_tipo IN ('GZIP', 'ZIP', 'TAR', 'BZIP2', 'LZ4', 'ZSTD')),
    CONSTRAINT chk_algoritmo_criptografia CHECK (algoritmo_criptografia IN ('AES256', 'RSA', 'BLOWFISH')),
    CONSTRAINT chk_modo_criptografia CHECK (modo_criptografia IN ('ECB', 'CBC', 'GCM', 'CTR')),
    CONSTRAINT chk_algoritmo_checksum CHECK (algoritmo_checksum IN ('MD5', 'SHA1', 'SHA256', 'SHA512')),
    CONSTRAINT chk_prioridade_processo CHECK (prioridade_processo IN ('BAIXA', 'NORMAL', 'ALTA', 'TEMPO_REAL')),
    CONSTRAINT chk_ambiente CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO', 'DESENVOLVIMENTO', 'TESTE')),
    CONSTRAINT chk_status_backup CHECK (status_backup IN ('ATIVO', 'INATIVO', 'EM_EXECUCAO', 'ERRO', 'PAUSADO')),
    CONSTRAINT chk_percentual_conclusao CHECK (percentual_conclusao >= 0 AND percentual_conclusao <= 100),
    CONSTRAINT chk_limites_backup CHECK (
        frequencia_horas IS NULL OR frequencia_horas > 0 AND
        dias_retencion IS NULL OR dias_retencion > 0 AND
        numero_copias IS NULL OR numero_copias > 0 AND
        nivel_compressao IS NULL OR (nivel_compressao >= 1 AND nivel_compressao <= 9) AND
        tamanho_volume_mb IS NULL OR tamanho_volume_mb > 0 AND
        threads_concorrentes IS NULL OR threads_concorrentes > 0 AND
        buffer_size_mb IS NULL OR buffer_size_mb > 0 AND
        limite_velocidade_mbps IS NULL OR limite_velocidade_mbps > 0 AND
        tempo_recuperacao_maximo IS NULL OR tempo_recuperacao_maximo > 0 AND
        log_tamanho_max_mb IS NULL OR log_tamanho_max_mb > 0 AND
        log_numero_arquivos IS NULL OR log_numero_arquivos > 0 AND
        custo_estimado IS NULL OR custo_estimado >= 0
    )
);

-- Criar tabela de Histórico de Execuções de Backup
CREATE TABLE IF NOT EXISTS backup_historico (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_backup INTEGER NOT NULL REFERENCES backup(id) ON DELETE CASCADE,
    
    -- Dados da Execução
    data_inicio_execucao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fim_execucao TIMESTAMP,
    duracao_segundos INTEGER,
    status_execucao VARCHAR(20) NOT NULL, -- INICIADO, EM_ANDAMENTO, CONCLUIDO_SUCESSO, CONCLUIDO_ERRO, CANCELADO, TIMEOUT
    
    -- Dados do Backup
    tipo_execucao VARCHAR(20), -- AUTOMATICO, MANUAL, AGENDADO, RESTAURACAO_TESTE
    tamanho_original_bytes BIGINT,
    tamanho_comprimido_bytes BIGINT,
    taxa_compressao DECIMAL(5,2), -- Percentual de compressão
    numero_arquivos INTEGER,
    numero_volumes INTEGER,
    
    -- Dados de Destino
    caminho_completo_backup VARCHAR(1000),
    nome_arquivo_backup VARCHAR(500),
    checksum_gerado VARCHAR(128),
    assinatura_digital TEXT,
    
    -- Dados de Performance
    velocidade_media_mbps DECIMAL(10,2),
    throughput_mb_por_segundo DECIMAL(10,2),
    uso_cpu_percentual DECIMAL(5,2),
    uso_memoria_mb INTEGER,
    uso_disco_gb DECIMAL(10,2),
    
    -- Dados de Contexto
    usuario_execucao VARCHAR(50),
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(255),
    sessao_usuario VARCHAR(100),
    
    -- Dados de Ambiente
    ambiente_execucao VARCHAR(20),
    versao_sistema VARCHAR(20),
    sistema_operacional VARCHAR(100),
    versao_java VARCHAR(20),
    
    -- Logs e Mensagens
    log_execucao TEXT,
    mensagem_sucesso TEXT,
    mensagem_erro TEXT,
    detalhes_erro TEXT,
    stack_trace_erro TEXT,
    warnings_execucao TEXT,
    
    -- Validações
    validacao_checksum BOOLEAN,
    validacao_integridade BOOLEAN,
    validacao_restauracao BOOLEAN,
    resultado_validacao TEXT,
    
    -- Notificações
    notificacoes_enviadas TEXT, -- JSON com notificações enviadas
    status_notificacoes VARCHAR(20), -- ENVIADO, PENDENTE, ERRO
    email_destinatarios TEXT,
    
    -- Metadados
    metadados_execucao TEXT, -- JSON com metadados da execução
    tags_execucao TEXT, -- Tags para classificação
    parametros_json TEXT, -- Parâmetros usados na execução
    
    -- Auditoria
    transacao_id VARCHAR(100),
    batch_id VARCHAR(100),
    job_id VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_status_execucao CHECK (status_execucao IN ('INICIADO', 'EM_ANDAMENTO', 'CONCLUIDO_SUCESSO', 'CONCLUIDO_ERRO', 'CANCELADO', 'TIMEOUT')),
    CONSTRAINT chk_tipo_execucao CHECK (tipo_execucao IN ('AUTOMATICO', 'MANUAL', 'AGENDADO', 'RESTAURACAO_TESTE')),
    CONSTRAINT chk_status_notificacoes CHECK (status_notificacoes IN ('ENVIADO', 'PENDENTE', 'ERRO')),
    CONSTRAINT chk_taxa_compressao CHECK (taxa_compressao >= 0 AND taxa_compressao <= 100),
    CONSTRAINT chk_limites_historico CHECK (
        duracao_segundos IS NULL OR duracao_segundos >= 0 AND
        tamanho_original_bytes IS NULL OR tamanho_original_bytes >= 0 AND
        tamanho_comprimido_bytes IS NULL OR tamanho_comprimido_bytes >= 0 AND
        numero_arquivos IS NULL OR numero_arquivos >= 0 AND
        numero_volumes IS NULL OR numero_volumes >= 0 AND
        velocidade_media_mbps IS NULL OR velocidade_media_mbps >= 0 AND
        throughput_mb_por_segundo IS NULL OR throughput_mb_por_segundo >= 0 AND
        uso_cpu_percentual IS NULL OR (uso_cpu_percentual >= 0 AND uso_cpu_percentual <= 100) AND
        uso_memoria_mb IS NULL OR uso_memoria_mb >= 0 AND
        uso_disco_gb IS NULL OR uso_disco_gb >= 0
    )
);

-- Criar tabela de Itens de Backup
CREATE TABLE IF NOT EXISTS backup_itens (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamentos
    id_backup INTEGER NOT NULL REFERENCES backup(id) ON DELETE CASCADE,
    id_historico INTEGER REFERENCES backup_historico(id) ON DELETE CASCADE,
    
    -- Dados do Item
    nome_item VARCHAR(500) NOT NULL,
    caminho_completo VARCHAR(1000) NOT NULL,
    tipo_item VARCHAR(20) NOT NULL, -- ARQUIVO, DIRETORIO, TABELA, BANCO_DADOS
    tamanho_original_bytes BIGINT,
    tamanho_comprimido_bytes BIGINT,
    data_modificacao TIMESTAMP,
    data_criacao TIMESTAMP,
    permisssoes_arquivo VARCHAR(20),
    
    -- Dados de Compressão
    metodo_compressao VARCHAR(20),
    nivel_compressao INTEGER,
    checksum_item VARCHAR(128),
    
    -- Dados de Criptografia
    criptografado BOOLEAN DEFAULT FALSE,
    algoritmo_criptografia VARCHAR(20),
    vetor_inicializacao TEXT,
    
    -- Dados de Validação
    validado BOOLEAN DEFAULT FALSE,
    data_validacao TIMESTAMP,
    resultado_validacao TEXT,
    
    -- Metadados
    metadados_item TEXT, -- JSON com metadados específicos do item
    atributos_adicionais TEXT, -- Atributos específicos por tipo de item
    
    -- Status
    status_item VARCHAR(20) DEFAULT 'PENDENTE', -- PENDENTE, PROCESSANDO, CONCLUIDO, ERRO, PULADO
    mensagem_status TEXT,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_tipo_item CHECK (tipo_item IN ('ARQUIVO', 'DIRETORIO', 'TABELA', 'BANCO_DADOS')),
    CONSTRAINT chk_status_item CHECK (status_item IN ('PENDENTE', 'PROCESSANDO', 'CONCLUIDO', 'ERRO', 'PULADO')),
    CONSTRAINT chk_nivel_compressao_item CHECK (nivel_compressao IS NULL OR (nivel_compressao >= 1 AND nivel_compressao <= 9)),
    CONSTRAINT chk_limites_itens CHECK (
        tamanho_original_bytes IS NULL OR tamanho_original_bytes >= 0 AND
        tamanho_comprimido_bytes IS NULL OR tamanho_comprimido_bytes >= 0
    )
);

-- Criar tabela de Políticas de Retenção
CREATE TABLE IF NOT EXISTS backup_politica_retention (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_politica VARCHAR(100) NOT NULL UNIQUE,
    descricao_politica TEXT,
    tipo_politica VARCHAR(20) NOT NULL, -- DIARIO, SEMANAL, MENSAL, ANUAL, PERSONALIZADO
    ativa BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Retenção
    dias_manter_diario INTEGER DEFAULT 7,
    semanas_manter_semanal INTEGER DEFAULT 4,
    meses_manter_mensal INTEGER DEFAULT 12,
    anos_manter_anual INTEGER DEFAULT 5,
    
    -- Configurações de Limpeza
    limpar_automaticamente BOOLEAN DEFAULT TRUE,
    hora_limpeza TIME DEFAULT '02:00:00',
    dia_limpeza_semanal INTEGER DEFAULT 1, -- 1=Dom, 2=Seg, ..., 7=Sáb
    dia_limpeza_mensal INTEGER DEFAULT 1,
    
    -- Configurações de Exceções
    excecoes_retention TEXT, -- JSON com exceções à política
    backups_preservar TEXT, -- Lista de backups que nunca devem ser excluídos
    tags_preservar TEXT, -- Tags de backups que devem ser preservados
    
    -- Configurações de Notificação
    notificar_exclusao BOOLEAN DEFAULT TRUE,
    dias_aviso_exclusao INTEGER DEFAULT 3,
    email_aviso_exclusao TEXT,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_politica CHECK (tipo_politica IN ('DIARIO', 'SEMANAL', 'MENSAL', 'ANUAL', 'PERSONALIZADO')),
    CONSTRAINT chk_dia_limpeza_semanal CHECK (dia_limpeza_semanal BETWEEN 1 AND 7),
    CONSTRAINT chk_dia_limpeza_mensal CHECK (dia_limpeza_mensal BETWEEN 1 AND 31),
    CONSTRAINT chk_limites_politica CHECK (
        dias_manter_diario IS NULL OR dias_manter_diario >= 0 AND
        semanas_manter_semanal IS NULL OR semanas_manter_semanal >= 0 AND
        meses_manter_mensal IS NULL OR meses_manter_mensal >= 0 AND
        anos_manter_anual IS NULL OR anos_manter_anual >= 0 AND
        dias_aviso_exclusao IS NULL OR dias_aviso_exclusao >= 0
    )
);

-- Criar tabela de Configurações de Serviços de Nuvem
CREATE TABLE IF NOT EXISTS backup_servicos_nuvem (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_servico VARCHAR(100) NOT NULL UNIQUE,
    descricao_servico TEXT,
    tipo_servico VARCHAR(20) NOT NULL, -- S3, GOOGLE_DRIVE, DROPBOX, AZURE, FTP, SFTP
    ativo BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Conexão
    endpoint_url VARCHAR(500),
    regiao VARCHAR(50),
    bucket_nome VARCHAR(200),
    pasta_base VARCHAR(500),
    
    -- Credenciais
    access_key VARCHAR(500),
    secret_key TEXT, -- Criptografada
    token_acesso TEXT, -- Token OAuth
    refresh_token TEXT,
    expires_token TIMESTAMP,
    
    -- Configurações de Segurança
    ssl_verificado BOOLEAN DEFAULT TRUE,
    fingerprint_ssl VARCHAR(128),
    algoritmo_assinatura VARCHAR(20),
    
    -- Configurações de Performance
    multipart_upload BOOLEAN DEFAULT TRUE,
    parte_tamanho_mb INTEGER DEFAULT 100,
    threads_upload INTEGER DEFAULT 4,
    timeout_conexao INTEGER DEFAULT 300,
    retry_uploads INTEGER DEFAULT 3,
    
    -- Configurações de Compressão
    compressao_upload BOOLEAN DEFAULT TRUE,
    nivel_compressao INTEGER DEFAULT 6,
    
    -- Configurações de Teste
    ultimo_teste_conexao TIMESTAMP,
    resultado_teste VARCHAR(20), -- SUCESSO, ERRO, TIMEOUT
    mensagem_teste TEXT,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_servico CHECK (tipo_servico IN ('S3', 'GOOGLE_DRIVE', 'DROPBOX', 'AZURE', 'FTP', 'SFTP')),
    CONSTRAINT chk_resultado_teste CHECK (resultado_teste IN ('SUCESSO', 'ERRO', 'TIMEOUT')),
    CONSTRAINT chk_nivel_compressao_servico CHECK (nivel_compressao IS NULL OR (nivel_compressao >= 1 AND nivel_compressao <= 9)),
    CONSTRAINT chk_limites_servico CHECK (
        parte_tamanho_mb IS NULL OR parte_tamanho_mb > 0 AND
        threads_upload IS NULL OR threads_upload > 0 AND
        timeout_conexao IS NULL OR timeout_conexao > 0 AND
        retry_uploads IS NULL OR retry_uploads >= 0
    )
);

-- Criar tabela de Relatórios de Backup
CREATE TABLE IF NOT EXISTS backup_relatorios (
    id SERIAL PRIMARY KEY,
    
    -- Dados do Relatório
    nome_relatorio VARCHAR(100) NOT NULL,
    tipo_relatorio VARCHAR(20) NOT NULL, -- EXECUCAO, PERFORMANCE, ESPACO, VALIDACAO, TENDENCIA
    periodo_inicio DATE,
    periodo_fim DATE,
    data_geracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Dados do Relatório
    dados_json TEXT, -- Dados do relatório em formato JSON
    resumo_texto TEXT, -- Resumo em texto
    graficos_dados TEXT, -- Dados para gráficos em JSON
    
    -- Filtros Aplicados
    filtros_json TEXT, -- Filtros aplicados no relatório
    parametros_geracao TEXT, -- Parâmetros usados na geração
    
    -- Formatos de Saída
    formato_saida VARCHAR(20) DEFAULT 'HTML', -- HTML, PDF, CSV, JSON, XML
    arquivo_gerado VARCHAR(500),
    tamanho_arquivo_bytes BIGINT,
    
    -- Configurações de Envio
    email_enviado BOOLEAN DEFAULT FALSE,
    email_destinatarios TEXT,
    data_email_envio TIMESTAMP,
    
    -- Auditoria
    usuario_geracao VARCHAR(50) DEFAULT CURRENT_USER,
    ip_geracao VARCHAR(45),
    
    -- Constraints
    CONSTRAINT chk_tipo_relatorio CHECK (tipo_relatorio IN ('EXECUCAO', 'PERFORMANCE', 'ESPACO', 'VALIDACAO', 'TENDENCIA')),
    CONSTRAINT chk_formato_saida CHECK (formato_saida IN ('HTML', 'PDF', 'CSV', 'JSON', 'XML')),
    CONSTRAINT chk_limites_relatorios CHECK (
        tamanho_arquivo_bytes IS NULL OR tamanho_arquivo_bytes >= 0
    )
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_backup_nome_backup ON backup(nome_backup);
CREATE INDEX IF NOT EXISTS idx_backup_codigo_backup ON backup(codigo_backup);
CREATE INDEX IF NOT EXISTS idx_backup_tipo_backup ON backup(tipo_backup);
CREATE INDEX IF NOT EXISTS idx_backup_categoria_backup ON backup(categoria_backup);
CREATE INDEX IF NOT EXISTS idx_backup_ativo ON backup(ativo);
CREATE INDEX IF NOT EXISTS idx_backup_automatico ON backup(automatico);
CREATE INDEX IF NOT EXISTS idx_backup_agendado ON backup(agendado);
CREATE INDEX IF NOT EXISTS idx_backup_destino_tipo ON backup(destino_tipo);
CREATE INDEX IF NOT EXISTS idx_backup_fonte_tipo ON backup(fonte_tipo);
CREATE INDEX IF NOT EXISTS idx_backup_status_backup ON backup(status_backup);
CREATE INDEX IF NOT EXISTS idx_backup_ambiente ON backup(ambiente);
CREATE INDEX IF NOT EXISTS idx_backup_empresa_id ON backup(empresa_id);
CREATE INDEX IF NOT EXISTS idx_backup_filial_id ON backup(filial_id);
CREATE INDEX IF NOT EXISTS idx_backup_proxima_execucao ON backup(proxima_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_data_ultima_atualizacao ON backup(data_ultima_atualizacao);

CREATE INDEX IF NOT EXISTS idx_backup_historico_id_backup ON backup_historico(id_backup);
CREATE INDEX IF NOT EXISTS idx_backup_historico_data_inicio_execucao ON backup_historico(data_inicio_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_historico_status_execucao ON backup_historico(status_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_historico_tipo_execucao ON backup_historico(tipo_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_historico_usuario_execucao ON backup_historico(usuario_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_historico_ambiente_execucao ON backup_historico(ambiente_execucao);
CREATE INDEX IF NOT EXISTS idx_backup_historico_transacao_id ON backup_historico(transacao_id);
CREATE INDEX IF NOT EXISTS idx_backup_historico_job_id ON backup_historico(job_id);

CREATE INDEX IF NOT EXISTS idx_backup_itens_id_backup ON backup_itens(id_backup);
CREATE INDEX IF NOT EXISTS idx_backup_itens_id_historico ON backup_itens(id_historico);
CREATE INDEX IF NOT EXISTS idx_backup_itens_tipo_item ON backup_itens(tipo_item);
CREATE INDEX IF NOT EXISTS idx_backup_itens_status_item ON backup_itens(status_item);
CREATE INDEX IF NOT EXISTS idx_backup_itens_nome_item ON backup_itens(nome_item);
CREATE INDEX IF NOT EXISTS idx_backup_itens_data_cadastro ON backup_itens(data_cadastro);

CREATE INDEX IF NOT EXISTS idx_backup_politica_retention_nome_politica ON backup_politica_retention(nome_politica);
CREATE INDEX IF NOT EXISTS idx_backup_politica_retention_tipo_politica ON backup_politica_retention(tipo_politica);
CREATE INDEX IF NOT EXISTS idx_backup_politica_retention_ativa ON backup_politica_retention(ativa);
CREATE INDEX IF NOT EXISTS idx_backup_politica_retention_limpar_automaticamente ON backup_politica_retention(limpar_automaticamente);

CREATE INDEX IF NOT EXISTS idx_backup_servicos_nuvem_nome_servico ON backup_servicos_nuvem(nome_servico);
CREATE INDEX IF NOT EXISTS idx_backup_servicos_nuvem_tipo_servico ON backup_servicos_nuvem(tipo_servico);
CREATE INDEX IF NOT EXISTS idx_backup_servicos_nuvem_ativo ON backup_servicos_nuvem(ativo);
CREATE INDEX IF NOT EXISTS idx_backup_servicos_nuvem_ultimo_teste_conexao ON backup_servicos_nuvem(ultimo_teste_conexao);
CREATE INDEX IF NOT EXISTS idx_backup_servicos_nuvem_resultado_teste ON backup_servicos_nuvem(resultado_teste);

CREATE INDEX IF NOT EXISTS idx_backup_relatorios_nome_relatorio ON backup_relatorios(nome_relatorio);
CREATE INDEX IF NOT EXISTS idx_backup_relatorios_tipo_relatorio ON backup_relatorios(tipo_relatorio);
CREATE INDEX IF NOT EXISTS idx_backup_relatorios_periodo_inicio ON backup_relatorios(periodo_inicio);
CREATE INDEX IF NOT EXISTS idx_backup_relatorios_periodo_fim ON backup_relatorios(periodo_fim);
CREATE INDEX IF NOT EXISTS idx_backup_relatorios_data_geracao ON backup_relatorios(data_geracao);
CREATE INDEX IF NOT EXISTS idx_backup_relatorios_usuario_geracao ON backup_relatorios(usuario_geracao);

-- Inserir dados padrão
INSERT INTO backup (
    nome_backup, descricao_backup, codigo_backup, tipo_backup, categoria_backup,
    ativo, automatico, prioritario, ordem_execucao,
    agendado, frequencia_horas, dias_retencion, numero_copias, politica_retention,
    destino_tipo, caminho_destino, compactar_backup,
    compressao_tipo, nivel_compressao,
    validar_backup, verificar_integridade, gerar_checksum, algoritmo_checksum,
    notificar_sucesso, notificar_erro, ambiente,
    threads_concorrentes, buffer_size_mb, prioridade_processo,
    log_detalhado, log_nivel, log_rotacionar, log_tamanho_max_mb, log_numero_arquivos,
    status_backup, usuario_cadastro
) VALUES 
('Backup Completo do Sistema', 'Backup completo de todo o sistema incluindo banco de dados e arquivos', 'BACKUP_COMPLETO_SISTEMA', 'COMPLETO', 'SISTEMA',
 TRUE, TRUE, TRUE, 1,
 TRUE, 24, 30, 7, 'DIARIO',
 'LOCAL', '/backup/sistema/completo', TRUE,
 'GZIP', 6,
 TRUE, TRUE, TRUE, 'SHA256',
 TRUE, TRUE, 'PRODUCAO',
 2, 64, 'NORMAL',
 FALSE, 'INFO', TRUE, 100, 5,
 'ATIVO', CURRENT_USER),

('Backup Incremental Banco de Dados', 'Backup incremental apenas dos dados alterados no banco de dados', 'BACKUP_INCREMENTAL_BD', 'INCREMENTAL', 'BANCO_DADOS',
 TRUE, TRUE, FALSE, 2,
 TRUE, 6, 30, 14, 'DIARIO',
 'LOCAL', '/backup/banco/incremental', TRUE,
 'GZIP', 6,
 TRUE, TRUE, TRUE, 'SHA256',
 TRUE, TRUE, 'PRODUCAO',
 2, 64, 'NORMAL',
 FALSE, 'INFO', TRUE, 100, 5,
 'ATIVO', CURRENT_USER),

('Backup de Logs do Sistema', 'Backup dos arquivos de log para análise e auditoria', 'BACKUP_LOGS_SISTEMA', 'MANUAL', 'LOGS',
 TRUE, FALSE, FALSE, 3,
 FALSE, NULL, 90, 4, 'SEMANAL',
 'LOCAL', '/backup/logs', TRUE,
 'GZIP', 6,
 TRUE, FALSE, TRUE, 'SHA256',
 TRUE, TRUE, 'PRODUCAO',
 1, 32, 'BAIXA',
 FALSE, 'INFO', TRUE, 50, 3,
 'ATIVO', CURRENT_USER),

('Backup de Configurações', 'Backup das configurações do sistema e parâmetros', 'BACKUP_CONFIGURACOES', 'MANUAL', 'CONFIGURACOES',
 TRUE, FALSE, FALSE, 4,
 FALSE, NULL, 30, 10, 'DIARIO',
 'LOCAL', '/backup/configuracoes', TRUE,
 'ZIP', 6,
 TRUE, TRUE, TRUE, 'SHA256',
 TRUE, TRUE, 'PRODUCAO',
 1, 32, 'NORMAL',
 FALSE, 'INFO', TRUE, 50, 3,
 'ATIVO', CURRENT_USER),

('Backup de Relatórios', 'Backup dos relatórios gerados pelo sistema', 'BACKUP_RELATORIOS', 'MANUAL', 'RELATORIOS',
 TRUE, FALSE, FALSE, 5,
 FALSE, NULL, 60, 6, 'MENSAL',
 'LOCAL', '/backup/relatorios', TRUE,
 'GZIP', 6,
 TRUE, FALSE, TRUE, 'SHA256',
 TRUE, TRUE, 'PRODUCAO',
 1, 32, 'BAIXA',
 FALSE, 'INFO', TRUE, 50, 3,
 'ATIVO', CURRENT_USER)
ON CONFLICT DO NOTHING;

-- Inserir políticas de retenção padrão
INSERT INTO backup_politica_retention (
    nome_politica, descricao_politica, tipo_politica, ativa,
    dias_manter_diario, semanas_manter_semanal, meses_manter_mensal, anos_manter_anual,
    limpar_automaticamente, hora_limpeza, dia_limpeza_semanal, dia_limpeza_mensal,
    notificar_exclusao, dias_aviso_exclusao, usuario_cadastro
) VALUES 
('Política Padrão Diária', 'Mantém backups diários por 7 dias, semanais por 4 semanas e mensais por 12 meses', 'DIARIO', TRUE,
 7, 4, 12, 5,
 TRUE, '02:00:00', 1, 1,
 TRUE, 3, CURRENT_USER),

('Política de Longo Prazo', 'Mantém backups por mais tempo para requisitos de compliance', 'ANUAL', TRUE,
 30, 12, 36, 10,
 TRUE, '03:00:00', 2, 1,
 TRUE, 7, CURRENT_USER),

('Política de Desenvolvimento', 'Política mais agressiva para ambiente de desenvolvimento', 'PERSONALIZADO', TRUE,
 3, 2, 6, 1,
 TRUE, '01:00:00', 1, 1,
 FALSE, 1, CURRENT_USER)
ON CONFLICT DO NOTHING;

-- Inserir serviços de nuvem exemplo
INSERT INTO backup_servicos_nuvem (
    nome_servico, descricao_servico, tipo_servico, ativo,
    endpoint_url, regiao, bucket_nome, pasta_base,
    access_key, secret_key,
    ssl_verificado, multipart_upload, parte_tamanho_mb, threads_upload,
    timeout_conexao, retry_uploads, compressao_upload, nivel_compressao,
    usuario_cadastro
) VALUES 
('AWS S3 Hermes Backup', 'Serviço de backup na nuvem AWS S3', 'S3', TRUE,
 'https://s3.amazonaws.com', 'us-east-1', 'hermes-backup-bucket', 'backups/',
 'AKIAIOSFODNN7EXAMPLE', 'wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY',
 TRUE, TRUE, 100, 4,
 300, 3, TRUE, 6,
 CURRENT_USER),

('Google Drive Backup', 'Serviço de backup no Google Drive', 'GOOGLE_DRIVE', FALSE,
 NULL, NULL, NULL, 'Hermes Comercial/Backups',
 NULL, NULL,
 TRUE, FALSE, 50, 2,
 180, 2, TRUE, 6,
 CURRENT_USER)
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE backup IS 'Tabela de configurações de backup do sistema';
COMMENT ON COLUMN backup.nome_backup IS 'Nome identificador do backup';
COMMENT ON COLUMN backup.descricao_backup IS 'Descrição detalhada do backup';
COMMENT ON COLUMN backup.codigo_backup IS 'Código único do backup';
COMMENT ON COLUMN backup.tipo_backup IS 'Tipo do backup (COMPLETO, INCREMENTAL, DIFERENCIAL, AGENDADO, MANUAL)';
COMMENT ON COLUMN backup.categoria_backup IS 'Categoria dos dados do backup';
COMMENT ON COLUMN backup.ativo IS 'Indica se o backup está ativo';
COMMENT ON COLUMN backup.automatico IS 'Indica se o backup é executado automaticamente';
COMMENT ON COLUMN backup.prioritario IS 'Indica se o backup tem prioridade alta';
COMMENT ON COLUMN backup.ordem_execucao IS 'Ordem de execução do backup';
COMMENT ON COLUMN backup.icone_backup IS 'Ícone representativo do backup';
COMMENT ON COLUMN backup.agendado IS 'Indica se o backup é agendado';
COMMENT ON COLUMN backup.frequencia_horas IS 'Frequência em horas para backups automáticos';
COMMENT ON COLUMN backup.proxima_execucao IS 'Data e hora da próxima execução programada';
COMMENT ON COLUMN backup.ultima_execucao IS 'Data e hora da última execução';
COMMENT ON COLUMN backup.timezone_execucao IS 'Timezone para agendamento do backup';
COMMENT ON COLUMN backup.dias_retencion IS 'Dias para manter os arquivos de backup';
COMMENT ON COLUMN backup.numero_copias IS 'Número máximo de cópias a manter';
COMMENT ON COLUMN backup.politica_retention IS 'Política de retenção dos backups';
COMMENT ON COLUMN backup.compactar_backup IS 'Indica se o backup deve ser compactado';
COMMENT ON COLUMN backup.destino_tipo IS 'Tipo de destino do backup';
COMMENT ON COLUMN backup.caminho_destino IS 'Caminho completo do destino do backup';
COMMENT ON COLUMN backup.servidor_destino IS 'Servidor de destino para backups remotos';
COMMENT ON COLUMN backup.porta_destino IS 'Porta do servidor de destino';
COMMENT ON COLUMN backup.usuario_destino IS 'Usuário para acesso ao destino';
COMMENT ON COLUMN backup.senha_destino IS 'Senha para acesso ao destino (criptografada)';
COMMENT ON COLUMN backup.chave_acesso IS 'Chave de acesso para serviços de nuvem';
COMMENT ON COLUMN backup.bucket_nome IS 'Nome do bucket para serviços S3/Google Cloud';
COMMENT ON COLUMN backup.pasta_destino IS 'Subpasta no destino do backup';
COMMENT ON COLUMN backup.fonte_tipo IS 'Tipo da fonte de dados do backup';
COMMENT ON COLUMN backup.caminho_fonte IS 'Caminho da fonte de dados';
COMMENT ON COLUMN backup.banco_tipo IS 'Tipo do banco de dados';
COMMENT ON COLUMN backup.banco_host IS 'Host do banco de dados';
COMMENT ON COLUMN backup.banco_porta IS 'Porta do banco de dados';
COMMENT ON COLUMN backup.banco_nome IS 'Nome do banco/schema';
COMMENT ON COLUMN backup.banco_usuario IS 'Usuário do banco de dados';
COMMENT ON COLUMN backup.banco_senha IS 'Senha do banco (criptografada)';
COMMENT ON COLUMN backup.tabelas_excluidas IS 'Tabelas a serem excluídas do backup';
COMMENT ON COLUMN backup.tabelas_incluidas IS 'Tabelas específicas para backup';
COMMENT ON COLUMN backup.compressao_tipo IS 'Tipo de compressão do backup';
COMMENT ON COLUMN backup.nivel_compressao IS 'Nível de compressão (1-9)';
COMMENT ON COLUMN backup.senha_descompactacao IS 'Senha para descompactação';
COMMENT ON COLUMN backup.dividir_volumes IS 'Dividir backup em volumes menores';
COMMENT ON COLUMN backup.tamanho_volume_mb IS 'Tamanho máximo de cada volume em MB';
COMMENT ON COLUMN backup.criptografar_backup IS 'Indica se o backup deve ser criptografado';
COMMENT ON COLUMN backup.algoritmo_criptografia IS 'Algoritmo de criptografia';
COMMENT ON COLUMN backup.chave_criptografia IS 'Chave de criptografia';
COMMENT ON COLUMN backup.modo_criptografia IS 'Modo de criptografia';
COMMENT ON COLUMN backup.validar_backup IS 'Validar backup após execução';
COMMENT ON COLUMN backup.verificar_integridade IS 'Verificar integridade dos dados';
COMMENT ON COLUMN backup.gerar_checksum IS 'Gerar checksum do backup';
COMMENT ON COLUMN backup.algoritmo_checksum IS 'Algoritmo do checksum';
COMMENT ON COLUMN backup.testar_restauracao IS 'Testar restauração do backup';
COMMENT ON COLUMN backup.notificar_sucesso IS 'Notificar em caso de sucesso';
COMMENT ON COLUMN backup.notificar_erro IS 'Notificar em caso de erro';
COMMENT ON COLUMN backup.notificar_aviso IS 'Notificar em caso de avisos';
COMMENT ON COLUMN backup.email_destinatarios IS 'Lista de e-mails para notificação';
COMMENT ON COLUMN backup.template_notificacao IS 'Template de notificação';
COMMENT ON COLUMN backup.webhook_url IS 'URL para webhook de notificação';
COMMENT ON COLUMN backup.threads_concorrentes IS 'Número de threads para backup';
COMMENT ON COLUMN backup.buffer_size_mb IS 'Tamanho do buffer em MB';
COMMENT ON COLUMN backup.limite_velocidade_mbps IS 'Limite de velocidade em Mbps';
COMMENT ON COLUMN backup.prioridade_processo IS 'Prioridade do processo';
COMMENT ON COLUMN backup.log_detalhado IS 'Gerar log detalhado';
COMMENT ON COLUMN backup.log_nivel IS 'Nível do log';
COMMENT ON COLUMN backup.arquivo_log IS 'Caminho do arquivo de log';
COMMENT ON COLUMN backup.log_rotacionar IS 'Rotacionar arquivo de log';
COMMENT ON COLUMN backup.log_tamanho_max_mb IS 'Tamanho máximo do arquivo de log';
COMMENT ON COLUMN backup.log_numero_arquivos IS 'Número máximo de arquivos de log';
COMMENT ON COLUMN backup.ponto_restauracao_padrao IS 'Ponto de restauração padrão';
COMMENT ON COLUMN backup.tempo_recuperacao_maximo IS 'Tempo máximo de recuperação em minutos';
COMMENT ON COLUMN backup.estrategia_recuperacao IS 'Estratégia de recuperação';
COMMENT ON COLUMN backup.ambiente IS 'Ambiente do backup';
COMMENT ON COLUMN backup.servidor_origem IS 'Servidor onde o backup é executado';
COMMENT ON COLUMN backup.ip_origem IS 'IP de origem do backup';
COMMENT ON COLUMN backup.hostname_origem IS 'Hostname de origem';
COMMENT ON COLUMN backup.empresa_id IS 'ID da empresa';
COMMENT ON COLUMN backup.filial_id IS 'ID da filial';
COMMENT ON COLUMN backup.departamento_id IS 'ID do departamento';
COMMENT ON COLUMN backup.responsavel_id IS 'ID do responsável';
COMMENT ON COLUMN backup.custo_estimado IS 'Custo estimado do backup';
COMMENT ON COLUMN backup.tags_backup IS 'Tags do backup em JSON';
COMMENT ON COLUMN backup.metadados_json IS 'Metadados adicionais em JSON';
COMMENT ON COLUMN backup.versao_sistema IS 'Versão do sistema';
COMMENT ON COLUMN backup.compatibilidade_versoes IS 'Versões compatíveis';
COMMENT ON COLUMN backup.status_backup IS 'Status atual do backup';
COMMENT ON COLUMN backup.motivo_status IS 'Motivo do status atual';
COMMENT ON COLUMN backup.percentual_conclusao IS 'Percentual de conclusão atual';
COMMENT ON COLUMN backup.erro_mensagem IS 'Mensagem de erro';
COMMENT ON COLUMN backup.erro_codigo IS 'Código do erro';
COMMENT ON COLUMN backup.erro_stack_trace IS 'Stack trace do erro';
COMMENT ON COLUMN backup.total_execucoes IS 'Total de execuções';
COMMENT ON COLUMN backup.sucesso_execucoes IS 'Execuções com sucesso';
COMMENT ON COLUMN backup.erro_execucoes IS 'Execuções com erro';
COMMENT ON COLUMN backup.ultimo_tamanho_bytes IS 'Tamanho do último backup em bytes';
COMMENT ON COLUMN backup.ultimo_tempo_execucao IS 'Tempo da última execução em segundos';
COMMENT ON COLUMN backup.media_tamanho_bytes IS 'Tamanho médio dos backups em bytes';
COMMENT ON COLUMN backup.media_tempo_execucao IS 'Tempo médio de execução em segundos';
COMMENT ON COLUMN backup.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN backup.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN backup.data_ultima_execucao_sucesso IS 'Data da última execução com sucesso';
COMMENT ON COLUMN backup.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN backup.usuario_ultima_atualizacao IS 'Usuário da última atualização';
COMMENT ON COLUMN backup.usuario_ultima_execucao IS 'Usuário da última execução';

COMMENT ON TABLE backup_historico IS 'Histórico de execuções de backup';
COMMENT ON TABLE backup_itens IS 'Itens individuais dos backups';
COMMENT ON TABLE backup_politica_retention IS 'Políticas de retenção de backups';
COMMENT ON TABLE backup_servicos_nuvem IS 'Configurações de serviços de nuvem para backup';
COMMENT ON TABLE backup_relatorios IS 'Relatórios gerados sobre backups';
