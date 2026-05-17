-- =====================================================
-- Hermes Comercial v3.6.10 - Migration
-- Criar tabela de Empresa
-- Data: 08/05/2026
-- =====================================================

-- Criar tabela de Empresa
CREATE TABLE IF NOT EXISTS empresa (
    id SERIAL PRIMARY KEY,
    
    -- Dados Básicos da Empresa
    razao_social VARCHAR(200) NOT NULL,
    nome_fantasia VARCHAR(150) NOT NULL,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    inscricao_estadual VARCHAR(20),
    inscricao_municipal VARCHAR(20),
    
    -- Dados de Contato
    telefone_empresa VARCHAR(20),
    celular_empresa VARCHAR(20),
    email_empresa VARCHAR(100),
    website_empresa VARCHAR(150),
    
    -- Endereço da Empresa
    endereco_empresa VARCHAR(200),
    bairro_empresa VARCHAR(100),
    cidade_empresa VARCHAR(100),
    estado_empresa VARCHAR(2),
    cep_empresa VARCHAR(9),
    pais_empresa VARCHAR(50),
    
    -- Dados de Localização
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    
    -- Dados de Identificação Visual
    logo_empresa VARCHAR(255), -- Caminho do arquivo de logo
    logo_base64 TEXT, -- Logo em base64 para armazenamento direto
    cor_primaria VARCHAR(7) DEFAULT '#0066CC', -- Cor primária da marca
    cor_secundaria VARCHAR(7) DEFAULT '#FF6600', -- Cor secundária da marca
    
    -- Dados de Regime Tributário
    regime_tributario VARCHAR(50), -- SIMPLES NACIONAL, LUCRO PRESUMIDO, LUCRO REAL, etc.
    aliquota_pis DECIMAL(5,2) DEFAULT 0.00,
    aliquota_cofins DECIMAL(5,2) DEFAULT 0.00,
    aliquota_ir DECIMAL(5,2) DEFAULT 0.00,
    aliquota_iss DECIMAL(5,2) DEFAULT 0.00,
    
    -- Dados de Certificado Digital
    certificado_digital VARCHAR(255), -- Caminho do certificado A1
    senha_certificado VARCHAR(255), -- Senha do certificado
    data_validade_certificado DATE,
    
    -- Dados de Contador
    contador_nome VARCHAR(100),
    contador_crc VARCHAR(11),
    contador_telefone VARCHAR(20),
    contador_email VARCHAR(100),
    
    -- Dados de Configuração
    formato_nf_e VARCHAR(10) DEFAULT '55', -- Formato da nota fiscal (55 ou 65)
    serie_nf_e VARCHAR(3) DEFAULT '001',
    numero_ultimo_nf_e INTEGER DEFAULT 0,
    ambiente_nf_e VARCHAR(10) DEFAULT 'HOMOLOGACAO', -- HOMOLOGACAO ou PRODUCAO
    
    -- Configurações de Sistema
    timezone_empresa VARCHAR(50) DEFAULT 'America/Sao_Paulo',
    idioma_padrao VARCHAR(10) DEFAULT 'PT-BR',
    moeda_padrao VARCHAR(3) DEFAULT 'BRL',
    formato_data VARCHAR(20) DEFAULT 'dd/MM/yyyy',
    formato_hora VARCHAR(10) DEFAULT 'HH:mm:ss',
    formato_numero VARCHAR(20) DEFAULT '#,##0.00',
    
    -- Configurações de PDV
    numero_pdv_padrao INTEGER DEFAULT 1,
    impressora_padrao VARCHAR(100),
    impressora_nao_fiscal_padrao VARCHAR(100),
    gaveta_dinheiro_padrao VARCHAR(100),
    leitor_codigo_barras_padrao VARCHAR(100),
    
    -- Configurações de Relatórios
    logo_relatorio VARCHAR(255), -- Logo específico para relatórios
    rodape_relatorio TEXT, -- Texto de rodapé para relatórios
    cabecalho_relatorio TEXT, -- Texto de cabeçalho para relatórios
    
    -- Configurações de Backup
    diretorio_backup VARCHAR(255),
    frequencia_backup VARCHAR(20) DEFAULT 'DIARIO', -- DIARIO, SEMANAL, MENSAL
    horario_backup TIME DEFAULT '23:00:00',
    backup_automatico BOOLEAN DEFAULT TRUE,
    dias_retention_backup INTEGER DEFAULT 30,
    
    -- Configurações de Integração
    api_key VARCHAR(255),
    webhook_url VARCHAR(255),
    integracao_contador BOOLEAN DEFAULT FALSE,
    integracao_banco BOOLEAN DEFAULT FALSE,
    integracao_erps BOOLEAN DEFAULT FALSE,
    
    -- Dados Financeiros
    banco_nome VARCHAR(100),
    banco_agencia VARCHAR(10),
    banco_conta VARCHAR(20),
    banco_tipo VARCHAR(20), -- CONTA_CORRENTE, POUPANCA, INVESTIMENTO
    banco_operacao VARCHAR(20), -- 001, 023, etc.
    
    -- Limites e Configurações
    limite_venda_diario DECIMAL(15,2) DEFAULT 999999.99,
    limite_desconto_venda DECIMAL(5,2) DEFAULT 50.00,
    exigir_cpf_venda BOOLEAN DEFAULT TRUE,
    permitir_venda_prazo BOOLEAN DEFAULT TRUE,
    prazo_maximo_venda INTEGER DEFAULT 30, -- Dias
    
    -- Configurações de Estoque
    controle_estoque BOOLEAN DEFAULT TRUE,
    estoque_negativo BOOLEAN DEFAULT FALSE,
    controle_lote BOOLEAN DEFAULT FALSE,
    controle_validade BOOLEAN DEFAULT FALSE,
    
    -- Configurações de Fiscal
    emitir_nf_e_automatico BOOLEAN DEFAULT FALSE,
    exigir_email_cliente BOOLEAN DEFAULT FALSE,
    salvar_xml_nf_e BOOLEAN DEFAULT TRUE,
    danfe_automatico BOOLEAN DEFAULT TRUE,
    
    -- Status e Controle
    status_empresa VARCHAR(20) NOT NULL DEFAULT 'ATIVA', -- ATIVA, INATIVA, BLOQUEADA
    data_cadastro DATE DEFAULT CURRENT_DATE,
    data_ultima_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_empresa CHECK (status_empresa IN ('ATIVA', 'INATIVA', 'BLOQUEADA')),
    CONSTRAINT chk_regime_tributario CHECK (regime_tributario IN ('SIMPLES NACIONAL', 'LUCRO PRESUMIDO', 'LUCRO REAL', 'LUCRO ARBITRADO')),
    CONSTRAINT chk_formato_nf_e CHECK (formato_nf_e IN ('55', '65')),
    CONSTRAINT chk_ambiente_nf_e CHECK (ambiente_nf_e IN ('HOMOLOGACAO', 'PRODUCAO')),
    CONSTRAINT chk_frequencia_backup CHECK (frequencia_backup IN ('DIARIO', 'SEMANAL', 'MENSAL')),
    CONSTRAINT chk_banco_tipo CHECK (banco_tipo IN ('CONTA_CORRENTE', 'POUPANCA', 'INVESTIMENTO')),
    CONSTRAINT chk_aliquotas_empresa CHECK (aliquota_pis >= 0 AND aliquota_pis <= 100 AND aliquota_cofins >= 0 AND aliquota_cofins <= 100 AND aliquota_ir >= 0 AND aliquota_ir <= 100 AND aliquota_iss >= 0 AND aliquota_iss <= 100),
    CONSTRAINT chk_limites_empresa CHECK (limite_venda_diario > 0 AND limite_desconto_venda >= 0 AND limite_desconto_venda <= 100 AND prazo_maximo_venda > 0),
    CONSTRAINT uq_cnpj_empresa UNIQUE (cnpj)
);

-- Criar tabela de Filiais
CREATE TABLE IF NOT EXISTS filiais (
    id SERIAL PRIMARY KEY,
    id_empresa INTEGER REFERENCES empresa(id) ON DELETE CASCADE,
    
    -- Dados Básicos da Filial
    nome_filial VARCHAR(100) NOT NULL,
    cnpj_filial VARCHAR(18),
    inscricao_estadual_filial VARCHAR(20),
    
    -- Dados de Contato
    telefone_filial VARCHAR(20),
    celular_filial VARCHAR(20),
    email_filial VARCHAR(100),
    
    -- Endereço da Filial
    endereco_filial VARCHAR(200),
    bairro_filial VARCHAR(100),
    cidade_filial VARCHAR(100),
    estado_filial VARCHAR(2),
    cep_filial VARCHAR(9),
    
    -- Dados Operacionais
    gerente_filial VARCHAR(100),
    telefone_gerente VARCHAR(20),
    email_gerente VARCHAR(100),
    
    -- Configurações Específicas
    numero_pdv_filial INTEGER DEFAULT 1,
    impressora_filial VARCHAR(100),
    gaveta_dinheiro_filial VARCHAR(100),
    
    -- Status e Controle
    status_filial VARCHAR(20) NOT NULL DEFAULT 'ATIVA', -- ATIVA, INATIVA, BLOQUEADA
    data_abertura DATE,
    data_fechamento DATE,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_status_filial CHECK (status_filial IN ('ATIVA', 'INATIVA', 'BLOQUEADA')),
    CONSTRAINT uq_nome_filial_empresa UNIQUE (id_empresa, nome_filial),
    CONSTRAINT uq_cnpj_filial_empresa UNIQUE (id_empresa, cnpj_filial)
);

-- Criar tabela de Configurações da Empresa
CREATE TABLE IF NOT EXISTS configuracoes_empresa (
    id SERIAL PRIMARY KEY,
    id_empresa INTEGER REFERENCES empresa(id) ON DELETE CASCADE,
    
    -- Chave e Valor
    chave_configuracao VARCHAR(100) NOT NULL,
    valor_configuracao TEXT,
    tipo_configuracao VARCHAR(20) DEFAULT 'TEXTO', -- TEXTO, NUMERO, BOOLEAN, DATA, JSON
    descricao_configuracao TEXT,
    
    -- Controle
    categoria_configuracao VARCHAR(50), -- GERAL, PDV, FISCAL, ESTOQUE, FINANCEIRO, INTEGRACOES
    editavel BOOLEAN DEFAULT TRUE,
    visivel BOOLEAN DEFAULT TRUE,
    
    -- Auditoria
    usuario_criacao VARCHAR(50) DEFAULT CURRENT_USER,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT CURRENT_USER,
    
    -- Constraints
    CONSTRAINT chk_tipo_configuracao CHECK (tipo_configuracao IN ('TEXTO', 'NUMERO', 'BOOLEAN', 'DATA', 'JSON')),
    CONSTRAINT chk_categoria_configuracao CHECK (categoria_configuracao IN ('GERAL', 'PDV', 'FISCAL', 'ESTOQUE', 'FINANCEIRO', 'INTEGRACOES')),
    CONSTRAINT uq_chave_empresa UNIQUE (id_empresa, chave_configuracao)
);

-- Criar tabela de Histórico de Alterações da Empresa
CREATE TABLE IF NOT EXISTS historico_empresa (
    id SERIAL PRIMARY KEY,
    id_empresa INTEGER REFERENCES empresa(id) ON DELETE CASCADE,
    
    -- Dados da Alteração
    campo_alterado VARCHAR(100) NOT NULL,
    valor_anterior TEXT,
    valor_novo TEXT,
    tipo_alteracao VARCHAR(20) NOT NULL, -- INSERCAO, ATUALIZACAO, EXCLUSAO
    
    -- Detalhes
    motivo_alteracao TEXT,
    descricao_alteracao TEXT,
    
    -- Controle
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_alteracao VARCHAR(50) NOT NULL,
    ip_alteracao VARCHAR(45),
    hostname_alteracao VARCHAR(100),
    
    -- Constraints
    CONSTRAINT chk_tipo_alteracao CHECK (tipo_alteracao IN ('INSERCAO', 'ATUALIZACAO', 'EXCLUSAO'))
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_empresa_cnpj ON empresa(cnpj);
CREATE INDEX IF NOT EXISTS idx_empresa_status ON empresa(status_empresa);
CREATE INDEX IF NOT EXISTS idx_empresa_regime_tributario ON empresa(regime_tributario);
CREATE INDEX IF NOT EXISTS idx_empresa_data_cadastro ON empresa(data_cadastro);

CREATE INDEX IF NOT EXISTS idx_filiais_empresa ON filiais(id_empresa);
CREATE INDEX IF NOT EXISTS idx_filiais_status ON filiais(status_filial);
CREATE INDEX IF NOT EXISTS idx_filiais_cnpj ON filiais(cnpj_filial);
CREATE INDEX IF NOT EXISTS idx_filiais_cidade ON filiais(cidade_filial);

CREATE INDEX IF NOT EXISTS idx_configuracoes_empresa_empresa ON configuracoes_empresa(id_empresa);
CREATE INDEX IF NOT EXISTS idx_configuracoes_empresa_chave ON configuracoes_empresa(chave_configuracao);
CREATE INDEX IF NOT EXISTS idx_configuracoes_empresa_categoria ON configuracoes_empresa(categoria_configuracao);

CREATE INDEX IF NOT EXISTS idx_historico_empresa_empresa ON historico_empresa(id_empresa);
CREATE INDEX IF NOT EXISTS idx_historico_empresa_data ON historico_empresa(data_alteracao);
CREATE INDEX IF NOT EXISTS idx_historico_empresa_campo ON historico_empresa(campo_alterado);
CREATE INDEX IF NOT EXISTS idx_historico_empresa_usuario ON historico_empresa(usuario_alteracao);

-- Inserir dados padrão de configurações
INSERT INTO configuracoes_empresa (id_empresa, chave_configuracao, valor_configuracao, tipo_configuracao, descricao_configuracao, categoria_configuracao) VALUES
-- Configurações Gerais
(1, 'NOME_SISTEMA', 'Hermes Comercial', 'TEXTO', 'Nome do sistema exibido na interface', 'GERAL'),
(1, 'VERSAO_SISTEMA', '3.6.10', 'TEXTO', 'Versão atual do sistema', 'GERAL'),
(1, 'MODO_MANUTENCAO', 'false', 'BOOLEAN', 'Indica se sistema está em manutenção', 'GERAL'),
(1, 'MENSAGEM_MANUTENCAO', '', 'TEXTO', 'Mensagem exibida durante manutenção', 'GERAL'),

-- Configurações de PDV
(1, 'EXIGIR_SENHA_SUPERVISOR', 'true', 'BOOLEAN', 'Exigir senha de supervisor para operações críticas', 'PDV'),
(1, 'PERMITIR_CANCELAR_VENDA', 'true', 'BOOLEAN', 'Permitir cancelamento de vendas', 'PDV'),
(1, 'IMPRIMIR_COMPROVANTE_AUTO', 'true', 'BOOLEAN', 'Imprimir comprovante automaticamente', 'PDV'),
(1, 'ARREDONDAMENTO_PRECO', '0.01', 'NUMERO', 'Valor de arredondamento de preços', 'PDV'),

-- Configurações Fiscais
(1, 'EMITIR_NFE_AUTOMATICO', 'false', 'BOOLEAN', 'Emitir NFe automaticamente após venda', 'FISCAL'),
(1, 'VALIDAR_CNPJ_CLIENTE', 'true', 'BOOLEAN', 'Validar CNPJ do cliente na NFe', 'FISCAL'),
(1, 'SALVAR_XML_NFE', 'true', 'BOOLEAN', 'Salvar XML das NFe emitidas', 'FISCAL'),
(1, 'ENVIAR_EMAIL_NFE', 'false', 'BOOLEAN', 'Enviar NFe por email automaticamente', 'FISCAL'),

-- Configurações de Estoque
(1, 'CONTROLAR_ESTOQUE_NEGATIVO', 'false', 'BOOLEAN', 'Permitir estoque negativo', 'ESTOQUE'),
(1, 'ALERTA_ESTOQUE_BAIXO', 'true', 'BOOLEAN', 'Alertar quando estoque estiver baixo', 'ESTOQUE'),
(1, 'BLOQUEAR_VENDA_SEM_ESTOQUE', 'true', 'BOOLEAN', 'Bloquear venda de produtos sem estoque', 'ESTOQUE'),
(1, 'ESTOQUE_MINIMO_PADRAO', '10', 'NUMERO', 'Estoque mínimo padrão para novos produtos', 'ESTOQUE'),

-- Configurações Financeiras
(1, 'FORMA_PAGAMENTO_PADRAO', 'DINHEIRO', 'TEXTO', 'Forma de pagamento padrão', 'FINANCEIRO'),
(1, 'PERMITIR_VENDA_PRAZO', 'true', 'BOOLEAN', 'Permitir vendas a prazo', 'FINANCEIRO'),
(1, 'PRAZO_MAXIMO_PADRAO', '30', 'NUMERO', 'Prazo máximo padrão em dias', 'FINANCEIRO'),
(1, 'JUROS_MENSAL_PADRAO', '2.00', 'NUMERO', 'Juros mensal padrão para vendas a prazo', 'FINANCEIRO'),
(1, 'MULTA_ATRASO_PADRAO', '5.00', 'NUMERO', 'Multa por atraso padrão em percentual', 'FINANCEIRO'),

-- Configurações de Integração
(1, 'WEBHOOK_URL', '', 'TEXTO', 'URL para webhook de integrações', 'INTEGRACOES'),
(1, 'API_TOKEN', '', 'TEXTO', 'Token de API para integrações', 'INTEGRACOES'),
(1, 'INTEGRACAO_CONTADOR', 'false', 'BOOLEAN', 'Integração com sistema do contador', 'INTEGRACOES'),
(1, 'INTEGRACAO_BANCO', 'false', 'BOOLEAN', 'Integração com sistema bancário', 'INTEGRACOES')
ON CONFLICT DO NOTHING;

-- Comentários nas tabelas
COMMENT ON TABLE empresa IS 'Tabela principal de dados da empresa';
COMMENT ON COLUMN empresa.id IS 'ID único da empresa';
COMMENT ON COLUMN empresa.razao_social IS 'Razão social da empresa';
COMMENT ON COLUMN empresa.nome_fantasia IS 'Nome fantasia da empresa';
COMMENT ON COLUMN empresa.cnpj IS 'CNPJ da empresa';
COMMENT ON COLUMN empresa.inscricao_estadual IS 'Inscrição estadual da empresa';
COMMENT ON COLUMN empresa.inscricao_municipal IS 'Inscrição municipal da empresa';
COMMENT ON COLUMN empresa.telefone_empresa IS 'Telefone principal da empresa';
COMMENT ON COLUMN empresa.email_empresa IS 'Email principal da empresa';
COMMENT ON COLUMN empresa.website_empresa IS 'Website da empresa';
COMMENT ON COLUMN empresa.endereco_empresa IS 'Endereço completo da empresa';
COMMENT ON COLUMN empresa.regime_tributario IS 'Regime tributário da empresa';
COMMENT ON COLUMN empresa.aliquota_pis IS 'Alíquota de PIS em percentual';
COMMENT ON COLUMN empresa.aliquota_cofins IS 'Alíquota de COFINS em percentual';
COMMENT ON COLUMN empresa.aliquota_ir IS 'Alíquota de Imposto de Renda em percentual';
COMMENT ON COLUMN empresa.aliquota_iss IS 'Alíquota de ISS em percentual';
COMMENT ON COLUMN empresa.certificado_digital IS 'Caminho do certificado digital A1';
COMMENT ON COLUMN empresa.contador_nome IS 'Nome do contador responsável';
COMMENT ON COLUMN empresa.ambiente_nf_e IS 'Ambiente da NFe (homologação ou produção)';
COMMENT ON COLUMN empresa.timezone_empresa IS 'Timezone da empresa';
COMMENT ON COLUMN empresa.idioma_padrao IS 'Idioma padrão do sistema';
COMMENT ON COLUMN empresa.moeda_padrao IS 'Moeda padrão do sistema';
COMMENT ON COLUMN empresa.limite_venda_diario IS 'Limite diário de vendas';
COMMENT ON COLUMN empresa.limite_desconto_venda IS 'Limite máximo de desconto em percentual';
COMMENT ON COLUMN empresa.exigir_cpf_venda IS 'Exigir CPF na venda';
COMMENT ON COLUMN empresa.permitir_venda_prazo IS 'Permitir vendas a prazo';
COMMENT ON COLUMN empresa.prazo_maximo_venda IS 'Prazo máximo para vendas a prazo em dias';
COMMENT ON COLUMN empresa.status_empresa IS 'Status da empresa';
COMMENT ON COLUMN empresa.data_cadastro IS 'Data de cadastro da empresa';

COMMENT ON TABLE filiais IS 'Tabela de filiais da empresa';
COMMENT ON COLUMN filiais.id IS 'ID único da filial';
COMMENT ON COLUMN filiais.id_empresa IS 'ID da empresa matriz';
COMMENT ON COLUMN filiais.nome_filial IS 'Nome da filial';
COMMENT ON COLUMN filiais.cnpj_filial IS 'CNPJ da filial';
COMMENT ON COLUMN filiais.gerente_filial IS 'Nome do gerente da filial';
COMMENT ON COLUMN filiais.numero_pdv_filial IS 'Número do PDV da filial';
COMMENT ON COLUMN filiais.status_filial IS 'Status da filial';
COMMENT ON COLUMN filiais.data_abertura IS 'Data de abertura da filial';
COMMENT ON COLUMN filiais.data_fechamento IS 'Data de fechamento da filial';

COMMENT ON TABLE configuracoes_empresa IS 'Tabela de configurações personalizadas da empresa';
COMMENT ON COLUMN configuracoes_empresa.chave_configuracao IS 'Chave única da configuração';
COMMENT ON COLUMN configuracoes_empresa.valor_configuracao IS 'Valor da configuração';
COMMENT ON COLUMN configuracoes_empresa.tipo_configuracao IS 'Tipo de dado da configuração';
COMMENT ON COLUMN configuracoes_empresa.categoria_configuracao IS 'Categoria da configuração';
COMMENT ON COLUMN configuracoes_empresa.editavel IS 'Indica se configuração pode ser editada';
COMMENT ON COLUMN configuracoes_empresa.visivel IS 'Indica se configuração é visível';

COMMENT ON TABLE historico_empresa IS 'Histórico de alterações nos dados da empresa';
COMMENT ON COLUMN historico_empresa.campo_alterado IS 'Campo que foi alterado';
COMMENT ON COLUMN historico_empresa.valor_anterior IS 'Valor anterior do campo';
COMMENT ON COLUMN historico_empresa.valor_novo IS 'Novo valor do campo';
COMMENT ON COLUMN historico_empresa.tipo_alteracao IS 'Tipo de alteração realizada';
COMMENT ON COLUMN historico_empresa.usuario_alteracao IS 'Usuário que realizou a alteração';
COMMENT ON COLUMN historico_empresa.data_alteracao IS 'Data e hora da alteração';
