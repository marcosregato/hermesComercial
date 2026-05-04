-- =====================================================
-- HERMES COMERCIAL - MIGRAÇÃO v2.8.1 (PRIORIDADE ALTA)
-- Criar tabelas essenciais para serviços críticos
-- Data: 2026-05-04
-- =====================================================

-- Tabela de KPIs do Dashboard
CREATE TABLE IF NOT EXISTS dashboard_kpi (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL, -- VENDAS_HOJE, VENDAS_MES, FATURAMENTO_DIA, FATURAMENTO_MES, TOP_PRODUTOS
    valor DECIMAL(15,2),
    valor_texto VARCHAR(500), -- Para KPIs não numéricos
    dados_adicionais JSONB, -- Dados complexos como lista de top produtos
    data_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_referencia DATE, -- Data de referência do KPI
    usuario_id VARCHAR(50),
    modulo VARCHAR(50), -- VENDAS, FINANCEIRO, ESTOQUE
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Configurações do Dashboard
CREATE TABLE IF NOT EXISTS dashboard_config (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50),
    widget_tipo VARCHAR(50) NOT NULL, -- GRAFICO_VENDAS, KPI_FINANCEIRO, ALERTAS_ESTOQUE
    posicao_x INTEGER DEFAULT 0,
    posicao_y INTEGER DEFAULT 0,
    largura INTEGER DEFAULT 4,
    altura INTEGER DEFAULT 3,
    visivel BOOLEAN DEFAULT TRUE,
    config JSONB, -- Configurações específicas do widget
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Favoritos do Dashboard
CREATE TABLE IF NOT EXISTS dashboard_favoritos (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    configuracao JSONB NOT NULL, -- Layout completo salvo
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_acesso TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Sistema de Permissões (RBAC) - PERFIS
CREATE TABLE IF NOT EXISTS perfil (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    nivel_acesso INTEGER DEFAULT 1, -- 1=Básico, 2=Intermediário, 3=Avançado, 4=Administrador
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Sistema de Permissões (RBAC) - PERMISSÕES
CREATE TABLE IF NOT EXISTS permissao (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    descricao TEXT,
    modulo VARCHAR(50) NOT NULL, -- VENDAS, ESTOQUE, FINANCEIRO, RELATORIOS, CONFIGURACOES
    acao VARCHAR(50) NOT NULL, -- CREATE, READ, UPDATE, DELETE, EXECUTE
    recurso VARCHAR(100), -- Recurso específico (ex: produto, cliente)
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sistema de Permissões (RBAC) - RELAÇÃO USUÁRIO/PERFIL
CREATE TABLE IF NOT EXISTS usuario_perfil (
    usuario_id INTEGER REFERENCES usuario(id) ON DELETE CASCADE,
    perfil_id INTEGER REFERENCES perfil(id) ON DELETE CASCADE,
    data_atribuicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atribuido_por VARCHAR(50) DEFAULT 'system',
    PRIMARY KEY (usuario_id, perfil_id)
);

-- Sistema de Permissões (RBAC) - RELAÇÃO PERFIL/PERMISSÃO
CREATE TABLE IF NOT EXISTS perfil_permissao (
    perfil_id INTEGER REFERENCES perfil(id) ON DELETE CASCADE,
    permissao_id INTEGER REFERENCES permissao(id) ON DELETE CASCADE,
    data_concessao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    concedido_por VARCHAR(50) DEFAULT 'system',
    PRIMARY KEY (perfil_id, permissao_id)
);

-- Templates de Relatórios
CREATE TABLE IF NOT EXISTS relatorio_template (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(50) NOT NULL, -- VENDAS, ESTOQUE, FINANCEIRO, FORNECEDORES, CLIENTES
    formato VARCHAR(20) NOT NULL DEFAULT 'PDF', -- PDF, EXCEL, CSV
    template_config JSONB NOT NULL, -- Estrutura do template
    cabecalho JSONB, -- Configuração de cabeçalho
    rodape JSONB, -- Configuração de rodapé
    query_sql TEXT, -- SQL para gerar dados (opcional)
    usuario_criacao VARCHAR(50) DEFAULT 'system',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE,
    publico BOOLEAN DEFAULT FALSE -- Se pode ser usado por outros usuários
);

-- Configurações de Relatórios
CREATE TABLE IF NOT EXISTS relatorio_config (
    id SERIAL PRIMARY KEY,
    template_id INTEGER REFERENCES relatorio_template(id),
    usuario_id VARCHAR(50),
    nome_config VARCHAR(255),
    filtros JSONB, -- Filtros salvos
    parametros JSONB, -- Parâmetros personalizados
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_uso TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Histórico de Relatórios Gerados
CREATE TABLE IF NOT EXISTS relatorio_historico (
    id SERIAL PRIMARY KEY,
    template_id INTEGER REFERENCES relatorio_template(id),
    usuario_id VARCHAR(50),
    nome_arquivo VARCHAR(255),
    caminho_arquivo VARCHAR(500),
    formato VARCHAR(20),
    tamanho_arquivo BIGINT,
    parametros_geracao JSONB,
    status VARCHAR(20) DEFAULT 'GERADO', -- GERADO, ERRO, CANCELADO
    data_geracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tempo_geracao_segundos INTEGER,
    mensagem_erro TEXT
);

-- Configurações de APIs de Pagamento
CREATE TABLE IF NOT EXISTS pagamento_config (
    id SERIAL PRIMARY KEY,
    provedor VARCHAR(50) NOT NULL, -- STRIPE, MERCADO_PAGO, PAGSEGURO
    nome_config VARCHAR(255) NOT NULL,
    ambiente VARCHAR(20) DEFAULT 'TESTE', -- TESTE, PRODUCAO
    api_key VARCHAR(500),
    api_secret VARCHAR(500),
    webhook_url VARCHAR(500),
    webhook_secret VARCHAR(255),
    moeda_padrao VARCHAR(3) DEFAULT 'BRL',
    ativo BOOLEAN DEFAULT TRUE,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_configuracao VARCHAR(50) DEFAULT 'system',
    config_adicional JSONB -- Configurações específicas do provedor
);

-- Transações de Pagamento Externo
CREATE TABLE IF NOT EXISTS pagamento_transacao (
    id SERIAL PRIMARY KEY,
    config_id INTEGER REFERENCES pagamento_config(id),
    provedor VARCHAR(50) NOT NULL,
    id_transacao_externa VARCHAR(100) UNIQUE,
    id_venda INTEGER REFERENCES venda(id), -- Relacionar com venda se aplicável
    tipo VARCHAR(20) NOT NULL, -- PAGAMENTO, REEMBOLSO, ESTORNO
    status VARCHAR(20) NOT NULL, -- PENDENTE, PROCESSANDO, APROVADO, REJEITADO, CANCELADO
    valor_original DECIMAL(10,2) NOT NULL,
    valor_liquido DECIMAL(10,2), -- Valor após taxas
    moeda VARCHAR(3) DEFAULT 'BRL',
    forma_pagamento VARCHAR(50), -- CREDIT_CARD, DEBIT_CARD, PIX, BOLETO
    parcelas INTEGER DEFAULT 1,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP,
    data_cancelamento TIMESTAMP,
    dados_cliente JSONB, -- Dados do cliente para pagamento
    dados_resposta JSONB, -- Resposta completa da API
    dados_webhook JSONB, -- Dados recebidos via webhook
    mensagem_erro TEXT,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Webhooks de Pagamento
CREATE TABLE IF NOT EXISTS pagamento_webhook (
    id SERIAL PRIMARY KEY,
    transacao_id INTEGER REFERENCES pagamento_transacao(id),
    provedor VARCHAR(50) NOT NULL,
    tipo_evento VARCHAR(50) NOT NULL, -- PAYMENT_SUCCESS, PAYMENT_FAILED, etc.
    dados_recebidos JSONB NOT NULL,
    data_recebimento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processado BOOLEAN DEFAULT FALSE,
    data_processamento TIMESTAMP,
    mensagem_erro TEXT
);

-- =====================================================
-- ÍNDICES PARA NOVAS TABELAS
-- =====================================================

-- Índices para dashboard_kpi
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_tipo ON dashboard_kpi(tipo);
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_data_referencia ON dashboard_kpi(data_referencia);
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_data_calculo ON dashboard_kpi(data_calculo);
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_modulo ON dashboard_kpi(modulo);
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_usuario ON dashboard_kpi(usuario_id);
CREATE INDEX IF NOT EXISTS idx_dashboard_kpi_ativo ON dashboard_kpi(ativo);

-- Índices para dashboard_config
CREATE INDEX IF NOT EXISTS idx_dashboard_config_usuario ON dashboard_config(usuario_id);
CREATE INDEX IF NOT EXISTS idx_dashboard_config_widget ON dashboard_config(widget_tipo);
CREATE INDEX IF NOT EXISTS idx_dashboard_config_visivel ON dashboard_config(visivel);

-- Índices para dashboard_favoritos
CREATE INDEX IF NOT EXISTS idx_dashboard_favoritos_usuario ON dashboard_favoritos(usuario_id);
CREATE INDEX IF NOT EXISTS idx_dashboard_favoritos_ativo ON dashboard_favoritos(ativo);

-- Índices para perfil
CREATE INDEX IF NOT EXISTS idx_perfil_nome ON perfil(nome);
CREATE INDEX IF NOT EXISTS idx_perfil_nivel_acesso ON perfil(nivel_acesso);
CREATE INDEX IF NOT EXISTS idx_perfil_ativo ON perfil(ativo);

-- Índices para permissao
CREATE INDEX IF NOT EXISTS idx_permissao_nome ON permissao(nome);
CREATE INDEX IF NOT EXISTS idx_permissao_modulo ON permissao(modulo);
CREATE INDEX IF NOT EXISTS idx_permissao_acao ON permissao(acao);
CREATE INDEX IF NOT EXISTS idx_permissao_recurso ON permissao(recurso);
CREATE INDEX IF NOT EXISTS idx_permissao_ativo ON permissao(ativo);

-- Índices para usuario_perfil
CREATE INDEX IF NOT EXISTS idx_usuario_perfil_usuario ON usuario_perfil(usuario_id);
CREATE INDEX IF NOT EXISTS idx_usuario_perfil_perfil ON usuario_perfil(perfil_id);

-- Índices para perfil_permissao
CREATE INDEX IF NOT EXISTS idx_perfil_permissao_perfil ON perfil_permissao(perfil_id);
CREATE INDEX IF NOT EXISTS idx_perfil_permissao_permissao ON perfil_permissao(permissao_id);

-- Índices para relatorio_template
CREATE INDEX IF NOT EXISTS idx_relatorio_template_tipo ON relatorio_template(tipo);
CREATE INDEX IF NOT EXISTS idx_relatorio_template_formato ON relatorio_template(formato);
CREATE INDEX IF NOT EXISTS idx_relatorio_template_ativo ON relatorio_template(ativo);
CREATE INDEX IF NOT EXISTS idx_relatorio_template_publico ON relatorio_template(publico);
CREATE INDEX IF NOT EXISTS idx_relatorio_template_usuario ON relatorio_template(usuario_criacao);

-- Índices para relatorio_config
CREATE INDEX IF NOT EXISTS idx_relatorio_config_template ON relatorio_config(template_id);
CREATE INDEX IF NOT EXISTS idx_relatorio_config_usuario ON relatorio_config(usuario_id);
CREATE INDEX IF NOT EXISTS idx_relatorio_config_ativo ON relatorio_config(ativo);

-- Índices para relatorio_historico
CREATE INDEX IF NOT EXISTS idx_relatorio_historico_template ON relatorio_historico(template_id);
CREATE INDEX IF NOT EXISTS idx_relatorio_historico_usuario ON relatorio_historico(usuario_id);
CREATE INDEX IF NOT EXISTS idx_relatorio_historico_status ON relatorio_historico(status);
CREATE INDEX IF NOT EXISTS idx_relatorio_historico_data_geracao ON relatorio_historico(data_geracao);

-- Índices para pagamento_config
CREATE INDEX IF NOT EXISTS idx_pagamento_config_provedor ON pagamento_config(provedor);
CREATE INDEX IF NOT EXISTS idx_pagamento_config_ambiente ON pagamento_config(ambiente);
CREATE INDEX IF NOT EXISTS idx_pagamento_config_ativo ON pagamento_config(ativo);

-- Índices para pagamento_transacao
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_config ON pagamento_transacao(config_id);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_provedor ON pagamento_transacao(provedor);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_externa ON pagamento_transacao(id_transacao_externa);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_venda ON pagamento_transacao(id_venda);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_status ON pagamento_transacao(status);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_data_criacao ON pagamento_transacao(data_criacao);
CREATE INDEX IF NOT EXISTS idx_pagamento_transacao_tipo ON pagamento_transacao(tipo);

-- Índices para pagamento_webhook
CREATE INDEX IF NOT EXISTS idx_pagamento_webhook_transacao ON pagamento_webhook(transacao_id);
CREATE INDEX IF NOT EXISTS idx_pagamento_webhook_provedor ON pagamento_webhook(provedor);
CREATE INDEX IF NOT EXISTS idx_pagamento_webhook_evento ON pagamento_webhook(tipo_evento);
CREATE INDEX IF NOT EXISTS idx_pagamento_webhook_processado ON pagamento_webhook(processado);
CREATE INDEX IF NOT EXISTS idx_pagamento_webhook_data_recebimento ON pagamento_webhook(data_recebimento);

-- =====================================================
-- TRIGGERS PARA NOVAS TABELAS
-- =====================================================

-- Trigger para atualizar data_atualizacao da tabela dashboard_config
CREATE OR REPLACE FUNCTION trg_dashboard_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_dashboard_config_atualizar_timestamp
    BEFORE UPDATE ON dashboard_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_dashboard_config_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela perfil
CREATE OR REPLACE FUNCTION trg_perfil_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_perfil_atualizar_timestamp
    BEFORE UPDATE ON perfil
    FOR EACH ROW
    EXECUTE FUNCTION trg_perfil_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela relatorio_template
CREATE OR REPLACE FUNCTION trg_relatorio_template_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_relatorio_template_atualizar_timestamp
    BEFORE UPDATE ON relatorio_template
    FOR EACH ROW
    EXECUTE FUNCTION trg_relatorio_template_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela pagamento_config
CREATE OR REPLACE FUNCTION trg_pagamento_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_pagamento_config_atualizar_timestamp
    BEFORE UPDATE ON pagamento_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_pagamento_config_atualizar_timestamp();

-- =====================================================
-- INSERÇÕES INICIAIS - DADOS ESSENCIAIS
-- =====================================================

-- Inserir perfis de acesso padrão
INSERT INTO perfil (nome, descricao, nivel_acesso) VALUES
('Administrador', 'Acesso completo a todas as funcionalidades do sistema', 4),
('Gerente', 'Acesso total às operações, sem configurações avançadas', 3),
('Operador', 'Acesso às operações diárias de vendas e estoque', 2),
('Visualizador', 'Acesso apenas para consulta de relatórios', 1)
ON CONFLICT (nome) DO NOTHING;

-- Inserir permissões básicas do sistema
INSERT INTO permissao (nome, descricao, modulo, acao, recurso) VALUES
-- Vendas
('venda_criar', 'Criar novas vendas', 'VENDAS', 'CREATE', 'venda'),
('venda_consultar', 'Consultar vendas', 'VENDAS', 'READ', 'venda'),
('venda_cancelar', 'Cancelar vendas', 'VENDAS', 'DELETE', 'venda'),

-- Estoque
('estoque_entrar', 'Dar entrada no estoque', 'ESTOQUE', 'CREATE', 'produto'),
('estoque_ajustar', 'Ajustar quantidade em estoque', 'ESTOQUE', 'UPDATE', 'produto'),
('estoque_consultar', 'Consultar estoque', 'ESTOQUE', 'READ', 'produto'),

-- Financeiro
('financeiro_contas_pagar', 'Gerenciar contas a pagar', 'FINANCEIRO', 'CREATE', 'conta_pagar'),
('financeiro_contas_receber', 'Gerenciar contas a receber', 'FINANCEIRO', 'CREATE', 'conta_receber'),
('financeiro_fluxo_caixa', 'Visualizar fluxo de caixa', 'FINANCEIRO', 'READ', 'fluxo_caixa'),

-- Relatórios
('relatorio_gerar', 'Gerar relatórios', 'RELATORIOS', 'EXECUTE', 'relatorio'),
('relatorio_exportar', 'Exportar relatórios', 'RELATORIOS', 'CREATE', 'relatorio'),

-- Configurações
('config_usuario', 'Gerenciar usuários', 'CONFIGURACOES', 'CREATE', 'usuario'),
('config_sistema', 'Configurar sistema', 'CONFIGURACOES', 'UPDATE', 'sistema')
ON CONFLICT (nome) DO NOTHING;

-- Associar permissões ao perfil Administrador
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, perm.id 
FROM perfil p, permissao perm 
WHERE p.nome = 'Administrador'
ON CONFLICT DO NOTHING;

-- Associar permissões ao perfil Gerente (exceto configurações de sistema)
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, perm.id 
FROM perfil p, permissao perm 
WHERE p.nome = 'Gerente' AND perm.modulo != 'CONFIGURACOES'
ON CONFLICT DO NOTHING;

-- Associar permissões ao perfil Operador (vendas e estoque)
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, perm.id 
FROM perfil p, permissao perm 
WHERE p.nome = 'Operador' AND perm.modulo IN ('VENDAS', 'ESTOQUE')
ON CONFLICT DO NOTHING;

-- Associar permissões ao perfil Visualizador (apenas consulta)
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, perm.id 
FROM perfil p, permissao perm 
WHERE p.nome = 'Visualizador' AND perm.acao = 'READ'
ON CONFLICT DO NOTHING;

-- Inserir templates de relatório padrão
INSERT INTO relatorio_template (nome, descricao, tipo, formato, template_config) VALUES
('Relatório de Vendas Diárias', 'Vendas realizadas no dia', 'VENDAS', 'PDF', 
'{"columns": ["data", "cliente", "valor_total", "forma_pagamento"], "groupBy": "data"}'),
('Relatório de Estoque Atual', 'Situação atual do estoque', 'ESTOQUE', 'EXCEL',
'{"columns": ["codigo", "descricao", "estoque", "preco"], "filter": "estoque > 0"}'),
('Relatório Financeiro Mensal', 'Resumo financeiro do mês', 'FINANCEIRO', 'PDF',
'{"columns": ["tipo", "valor", "data_vencimento", "status"], "period": "monthly"}')
ON CONFLICT (nome) DO NOTHING;

-- Criar configuração de pagamento de teste (Stripe)
INSERT INTO pagamento_config (provedor, nome_config, ambiente, api_key, webhook_url, moeda_padrao) VALUES
('STRIPE', 'Stripe - Teste', 'TESTE', 'sk_test_123456789', 'https://hermes.com/webhook/stripe', 'BRL')
ON CONFLICT (provedor, nome_config) DO NOTHING;

-- Criar configuração de pagamento de teste (Mercado Pago)
INSERT INTO pagamento_config (provedor, nome_config, ambiente, api_key, webhook_url, moeda_padrao) VALUES
('MERCADO_PAGO', 'Mercado Pago - Teste', 'TESTE', 'TEST_123456789', 'https://hermes.com/webhook/mercadopago', 'BRL')
ON CONFLICT (provedor, nome_config) DO NOTHING;
