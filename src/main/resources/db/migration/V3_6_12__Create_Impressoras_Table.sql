-- =====================================================
-- Hermes Comercial v3.6.12 - Migration
-- Criar tabela de Impressoras
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Impressoras
CREATE TABLE IF NOT EXISTS impressoras (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos da Impressora
    nome_impressora VARCHAR(100) NOT NULL UNIQUE,
    descricao_impressora TEXT,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    numero_serie VARCHAR(100),
    tipo_impressora VARCHAR(30) NOT NULL, -- TERMICA, MATRICIAL, LASER, JATO_DE_TINTA, NAO_FISCAL
    
    -- Configurações de Conexão
    tipo_conexao VARCHAR(20) NOT NULL, -- USB, REDE, SERIAL, PARALELA, BLUETOOTH
    ip_impressora VARCHAR(45),
    porta_impressora VARCHAR(50),
    host_impressora VARCHAR(255),
    protocolo_impressora VARCHAR(10) DEFAULT 'RAW', -- RAW, ESC_POS, ZPL, EPL, CPCL
    
    -- Configurações de Rede
    porta_rede INTEGER DEFAULT 9100,
    timeout_conexao_segundos INTEGER DEFAULT 30,
    tentativas_conexao INTEGER DEFAULT 3,
    intervalo_reconexao_segundos INTEGER DEFAULT 60,
    
    -- Configurações de Impressão
    largura_papel_mm INTEGER,
    altura_papel_mm INTEGER,
    colunas_caracteres INTEGER DEFAULT 48,
    linhas_por_pagina INTEGER DEFAULT 30,
    densidade_impressao INTEGER DEFAULT 203, -- DPI
    velocidade_impressao_mm_s INTEGER,
    
    -- Configurações de Papel
    tipo_papel VARCHAR(30) DEFAULT 'CONTINUO', -- CONTINUO, BOBINA, FOLHA, ETIQUETA
    tamanho_papel VARCHAR(20) DEFAULT '80MM', -- 58MM, 80MM, 76MM, A4, A5, LETTER
    gramatura_papel INTEGER,
    direcao_impressao VARCHAR(10) DEFAULT 'NORMAL', -- NORMAL, INVERTIDO, ROTACIONADO
    
    -- Configurações de Formatação
    fonte_padrao VARCHAR(50) DEFAULT 'COURIER',
    tamanho_fonte INTEGER DEFAULT 12,
    estilo_fonte VARCHAR(20) DEFAULT 'NORMAL', -- NORMAL, NEGRITO, ITALICO, SUBLINHADO
    alinhamento_texto VARCHAR(10) DEFAULT 'ESQUERDA', -- ESQUERDA, CENTRO, DIREITA, JUSTIFICADO
    espacamento_linhas DECIMAL(5,2) DEFAULT 1.0,
    
    -- Configurações de Código de Barras
    suporta_codigo_barras BOOLEAN DEFAULT TRUE,
    tipos_codigo_barras TEXT, -- EAN13, EAN8, CODE128, CODE39, CODE93, ITF, QR_CODE, DATA_MATRIX
    altura_codigo_barras INTEGER DEFAULT 60,
    largura_codigo_barras INTEGER DEFAULT 2,
    exibir_texto_codigo BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Imagem
    suporta_imagem BOOLEAN DEFAULT FALSE,
    formato_imagem VARCHAR(10) DEFAULT 'MONO', -- MONO, GRAYSCALE, COLOR
    resolucao_imagem INTEGER DEFAULT 203,
    compressao_imagem BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Cupom Fiscal
    impressora_nao_fiscal BOOLEAN DEFAULT FALSE,
    modelo_nao_fiscal VARCHAR(50),
    porta_nao_fiscal VARCHAR(50),
    baud_rate INTEGER DEFAULT 9600,
    data_bits INTEGER DEFAULT 8,
    stop_bits INTEGER DEFAULT 1,
    paridade VARCHAR(10) DEFAULT 'NONE', -- NONE, EVEN, ODD, MARK, SPACE
    
    -- Configurações de Etiqueta
    impressora_etiqueta BOOLEAN DEFAULT FALSE,
    modelo_etiqueta VARCHAR(50),
    largura_etiqueta_mm INTEGER,
    altura_etiqueta_mm INTEGER,
    espacamento_etiquetas_mm INTEGER,
    colunas_etiqueta INTEGER DEFAULT 2,
    linhas_etiqueta INTEGER DEFAULT 5,
    
    -- Configurações de Relatório
    impressora_relatorio BOOLEAN DEFAULT TRUE,
    formato_relatorio VARCHAR(10) DEFAULT 'A4', -- A4, A3, A5, LETTER, LEGAL
    orientacao_relatorio VARCHAR(10) DEFAULT 'RETRATO', -- RETRATO, PAISAGEM
    margem_superior DECIMAL(5,2) DEFAULT 10.00,
    margem_inferior DECIMAL(5,2) DEFAULT 10.00,
    margem_esquerda DECIMAL(5,2) DEFAULT 10.00,
    margem_direita DECIMAL(5,2) DEFAULT 10.00,
    
    -- Configurações de Comando
    comando_abertura_gaveta VARCHAR(100),
    comando_corte_papel VARCHAR(100),
    comando_inicializacao TEXT,
    comando_finalizacao TEXT,
    conjunto_comandos TEXT, -- Comandos específicos da impressora
    
    -- Configurações de Driver
    driver_impressora VARCHAR(100),
    versao_driver VARCHAR(50),
    caminho_driver VARCHAR(500),
    biblioteca_impressao VARCHAR(100), -- DLL, SO, JAR
    classe_impressao VARCHAR(200),
    
    -- Configurações de Fila
    nome_fila_impressao VARCHAR(100),
    prioridade_fila INTEGER DEFAULT 50, -- 1 (mais alta) a 100 (mais baixa)
    tamanho_maximo_fila INTEGER DEFAULT 1000,
    tempo_espera_fila_segundos INTEGER DEFAULT 30,
    
    -- Configurações de Teste
    comando_teste TEXT,
    tempo_teste_segundos INTEGER DEFAULT 10,
    resultado_ultimo_teste TEXT,
    data_ultimo_teste TIMESTAMP,
    
    -- Configurações de Manutenção
    contador_impressoes INTEGER DEFAULT 0,
    contador_paginas INTEGER DEFAULT 0,
    horas_uso INTEGER DEFAULT 0,
    data_ultima_manutencao DATE,
    intervalo_manutencao_horas INTEGER DEFAULT 2000,
    alerta_manutencao BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Status
    status_impressora VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, MANUTENCAO, ERRO, OFFLINE
    status_conexao VARCHAR(20) DEFAULT 'DESCONECTADA', -- CONECTADA, DESCONECTADA, TESTANDO
    mensagem_status TEXT,
    data_ultima_verificacao TIMESTAMP,
    
    -- Configurações de Uso
    impressora_padrao BOOLEAN DEFAULT FALSE,
    impressora_pdv BOOLEAN DEFAULT FALSE,
    impressora_cupom BOOLEAN DEFAULT FALSE,
    impressora_relatorio_padrao BOOLEAN DEFAULT FALSE,
    impressora_etiqueta_padrao BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Ambiente
    ambiente_trabalho VARCHAR(20) DEFAULT 'INTERNO', -- INTERNO, EXTERNO, PRODUCAO, HOMOLOGACAO
    temperatura_operacao_min INTEGER,
    temperatura_operacao_max INTEGER,
    umidade_operacao_min INTEGER,
    umidade_operacao_max INTEGER,
    
    -- Configurações de Segurança
    acesso_restrito BOOLEAN DEFAULT FALSE,
    usuarios_autorizados TEXT, -- Lista de usuários autorizados
    senha_acesso VARCHAR(255),
    nivel_acesso VARCHAR(20) DEFAULT 'BASICO', -- BASICO, INTERMEDIARIO, AVANCADO, ADMINISTRADOR
    
    -- Configurações de Integração
    integracao_sistema BOOLEAN DEFAULT FALSE,
    api_conexao VARCHAR(100),
    token_acesso VARCHAR(255),
    webhook_status VARCHAR(500),
    
    -- Localização Física
    localizacao VARCHAR(200),
    departamento VARCHAR(100),
    responsavel VARCHAR(100),
    contato_responsavel VARCHAR(100),
    
    -- Dados de Compra
    data_compra DATE,
    fornecedor VARCHAR(200),
    numero_nota_fiscal VARCHAR(50),
    valor_compra DECIMAL(10,2),
    data_garantia DATE,
    tipo_garantia VARCHAR(30), -- FABRICA, EXTENDIDA, NENHUMA
    numero_ordem_servico VARCHAR(50),
    
    -- Configurações de Economia
    modo_economia BOOLEAN DEFAULT FALSE,
    tempo_desligamento_automatico INTEGER, -- minutos
    nivel_energia_economia VARCHAR(20) DEFAULT 'NORMAL', -- BAIXO, NORMAL, ALTO
    consumo_energia_watts INTEGER,
    
    -- Configurações de Backup
    backup_configuracoes BOOLEAN DEFAULT TRUE,
    data_ultimo_backup TIMESTAMP,
    local_backup VARCHAR(500),
    
    -- Observações e Documentação
    observacoes TEXT,
    manual_usuario TEXT,
    manual_tecnico TEXT,
    link_documentacao VARCHAR(500),
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_impressora CHECK (tipo_impressora IN ('TERMICA', 'MATRICIAL', 'LASER', 'JATO_DE_TINTA', 'NAO_FISCAL')),
    CONSTRAINT chk_tipo_conexao CHECK (tipo_conexao IN ('USB', 'REDE', 'SERIAL', 'PARALELA', 'BLUETOOTH')),
    CONSTRAINT chk_protocolo_impressao CHECK (protocolo_impressao IN ('RAW', 'ESC_POS', 'ZPL', 'EPL', 'CPCL')),
    CONSTRAINT chk_tipo_papel CHECK (tipo_papel IN ('CONTINUO', 'BOBINA', 'FOLHA', 'ETIQUETA')),
    CONSTRAINT chk_tamanho_papel CHECK (tamanho_papel IN ('58MM', '80MM', '76MM', 'A4', 'A5', 'LETTER')),
    CONSTRAINT chk_direcao_impressao CHECK (direcao_impressao IN ('NORMAL', 'INVERTIDO', 'ROTACIONADO')),
    CONSTRAINT chk_estilo_fonte CHECK (estilo_fonte IN ('NORMAL', 'NEGRITO', 'ITALICO', 'SUBLINHADO')),
    CONSTRAINT chk_alinhamento_texto CHECK (alinhamento_texto IN ('ESQUERDA', 'CENTRO', 'DIREITA', 'JUSTIFICADO')),
    CONSTRAINT chk_formato_imagem CHECK (formato_imagem IN ('MONO', 'GRAYSCALE', 'COLOR')),
    CONSTRAINT chk_paridade CHECK (paridade IN ('NONE', 'EVEN', 'ODD', 'MARK', 'SPACE')),
    CONSTRAINT chk_formato_relatorio CHECK (formato_relatorio IN ('A4', 'A3', 'A5', 'LETTER', 'LEGAL')),
    CONSTRAINT chk_orientacao_relatorio CHECK (orientacao_relatorio IN ('RETRATO', 'PAISAGEM')),
    CONSTRAINT chk_status_impressora CHECK (status_impressora IN ('ATIVA', 'INATIVA', 'MANUTENCAO', 'ERRO', 'OFFLINE')),
    CONSTRAINT chk_status_conexao CHECK (status_conexao IN ('CONECTADA', 'DESCONECTADA', 'TESTANDO')),
    CONSTRAINT chk_ambiente_trabalho CHECK (ambiente_trabalho IN ('INTERNO', 'EXTERNO', 'PRODUCAO', 'HOMOLOGACAO')),
    CONSTRAINT chk_nivel_acesso CHECK (nivel_acesso IN ('BASICO', 'INTERMEDIARIO', 'AVANCADO', 'ADMINISTRADOR')),
    CONSTRAINT chk_tipo_garantia CHECK (tipo_garantia IN ('FABRICA', 'EXTENDIDA', 'NENHUMA')),
    CONSTRAINT chk_nivel_energia_economia CHECK (nivel_energia_economia IN ('BAIXO', 'NORMAL', 'ALTO')),
    CONSTRAINT chk_limites_impressora CHECK (
        largura_papel_mm > 0 AND 
        altura_papel_mm > 0 AND 
        colunas_caracteres > 0 AND 
        linhas_por_pagina > 0 AND
        densidade_impressao > 0 AND
        velocidade_impressao_mm_s > 0 AND
        gramatura_papel > 0 AND
        tamanho_fonte > 0 AND
        espacamento_linhas > 0 AND
        altura_codigo_barras > 0 AND
        largura_codigo_barras > 0 AND
        resolucao_imagem > 0 AND
        largura_etiqueta_mm > 0 AND
        altura_etiqueta_mm > 0 AND
        espacamento_etiquetas_mm > 0 AND
        colunas_etiqueta > 0 AND
        linhas_etiqueta > 0 AND
        margem_superior >= 0 AND
        margem_inferior >= 0 AND
        margem_esquerda >= 0 AND
        margem_direita >= 0 AND
        porta_rede > 0 AND
        porta_rede <= 65535 AND
        timeout_conexao_segundos > 0 AND
        tentativas_conexao > 0 AND
        intervalo_reconexao_segundos > 0 AND
        prioridade_fila BETWEEN 1 AND 100 AND
        tamanho_maximo_fila > 0 AND
        tempo_espera_fila_segundos > 0 AND
        tempo_teste_segundos > 0 AND
        horas_uso >= 0 AND
        intervalo_manutencao_horas > 0 AND
        temperatura_operacao_min < temperatura_operacao_max AND
        umidade_operacao_min < umidade_operacao_max AND
        tempo_desligamento_automatico > 0 AND
        consumo_energia_watts > 0 AND
        valor_compra >= 0
    )
);

-- Criar tabela de Configurações de Impressão por Documento
CREATE TABLE IF NOT EXISTS impressao_configuracoes_documento (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_impressora INTEGER NOT NULL REFERENCES impressoras(id) ON DELETE CASCADE,
    
    -- Configurações do Documento
    tipo_documento VARCHAR(50) NOT NULL, -- CUPOM_FISCAL, NOTA_FISCAL, RELATORIO, ETIQUETA, BOLETO, RECIBO
    nome_configuracao VARCHAR(100) NOT NULL,
    descricao_configuracao TEXT,
    
    -- Configurações de Layout
    layout_personalizado BOOLEAN DEFAULT FALSE,
    template_layout TEXT,
    css_layout TEXT,
    javascript_layout TEXT,
    
    -- Configurações de Conteúdo
    cabecalho_personalizado BOOLEAN DEFAULT FALSE,
    cabecalho_template TEXT,
    rodape_personalizado BOOLEAN DEFAULT FALSE,
    rodape_template TEXT,
    marca_dagua BOOLEAN DEFAULT FALSE,
    texto_marca_dagua VARCHAR(200),
    
    -- Configurações de Formatação
    fonte_personalizada BOOLEAN DEFAULT FALSE,
    fonte_nome VARCHAR(50),
    fonte_tamanho INTEGER DEFAULT 12,
    fonte_cor VARCHAR(7) DEFAULT '#000000',
    fundo_cor VARCHAR(7) DEFAULT '#FFFFFF',
    
    -- Configurações de Impressão
    copias_documento INTEGER DEFAULT 1,
    impressao_frente_verso BOOLEAN DEFAULT FALSE,
    impressao_colorida BOOLEAN DEFAULT FALSE,
    qualidade_impressao VARCHAR(20) DEFAULT 'NORMAL', -- BAIXA, NORMAL, ALTA, MUITO_ALTA
    
    -- Configurações de Papel
    papel_tipo VARCHAR(30) DEFAULT 'AUTOMATICO', -- AUTOMATICO, ESPECIFICO
    papel_tamanho VARCHAR(20) DEFAULT 'AUTOMATICO',
    papel_origem VARCHAR(20) DEFAULT 'BANDEJA_1', -- BANDEJA_1, BANDEJA_2, MANUAL
    
    -- Configurações de Código de Barras
    codigo_barras_incluir BOOLEAN DEFAULT FALSE,
    codigo_barras_tipo VARCHAR(20) DEFAULT 'CODE128',
    codigo_barras_posicao VARCHAR(20) DEFAULT 'TOPO', -- TOPO, FUNDO, ESQUERDA, DIREITA
    codigo_barras_tamanho INTEGER DEFAULT 30,
    
    -- Configurações de QR Code
    qrcode_incluir BOOLEAN DEFAULT FALSE,
    qrcode_conteudo VARCHAR(500), -- URL, TEXTO, JSON
    qrcode_tamanho INTEGER DEFAULT 50,
    qrcode_posicao VARCHAR(20) DEFAULT 'TOPO',
    qrcode_correcao VARCHAR(10) DEFAULT 'M', -- L, M, Q, H
    
    -- Configurações de Imagem
    imagem_incluir BOOLEAN DEFAULT FALSE,
    imagem_caminho VARCHAR(500),
    imagem_posicao VARCHAR(20) DEFAULT 'TOPO',
    imagem_tamanho VARCHAR(20) DEFAULT 'ORIGINAL', -- ORIGINAL, PEQUENO, MEDIO, GRANDE
    imagem_alinhamento VARCHAR(20) DEFAULT 'CENTRO', -- ESQUERDA, CENTRO, DIREITA
    
    -- Configurações de Tabela
    tabela_incluir BOOLEAN DEFAULT FALSE,
    tabela_colunas_visiveis TEXT, -- Lista de colunas separadas por vírgula
    tabela_zebrada BOOLEAN DEFAULT TRUE,
    tabela_bordas BOOLEAN DEFAULT TRUE,
    tabela_cabecalho_repetir BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Paginação
    paginacao_ativa BOOLEAN DEFAULT TRUE,
    linhas_por_pagina INTEGER DEFAULT 50,
    mostrar_numero_pagina BOOLEAN DEFAULT TRUE,
    mostrar_total_paginas BOOLEAN DEFAULT TRUE,
    
    -- Configurações de Filtro
    filtro_ativo BOOLEAN DEFAULT FALSE,
    filtro_campos TEXT, -- Campos para filtrar
    filtro_valores_padrao TEXT, -- Valores padrão dos filtros
    
    -- Configurações de Ordenação
    ordenacao_ativa BOOLEAN DEFAULT TRUE,
    ordenacao_campo_padrao VARCHAR(50),
    ordenacao_direcao VARCHAR(10) DEFAULT 'ASC', -- ASC, DESC
    
    -- Configurações de Exportação
    exportacao_ativa BOOLEAN DEFAULT TRUE,
    exportacao_formatos TEXT, -- PDF, EXCEL, CSV, HTML
    exportacao_caminho_padrao VARCHAR(500),
    
    -- Configurações de Envio
    envio_email BOOLEAN DEFAULT FALSE,
    email_destinatarios TEXT,
    email_assunto_padrao VARCHAR(200),
    email_mensagem_padrao TEXT,
    
    -- Configurações de Agendamento
    agendamento_ativo BOOLEAN DEFAULT FALSE,
    agendamento_frequencia VARCHAR(20), -- IMEDIATO, DIARIO, SEMANAL, MENSAL
    agendamento_horario TIME DEFAULT '08:00:00',
    agendamento_dias_semana TEXT, -- 1,2,3,4,5 para segunda a sexta
    
    -- Configurações de Prioridade
    prioridade_impressao INTEGER DEFAULT 50, -- 1 (mais alta) a 100 (mais baixa)
    prioridade_fila INTEGER DEFAULT 50,
    
    -- Configurações de Retentativas
    retentativas_maximas INTEGER DEFAULT 3,
    intervalo_retentativas_segundos INTEGER DEFAULT 60,
    notificar_falha BOOLEAN DEFAULT TRUE,
    
    -- Status e Controle
    status_configuracao VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, TESTE, ERRO
    data_ultima_impressao TIMESTAMP,
    total_impresso INTEGER DEFAULT 0,
    
    -- Auditoria
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_cadastro VARCHAR(50) DEFAULT CURRENT_USER,
    usuario_ultima_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_documento CHECK (tipo_documento IN ('CUPOM_FISCAL', 'NOTA_FISCAL', 'RELATORIO', 'ETIQUETA', 'BOLETO', 'RECIBO')),
    CONSTRAINT chk_qualidade_impressao CHECK (qualidade_impressao IN ('BAIXA', 'NORMAL', 'ALTA', 'MUITO_ALTA')),
    CONSTRAINT chk_papel_tipo CHECK (papel_tipo IN ('AUTOMATICO', 'ESPECIFICO')),
    CONSTRAINT chk_papel_tamanho CHECK (papel_tamanho IN ('AUTOMATICO', 'A4', 'A3', 'A5', 'LETTER', 'LEGAL')),
    CONSTRAINT chk_papel_origem CHECK (papel_origem IN ('BANDEJA_1', 'BANDEJA_2', 'MANUAL')),
    CONSTRAINT chk_codigo_barras_tipo CHECK (codigo_barras_tipo IN ('EAN13', 'EAN8', 'CODE128', 'CODE39', 'CODE93', 'ITF', 'QR_CODE', 'DATA_MATRIX')),
    CONSTRAINT chk_codigo_barras_posicao CHECK (codigo_barras_posicao IN ('TOPO', 'FUNDO', 'ESQUERDA', 'DIREITA')),
    CONSTRAINT chk_qrcode_posicao CHECK (qrcode_posicao IN ('TOPO', 'FUNDO', 'ESQUERDA', 'DIREITA')),
    CONSTRAINT chk_correcao_qrcode CHECK (qrcode_correcao IN ('L', 'M', 'Q', 'H')),
    CONSTRAINT chk_imagem_posicao CHECK (imagem_posicao IN ('TOPO', 'FUNDO', 'ESQUERDA', 'DIREITA')),
    CONSTRAINT chk_imagem_tamanho CHECK (imagem_tamanho IN ('ORIGINAL', 'PEQUENO', 'MEDIO', 'GRANDE')),
    CONSTRAINT chk_imagem_alinhamento CHECK (imagem_alinhamento IN ('ESQUERDA', 'CENTRO', 'DIREITA')),
    CONSTRAINT chk_ordenacao_direcao CHECK (ordenacao_direcao IN ('ASC', 'DESC')),
    CONSTRAINT chk_agendamento_frequencia CHECK (agendamento_frequencia IN ('IMEDIATO', 'DIARIO', 'SEMANAL', 'MENSAL')),
    CONSTRAINT chk_status_configuracao CHECK (status_configuracao IN ('ATIVA', 'INATIVA', 'TESTE', 'ERRO')),
    CONSTRAINT chk_limites_config_documento CHECK (
        copias_documento > 0 AND
        fonte_tamanho > 0 AND
        codigo_barras_tamanho > 0 AND
        qrcode_tamanho > 0 AND
        linhas_por_pagina > 0 AND
        prioridade_impressao BETWEEN 1 AND 100 AND
        prioridade_fila BETWEEN 1 AND 100 AND
        retentativas_maximas > 0 AND
        intervalo_retentativas_segundos > 0 AND
        total_impresso >= 0
    ),
    
    -- Unique constraint para evitar configurações duplicadas
    UNIQUE (id_impressora, tipo_documento, nome_configuracao)
);

-- Criar tabela de Histórico de Impressão
CREATE TABLE IF NOT EXISTS impressao_historico (
    id SERIAL PRIMARY KEY,
    
    -- Dados da Impressão
    id_impressora INTEGER NOT NULL REFERENCES impressoras(id),
    id_configuracao_documento INTEGER REFERENCES impressao_configuracoes_documento(id),
    
    -- Informações do Documento
    tipo_documento VARCHAR(50),
    nome_documento VARCHAR(200),
    numero_documento VARCHAR(50),
    quantidade_paginas INTEGER,
    quantidade_copias INTEGER DEFAULT 1,
    
    -- Dados do Conteúdo
    tamanho_documento_kb INTEGER,
    formato_documento VARCHAR(10), -- PDF, TXT, HTML, XML
    resumo_conteudo TEXT,
    
    -- Status da Impressão
    status_impressao VARCHAR(20) NOT NULL, -- ENVIADO, IMPRIMINDO, CONCLUIDO, FALHOU, CANCELADO
    mensagem_status TEXT,
    codigo_erro VARCHAR(50),
    
    -- Tempo de Processamento
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_inicio_impressao TIMESTAMP,
    data_fim_impressao TIMESTAMP,
    tempo_total_segundos INTEGER,
    
    -- Recursos Utilizados
    papel_utilizado VARCHAR(50),
    tinta_utilizada VARCHAR(50),
    energia_consumida_watts_hora DECIMAL(10,4),
    
    -- Contexto da Solicitação
    usuario_solicitante VARCHAR(50),
    modulo_origem VARCHAR(100),
    funcionalidade_origem VARCHAR(100),
    ip_origem VARCHAR(45),
    hostname_origem VARCHAR(100),
    
    -- Detalhes da Impressão
    configuracoes_usadas TEXT, -- JSON com configurações utilizadas
    parametros_especiais TEXT, -- Parâmetros especiais da impressão
    
    -- Resultados
    qualidade_impressao VARCHAR(20), -- EXCELENTE, BOA, REGULAR, RUIM
    avaliacao_usuario INTEGER, -- 1 a 5
    comentario_avaliacao TEXT,
    
    -- Informações de Retentativas
    tentativas_realizadas INTEGER DEFAULT 1,
    motivos_falha TEXT,
    
    -- Auditoria
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_status_impressao_historico CHECK (status_impressao IN ('ENVIADO', 'IMPRIMINDO', 'CONCLUIDO', 'FALHOU', 'CANCELADO')),
    CONSTRAINT chk_qualidade_impressao CHECK (qualidade_impressao IN ('EXCELENTE', 'BOA', 'REGULAR', 'RUIM')),
    CONSTRAINT chk_avaliacao_usuario CHECK (avaliacao_usuario BETWEEN 1 AND 5),
    CONSTRAINT chk_limites_historico_impressao CHECK (
        quantidade_paginas > 0 AND
        quantidade_copias > 0 AND
        tamanho_documento_kb >= 0 AND
        tempo_total_segundos >= 0 AND
        tentativas_realizadas > 0 AND
        energia_consumida_watts_hora >= 0
    )
);

-- Criar tabela de Status de Impressoras em Tempo Real
CREATE TABLE IF NOT EXISTS impressora_status_tempo_real (
    id SERIAL PRIMARY KEY,
    
    -- Relacionamento
    id_impressora INTEGER NOT NULL REFERENCES impressoras(id) ON DELETE CASCADE,
    
    -- Status Atual
    status_atual VARCHAR(20) NOT NULL,
    mensagem_status TEXT,
    nivel_erro INTEGER DEFAULT 0, -- 0 (sem erro) a 5 (erro crítico)
    
    -- Conectividade
    status_conexao VARCHAR(20),
    qualidade_sinal INTEGER, -- 1 a 100
    latencia_ms INTEGER,
    velocidade_conexao_kbps INTEGER,
    
    -- Recursos
    nivel_papel INTEGER DEFAULT 100, -- Percentual de papel
    nivel_tinta_ciano INTEGER DEFAULT 100, -- Percentual de tinta ciano
    nivel_tinta_magenta INTEGER DEFAULT 100, -- Percentual de tinta magenta
    nivel_tinta_amarelo INTEGER DEFAULT 100, -- Percentual de tinta amarelo
    nivel_tinta_preto INTEGER DEFAULT 100, -- Percentual de tinta preto
    nivel_toner INTEGER DEFAULT 100, -- Percentual de toner
    
    -- Hardware
    temperatura_atual INTEGER,
    umidade_atual INTEGER,
    horas_funcionamento INTEGER,
    contador_total_impressoes INTEGER,
    contador_ultima_manutencao INTEGER,
    
    -- Performance
    paginas_por_minuto INTEGER,
    tempo_medio_impressao_seg INTEGER,
    taxa_erro_percentual DECIMAL(5,2),
    tempo_inatividade_minutos INTEGER,
    
    -- Alertas
    alerta_papel_baixo BOOLEAN DEFAULT FALSE,
    alerta_tinta_baixa BOOLEAN DEFAULT FALSE,
    alerta_manutencao BOOLEAN DEFAULT FALSE,
    alerta_conexao_perdida BOOLEAN DEFAULT FALSE,
    alerta_erro_critico BOOLEAN DEFAULT FALSE,
    
    -- Localização
    localizacao_atual VARCHAR(200),
    ultima_movimentacao TIMESTAMP,
    
    -- Controle
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_atual CHECK (status_atual IN ('ONLINE', 'OFFLINE', 'IMPRIMINDO', 'ERRO', 'MANUTENCAO', 'OCIOSA', 'PAUSADA')),
    CONSTRAINT chk_status_conexao_real CHECK (status_conexao IN ('CONECTADA', 'DESCONECTADA', 'INSTAVEL', 'LENTA')),
    CONSTRAINT chk_nivel_erro CHECK (nivel_erro BETWEEN 0 AND 5),
    CONSTRAINT chk_qualidade_sinal CHECK (qualidade_sinal BETWEEN 1 AND 100),
    CONSTRAINT chk_nivel_recursos CHECK (
        nivel_papel BETWEEN 0 AND 100 AND
        nivel_tinta_ciano BETWEEN 0 AND 100 AND
        nivel_tinta_magenta BETWEEN 0 AND 100 AND
        nivel_tinta_amarelo BETWEEN 0 AND 100 AND
        nivel_tinta_preto BETWEEN 0 AND 100 AND
        nivel_toner BETWEEN 0 AND 100
    ),
    CONSTRAINT chk_limites_status_real CHECK (
        temperatura_atual IS NULL OR (temperatura_atual > -50 AND temperatura_atual < 100) AND
        umidade_atual IS NULL OR (umidade_atual >= 0 AND umidade_atual <= 100) AND
        horas_funcionamento >= 0 AND
        contador_total_impressoes >= 0 AND
        contador_ultima_manutencao >= 0 AND
        paginas_por_minuto >= 0 AND
        tempo_medio_impressao_seg >= 0 AND
        taxa_erro_percentual >= 0 AND
        tempo_inatividade_minutos >= 0
    )
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_impressoras_nome_impressora ON impressoras(nome_impressora);
CREATE INDEX IF NOT EXISTS idx_impressoras_tipo_impressora ON impressoras(tipo_impressora);
CREATE INDEX IF NOT EXISTS idx_impressoras_tipo_conexao ON impressoras(tipo_conexao);
CREATE INDEX IF NOT EXISTS idx_impressoras_ip_impressora ON impressoras(ip_impressora);
CREATE INDEX IF NOT EXISTS idx_impressoras_status_impressora ON impressoras(status_impressora);
CREATE INDEX IF NOT EXISTS idx_impressoras_status_conexao ON impressoras(status_conexao);
CREATE INDEX IF NOT EXISTS idx_impressoras_impressora_padrao ON impressoras(impressora_padrao);
CREATE INDEX IF NOT EXISTS idx_impressoras_impressora_pdv ON impressoras(impressora_pdv);
CREATE INDEX IF NOT EXISTS idx_impressoras_impressora_cupom ON impressoras(impressora_cupom);
CREATE INDEX IF NOT EXISTS idx_impressoras_impressora_relatorio_padrao ON impressoras(impressora_relatorio_padrao);
CREATE INDEX IF NOT EXISTS idx_impressoras_impressora_etiqueta_padrao ON impressoras(impressora_etiqueta_padrao);

CREATE INDEX IF NOT EXISTS idx_impressao_config_documento_id_impressora ON impressao_configuracoes_documento(id_impressora);
CREATE INDEX IF NOT EXISTS idx_impressao_config_documento_tipo_documento ON impressao_configuracoes_documento(tipo_documento);
CREATE INDEX IF NOT EXISTS idx_impressao_config_documento_status_configuracao ON impressao_configuracoes_documento(status_configuracao);
CREATE INDEX IF NOT EXISTS idx_impressao_config_documento_prioridade_impressao ON impressao_configuracoes_documento(prioridade_impressao);

CREATE INDEX IF NOT EXISTS idx_impressao_historico_id_impressora ON impressao_historico(id_impressora);
CREATE INDEX IF NOT EXISTS idx_impressao_historico_data_envio ON impressao_historico(data_envio);
CREATE INDEX IF NOT EXISTS idx_impressao_historico_status_impressao ON impressao_historico(status_impressao);
CREATE INDEX IF NOT EXISTS idx_impressao_historico_usuario_solicitante ON impressao_historico(usuario_solicitante);
CREATE INDEX IF NOT EXISTS idx_impressao_historico_tipo_documento ON impressao_historico(tipo_documento);

CREATE INDEX IF NOT EXISTS idx_impressora_status_real_id_impressora ON impressora_status_tempo_real(id_impressora);
CREATE INDEX IF NOT EXISTS idx_impressora_status_real_status_atual ON impressora_status_tempo_real(status_atual);
CREATE INDEX IF NOT EXISTS idx_impressora_status_real_status_conexao ON impressora_status_tempo_real(status_conexao);
CREATE INDEX IF NOT EXISTS idx_impressora_status_real_data_atualizacao ON impressora_status_tempo_real(data_atualizacao);
CREATE INDEX IF NOT EXISTS idx_impressora_status_real_alertas ON impressora_status_tempo_real(alerta_papel_baixo, alerta_tinta_baixa, alerta_manutencao, alerta_conexao_perdida, alerta_erro_critico);

-- Inserir impressoras padrão
INSERT INTO impressoras (
    nome_impressora, descricao_impressora, marca, modelo, tipo_impressora,
    tipo_conexao, ip_impressora, porta_impressora, protocolo_impressao,
    porta_rede, largura_papel_mm, altura_papel_mm, colunas_caracteres,
    linhas_por_pagina, densidade_impressao, tipo_papel, tamanho_papel,
    fonte_padrao, tamanho_fonte, suporta_codigo_barras, tipos_codigo_barras,
    altura_codigo_barras, largura_codigo_barras, exibir_texto_codigo,
    impressora_nao_fiscal, modelo_nao_fiscal, porta_nao_fiscal,
    baud_rate, impressora_padrao, impressora_pdv, impressora_cupom,
    status_impressora, localizacao, departamento, responsavel
) VALUES 
(
    'Impressora Térmica PDV', 'Impressora térmica para cupons fiscais e não fiscais', 
    'Bematech', 'MP-4200 TH', 'TERMICA', 'USB', NULL, 'USB001', 'ESC_POS',
    NULL, 80, 297, 48, 30, 203, 'CONTINUO', '80MM',
    'COURIER', 12, TRUE, 'EAN13,EAN8,CODE128,CODE39,QR_CODE',
    60, 2, TRUE, TRUE, 'Daruma', 'DR700', 9600,
    TRUE, TRUE, TRUE, 'ATIVA', 'Caixa 1', 'Vendas', 'João Silva'
),
(
    'Impressora Matricial Relatórios', 'Impressora matricial para relatórios e boletos',
    'Epson', 'LX-300', 'MATRICIAL', 'PARALELA', NULL, 'LPT001', 'ESC_POS',
    NULL, 210, 297, 80, 60, 360, 'FOLHA', 'A4',
    'COURIER', 10, FALSE, NULL, NULL, NULL, FALSE,
    FALSE, FALSE, FALSE, FALSE, NULL, NULL, NULL,
    FALSE, FALSE, FALSE, FALSE, 'ATIVA', 'Escritório', 'Financeiro', 'Maria Santos'
),
(
    'Impressora de Etiquetas', 'Impressora de etiquetas para produtos e estoque',
    'Zebra', 'ZT410', 'TERMICA', 'REDE', '192.168.1.100', '9100', 'ZPL',
    9100, 100, 50, 32, 10, 203, 'ETIQUETA', '100X50',
    'COURIER', 8, TRUE, 'CODE128,CODE39,QR_CODE',
    30, 1, TRUE, FALSE, FALSE, NULL, NULL, NULL,
    FALSE, FALSE, FALSE, TRUE, 'ATIVA', 'Almoxarifado', 'Estoque', 'Carlos Oliveira'
),
(
    'Impressora Laser A4', 'Impressora laser para documentos e relatórios',
    'HP', 'LaserJet Pro M404n', 'LASER', 'REDE', '192.168.1.101', '9100', 'RAW',
    9100, 210, 297, 80, 60, 600, 'FOLHA', 'A4',
    'ARIAL', 11, FALSE, NULL, NULL, NULL, FALSE,
    FALSE, FALSE, FALSE, FALSE, NULL, NULL, NULL,
    FALSE, FALSE, FALSE, FALSE, 'ATIVA', 'Escritório', 'Administração', 'Ana Costa'
),
(
    'Impressora Térmica Portátil', 'Impressora térmica portátil para delivery',
    'Datecs', 'DPP-250', 'TERMICA', 'BLUETOOTH', NULL, 'BT001', 'ESC_POS',
    NULL, 58, 210, 32, 20, 203, 'CONTINUO', '58MM',
    'COURIER', 9, TRUE, 'CODE128,QR_CODE',
    40, 1, TRUE, TRUE, 'Daruma', 'DR700', 9600,
    FALSE, FALSE, TRUE, FALSE, 'ATIVA', 'Delivery', 'Vendas', 'Pedro Souza'
) ON CONFLICT DO NOTHING;

-- Inserir configurações de documento padrão
INSERT INTO impressao_configuracoes_documento (
    id_impressora, tipo_documento, nome_configuracao, descricao_configuracao,
    layout_personalizado, cabecalho_personalizado, rodape_personalizado,
    fonte_personalizada, fonte_nome, fonte_tamanho, copias_documento,
    impressao_frente_verso, qualidade_impressao, papel_tipo, papel_origem,
    codigo_barras_incluir, codigo_barras_tipo, codigo_barras_posicao,
    tabela_incluir, tabela_zebrada, paginacao_ativa, linhas_por_pagina,
    mostrar_numero_pagina, exportacao_ativa, exportacao_formatos,
    prioridade_impressao, status_configuracao
) 
SELECT 
    i.id, 'CUPOM_FISCAL', 'Cupom Fiscal Padrão', 'Configuração padrão para cupom fiscal',
    FALSE, TRUE, TRUE, FALSE, NULL, 10, 1, FALSE, 'NORMAL',
    'AUTOMATICO', 'BANDEJA_1', TRUE, 'CODE128', 'TOPO', TRUE, TRUE, 
    TRUE, 30, TRUE, TRUE, 'PDF,HTML', 50, 'ATIVA'
FROM impressoras i 
WHERE i.nome_impressora = 'Impressora Térmica PDV'
ON CONFLICT DO NOTHING;

INSERT INTO impressao_configuracoes_documento (
    id_impressora, tipo_documento, nome_configuracao, descricao_configuracao,
    layout_personalizado, cabecalho_personalizado, rodape_personalizado,
    fonte_personalizada, fonte_nome, fonte_tamanho, copias_documento,
    impressao_frente_verso, qualidade_impressao, papel_tipo, papel_origem,
    tabela_incluir, tabela_zebrada, paginacao_ativa, linhas_por_pagina,
    mostrar_numero_pagina, exportacao_ativa, exportacao_formatos,
    prioridade_impressao, status_configuracao
) 
SELECT 
    i.id, 'RELATORIO', 'Relatório Padrão', 'Configuração padrão para relatórios',
    FALSE, TRUE, TRUE, FALSE, NULL, 9, 1, FALSE, 'ALTA',
    'ESPECIFICO', 'A4', TRUE, TRUE, TRUE, 40, TRUE, TRUE,
    'PDF,EXCEL,CSV', 30, 'ATIVA'
FROM impressoras i 
WHERE i.nome_impressora = 'Impressora Matricial Relatórios'
ON CONFLICT DO NOTHING;

INSERT INTO impressao_configuracoes_documento (
    id_impressora, tipo_documento, nome_configuracao, descricao_configuracao,
    layout_personalizado, cabecalho_personalizado, rodape_personalizado,
    fonte_personalizada, fonte_nome, fonte_tamanho, copias_documento,
    impressao_frente_verso, qualidade_impressao, papel_tipo, papel_origem,
    codigo_barras_incluir, codigo_barras_tipo, codigo_barras_posicao,
    tabela_incluir, tabela_zebrada, paginacao_ativa, linhas_por_pagina,
    mostrar_numero_pagina, exportacao_ativa, exportacao_formatos,
    prioridade_impressao, status_configuracao
) 
SELECT 
    i.id, 'ETIQUETA', 'Etiqueta Produto Padrão', 'Configuração padrão para etiquetas de produto',
    FALSE, FALSE, FALSE, FALSE, NULL, 8, 1, FALSE, 'NORMAL',
    'ESPECIFICO', '100X50', FALSE, FALSE, FALSE, 10, FALSE, FALSE,
    'PDF', 40, 'ATIVA'
FROM impressoras i 
WHERE i.nome_impressora = 'Impressora de Etiquetas'
ON CONFLICT DO NOTHING;

-- Inserir status inicial em tempo real
INSERT INTO impressora_status_tempo_real (
    id_impressora, status_atual, mensagem_status, nivel_erro,
    status_conexao, qualidade_sinal, nivel_papel, nivel_tinta_preto,
    temperatura_atual, umidade_atual, horas_funcionamento,
    contador_total_impressoes, paginas_por_minuto, tempo_medio_impressao_seg
) 
SELECT 
    i.id, 'ONLINE', 'Impressora conectada e pronta', 0, 'CONECTADA',
    100, 100, 100, 25, 45, 0, 0, 10, 5
FROM impressoras i 
WHERE i.nome_impressora = 'Impressora Térmica PDV'
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE impressoras IS 'Tabela de impressoras do sistema';
COMMENT ON COLUMN impressoras.nome_impressora IS 'Nome único da impressora';
COMMENT ON COLUMN impressoras.descricao_impressora IS 'Descrição detalhada da impressora';
COMMENT ON COLUMN impressoras.marca IS 'Fabricante da impressora';
COMMENT ON COLUMN impressoras.modelo IS 'Modelo da impressora';
COMMENT ON COLUMN impressoras.numero_serie IS 'Número de série da impressora';
COMMENT ON COLUMN impressoras.tipo_impressora IS 'Tipo da impressora (térmica, matricial, laser, etc)';
COMMENT ON COLUMN impressoras.tipo_conexao IS 'Tipo de conexão (USB, rede, serial, etc)';
COMMENT ON COLUMN impressoras.ip_impressora IS 'Endereço IP da impressora';
COMMENT ON COLUMN impressoras.porta_impressora IS 'Porta de conexão da impressora';
COMMENT ON COLUMN impressoras.host_impressora IS 'Host da impressora para conexão';
COMMENT ON COLUMN impressoras.protocolo_impressao IS 'Protocolo de comunicação da impressora';
COMMENT ON COLUMN impressoras.porta_rede IS 'Porta de rede da impressora';
COMMENT ON COLUMN impressoras.timeout_conexao_segundos IS 'Timeout de conexão em segundos';
COMMENT ON COLUMN impressoras.tentativas_conexao IS 'Número de tentativas de conexão';
COMMENT ON COLUMN impressoras.intervalo_reconexao_segundos IS 'Intervalo entre tentativas de reconexão';
COMMENT ON COLUMN impressoras.largura_papel_mm IS 'Largura do papel em milímetros';
COMMENT ON COLUMN impressoras.altura_papel_mm IS 'Altura do papel em milímetros';
COMMENT ON COLUMN impressoras.colunas_caracteres IS 'Número de colunas de caracteres';
COMMENT ON COLUMN impressoras.linhas_por_pagina IS 'Número de linhas por página';
COMMENT ON COLUMN impressoras.densidade_impressao IS 'Densidade de impressão em DPI';
COMMENT ON COLUMN impressoras.velocidade_impressao_mm_s IS 'Velocidade de impressão em mm/s';
COMMENT ON COLUMN impressoras.tipo_papel IS 'Tipo de papel (contínuo, bobina, folha, etiqueta)';
COMMENT ON COLUMN impressoras.tamanho_papel IS 'Tamanho do papel';
COMMENT ON COLUMN impressoras.gramatura_papel IS 'Gramatura do papel em g/m²';
COMMENT ON COLUMN impressoras.direcao_impressao IS 'Direção da impressão';
COMMENT ON COLUMN impressoras.fonte_padrao IS 'Fonte padrão da impressora';
COMMENT ON COLUMN impressoras.tamanho_fonte IS 'Tamanho da fonte padrão';
COMMENT ON COLUMN impressoras.estilo_fonte IS 'Estilo da fonte';
COMMENT ON COLUMN impressoras.alinhamento_texto IS 'Alinhamento do texto';
COMMENT ON COLUMN impressoras.espacamento_linhas IS 'Espaçamento entre linhas';
COMMENT ON COLUMN impressoras.suporta_codigo_barras IS 'Suporta impressão de código de barras';
COMMENT ON COLUMN impressoras.tipos_codigo_barras IS 'Tipos de código de barras suportados';
COMMENT ON COLUMN impressoras.altura_codigo_barras IS 'Altura do código de barras';
COMMENT ON COLUMN impressoras.largura_codigo_barras IS 'Largura do código de barras';
COMMENT ON COLUMN impressoras.exibir_texto_codigo IS 'Exibir texto abaixo do código de barras';
COMMENT ON COLUMN impressoras.impressora_nao_fiscal IS 'É impressora não fiscal';
COMMENT ON COLUMN impressoras.modelo_nao_fiscal IS 'Modelo da impressora não fiscal';
COMMENT ON COLUMN impressoras.porta_nao_fiscal IS 'Porta da impressora não fiscal';
COMMENT ON COLUMN impressoras.baud_rate IS 'Baud rate da impressora não fiscal';
COMMENT ON COLUMN impressoras.data_bits IS 'Bits de dados da conexão serial';
COMMENT ON COLUMN impressoras.stop_bits IS 'Bits de parada da conexão serial';
COMMENT ON COLUMN impressoras.paridade IS 'Paridade da conexão serial';
COMMENT ON COLUMN impressoras.impressora_etiqueta IS 'É impressora de etiquetas';
COMMENT ON COLUMN impressoras.modelo_etiqueta IS 'Modelo da impressora de etiquetas';
COMMENT ON COLUMN impressoras.largura_etiqueta_mm IS 'Largura da etiqueta em mm';
COMMENT ON COLUMN impressoras.altura_etiqueta_mm IS 'Altura da etiqueta em mm';
COMMENT ON COLUMN impressoras.espacamento_etiquetas_mm IS 'Espaçamento entre etiquetas em mm';
COMMENT ON COLUMN impressoras.colunas_etiqueta IS 'Colunas por etiqueta';
COMMENT ON COLUMN impressoras.linhas_etiqueta IS 'Linhas por etiqueta';
COMMENT ON COLUMN impressoras.impressora_relatorio IS 'É impressora de relatórios';
COMMENT ON COLUMN impressoras.formato_relatorio IS 'Formato de papel para relatórios';
COMMENT ON COLUMN impressoras.orientacao_relatorio IS 'Orientação do papel para relatórios';
COMMENT ON COLUMN impressoras.margem_superior IS 'Margem superior em mm';
COMMENT ON COLUMN impressoras.margem_inferior IS 'Margem inferior em mm';
COMMENT ON COLUMN impressoras.margem_esquerda IS 'Margem esquerda em mm';
COMMENT ON COLUMN impressoras.margem_direita IS 'Margem direita em mm';
COMMENT ON COLUMN impressoras.comando_abertura_gaveta IS 'Comando para abrir gaveta';
COMMENT ON COLUMN impressoras.comando_corte_papel IS 'Comando para cortar papel';
COMMENT ON COLUMN impressoras.comando_inicializacao IS 'Comando de inicialização';
COMMENT ON COLUMN impressoras.comando_finalizacao IS 'Comando de finalização';
COMMENT ON COLUMN impressoras.conjunto_comandos IS 'Conjunto de comandos específicos';
COMMENT ON COLUMN impressoras.driver_impressora IS 'Driver da impressora';
COMMENT ON COLUMN impressoras.versao_driver IS 'Versão do driver';
COMMENT ON COLUMN impressoras.caminho_driver IS 'Caminho do driver';
COMMENT ON COLUMN impressoras.biblioteca_impressao IS 'Biblioteca de impressão';
COMMENT ON COLUMN impressoras.classe_impressao IS 'Classe de impressão';
COMMENT ON COLUMN impressoras.nome_fila_impressao IS 'Nome da fila de impressão';
COMMENT ON COLUMN impressoras.prioridade_fila IS 'Prioridade na fila (1-100)';
COMMENT ON COLUMN impressoras.tamanho_maximo_fila IS 'Tamanho máximo da fila';
COMMENT ON COLUMN impressoras.tempo_espera_fila_segundos IS 'Tempo de espera na fila';
COMMENT ON COLUMN impressoras.comando_teste IS 'Comando para teste de impressão';
COMMENT ON COLUMN impressoras.tempo_teste_segundos IS 'Tempo de teste em segundos';
COMMENT ON COLUMN impressoras.resultado_ultimo_teste IS 'Resultado do último teste';
COMMENT ON COLUMN impressoras.data_ultimo_teste IS 'Data do último teste';
COMMENT ON COLUMN impressoras.contador_impressoes IS 'Contador total de impressões';
COMMENT ON COLUMN impressoras.contador_paginas IS 'Contador total de páginas';
COMMENT ON COLUMN impressoras.horas_uso IS 'Horas de uso da impressora';
COMMENT ON COLUMN impressoras.data_ultima_manutencao IS 'Data da última manutenção';
COMMENT ON COLUMN impressoras.intervalo_manutencao_horas IS 'Intervalo de manutenção em horas';
COMMENT ON COLUMN impressoras.alerta_manutencao IS 'Alertar para manutenção';
COMMENT ON COLUMN impressoras.status_impressora IS 'Status atual da impressora';
COMMENT ON COLUMN impressoras.status_conexao IS 'Status da conexão';
COMMENT ON COLUMN impressoras.mensagem_status IS 'Mensagem de status detalhada';
COMMENT ON COLUMN impressoras.data_ultima_verificacao IS 'Data da última verificação';
COMMENT ON COLUMN impressoras.impressora_padrao IS 'É impressora padrão do sistema';
COMMENT ON COLUMN impressoras.impressora_pdv IS 'É impressora do PDV';
COMMENT ON COLUMN impressoras.impressora_cupom IS 'É impressora de cupons';
COMMENT ON COLUMN impressoras.impressora_relatorio_padrao IS 'É impressora padrão de relatórios';
COMMENT ON COLUMN impressoras.impressora_etiqueta_padrao IS 'É impressora padrão de etiquetas';
COMMENT ON COLUMN impressoras.ambiente_trabalho IS 'Ambiente de trabalho';
COMMENT ON COLUMN impressoras.temperatura_operacao_min IS 'Temperatura mínima de operação';
COMMENT ON COLUMN impressoras.temperatura_operacao_max IS 'Temperatura máxima de operação';
COMMENT ON COLUMN impressoras.umidade_operacao_min IS 'Umidade mínima de operação';
COMMENT ON COLUMN impressoras.umidade_operacao_max IS 'Umidade máxima de operação';
COMMENT ON COLUMN impressoras.acesso_restrito IS 'Acesso restrito à impressora';
COMMENT ON COLUMN impressoras.usuarios_autorizados IS 'Lista de usuários autorizados';
COMMENT ON COLUMN impressoras.senha_acesso IS 'Senha de acesso à impressora';
COMMENT ON COLUMN impressoras.nivel_acesso IS 'Nível de acesso necessário';
COMMENT ON COLUMN impressoras.integracao_sistema IS 'Integração com sistemas externos';
COMMENT ON COLUMN impressoras.api_conexao IS 'API de conexão';
COMMENT ON COLUMN impressoras.token_acesso IS 'Token de acesso';
COMMENT ON COLUMN impressoras.webhook_status IS 'Webhook para status';
COMMENT ON COLUMN impressoras.localizacao IS 'Localização física da impressora';
COMMENT ON COLUMN impressoras.departamento IS 'Departamento responsável';
COMMENT ON COLUMN impressoras.responsavel IS 'Responsável pela impressora';
COMMENT ON COLUMN impressoras.contato_responsavel IS 'Contato do responsável';
COMMENT ON COLUMN impressoras.data_compra IS 'Data de compra da impressora';
COMMENT ON COLUMN impressoras.fornecedor IS 'Fornecedor da impressora';
COMMENT ON COLUMN impressoras.numero_nota_fiscal IS 'Número da nota fiscal';
COMMENT ON COLUMN impressoras.valor_compra IS 'Valor de compra';
COMMENT ON COLUMN impressoras.data_garantia IS 'Data de término da garantia';
COMMENT ON COLUMN impressoras.tipo_garantia IS 'Tipo de garantia';
COMMENT ON COLUMN impressoras.numero_ordem_servico IS 'Número da ordem de serviço';
COMMENT ON COLUMN impressoras.modo_economia IS 'Modo economia ativado';
COMMENT ON COLUMN impressoras.tempo_desligamento_automatico IS 'Tempo para desligamento automático';
COMMENT ON COLUMN impressoras.nivel_energia_economia IS 'Nível de energia em modo economia';
COMMENT ON COLUMN impressoras.consumo_energia_watts IS 'Consumo de energia em watts';
COMMENT ON COLUMN impressoras.backup_configuracoes IS 'Backup das configurações';
COMMENT ON COLUMN impressoras.data_ultimo_backup IS 'Data do último backup';
COMMENT ON COLUMN impressoras.local_backup IS 'Local do backup';
COMMENT ON COLUMN impressoras.observacoes IS 'Observações gerais';
COMMENT ON COLUMN impressoras.manual_usuario IS 'Manual do usuário';
COMMENT ON COLUMN impressoras.manual_tecnico IS 'Manual técnico';
COMMENT ON COLUMN impressoras.link_documentacao IS 'Link para documentação';
COMMENT ON COLUMN impressoras.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN impressoras.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN impressoras.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN impressoras.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE impressao_configuracoes_documento IS 'Configurações de impressão por tipo de documento';
COMMENT ON COLUMN impressao_configuracoes_documento.id_impressora IS 'ID da impressora';
COMMENT ON COLUMN impressao_configuracoes_documento.tipo_documento IS 'Tipo de documento';
COMMENT ON COLUMN impressao_configuracoes_documento.nome_configuracao IS 'Nome da configuração';
COMMENT ON COLUMN impressao_configuracoes_documento.descricao_configuracao IS 'Descrição da configuração';
COMMENT ON COLUMN impressao_configuracoes_documento.layout_personalizado IS 'Layout personalizado';
COMMENT ON COLUMN impressao_configuracoes_documento.template_layout IS 'Template do layout';
COMMENT ON COLUMN impressao_configuracoes_documento.css_layout IS 'CSS do layout';
COMMENT ON COLUMN impressao_configuracoes_documento.javascript_layout IS 'JavaScript do layout';
COMMENT ON COLUMN impressao_configuracoes_documento.cabecalho_personalizado IS 'Cabeçalho personalizado';
COMMENT ON COLUMN impressao_configuracoes_documento.cabecalho_template IS 'Template do cabeçalho';
COMMENT ON COLUMN impressao_configuracoes_documento.rodape_personalizado IS 'Rodapé personalizado';
COMMENT ON COLUMN impressao_configuracoes_documento.rodape_template IS 'Template do rodapé';
COMMENT ON COLUMN impressao_configuracoes_documento.marca_dagua IS 'Marca dágua';
COMMENT ON COLUMN impressao_configuracoes_documento.texto_marca_dagua IS 'Texto da marca dágua';
COMMENT ON COLUMN impressao_configuracoes_documento.fonte_personalizada IS 'Fonte personalizada';
COMMENT ON COLUMN impressao_configuracoes_documento.fonte_nome IS 'Nome da fonte';
COMMENT ON COLUMN impressao_configuracoes_documento.fonte_tamanho IS 'Tamanho da fonte';
COMMENT ON COLUMN impressao_configuracoes_documento.fonte_cor IS 'Cor da fonte';
COMMENT ON COLUMN impressao_configuracoes_documento.fundo_cor IS 'Cor de fundo';
COMMENT ON COLUMN impressao_configuracoes_documento.copias_documento IS 'Número de cópias';
COMMENT ON COLUMN impressao_configuracoes_documento.impressao_frente_verso IS 'Impressão frente e verso';
COMMENT ON COLUMN impressao_configuracoes_documento.impressao_colorida IS 'Impressão colorida';
COMMENT ON COLUMN impressao_configuracoes_documento.qualidade_impressao IS 'Qualidade da impressão';
COMMENT ON COLUMN impressao_configuracoes_documento.papel_tipo IS 'Tipo de papel';
COMMENT ON COLUMN impressao_configuracoes_documento.papel_tamanho IS 'Tamanho do papel';
COMMENT ON COLUMN impressao_configuracoes_documento.papel_origem IS 'Origem do papel';
COMMENT ON COLUMN impressao_configuracoes_documento.codigo_barras_incluir IS 'Incluir código de barras';
COMMENT ON COLUMN impressao_configuracoes_documento.codigo_barras_tipo IS 'Tipo de código de barras';
COMMENT ON COLUMN impressao_configuracoes_documento.codigo_barras_posicao IS 'Posição do código de barras';
COMMENT ON COLUMN impressao_configuracoes_documento.codigo_barras_tamanho IS 'Tamanho do código de barras';
COMMENT ON COLUMN impressao_configuracoes_documento.qrcode_incluir IS 'Incluir QR code';
COMMENT ON COLUMN impressao_configuracoes_documento.qrcode_conteudo IS 'Conteúdo do QR code';
COMMENT ON COLUMN impressao_configuracoes_documento.qrcode_tamanho IS 'Tamanho do QR code';
COMMENT ON COLUMN impressao_configuracoes_documento.qrcode_posicao IS 'Posição do QR code';
COMMENT ON COLUMN impressao_configuracoes_documento.qrcode_correcao IS 'Correção do QR code';
COMMENT ON COLUMN impressao_configuracoes_documento.imagem_incluir IS 'Incluir imagem';
COMMENT ON COLUMN impressao_configuracoes_documento.imagem_caminho IS 'Caminho da imagem';
COMMENT ON COLUMN impressao_configuracoes_documento.imagem_posicao IS 'Posição da imagem';
COMMENT ON COLUMN impressao_configuracoes_documento.imagem_tamanho IS 'Tamanho da imagem';
COMMENT ON COLUMN impressao_configuracoes_documento.imagem_alinhamento IS 'Alinhamento da imagem';
COMMENT ON COLUMN impressao_configuracoes_documento.tabela_incluir IS 'Incluir tabela';
COMMENT ON COLUMN impressao_configuracoes_documento.tabela_colunas_visiveis IS 'Colunas visíveis da tabela';
COMMENT ON COLUMN impressao_configuracoes_documento.tabela_zebrada IS 'Tabela zebrada';
COMMENT ON COLUMN impressao_configuracoes_documento.tabela_bordas IS 'Bordas da tabela';
COMMENT ON COLUMN impressao_configuracoes_documento.tabela_cabecalho_repetir IS 'Repetir cabeçalho da tabela';
COMMENT ON COLUMN impressao_configuracoes_documento.paginacao_ativa IS 'Paginação ativa';
COMMENT ON COLUMN impressao_configuracoes_documento.linhas_por_pagina IS 'Linhas por página';
COMMENT ON COLUMN impressao_configuracoes_documento.mostrar_numero_pagina IS 'Mostrar número da página';
COMMENT ON COLUMN impressao_configuracoes_documento.mostrar_total_paginas IS 'Mostrar total de páginas';
COMMENT ON COLUMN impressao_configuracoes_documento.filtro_ativo IS 'Filtro ativo';
COMMENT ON COLUMN impressao_configuracoes_documento.filtro_campos IS 'Campos para filtrar';
COMMENT ON COLUMN impressao_configuracoes_documento.filtro_valores_padrao IS 'Valores padrão dos filtros';
COMMENT ON COLUMN impressao_configuracoes_documento.ordenacao_ativa IS 'Ordenação ativa';
COMMENT ON COLUMN impressao_configuracoes_documento.ordenacao_campo_padrao IS 'Campo padrão de ordenação';
COMMENT ON COLUMN impressao_configuracoes_documento.ordenacao_direcao IS 'Direção da ordenação';
COMMENT ON COLUMN impressao_configuracoes_documento.exportacao_ativa IS 'Exportação ativa';
COMMENT ON COLUMN impressao_configuracoes_documento.exportacao_formatos IS 'Formatos de exportação';
COMMENT ON COLUMN impressao_configuracoes_documento.exportacao_caminho_padrao IS 'Caminho padrão de exportação';
COMMENT ON COLUMN impressao_configuracoes_documento.envio_email IS 'Envio por email';
COMMENT ON COLUMN impressao_configuracoes_documento.email_destinatarios IS 'Destinatários do email';
COMMENT ON COLUMN impressao_configuracoes_documento.email_assunto_padrao IS 'Assunto padrão do email';
COMMENT ON COLUMN impressao_configuracoes_documento.email_mensagem_padrao IS 'Mensagem padrão do email';
COMMENT ON COLUMN impressao_configuracoes_documento.agendamento_ativo IS 'Agendamento ativo';
COMMENT ON COLUMN impressao_configuracoes_documento.agendamento_frequencia IS 'Frequência do agendamento';
COMMENT ON COLUMN impressao_configuracoes_documento.agendamento_horario IS 'Horário do agendamento';
COMMENT ON COLUMN impressao_configuracoes_documento.agendamento_dias_semana IS 'Dias da semana do agendamento';
COMMENT ON COLUMN impressao_configuracoes_documento.prioridade_impressao IS 'Prioridade de impressão';
COMMENT ON COLUMN impressao_configuracoes_documento.prioridade_fila IS 'Prioridade na fila';
COMMENT ON COLUMN impressao_configuracoes_documento.retentativas_maximas IS 'Máximo de retentativas';
COMMENT ON COLUMN impressao_configuracoes_documento.intervalo_retentativas_segundos IS 'Intervalo entre retentativas';
COMMENT ON COLUMN impressao_configuracoes_documento.notificar_falha IS 'Notificar falha';
COMMENT ON COLUMN impressao_configuracoes_documento.status_configuracao IS 'Status da configuração';
COMMENT ON COLUMN impressao_configuracoes_documento.data_ultima_impressao IS 'Data da última impressão';
COMMENT ON COLUMN impressao_configuracoes_documento.total_impresso IS 'Total impresso';
COMMENT ON COLUMN impressao_configuracoes_documento.data_cadastro IS 'Data de cadastro';
COMMENT ON COLUMN impressao_configuracoes_documento.data_ultima_atualizacao IS 'Data da última atualização';
COMMENT ON COLUMN impressao_configuracoes_documento.usuario_cadastro IS 'Usuário que cadastrou';
COMMENT ON COLUMN impressao_configuracoes_documento.usuario_ultima_atualizacao IS 'Usuário da última atualização';

COMMENT ON TABLE impressao_historico IS 'Histórico de impressões';
COMMENT ON COLUMN impressao_historico.id_impressora IS 'ID da impressora';
COMMENT ON COLUMN impressao_historico.id_configuracao_documento IS 'ID da configuração do documento';
COMMENT ON COLUMN impressao_historico.tipo_documento IS 'Tipo de documento';
COMMENT ON COLUMN impressao_historico.nome_documento IS 'Nome do documento';
COMMENT ON COLUMN impressao_historico.numero_documento IS 'Número do documento';
COMMENT ON COLUMN impressao_historico.quantidade_paginas IS 'Quantidade de páginas';
COMMENT ON COLUMN impressao_historico.quantidade_copias IS 'Quantidade de cópias';
COMMENT ON COLUMN impressao_historico.tamanho_documento_kb IS 'Tamanho do documento em KB';
COMMENT ON COLUMN impressao_historico.formato_documento IS 'Formato do documento';
COMMENT ON COLUMN impressao_historico.resumo_conteudo IS 'Resumo do conteúdo';
COMMENT ON COLUMN impressao_historico.status_impressao IS 'Status da impressão';
COMMENT ON COLUMN impressao_historico.mensagem_status IS 'Mensagem de status';
COMMENT ON COLUMN impressao_historico.codigo_erro IS 'Código do erro';
COMMENT ON COLUMN impressao_historico.data_envio IS 'Data de envio';
COMMENT ON COLUMN impressao_historico.data_inicio_impressao IS 'Data de início da impressão';
COMMENT ON COLUMN impressao_historico.data_fim_impressao IS 'Data de fim da impressão';
COMMENT ON COLUMN impressao_historico.tempo_total_segundos IS 'Tempo total em segundos';
COMMENT ON COLUMN impressao_historico.papel_utilizado IS 'Papel utilizado';
COMMENT ON COLUMN impressao_historico.tinta_utilizada IS 'Tinta utilizada';
COMMENT ON COLUMN impressao_historico.energia_consumida_watts_hora IS 'Energia consumida';
COMMENT ON COLUMN impressao_historico.usuario_solicitante IS 'Usuário solicitante';
COMMENT ON COLUMN impressao_historico.modulo_origem IS 'Módulo de origem';
COMMENT ON COLUMN impressao_historico.funcionalidade_origem IS 'Funcionalidade de origem';
COMMENT ON COLUMN impressao_historico.ip_origem IS 'IP de origem';
COMMENT ON COLUMN impressao_historico.hostname_origem IS 'Hostname de origem';
COMMENT ON COLUMN impressao_historico.configuracoes_usadas IS 'Configurações utilizadas';
COMMENT ON COLUMN impressao_historico.parametros_especiais IS 'Parâmetros especiais';
COMMENT ON COLUMN impressao_historico.qualidade_impressao IS 'Qualidade da impressão';
COMMENT ON COLUMN impressao_historico.avaliacao_usuario IS 'Avaliação do usuário';
COMMENT ON COLUMN impressao_historico.comentario_avaliacao IS 'Comentário da avaliação';
COMMENT ON COLUMN impressao_historico.tentativas_realizadas IS 'Tentativas realizadas';
COMMENT ON COLUMN impressao_historico.motivos_falha IS 'Motivos da falha';
COMMENT ON COLUMN impressao_historico.data_registro IS 'Data de registro';

COMMENT ON TABLE impressora_status_tempo_real IS 'Status das impressoras em tempo real';
COMMENT ON COLUMN impressora_status_tempo_real.id_impressora IS 'ID da impressora';
COMMENT ON COLUMN impressora_status_tempo_real.status_atual IS 'Status atual';
COMMENT ON COLUMN impressora_status_tempo_real.mensagem_status IS 'Mensagem de status';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_erro IS 'Nível de erro';
COMMENT ON COLUMN impressora_status_tempo_real.status_conexao IS 'Status da conexão';
COMMENT ON COLUMN impressora_status_tempo_real.qualidade_sinal IS 'Qualidade do sinal';
COMMENT ON COLUMN impressora_status_tempo_real.latencia_ms IS 'Latência em ms';
COMMENT ON COLUMN impressora_status_tempo_real.velocidade_conexao_kbps IS 'Velocidade de conexão';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_papel IS 'Nível de papel (%)';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_tinta_ciano IS 'Nível de tinta ciano (%)';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_tinta_magenta IS 'Nível de tinta magenta (%)';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_tinta_amarelo IS 'Nível de tinta amarelo (%)';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_tinta_preto IS 'Nível de tinta preto (%)';
COMMENT ON COLUMN impressora_status_tempo_real.nivel_toner IS 'Nível de toner (%)';
COMMENT ON COLUMN impressora_status_tempo_real.temperatura_atual IS 'Temperatura atual';
COMMENT ON COLUMN impressora_status_tempo_real.umidade_atual IS 'Umidade atual';
COMMENT ON COLUMN impressora_status_tempo_real.horas_funcionamento IS 'Horas de funcionamento';
COMMENT ON COLUMN impressora_status_tempo_real.contador_total_impressoes IS 'Contador total de impressões';
COMMENT ON COLUMN impressora_status_tempo_real.contador_ultima_manutencao IS 'Contador desde última manutenção';
COMMENT ON COLUMN impressora_status_tempo_real.paginas_por_minuto IS 'Páginas por minuto';
COMMENT ON COLUMN impressora_status_tempo_real.tempo_medio_impressao_seg IS 'Tempo médio de impressão';
COMMENT ON COLUMN impressora_status_tempo_real.taxa_erro_percentual IS 'Taxa de erro (%)';
COMMENT ON COLUMN impressora_status_tempo_real.tempo_inatividade_minutos IS 'Tempo de inatividade';
COMMENT ON COLUMN impressora_status_tempo_real.alerta_papel_baixo IS 'Alerta de papel baixo';
COMMENT ON COLUMN impressora_status_tempo_real.alerta_tinta_baixa IS 'Alerta de tinta baixa';
COMMENT ON COLUMN impressora_status_tempo_real.alerta_manutencao IS 'Alerta de manutenção';
COMMENT ON COLUMN impressora_status_tempo_real.alerta_conexao_perdida IS 'Alerta de conexão perdida';
COMMENT ON COLUMN impressora_status_tempo_real.alerta_erro_critico IS 'Alerta de erro crítico';
COMMENT ON COLUMN impressora_status_tempo_real.localizacao_atual IS 'Localização atual';
COMMENT ON COLUMN impressora_status_tempo_real.ultima_movimentacao IS 'Última movimentação';
COMMENT ON COLUMN impressora_status_tempo_real.data_atualizacao IS 'Data de atualização';
COMMENT ON COLUMN impressora_status_tempo_real.usuario_atualizacao IS 'Usuário da atualização';
