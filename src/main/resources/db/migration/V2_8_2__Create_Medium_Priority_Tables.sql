-- =====================================================
-- HERMES COMERCIAL - MIGRAÇÃO v2.8.2 (PRIORIDADE MÉDIA)
-- Criar tabelas para melhorias e funcionalidades adicionais
-- Data: 2026-05-04
-- =====================================================

-- Tabela de Configurações de Cache
CREATE TABLE IF NOT EXISTS cache_config (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(100) UNIQUE NOT NULL,
    valor TEXT,
    tipo VARCHAR(50) NOT NULL DEFAULT 'STRING', -- STRING, NUMBER, BOOLEAN, JSON
    descricao TEXT,
    modulo VARCHAR(50), -- DASHBOARD, PRODUTOS, VENDAS, etc.
    tempo_vida_minutos INTEGER DEFAULT 5, -- TTL em minutos
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Estatísticas de Cache
CREATE TABLE IF NOT EXISTS cache_stats (
    id SERIAL PRIMARY KEY,
    chave_cache VARCHAR(100) NOT NULL,
    tipo_operacao VARCHAR(20) NOT NULL, -- HIT, MISS, SET, DELETE, EVICT
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id VARCHAR(50),
    tamanho_bytes INTEGER,
    tempo_acesso_ms INTEGER, -- Tempo de acesso em milissegundos
    dados_adicionais JSONB
);

-- Tabela de Traduções Customizadas
CREATE TABLE IF NOT EXISTS traducao (
    id SERIAL PRIMARY KEY,
    idioma VARCHAR(10) NOT NULL, -- pt_BR, en_US, es_ES, etc.
    chave VARCHAR(255) NOT NULL,
    valor TEXT NOT NULL,
    modulo VARCHAR(50), -- VENDAS, ESTOQUE, FINANCEIRO, etc.
    contexto TEXT, -- Contexto de uso da tradução
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT 'system',
    ativo BOOLEAN DEFAULT TRUE,
    UNIQUE(idioma, chave)
);

-- Tabela de Preferências de Idioma por Usuário
CREATE TABLE IF NOT EXISTS idioma_preferencia (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50) UNIQUE NOT NULL,
    idioma VARCHAR(10) NOT NULL DEFAULT 'pt_BR',
    formato_data VARCHAR(20) DEFAULT 'dd/MM/yyyy',
    formato_hora VARCHAR(10) DEFAULT 'HH:mm',
    formato_numero VARCHAR(20) DEFAULT '#,##0.00',
    formato_moeda VARCHAR(20) DEFAULT 'R$ #,##0.00',
    data_definicao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_acesso TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Logs de Busca Avançada
CREATE TABLE IF NOT EXISTS busca_log (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50),
    termo_busca TEXT,
    tipo_busca VARCHAR(50) NOT NULL, -- PRODUTO, CLIENTE, VENDA, FORNECEDOR
    filtros JSONB, -- Filtros aplicados na busca
    resultados_encontrados INTEGER DEFAULT 0,
    resultados_exibidos INTEGER DEFAULT 0,
    tempo_execucao_ms INTEGER,
    data_busca TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT
);

-- Tabela de Buscas Favoritas Salvas
CREATE TABLE IF NOT EXISTS busca_favorita (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo_busca VARCHAR(50) NOT NULL,
    termo_busca TEXT,
    filtros JSONB NOT NULL, -- Configuração completa da busca
    ordem_campos TEXT[], -- Ordem dos campos de exibição
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ultimo_uso TIMESTAMP,
    vezes_usado INTEGER DEFAULT 0,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Índice de Busca Otimizado
CREATE TABLE IF NOT EXISTS busca_indice (
    id SERIAL PRIMARY KEY,
    tabela_referencia VARCHAR(50) NOT NULL, -- produto, cliente, venda, etc.
    id_registro VARCHAR(50) NOT NULL, -- ID do registro na tabela original
    conteudo_indexado TEXT NOT NULL, -- Texto concatenado para busca full-text
    palavras_chave TEXT[], -- Array de palavras-chave
    peso_relevancia DECIMAL(3,2) DEFAULT 1.0, -- Peso para ranking
    data_indexacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Configurações de Interface
CREATE TABLE IF NOT EXISTS interface_config (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50),
    componente VARCHAR(100) NOT NULL, -- dashboard, pdv, relatorios, etc.
    configuracao JSONB NOT NULL, -- Configurações específicas
    tema VARCHAR(50) DEFAULT 'DEFAULT', -- DEFAULT, DARK, LIGHT, CUSTOM
    fonte_tamanho INTEGER DEFAULT 12,
    idioma_interface VARCHAR(10) DEFAULT 'pt_BR',
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

-- Tabela de Logs de Performance
CREATE TABLE IF NOT EXISTS performance_log (
    id SERIAL PRIMARY KEY,
    usuario_id VARCHAR(50),
    modulo VARCHAR(50) NOT NULL,
    operacao VARCHAR(100) NOT NULL,
    tempo_execucao_ms INTEGER NOT NULL,
    memoria_usada_mb INTEGER,
    dados_entrada JSONB,
    dados_saida JSONB,
    status VARCHAR(20) NOT NULL, -- SUCESSO, ERRO, TIMEOUT
    mensagem_erro TEXT,
    data_operacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    detalhes_adicionais JSONB
);

-- Tabela de Agendamentos de Tarefas
CREATE TABLE IF NOT EXISTS agendamento_tarefa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    tipo_tarefa VARCHAR(50) NOT NULL, -- BACKUP, RELATORIO, LIMPEZA_CACHE, etc.
    expressao_cron VARCHAR(100) NOT NULL, -- Expressão Cron para agendamento
    proxima_execucao TIMESTAMP NOT NULL,
    ultima_execucao TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ATIVA', -- ATIVA, INATIVA, PAUSADA, ERRO
    configuracao JSONB, -- Parâmetros da tarefa
    tempo_limite_segundos INTEGER DEFAULT 3600, -- Timeout em segundos
    tentativas_maximas INTEGER DEFAULT 3,
    tentativas_atuais INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_criacao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Histórico de Execução de Tarefas
CREATE TABLE IF NOT EXISTS agendamento_historico (
    id SERIAL PRIMARY KEY,
    agendamento_id INTEGER REFERENCES agendamento_tarefa(id),
    data_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_fim TIMESTAMP,
    status VARCHAR(20) NOT NULL, -- EXECUTANDO, CONCLUIDA, ERRO, TIMEOUT
    resultado JSONB, -- Resultado da execução
    mensagem_erro TEXT,
    tempo_execucao_segundos INTEGER,
    registros_processados INTEGER DEFAULT 0,
    dados_entrada JSONB,
    usuario_execucao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Configurações de E-mail
CREATE TABLE IF NOT EXISTS email_config (
    id SERIAL PRIMARY KEY,
    nome_config VARCHAR(255) NOT NULL,
    host_smtp VARCHAR(255) NOT NULL,
    porta_smtp INTEGER DEFAULT 587,
    usuario_email VARCHAR(255) NOT NULL,
    senha_email VARCHAR(500), -- Em produção, usar criptografia
    uso_ssl BOOLEAN DEFAULT TRUE,
    uso_tls BOOLEAN DEFAULT TRUE,
    email_remetente VARCHAR(255),
    nome_remetente VARCHAR(255),
    tempo_timeout_segundos INTEGER DEFAULT 30,
    ativo BOOLEAN DEFAULT TRUE,
    data_configuracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_configuracao VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Histórico de E-mails Enviados
CREATE TABLE IF NOT EXISTS email_historico (
    id SERIAL PRIMARY KEY,
    config_id INTEGER REFERENCES email_config(id),
    destinatario VARCHAR(255) NOT NULL,
    assunto VARCHAR(500) NOT NULL,
    corpo_mensagem TEXT,
    anexos JSONB, -- Lista de anexos
    status VARCHAR(20) DEFAULT 'ENVIADO', -- ENVIADO, ERRO, PENDENTE
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mensagem_erro TEXT,
    tentativas INTEGER DEFAULT 1,
    usuario_envio VARCHAR(50) DEFAULT 'system'
);

-- Tabela de Configurações de Sistema
CREATE TABLE IF NOT EXISTS sistema_config (
    id SERIAL PRIMARY KEY,
    chave VARCHAR(100) UNIQUE NOT NULL,
    valor TEXT,
    tipo VARCHAR(50) NOT NULL DEFAULT 'STRING', -- STRING, NUMBER, BOOLEAN, JSON
    categoria VARCHAR(50), -- GERAL, SEGURANCA, PERFORMANCE, etc.
    descricao TEXT,
    editavel BOOLEAN DEFAULT TRUE,
    requer_reinicializacao BOOLEAN DEFAULT FALSE,
    valor_padrao TEXT,
    validacao_regex VARCHAR(500),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_atualizacao VARCHAR(50) DEFAULT 'system'
);

-- =====================================================
-- ÍNDICES PARA NOVAS TABELAS
-- =====================================================

-- Índices para cache_config
CREATE INDEX IF NOT EXISTS idx_cache_config_chave ON cache_config(chave);
CREATE INDEX IF NOT EXISTS idx_cache_config_modulo ON cache_config(modulo);
CREATE INDEX IF NOT EXISTS idx_cache_config_ativo ON cache_config(ativo);
CREATE INDEX IF NOT EXISTS idx_cache_config_ttl ON cache_config(tempo_vida_minutos);

-- Índices para cache_stats
CREATE INDEX IF NOT EXISTS idx_cache_stats_chave ON cache_stats(chave_cache);
CREATE INDEX IF NOT EXISTS idx_cache_stats_operacao ON cache_stats(tipo_operacao);
CREATE INDEX IF NOT EXISTS idx_cache_stats_data ON cache_stats(data_operacao);
CREATE INDEX IF NOT EXISTS idx_cache_stats_usuario ON cache_stats(usuario_id);

-- Índices para traducao
CREATE INDEX IF NOT EXISTS idx_traducao_idioma ON traducao(idioma);
CREATE INDEX IF NOT EXISTS idx_traducao_chave ON traducao(chave);
CREATE INDEX IF NOT EXISTS idx_traducao_modulo ON traducao(modulo);
CREATE INDEX IF NOT EXISTS idx_traducao_ativa ON traducao(ativo);
CREATE INDEX IF NOT EXISTS idx_traducao_idioma_chave ON traducao(idioma, chave);

-- Índices para idioma_preferencia
CREATE INDEX IF NOT EXISTS idx_idioma_preferencia_usuario ON idioma_preferencia(usuario_id);
CREATE INDEX IF NOT EXISTS idx_idioma_preferencia_idioma ON idioma_preferencia(idioma);

-- Índices para busca_log
CREATE INDEX IF NOT EXISTS idx_busca_log_usuario ON busca_log(usuario_id);
CREATE INDEX IF NOT EXISTS idx_busca_log_tipo ON busca_log(tipo_busca);
CREATE INDEX IF NOT EXISTS idx_busca_log_data ON busca_log(data_busca);
CREATE INDEX IF NOT EXISTS idx_busca_log_termo ON busca_log(termo_busca);

-- Índices para busca_favorita
CREATE INDEX IF NOT EXISTS idx_busca_favorita_usuario ON busca_favorita(usuario_id);
CREATE INDEX IF NOT EXISTS idx_busca_favorita_tipo ON busca_favorita(tipo_busca);
CREATE INDEX IF NOT EXISTS idx_busca_favorita_ativa ON busca_favorita(ativo);
CREATE INDEX IF NOT EXISTS idx_busca_favorita_uso ON busca_favorita(vezes_usado DESC);

-- Índices para busca_indice
CREATE INDEX IF NOT EXISTS idx_busca_indice_tabela ON busca_indice(tabela_referencia);
CREATE INDEX IF NOT EXISTS idx_busca_indice_registro ON busca_indice(id_registro);
CREATE INDEX IF NOT EXISTS idx_busca_indice_conteudo ON busca_indice(conteudo_indexado);
CREATE INDEX IF NOT EXISTS idx_busca_indice_ativo ON busca_indice(ativo);
CREATE INDEX IF NOT EXISTS idx_busca_indice_peso ON busca_indice(peso_relevancia DESC);

-- Índices para interface_config
CREATE INDEX IF NOT EXISTS idx_interface_config_usuario ON interface_config(usuario_id);
CREATE INDEX IF NOT EXISTS idx_interface_config_componente ON interface_config(componente);
CREATE INDEX IF NOT EXISTS idx_interface_config_ativo ON interface_config(ativo);
CREATE INDEX IF NOT EXISTS idx_interface_config_tema ON interface_config(tema);

-- Índices para performance_log
CREATE INDEX IF NOT EXISTS idx_performance_log_usuario ON performance_log(usuario_id);
CREATE INDEX IF NOT EXISTS idx_performance_log_modulo ON performance_log(modulo);
CREATE INDEX IF NOT EXISTS idx_performance_log_operacao ON performance_log(operacao);
CREATE INDEX IF NOT EXISTS idx_performance_log_data ON performance_log(data_operacao);
CREATE INDEX IF NOT EXISTS idx_performance_log_status ON performance_log(status);
CREATE INDEX IF NOT EXISTS idx_performance_log_tempo ON performance_log(tempo_execucao_ms DESC);

-- Índices para agendamento_tarefa
CREATE INDEX IF NOT EXISTS idx_agendamento_tarefa_status ON agendamento_tarefa(status);
CREATE INDEX IF NOT EXISTS idx_agendamento_tarefa_tipo ON agendamento_tarefa(tipo_tarefa);
CREATE INDEX IF NOT EXISTS idx_agendamento_tarefa_proxima ON agendamento_tarefa(proxima_execucao);
CREATE INDEX IF NOT EXISTS idx_agendamento_tarefa_ativa ON agendamento_tarefa(status) WHERE status = 'ATIVA';

-- Índices para agendamento_historico
CREATE INDEX IF NOT EXISTS idx_agendamento_historico_agendamento ON agendamento_historico(agendamento_id);
CREATE INDEX IF NOT EXISTS idx_agendamento_historico_status ON agendamento_historico(status);
CREATE INDEX IF NOT EXISTS idx_agendamento_historico_data ON agendamento_historico(data_inicio DESC);

-- Índices para email_config
CREATE INDEX IF NOT EXISTS idx_email_config_ativo ON email_config(ativo);
CREATE INDEX IF NOT EXISTS idx_email_config_nome ON email_config(nome_config);

-- Índices para email_historico
CREATE INDEX IF NOT EXISTS idx_email_historico_config ON email_historico(config_id);
CREATE INDEX IF NOT EXISTS idx_email_historico_destinatario ON email_historico(destinatario);
CREATE INDEX IF NOT EXISTS idx_email_historico_status ON email_historico(status);
CREATE INDEX IF NOT EXISTS idx_email_historico_data ON email_historico(data_envio DESC);

-- Índices para sistema_config
CREATE INDEX IF NOT EXISTS idx_sistema_config_chave ON sistema_config(chave);
CREATE INDEX IF NOT EXISTS idx_sistema_config_categoria ON sistema_config(categoria);
CREATE INDEX IF NOT EXISTS idx_sistema_config_editavel ON sistema_config(editavel);

-- =====================================================
-- TRIGGERS PARA NOVAS TABELAS
-- =====================================================

-- Trigger para atualizar data_atualizacao da tabela cache_config
CREATE OR REPLACE FUNCTION trg_cache_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_cache_config_atualizar_timestamp
    BEFORE UPDATE ON cache_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_cache_config_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela traducao
CREATE OR REPLACE FUNCTION trg_traducao_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_traducao_atualizar_timestamp
    BEFORE UPDATE ON traducao
    FOR EACH ROW
    EXECUTE FUNCTION trg_traducao_atualizar_timestamp();

-- Trigger para atualizar data_ultimo_acesso da tabela idioma_preferencia
CREATE OR REPLACE FUNCTION trg_idioma_preferencia_atualizar_acesso()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultimo_acesso = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_idioma_preferencia_atualizar_acesso
    BEFORE UPDATE ON idioma_preferencia
    FOR EACH ROW
    EXECUTE FUNCTION trg_idioma_preferencia_atualizar_acesso();

-- Trigger para atualizar data_ultimo_uso da tabela busca_favorita
CREATE OR REPLACE FUNCTION trg_busca_favorita_atualizar_uso()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultimo_uso = CURRENT_TIMESTAMP;
    NEW.vezes_usado = COALESCE(OLD.vezes_usado, 0) + 1;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_busca_favorita_atualizar_uso
    BEFORE UPDATE ON busca_favorita
    FOR EACH ROW
    WHEN (OLD.data_ultimo_uso IS NULL OR OLD.data_ultimo_uso < NEW.data_ultimo_uso)
    EXECUTE FUNCTION trg_busca_favorita_atualizar_uso();

-- Trigger para atualizar data_atualizacao da tabela busca_indice
CREATE OR REPLACE FUNCTION trg_busca_indice_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_busca_indice_atualizar_timestamp
    BEFORE UPDATE ON busca_indice
    FOR EACH ROW
    EXECUTE FUNCTION trg_busca_indice_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela interface_config
CREATE OR REPLACE FUNCTION trg_interface_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_interface_config_atualizar_timestamp
    BEFORE UPDATE ON interface_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_interface_config_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela agendamento_tarefa
CREATE OR REPLACE FUNCTION trg_agendamento_tarefa_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_agendamento_tarefa_atualizar_timestamp
    BEFORE UPDATE ON agendamento_tarefa
    FOR EACH ROW
    EXECUTE FUNCTION trg_agendamento_tarefa_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela email_config
CREATE OR REPLACE FUNCTION trg_email_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_email_config_atualizar_timestamp
    BEFORE UPDATE ON email_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_email_config_atualizar_timestamp();

-- Trigger para atualizar data_atualizacao da tabela sistema_config
CREATE OR REPLACE FUNCTION trg_sistema_config_atualizar_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_sistema_config_atualizar_timestamp
    BEFORE UPDATE ON sistema_config
    FOR EACH ROW
    EXECUTE FUNCTION trg_sistema_config_atualizar_timestamp();

-- =====================================================
-- INSERÇÕES INICIAIS - DADOS DE CONFIGURAÇÃO
-- =====================================================

-- Inserir configurações de cache padrão
INSERT INTO cache_config (chave, valor, tipo, descricao, modulo, tempo_vida_minutos) VALUES
('dashboard.kpi.ttl', '10', 'NUMBER', 'Tempo de vida dos KPIs do dashboard em minutos', 'DASHBOARD', 10),
('produtos.cache.size', '1000', 'NUMBER', 'Tamanho máximo do cache de produtos', 'PRODUTOS', 30),
('vendas.cache.enabled', 'true', 'BOOLEAN', 'Habilitar cache de vendas recentes', 'VENDAS', 5),
('relatorios.cache.ttl', '15', 'NUMBER', 'Tempo de vida do cache de relatórios', 'RELATORIOS', 15)
ON CONFLICT (chave) DO NOTHING;

-- Inserir traduções adicionais para português brasileiro
INSERT INTO traducao (idioma, chave, valor, modulo, contexto) VALUES
('pt_BR', 'dashboard.title', 'Painel de Controle', 'DASHBOARD', 'Título principal do dashboard'),
('pt_BR', 'menu.vendas', 'Vendas', 'MENU', 'Item de menu vendas'),
('pt_BR', 'menu.estoque', 'Estoque', 'MENU', 'Item de menu estoque'),
('pt_BR', 'menu.financeiro', 'Financeiro', 'MENU', 'Item de menu financeiro'),
('pt_BR', 'menu.relatorios', 'Relatórios', 'MENU', 'Item de menu relatórios'),
('pt_BR', 'menu.configuracoes', 'Configurações', 'MENU', 'Item de menu configurações'),
('pt_BR', 'button.salvar', 'Salvar', 'GERAL', 'Botão de salvar'),
('pt_BR', 'button.cancelar', 'Cancelar', 'GERAL', 'Botão de cancelar'),
('pt_BR', 'button.editar', 'Editar', 'GERAL', 'Botão de editar'),
('pt_BR', 'button.excluir', 'Excluir', 'GERAL', 'Botão de excluir'),
('pt_BR', 'message.sucesso', 'Operação realizada com sucesso!', 'GERAL', 'Mensagem de sucesso'),
('pt_BR', 'message.erro', 'Ocorreu um erro na operação.', 'GERAL', 'Mensagem de erro'),
('pt_BR', 'validation.campo_obrigatorio', 'Campo obrigatório', 'GERAL', 'Validação de campo obrigatório')
ON CONFLICT (idioma, chave) DO NOTHING;

-- Inserir traduções para inglês
INSERT INTO traducao (idioma, chave, valor, modulo, contexto) VALUES
('en_US', 'dashboard.title', 'Dashboard', 'DASHBOARD', 'Main dashboard title'),
('en_US', 'menu.vendas', 'Sales', 'MENU', 'Sales menu item'),
('en_US', 'menu.estoque', 'Inventory', 'MENU', 'Inventory menu item'),
('en_US', 'menu.financeiro', 'Financial', 'MENU', 'Financial menu item'),
('en_US', 'menu.relatorios', 'Reports', 'MENU', 'Reports menu item'),
('en_US', 'menu.configuracoes', 'Settings', 'MENU', 'Settings menu item'),
('en_US', 'button.salvar', 'Save', 'GERAL', 'Save button'),
('en_US', 'button.cancelar', 'Cancel', 'GERAL', 'Cancel button'),
('en_US', 'button.editar', 'Edit', 'GERAL', 'Edit button'),
('en_US', 'button.excluir', 'Delete', 'GERAL', 'Delete button'),
('en_US', 'message.sucesso', 'Operation completed successfully!', 'GERAL', 'Success message'),
('en_US', 'message.erro', 'An error occurred during the operation.', 'GERAL', 'Error message'),
('en_US', 'validation.campo_obrigatorio', 'Required field', 'GERAL', 'Required field validation')
ON CONFLICT (idioma, chave) DO NOTHING;

-- Inserir traduções para espanhol
INSERT INTO traducao (idioma, chave, valor, modulo, contexto) VALUES
('es_ES', 'dashboard.title', 'Panel de Control', 'DASHBOARD', 'Título principal del dashboard'),
('es_ES', 'menu.vendas', 'Ventas', 'MENU', 'Item de menú ventas'),
('es_ES', 'menu.estoque', 'Inventario', 'MENU', 'Item de menú inventario'),
('es_ES', 'menu.financeiro', 'Financiero', 'MENU', 'Item de menú financiero'),
('es_ES', 'menu.relatorios', 'Informes', 'MENU', 'Item de menú informes'),
('es_ES', 'menu.configuracoes', 'Configuraciones', 'MENU', 'Item de menú configuraciones'),
('es_ES', 'button.salvar', 'Guardar', 'GERAL', 'Botón de guardar'),
('es_ES', 'button.cancelar', 'Cancelar', 'GERAL', 'Botón de cancelar'),
('es_ES', 'button.editar', 'Editar', 'GERAL', 'Botón de editar'),
('es_ES', 'button.excluir', 'Eliminar', 'GERAL', 'Botón de eliminar'),
('es_ES', 'message.sucesso', '¡Operación realizada con éxito!', 'GERAL', 'Mensaje de éxito'),
('es_ES', 'message.erro', 'Ocurrió un error en la operación.', 'GERAL', 'Mensaje de error'),
('es_ES', 'validation.campo_obrigatorio', 'Campo obligatorio', 'GERAL', 'Validación de campo obligatorio')
ON CONFLICT (idioma, chave) DO NOTHING;

-- Inserir agendamentos de tarefas padrão
INSERT INTO agendamento_tarefa (nome, descricao, tipo_tarefa, expressao_cron, proxima_execucao, configuracao) VALUES
('Limpeza de Cache', 'Limpar cache expirado automaticamente', 'LIMPEZA_CACHE', '0 */5 * * *', 
 CURRENT_TIMESTAMP + INTERVAL '5 minutes', '{"tipos": ["expirado", "invalido"]}'),
('Backup Diário', 'Backup automático diário do sistema', 'BACKUP', '0 2 * * *', 
 CURRENT_TIMESTAMP + INTERVAL '1 day', '{"tipo": "completo", "comprimir": true}'),
('Relatório de Vendas', 'Gerar relatório diário de vendas', 'RELATORIO', '0 1 * * *', 
 CURRENT_TIMESTAMP + INTERVAL '1 day', '{"tipo": "vendas_diarias", "formato": "PDF", "email": true}'),
('Atualizar Índices', 'Recriar índices de busca', 'ATUALIZAR_INDICES', '0 3 * * 0', 
 CURRENT_TIMESTAMP + INTERVAL '1 week', '{"tipos": ["produto", "cliente", "venda"]}')
ON CONFLICT DO NOTHING;

-- Inserir configurações de sistema padrão
INSERT INTO sistema_config (chave, valor, tipo, categoria, descricao, editavel, valor_padrao) VALUES
('sistema.nome', 'Hermes Comercial PDV', 'STRING', 'GERAL', 'Nome do sistema', true, 'Hermes Comercial PDV'),
('sistema.versao', '2.8.2', 'STRING', 'GERAL', 'Versão atual do sistema', false, '2.8.2'),
('sistema.timezone', 'America/Sao_Paulo', 'STRING', 'GERAL', 'Fuso horário padrão', true, 'America/Sao_Paulo'),
('sistema.idioma_padrao', 'pt_BR', 'STRING', 'GERAL', 'Idioma padrão do sistema', true, 'pt_BR'),
('sistema.formato_data', 'dd/MM/yyyy', 'STRING', 'GERAL', 'Formato de data padrão', true, 'dd/MM/yyyy'),
('sistema.formato_hora', 'HH:mm:ss', 'STRING', 'GERAL', 'Formato de hora padrão', true, 'HH:mm:ss'),
('sistema.limite_registros_pagina', '50', 'NUMBER', 'PERFORMANCE', 'Limite de registros por página', true, '50'),
('sistema.timeout_sessao', '30', 'NUMBER', 'SEGURANCA', 'Timeout da sessão em minutos', true, '30'),
('sistema.tentativas_login', '3', 'NUMBER', 'SEGURANCA', 'Número máximo de tentativas de login', true, '3'),
('sistema.bloqueio_tempo', '15', 'NUMBER', 'SEGURANCA', 'Tempo de bloqueio em minutos após falhas', true, '15'),
('sistema.backup_auto', 'true', 'BOOLEAN', 'BACKUP', 'Habilitar backup automático', true, 'true'),
('sistema.backup_intervalo', '24', 'NUMBER', 'BACKUP', 'Intervalo de backup em horas', true, '24'),
('sistema.log_level', 'INFO', 'STRING', 'LOG', 'Nível de log padrão', true, 'INFO'),
('sistema.performance_monitoring', 'true', 'BOOLEAN', 'PERFORMANCE', 'Habilitar monitoramento de performance', true, 'true')
ON CONFLICT (chave) DO NOTHING;

-- Inserir configuração de e-mail de teste
INSERT INTO email_config (nome_config, host_smtp, porta_smtp, usuario_email, email_remetente, nome_remetente) VALUES
('Email Teste', 'smtp.gmail.com', 587, 'test@hermes.com', 'noreply@hermes.com', 'Hermes Comercial')
ON CONFLICT (nome_config) DO NOTHING;
