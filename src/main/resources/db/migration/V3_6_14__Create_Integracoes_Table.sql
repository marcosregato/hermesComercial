-- =====================================================
-- Hermes Comercial v3.6.14 - Migration
-- Criar tabela de Integrações
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Integrações
CREATE TABLE IF NOT EXISTS integracoes (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_integracao VARCHAR(100) NOT NULL UNIQUE,
    descricao_integracao TEXT,
    codigo_integracao VARCHAR(20) UNIQUE,
    sigla_integracao VARCHAR(10),
    
    -- Tipo e Categoria
    tipo_integracao VARCHAR(30) NOT NULL, -- API, WEBSERVICE, DATABASE, FILE, EMAIL, SMS, PAYMENT, ERP, CRM, ECOMMERCE
    categoria_integracao VARCHAR(30) NOT NULL, -- FINANCEIRO, VENDAS, ESTOQUE, CLIENTE, PRODUTO, LOGISTICA, MARKETING, RH
    subcategoria_integracao VARCHAR(30), -- BOLETO, CARTAO, PIX, NOTA_FISCAL, NFCE, NFE, SAT, MFE
    
    -- Configurações Gerais
    ativa BOOLEAN DEFAULT TRUE,
    padrao BOOLEAN DEFAULT FALSE,
    ordem_exibicao INTEGER DEFAULT 0,
    icone_integracao VARCHAR(100),
    cor_integracao VARCHAR(7) DEFAULT '#000000',
    
    -- Configurações de Conexão
    tipo_conexao VARCHAR(20) NOT NULL, -- HTTP, HTTPS, FTP, SFTP, SSH, JDBC, ODBC, SMTP, POP3, IMAP
    host_servidor VARCHAR(255),
    porta_conexao INTEGER,
    contexto_aplicacao VARCHAR(100),
    protocolo_comunicacao VARCHAR(20), -- REST, SOAP, GraphQL, WebSocket, TCP, UDP
    
    -- Configurações de Autenticação
    tipo_autenticacao VARCHAR(20) NOT NULL, -- NONE, BASIC, BEARER, OAUTH2, API_KEY, JWT, CERTIFICATE, SSH_KEY
    usuario_acesso VARCHAR(100),
    senha_acesso TEXT,
    token_acesso TEXT,
    api_key VARCHAR(500),
    api_secret VARCHAR(500),
    
    -- Configurações OAuth2
    oauth2_client_id VARCHAR(100),
    oauth2_client_secret VARCHAR(255),
    oauth2_authorization_url VARCHAR(500),
    oauth2_token_url VARCHAR(500),
    oauth2_scope VARCHAR(200),
    oauth2_grant_type VARCHAR(20), -- AUTHORIZATION_CODE, CLIENT_CREDENTIALS, PASSWORD, REFRESH_TOKEN
    
    -- Configurações de Certificado
    certificado_path VARCHAR(500),
    certificado_password VARCHAR(100),
    certificado_alias VARCHAR(100),
    truststore_path VARCHAR(500),
    truststore_password VARCHAR(100),
    
    -- Configurações de Proxy
    usa_proxy BOOLEAN DEFAULT FALSE,
    proxy_host VARCHAR(255),
    proxy_porta INTEGER,
    proxy_usuario VARCHAR(100),
    proxy_senha VARCHAR(255),
    proxy_tipo VARCHAR(20), -- HTTP, SOCKS4, SOCKS5
    
    -- Configurações de Timeout e Retentativas
    timeout_conexao INTEGER DEFAULT 30,
    timeout_leitura INTEGER DEFAULT 60,
    timeout_escrita INTEGER DEFAULT 30,
    maximo_retries INTEGER DEFAULT 3,
    intervalo_retries INTEGER DEFAULT 5,
    retry_backoff_multiplier DECIMAL(3,2) DEFAULT 2.0,
    
    -- Configurações de Formato
    formato_requisicao VARCHAR(20) DEFAULT 'JSON', -- JSON, XML, FORM_DATA, TEXT, BINARY
    formato_resposta VARCHAR(20) DEFAULT 'JSON',
    encoding_dados VARCHAR(20) DEFAULT 'UTF-8',
    content_type VARCHAR(100) DEFAULT 'application/json',
    accept_type VARCHAR(100) DEFAULT 'application/json',
    
    -- Configurações de Segurança
    ssl_verificado BOOLEAN DEFAULT TRUE,
    tls_version VARCHAR(20), -- TLS_1_2, TLS_1_3
    cipher_suite VARCHAR(100),
    hmac_algoritmo VARCHAR(50), -- HMAC_SHA256, HMAC_SHA512
    criptografia_dados VARCHAR(20), -- AES_256, AES_128, RSA_2048
    
    -- Configurações de Ambiente
    ambiente VARCHAR(20) DEFAULT 'PRODUCAO', -- PRODUCAO, HOMOLOGACAO, DESENVOLVIMENTO, TESTE
    debug_mode BOOLEAN DEFAULT FALSE,
    log_detalhado BOOLEAN DEFAULT FALSE,
    log_persistente BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Webhook
    webhook_enabled BOOLEAN DEFAULT FALSE,
    webhook_url VARCHAR(500),
    webhook_secret VARCHAR(255),
    webhook_eventos TEXT, -- Lista de eventos separados por vírgula
    webhook_metodo VARCHAR(10) DEFAULT 'POST', -- GET, POST, PUT, DELETE
    webhook_headers TEXT, -- Headers customizados em formato JSON
    webhook_timeout INTEGER DEFAULT 30,
    
    -- Configurações de Rate Limiting
    rate_limit_enabled BOOLEAN DEFAULT FALSE,
    rate_limit_requests INTEGER DEFAULT 100,
    rate_limit_period INTEGER DEFAULT 60, -- segundos
    rate_limit_strategy VARCHAR(20) DEFAULT 'FIXED_WINDOW', -- FIXED_WINDOW, SLIDING_WINDOW, TOKEN_BUCKET
    
    -- Configurações de Cache
    cache_enabled BOOLEAN DEFAULT FALSE,
    cache_ttl INTEGER DEFAULT 300, -- segundos
    cache_strategy VARCHAR(20) DEFAULT 'MEMORY', -- MEMORY, REDIS, MEMCACHED, DATABASE
    cache_max_size INTEGER DEFAULT 1000,
    cache_eviction_policy VARCHAR(20) DEFAULT 'LRU', -- LRU, LFU, FIFO
    
    -- Configurações de Processamento
    processamento_assincrono BOOLEAN DEFAULT FALSE,
    queue_name VARCHAR(100),
    max_concurrent_requests INTEGER DEFAULT 10,
    batch_size INTEGER DEFAULT 100,
    batch_timeout INTEGER DEFAULT 30,
    
    -- Configurações de Validação
    valida_schema BOOLEAN DEFAULT TRUE,
    schema_path VARCHAR(500),
    valida_certificado BOOLEAN DEFAULT FALSE,
    valida_assinatura BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Transformação
    transformacao_request_enabled BOOLEAN DEFAULT FALSE,
    transformacao_request_template TEXT,
    transformacao_response_enabled BOOLEAN DEFAULT FALSE,
    transformacao_response_template TEXT,
    
    -- Configurações de Mapeamento
    mapeamento_campos_enabled BOOLEAN DEFAULT FALSE,
    mapeamento_campos_json TEXT, -- Mapeamento em formato JSON
    mapeamento_valores_json TEXT, -- Mapeamento de valores em formato JSON
    
    -- Configurações de Monitoramento
    monitoramento_enabled BOOLEAN DEFAULT TRUE,
    health_check_enabled BOOLEAN DEFAULT TRUE,
    health_check_interval INTEGER DEFAULT 60, -- segundos
    health_check_endpoint VARCHAR(500),
    health_check_timeout INTEGER DEFAULT 10,
    
    -- Configurações de Logs
    log_requests BOOLEAN DEFAULT TRUE,
    log_responses BOOLEAN DEFAULT TRUE,
    log_errors BOOLEAN DEFAULT TRUE,
    log_performance BOOLEAN DEFAULT FALSE,
    log_retention_days INTEGER DEFAULT 30,
    log_level VARCHAR(20) DEFAULT 'INFO', -- DEBUG, INFO, WARN, ERROR, FATAL
    
    -- Configurações de Alertas
    alert_enabled BOOLEAN DEFAULT FALSE,
    alert_threshold_errors INTEGER DEFAULT 10,
    alert_threshold_response_time INTEGER DEFAULT 5000, -- milissegundos
    alert_channels TEXT, -- EMAIL, SMS, SLACK, WEBHOOK
    alert_recipients TEXT,
    
    -- Configurações de Versionamento
    api_version VARCHAR(20),
    endpoint_base VARCHAR(500),
    version_compatibility_mode BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Negócio
    empresa_id INTEGER,
    filial_id INTEGER,
    departamento_id INTEGER,
    responsavel_id INTEGER,
    
    -- Configurações de Agendamento
    agendamento_enabled BOOLEAN DEFAULT FALSE,
    agendamento_cron VARCHAR(100), -- Expressão cron
    agendamento_timezone VARCHAR(50) DEFAULT 'America/Sao_Paulo',
    agendamento_max_executions INTEGER DEFAULT 1,
    
    -- Configurações de Dependências
    depende_de INTEGER REFERENCES integracoes(id), -- ID da integração dependente
    ordem_execucao INTEGER DEFAULT 0,
    
    -- Configurações de Failover
    failover_enabled BOOLEAN DEFAULT FALSE,
    failover_integracao_id INTEGER REFERENCES integracoes(id),
    failover_threshold_errors INTEGER DEFAULT 5,
    failover_timeout INTEGER DEFAULT 30,
    
    -- Configurações de Performance
    connection_pool_size INTEGER DEFAULT 10,
    connection_pool_max_size INTEGER DEFAULT 50,
    keep_alive_timeout INTEGER DEFAULT 30,
    compression_enabled BOOLEAN DEFAULT FALSE,
    compression_algorithm VARCHAR(20) DEFAULT 'GZIP', -- GZIP, DEFLATE, BROTLI
    
    -- Configurações de Documentação
    documentacao_url VARCHAR(500),
    documentacao_swagger_url VARCHAR(500),
    documentacao_postman_url VARCHAR(500),
    suporte_email VARCHAR(255),
    suporte_telefone VARCHAR(50),
    
    -- Configurações de Teste
    teste_endpoint VARCHAR(500),
    teste_metodo VARCHAR(10) DEFAULT 'GET',
    teste_headers TEXT,
    teste_body TEXT,
    teste_expected_status INTEGER DEFAULT 200,
    teste_expected_response TEXT,
    
    -- Status e Controle
    status_integracao VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, ERRO, MANUTENCAO, BLOQUEADA
    motivo_status TEXT,
    data_ultimo_teste TIMESTAMP,
    resultado_ultimo_teste TEXT,
    data_ultima_sincronizacao TIMESTAMP,
    status_ultima_sincronizacao VARCHAR(20),
    
    -- Métricas
    total_requests INTEGER DEFAULT 0,
    total_success INTEGER DEFAULT 0,
    total_errors INTEGER DEFAULT 0,
    avg_response_time DECIMAL(10,2) DEFAULT 0.00,
    last_request_timestamp TIMESTAMP,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_integracao CHECK (tipo_integracao IN ('API', 'WEBSERVICE', 'DATABASE', 'FILE', 'EMAIL', 'SMS', 'PAYMENT', 'ERP', 'CRM', 'ECOMMERCE')),
    CONSTRAINT chk_categoria_integracao CHECK (categoria_integracao IN ('FINANCEIRO', 'VENDAS', 'ESTOQUE', 'CLIENTE', 'PRODUTO', 'LOGISTICA', 'MARKETING', 'RH')),
    CONSTRAINT chk_subcategoria_integracao CHECK (subcategoria_integracao IN ('BOLETO', 'CARTAO', 'PIX', 'NOTA_FISCAL', 'NFCE', 'NFE', 'SAT', 'MFE')),
    CONSTRAINT chk_tipo_conexao CHECK (tipo_conexao IN ('HTTP', 'HTTPS', 'FTP', 'SFTP', 'SSH', 'JDBC', 'ODBC', 'SMTP', 'POP3', 'IMAP')),
    CONSTRAINT chk_protocolo_comunicacao CHECK (protocolo_comunicacao IN ('REST', 'SOAP', 'GraphQL', 'WebSocket', 'TCP', 'UDP')),
    CONSTRAINT chk_tipo_autenticacao CHECK (tipo_autenticacao IN ('NONE', 'BASIC', 'BEARER', 'OAUTH2', 'API_KEY', 'JWT', 'CERTIFICATE', 'SSH_KEY')),
    CONSTRAINT chk_oauth2_grant_type CHECK (oauth2_grant_type IN ('AUTHORIZATION_CODE', 'CLIENT_CREDENTIALS', 'PASSWORD', 'REFRESH_TOKEN')),
    CONSTRAINT chk_proxy_tipo CHECK (proxy_tipo IN ('HTTP', 'SOCKS4', 'SOCKS5')),
    CONSTRAINT chk_formato_requisicao CHECK (formato_requisicao IN ('JSON', 'XML', 'FORM_DATA', 'TEXT', 'BINARY')),
    CONSTRAINT chk_formato_resposta CHECK (formato_resposta IN ('JSON', 'XML', 'FORM_DATA', 'TEXT', 'BINARY')),
    CONSTRAINT chk_encoding_dados CHECK (encoding_dados IN ('UTF-8', 'ISO-8859-1', 'ASCII', 'UTF-16', 'UTF-32')),
    CONSTRAINT chk_tls_version CHECK (tls_version IN ('TLS_1_0', 'TLS_1_1', 'TLS_1_2', 'TLS_1_3')),
    CONSTRAINT chk_hmac_algoritmo CHECK (hmac_algoritmo IN ('HMAC_SHA256', 'HMAC_SHA512', 'HMAC_MD5')),
    CONSTRAINT chk_criptografia_dados CHECK (criptografia_dados IN ('AES_256', 'AES_128', 'RSA_2048', 'RSA_4096')),
    CONSTRAINT chk_ambiente CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO', 'DESENVOLVIMENTO', 'TESTE')),
    CONSTRAINT chk_webhook_metodo CHECK (webhook_metodo IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH')),
    CONSTRAINT chk_rate_limit_strategy CHECK (rate_limit_strategy IN ('FIXED_WINDOW', 'SLIDING_WINDOW', 'TOKEN_BUCKET')),
    CONSTRAINT chk_cache_strategy CHECK (cache_strategy IN ('MEMORY', 'REDIS', 'MEMCACHED', 'DATABASE')),
    CONSTRAINT chk_cache_eviction_policy CHECK (cache_eviction_policy IN ('LRU', 'LFU', 'FIFO')),
    CONSTRAINT chk_log_level CHECK (log_level IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    CONSTRAINT chk_status_integracao CHECK (status_integracao IN ('ATIVA', 'INATIVA', 'ERRO', 'MANUTENCAO', 'BLOQUEADA')),
    CONSTRAINT chk_status_ultima_sincronizacao CHECK (status_ultima_sincronizacao IN ('SUCESSO', 'ERRO', 'PARCIAL', 'EM_ANDAMENTO')),
    CONSTRAINT chk_webhook_timeout CHECK (webhook_timeout > 0),
    CONSTRAINT chk_health_check_timeout CHECK (health_check_timeout > 0),
    CONSTRAINT chk_limites_integracoes CHECK (
        porta_conexao IS NULL OR (porta_conexao > 0 AND porta_conexao <= 65535) AND
        timeout_conexao > 0 AND
        timeout_leitura > 0 AND
        timeout_escrita > 0 AND
        maximo_retries > 0 AND
        intervalo_retries > 0 AND
        retry_backoff_multiplier > 0 AND
        rate_limit_requests > 0 AND
        rate_limit_period > 0 AND
        cache_ttl > 0 AND
        cache_max_size > 0 AND
        max_concurrent_requests > 0 AND
        batch_size > 0 AND
        batch_timeout > 0 AND
        health_check_interval > 0 AND
        log_retention_days > 0 AND
        alert_threshold_errors > 0 AND
        alert_threshold_response_time > 0 AND
        ordem_execucao >= 0 AND
        failover_threshold_errors > 0 AND
        failover_timeout > 0 AND
        connection_pool_size > 0 AND
        connection_pool_max_size > 0 AND
        keep_alive_timeout > 0 AND
        agendamento_max_executions > 0 AND
        avg_response_time >= 0 AND
        total_requests >= 0 AND
        total_success >= 0 AND
        total_errors >= 0 AND
        ordem_exibicao >= 0
    )
);

-- Criar tabela de Endpoints de Integração
CREATE TABLE IF NOT EXISTS integracoes_endpoints (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_integracao INTEGER NOT NULL REFERENCES integracoes(id) ON DELETE CASCADE,
    
    -- Dados do Endpoint
    nome_endpoint VARCHAR(100) NOT NULL,
    descricao_endpoint TEXT,
    path_endpoint VARCHAR(500) NOT NULL,
    metodo_http VARCHAR(10) NOT NULL, -- GET, POST, PUT, DELETE, PATCH
    
    -- Configurações do Endpoint
    content_type VARCHAR(100) DEFAULT 'application/json',
    accept_type VARCHAR(100) DEFAULT 'application/json',
    headers_padrao TEXT, -- Headers em formato JSON
    parametros_query TEXT, -- Parâmetros de query em formato JSON
    
    -- Configurações de Validação
    valida_request BOOLEAN DEFAULT TRUE,
    valida_response BOOLEAN DEFAULT TRUE,
    schema_request TEXT, -- Schema JSON para validação
    schema_response TEXT, -- Schema JSON para validação
    
    -- Configurações de Transformação
    transformacao_request_enabled BOOLEAN DEFAULT FALSE,
    transformacao_request_template TEXT,
    transformacao_response_enabled BOOLEAN DEFAULT FALSE,
    transformacao_response_template TEXT,
    
    -- Configurações de Cache
    cache_enabled BOOLEAN DEFAULT FALSE,
    cache_ttl INTEGER DEFAULT 300,
    cache_key_pattern VARCHAR(200),
    
    -- Configurações de Rate Limiting
    rate_limit_enabled BOOLEAN DEFAULT FALSE,
    rate_limit_requests INTEGER DEFAULT 100,
    rate_limit_period INTEGER DEFAULT 60,
    
    -- Configurações de Timeout
    timeout_conexao INTEGER DEFAULT 30,
    timeout_leitura INTEGER DEFAULT 60,
    
    -- Configurações de Retentativas
    maximo_retries INTEGER DEFAULT 3,
    intervalo_retries INTEGER DEFAULT 5,
    retry_backoff_multiplier DECIMAL(3,2) DEFAULT 2.0,
    
    -- Configurações de Teste
    teste_enabled BOOLEAN DEFAULT TRUE,
    teste_params TEXT, -- Parâmetros de teste em formato JSON
    teste_expected_status INTEGER DEFAULT 200,
    teste_expected_response TEXT,
    
    -- Status e Controle
    ativo BOOLEAN DEFAULT TRUE,
    ordem_execucao INTEGER DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_metodo_http CHECK (metodo_http IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS')),
    CONSTRAINT chk_limites_endpoints CHECK (
        timeout_conexao > 0 AND
        timeout_leitura > 0 AND
        maximo_retries > 0 AND
        intervalo_retries > 0 AND
        retry_backoff_multiplier > 0 AND
        rate_limit_requests > 0 AND
        rate_limit_period > 0 AND
        cache_ttl > 0 AND
        teste_expected_status > 0 AND
        ordem_execucao >= 0
    ),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_integracao, path_endpoint, metodo_http)
);

-- Criar tabela de Histórico de Execuções de Integração
CREATE TABLE IF NOT EXISTS integracoes_historico (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_integracao INTEGER NOT NULL REFERENCES integracoes(id),
    id_endpoint INTEGER REFERENCES integracoes_endpoints(id),
    
    -- Dados da Execução
    id_execucao VARCHAR(100) UNIQUE NOT NULL,
    tipo_execucao VARCHAR(20) NOT NULL, -- SYNC, ASYNC, SCHEDULED, MANUAL, AUTOMATIC
    data_inicio_execucao TIMESTAMP NOT NULL,
    data_fim_execucao TIMESTAMP,
    duracao_execucao_ms INTEGER,
    
    -- Dados da Requisição
    metodo_http VARCHAR(10),
    url_requisicao VARCHAR(1000),
    headers_requisicao TEXT,
    body_requisicao TEXT,
    parametros_query TEXT,
    
    -- Dados da Resposta
    status_resposta INTEGER,
    mensagem_status VARCHAR(500),
    headers_resposta TEXT,
    body_resposta TEXT,
    tamanho_resposta_bytes INTEGER,
    
    -- Dados de Performance
    tempo_conexao_ms INTEGER,
    tempo_primeiro_byte_ms INTEGER,
    tempo_total_ms INTEGER,
    tamanho_requisicao_bytes INTEGER,
    
    -- Dados de Contexto
    usuario_solicitante VARCHAR(100),
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(255),
    user_agent VARCHAR(500),
    modulo_origem VARCHAR(50),
    funcionalidade_origem VARCHAR(100),
    
    -- Dados de Negócio
    id_transacao_negocio VARCHAR(100),
    tipo_transacao VARCHAR(50),
    dados_negocio_json TEXT, -- Dados de negócio em formato JSON
    
    -- Dados de Erro
    erro_ocorrido BOOLEAN DEFAULT FALSE,
    tipo_erro VARCHAR(100),
    mensagem_erro TEXT,
    stack_trace_erro TEXT,
    codigo_erro_interno VARCHAR(50),
    
    -- Dados de Retentativas
    tentativas_realizadas INTEGER DEFAULT 1,
    motivo_retries TEXT,
    tempo_total_retries_ms INTEGER,
    
    -- Dados de Cache
    cache_hit BOOLEAN DEFAULT FALSE,
    cache_key VARCHAR(500),
    cache_ttl INTEGER,
    
    -- Status e Resultado
    status_execucao VARCHAR(20) NOT NULL, -- SUCESSO, ERRO, TIMEOUT, PARCIAL, CANCELADO
    resultado_execucao TEXT,
    observacoes TEXT,
    
    -- Auditoria
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_execucao CHECK (tipo_execucao IN ('SYNC', 'ASYNC', 'SCHEDULED', 'MANUAL', 'AUTOMATIC')),
    CONSTRAINT chk_metodo_http_historico CHECK (metodo_http IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS')),
    CONSTRAINT chk_status_execucao CHECK (status_execucao IN ('SUCESSO', 'ERRO', 'TIMEOUT', 'PARCIAL', 'CANCELADO')),
    CONSTRAINT chk_limites_historico CHECK (
        duracao_execucao_ms IS NULL OR duracao_execucao_ms >= 0 AND
        status_resposta IS NULL OR (status_resposta >= 100 AND status_resposta < 600) AND
        tamanho_resposta_bytes IS NULL OR tamanho_resposta_bytes >= 0 AND
        tempo_conexao_ms IS NULL OR tempo_conexao_ms >= 0 AND
        tempo_primeiro_byte_ms IS NULL OR tempo_primeiro_byte_ms >= 0 AND
        tempo_total_ms IS NULL OR tempo_total_ms >= 0 AND
        tamanho_requisicao_bytes IS NULL OR tamanho_requisicao_bytes >= 0 AND
        tentativas_realizadas > 0 AND
        tempo_total_retries_ms IS NULL OR tempo_total_retries_ms >= 0 AND
        cache_ttl IS NULL OR cache_ttl > 0
    )
);

-- Criar tabela de Configurações de Mapeamento de Integração
CREATE TABLE IF NOT EXISTS integracoes_mapeamento (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_integracao INTEGER NOT NULL REFERENCES integracoes(id) ON DELETE CASCADE,
    id_endpoint INTEGER REFERENCES integracoes_endpoints(id) ON DELETE CASCADE,
    
    -- Dados do Mapeamento
    nome_mapeamento VARCHAR(100) NOT NULL,
    descricao_mapeamento TEXT,
    tipo_mapeamento VARCHAR(20) NOT NULL, -- REQUEST, RESPONSE, BOTH
    
    -- Configurações de Mapeamento de Campos
    mapeamento_campos_json TEXT NOT NULL, -- Mapeamento em formato JSON
    campo_origem_padrao VARCHAR(100), -- Campo padrão quando não encontrado
    campo_destino_padrao VARCHAR(100), -- Campo padrão quando não encontrado
    
    -- Configurações de Mapeamento de Valores
    mapeamento_valores_json TEXT, -- Mapeamento de valores em formato JSON
    valor_padrao TEXT, -- Valor padrão quando não encontrado
    
    -- Configurações de Transformação
    transformacao_enabled BOOLEAN DEFAULT FALSE,
    transformacao_expression TEXT, -- Expressão de transformação (JavaScript, Groovy, etc.)
    transformacao_funcao TEXT, -- Função de transformação
    
    -- Configurações de Validação
    validacao_enabled BOOLEAN DEFAULT TRUE,
    validacao_regras TEXT, -- Regras de validação em formato JSON
    mensagem_erro_validacao VARCHAR(500),
    
    -- Configurações de Formato
    formato_origem VARCHAR(20) DEFAULT 'JSON', -- JSON, XML, CSV, FIXED_LENGTH
    formato_destino VARCHAR(20) DEFAULT 'JSON',
    encoding_origem VARCHAR(20) DEFAULT 'UTF-8',
    encoding_destino VARCHAR(20) DEFAULT 'UTF-8',
    
    -- Configurações de Data/Hora
    formato_data_origem VARCHAR(50),
    formato_data_destino VARCHAR(50),
    timezone_origem VARCHAR(50),
    timezone_destino VARCHAR(50),
    
    -- Configurações de Números
    formato_decimal_origem VARCHAR(10), -- '.', ',', ' '
    formato_decimal_destino VARCHAR(10),
    formato_milhar_origem VARCHAR(10), -- '.', ',', ' ', ''
    formato_milhar_destino VARCHAR(10),
    
    -- Status e Controle
    ativo BOOLEAN DEFAULT TRUE,
    ordem_execucao INTEGER DEFAULT 0,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_mapeamento CHECK (tipo_mapeamento IN ('REQUEST', 'RESPONSE', 'BOTH')),
    CONSTRAINT chk_formato_origem CHECK (formato_origem IN ('JSON', 'XML', 'CSV', 'FIXED_LENGTH', 'DELIMITED')),
    CONSTRAINT chk_formato_destino CHECK (formato_destino IN ('JSON', 'XML', 'CSV', 'FIXED_LENGTH', 'DELIMITED')),
    CONSTRAINT chk_encoding_origem CHECK (encoding_origem IN ('UTF-8', 'ISO-8859-1', 'ASCII', 'UTF-16', 'UTF-32')),
    CONSTRAINT chk_encoding_destino CHECK (encoding_destino IN ('UTF-8', 'ISO-8859-1', 'ASCII', 'UTF-16', 'UTF-32')),
    CONSTRAINT chk_ordem_execucao_mapeamento CHECK (ordem_execucao >= 0),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_integracao, nome_mapeamento, tipo_mapeamento)
);

-- Criar tabela de Logs de Integração
CREATE TABLE IF NOT EXISTS integracoes_logs (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_integracao INTEGER NOT NULL REFERENCES integracoes(id),
    id_endpoint INTEGER REFERENCES integracoes_endpoints(id),
    id_historico INTEGER REFERENCES integracoes_historico(id),
    
    -- Dados do Log
    nivel_log VARCHAR(20) NOT NULL, -- DEBUG, INFO, WARN, ERROR, FATAL
    mensagem_log TEXT NOT NULL,
    categoria_log VARCHAR(50), -- REQUEST, RESPONSE, ERROR, PERFORMANCE, SECURITY
    
    -- Dados de Contexto
    timestamp_log TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    thread_id VARCHAR(50),
    classe_origem VARCHAR(200),
    metodo_origem VARCHAR(100),
    linha_origem INTEGER,
    
    -- Dados Adicionais
    dados_adicionais_json TEXT, -- Dados adicionais em formato JSON
    exception_class VARCHAR(200),
    exception_message TEXT,
    stack_trace TEXT,
    
    -- Dados de Performance
    memoria_usada_mb DECIMAL(10,2),
    cpu_usada_percentual DECIMAL(5,2),
    tempo_execucao_ms INTEGER,
    
    -- Dados de Requisição (se aplicável)
    id_execucao VARCHAR(100),
    url_requisicao VARCHAR(1000),
    metodo_http VARCHAR(10),
    status_resposta INTEGER,
    
    -- Filtros
    usuario_solicitante VARCHAR(100),
    ip_origem VARCHAR(45),
    modulo_origem VARCHAR(50),
    
    -- Auditoria
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_nivel_log CHECK (nivel_log IN ('DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL')),
    CONSTRAINT chk_categoria_log CHECK (categoria_log IN ('REQUEST', 'RESPONSE', 'ERROR', 'PERFORMANCE', 'SECURITY', 'BUSINESS', 'SYSTEM')),
    CONSTRAINT chk_metodo_http_log CHECK (metodo_http IN ('GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS')),
    CONSTRAINT chk_limites_logs CHECK (
        linha_origem IS NULL OR linha_origem > 0 AND
        memoria_usada_mb IS NULL OR memoria_usada_mb >= 0 AND
        cpu_usada_percentual IS NULL OR (cpu_usada_percentual >= 0 AND cpu_usada_percentual <= 100) AND
        tempo_execucao_ms IS NULL OR tempo_execucao_ms >= 0 AND
        status_resposta IS NULL OR (status_resposta >= 100 AND status_resposta < 600)
    )
);

-- Criar tabela de Configurações de Agendamento de Integração
CREATE TABLE IF NOT EXISTS integracoes_agendamento (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_integracao INTEGER NOT NULL REFERENCES integracoes(id) ON DELETE CASCADE,
    id_endpoint INTEGER REFERENCES integracoes_endpoints(id) ON DELETE CASCADE,
    
    -- Dados do Agendamento
    nome_agendamento VARCHAR(100) NOT NULL,
    descricao_agendamento TEXT,
    
    -- Configurações de Agendamento
    expressao_cron VARCHAR(100) NOT NULL, -- Expressão cron
    timezone VARCHAR(50) DEFAULT 'America/Sao_Paulo',
    data_inicio_agendamento TIMESTAMP,
    data_fim_agendamento TIMESTAMP,
    
    -- Configurações de Execução
    max_execucoes_simultaneas INTEGER DEFAULT 1,
    timeout_execucao INTEGER DEFAULT 300, -- segundos
    retry_on_failure BOOLEAN DEFAULT TRUE,
    max_retries INTEGER DEFAULT 3,
    retry_delay_seconds INTEGER DEFAULT 60,
    
    -- Configurações de Parâmetros
    parametros_execucao TEXT, -- Parâmetros em formato JSON
    headers_personalizados TEXT, -- Headers personalizados em formato JSON
    body_template TEXT, -- Template do body da requisição
    
    -- Configurações de Notificação
    notificar_sucesso BOOLEAN DEFAULT FALSE,
    notificar_erro BOOLEAN DEFAULT TRUE,
    email_destinatarios TEXT, -- Lista de e-mails separados por vírgula
    template_notificacao TEXT,
    
    -- Configurações de Concorrência
    evitar_concorrencia BOOLEAN DEFAULT TRUE,
    lock_timeout_seconds INTEGER DEFAULT 300,
    lock_key_pattern VARCHAR(200),
    
    -- Configurações de Histórico
    manter_historico_execucoes BOOLEAN DEFAULT TRUE,
    dias_retention_historico INTEGER DEFAULT 30,
    
    -- Status e Controle
    ativo BOOLEAN DEFAULT TRUE,
    status_agendamento VARCHAR(20) DEFAULT 'ATIVO', -- ATIVO, INATIVO, PAUSADO, ERRO
    motivo_status TEXT,
    
    -- Controle de Execução
    data_ultima_execucao TIMESTAMP,
    data_proxima_execucao TIMESTAMP,
    total_execucoes INTEGER DEFAULT 0,
    total_sucessos INTEGER DEFAULT 0,
    total_erros INTEGER DEFAULT 0,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_agendamento CHECK (status_agendamento IN ('ATIVO', 'INATIVO', 'PAUSADO', 'ERRO')),
    CONSTRAINT chk_limites_agendamento CHECK (
        max_execucoes_simultaneas > 0 AND
        timeout_execucao > 0 AND
        max_retries >= 0 AND
        retry_delay_seconds > 0 AND
        lock_timeout_seconds > 0 AND
        dias_retention_historico > 0 AND
        total_execucoes >= 0 AND
        total_sucessos >= 0 AND
        total_erros >= 0
    ),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_integracao, nome_agendamento)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_integracoes_nome_integracao ON integracoes(nome_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_tipo_integracao ON integracoes(tipo_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_categoria_integracao ON integracoes(categoria_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_ativa ON integracoes(ativa);
CREATE INDEX IF NOT EXISTS idx_integracoes_padrao ON integracoes(padrao);
CREATE INDEX IF NOT EXISTS idx_integracoes_status_integracao ON integracoes(status_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_ordem_exibicao ON integracoes(ordem_exibicao);
CREATE INDEX IF NOT EXISTS idx_integracoes_ambiente ON integracoes(ambiente);
CREATE INDEX IF NOT EXISTS idx_integracoes_host_servidor ON integracoes(host_servidor);
CREATE INDEX IF NOT EXISTS idx_integracoes_empresa_id ON integracoes(empresa_id);
CREATE INDEX IF NOT EXISTS idx_integracoes_filial_id ON integracoes(filial_id);

CREATE INDEX IF NOT EXISTS idx_integracoes_endpoints_id_integracao ON integracoes_endpoints(id_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_endpoints_metodo_http ON integracoes_endpoints(metodo_http);
CREATE INDEX IF NOT EXISTS idx_integracoes_endpoints_ativo ON integracoes_endpoints(ativo);
CREATE INDEX IF NOT EXISTS idx_integracoes_endpoints_ordem_execucao ON integracoes_endpoints(ordem_execucao);

CREATE INDEX IF NOT EXISTS idx_integracoes_historico_id_integracao ON integracoes_historico(id_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_id_endpoint ON integracoes_historico(id_endpoint);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_id_execucao ON integracoes_historico(id_execucao);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_data_inicio_execucao ON integracoes_historico(data_inicio_execucao);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_status_execucao ON integracoes_historico(status_execucao);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_tipo_execucao ON integracoes_historico(tipo_execucao);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_erro_ocorrido ON integracoes_historico(erro_ocorrido);
CREATE INDEX IF NOT EXISTS idx_integracoes_historico_usuario_solicitante ON integracoes_historico(usuario_solicitante);

CREATE INDEX IF NOT EXISTS idx_integracoes_mapeamento_id_integracao ON integracoes_mapeamento(id_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_mapeamento_id_endpoint ON integracoes_mapeamento(id_endpoint);
CREATE INDEX IF NOT EXISTS idx_integracoes_mapeamento_tipo_mapeamento ON integracoes_mapeamento(tipo_mapeamento);
CREATE INDEX IF NOT EXISTS idx_integracoes_mapeamento_ativo ON integracoes_mapeamento(ativo);
CREATE INDEX IF NOT EXISTS idx_integracoes_mapeamento_ordem_execucao ON integracoes_mapeamento(ordem_execucao);

CREATE INDEX IF NOT EXISTS idx_integracoes_logs_id_integracao ON integracoes_logs(id_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_id_endpoint ON integracoes_logs(id_endpoint);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_id_historico ON integracoes_logs(id_historico);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_timestamp_log ON integracoes_logs(timestamp_log);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_nivel_log ON integracoes_logs(nivel_log);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_categoria_log ON integracoes_logs(categoria_log);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_id_execucao ON integracoes_logs(id_execucao);
CREATE INDEX IF NOT EXISTS idx_integracoes_logs_usuario_solicitante ON integracoes_logs(usuario_solicitante);

CREATE INDEX IF NOT EXISTS idx_integracoes_agendamento_id_integracao ON integracoes_agendamento(id_integracao);
CREATE INDEX IF NOT EXISTS idx_integracoes_agendamento_id_endpoint ON integracoes_agendamento(id_endpoint);
CREATE INDEX IF NOT EXISTS idx_integracoes_agendamento_ativo ON integracoes_agendamento(ativo);
CREATE INDEX IF NOT EXISTS idx_integracoes_agendamento_status_agendamento ON integracoes_agendamento(status_agendamento);
CREATE INDEX IF NOT EXISTS idx_integracoes_agendamento_data_proxima_execucao ON integracoes_agendamento(data_proxima_execucao);

-- Inserir integrações padrão
INSERT INTO integracoes (
    nome_integracao, descricao_integracao, codigo_integracao, sigla_integracao, 
    tipo_integracao, categoria_integracao, subcategoria_integracao,
    ativa, padrao, ordem_exibicao, icone_integracao, cor_integracao,
    tipo_conexao, host_servidor, porta_conexao, protocolo_comunicacao,
    tipo_autenticacao, ambiente, status_integracao
) VALUES 
(
    'API Correios', 'Integração com API dos Correios para consulta de CEP e rastreamento', 'CORREIOS', 'COR',
    'API', 'LOGISTICA', NULL,
    TRUE, FALSE, 1, 'truck', '#FF6B35',
    'HTTPS', 'apps.correios.com.br', 443, 'REST',
    'NONE', 'PRODUCAO', 'ATIVA'
),
(
    'Mercado Pago', 'Integração com gateway de pagamento Mercado Pago', 'MERCADO_PAGO', 'MP',
    'PAYMENT', 'FINANCEIRO', 'CARTAO',
    TRUE, TRUE, 2, 'credit-card', '#009EE3',
    'HTTPS', 'api.mercadopago.com', 443, 'REST',
    'BEARER', 'PRODUCAO', 'ATIVA'
),
(
    'PagSeguro', 'Integração com gateway de pagamento PagSeguro', 'PAGSEGURO', 'PS',
    'PAYMENT', 'FINANCEIRO', 'BOLETO',
    TRUE, FALSE, 3, 'money-check', '#FFCC00',
    'HTTPS', 'ws.pagseguro.uol.com.br', 443, 'SOAP',
    'API_KEY', 'PRODUCAO', 'ATIVA'
),
(
    'NFe Brasil', 'Integração com serviço de emissão de NFe', 'NFE_BRASIL', 'NFE',
    'API', 'FINANCEIRO', 'NFE',
    TRUE, FALSE, 4, 'file-invoice', '#1E88E5',
    'HTTPS', 'nfe.fazenda.sp.gov.br', 443, 'REST',
    'CERTIFICATE', 'PRODUCAO', 'ATIVA'
),
(
    'NFCe Brasil', 'Integração com serviço de emissão de NFCe', 'NFCE_BRASIL', 'NFCE',
    'API', 'FINANCEIRO', 'NFCE',
    TRUE, FALSE, 5, 'receipt', '#43A047',
    'HTTPS', 'nfce.fazenda.sp.gov.br', 443, 'REST',
    'CERTIFICATE', 'PRODUCAO', 'ATIVA'
),
(
    'SAT São Paulo', 'Integração com SAT Fiscal de São Paulo', 'SAT_SP', 'SAT',
    'API', 'FINANCEIRO', 'SAT',
    TRUE, FALSE, 6, 'printer', '#E53935',
    'HTTPS', 'sat.fazenda.sp.gov.br', 443, 'REST',
    'CERTIFICATE', 'PRODUCAO', 'ATIVA'
),
(
    'MFe São Paulo', 'Integração com MFe Fiscal de São Paulo', 'MFE_SP', 'MFE',
    'API', 'FINANCEIRO', 'MFE',
    TRUE, FALSE, 7, 'cash-register', '#8E24AA',
    'HTTPS', 'mfe.fazenda.sp.gov.br', 443, 'REST',
    'CERTIFICATE', 'PRODUCAO', 'ATIVA'
),
(
    'WhatsApp Business', 'Integração com WhatsApp Business API', 'WHATSAPP', 'WPP',
    'API', 'MARKETING', NULL,
    TRUE, FALSE, 8, 'whatsapp', '#25D366',
    'HTTPS', 'graph.facebook.com', 443, 'REST',
    'BEARER', 'PRODUCAO', 'ATIVA'
),
(
    'SendGrid', 'Integração com serviço de e-mail SendGrid', 'SENDGRID', 'SG',
    'EMAIL', 'MARKETING', NULL,
    TRUE, FALSE, 9, 'envelope', '#4A90E2',
    'HTTPS', 'api.sendgrid.com', 443, 'REST',
    'API_KEY', 'PRODUCAO', 'ATIVA'
),
(
    'Twilio SMS', 'Integração com serviço de SMS Twilio', 'TWILIO', 'TWSMS',
    'SMS', 'MARKETING', NULL,
    TRUE, FALSE, 10, 'sms', '#F22F46',
    'HTTPS', 'api.twilio.com', 443, 'REST',
    'API_KEY', 'PRODUCAO', 'ATIVA'
) ON CONFLICT DO NOTHING;

-- Inserir endpoints padrão para integrações
INSERT INTO integracoes_endpoints (
    id_integracao, nome_endpoint, descricao_endpoint, path_endpoint, metodo_http,
    content_type, accept_type, headers_padrao, ativo, ordem_execucao
)
SELECT 
    i.id, 'Consulta CEP', 'Consulta de CEP dos Correios', '/cep/v2/{cep}', 'GET',
    'application/json', 'application/json', '{"Accept": "application/json"}', TRUE, 1
FROM integracoes i 
WHERE i.codigo_integracao = 'CORREIOS'
ON CONFLICT DO NOTHING;

INSERT INTO integracoes_endpoints (
    id_integracao, nome_endpoint, descricao_endpoint, path_endpoint, metodo_http,
    content_type, accept_type, headers_padrao, ativo, ordem_execucao
)
SELECT 
    i.id, 'Rastreamento', 'Rastreamento de objetos dos Correios', '/sro-rastro/v1/trackings', 'POST',
    'application/json', 'application/json', '{"Accept": "application/json"}', TRUE, 2
FROM integracoes i 
WHERE i.codigo_integracao = 'CORREIOS'
ON CONFLICT DO NOTHING;

INSERT INTO integracoes_endpoints (
    id_integracao, nome_endpoint, descricao_endpoint, path_endpoint, metodo_http,
    content_type, accept_type, headers_padrao, ativo, ordem_execucao
)
SELECT 
    i.id, 'Criar Pagamento', 'Criação de pagamento no Mercado Pago', '/v1/payments', 'POST',
    'application/json', 'application/json', '{"Accept": "application/json"}', TRUE, 1
FROM integracoes i 
WHERE i.codigo_integracao = 'MERCADO_PAGO'
ON CONFLICT DO NOTHING;

INSERT INTO integracoes_endpoints (
    id_integracao, nome_endpoint, descricao_endpoint, path_endpoint, metodo_http,
    content_type, accept_type, headers_padrao, ativo, ordem_execucao
)
SELECT 
    i.id, 'Consultar Pagamento', 'Consulta de pagamento no Mercado Pago', '/v1/payments/{id}', 'GET',
    'application/json', 'application/json', '{"Accept": "application/json"}', TRUE, 2
FROM integracoes i 
WHERE i.codigo_integracao = 'MERCADO_PAGO'
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE integracoes IS 'Tabela de configurações de integrações do sistema';
COMMENT ON COLUMN integracoes.nome_integracao IS 'Nome único da integração';
COMMENT ON COLUMN integracoes.descricao_integracao IS 'Descrição detalhada da integração';
COMMENT ON COLUMN integracoes.codigo_integracao IS 'Código único da integração';
COMMENT ON COLUMN integracoes.sigla_integracao IS 'Sigla abreviada da integração';
COMMENT ON COLUMN integracoes.tipo_integracao IS 'Tipo principal da integração';
COMMENT ON COLUMN integracoes.categoria_integracao IS 'Categoria da integração';
COMMENT ON COLUMN integracoes.subcategoria_integracao IS 'Subcategoria específica da integração';
COMMENT ON COLUMN integracoes.ativa IS 'Indica se a integração está ativa';
COMMENT ON COLUMN integracoes.padrao IS 'Indica se é a integração padrão';
COMMENT ON COLUMN integracoes.ordem_exibicao IS 'Ordem de exibição na interface';
COMMENT ON COLUMN integracoes.icone_integracao IS 'Ícone representativo da integração';
COMMENT ON COLUMN integracoes.cor_integracao IS 'Cor hexadecimal da integração';
COMMENT ON COLUMN integracoes.tipo_conexao IS 'Tipo de conexão com o serviço';
COMMENT ON COLUMN integracoes.host_servidor IS 'Endereço do servidor da integração';
COMMENT ON COLUMN integracoes.porta_conexao IS 'Porta de conexão do servidor';
COMMENT ON COLUMN integracoes.contexto_aplicacao IS 'Contexto da aplicação no servidor';
COMMENT ON COLUMN integracoes.protocolo_comunicacao IS 'Protocolo de comunicação';
COMMENT ON COLUMN integracoes.tipo_autenticacao IS 'Tipo de autenticação exigido';
COMMENT ON COLUMN integracoes.usuario_acesso IS 'Usuário para acesso à integração';
COMMENT ON COLUMN integracoes.senha_acesso IS 'Senha para acesso à integração';
COMMENT ON COLUMN integracoes.token_acesso IS 'Token de acesso à integração';
COMMENT ON COLUMN integracoes.api_key IS 'Chave de API para acesso';
COMMENT ON COLUMN integracoes.api_secret IS 'Segredo da chave de API';
COMMENT ON COLUMN integracoes.oauth2_client_id IS 'Client ID para OAuth2';
COMMENT ON COLUMN integracoes.oauth2_client_secret IS 'Client Secret para OAuth2';
COMMENT ON COLUMN integracoes.oauth2_authorization_url IS 'URL de autorização OAuth2';
COMMENT ON COLUMN integracoes.oauth2_token_url IS 'URL de token OAuth2';
COMMENT ON COLUMN integracoes.oauth2_scope IS 'Scope do OAuth2';
COMMENT ON COLUMN integracoes.oauth2_grant_type IS 'Grant type do OAuth2';
COMMENT ON COLUMN integracoes.certificado_path IS 'Caminho do certificado digital';
COMMENT ON COLUMN integracoes.certificado_password IS 'Senha do certificado digital';
COMMENT ON COLUMN integracoes.certificado_alias IS 'Alias do certificado no keystore';
COMMENT ON COLUMN integracoes.truststore_path IS 'Caminho do truststore';
COMMENT ON COLUMN integracoes.truststore_password IS 'Senha do truststore';
COMMENT ON COLUMN integracoes.usa_proxy IS 'Indica se usa proxy';
COMMENT ON COLUMN integracoes.proxy_host IS 'Endereço do proxy';
COMMENT ON COLUMN integracoes.proxy_porta IS 'Porta do proxy';
COMMENT ON COLUMN integracoes.proxy_usuario IS 'Usuário do proxy';
COMMENT ON COLUMN integracoes.proxy_senha IS 'Senha do proxy';
COMMENT ON COLUMN integracoes.proxy_tipo IS 'Tipo de proxy';
COMMENT ON COLUMN integracoes.timeout_conexao IS 'Timeout de conexão em segundos';
COMMENT ON COLUMN integracoes.timeout_leitura IS 'Timeout de leitura em segundos';
COMMENT ON COLUMN integracoes.timeout_escrita IS 'Timeout de escrita em segundos';
COMMENT ON COLUMN integracoes.maximo_retries IS 'Número máximo de retentativas';
COMMENT ON COLUMN integracoes.intervalo_retries IS 'Intervalo entre retentativas em segundos';
COMMENT ON COLUMN integracoes.retry_backoff_multiplier IS 'Multiplicador de backoff para retentativas';
COMMENT ON COLUMN integracoes.formato_requisicao IS 'Formato dos dados da requisição';
COMMENT ON COLUMN integracoes.formato_resposta IS 'Formato dos dados da resposta';
COMMENT ON COLUMN integracoes.encoding_dados IS 'Encoding dos dados';
COMMENT ON COLUMN integracoes.content_type IS 'Content-Type das requisições';
COMMENT ON COLUMN integracoes.accept_type IS 'Accept-Type das requisições';
COMMENT ON COLUMN integracoes.ssl_verificado IS 'Indica se verifica certificado SSL';
COMMENT ON COLUMN integracoes.tls_version IS 'Versão do TLS';
COMMENT ON COLUMN integracoes.cipher_suite IS 'Suite de criptografia';
COMMENT ON COLUMN integracoes.hmac_algoritmo IS 'Algoritmo HMAC';
COMMENT ON COLUMN integracoes.criptografia_dados IS 'Algoritmo de criptografia de dados';
COMMENT ON COLUMN integracoes.ambiente IS 'Ambiente da integração';
COMMENT ON COLUMN integracoes.debug_mode IS 'Modo debug ativado';
COMMENT ON COLUMN integracoes.log_detalhado IS 'Log detalhado ativado';
COMMENT ON COLUMN integracoes.log_persistente IS 'Log persistente ativado';
COMMENT ON COLUMN integracoes.webhook_enabled IS 'Webhook ativado';
COMMENT ON COLUMN integracoes.webhook_url IS 'URL do webhook';
COMMENT ON COLUMN integracoes.webhook_secret IS 'Segredo do webhook';
COMMENT ON COLUMN integracoes.webhook_eventos IS 'Eventos do webhook';
COMMENT ON COLUMN integracoes.webhook_metodo IS 'Método HTTP do webhook';
COMMENT ON COLUMN integracoes.webhook_headers IS 'Headers personalizados do webhook';
COMMENT ON COLUMN integracoes.webhook_timeout IS 'Timeout do webhook';
COMMENT ON COLUMN integracoes.rate_limit_enabled IS 'Rate limiting ativado';
COMMENT ON COLUMN integracoes.rate_limit_requests IS 'Limite de requisições';
COMMENT ON COLUMN integracoes.rate_limit_period IS 'Período do rate limiting';
COMMENT ON COLUMN integracoes.rate_limit_strategy IS 'Estratégia de rate limiting';
COMMENT ON COLUMN integracoes.cache_enabled IS 'Cache ativado';
COMMENT ON COLUMN integracoes.cache_ttl IS 'TTL do cache em segundos';
COMMENT ON COLUMN integracoes.cache_strategy IS 'Estratégia de cache';
COMMENT ON COLUMN integracoes.cache_max_size IS 'Tamanho máximo do cache';
COMMENT ON COLUMN integracoes.cache_eviction_policy IS 'Política de evicção do cache';
COMMENT ON COLUMN integracoes.processamento_assincrono IS 'Processamento assíncrono ativado';
COMMENT ON COLUMN integracoes.queue_name IS 'Nome da fila de processamento';
COMMENT ON COLUMN integracoes.max_concurrent_requests IS 'Máximo de requisições concorrentes';
COMMENT ON COLUMN integracoes.batch_size IS 'Tamanho do lote de processamento';
COMMENT ON COLUMN integracoes.batch_timeout IS 'Timeout do lote';
COMMENT ON COLUMN integracoes.valida_schema IS 'Validação de schema ativada';
COMMENT ON COLUMN integracoes.schema_path IS 'Caminho do schema de validação';
COMMENT ON COLUMN integracoes.valida_certificado IS 'Validação de certificado ativada';
COMMENT ON COLUMN integracoes.valida_assinatura IS 'Validação de assinatura ativada';
COMMENT ON COLUMN integracoes.transformacao_request_enabled IS 'Transformação de request ativada';
COMMENT ON COLUMN integracoes.transformacao_request_template IS 'Template de transformação de request';
COMMENT ON COLUMN integracoes.transformacao_response_enabled IS 'Transformação de response ativada';
COMMENT ON COLUMN integracoes.transformacao_response_template IS 'Template de transformação de response';
COMMENT ON COLUMN integracoes.mapeamento_campos_enabled IS 'Mapeamento de campos ativado';
COMMENT ON COLUMN integracoes.mapeamento_campos_json IS 'Mapeamento de campos em JSON';
COMMENT ON COLUMN integracoes.mapeamento_valores_json IS 'Mapeamento de valores em JSON';
COMMENT ON COLUMN integracoes.monitoramento_enabled IS 'Monitoramento ativado';
COMMENT ON COLUMN integracoes.health_check_enabled IS 'Health check ativado';
COMMENT ON COLUMN integracoes.health_check_interval IS 'Intervalo do health check';
COMMENT ON COLUMN integracoes.health_check_endpoint IS 'Endpoint do health check';
COMMENT ON COLUMN integracoes.health_check_timeout IS 'Timeout do health check';
COMMENT ON COLUMN integracoes.log_requests IS 'Log de requisições ativado';
COMMENT ON COLUMN integracoes.log_responses IS 'Log de respostas ativado';
COMMENT ON COLUMN integracoes.log_errors IS 'Log de erros ativado';
COMMENT ON COLUMN integracoes.log_performance IS 'Log de performance ativado';
COMMENT ON COLUMN integracoes.log_retention_days IS 'Dias de retenção dos logs';
COMMENT ON COLUMN integracoes.log_level IS 'Nível dos logs';
COMMENT ON COLUMN integracoes.alert_enabled IS 'Alertas ativados';
COMMENT ON COLUMN integracoes.alert_threshold_errors IS 'Limite de erros para alerta';
COMMENT ON COLUMN integracoes.alert_threshold_response_time IS 'Limite de tempo de resposta para alerta';
COMMENT ON COLUMN integracoes.alert_channels IS 'Canais de alerta';
COMMENT ON COLUMN integracoes.alert_recipients IS 'Destinatários dos alertas';
COMMENT ON COLUMN integracoes.api_version IS 'Versão da API';
COMMENT ON COLUMN integracoes.endpoint_base IS 'Endpoint base da API';
COMMENT ON COLUMN integracoes.version_compatibility_mode IS 'Modo de compatibilidade de versão';
COMMENT ON COLUMN integracoes.empresa_id IS 'ID da empresa';
COMMENT ON COLUMN integracoes.filial_id IS 'ID da filial';
COMMENT ON COLUMN integracoes.departamento_id IS 'ID do departamento';
COMMENT ON COLUMN integracoes.responsavel_id IS 'ID do responsável';
COMMENT ON COLUMN integracoes.agendamento_enabled IS 'Agendamento ativado';
COMMENT ON COLUMN integracoes.agendamento_cron IS 'Expressão cron do agendamento';
COMMENT ON COLUMN integracoes.agendamento_timezone IS 'Timezone do agendamento';
COMMENT ON COLUMN integracoes.agendamento_max_executions IS 'Máximo de execuções agendadas';
COMMENT ON COLUMN integracoes.depende_de IS 'ID da integração dependente';
COMMENT ON COLUMN integracoes.ordem_execucao IS 'Ordem de execução';
COMMENT ON COLUMN integracoes.failover_enabled IS 'Failover ativado';
COMMENT ON COLUMN integracoes.failover_integracao_id IS 'ID da integração de failover';
COMMENT ON COLUMN integracoes.failover_threshold_errors IS 'Limite de erros para failover';
COMMENT ON COLUMN integracoes.failover_timeout IS 'Timeout do failover';
COMMENT ON COLUMN integracoes.connection_pool_size IS 'Tamanho do pool de conexões';
COMMENT ON COLUMN integracoes.connection_pool_max_size IS 'Tamanho máximo do pool de conexões';
COMMENT ON COLUMN integracoes.keep_alive_timeout IS 'Timeout de keep alive';
COMMENT ON COLUMN integracoes.compression_enabled IS 'Compressão ativada';
COMMENT ON COLUMN integracoes.compression_algorithm IS 'Algoritmo de compressão';
COMMENT ON COLUMN integracoes.documentacao_url IS 'URL da documentação';
COMMENT ON COLUMN integracoes.documentacao_swagger_url IS 'URL da documentação Swagger';
COMMENT ON COLUMN integracoes.documentacao_postman_url IS 'URL da documentação Postman';
COMMENT ON COLUMN integracoes.suporte_email IS 'E-mail de suporte';
COMMENT ON COLUMN integracoes.suporte_telefone IS 'Telefone de suporte';
COMMENT ON COLUMN integracoes.teste_endpoint IS 'Endpoint para teste';
COMMENT ON COLUMN integracoes.teste_metodo IS 'Método HTTP para teste';
COMMENT ON COLUMN integracoes.teste_headers IS 'Headers para teste';
COMMENT ON COLUMN integracoes.teste_body IS 'Body para teste';
COMMENT ON COLUMN integracoes.teste_expected_status IS 'Status esperado no teste';
COMMENT ON COLUMN integracoes.teste_expected_response IS 'Resposta esperada no teste';
COMMENT ON COLUMN integracoes.status_integracao IS 'Status atual da integração';
COMMENT ON COLUMN integracoes.motivo_status IS 'Motivo do status atual';
COMMENT ON COLUMN integracoes.data_ultimo_teste IS 'Data do último teste';
COMMENT ON COLUMN integracoes.resultado_ultimo_teste IS 'Resultado do último teste';
COMMENT ON COLUMN integracoes.data_ultima_sincronizacao IS 'Data da última sincronização';
COMMENT ON COLUMN integracoes.status_ultima_sincronizacao IS 'Status da última sincronização';
COMMENT ON COLUMN integracoes.total_requests IS 'Total de requisições';
COMMENT ON COLUMN integracoes.total_success IS 'Total de sucessos';
COMMENT ON COLUMN integracoes.total_errors IS 'Total de erros';
COMMENT ON COLUMN integracoes.avg_response_time IS 'Tempo médio de resposta';
COMMENT ON COLUMN integracoes.last_request_timestamp IS 'Timestamp da última requisição';
COMMENT ON COLUMN integracoes.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN integracoes.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN integracoes.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN integracoes.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE integracoes_endpoints IS 'Endpoints das integrações';
COMMENT ON COLUMN integracoes_endpoints.id_integracao IS 'ID da integração';
COMMENT ON COLUMN integracoes_endpoints.nome_endpoint IS 'Nome do endpoint';
COMMENT ON COLUMN integracoes_endpoints.descricao_endpoint IS 'Descrição do endpoint';
COMMENT ON COLUMN integracoes_endpoints.path_endpoint IS 'Path do endpoint';
COMMENT ON COLUMN integracoes_endpoints.metodo_http IS 'Método HTTP do endpoint';
COMMENT ON COLUMN integracoes_endpoints.content_type IS 'Content-Type do endpoint';
COMMENT ON COLUMN integracoes_endpoints.accept_type IS 'Accept-Type do endpoint';
COMMENT ON COLUMN integracoes_endpoints.headers_padrao IS 'Headers padrão do endpoint';
COMMENT ON COLUMN integracoes_endpoints.parametros_query IS 'Parâmetros de query do endpoint';
COMMENT ON COLUMN integracoes_endpoints.valida_request IS 'Validação de request ativada';
COMMENT ON COLUMN integracoes_endpoints.valida_response IS 'Validação de response ativada';
COMMENT ON COLUMN integracoes_endpoints.schema_request IS 'Schema de validação do request';
COMMENT ON COLUMN integracoes_endpoints.schema_response IS 'Schema de validação do response';
COMMENT ON COLUMN integracoes_endpoints.transformacao_request_enabled IS 'Transformação de request ativada';
COMMENT ON COLUMN integracoes_endpoints.transformacao_request_template IS 'Template de transformação de request';
COMMENT ON COLUMN integracoes_endpoints.transformacao_response_enabled IS 'Transformação de response ativada';
COMMENT ON COLUMN integracoes_endpoints.transformacao_response_template IS 'Template de transformação de response';
COMMENT ON COLUMN integracoes_endpoints.cache_enabled IS 'Cache ativado';
COMMENT ON COLUMN integracoes_endpoints.cache_ttl IS 'TTL do cache';
COMMENT ON COLUMN integracoes_endpoints.cache_key_pattern IS 'Pattern da chave do cache';
COMMENT ON COLUMN integracoes_endpoints.rate_limit_enabled IS 'Rate limiting ativado';
COMMENT ON COLUMN integracoes_endpoints.rate_limit_requests IS 'Limite de requisições';
COMMENT ON COLUMN integracoes_endpoints.rate_limit_period IS 'Período do rate limiting';
COMMENT ON COLUMN integracoes_endpoints.timeout_conexao IS 'Timeout de conexão';
COMMENT ON COLUMN integracoes_endpoints.timeout_leitura IS 'Timeout de leitura';
COMMENT ON COLUMN integracoes_endpoints.maximo_retries IS 'Máximo de retentativas';
COMMENT ON COLUMN integracoes_endpoints.intervalo_retries IS 'Intervalo entre retentativas';
COMMENT ON COLUMN integracoes_endpoints.retry_backoff_multiplier IS 'Multiplicador de backoff';
COMMENT ON COLUMN integracoes_endpoints.teste_enabled IS 'Teste ativado';
COMMENT ON COLUMN integracoes_endpoints.teste_params IS 'Parâmetros de teste';
COMMENT ON COLUMN integracoes_endpoints.teste_expected_status IS 'Status esperado no teste';
COMMENT ON COLUMN integracoes_endpoints.teste_expected_response IS 'Resposta esperada no teste';
COMMENT ON COLUMN integracoes_endpoints.ativo IS 'Endpoint ativo';
COMMENT ON COLUMN integracoes_endpoints.ordem_execucao IS 'Ordem de execução';
COMMENT ON COLUMN integracoes_endpoints.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN integracoes_endpoints.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN integracoes_endpoints.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN integracoes_endpoints.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE integracoes_historico IS 'Histórico de execuções das integrações';
COMMENT ON COLUMN integracoes_historico.id_integracao IS 'ID da integração';
COMMENT ON COLUMN integracoes_historico.id_endpoint IS 'ID do endpoint';
COMMENT ON COLUMN integracoes_historico.id_execucao IS 'ID único da execução';
COMMENT ON COLUMN integracoes_historico.tipo_execucao IS 'Tipo da execução';
COMMENT ON COLUMN integracoes_historico.data_inicio_execucao IS 'Data de início da execução';
COMMENT ON COLUMN integracoes_historico.data_fim_execucao IS 'Data de fim da execução';
COMMENT ON COLUMN integracoes_historico.duracao_execucao_ms IS 'Duração da execução em ms';
COMMENT ON COLUMN integracoes_historico.metodo_http IS 'Método HTTP';
COMMENT ON COLUMN integracoes_historico.url_requisicao IS 'URL da requisição';
COMMENT ON COLUMN integracoes_historico.headers_requisicao IS 'Headers da requisição';
COMMENT ON COLUMN integracoes_historico.body_requisicao IS 'Body da requisição';
COMMENT ON COLUMN integracoes_historico.parametros_query IS 'Parâmetros de query';
COMMENT ON COLUMN integracoes_historico.status_resposta IS 'Status da resposta';
COMMENT ON COLUMN integracoes_historico.mensagem_status IS 'Mensagem do status';
COMMENT ON COLUMN integracoes_historico.headers_resposta IS 'Headers da resposta';
COMMENT ON COLUMN integracoes_historico.body_resposta IS 'Body da resposta';
COMMENT ON COLUMN integracoes_historico.tamanho_resposta_bytes IS 'Tamanho da resposta em bytes';
COMMENT ON COLUMN integracoes_historico.tempo_conexao_ms IS 'Tempo de conexão em ms';
COMMENT ON COLUMN integracoes_historico.tempo_primeiro_byte_ms IS 'Tempo até primeiro byte em ms';
COMMENT ON COLUMN integracoes_historico.tempo_total_ms IS 'Tempo total em ms';
COMMENT ON COLUMN integracoes_historico.tamanho_requisicao_bytes IS 'Tamanho da requisição em bytes';
COMMENT ON COLUMN integracoes_historico.usuario_solicitante IS 'Usuário solicitante';
COMMENT ON COLUMN integracoes_historico.ip_origem IS 'IP de origem';
COMMENT ON COLUMN integracoes_historico.hostname_origem IS 'Hostname de origem';
COMMENT ON COLUMN integracoes_historico.user_agent IS 'User agent';
COMMENT ON COLUMN integracoes_historico.modulo_origem IS 'Módulo de origem';
COMMENT ON COLUMN integracoes_historico.funcionalidade_origem IS 'Funcionalidade de origem';
COMMENT ON COLUMN integracoes_historico.id_transacao_negocio IS 'ID da transação de negócio';
COMMENT ON COLUMN integracoes_historico.tipo_transacao IS 'Tipo da transação';
COMMENT ON COLUMN integracoes_historico.dados_negocio_json IS 'Dados de negócio em JSON';
COMMENT ON COLUMN integracoes_historico.erro_ocorrido IS 'Erro ocorreu';
COMMENT ON COLUMN integracoes_historico.tipo_erro IS 'Tipo do erro';
COMMENT ON COLUMN integracoes_historico.mensagem_erro IS 'Mensagem do erro';
COMMENT ON COLUMN integracoes_historico.stack_trace_erro IS 'Stack trace do erro';
COMMENT ON COLUMN integracoes_historico.codigo_erro_interno IS 'Código do erro interno';
COMMENT ON COLUMN integracoes_historico.tentativas_realizadas IS 'Tentativas realizadas';
COMMENT ON COLUMN integracoes_historico.motivo_retries IS 'Motivo das retentativas';
COMMENT ON COLUMN integracoes_historico.tempo_total_retries_ms IS 'Tempo total das retentativas';
COMMENT ON COLUMN integracoes_historico.cache_hit IS 'Cache hit';
COMMENT ON COLUMN integracoes_historico.cache_key IS 'Chave do cache';
COMMENT ON COLUMN integracoes_historico.cache_ttl IS 'TTL do cache';
COMMENT ON COLUMN integracoes_historico.status_execucao IS 'Status da execução';
COMMENT ON COLUMN integracoes_historico.resultado_execucao IS 'Resultado da execução';
COMMENT ON COLUMN integracoes_historico.observacoes IS 'Observações';
COMMENT ON COLUMN integracoes_historico.data_registro IS 'Data de registro';
COMMENT ON COLUMN integracoes_historico.usuario_registro IS 'Usuário que registrou';

COMMENT ON TABLE integracoes_mapeamento IS 'Configurações de mapeamento de integração';
COMMENT ON COLUMN integracoes_mapeamento.id_integracao IS 'ID da integração';
COMMENT ON COLUMN integracoes_mapeamento.id_endpoint IS 'ID do endpoint';
COMMENT ON COLUMN integracoes_mapeamento.nome_mapeamento IS 'Nome do mapeamento';
COMMENT ON COLUMN integracoes_mapeamento.descricao_mapeamento IS 'Descrição do mapeamento';
COMMENT ON COLUMN integracoes_mapeamento.tipo_mapeamento IS 'Tipo do mapeamento';
COMMENT ON COLUMN integracoes_mapeamento.mapeamento_campos_json IS 'Mapeamento de campos em JSON';
COMMENT ON COLUMN integracoes_mapeamento.campo_origem_padrao IS 'Campo de origem padrão';
COMMENT ON COLUMN integracoes_mapeamento.campo_destino_padrao IS 'Campo de destino padrão';
COMMENT ON COLUMN integracoes_mapeamento.mapeamento_valores_json IS 'Mapeamento de valores em JSON';
COMMENT ON COLUMN integracoes_mapeamento.valor_padrao IS 'Valor padrão';
COMMENT ON COLUMN integracoes_mapeamento.transformacao_enabled IS 'Transformação ativada';
COMMENT ON COLUMN integracoes_mapeamento.transformacao_expression IS 'Expressão de transformação';
COMMENT ON COLUMN integracoes_mapeamento.transformacao_funcao IS 'Função de transformação';
COMMENT ON COLUMN integracoes_mapeamento.validacao_enabled IS 'Validação ativada';
COMMENT ON COLUMN integracoes_mapeamento.validacao_regras IS 'Regras de validação';
COMMENT ON COLUMN integracoes_mapeamento.mensagem_erro_validacao IS 'Mensagem de erro de validação';
COMMENT ON COLUMN integracoes_mapeamento.formato_origem IS 'Formato de origem';
COMMENT ON COLUMN integracoes_mapeamento.formato_destino IS 'Formato de destino';
COMMENT ON COLUMN integracoes_mapeamento.encoding_origem IS 'Encoding de origem';
COMMENT ON COLUMN integracoes_mapeamento.encoding_destino IS 'Encoding de destino';
COMMENT ON COLUMN integracoes_mapeamento.formato_data_origem IS 'Formato de data de origem';
COMMENT ON COLUMN integracoes_mapeamento.formato_data_destino IS 'Formato de data de destino';
COMMENT ON COLUMN integracoes_mapeamento.timezone_origem IS 'Timezone de origem';
COMMENT ON COLUMN integracoes_mapeamento.timezone_destino IS 'Timezone de destino';
COMMENT ON COLUMN integracoes_mapeamento.formato_decimal_origem IS 'Formato decimal de origem';
COMMENT ON COLUMN integracoes_mapeamento.formato_decimal_destino IS 'Formato decimal de destino';
COMMENT ON COLUMN integracoes_mapeamento.formato_milhar_origem IS 'Formato de milhar de origem';
COMMENT ON COLUMN integracoes_mapeamento.formato_milhar_destino IS 'Formato de milhar de destino';
COMMENT ON COLUMN integracoes_mapeamento.ativo IS 'Mapeamento ativo';
COMMENT ON COLUMN integracoes_mapeamento.ordem_execucao IS 'Ordem de execução';
COMMENT ON COLUMN integracoes_mapeamento.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN integracoes_mapeamento.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN integracoes_mapeamento.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN integracoes_mapeamento.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE integracoes_logs IS 'Logs de integração';
COMMENT ON COLUMN integracoes_logs.id_integracao IS 'ID da integração';
COMMENT ON COLUMN integracoes_logs.id_endpoint IS 'ID do endpoint';
COMMENT ON COLUMN integracoes_logs.id_historico IS 'ID do histórico';
COMMENT ON COLUMN integracoes_logs.nivel_log IS 'Nível do log';
COMMENT ON COLUMN integracoes_logs.mensagem_log IS 'Mensagem do log';
COMMENT ON COLUMN integracoes_logs.categoria_log IS 'Categoria do log';
COMMENT ON COLUMN integracoes_logs.timestamp_log IS 'Timestamp do log';
COMMENT ON COLUMN integracoes_logs.thread_id IS 'ID da thread';
COMMENT ON COLUMN integracoes_logs.classe_origem IS 'Classe de origem';
COMMENT ON COLUMN integracoes_logs.metodo_origem IS 'Método de origem';
COMMENT ON COLUMN integracoes_logs.linha_origem IS 'Linha de origem';
COMMENT ON COLUMN integracoes_logs.dados_adicionais_json IS 'Dados adicionais em JSON';
COMMENT ON COLUMN integracoes_logs.exception_class IS 'Classe da exceção';
COMMENT ON COLUMN integracoes_logs.exception_message IS 'Mensagem da exceção';
COMMENT ON COLUMN integracoes_logs.stack_trace IS 'Stack trace';
COMMENT ON COLUMN integracoes_logs.memoria_usada_mb IS 'Memória usada em MB';
COMMENT ON COLUMN integracoes_logs.cpu_usada_percentual IS 'CPU usada em percentual';
COMMENT ON COLUMN integracoes_logs.tempo_execucao_ms IS 'Tempo de execução em ms';
COMMENT ON COLUMN integracoes_logs.id_execucao IS 'ID da execução';
COMMENT ON COLUMN integracoes_logs.url_requisicao IS 'URL da requisição';
COMMENT ON COLUMN integracoes_logs.metodo_http IS 'Método HTTP';
COMMENT ON COLUMN integracoes_logs.status_resposta IS 'Status da resposta';
COMMENT ON COLUMN integracoes_logs.usuario_solicitante IS 'Usuário solicitante';
COMMENT ON COLUMN integracoes_logs.ip_origem IS 'IP de origem';
COMMENT ON COLUMN integracoes_logs.modulo_origem IS 'Módulo de origem';
COMMENT ON COLUMN integracoes_logs.data_registro IS 'Data de registro';

COMMENT ON TABLE integracoes_agendamento IS 'Configurações de agendamento de integração';
COMMENT ON COLUMN integracoes_agendamento.id_integracao IS 'ID da integração';
COMMENT ON COLUMN integracoes_agendamento.id_endpoint IS 'ID do endpoint';
COMMENT ON COLUMN integracoes_agendamento.nome_agendamento IS 'Nome do agendamento';
COMMENT ON COLUMN integracoes_agendamento.descricao_agendamento IS 'Descrição do agendamento';
COMMENT ON COLUMN integracoes_agendamento.expressao_cron IS 'Expressão cron';
COMMENT ON COLUMN integracoes_agendamento.timezone IS 'Timezone';
COMMENT ON COLUMN integracoes_agendamento.data_inicio_agendamento IS 'Data de início do agendamento';
COMMENT ON COLUMN integracoes_agendamento.data_fim_agendamento IS 'Data de fim do agendamento';
COMMENT ON COLUMN integracoes_agendamento.max_execucoes_simultaneas IS 'Máximo de execuções simultâneas';
COMMENT ON COLUMN integracoes_agendamento.timeout_execucao IS 'Timeout de execução';
COMMENT ON COLUMN integracoes_agendamento.retry_on_failure IS 'Retry em caso de falha';
COMMENT ON COLUMN integracoes_agendamento.max_retries IS 'Máximo de retentativas';
COMMENT ON COLUMN integracoes_agendamento.retry_delay_seconds IS 'Delay entre retentativas';
COMMENT ON COLUMN integracoes_agendamento.parametros_execucao IS 'Parâmetros de execução';
COMMENT ON COLUMN integracoes_agendamento.headers_personalizados IS 'Headers personalizados';
COMMENT ON COLUMN integracoes_agendamento.body_template IS 'Template do body';
COMMENT ON COLUMN integracoes_agendamento.notificar_sucesso IS 'Notificar em caso de sucesso';
COMMENT ON COLUMN integracoes_agendamento.notificar_erro IS 'Notificar em caso de erro';
COMMENT ON COLUMN integracoes_agendamento.email_destinatarios IS 'Destinatários dos e-mails';
COMMENT ON COLUMN integracoes_agendamento.template_notificacao IS 'Template de notificação';
COMMENT ON COLUMN integracoes_agendamento.evitar_concorrencia IS 'Evitar concorrência';
COMMENT ON COLUMN integracoes_agendamento.lock_timeout_seconds IS 'Timeout do lock';
COMMENT ON COLUMN integracoes_agendamento.lock_key_pattern IS 'Pattern da chave do lock';
COMMENT ON COLUMN integracoes_agendamento.manter_historico_execucoes IS 'Manter histórico de execuções';
COMMENT ON COLUMN integracoes_agendamento.dias_retention_historico IS 'Dias de retenção do histórico';
COMMENT ON COLUMN integracoes_agendamento.ativo IS 'Agendamento ativo';
COMMENT ON COLUMN integracoes_agendamento.status_agendamento IS 'Status do agendamento';
COMMENT ON COLUMN integracoes_agendamento.motivo_status IS 'Motivo do status';
COMMENT ON COLUMN integracoes_agendamento.data_ultima_execucao IS 'Data da última execução';
COMMENT ON COLUMN integracoes_agendamento.data_proxima_execucao IS 'Data da próxima execução';
COMMENT ON COLUMN integracoes_agendamento.total_execucoes IS 'Total de execuções';
COMMENT ON COLUMN integracoes_agendamento.total_sucessos IS 'Total de sucessos';
COMMENT ON COLUMN integracoes_agendamento.total_erros IS 'Total de erros';
COMMENT ON COLUMN integracoes_agendamento.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN integracoes_agendamento.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN integracoes_agendamento.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN integracoes_agendamento.usuario_ultima_atualizacao IS 'Usuário da última atualização';
