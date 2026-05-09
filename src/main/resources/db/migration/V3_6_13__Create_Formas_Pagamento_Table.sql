-- =====================================================
-- Hermes Comercial v3.6.13 - Migration
-- Criar tabela de Formas de Pagamento
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Formas de Pagamento
CREATE TABLE IF NOT EXISTS formas_pagamento (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos
    nome_forma VARCHAR(100) NOT NULL UNIQUE,
    descricao_forma TEXT,
    codigo_forma VARCHAR(20) UNIQUE,
    sigla_forma VARCHAR(10),
    
    -- Tipo e Categoria
    tipo_forma VARCHAR(30) NOT NULL, -- DINHEIRO, CARTAO, CHEQUE, CREDITO, DEBITO, PIX, BOLETO, CARNÊ, VALE, OUTROS
    categoria_forma VARCHAR(30) NOT NULL, -- A_VISTA, A_PRAZO, PARCELADO, RECARGA, TRANSFERENCIA
    subcategoria_forma VARCHAR(30), -- CREDITO_PARCELADO, DEBITO_A_VISTA, PIX_INSTANTANEO, BOLETO_BANCARIO
    
    -- Configurações Gerais
    ativa BOOLEAN DEFAULT TRUE,
    padrao BOOLEAN DEFAULT FALSE,
    ordem_exibicao INTEGER DEFAULT 0,
    icone_forma VARCHAR(100),
    cor_forma VARCHAR(7) DEFAULT '#000000',
    
    -- Configurações de Valor
    valor_minimo DECIMAL(15,2) DEFAULT 0.00,
    valor_maximo DECIMAL(15,2),
    valor_padrao DECIMAL(15,2),
    permite_troco BOOLEAN DEFAULT TRUE,
    exige_valor_exato BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Parcelamento
    permite_parcelamento BOOLEAN DEFAULT FALSE,
    parcelamento_minimo INTEGER DEFAULT 2,
    parcelamento_maximo INTEGER DEFAULT 12,
    parcelamento_sem_juros INTEGER DEFAULT 1,
    juros_parcelamento DECIMAL(8,4) DEFAULT 0.0000,
    juros_mes BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Taxa
    taxa_administracao DECIMAL(8,4) DEFAULT 0.0000,
    taxa_tipo VARCHAR(20) DEFAULT 'PERCENTUAL', -- PERCENTUAL, FIXO
    taxa_valor_fixo DECIMAL(15,2) DEFAULT 0.00,
    taxa_antecipacao DECIMAL(8,4) DEFAULT 0.0000,
    repasse_taxa_cliente BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Cartão
    bandeira_cartao VARCHAR(50), -- VISA, MASTERCARD, ELO, AMEX, HIPERCARD, AURA, DINERS
    tipo_cartao VARCHAR(20), -- CREDITO, DEBITO, MULTIPLO
    exigir_senha BOOLEAN DEFAULT FALSE,
    exigir_documento BOOLEAN DEFAULT FALSE,
    autorizacao_online BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Cheque
    tipo_cheque VARCHAR(20), -- ADMINISTRATIVO, VISADO, NOMINAL, PRE_DATADO
    dias_compensacao INTEGER DEFAULT 3,
    exige_cadastro_cheque BOOLEAN DEFAULT TRUE,
    permite_terceiros BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Pix
    chave_pix_tipo VARCHAR(20), -- CPF, CNPJ, EMAIL, TELEFONE, CHAVE_ALEATORIA
    chave_pix_valor VARCHAR(255),
    banco_pix VARCHAR(100),
    agencia_pix VARCHAR(20),
    conta_pix VARCHAR(30),
    titular_pix VARCHAR(100),
    
    -- Configurações de Boleto
    banco_boleto VARCHAR(100),
    convenio_boleto VARCHAR(50),
    carteira_boleto VARCHAR(20),
    nosso_numero VARCHAR(20),
    instrucoes_boleto TEXT,
    dias_vencimento_boleto INTEGER DEFAULT 5,
    multa_atraso DECIMAL(8,4) DEFAULT 0.0200,
    juros_atraso_diario DECIMAL(8,6) DEFAULT 0.000333,
    
    -- Configurações de Vale
    tipo_vale VARCHAR(30), -- ALIMENTACAO, REFEICAO, TRANSPORTE, CULTURA, COMBUSTIVEL, PRESENTE
    empresa_vale VARCHAR(100),
    codigo_vale VARCHAR(50),
    saldo_verificacao BOOLEAN DEFAULT TRUE,
    permite_saldo_negativo BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Sistema
    integracao_externa BOOLEAN DEFAULT FALSE,
    api_endpoint VARCHAR(500),
    api_token VARCHAR(255),
    api_timeout INTEGER DEFAULT 30,
    api_retries INTEGER DEFAULT 3,
    
    -- Configurações de Caixa
    movimenta_caixa BOOLEAN DEFAULT TRUE,
    tipo_movimentacao VARCHAR(20) DEFAULT 'ENTRADA', -- ENTRADA, SAIDA, AMBOS
    exige_conferencia BOOLEAN DEFAULT FALSE,
    permite_cancelamento BOOLEAN DEFAULT TRUE,
    prazo_cancelamento_horas INTEGER DEFAULT 24,
    
    -- Configurações de Relatório
    emite_comprovante BOOLEAN DEFAULT TRUE,
    modelo_comprovante VARCHAR(50) DEFAULT 'PADRAO',
    informa_cliente_comprovante BOOLEAN DEFAULT TRUE,
    detalhes_comprovante TEXT,
    
    -- Configurações de Notificação
    notifica_cliente BOOLEAN DEFAULT FALSE,
    tipo_notificacao VARCHAR(20), -- EMAIL, SMS, WHATSAPP, PUSH
    template_notificacao TEXT,
    envia_recibo_automatico BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Segurança
    exige_autenticacao BOOLEAN DEFAULT FALSE,
    nivel_autenticacao VARCHAR(20) DEFAULT 'BASICO', -- BASICO, INTERMEDIARIO, AVANCADO
    limite_diario DECIMAL(15,2),
    limite_transacao DECIMAL(15,2),
    monitoramento_fraude BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Integração Fiscal
    cfop_padrao VARCHAR(10),
    natureza_operacao VARCHAR(100),
    informar_fiscal BOOLEAN DEFAULT TRUE,
    codigo_tributacao VARCHAR(20),
    
    -- Configurações de Contabilidade
    conta_contabil VARCHAR(20),
    centro_custo VARCHAR(20),
    plano_contas VARCHAR(20),
    rateio_despesa BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Estoque
    libera_estoque BOOLEAN DEFAULT TRUE,
    momento_liberacao VARCHAR(20) DEFAULT 'PAGAMENTO', -- PAGAMENTO, AUTORIZACAO, CONCLUSAO
    bloqueia_estoque_pendente BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Validade
    data_validade_inicio DATE,
    data_validade_fim DATE,
    horario_funcionamento_inicio TIME,
    horario_funcionamento_fim TIME,
    dias_semana_permitidos TEXT, -- 1,2,3,4,5 (segunda a sexta)
    
    -- Configurações de Filial
    todas_filiais BOOLEAN DEFAULT TRUE,
    filiais_permitidas TEXT, -- Lista de IDs das filiais separados por vírgula
    
    -- Configurações de Campanha
    permite_campanha BOOLEAN DEFAULT FALSE,
    desconto_campanha DECIMAL(8,4) DEFAULT 0.0000,
    cashback_campanha DECIMAL(8,4) DEFAULT 0.0000,
    
    -- Configurações de Moeda
    moeda_padrao VARCHAR(10) DEFAULT 'BRL',
    aceita_outras_moedas BOOLEAN DEFAULT FALSE,
    moedas_aceitas TEXT, -- USD, EUR, etc
    conversao_automatica BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Arredondamento
    tipo_arredondamento VARCHAR(20) DEFAULT 'NORMAL', -- NORMAL, CIMA, BAIXO, PROXIMO_10, PROXIMO_50
    precisao_decimal INTEGER DEFAULT 2,
    
    -- Status e Controle
    status_forma VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, BLOQUEADA, EM_MANUTENCAO
    motivo_status TEXT,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_forma CHECK (tipo_forma IN ('DINHEIRO', 'CARTAO', 'CHEQUE', 'CREDITO', 'DEBITO', 'PIX', 'BOLETO', 'CARNE', 'VALE', 'OUTROS')),
    CONSTRAINT chk_categoria_forma CHECK (categoria_forma IN ('A_VISTA', 'A_PRAZO', 'PARCELADO', 'RECARGA', 'TRANSFERENCIA')),
    CONSTRAINT chk_subcategoria_forma CHECK (subcategoria_forma IN ('CREDITO_PARCELADO', 'DEBITO_A_VISTA', 'PIX_INSTANTANEO', 'BOLETO_BANCARIO', 'CHEQUE_PRE_DATADO', 'VALE_ALIMENTACAO', 'VALE_REFEICAO', 'VALE_TRANSPORTE', 'VALE_CULTURA', 'VALE_COMBUSTIVEL', 'VALE_PRESENTE')),
    CONSTRAINT chk_taxa_tipo CHECK (taxa_tipo IN ('PERCENTUAL', 'FIXO')),
    CONSTRAINT chk_tipo_cartao CHECK (tipo_cartao IN ('CREDITO', 'DEBITO', 'MULTIPLO')),
    CONSTRAINT chk_tipo_cheque CHECK (tipo_cheque IN ('ADMINISTRATIVO', 'VISADO', 'NOMINAL', 'PRE_DATADO')),
    CONSTRAINT chk_chave_pix_tipo CHECK (chave_pix_tipo IN ('CPF', 'CNPJ', 'EMAIL', 'TELEFONE', 'CHAVE_ALEATORIA')),
    CONSTRAINT chk_tipo_vale CHECK (tipo_vale IN ('ALIMENTACAO', 'REFEICAO', 'TRANSPORTE', 'CULTURA', 'COMBUSTIVEL', 'PRESENTE')),
    CONSTRAINT chk_tipo_notificacao CHECK (tipo_notificacao IN ('EMAIL', 'SMS', 'WHATSAPP', 'PUSH')),
    CONSTRAINT chk_nivel_autenticacao CHECK (nivel_autenticacao IN ('BASICO', 'INTERMEDIARIO', 'AVANCADO')),
    CONSTRAINT chk_tipo_movimentacao CHECK (tipo_movimentacao IN ('ENTRADA', 'SAIDA', 'AMBOS')),
    CONSTRAINT chk_status_forma CHECK (status_forma IN ('ATIVA', 'INATIVA', 'BLOQUEADA', 'EM_MANUTENCAO')),
    CONSTRAINT chk_tipo_arredondamento CHECK (tipo_arredondamento IN ('NORMAL', 'CIMA', 'BAIXO', 'PROXIMO_10', 'PROXIMO_50')),
    CONSTRAINT chk_limites_formas_pagamento CHECK (
        valor_minimo >= 0 AND
        valor_maximo IS NULL OR valor_maximo > 0 AND
        valor_padrao IS NULL OR valor_padrao >= 0 AND
        parcelamento_minimo >= 2 AND
        parcelamento_maximo >= parcelamento_minimo AND
        juros_parcelamento >= 0 AND
        taxa_administracao >= 0 AND
        taxa_valor_fixo >= 0 AND
        taxa_antecipacao >= 0 AND
        dias_compensacao >= 0 AND
        dias_vencimento_boleto >= 0 AND
        multa_atraso >= 0 AND
        juros_atraso_diario >= 0 AND
        api_timeout > 0 AND
        api_retries > 0 AND
        prazo_cancelamento_horas > 0 AND
        limite_diario IS NULL OR limite_diario > 0 AND
        limite_transacao IS NULL OR limite_transacao > 0 AND
        desconto_campanha >= 0 AND
        cashback_campanha >= 0 AND
        precisao_decimal >= 0 AND
        ordem_exibicao >= 0
    )
);

-- Criar tabela de Configurações de Parcelamento
CREATE TABLE IF NOT EXISTS formas_pagamento_parcelamento (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_forma_pagamento INTEGER NOT NULL REFERENCES formas_pagamento(id) ON DELETE CASCADE,
    
    -- Configurações do Parcelamento
    numero_parcelas INTEGER NOT NULL,
    descricao_parcela VARCHAR(100),
    
    -- Configurações de Valor
    percentual_entrada DECIMAL(8,4) DEFAULT 0.0000,
    juros_parcela DECIMAL(8,4) DEFAULT 0.0000,
    taxa_administracao DECIMAL(8,4) DEFAULT 0.0000,
    
    -- Configurações de Prazo
    dias_primeira_parcela INTEGER DEFAULT 0,
    intervalo_dias_parcelas INTEGER DEFAULT 30,
    
    -- Configurações de Calculo
    tipo_calculo VARCHAR(20) DEFAULT 'PRICE', -- PRICE, SAC, ALEMAO
    capitalizacao_juros VARCHAR(20) DEFAULT 'MENSAL', -- MENSAL, ANUAL, DIARIO
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_numero_parcelas CHECK (numero_parcelas > 0),
    CONSTRAINT chk_percentual_entrada CHECK (percentual_entrada >= 0 AND percentual_entrada <= 100),
    CONSTRAINT chk_juros_parcela CHECK (juros_parcela >= 0),
    CONSTRAINT chk_taxa_administracao_parcela CHECK (taxa_administracao >= 0),
    CONSTRAINT chk_dias_primeira_parcela CHECK (dias_primeira_parcela >= 0),
    CONSTRAINT chk_intervalo_dias_parcelas CHECK (intervalo_dias_parcelas > 0),
    CONSTRAINT chk_tipo_calculo CHECK (tipo_calculo IN ('PRICE', 'SAC', 'ALEMAO')),
    CONSTRAINT chk_capitalizacao_juros CHECK (capitalizacao_juros IN ('MENSAL', 'ANUAL', 'DIARIO')),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_forma_pagamento, numero_parcelas)
);

-- Criar tabela de Taxas de Formas de Pagamento
CREATE TABLE IF NOT EXISTS formas_pagamento_taxas (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_forma_pagamento INTEGER NOT NULL REFERENCES formas_pagamento(id) ON DELETE CASCADE,
    
    -- Configurações da Taxa
    nome_taxa VARCHAR(100) NOT NULL,
    descricao_taxa TEXT,
    tipo_taxa VARCHAR(20) NOT NULL, -- PERCENTUAL, FIXO, FAIXA_VALOR
    
    -- Configurações de Valor
    valor_taxa DECIMAL(15,2),
    percentual_taxa DECIMAL(8,4),
    
    -- Configurações de Faixa (para taxas por faixa de valor)
    valor_minimo_faixa DECIMAL(15,2),
    valor_maximo_faixa DECIMAL(15,2),
    
    -- Configurações de Aplicação
    aplica_sobre VARCHAR(20) DEFAULT 'TOTAL', -- TOTAL, PARCELA, ENTRADA
    repasse_cliente BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Vigência
    data_vigencia_inicio DATE,
    data_vigencia_fim DATE,
    
    -- Configurações de Segmento
    segmento_cliente VARCHAR(50), -- PF, PJ, GOVERNO
    tipo_cliente VARCHAR(50), -- VIP, COMUM, CORPORATIVO
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_taxa CHECK (tipo_taxa IN ('PERCENTUAL', 'FIXO', 'FAIXA_VALOR')),
    CONSTRAINT chk_valor_taxa CHECK (valor_taxa IS NULL OR valor_taxa >= 0),
    CONSTRAINT chk_percentual_taxa CHECK (percentual_taxa IS NULL OR (percentual_taxa >= 0 AND percentual_taxa <= 100)),
    CONSTRAINT chk_valor_minimo_faixa CHECK (valor_minimo_faixa IS NULL OR valor_minimo_faixa >= 0),
    CONSTRAINT chk_valor_maximo_faixa CHECK (valor_maximo_faixa IS NULL OR valor_maximo_faixa > 0),
    CONSTRAINT chk_aplica_sobre CHECK (aplica_sobre IN ('TOTAL', 'PARCELA', 'ENTRADA')),
    CONSTRAINT chk_limites_taxas CHECK (
        (tipo_taxa = 'PERCENTUAL' AND percentual_taxa IS NOT NULL) OR
        (tipo_taxa = 'FIXO' AND valor_taxa IS NOT NULL) OR
        (tipo_taxa = 'FAIXA_VALOR' AND valor_minimo_faixa IS NOT NULL AND valor_maximo_faixa IS NOT NULL)
    )
);

-- Criar tabela de Histórico de Uso de Formas de Pagamento
CREATE TABLE IF NOT EXISTS formas_pagamento_historico (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_forma_pagamento INTEGER NOT NULL REFERENCES formas_pagamento(id),
    
    -- Dados da Transação
    id_transacao VARCHAR(100),
    tipo_transacao VARCHAR(50), -- VENDA, RECEBIMENTO, ESTORNO, CANCELAMENTO
    valor_transacao DECIMAL(15,2) NOT NULL,
    valor_liquido DECIMAL(15,2),
    valor_taxa DECIMAL(15,2),
    
    -- Dados do Parcelamento
    numero_parcelas INTEGER,
    valor_parcela DECIMAL(15,2),
    parcela_atual INTEGER,
    
    -- Dados do Cartão
    numero_cartao_mascarado VARCHAR(20),
    bandeira_cartao VARCHAR(50),
    autorizacao_cartao VARCHAR(50),
    nsu_transacao VARCHAR(50),
    
    -- Dados do Cheque
    numero_cheque VARCHAR(20),
    banco_cheque VARCHAR(50),
    agencia_cheque VARCHAR(20),
    conta_cheque VARCHAR(30),
    data_compensacao_cheque DATE,
    
    -- Dados do Pix
    chave_pix_usada VARCHAR(255),
    txid_pix VARCHAR(100),
    data_horario_pix TIMESTAMP,
    
    -- Dados do Boleto
    numero_boleto VARCHAR(50),
    nosso_numero_boleto VARCHAR(20),
    data_vencimento_boleto DATE,
    data_pagamento_boleto DATE,
    
    -- Dados Contextuais
    id_venda INTEGER,
    id_cliente INTEGER,
    id_usuario INTEGER,
    id_caixa INTEGER,
    id_filia INTEGER,
    
    -- Dados de Localização
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(100),
    user_agent VARCHAR(500),
    
    -- Status e Resultado
    status_transacao VARCHAR(20) NOT NULL, -- APROVADO, REPROVADO, PENDENTE, CANCELADO, ESTORNADO
    mensagem_retorno TEXT,
    codigo_erro VARCHAR(50),
    
    -- Performance
    tempo_processamento_ms INTEGER,
    data_hora_transacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Auditoria
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_registro VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_transacao CHECK (tipo_transacao IN ('VENDA', 'RECEBIMENTO', 'ESTORNO', 'CANCELAMENTO')),
    CONSTRAINT chk_status_transacao CHECK (status_transacao IN ('APROVADO', 'REPROVADO', 'PENDENTE', 'CANCELADO', 'ESTORNADO')),
    CONSTRAINT chk_limites_historico CHECK (
        valor_transacao > 0 AND
        valor_liquido IS NULL OR valor_liquido >= 0 AND
        valor_taxa IS NULL OR valor_taxa >= 0 AND
        numero_parcelas IS NULL OR numero_parcelas > 0 AND
        valor_parcela IS NULL OR valor_parcela > 0 AND
        parcela_atual IS NULL OR parcela_atual > 0 AND
        tempo_processamento_ms IS NULL OR tempo_processamento_ms >= 0
    )
);

-- Criar tabela de Configurações de Integração de Formas de Pagamento
CREATE TABLE IF NOT EXISTS formas_pagamento_integracao (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_forma_pagamento INTEGER NOT NULL REFERENCES formas_pagamento(id) ON DELETE CASCADE,
    
    -- Configurações da Integração
    nome_integracao VARCHAR(100) NOT NULL,
    provedor_integracao VARCHAR(100), -- MERCADO_PAGO, PAGSEGURO, GETNET, REDE, CIELO, STONE, ADYEN
    
    -- Configurações de API
    endpoint_producao VARCHAR(500),
    endpoint_homologacao VARCHAR(500),
    api_key VARCHAR(255),
    api_secret VARCHAR(255),
    token_acesso VARCHAR(500),
    
    -- Configurações de Autenticação
    tipo_autenticacao VARCHAR(20), -- BASIC, BEARER, OAUTH2, API_KEY
    client_id VARCHAR(100),
    client_secret VARCHAR(255),
    scope_autenticacao VARCHAR(200),
    
    -- Configurações de Webhook
    webhook_url VARCHAR(500),
    webhook_secret VARCHAR(255),
    webhook_eventos TEXT, -- payment.created, payment.updated, etc
    
    -- Configurações de Timeout e Retentativas
    timeout_conexao INTEGER DEFAULT 30,
    timeout_leitura INTEGER DEFAULT 60,
    maximo_retries INTEGER DEFAULT 3,
    intervalo_retries INTEGER DEFAULT 5,
    
    -- Configurações de Ambiente
    ambiente VARCHAR(20) DEFAULT 'HOMOLOGACAO', -- PRODUCAO, HOMOLOGACAO, DESENVOLVIMENTO
    debug_mode BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Formato
    formato_requisicao VARCHAR(20) DEFAULT 'JSON', -- JSON, XML, FORM_DATA
    formato_resposta VARCHAR(20) DEFAULT 'JSON',
    encoding VARCHAR(20) DEFAULT 'UTF-8',
    
    -- Configurações de Segurança
    ssl_verificado BOOLEAN DEFAULT TRUE,
    certificado_ssl VARCHAR(500),
    chave_ssl VARCHAR(500),
    
    -- Configurações de Proxy
    usa_proxy BOOLEAN DEFAULT FALSE,
    proxy_host VARCHAR(255),
    proxy_porta INTEGER,
    proxy_usuario VARCHAR(100),
    proxy_senha VARCHAR(255),
    
    -- Configurações de Logs
    log_requisicoes BOOLEAN DEFAULT TRUE,
    log_respostas BOOLEAN DEFAULT TRUE,
    log_erros BOOLEAN DEFAULT TRUE,
    dias_retention_logs INTEGER DEFAULT 30,
    
    -- Status e Controle
    ativa BOOLEAN DEFAULT TRUE,
    data_ultimo_teste TIMESTAMP,
    resultado_ultimo_teste TEXT,
    status_integracao VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, ERRO, MANUTENCAO
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_provedor_integracao CHECK (provedor_integracao IN ('MERCADO_PAGO', 'PAGSEGURO', 'GETNET', 'REDE', 'CIELO', 'STONE', 'ADYEN', 'STRIPE', 'PAYPAL')),
    CONSTRAINT chk_tipo_autenticacao CHECK (tipo_autenticacao IN ('BASIC', 'BEARER', 'OAUTH2', 'API_KEY')),
    CONSTRAINT chk_ambiente CHECK (ambiente IN ('PRODUCAO', 'HOMOLOGACAO', 'DESENVOLVIMENTO')),
    CONSTRAINT chk_formato_requisicao CHECK (formato_requisicao IN ('JSON', 'XML', 'FORM_DATA')),
    CONSTRAINT chk_formato_resposta CHECK (formato_resposta IN ('JSON', 'XML')),
    CONSTRAINT chk_encoding CHECK (encoding IN ('UTF-8', 'ISO-8859-1', 'ASCII')),
    CONSTRAINT chk_status_integracao CHECK (status_integracao IN ('ATIVA', 'INATIVA', 'ERRO', 'MANUTENCAO')),
    CONSTRAINT chk_limites_integracao CHECK (
        timeout_conexao > 0 AND
        timeout_leitura > 0 AND
        maximo_retries > 0 AND
        intervalo_retries > 0 AND
        proxy_porta IS NULL OR (proxy_porta > 0 AND proxy_porta <= 65535) AND
        dias_retention_logs > 0
    ),
    
    -- Unique constraint para evitar duplicatas
    UNIQUE (id_forma_pagamento, nome_integracao)
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_nome_forma ON formas_pagamento(nome_forma);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_tipo_forma ON formas_pagamento(tipo_forma);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_categoria_forma ON formas_pagamento(categoria_forma);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_ativa ON formas_pagamento(ativa);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_padrao ON formas_pagamento(padrao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_status_forma ON formas_pagamento(status_forma);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_ordem_exibicao ON formas_pagamento(ordem_exibicao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_bandeira_cartao ON formas_pagamento(bandeira_cartao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_tipo_cartao ON formas_pagamento(tipo_cartao);

CREATE INDEX IF NOT EXISTS idx_formas_pagamento_parcelamento_id_forma ON formas_pagamento_parcelamento(id_forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_parcelamento_numero_parcelas ON formas_pagamento_parcelamento(numero_parcelas);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_parcelamento_ativa ON formas_pagamento_parcelamento(ativa);

CREATE INDEX IF NOT EXISTS idx_formas_pagamento_taxas_id_forma ON formas_pagamento_taxas(id_forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_taxas_tipo_taxa ON formas_pagamento_taxas(tipo_taxa);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_taxas_ativa ON formas_pagamento_taxas(ativa);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_taxas_data_vigencia ON formas_pagamento_taxas(data_vigencia_inicio, data_vigencia_fim);

CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_id_forma ON formas_pagamento_historico(id_forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_data_transacao ON formas_pagamento_historico(data_hora_transacao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_tipo_transacao ON formas_pagamento_historico(tipo_transacao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_status_transacao ON formas_pagamento_historico(status_transacao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_id_venda ON formas_pagamento_historico(id_venda);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_historico_id_cliente ON formas_pagamento_historico(id_cliente);

CREATE INDEX IF NOT EXISTS idx_formas_pagamento_integracao_id_forma ON formas_pagamento_integracao(id_forma_pagamento);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_integracao_provedor ON formas_pagamento_integracao(provedor_integracao);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_integracao_ambiente ON formas_pagamento_integracao(ambiente);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_integracao_ativa ON formas_pagamento_integracao(ativa);
CREATE INDEX IF NOT EXISTS idx_formas_pagamento_integracao_status ON formas_pagamento_integracao(status_integracao);

-- Inserir formas de pagamento padrão
INSERT INTO formas_pagamento (
    nome_forma, descricao_forma, codigo_forma, sigla_forma, tipo_forma, categoria_forma,
    subcategoria_forma, ativa, padrao, ordem_exibicao, icone_forma, cor_forma,
    valor_minimo, permite_troco, exige_valor_exato, permite_parcelamento,
    parcelamento_minimo, parcelamento_maximo, parcelamento_sem_juros,
    juros_parcelamento, juros_mes, taxa_administracao, taxa_tipo,
    movimenta_caixa, tipo_movimentacao, exige_conferencia, permite_cancelamento,
    prazo_cancelamento_horas, emite_comprovante, modelo_comprovante,
    informa_cliente_comprovante, exige_autenticacao, nivel_autenticacao,
    informar_fiscal, moeda_padrao, status_forma
) VALUES 
(
    'Dinheiro', 'Pagamento em espécie', 'DIN', 'DIN', 'DINHEIRO', 'A_VISTA',
    NULL, TRUE, TRUE, 1, 'cash', '#008000',
    0.00, TRUE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.0000, FALSE, 0.0000, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Cartão de Crédito', 'Pagamento com cartão de crédito', 'CC', 'CC', 'CARTAO', 'A_PRAZO',
    'CREDITO_PARCELADO', TRUE, FALSE, 2, 'credit-card', '#FF6B6B',
    5.00, FALSE, FALSE, TRUE,
    2, 12, 1,
    5.9900, TRUE, 4.9900, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, TRUE, 'INTERMEDIARIO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Cartão de Débito', 'Pagamento com cartão de débito', 'CD', 'CD', 'CARTAO', 'A_VISTA',
    'DEBITO_A_VISTA', TRUE, FALSE, 3, 'debit-card', '#4ECDC4',
    5.00, TRUE, FALSE, FALSE,
    NULL, NULL, NULL,
    2.9900, FALSE, 2.9900, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, TRUE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'PIX', 'Pagamento instantâneo via PIX', 'PIX', 'PIX', 'PIX', 'A_VISTA',
    'PIX_INSTANTANEO', TRUE, FALSE, 4, 'qrcode', '#9B59B6',
    0.50, TRUE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.9900, FALSE, 0.9900, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Cheque', 'Pagamento com cheque', 'CHQ', 'CHQ', 'CHEQUE', 'A_PRAZO',
    'CHEQUE_PRE_DATADO', TRUE, FALSE, 5, 'check', '#F39C12',
    50.00, FALSE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.0000, FALSE, 0.0000, 'PERCENTUAL',
    TRUE, 'ENTRADA', TRUE, TRUE,
    168, TRUE, 'PADRAO',
    TRUE, FALSE, 'INTERMEDIARIO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Boleto Bancário', 'Pagamento com boleto bancário', 'BOL', 'BOL', 'BOLETO', 'A_PRAZO',
    'BOLETO_BANCARIO', TRUE, FALSE, 6, 'file-invoice-dollar', '#3498DB',
    10.00, FALSE, FALSE, FALSE,
    NULL, NULL, NULL,
    3.9900, FALSE, 3.9900, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    168, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Vale Alimentação', 'Pagamento com vale alimentação', 'VA', 'VAL', 'VALE', 'A_VISTA',
    'VALE_ALIMENTACAO', TRUE, FALSE, 7, 'utensils', '#E67E22',
    1.00, FALSE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.0000, FALSE, 0.0000, 'PERCENTUAL',
    TRUE, 'ENTRADA', TRUE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Vale Refeição', 'Pagamento com vale refeição', 'VR', 'VRE', 'VALE', 'A_VISTA',
    'VALE_REFEICAO', TRUE, FALSE, 8, 'coffee', '#D35400',
    1.00, FALSE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.0000, FALSE, 0.0000, 'PERCENTUAL',
    TRUE, 'ENTRADA', TRUE, TRUE,
    24, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Transferência Bancária', 'Pagamento por transferência bancária', 'TRF', 'TRF', 'OUTROS', 'A_PRAZO',
    'TRANSFERENCIA', TRUE, FALSE, 9, 'exchange-alt', '#16A085',
    100.00, FALSE, FALSE, FALSE,
    NULL, NULL, NULL,
    0.0000, FALSE, 0.0000, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    168, TRUE, 'PADRAO',
    TRUE, FALSE, 'INTERMEDIARIO',
    TRUE, 'BRL', 'ATIVA'
),
(
    'Carnê', 'Pagamento parcelado com carnê', 'CAR', 'CAR', 'CARNE', 'A_PRAZO',
    NULL, TRUE, FALSE, 10, 'receipt', '#7F8C8D',
    20.00, FALSE, FALSE, TRUE,
    2, 24, 1,
    8.9900, TRUE, 8.9900, 'PERCENTUAL',
    TRUE, 'ENTRADA', FALSE, TRUE,
    168, TRUE, 'PADRAO',
    TRUE, FALSE, 'BASICO',
    TRUE, 'BRL', 'ATIVA'
) ON CONFLICT DO NOTHING;

-- Inserir configurações de parcelamento padrão para cartão de crédito
INSERT INTO formas_pagamento_parcelamento (
    id_forma_pagamento, numero_parcelas, descricao_parcela, percentual_entrada,
    juros_parcela, taxa_administracao, dias_primeira_parcela, intervalo_dias_parcelas,
    tipo_calculo, capitalizacao_juros, ativa
)
SELECT 
    fp.id, 2, '2x sem juros', 0.0000, 0.0000, 4.9900, 0, 30, 'PRICE', 'MENSAL', TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CC'
ON CONFLICT DO NOTHING;

INSERT INTO formas_pagamento_parcelamento (
    id_forma_pagamento, numero_parcelas, descricao_parcela, percentual_entrada,
    juros_parcela, taxa_administracao, dias_primeira_parcela, intervalo_dias_parcelas,
    tipo_calculo, capitalizacao_juros, ativa
)
SELECT 
    fp.id, 3, '3x sem juros', 0.0000, 0.0000, 4.9900, 0, 30, 'PRICE', 'MENSAL', TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CC'
ON CONFLICT DO NOTHING;

INSERT INTO formas_pagamento_parcelamento (
    id_forma_pagamento, numero_parcelas, descricao_parcela, percentual_entrada,
    juros_parcela, taxa_administracao, dias_primeira_parcela, intervalo_dias_parcelas,
    tipo_calculo, capitalizacao_juros, ativa
)
SELECT 
    fp.id, 6, '6x com juros', 0.0000, 5.9900, 4.9900, 0, 30, 'PRICE', 'MENSAL', TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CC'
ON CONFLICT DO NOTHING;

INSERT INTO formas_pagamento_parcelamento (
    id_forma_pagamento, numero_parcelas, descricao_parcela, percentual_entrada,
    juros_parcela, taxa_administracao, dias_primeira_parcela, intervalo_dias_parcelas,
    tipo_calculo, capitalizacao_juros, ativa
)
SELECT 
    fp.id, 12, '12x com juros', 0.0000, 5.9900, 4.9900, 0, 30, 'PRICE', 'MENSAL', TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CC'
ON CONFLICT DO NOTHING;

-- Inserir taxas padrão
INSERT INTO formas_pagamento_taxas (
    id_forma_pagamento, nome_taxa, descricao_taxa, tipo_taxa, percentual_taxa,
    aplica_sobre, repasse_cliente, ativa
)
SELECT 
    fp.id, 'Taxa Máquina', 'Taxa administrativa da máquina de cartão', 'PERCENTUAL', 4.9900,
    'TOTAL', FALSE, TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CD'
ON CONFLICT DO NOTHING;

INSERT INTO formas_pagamento_taxas (
    id_forma_pagamento, nome_taxa, descricao_taxa, tipo_taxa, percentual_taxa,
    aplica_sobre, repasse_cliente, ativa
)
SELECT 
    fp.id, 'Taxa Antecipação', 'Taxa de antecipação de recebíveis', 'PERCENTUAL', 2.9900,
    'TOTAL', FALSE, TRUE
FROM formas_pagamento fp 
WHERE fp.codigo_forma = 'CC'
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE formas_pagamento IS 'Tabela de formas de pagamento do sistema';
COMMENT ON COLUMN formas_pagamento.nome_forma IS 'Nome único da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.descricao_forma IS 'Descrição detalhada da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.codigo_forma IS 'Código único da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.sigla_forma IS 'Sigla abreviada da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.tipo_forma IS 'Tipo principal da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.categoria_forma IS 'Categoria da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.subcategoria_forma IS 'Subcategoria específica da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.ativa IS 'Indica se a forma de pagamento está ativa';
COMMENT ON COLUMN formas_pagamento.padrao IS 'Indica se é a forma de pagamento padrão';
COMMENT ON COLUMN formas_pagamento.ordem_exibicao IS 'Ordem de exibição na interface';
COMMENT ON COLUMN formas_pagamento.icone_forma IS 'Ícone representativo da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.cor_forma IS 'Cor hexadecimal da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.valor_minimo IS 'Valor mínimo permitido para pagamento';
COMMENT ON COLUMN formas_pagamento.valor_maximo IS 'Valor máximo permitido para pagamento';
COMMENT ON COLUMN formas_pagamento.valor_padrao IS 'Valor padrão sugerido';
COMMENT ON COLUMN formas_pagamento.permite_troco IS 'Permite dar troco ao cliente';
COMMENT ON COLUMN formas_pagamento.exige_valor_exato IS 'Exige valor exato sem troco';
COMMENT ON COLUMN formas_pagamento.permite_parcelamento IS 'Permite parcelamento do pagamento';
COMMENT ON COLUMN formas_pagamento.parcelamento_minimo IS 'Número mínimo de parcelas';
COMMENT ON COLUMN formas_pagamento.parcelamento_maximo IS 'Número máximo de parcelas';
COMMENT ON COLUMN formas_pagamento.parcelamento_sem_juros IS 'Quantidade de parcelas sem juros';
COMMENT ON COLUMN formas_pagamento.juros_parcelamento IS 'Percentual de juros ao mês';
COMMENT ON COLUMN formas_pagamento.juros_mes IS 'Indica se juros são calculados por mês';
COMMENT ON COLUMN formas_pagamento.taxa_administracao IS 'Taxa de administração percentual';
COMMENT ON COLUMN formas_pagamento.taxa_tipo IS 'Tipo de taxa (percentual ou fixo)';
COMMENT ON COLUMN formas_pagamento.taxa_valor_fixo IS 'Valor fixo da taxa';
COMMENT ON COLUMN formas_pagamento.taxa_antecipacao IS 'Taxa de antecipação percentual';
COMMENT ON COLUMN formas_pagamento.repasse_taxa_cliente IS 'Repassa taxa para o cliente';
COMMENT ON COLUMN formas_pagamento.bandeira_cartao IS 'Bandeira do cartão';
COMMENT ON COLUMN formas_pagamento.tipo_cartao IS 'Tipo do cartão (crédito/débito)';
COMMENT ON COLUMN formas_pagamento.exigir_senha IS 'Exige senha do cartão';
COMMENT ON COLUMN formas_pagamento.exigir_documento IS 'Exige documento do cliente';
COMMENT ON COLUMN formas_pagamento.autorizacao_online IS 'Requer autorização online';
COMMENT ON COLUMN formas_pagamento.tipo_cheque IS 'Tipo de cheque';
COMMENT ON COLUMN formas_pagamento.dias_compensacao IS 'Dias para compensação do cheque';
COMMENT ON COLUMN formas_pagamento.exige_cadastro_cheque IS 'Exige cadastro prévio do cheque';
COMMENT ON COLUMN formas_pagamento.permite_terceiros IS 'Permite cheques de terceiros';
COMMENT ON COLUMN formas_pagamento.chave_pix_tipo IS 'Tipo da chave PIX';
COMMENT ON COLUMN formas_pagamento.chave_pix_valor IS 'Valor da chave PIX';
COMMENT ON COLUMN formas_pagamento.banco_pix IS 'Banco para recebimento PIX';
COMMENT ON COLUMN formas_pagamento.agencia_pix IS 'Agência bancária PIX';
COMMENT ON COLUMN formas_pagamento.conta_pix IS 'Conta bancária PIX';
COMMENT ON COLUMN formas_pagamento.titular_pix IS 'Titular da conta PIX';
COMMENT ON COLUMN formas_pagamento.banco_boleto IS 'Banco emissor do boleto';
COMMENT ON COLUMN formas_pagamento.convenio_boleto IS 'Código do convênio bancário';
COMMENT ON COLUMN formas_pagamento.carteira_boleto IS 'Número da carteira';
COMMENT ON COLUMN formas_pagamento.nosso_numero IS 'Nosso número do boleto';
COMMENT ON COLUMN formas_pagamento.instrucoes_boleto IS 'Instruções do boleto';
COMMENT ON COLUMN formas_pagamento.dias_vencimento_boleto IS 'Dias para vencimento do boleto';
COMMENT ON COLUMN formas_pagamento.multa_atraso IS 'Percentual de multa por atraso';
COMMENT ON COLUMN formas_pagamento.juros_atraso_diario IS 'Juros diários por atraso';
COMMENT ON COLUMN formas_pagamento.tipo_vale IS 'Tipo do vale';
COMMENT ON COLUMN formas_pagamento.empresa_vale IS 'Empresa emissora do vale';
COMMENT ON COLUMN formas_pagamento.codigo_vale IS 'Código do vale';
COMMENT ON COLUMN formas_pagamento.saldo_verificacao IS 'Verifica saldo do vale';
COMMENT ON COLUMN formas_pagamento.permite_saldo_negativo IS 'Permite saldo negativo';
COMMENT ON COLUMN formas_pagamento.integracao_externa IS 'Possui integração externa';
COMMENT ON COLUMN formas_pagamento.api_endpoint IS 'Endpoint da API externa';
COMMENT ON COLUMN formas_pagamento.api_token IS 'Token de acesso à API';
COMMENT ON COLUMN formas_pagamento.api_timeout IS 'Timeout da API em segundos';
COMMENT ON COLUMN formas_pagamento.api_retries IS 'Número de retentativas da API';
COMMENT ON COLUMN formas_pagamento.movimenta_caixa IS 'Movimenta o caixa do PDV';
COMMENT ON COLUMN formas_pagamento.tipo_movimentacao IS 'Tipo de movimentação do caixa';
COMMENT ON COLUMN formas_pagamento.exige_conferencia IS 'Exige conferência manual';
COMMENT ON COLUMN formas_pagamento.permite_cancelamento IS 'Permite cancelamento';
COMMENT ON COLUMN formas_pagamento.prazo_cancelamento_horas IS 'Prazo para cancelamento em horas';
COMMENT ON COLUMN formas_pagamento.emite_comprovante IS 'Emite comprovante automaticamente';
COMMENT ON COLUMN formas_pagamento.modelo_comprovante IS 'Modelo do comprovante';
COMMENT ON COLUMN formas_pagamento.informa_cliente_comprovante IS 'Inclui dados do cliente no comprovante';
COMMENT ON COLUMN formas_pagamento.notifica_cliente IS 'Envia notificação ao cliente';
COMMENT ON COLUMN formas_pagamento.tipo_notificacao IS 'Tipo de notificação';
COMMENT ON COLUMN formas_pagamento.template_notificacao IS 'Template da notificação';
COMMENT ON COLUMN formas_pagamento.envia_recibo_automatico IS 'Envia recibo automaticamente';
COMMENT ON COLUMN formas_pagamento.exige_autenticacao IS 'Exige autenticação adicional';
COMMENT ON COLUMN formas_pagamento.nivel_autenticacao IS 'Nível de autenticação exigido';
COMMENT ON COLUMN formas_pagamento.limite_diario IS 'Limite diário de transações';
COMMENT ON COLUMN formas_pagamento.limite_transacao IS 'Limite por transação';
COMMENT ON COLUMN formas_pagamento.monitoramento_fraude IS 'Ativa monitoramento de fraude';
COMMENT ON COLUMN formas_pagamento.cfop_padrao IS 'CFOP padrão para esta forma';
COMMENT ON COLUMN formas_pagamento.natureza_operacao IS 'Natureza da operação fiscal';
COMMENT ON COLUMN formas_pagamento.informar_fiscal IS 'Informa dados fiscais';
COMMENT ON COLUMN formas_pagamento.codigo_tributacao IS 'Código de tributação';
COMMENT ON COLUMN formas_pagamento.conta_contabil IS 'Conta contábil padrão';
COMMENT ON COLUMN formas_pagamento.centro_custo IS 'Centro de custo padrão';
COMMENT ON COLUMN formas_pagamento.plano_contas IS 'Plano de contas padrão';
COMMENT ON COLUMN formas_pagamento.rateio_despesa IS 'Permite rateio de despesas';
COMMENT ON COLUMN formas_pagamento.libera_estoque IS 'Libera estoque automaticamente';
COMMENT ON COLUMN formas_pagamento.momento_liberacao IS 'Momento da liberação do estoque';
COMMENT ON COLUMN formas_pagamento.bloqueia_estoque_pendente IS 'Bloqueia estoque enquanto pendente';
COMMENT ON COLUMN formas_pagamento.data_validade_inicio IS 'Data inicial de validade';
COMMENT ON COLUMN formas_pagamento.data_validade_fim IS 'Data final de validade';
COMMENT ON COLUMN formas_pagamento.horario_funcionamento_inicio IS 'Horário inicial de funcionamento';
COMMENT ON COLUMN formas_pagamento.horario_funcionamento_fim IS 'Horário final de funcionamento';
COMMENT ON COLUMN formas_pagamento.dias_semana_permitidos IS 'Dias da semana permitidos';
COMMENT ON COLUMN formas_pagamento.todas_filiais IS 'Disponível para todas as filiais';
COMMENT ON COLUMN formas_pagamento.filiais_permitidas IS 'Lista de filiais permitidas';
COMMENT ON COLUMN formas_pagamento.permite_campanha IS 'Permite aplicações de campanha';
COMMENT ON COLUMN formas_pagamento.desconto_campanha IS 'Desconto de campanha percentual';
COMMENT ON COLUMN formas_pagamento.cashback_campanha IS 'Cashback de campanha percentual';
COMMENT ON COLUMN formas_pagamento.moeda_padrao IS 'Moeda padrão da forma';
COMMENT ON COLUMN formas_pagamento.aceita_outras_moedas IS 'Aceita outras moedas';
COMMENT ON COLUMN formas_pagamento.moedas_aceitas IS 'Lista de moedas aceitas';
COMMENT ON COLUMN formas_pagamento.conversao_automatica IS 'Conversão automática de moeda';
COMMENT ON COLUMN formas_pagamento.tipo_arredondamento IS 'Tipo de arredondamento';
COMMENT ON COLUMN formas_pagamento.precisao_decimal IS 'Precisão decimal';
COMMENT ON COLUMN formas_pagamento.status_forma IS 'Status atual da forma de pagamento';
COMMENT ON COLUMN formas_pagamento.motivo_status IS 'Motivo do status atual';
COMMENT ON COLUMN formas_pagamento.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN formas_pagamento.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN formas_pagamento.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN formas_pagamento.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE formas_pagamento_parcelamento IS 'Configurações de parcelamento das formas de pagamento';
COMMENT ON COLUMN formas_pagamento_parcelamento.id_forma_pagamento IS 'ID da forma de pagamento';
COMMENT ON COLUMN formas_pagamento_parcelamento.numero_parcelas IS 'Número de parcelas';
COMMENT ON COLUMN formas_pagamento_parcelamento.descricao_parcela IS 'Descrição do plano de parcelamento';
COMMENT ON COLUMN formas_pagamento_parcelamento.percentual_entrada IS 'Percentual de entrada exigido';
COMMENT ON COLUMN formas_pagamento_parcelamento.juros_parcela IS 'Juros da parcela percentual';
COMMENT ON COLUMN formas_pagamento_parcelamento.taxa_administracao IS 'Taxa administrativa percentual';
COMMENT ON COLUMN formas_pagamento_parcelamento.dias_primeira_parcela IS 'Dias até a primeira parcela';
COMMENT ON COLUMN formas_pagamento_parcelamento.intervalo_dias_parcelas IS 'Intervalo em dias entre parcelas';
COMMENT ON COLUMN formas_pagamento_parcelamento.tipo_calculo IS 'Tipo de cálculo (Price, SAC, Alemão)';
COMMENT ON COLUMN formas_pagamento_parcelamento.capitalizacao_juros IS 'Capitalização dos juros';
COMMENT ON COLUMN formas_pagamento_parcelamento.ativa IS 'Indica se a configuração está ativa';
COMMENT ON COLUMN formas_pagamento_parcelamento.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN formas_pagamento_parcelamento.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN formas_pagamento_parcelamento.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN formas_pagamento_parcelamento.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE formas_pagamento_taxas IS 'Taxas aplicáveis às formas de pagamento';
COMMENT ON COLUMN formas_pagamento_taxas.id_forma_pagamento IS 'ID da forma de pagamento';
COMMENT ON COLUMN formas_pagamento_taxas.nome_taxa IS 'Nome da taxa';
COMMENT ON COLUMN formas_pagamento_taxas.descricao_taxa IS 'Descrição detalhada da taxa';
COMMENT ON COLUMN formas_pagamento_taxas.tipo_taxa IS 'Tipo da taxa (percentual, fixo, faixa)';
COMMENT ON COLUMN formas_pagamento_taxas.valor_taxa IS 'Valor fixo da taxa';
COMMENT ON COLUMN formas_pagamento_taxas.percentual_taxa IS 'Percentual da taxa';
COMMENT ON COLUMN formas_pagamento_taxas.valor_minimo_faixa IS 'Valor mínimo da faixa';
COMMENT ON COLUMN formas_pagamento_taxas.valor_maximo_faixa IS 'Valor máximo da faixa';
COMMENT ON COLUMN formas_pagamento_taxas.aplica_sobre IS 'Base de aplicação da taxa';
COMMENT ON COLUMN formas_pagamento_taxas.repasse_cliente IS 'Repassa taxa para o cliente';
COMMENT ON COLUMN formas_pagamento_taxas.data_vigencia_inicio IS 'Data inicial de vigência';
COMMENT ON COLUMN formas_pagamento_taxas.data_vigencia_fim IS 'Data final de vigência';
COMMENT ON COLUMN formas_pagamento_taxas.segmento_cliente IS 'Segmento do cliente';
COMMENT ON COLUMN formas_pagamento_taxas.tipo_cliente IS 'Tipo do cliente';
COMMENT ON COLUMN formas_pagamento_taxas.ativa IS 'Indica se a taxa está ativa';
COMMENT ON COLUMN formas_pagamento_taxas.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN formas_pagamento_taxas.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN formas_pagamento_taxas.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN formas_pagamento_taxas.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE formas_pagamento_historico IS 'Histórico de uso das formas de pagamento';
COMMENT ON COLUMN formas_pagamento_historico.id_forma_pagamento IS 'ID da forma de pagamento';
COMMENT ON COLUMN formas_pagamento_historico.id_transacao IS 'ID único da transação';
COMMENT ON COLUMN formas_pagamento_historico.tipo_transacao IS 'Tipo da transação';
COMMENT ON COLUMN formas_pagamento_historico.valor_transacao IS 'Valor total da transação';
COMMENT ON COLUMN formas_pagamento_historico.valor_liquido IS 'Valor líquido recebido';
COMMENT ON COLUMN formas_pagamento_historico.valor_taxa IS 'Valor da taxa aplicada';
COMMENT ON COLUMN formas_pagamento_historico.numero_parcelas IS 'Número de parcelas';
COMMENT ON COLUMN formas_pagamento_historico.valor_parcela IS 'Valor de cada parcela';
COMMENT ON COLUMN formas_pagamento_historico.parcela_atual IS 'Número da parcela atual';
COMMENT ON COLUMN formas_pagamento_historico.numero_cartao_mascarado IS 'Número do cartão mascarado';
COMMENT ON COLUMN formas_pagamento_historico.bandeira_cartao IS 'Bandeira do cartão';
COMMENT ON COLUMN formas_pagamento_historico.autorizacao_cartao IS 'Código de autorização';
COMMENT ON COLUMN formas_pagamento_historico.nsu_transacao IS 'NSU da transação';
COMMENT ON COLUMN formas_pagamento_historico.numero_cheque IS 'Número do cheque';
COMMENT ON COLUMN formas_pagamento_historico.banco_cheque IS 'Banco do cheque';
COMMENT ON COLUMN formas_pagamento_historico.agencia_cheque IS 'Agência do cheque';
COMMENT ON COLUMN formas_pagamento_historico.conta_cheque IS 'Conta do cheque';
COMMENT ON COLUMN formas_pagamento_historico.data_compensacao_cheque IS 'Data de compensação';
COMMENT ON COLUMN formas_pagamento_historico.chave_pix_usada IS 'Chave PIX utilizada';
COMMENT ON COLUMN formas_pagamento_historico.txid_pix IS 'TXID da transação PIX';
COMMENT ON COLUMN formas_pagamento_historico.data_horario_pix IS 'Data e hora do PIX';
COMMENT ON COLUMN formas_pagamento_historico.numero_boleto IS 'Número do boleto';
COMMENT ON COLUMN formas_pagamento_historico.nosso_numero_boleto IS 'Nosso número do boleto';
COMMENT ON COLUMN formas_pagamento_historico.data_vencimento_boleto IS 'Data de vencimento';
COMMENT ON COLUMN formas_pagamento_historico.data_pagamento_boleto IS 'Data de pagamento';
COMMENT ON COLUMN formas_pagamento_historico.id_venda IS 'ID da venda relacionada';
COMMENT ON COLUMN formas_pagamento_historico.id_cliente IS 'ID do cliente';
COMMENT ON COLUMN formas_pagamento_historico.id_usuario IS 'ID do usuário';
COMMENT ON COLUMN formas_pagamento_historico.id_caixa IS 'ID do caixa';
COMMENT ON COLUMN formas_pagamento_historico.id_filia IS 'ID da filial';
COMMENT ON COLUMN formas_pagamento_historico.ip_origem IS 'IP de origem';
COMMENT ON COLUMN formas_pagamento_historico.hostname_origem IS 'Hostname de origem';
COMMENT ON COLUMN formas_pagamento_historico.user_agent IS 'User agent do cliente';
COMMENT ON COLUMN formas_pagamento_historico.status_transacao IS 'Status da transação';
COMMENT ON COLUMN formas_pagamento_historico.mensagem_retorno IS 'Mensagem de retorno';
COMMENT ON COLUMN formas_pagamento_historico.codigo_erro IS 'Código de erro';
COMMENT ON COLUMN formas_pagamento_historico.tempo_processamento_ms IS 'Tempo de processamento';
COMMENT ON COLUMN formas_pagamento_historico.data_hora_transacao IS 'Data e hora da transação';
COMMENT ON COLUMN formas_pagamento_historico.data_registro IS 'Data de registro';
COMMENT ON COLUMN formas_pagamento_historico.usuario_registro IS 'Usuário que registrou';

COMMENT ON TABLE formas_pagamento_integracao IS 'Configurações de integração das formas de pagamento';
COMMENT ON COLUMN formas_pagamento_integracao.id_forma_pagamento IS 'ID da forma de pagamento';
COMMENT ON COLUMN formas_pagamento_integracao.nome_integracao IS 'Nome da integração';
COMMENT ON COLUMN formas_pagamento_integracao.provedor_integracao IS 'Provedor da integração';
COMMENT ON COLUMN formas_pagamento_integracao.endpoint_producao IS 'Endpoint de produção';
COMMENT ON COLUMN formas_pagamento_integracao.endpoint_homologacao IS 'Endpoint de homologação';
COMMENT ON COLUMN formas_pagamento_integracao.api_key IS 'Chave da API';
COMMENT ON COLUMN formas_pagamento_integracao.api_secret IS 'Segredo da API';
COMMENT ON COLUMN formas_pagamento_integracao.token_acesso IS 'Token de acesso';
COMMENT ON COLUMN formas_pagamento_integracao.tipo_autenticacao IS 'Tipo de autenticação';
COMMENT ON COLUMN formas_pagamento_integracao.client_id IS 'Client ID OAuth2';
COMMENT ON COLUMN formas_pagamento_integracao.client_secret IS 'Client Secret OAuth2';
COMMENT ON COLUMN formas_pagamento_integracao.scope_autenticacao IS 'Scope da autenticação';
COMMENT ON COLUMN formas_pagamento_integracao.webhook_url IS 'URL do webhook';
COMMENT ON COLUMN formas_pagamento_integracao.webhook_secret IS 'Segredo do webhook';
COMMENT ON COLUMN formas_pagamento_integracao.webhook_eventos IS 'Eventos do webhook';
COMMENT ON COLUMN formas_pagamento_integracao.timeout_conexao IS 'Timeout de conexão';
COMMENT ON COLUMN formas_pagamento_integracao.timeout_leitura IS 'Timeout de leitura';
COMMENT ON COLUMN formas_pagamento_integracao.maximo_retries IS 'Máximo de retentativas';
COMMENT ON COLUMN formas_pagamento_integracao.intervalo_retries IS 'Intervalo entre retentativas';
COMMENT ON COLUMN formas_pagamento_integracao.ambiente IS 'Ambiente da integração';
COMMENT ON COLUMN formas_pagamento_integracao.debug_mode IS 'Modo debug';
COMMENT ON COLUMN formas_pagamento_integracao.formato_requisicao IS 'Formato da requisição';
COMMENT ON COLUMN formas_pagamento_integracao.formato_resposta IS 'Formato da resposta';
COMMENT ON COLUMN formas_pagamento_integracao.encoding IS 'Encoding dos dados';
COMMENT ON COLUMN formas_pagamento_integracao.ssl_verificado IS 'Verificação SSL';
COMMENT ON COLUMN formas_pagamento_integracao.certificado_ssl IS 'Caminho do certificado SSL';
COMMENT ON COLUMN formas_pagamento_integracao.chave_ssl IS 'Caminho da chave SSL';
COMMENT ON COLUMN formas_pagamento_integracao.usa_proxy IS 'Usa proxy';
COMMENT ON COLUMN formas_pagamento_integracao.proxy_host IS 'Host do proxy';
COMMENT ON COLUMN formas_pagamento_integracao.proxy_porta IS 'Porta do proxy';
COMMENT ON COLUMN formas_pagamento_integracao.proxy_usuario IS 'Usuário do proxy';
COMMENT ON COLUMN formas_pagamento_integracao.proxy_senha IS 'Senha do proxy';
COMMENT ON COLUMN formas_pagamento_integracao.log_requisicoes IS 'Log de requisições';
COMMENT ON COLUMN formas_pagamento_integracao.log_respostas IS 'Log de respostas';
COMMENT ON COLUMN formas_pagamento_integracao.log_erros IS 'Log de erros';
COMMENT ON COLUMN formas_pagamento_integracao.dias_retention_logs IS 'Dias de retenção dos logs';
COMMENT ON COLUMN formas_pagamento_integracao.ativa IS 'Indica se a integração está ativa';
COMMENT ON COLUMN formas_pagamento_integracao.data_ultimo_teste IS 'Data do último teste';
COMMENT ON COLUMN formas_pagamento_integracao.resultado_ultimo_teste IS 'Resultado do último teste';
COMMENT ON COLUMN formas_pagamento_integracao.status_integracao IS 'Status da integração';
COMMENT ON COLUMN formas_pagamento_integracao.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN formas_pagamento_integracao.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN formas_pagamento_integracao.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN formas_pagamento_integracao.usuario_ultima_atualizacao IS 'Usuário da última atualização';
